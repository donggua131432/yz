package com.yz.api;

import javax.jws.WebService;

@WebService
public interface ScoringAPI {

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

    public Object GetPreScoringResaulByPage(String AuthNo, String UserName, String Pwd, String WhereSQL, int PageSize, int PageIndex);


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
    public Object UploadScoringResult(String AuthNo, String UserName, String Pwd, String SID, String StudyID, String StudyYear, String CCode, String score);
    
    
    
    
    
}
