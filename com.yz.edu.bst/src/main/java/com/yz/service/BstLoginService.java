package com.yz.service;



import com.yz.core.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yz.dao.StudentAllMapper;
import com.yz.enums.ReturnStatus;
import com.yz.model.BdStudentBaseInfo;
import com.yz.vo.LoginUser;
import com.yz.vo.ReturnModel;


@Service
public class BstLoginService {

	private static final Logger log = LoggerFactory.getLogger(BstLoginService.class);

	@Autowired
	private StudentAllMapper studentMapper;


	
	/**
	 * 手机号登录
	 * 
	 * @param mobile
	 * @param valicode
	 * @param ip
	 * @param mac
	 * @param coordinate
	 * @return
	 */
	public ReturnModel login(String idCard, String pwd) {

		ReturnModel rq=new ReturnModel();
		rq.setCode(ReturnStatus.LoginError);
		BdStudentBaseInfo stu=studentMapper.getStudentInfoByIdCard(idCard);
		if(stu!=null) {
			String subCard=stu.getIdCard().substring(stu.getIdCard().length()-6,stu.getIdCard().length());
			if(stu.getIdCard()!=null&&subCard.equals(pwd)) {
				String userId=studentMapper.getRelationUserIdByStdId(stu.getStdId());
				//加入缓存
				LoginUser user=new LoginUser();
				user.setIdcard(stu.getIdCard());
				user.setPhone(stu.getMobile());
				user.setStdId(stu.getStdId());
				user.setUserId(userId);
				user.setUsername(stu.getStdName());
				SessionUtil.setKey(stu.getStdId());// 存入用户唯一登录标识
				SessionUtil.setUser(user);// 存入session对象
				
				
				rq.setCode(ReturnStatus.Success);
				rq.setMsg("登录成功！");
				rq.setBasemodle(stu);
			}else {
				rq.setCode(ReturnStatus.ParamError);
				rq.setMsg("密码输入错误！");
				SessionUtil.clearUser(stu.getStdId());// 存入session对象
				
			}
		}else {
			rq.setCode(ReturnStatus.ParamError);
			rq.setMsg("学员身份证号未注册！");
		}
		return rq;
	}

	

}
