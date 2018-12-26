package com.yz;

import com.yz.app.BmsApplication;
import com.yz.constants.GlobalConstants;
import com.yz.constants.StudentConstants;
import com.yz.dao.finance.BdStdPayFeeMapper;
import com.yz.dao.recruit.BdLearnAnnexMapper;
import com.yz.model.recruit.BdLearnAnnex;
import com.yz.model.recruit.BdLearnInfo;
import com.yz.model.system.SysDict;
import com.yz.service.system.SysDictService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @描述: ${DESCRIPTION}
 * @作者: DuKai
 * @创建时间: 2018/4/20 12:06
 * @版本号: V1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=BmsApplication.class)
@WebAppConfiguration
public class BdLearnAnnexTest {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private BdStdPayFeeMapper payMapper;

    @Autowired
    private BdLearnAnnexMapper lAnnexMapper;

    @Test
    public void initLearnInfo() throws Exception {
        /*String learnIdStr = "457743676863638009,457743676863638743,457743676863640071,457743676863640693,457743676863641848,457743676863648645,457743676863650638,457743676863654923,457743676863655212,457743676863655254," +
                "457743676863656205,457743676863656287,457743676863656666,457743676863657008,457743676863657240,457743676863657700,457743676863657916,457743676863658058,457743676863658176,457743676863658653," +
                "457743676863659026,457743676863659239,457743676863659847,457743676863660245,457743676863660552,457743676863660981,457743676863661036,457743676863661924,457743676863662066,457743676863662575," +
                "457743676863662705,457743676863664263,457743676863664992,457743676863665364,457743676863665526,457743676863665576,457743676863665627,457743676863666142";*/
        String learnIdStr = "";

        String[] learnIdArr = learnIdStr.split(",");

        for (String learnId:learnIdArr) {
            BdLearnInfo learnInfo = payMapper.selectLearnInfoByLearnId(learnId);
            String recruitType = learnInfo.getRecruitType();
            // 初始化附件信息
            List<SysDict> annexTypeList = sysDictService.getDicts("annexType");
            List<String> isRequire = new ArrayList<String>();

            if (StudentConstants.RECRUIT_TYPE_CJ.equals(recruitType)) {
                isRequire.add(StudentConstants.ANNEX_TYPE_EDUCATION);
                isRequire.add(StudentConstants.ANNEX_TYPE_IDCARD_FRONT);
                isRequire.add(StudentConstants.ANNEX_TYPE_IDCARD_BEHIND);
            } else {
                isRequire.add(StudentConstants.ANNEX_TYPE_PHOTO);
            }

            List<BdLearnAnnex> annexList = new ArrayList<BdLearnAnnex>();
            for (SysDict annexType : annexTypeList) {
                BdLearnAnnex annex = new BdLearnAnnex();
                String type = annexType.getDictValue();
                String name = annexType.getDictName();

                annex.setAnnexType(type);
                annex.setAnnexName(name);
                annex.setAnnexStatus(StudentConstants.ANNEX_STATUS_UNUPLOAD);

                if (isRequire.contains(type)) {
                    annex.setIsRequire(GlobalConstants.TRUE);
                } else {
                    annex.setIsRequire(GlobalConstants.FALSE);
                }
                annex.setLearnId(learnId);

                annexList.add(annex);
            }

            lAnnexMapper.batchInsert(annexList);

            System.out.println("---------------------------- 学业[" + learnId + "]附件信息初始化成功");
        }
    }
}
