package com.yz.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yz.service.BdsStudentTScoreService;
import com.yz.util.JsonUtil;

//spring注解使用
@Service("ScoringAPIService")
//webservice地址使用
@WebService(serviceName="ScoringAPI")
//防止jdk版本问题
@SOAPBinding(style=Style.RPC)
public class BdsScoringApiImpl {

	@Autowired
	private BdsStudentTScoreService tscoreService;
	
	
	

	/// <summary>
    /// 分布获取学员基础数据
    /// </summary>
    /// <param name="AuthNo">节点编号</param>
    /// <param name="UserName">访问标识</param>
    /// <param name="Pwd">访问密码</param>
    /// <param name="WhereSQL">SQL的Where条件</param>
    /// <param name="PageSize">每页数量</param>
    /// <param name="PageIndex"></param>
    /// <returns></returns>
	@WebMethod
	public Object GetPreScoringResaulByPage(String AuthNo, String UserName, String Pwd, String WhereSQL, int PageSize,
			int PageIndex) {
		// TODO Auto-generated method stub
		if(AuthNo.equals("YZScore")&&UserName.equals("YZScore")&&Pwd.equals("2017@YZScore")) {
			Object pageresult=tscoreService.GetPreScoringResaulByPage(WhereSQL, PageSize, PageIndex);
			return JsonUtil.object2String(pageresult);
		}
		return null;
		
	}

	/// <summary>
    /// 根据报读ID和课程编码等条件更新学员的基本成绩
    /// </summary>
    /// <param name="AuthNo">授权号</param>
    /// <param name="UserName">授权登录名</param>
    /// <param name="Pwd">授权登录密码</param>
    /// <param name="SID">学员ID</param>
    /// <param name="StudyID">报读ID</param>
    /// <param name="StudyYear">学期</param>
    /// <param name="CCode">课程编码</param>
    /// <param name="score">成绩</param>
    /// <returns></returns>
	@WebMethod
	public Object UploadScoringResult(String AuthNo, String UserName, String Pwd, String SID, String StudyID, String StudyYear,
			String CCode, String score) {
		if(AuthNo.equals("YZScore")&&UserName.equals("YZScore")&&Pwd.equals("2017@YZScore")) {
		 Object object=tscoreService.UploadScoringResult(SID, StudyID,StudyYear,
			CCode, score);
		 return object;
		}
		return null;
	}
	

}
