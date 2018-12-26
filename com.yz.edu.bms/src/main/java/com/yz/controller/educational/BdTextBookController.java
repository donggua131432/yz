package com.yz.controller.educational;

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

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.util.RequestUtil;
import com.yz.dao.baseinfo.BdUnvsProfessionMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdTextBook;
import com.yz.service.educational.BdTextBookService;
import com.yz.util.Assert;
import com.yz.util.ExcelUtil;

@RequestMapping("/textBook")
@Controller
public class BdTextBookController {

	@Autowired
	private BdTextBookService textBookService;
	
	@Autowired
	private BdUnvsProfessionMapper upMapper;

	@RequestMapping("/list")
	@Rule("textBook:find")
	public String showList(HttpServletRequest request) {
		return "/educational/textBook/text-book-list";
	}

	
	@RequestMapping("/excelImport")
	@Rule("textBook:upload")
	public String excelImport(HttpServletRequest request) {
		return "/educational/textBook/text-book-import";
	}
	
	
	@RequestMapping("/upload")
	@ResponseBody
	@Rule("textBook:upload")
	public Object upload(@RequestParam(value = "excelTextBook", required = false) MultipartFile excelTestArea) {
		
		//对导入工具进行字段填充
		ExcelUtil.IExcelConfig<BdTextBook> testExcelCofing = new ExcelUtil.IExcelConfig<BdTextBook>();
		testExcelCofing.setSheetName("index").setType(BdTextBook.class)
				.addTitle(new ExcelUtil.IExcelTitle("教材名称", "textbookName"))
				.addTitle(new ExcelUtil.IExcelTitle("教材类型", "textbookType"))
				.addTitle(new ExcelUtil.IExcelTitle("代号标记", "alias"))
				.addTitle(new ExcelUtil.IExcelTitle("是否书籍", "isBook"))
				.addTitle(new ExcelUtil.IExcelTitle("出版社", "publisher"))
				.addTitle(new ExcelUtil.IExcelTitle("出版时间", "publisherTime"))
				.addTitle(new ExcelUtil.IExcelTitle("ISBN", "isbn"))
				.addTitle(new ExcelUtil.IExcelTitle("作者", "author"))
				.addTitle(new ExcelUtil.IExcelTitle("价格", "price"))
				.addTitle(new ExcelUtil.IExcelTitle("备注", "remark"));

		textBookService.importTextBook(testExcelCofing,excelTestArea);
		
		return "SUCCESS";
	}
	
	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param textBook
	 *            BdTextBook对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentScoreController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findAllTextBook")
	@ResponseBody
	@Rule("textBook:find")
	public Object findAllTextBook(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, BdTextBook textBook) {
		PageHelper.offsetPage(start, length);
		List<BdTextBook> resultList = textBookService.findAllTextBook(textBook);
		return new IPageInfo((Page) resultList);
	}
	
	@RequestMapping("/findTextBookToFD")
	@ResponseBody
	@Rule("textBook:find")
	public Object findTextBookToFD(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		BdTextBook textBook = new BdTextBook();
		textBook.setTextbookType("FD");
		textBook.setTextbookName(sName);
		return new IPageInfo((Page) textBookService.findAllTextBook(textBook));
	}
	

	@RequestMapping("/edit")
	@Rule("textBook:find")
	public String edit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request, Model model) {
		BdTextBook textBook = new BdTextBook();
		
		if ("UPDATE".equals(exType.trim().toUpperCase())) {
			String textbookId = RequestUtil.getString("textbookId");
			Assert.hasText(textbookId, "参数textbookId名称不能为空");
			textBook = textBookService.findTextBookById(textbookId);
		}
		
		model.addAttribute("subjects", upMapper.selectAllSubject());
		model.addAttribute("exType", exType);
		model.addAttribute("textBookInfo", textBook);
		return "/educational/textBook/text-book-edit";
	}

	/*
	 * @Description:新增教材
	 */
	@RequestMapping("/insertTextBook")
	@ResponseBody
	@Rule("textBook:insert")
	public Object insertTextBook(BdTextBook textBook) {
		textBookService.insertTextBook(textBook);
		return "SUCCESS";
	}
	
	/*
	 * @Description:修改教材
	 */
	@RequestMapping("/updateTextBook")
	@ResponseBody
	@Rule("textBook:update")
	public Object updateTextBook(BdTextBook textBook) {
		textBookService.updateTextBook(textBook);
		return "SUCCESS";
	}
	
	@RequestMapping("/findTextBookByName")
	@ResponseBody
	@Rule("textBook:find")
	public Object findTextBookByName(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) textBookService.findTextBookByName(sName));
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/findPublisherByName")
	@ResponseBody
	@Rule("textBook:find")
	public Object findPublisherByName(String sName, @RequestParam(value = "rows", defaultValue = "7") int rows,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		PageHelper.startPage(page, rows);
		return new IPageInfo((Page) textBookService.findPublisherByName(sName));
	}
	
	@RequestMapping("/deletePublisher")
	@ResponseBody
	@Rule("textBook:delete")
	public Object deletePublisher(@RequestParam(name = "idArray[]", required = true) String[] idArray) {
		textBookService.deletePublisher(idArray);
		return "success";
	}
	
	@RequestMapping("/excelExport")
	@Rule("textBook:export")
	public void export(BdTextBook textBook, HttpServletResponse response) {
		textBookService.exportTextBook(textBook, response);
	}
}
