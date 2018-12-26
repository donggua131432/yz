package com.yz.service.educational;

import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.educational.BdExamYearProfessionMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.admin.BaseUser;
import com.yz.model.educational.BdExamYearProfession;
import com.yz.service.pubquery.TutorshipBookSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
@Transactional
public class BdExamYearProfessionService {

    private static Logger log = LoggerFactory.getLogger(TutorshipBookSendService.class);

    @Autowired
    BdExamYearProfessionMapper examYearProfessionMapper;

    /**
     * 查询考场年度ID
     *
     * @param examYear
     * @return
     */
    public String selectExamYearId(String examYear) {
        return examYearProfessionMapper.selectExamYearId(examYear);
    }

    /**
     * 查询专业信息
     *
     * @param grade
     * @param unvsName
     * @param pfsnLevel
     * @param pfsnName
     * @return
     */
    public Map<String, String> selectProfession(String grade, String unvsName, String pfsnLevel, String pfsnName) {
        return examYearProfessionMapper.selectProfession(grade, unvsName, pfsnLevel, pfsnName);
    }

    /**
     * 插入数据
     *
     * @param profession
     * @return
     */
    public int insert(BdExamYearProfession profession) {
        BaseUser user = SessionUtil.getUser();
        profession.setCreateTime(new Date());
        profession.setCreateUser(user.getRealName());
        profession.setCreateUserId(user.getUserId());
        profession.setEypId(IDGenerator.generatorId());
        return examYearProfessionMapper.insert(profession);
    }

    /**
     * 更新专业代号编码
     *
     * @param eypId
     * @param eypCode
     * @return
     */
    public int updateCode(String eypId, String eypCode) {
        BaseUser user = SessionUtil.getUser();
        return examYearProfessionMapper.updateCode(eypId, eypCode,user.getRealName(),user.getUserId(),new Date());
    }

    /**
     * 获取专业信息
     *
     * @param eyId
     * @param grade
     * @param unvsId
     * @param pfsnLevel
     * @param pfsnId
     * @return
     */
    public BdExamYearProfession getProfession(String eyId, String grade, String unvsId, String pfsnLevel, String pfsnId) {
        return examYearProfessionMapper.getProfession(eyId, grade, unvsId, pfsnLevel, pfsnId);
    }
}
