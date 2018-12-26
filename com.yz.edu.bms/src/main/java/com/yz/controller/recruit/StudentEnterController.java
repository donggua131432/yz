package com.yz.controller.recruit;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.util.IdCardVerifyUtil;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.recruit.BdStudentBaseInfo;
import com.yz.model.recruit.BdStudentHistory;
import com.yz.model.recruit.BdStudentOther;
import com.yz.model.recruit.BdStudentRecruit;
import com.yz.service.common.BaseInfoService;
import com.yz.service.recruit.StudentEnterService;
import com.yz.service.recruit.StudentRecruitService;
import com.yz.util.CodeUtil;
import com.yz.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * 针对钉钉-招生应用,录入学员信息
 * @author lx
 * @date 2017年12月4日 下午3:24:31
 */
@Controller
@RequestMapping("/stdenter")
public class StudentEnterController
{
	@Autowired
	private StudentEnterService studentEnterService;
	
	@Autowired
	private StudentRecruitService studentRecruitService;
	
	@Autowired
	private BaseInfoService baseInfoService;
	
	@Log
	@RequestMapping(value="/input",produces={"text/html;charset=UTF-8;","application/json;"})
	@ResponseBody
	public Object stdInput(HttpServletRequest request,HttpServletResponse response) {
		String stdInfo = request.getParameter("stdInfo");
		String empId = request.getParameter("empId");
		JSONObject obj = JSONObject.fromObject(stdInfo);
		BdStudentBaseInfo baseInfo = new BdStudentBaseInfo();
		BdStudentRecruit recruit = new BdStudentRecruit();
		BdStudentHistory history = new BdStudentHistory();
		BdStudentOther other = new BdStudentOther();
		baseInfo.setStdId(obj.getString("stdId"));
		baseInfo.setStdName(CodeUtil.base64Decode2String(obj.getString("stdName")));
		baseInfo.setIdType(obj.getString("idType"));
		baseInfo.setIdCard(obj.getString("idCard"));
		baseInfo.setSex(obj.getString("sex"));
		baseInfo.setBirthday(obj.getString("birthday"));
		baseInfo.setMobile(obj.getString("mobile"));
		baseInfo.setNation(obj.getString("nation"));
		baseInfo.setPoliticalStatus(obj.getString("politicalStatus"));
		baseInfo.setRprType(obj.getString("rprType"));
		baseInfo.setRprProvinceCode(obj.getString("rprProvinceCode"));
		baseInfo.setRprCityCode(obj.getString("rprCityCode"));
		baseInfo.setRprDistrictCode(obj.getString("rprDistrictCode"));
		baseInfo.setNowProvinceCode(obj.getString("nowProvinceCode"));
		baseInfo.setNowCityCode(obj.getString("nowCityCode"));
		baseInfo.setNowDistrictCode(obj.getString("nowDistrictCode"));
		baseInfo.setJobStatus(obj.getString("jobStatus"));
		baseInfo.setAddress(CodeUtil.base64Decode2String(obj.getString("address")));

		history.setUnvsName(CodeUtil.base64Decode2String(obj.getString("unvsName")));
		history.setGraduateTime(obj.getString("graduateTime"));
		history.setProfession(CodeUtil.base64Decode2String(obj.getString("profession")));
		history.setEdcsType(obj.getString("edcsType"));
		history.setDiploma(CodeUtil.base64Decode2String(obj.getString("diploma")));
		history.setOldProvinceCode(obj.getString("oldProvinceCode"));
		history.setOldDistrictCode(obj.getString("oldDistrictCode"));
		history.setOldCityCode(obj.getString("oldCityCode"));
		history.setAdminssionTime(obj.getString("adminssionTime"));
		history.setEdcsSystem(obj.getString("edcsSystem"));
		history.setIsOpenUnvs(obj.getString("isOpenUnvs"));
		history.setStudyType(obj.getString("studyType"));
		history.setMaterialType(obj.getString("materialType"));
		history.setMaterialCode(CodeUtil.base64Decode2String(obj.getString("materialCode")));

		recruit.setScholarship(obj.getString("scholarship"));
		recruit.setPfsnLevel(obj.getString("pfsnLevel"));
		recruit.setEnrollType(obj.getString("enrollType"));
		recruit.setGrade(obj.getString("grade"));
		recruit.setUnvsId(obj.getString("unvsId"));
		recruit.setPfsnId(obj.getString("pfsnId"));
		recruit.setTaId(obj.getString("taId"));
		recruit.setPoints(obj.getString("points"));
		recruit.setBpType(obj.getString("bpType"));
		recruit.setRecruitType(obj.getString("recruitType"));
		recruit.setFeeId(obj.getString("feeId"));
		recruit.setOfferId(obj.getString("offerId"));

		other.setMaritalStatus(obj.getString("maritalStatus"));
		
		recruit.setEmpId(empId);
		baseInfo.setStdSource("D");
		studentRecruitService.recruit(baseInfo, other, recruit, history);
		return null;
	}
	
