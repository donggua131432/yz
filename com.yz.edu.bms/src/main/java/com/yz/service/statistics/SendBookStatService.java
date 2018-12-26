package com.yz.service.statistics;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.util.DictExchangeUtil;
import com.yz.core.util.StudentStatUtil;
import com.yz.dao.statistics.SendBookStatMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.statistics.SendBookStatInfo;
import com.yz.model.statistics.SendBookStatQuery;
import com.yz.util.ExcelUtil;

@Service
@Transactional
public class SendBookStatService
{
	private static Logger log = LoggerFactory.getLogger(SendBookStatService.class);
	@Autowired
	private SendBookStatMapper bookStatMapper;
	
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo querySendBookStatByPage(int start, int length,SendBookStatQuery statQuery)
	{
		PageHelper.offsetPage(start, length);
		List<SendBookStatInfo> courseS = bookStatMapper.getSendBookStatByPage(statQuery);
		return new IPageInfo((Page) courseS);
	}
	
	@SuppressWarnings("unchecked")
	public void exportBookStat(SendBookStatQuery statQuery,HttpServletResponse response){
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<SendBookStatInfo> testExcelCofing = new ExcelUtil.IExcelConfig<SendBookStatInfo>();
		testExcelCofing.setSheetName("index").setType(SendBookStatInfo.class);
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bu.unvs_id")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("院校名称", "unvsName"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bli.pfsn_id")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("专业名称", "pfsnName"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bup.pfsn_level")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("报考层次", "pfsnLevel"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bli.grade")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("年级", "grade"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bss.semester")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("学期", "semester"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bup.year")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("年度", "year"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bsi.std_id")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("学员", "stdName"));
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("身份证", "idCard"));
		}
		if(StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bli.std_stage")){
			testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("学员状态", "stdStage"));
		}
		testExcelCofing.addTitle(new ExcelUtil.IExcelTitle("教材编码", "alias"))
				.addTitle(new ExcelUtil.IExcelTitle("教材名称", "textbookName"))
				.addTitle(new ExcelUtil.IExcelTitle("单价", "price"))
				.addTitle(new ExcelUtil.IExcelTitle("数量", "orderNum"))
				.addTitle(new ExcelUtil.IExcelTitle("总价", "totalPrice"));

		List<SendBookStatInfo> list = bookStatMapper.getSendBookStatByPage(statQuery);

		if (StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bss.semester") && StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bli.std_stage")) {
			for (SendBookStatInfo book : list) {

				String semester = dictExchangeUtil.getParamKey("semester", book.getSemester());
				book.setSemester(semester);
				
				String stdStage =dictExchangeUtil.getParamKey("stdStage", book.getStdStage()); 
				book.setStdStage(stdStage);
			}
		}else{
			if (StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bss.semester")) {
				for (SendBookStatInfo book : list) {
					String semester = dictExchangeUtil.getParamKey("semester", book.getSemester());
					book.setSemester(semester);
				}
			}
			if (StudentStatUtil.isFieldString(statQuery.getStatGroup(), "bli.std_stage")) {
				for (SendBookStatInfo book : list) {
					String stdStage =dictExchangeUtil.getParamKey("stdStage", book.getStdStage()); 
					book.setStdStage(stdStage);
				}
			}	
		}

		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(list, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=bookStat.xls");
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
	
	public void okOrder(String statGroup){
		bookStatMapper.okOrder(statGroup);
	}
}
