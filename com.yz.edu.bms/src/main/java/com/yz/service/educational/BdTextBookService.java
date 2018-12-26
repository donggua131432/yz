package com.yz.service.educational;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yz.constants.GlobalConstants;
import com.yz.core.util.DictExchangeUtil;
import com.yz.dao.educational.BdPublisherMapper;
import com.yz.dao.educational.BdTextBookMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.educational.BdPublisher;
import com.yz.model.educational.BdTextBook;
import com.yz.util.ExcelUtil;
import com.yz.util.StringUtil;
import com.yz.util.ExcelUtil.IExcelConfig;

@Service
@Transactional
public class BdTextBookService {
	
	private static final Logger log = LoggerFactory.getLogger(BdTextBookService.class);
	@Autowired
	private BdTextBookMapper textBookMapper;
	@Autowired
	private BdPublisherMapper publisherMapper;
	
	@Autowired
	private DictExchangeUtil dictExchangeUtil;

	public List<BdTextBook> findAllTextBook(BdTextBook textBook) {
		// TODO Auto-generated method stub
		List<BdTextBook> textBookList = textBookMapper.findAllTextBook(textBook);
		if (null != textBookList)
			for (BdTextBook bdTextBook : textBookList) {
				List<Map<String, Object>> professionalMap = textBookMapper.findProfessional(bdTextBook.getTextbookId());
				bdTextBook.setProfessional(professionalMap);
			}
		return textBookList;
	}

	public BdTextBook findTextBookById(String textbookId) {
		// TODO Auto-generated method stub
		return textBookMapper.selectByPrimaryKey(textbookId);
	}

	public void insertTextBook(BdTextBook textBook) {
		// TODO Auto-generated method stub
		// 是否存在此出版社
		if (StringUtil.hasValue(textBook.getPublisher())) {

			boolean isPublisher = publisherMapper.isPublisher(textBook.getPublisher());
			if (!isPublisher) {
				BdPublisher publisher = new BdPublisher();
				publisher.setPressName(textBook.getPublisher());
				publisher.setCreateTime(textBook.getPublisherTime());
				publisher.setPressId(IDGenerator.generatorId());
				publisherMapper.insertSelective(publisher);
			}
		}
		textBookMapper.insertSelective(textBook);
	}

	private int publisherCount(String code) {
		// TODO Auto-generated method stub
		return textBookMapper.publisherCount(code);
	}

	public void updateTextBook(BdTextBook textBook) {
		// TODO Auto-generated method stub
		textBookMapper.updateByPrimaryKeySelective(textBook);
	}

	public List<BdPublisher> findPublisherByName(String sName) {
		// TODO Auto-generated method stub
		return publisherMapper.findPublisherByName(sName);
	}

	public void deletePublisher(String[] idArray) {
		// TODO Auto-generated method stub
		textBookMapper.deletePublisher(idArray);
		textBookMapper.deleteCourseTextbook(idArray);
	}

	public List<BdTextBook> findTextBookByName(String sName) {
		// TODO Auto-generated method stub
		return textBookMapper.findTextBookByName(sName);
	}

	public List<BdTextBook> findTextBookByThpId(String thpId) {
		// TODO Auto-generated method stub
		return textBookMapper.findTextBookByThpId(thpId);
	}

	public void importTextBook(IExcelConfig<BdTextBook> testExcelCofing, MultipartFile excelTestArea) {
		// 行数记录
		int index = 1;
		try {
			// 对文件进行分析转对象
			List<BdTextBook> list = ExcelUtil.importWorkbook(excelTestArea.getInputStream(), testExcelCofing,
					excelTestArea.getOriginalFilename());
			// 遍历插入
			for (BdTextBook textBook : list) {
				// 判断必填项是否为空
				if (!(StringUtil.hasValue(textBook.getTextbookName()) && StringUtil.hasValue(textBook.getTextbookType()))) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				}
				// 教材类型转换
				String valueTemple = dictExchangeUtil.getParamValue("textbookType", textBook.getTextbookType());
				if (null == valueTemple) {
					throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
				} else {
					textBook.setTextbookType(valueTemple);
				}

				textBook.setIsBook("是".equals(textBook.getIsBook()) ? "1" : "0");
				insertTextBook(textBook);
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException("excel数据格式错误，请检查第" + index + "行数据！");
		}

	}
	/**
	 * 教材导出
	 * @param textBook
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void exportTextBook(BdTextBook textBook, HttpServletResponse response){
		// 对导出工具进行字段填充
		ExcelUtil.IExcelConfig<BdTextBook> testExcelCofing = new ExcelUtil.IExcelConfig<BdTextBook>();
		testExcelCofing.setSheetName("index").setType(BdTextBook.class)
				.addTitle(new ExcelUtil.IExcelTitle("教材编码", "textbookId"))
				.addTitle(new ExcelUtil.IExcelTitle("教材名称", "textbookName"))
				.addTitle(new ExcelUtil.IExcelTitle("教材类型", "textbookType"))
				.addTitle(new ExcelUtil.IExcelTitle("代号标记", "alias"))
				.addTitle(new ExcelUtil.IExcelTitle("是否为书籍", "isBook"))
				.addTitle(new ExcelUtil.IExcelTitle("出版社", "publisher"))
				.addTitle(new ExcelUtil.IExcelTitle("出版时间", "publisherTime"))
				.addTitle(new ExcelUtil.IExcelTitle("ISBN", "isbn"))
				.addTitle(new ExcelUtil.IExcelTitle("作者", "author"))
				.addTitle(new ExcelUtil.IExcelTitle("价格", "price"))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));
		
		List<BdTextBook> textBookList = textBookMapper.findAllTextBook(textBook);
		if(null != textBookList && textBookList.size() >0){
			for(BdTextBook tBook : textBookList){
				String bookType = dictExchangeUtil.getParamKey("textbookType", tBook.getTextbookType());
				tBook.setTextbookType(bookType);//书籍类型
				if (GlobalConstants.TRUE.equals(tBook.getIsBook())) {
					tBook.setIsBook("是");
				} else {
					tBook.setIsBook("否");
				}
			}
		}
		
		SXSSFWorkbook wb = ExcelUtil.exportWorkbook(textBookList, testExcelCofing);

		ServletOutputStream out = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=TextBook.xls");
			out = response.getOutputStream();
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
