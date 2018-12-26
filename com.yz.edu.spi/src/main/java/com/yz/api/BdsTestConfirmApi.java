package com.yz.api;

import com.yz.exception.IRpcException;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

/**
 * @描述: 考前确认
 * @作者: DuKai
 * @创建时间: 2017/10/24 10:58
 * @版本号: V1.0
 */
public interface BdsTestConfirmApi {

    /**
     * 修改考前确认信息
     * @param header
     * @param body
     * @return
     */
	@YzService(sysBelong="bds",methodName="updateTestConfirmStat",methodRemark="修改考前确认状态信息",needLogin=true)
    public Object updateTestConfirmStat(Header header, Body body) throws IRpcException;
    
    /**
     * 学生自己扫码签到
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="studentExamSignBySelf",methodRemark="期末考试学员自己签到",needLogin=true)
    public Object studentExamSignBySelf(Header header, Body body) throws IRpcException;
    
    /**
     * 老师通过学生身份证号签到
     * @param header
     * @param body
     * @return
     * @throws IRpcException
     */
	@YzService(sysBelong="bds",methodName="studentExamSignByTeacher",methodRemark="期末考试老师替学员签到",needLogin=true)
    public Object studentExamSignByTeacher(Header header, Body body) throws IRpcException;
}