	/**
	 * 是否存在报读
	 * @return
	 */
	@RequestMapping("/ifExistInfo")
	@ResponseBody
	public Object ifExistInfo(@RequestParam(name = "paramInfo", required = true) String paramInfo,
			@RequestParam(name = "userId", required = true) String userId) {
		
		JSONObject obj = JSONObject.fromObject(paramInfo);
		
		String idType = obj.getString("idType");
		String idCard = obj.getString("idCard");
		String recruitType = obj.getString("recruitType");

		if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idCard)) {
			return null;
		}
		//验证身份证是否合法
		if(idType.equals("1") && !IdCardVerifyUtil.strongVerifyIdNumber(idCard)){
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("errCode", "E00097");
			return result;
		}
		return studentEnterService.ifExistInfo(idType, idCard, null,recruitType,userId);
	}
	/**
	 * 验证是当前学年是否有报读
	 * @return
	 */
	@RequestMapping("/validateBaseInfo")
	@ResponseBody
	public Object validateBaseInfo(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		
		JSONObject obj = JSONObject.fromObject(paramInfo);
		
		String grade = obj.getString("grade");
		String idType = obj.getString("idType");
		String idCard = obj.getString("idCard");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("idType", idType);
		param.put("idCard", idCard);
		param.put("grade", grade);

		return studentRecruitService.isExist(param) == false;
	}
	/**
	 * 验证优惠类型
	 * @return
	 */
	@RequestMapping("/validateRecruit")
	@ResponseBody
	public Object validateRecruit(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		JSONObject obj = JSONObject.fromObject(paramInfo);
		
		String stdId = obj.getString("stdId");
		String scholarship = obj.getString("scholarship");
		
		return studentRecruitService.check(stdId, scholarship);
	}
	/**
	 * 展示收费列表
	 * @return
	 */
	@RequestMapping("/showFeeList")
	@ResponseBody
	public Object showFeeList(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		
		JSONObject obj = JSONObject.fromObject(paramInfo);
		
		String pfsnId = obj.getString("pfsnId");
		String scholarship = obj.getString("scholarship");
		String taId = obj.getString("taId");
		String recruitType = obj.getString("recruitType");
		
		if (StringUtil.isEmpty(pfsnId) || StringUtil.isEmpty(scholarship) || StringUtil.isEmpty(taId)
				|| StringUtil.isEmpty(recruitType)) {
			return null;
		}
		return studentRecruitService.findFeeOfferInfo(pfsnId, scholarship, taId, recruitType);
	}
	
	/**
	 * 院校
	 * @return
	 */
	@RequestMapping("/sUnvs")
	@ResponseBody
	public Object getUnvsList(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		SelectQueryInfo queryInfo = new SelectQueryInfo();
		JSONObject obj = JSONObject.fromObject(paramInfo);
		queryInfo.setExt1(obj.getString("ext1"));
		queryInfo.setsName(obj.getString("sName"));
		queryInfo.setPage(obj.getInt("page"));
		queryInfo.setRows(obj.getInt("rows"));
		
		return baseInfoService.getUnvsSelectList(queryInfo);
	}

	/**
	 * 专业
	 * @return
	 */
	@RequestMapping("/sPfsn")
	@ResponseBody
	public Object getPfsnList(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		SelectQueryInfo queryInfo = new SelectQueryInfo();
		JSONObject obj = JSONObject.fromObject(paramInfo);
		
		queryInfo.setExt1(obj.getString("ext1"));
		queryInfo.setExt2(obj.getString("ext2"));
		queryInfo.setsId(obj.getString("sId"));
		queryInfo.setsName(obj.getString("sName"));
		queryInfo.setPage(obj.getInt("page"));
		queryInfo.setRows(obj.getInt("rows"));
		
		return baseInfoService.getPfsnSelectList(queryInfo);
	}
	
	/**
	 * 考区
	 * @return
	 */
	@RequestMapping("/sTa")
	@ResponseBody
	public Object getTaList(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		SelectQueryInfo queryInfo = new SelectQueryInfo();
		JSONObject obj = JSONObject.fromObject(paramInfo);
		queryInfo.setsId(obj.getString("sId"));
		queryInfo.setsName(obj.getString("sName"));
		queryInfo.setPage(obj.getInt("page"));
		queryInfo.setRows(obj.getInt("rows"));
		return baseInfoService.getTaSelectList(queryInfo);
	}
	/**
	 * 优惠类型
	 * @param paramInfo
	 * @return
	 */
	@RequestMapping("/getScholarships")
	@ResponseBody
	public Object getScholarships(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
        JSONObject obj = JSONObject.fromObject(paramInfo);
		String pfsnId = obj.getString("pfsnId");
		String taId = obj.getString("taId");
		if (StringUtil.isEmpty(pfsnId) || StringUtil.isEmpty(taId)) {
			return null;
		}
		return studentRecruitService.getScholarships(pfsnId, taId);
	}
	
	/**
	 * 验证手机号
	 * @param paramInfo
	 * @return
	 */
	@RequestMapping("/validateMobile")
	@ResponseBody
	public Object validateMobile(@RequestParam(name = "paramInfo", required = true) String paramInfo) {
		JSONObject obj = JSONObject.fromObject(paramInfo);
		String mobile = obj.getString("mobile");
		return studentRecruitService.ifMobileExistInfo(mobile)<1;
	}
	
}
