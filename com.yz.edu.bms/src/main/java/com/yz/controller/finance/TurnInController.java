package com.yz.controller.finance;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yz.core.security.annotation.Rule;
import com.yz.dao.finance.BdTurnInMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.finance.BdTurnIn;
import com.yz.model.finance.BdTurnInQuery;
import com.yz.service.finance.BdTurnInService;
import com.yz.util.ExcelUtil;

/**
 * 
 * Description: 学费上缴管理
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2018年3月15日.
 *
 */
@Controller
@RequestMapping("/turnIn")
public class TurnInController {

	@Autowired
	private BdTurnInService turnService;

	@Autowired
	private BdTurnInMapper turnMapper;

	@RequestMapping("/toList")
	@Rule("turnIn:query")
	public Object toList() {
		return "finance/turnin/turnin-list";
	}

	@RequestMapping("/toImport")
	@Rule("turnIn:insert")
	public Object toImport() {
		return "finance/turnin/turnin-import";
	}

	@RequestMapping("/toEdit")
	@Rule("turnIn:insert")
	public Object toEdit(Model model, @RequestParam(name = "learnId", required = true) String learnId,
			@RequestParam(name = "schoolYear", required = true) String schoolYear) {
		model.addAttribute("learnId", learnId);
		model.addAttribute("schoolYear", schoolYear);
		model.addAttribute("turnInfo", turnService.selectTurnInfo(learnId, schoolYear));
		return "finance/turnin/turnin-edit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	@Rule("turnIn:query")
	public Object edit(BdTurnIn turn) {
		turnService.editTurn(turn);
		return "SUCCESS";
	}

	@RequestMapping("/list")
	@ResponseBody
	@Rule("turnIn:query")
	public Object list(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length, BdTurnInQuery query) {

		return turnService.selectTurnInfoByPage(start, length, query);
	}

	@RequestMapping("/toExcelImport")
	@Rule("turnIn:insert")
	public String toExcelImport(HttpServletRequest request) {
		return "finance/turnin/turnin-import";
	}

	@RequestMapping("/excelImport")
	@ResponseBody
	@Rule("turnIn:insert")
	public Object excelImport(@RequestParam(name = "turnExcel", required = false) MultipartFile turnExcel) {
		if (null != turnExcel) {
			ExcelUtil.IExcelConfig<BdTurnIn> testExcelCofing = new ExcelUtil.IExcelConfig<BdTurnIn>();
			testExcelCofing.setSheetName("index").setType(BdTurnIn.class)
					.addTitle(new ExcelUtil.IExcelTitle("学号", "schoolRoll"))
					.addTitle(new ExcelUtil.IExcelTitle("姓名", "stdName"))
					.addTitle(new ExcelUtil.IExcelTitle("身份证号", "idCard"))
					.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"))
					.addTitle(new ExcelUtil.IExcelTitle("学年", "schoolYear"))
					.addTitle(new ExcelUtil.IExcelTitle("上缴金额", "amount"))
					.addTitle(new ExcelUtil.IExcelTitle("上缴日期", "turnDate"))
					.addTitle(new ExcelUtil.IExcelTitle("返款金额", "returnAmount"))
					.addTitle(new ExcelUtil.IExcelTitle("返款日期", "backDate"))
					.addTitle(new ExcelUtil.IExcelTitle("发票号码", "billNo"))
					.addTitle(new ExcelUtil.IExcelTitle("发票接收老师", "empName"));

			// 行数记录
			int index = 1;
			try {
				// 对文件进行分析转对象
				List<BdTurnIn> list = ExcelUtil.importWorkbook(turnExcel.getInputStream(), testExcelCofing,
						turnExcel.getOriginalFilename());

				// 遍历插入
				for (BdTurnIn turn : list) {

					List<String> learnIds = turnMapper.selectLearnId(turn.getIdCard(), turn.getGrade());

					if (learnIds.isEmpty()) {
						throw new IllegalArgumentException(
								"excel数据第" + index + "行，学员" + turn.getStdName() + "学业记录查询为空,或学员状态不在范围！");
					}
					if (learnIds.size() > 1) {
						throw new IllegalArgumentException(
								"excel数据第" + index + "行，学员" + turn.getStdName() + "拥有多条报读学业");
					}

					String learnId = learnIds.get(0);

					// 查看是否重复导入
					int count = turnMapper.selectTurnInCount(learnId, turn.getSchoolYear());

					if (count > 0) {
						String bdTurnIn = turnMapper.selectTurnId(learnId, turn.getSchoolYear());
						turn.setLearnId(learnId);
						turn.setTurnId(bdTurnIn);
						turnMapper.updateByPrimaryKeySelective(turn);
					} else {
						turn.setLearnId(learnId);
						turn.setTurnId(IDGenerator.generatorId());
						// 插入上缴记录
						turnMapper.insertSelective(turn);
					}
					index++;
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
			}
		}
		return "SUCCESS";
	}

	@RequestMapping("/export")
	@Rule("turnIn:insert")
	public void export(BdTurnInQuery query, HttpServletResponse response) {

		turnService.exportTurnInfo(query, response);
	}

}
