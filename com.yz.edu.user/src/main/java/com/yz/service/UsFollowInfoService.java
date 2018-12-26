package com.yz.service;

import com.yz.dao.UsFollowMapper;
import com.yz.model.UsFollowMb;
import com.yz.util.StringUtil;
import com.yz.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @描述: 跟进人相关接口业务
 * @作者: DuKai
 * @创建时间: 2018/3/5 17:50
 * @版本号: V1.0
 */
@Service
public class UsFollowInfoService {

    @Autowired
    private UsFollowMapper usFollowMapper;


    public Object getUsFollowMbList(String empId, String nameOrMobile){
        List<UsFollowMb> usFollowMbList = usFollowMapper.selectFollowMbByNameOrMobile(empId, nameOrMobile);

        for (UsFollowMb usFollowMb:usFollowMbList) {
            String authToken = TokenUtil.createToken(usFollowMb.getUserId(), StringUtil.UUID());
            usFollowMb.setAuthToken(authToken);
        }

        return usFollowMbList;
    }

}
