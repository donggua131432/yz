package com.yz.controller.pubquery;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.annotation.Rule;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdStudentSendMap;
import com.yz.model.pubquery.TutorshipBookSendQuery;
import com.yz.service.pubquery.TutorshipBookSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@RequestMapping("/tutorship")
@Controller
public class TutorshipBookSendController {

    @Autowired
    TutorshipBookSendService tutorshipBookSendService;

    @RequestMapping("/list")
    @Rule("tutorshipBookSend")
    public String showList(HttpServletRequest request) {
        return "pubquery/tutorship/tutorship-book-send-list";
    }

    @RequestMapping("/findAllTutorshipBookSend")
    @Rule("tutorshipBookSend:query")
    @ResponseBody
    public Object findAllTutorshipBookSend(@RequestParam(value = "start", defaultValue = "1") int start,
                                          @RequestParam(value = "length", defaultValue = "10") int length, TutorshipBookSendQuery query) {
        PageHelper.offsetPage(start, length);
        List<Map<Object,String>> resultList = tutorshipBookSendService.findAllTutorshipBookSend(query);
        return new IPageInfo((Page) resultList);
    }
}
