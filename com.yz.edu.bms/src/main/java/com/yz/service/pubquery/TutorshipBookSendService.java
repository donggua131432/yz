package com.yz.service.pubquery;

import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.pubquery.TutorshipBookSendMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
public class TutorshipBookSendService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Autowired
    TutorshipBookSendMapper tutorshipBookSendMapper;

    /**
     * 辅导书发放查询
     * @param query
     * @return
     */
    public List<Map<Object, String>> findAllTutorshipBookSend(TutorshipBookSendQuery query) {
        BaseUser user = SessionUtil.getUser();

        //400专员也能查看所有记录
        List<String> jtList = user.getJtList();
        if(jtList != null) {
            if(jtList.contains("400")) {
                user.setUserLevel(GlobalConstants.USER_LEVEL_SUPER);
            }
        }

        List<Map<Object, String>> result = tutorshipBookSendMapper.findAllTutorshipBookSend(query,user);
        return result;
    }
}
