package com.yz.controller.educational;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.annotation.Token;
import com.yz.core.security.annotation.Token.Flag;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdStudentEScoreReceive;
import com.yz.model.educational.BdStudentScoreMap;
import com.yz.service.educational.BdStudentScoreService;
import com.yz.util.Assert;

@RequestMapping("/studentScore")
@Controller
public class BdStudentScoreController {

	@Autowired
	private BdStudentScoreService studentScoreService;

	@RequestMapping("/list")
	public String showList(HttpServletRequest request) {
		return "/educational/studentScore/studentScore-list";
	}

	/**
	 * Description: 根据条件查询所有符合数据
	 * 
	 * @param studentScoreMap
	 *            BdStudentScoreMap对象字段
	 * @return 返回PageInfo对象json
	 * @return 返回pageSize对象每页显示长度
	 * @return 返回page对象页码
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.BdStudentScoreController Note: Nothing
	 *      much.
	 */
	@RequestMapping("/findAllStudentScore")
	@ResponseBody
	@Rule("studentScore:query")
	public Object findAllStudentScore(@RequestParam(value = "start", defaultValue = "1") int start,
			@RequestParam(value = "length", defaultValue = "10") int length, @RequestParam(name = "grade") String grade,
			BdStudentScoreMap studentScoreMap) {
		PageHelper.offsetPage(start, length);
		studentScoreMap.setGrade(grade);
		List<BdStudentScoreMap> resultList = studentScoreService.selectAll(studentScoreMap);
		return new IPageInfo((Page) resultList);
	}

	@RequestMapping("/edit")
	@Rule("studentScore:update")
	@Token(action = Flag.Save, groupId = "studentScore:update")
	public String edit(HttpServletRequest request, Model model, @RequestParam(name = "learnId") String learnId,
			@RequestParam(name = "idCard") String idCard,
			@RequestParam(name = "stdName") String stdName) {
		Assert.hasText(learnId, "参数learnId不能为空");
		List<Map<String, String>> list=(List<Map<String, String>>) studentScoreService.findStudentExamCourse(learnId);
		model.addAttribute("learnId", learnId);
		model.addAttribute("course", list);
		model.addAttribute("idCard",idCard);
		model.addAttribute("stdName",stdName);
		return "/educational/studentScore/studentScore-edit";
	}

	/**
	 * Description: 根据escoreId修改分数
	 * 
	 * @param studentEScore
	 *            List<BdStudentEScore>集合
	 * @throws Exception
	 *             抛出一个异常
	 * @see com.yz.controller.educational.updateStudentScore Note: Nothing much.
	 */
	@RequestMapping("/updateStudentScore")
	@ResponseBody
	@Rule("studentScore:update")
	@Token(action = Flag.Remove, groupId = "studentScore:update")
	public Object updateStudentScore(BdStudentEScoreReceive scores) {
		studentScoreService.insertStudentScore(scores);
		return "SUCCESS";
	}
}
