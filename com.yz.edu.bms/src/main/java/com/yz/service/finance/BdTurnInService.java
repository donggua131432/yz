package com.yz.service.finance;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.finance.BdTurnInMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.baseinfo.BdCourseMap;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.BdTurnIn;
import com.yz.model.finance.BdTurnInQuery;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdTurnInService {

	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@Autowired
	private BdTurnInMapper turnMapper;

	public Object selectTurnInfoByPage(int start, int length, BdTurnInQuery query) {
		if (StringUtil.hasValue(query.getUnvsId()) && StringUtil.hasValue(query.getGrade())
				&& StringUtil.hasValue(query.getSchoolYear())) {
			PageHelper.offsetPage(start, length);
			List<BdTurnIn> list = turnMapper.selectTurnInfo(query);
			for (BdTurnIn t : list) {
				if(!StringUtil.hasValue(t.getSchoolYear())){
					t.setSchoolYear(query.getSchoolYear());
				}
			}
			return new IPageInfo<BdTurnIn>((Page<BdTurnIn>) list);
		}
		return new IPageInfo<Map<String, String>>();
	}

	public void exportTurnInfo(BdTurnInQuery query, HttpServletResponse response) {
		if (!StringUtil.hasValue(query.getUnvsId()) || !StringUtil.hasValue(query.getGrade())
				|| !StringUtil.hasValue(query.getSchoolYear())) {
			throw new BusinessException("E000094"); // 数据量过大，请选择筛选条件
		}

		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdTurnIn> testExcelCofing = new ExcelUtil.IExcelConfig<BdTurnIn>();
		testExcelCofing.setSheetName("index").setType(BdTurnIn.class)
				.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
				.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
				.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard")).addTitle(new ExcelUtil.IExcelTitle("性别", "sex"))
				.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
				.addTitle(new ExcelUtil.IExcelTitle("院校", "unvsName"))
				.addTitle(new ExcelUtil.IExcelTitle("层次", "pfsnLevel"))
				.addTitle(new ExcelUtil.IExcelTitle("专业", "pfsnName"))
				.addTitle(new ExcelUtil.IExcelTitle("优惠类型", "scholarship"))
				.addTitle(new ExcelUtil.IExcelTitle("入围状态", "inclusionStatus"))
				.addTitle(new ExcelUtil.IExcelTitle("学年", "schoolYear"))
				.addTitle(new ExcelUtil.IExcelTitle("上缴金额", "amount"))
				.addTitle(new ExcelUtil.IExcelTitle("上缴日期", "turnDate"))
				.addTitle(new ExcelUtil.IExcelTitle("返款金额", "returnAmount"))
				.addTitle(new ExcelUtil.IExcelTitle("返款日期", "backDate"))
				.addTitle(new ExcelUtil.IExcelTitle("发票号码", "billNo"))
				.addTitle(new ExcelUtil.IExcelTitle("发票接收老师", "empName"));

		List<BdTurnIn> list = turnMapper.selectTurnInfo(query);
		for (BdTurnIn turn : list) {

			turn.setSex(dictExchangeUtil.getParamKey("sex", turn.getSex()));
			turn.setPfsnLevel(dictExchangeUtil.getParamKey("pfsnLevel", turn.getPfsnLevel()));
			turn.setScholarship(dictExchangeUtil.getParamKey("scholarship", turn.getScholarship()));
			turn.setInclusionStatus(dictExchangeUtil.getParamKey("inclusionStatus", turn.getInclusionStatus()));

		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);
		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=TurnInfo.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Object selectTurnInfo(String learnId,String schoolYear) {
		return turnMapper.selectStdInfo(learnId,schoolYear);
	}

	public void editTurn(BdTurnIn turn) {
		String turnId = turnMapper.selectTurnId(turn.getLearnId(), turn.getSchoolYear());

		if (StringUtil.hasValue(turnId)) {
			turn.setTurnId(turnId);
			turnMapper.updateByPrimaryKeySelective(turn);
		} else {
			turn.setTurnId(IDGenerator.generatorId());
			turnMapper.insertSelective(turn);
		}
	}

}
