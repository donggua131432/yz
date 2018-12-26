package com.yz.dao.pubquery;

import com.yz.model.admin.BaseUser;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
public interface TutorshipBookSendMapper {
        List<Map<Object,String>> findAllTutorshipBookSend(@Param("query")TutorshipBookSendQuery query,@Param("user") BaseUser user);
}
