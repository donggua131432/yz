package com.yz.service.educational;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yz.model.educational.*;
import com.yz.util.ExcelUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.educational.BdExamClassMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.util.StringUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
public class BdExamClassService {

	private static final Logger log = LoggerFactory.getLogger(BdExamClassService.class);

	@Autowired
	private BdExamClassMapper classMapper;

	public Object selectExamClassByPage(int start, int length, BdExamClassQuery query) {
		PageHelper.offsetPage(start, length);
		List<Map<String, String>> list = classMapper.selectAllExamClass(query);
		return new IPageInfo<Map<String, String>>((Page<Map<String, String>>) list);
	}

	public BdExamClass selectExamClass(String pyId) {
		return classMapper.selectExamClassByPyId(pyId);
	}

	/**
	 * 考场安排划分课室
	 * 
	 * @param examClass
	 */
	public void divideExamClass(BdExamClass examClass) {
		BdExamClass p = classMapper.selectExamClassByPyId(examClass.getPyId());
		if (null != p.getPlaces() && p.getPlaces().size() > 0) {
			classMapper.deleteExamClassByPyId(examClass.getPyId());
		}

		int seats = Integer.parseInt(p.getSeats());
		List<BdPlaceInfo> ps = new ArrayList<BdPlaceInfo>();

		int count = 0;

		for (int i = 0; i < examClass.getPlaces().size(); i++) {
			BdPlaceInfo c = examClass.getPlaces().get(i);
			if (StringUtil.hasValue(c.getMaxCount()) && StringUtil.hasValue(c.getPlaceName())) {

				count += Integer.parseInt(c.getMaxCount());
				c.setOrderNum(i + "");
				ps.add(c);
			}
		}

		if (count > seats || count <= 0) {
			throw new BusinessException("E000111"); // 课室容量不能为0或大于考场容量
		}

		classMapper.updateDivideRemark(examClass.getPyId(), examClass.getDivideRemark());

		for (BdPlaceInfo bdPlaceInfo : examClass.getPlaces()) {
			bdPlaceInfo.setPlaceId(IDGenerator.generatorId());
		}
		classMapper.insertExamClass(examClass);

		// TODO 检查是否已安排座位，否则无法删除

	}

	public Object selectEpName(String eyId, String cityCode, String districtCode) {
		return classMapper.selectEpName(eyId, cityCode, districtCode);
	}

	public void classExport(String eyId, String cityCode, String districtCode, String epId,
			HttpServletResponse response) {
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdExamClassExcel> testExcelCofing = new ExcelUtil.IExcelConfig<BdExamClassExcel>();
		testExcelCofing.setSheetName("index").setType(BdExamClassExcel.class)
				.addTitle(new ExcelUtil.IExcelTitle("年度", "examYear"))
				.addTitle(new ExcelUtil.IExcelTitle("城市", "cityName"))
				.addTitle(new ExcelUtil.IExcelTitle("区域", "districtName"))
				.addTitle(new ExcelUtil.IExcelTitle("考场", "epName"))
				.addTitle(new ExcelUtil.IExcelTitle("课室", "placeName"))
				.addTitle(new ExcelUtil.IExcelTitle("考试时间", "examTime"))
				.addTitle(new ExcelUtil.IExcelTitle("最大容量", "maxCount"))
				.addTitle(new ExcelUtil.IExcelTitle("已选座位", "seats"))
				.addTitle(new ExcelUtil.IExcelTitle("是否可用", "isUse"))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remeak"));

		List<BdExamClassExcel> list = classMapper.getExamClass(eyId, cityCode, districtCode, epId);
		Map<String, String> map = new HashMap<String, String>();
		for (BdExamClassExcel bdExamClassExcel : list) {

			bdExamClassExcel.setExamTime(bdExamClassExcel.getStartTime().replace("AM", "上午").replace("PM", "下午") + "-"
					+ bdExamClassExcel.getEndTime());

			if (bdExamClassExcel.getIsMulti().equals("1")) {
				String pyId = bdExamClassExcel.getPyId();
				String seats = bdExamClassExcel.getSeats();
				if (!map.containsKey(pyId)) {
					map.put(pyId, seats);
				}
				bdExamClassExcel.setSeats("0");
			}
		}

		for (Map.Entry<String, String> entry : map.entrySet()) {
			for (BdExamClassExcel bdExamClassExcel : list) {
				if (entry.getKey().equals(bdExamClassExcel.getPyId())) {
					int rest = Integer.parseInt(entry.getValue()) - Integer.parseInt(bdExamClassExcel.getMaxCount());
					if (rest >= 0) {
						bdExamClassExcel.setSeats(bdExamClassExcel.getMaxCount());
					} else {
						if (Integer.parseInt(entry.getValue()) >= 0) {
							bdExamClassExcel.setSeats(entry.getValue());
						}
					}
					map.put(bdExamClassExcel.getPyId(), String.valueOf(rest));
				}
			}
		}

		for (BdExamClassExcel bdExamClassExcel : list) {
			if (Integer.parseInt(bdExamClassExcel.getSeats()) == Integer.parseInt(bdExamClassExcel.getMaxCount())) {
				bdExamClassExcel.setIsUse("不可用");
			}
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=examClass.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
}
