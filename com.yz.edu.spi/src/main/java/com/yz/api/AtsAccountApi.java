package com.yz.api;

import java.util.List;
import java.util.Map;

import com.yz.exception.IRpcException;
import com.yz.model.CommunicationMap;
import com.yz.model.YzService;
import com.yz.model.communi.Body;
import com.yz.model.communi.Header;

public interface AtsAccountApi {
	/**
	 * 【PRI】查询用户账户余额（用户、学员、员工编号 必须填写一个）
	 * @param [S]userId 用户ID
	 * @param [S]stdId 学员ID
	 * @param [S]empId 员工ID
	 * @param [M]accType 账户类型 1-现金账户  2-智米3-滞留账户
	 * @return
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>accStatus : 账户状态</td></tr>
	 *  	<tr><td>accAmount : 账户金额</td></tr>
	 *  	<tr><td>accType : 账户类型</td></tr>
	 *  	<tr><td>canDeposit : 是否可提现 0-否 1-是</td></tr>
	 *  	<tr><td>thawTime : 解冻时间</td></tr>
	 *  	<tr><td>frezTime : 冻结时间</td></tr>
	 *  	<tr><td>userId : 用户ID</td></tr>
	 *  	<tr><td>empId : 员工ID</td></tr>
	 *  	<tr><td>stdId : 学员ID</td></tr>
	 *  </tbody>
	 * </table>
	 */
	public Map<String, String> getAccount(String userId, String stdId, String empId, String accType) throws IRpcException;
	
	/**
	 * 【PRI】根据类型初始化账户（用户、学员、员工编号 必须填写一个）
	 * @param [S]userId 用户ID
	 * @param [S]stdId 学员ID
	 * @param [S]empId 员工ID
	 * @param [M]accTypes 数组【 账户类型 1-现金账户  2-智米3-滞留账户】
	 * @return
	 */
	public void initAccount(String userId, String stdId, String empId, String[] accTypes) throws IRpcException;
	/**
	 * 【PUB】我的账户
	 * @param header
	 * @param body
	 * @return
	 */
	@YzService(sysBelong="ats",methodName="accountList",methodRemark="我的账户",needLogin = true)
	public Object getAccountList(Header header, Body body) throws IRpcException;
	/**
	 * 【PUB】账户详情
	 * @param header
	 * @param body
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  </tbody>
	 * </table>
	 * @return
	 */
	@YzService(sysBelong="ats",methodName="accountDetail",methodRemark="账户详情",needLogin =true)
	public Object getAccountDetail(Header header, Body body) throws IRpcException;
	/**
	 * 【PRI】动账交易（用户、学员、员工编号 必须填写一个）
	 * @param body : 
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[S]userId : 用户ID</td></tr>
	 *  	<tr><td>[S]stdId : 学员ID</td></tr>
	 *  	<tr><td>[S]empId : 员工ID</td></tr>
	 *  	<tr><td>[M]amount : 动账金额 </td></tr>
	 *  	<tr><td>[M]action : 进出账动作 1-进账 2-出账</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 * @return
	 */
	public void trans(Body body) throws IRpcException; 
	
	/**
	 * 【PRI】奖励（用户、学员、员工编号 必须填写一个）
	 * @param body : 
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[M]ruleCode : 规则编码</td></tr>
	 *  	<tr><td>[M]userId : 用户ID</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>triggerUserId : 触发人ID</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 * @return
	 */
	public Map<String, String> award(CommunicationMap body) throws IRpcException;
	/**
	 * 将学员智米账户金额迁移到用户智米账户，删除学员智米账户， 并将流水中的accId改为用户accId
	 * @param userId
	 * @param stdId
	 * @throws IRpcException
	 */
	public void copyZhimi(String userId, String stdId) throws IRpcException;
	/**
	 * 【PRI】奖励（用户、学员、员工编号 必须填写一个）
	 * @param cmList : 
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[M]ruleCode : 规则编码</td></tr>
	 *  	<tr><td>[M]userId : 用户ID</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>triggerUserId : 触发人ID</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 * @return
	 */
	public void awardMore(List<CommunicationMap> cmList);
	/**
	 * 【PRI】交易与奖励
	 * @param cmList : 
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[M]ruleCode : 规则编码</td></tr>
	 *  	<tr><td>[M]userId : 用户ID</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>triggerUserId : 触发人ID</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 * @param
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[S]userId : 用户ID</td></tr>
	 *  	<tr><td>[S]stdId : 学员ID</td></tr>
	 *  	<tr><td>[S]empId : 员工ID</td></tr>
	 *  	<tr><td>[M]amount : 动账金额 </td></tr>
	 *  	<tr><td>[M]action : 进出账动作 1-进账 2-出账</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 * @return
	 */
	public void transAndAwardMore(List<Body> transList, List<CommunicationMap> mList);
	/**
	 * 【PRI】动账交易（用户、学员、员工编号 必须填写一个）
	 * @param body : 
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[S]userId : 用户ID</td></tr>
	 *  	<tr><td>[S]stdId : 学员ID</td></tr>
	 *  	<tr><td>[S]empId : 员工ID</td></tr>
	 *  	<tr><td>[M]amount : 动账金额 </td></tr>
	 *  	<tr><td>[M]action : 进出账动作 1-进账 2-出账</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 */
	public void transMore(List<Body> transList);
	/**
	 * 【PRI】动账交易（用户、学员、员工编号 必须填写一个）
	 * @param body
	 * <table>
	 *  <tbody>
	 *  	<tr><td>accId : 账户ID</td></tr>
	 *  	<tr><td>[M]accType : 账户类型</td></tr>
	 *  	<tr><td>[M]ruleCode : 规则编码</td></tr>
	 *  	<tr><td>[M]userId : 用户ID</td></tr>
	 *  	<tr><td>[M]excDesc : 动账描述</td></tr>
	 *  	<tr><td>triggerUserId : 触发人ID</td></tr>
	 *  	<tr><td>mappingId : 映射ID - 如：订单号或者其他编号</td></tr>
	 *  </tbody>
	 * </table>
	 */
	public void copyAndAward(Body body);
}
