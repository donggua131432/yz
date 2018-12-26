package com.yz.service.invite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.common.BaseInfoMapper;
import com.yz.dao.finance.AtsAccountMapper;
import com.yz.dao.invite.InviteStayFansMapper;
import com.yz.dao.oa.OaEmployeeMapper;
import com.yz.dao.us.UsFollowLogMapper;
import com.yz.edu.paging.bean.IPageInfo;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.condition.invite.InviteUserQuery;
import com.yz.model.invite.InviteUserList;
import com.yz.model.oa.OaEmpFollowInfo;
import com.yz.util.CodeUtil;

/**
 * @描述: 校监分配粉丝
 * @作者: DuKai
 * @创建时间: 2017/12/4 17:30
 * @版本号: V1.0
 */
@Transactional
@Service
public class InviteStayFansService {

    @Autowired
    InviteStayFansMapper inviteStayFansMapper;

    @Autowired
    private OaEmployeeMapper employeeMapper;

    @Autowired
    private UsFollowLogMapper logMapper;

    @Autowired
    private AtsAccountMapper accMapper;

    @Autowired
    private BaseInfoMapper baseInfoMapper;

    public IPageInfo getList(InviteUserQuery queryInfo, String userType) {
        List<String> dpList = new ArrayList<>();
        //如果userType的值为1表示为管理员用户
        if("1".equals(userType)){
            dpList = baseInfoMapper.getDpIdList(queryInfo.getDpManager());
            queryInfo.setDpIds(dpList);
        }
        //如果userType的值为2表示为校监用户 获取校监管理的部门
        if("2".equals(userType)){
            BaseUser user = SessionUtil.getUser();
            dpList = baseInfoMapper.getDpIdList(user.getEmpId());
            queryInfo.setDpIds(dpList);
        }

        PageHelper.offsetPage(queryInfo.getStart(), queryInfo.getLength());
        List<InviteUserList> list = inviteStayFansMapper.getList(queryInfo);
        if (list == null) return null;

        for (InviteUserList listInfo : list) {
            listInfo.setNickname(CodeUtil.base64Decode2String(listInfo.getNickname()));
            listInfo.setpNickname(CodeUtil.base64Decode2String(listInfo.getpNickname()));
            String empId = listInfo.getEmpId();
            String dpManager = listInfo.getDpManager();

            //设置跟进人信息 校监信息
            OaEmpFollowInfo empInfo = employeeMapper.getEmpFollowInfo(empId);
            if (empInfo != null) {
                listInfo.setEmpName(empInfo.getEmpName());
                listInfo.setEmpMobile(empInfo.getMobile());
                listInfo.setEmpStatus(empInfo.getEmpStatus());
                listInfo.setDpManager(empInfo.getDpManager());
                listInfo.setDpManagerName(empInfo.getDpManagerName());
            }

            listInfo.setAllotCount(logMapper.selectCountByUserId(listInfo.getUserId()));
            // 查询账户余额
            List<Map<String, String>> accList = accMapper.getAccountList(listInfo.getUserId());
            listInfo.setAccList(accList);
        }
        return new IPageInfo((Page<InviteUserList>) list);
    }

}
