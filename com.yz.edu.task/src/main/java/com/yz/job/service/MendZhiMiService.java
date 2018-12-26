package com.yz.job.service;

import com.yz.constants.FinanceConstants;
import com.yz.generator.IDGenerator;
import com.yz.job.dao.MendZhiMiMapper;
import com.yz.job.dao.UserRegisterMapper;
import com.yz.job.model.AtsAccount;
import com.yz.job.model.AtsAccountSerial;
import com.yz.job.model.AtsAwardRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @描述: 智米补送
 * @作者: DuKai
 * @创建时间: 2018/5/29 12:27
 * @版本号: V1.0
 */

@Service
public class MendZhiMiService {

    private static final Logger log = LoggerFactory.getLogger(MendZhiMiService.class);

    @Autowired
    private MendZhiMiMapper mendZhiMiMapper;

    @Autowired
    private UserRegisterMapper userRegisterMapper;


    /**
     * 专本连读补送智米
     */
    public void evenReadMend(){
        List<Map<String, Object>> evenReadList =  mendZhiMiMapper.selectEvenReadMendStdList();
        List<String> triggerUserIdList = mendZhiMiMapper.selectAwardRecordByRuleCode("even_read");
        evenReadList.forEach(evenReadMap ->{
            if (!triggerUserIdList.contains(evenReadMap.get("user_id").toString())) {
                AtsAwardRecord record = new AtsAwardRecord();
                record.setAwardId(IDGenerator.generatorId());
                record.setRuleCode("even_read");
                record.setAwardDesc("2018级专本连读报名成功");
                record.setUserId(evenReadMap.get("user_id").toString());
                record.setTriggerUserId(evenReadMap.get("user_id").toString());
                record.setRuleType("2");
                record.setZhimiCount("50000");
                //添加赠送记录
                userRegisterMapper.saveAwardRecord(record);
                //更新账户并记录流水
                addAtsAccountSerial(evenReadMap.get("user_id").toString(),new BigDecimal(50000),
                        evenReadMap.get("order_no").toString(),"2018级专本连读报名成功");
                log.info("专本连读智米补送成功【user_id："+evenReadMap.get("user_id").toString()
                        +"===std_name："+evenReadMap.get("std_name").toString()
                        +"===id_card："+evenReadMap.get("id_card").toString()
                        +"===mobile："+evenReadMap.get("mobile").toString()+"】");
            }
        });
    }

    /**
     * 转报补送智米
     */
    public void referralMend(){
        List<Map<String, Object>> referralList =  mendZhiMiMapper.selectReferralStdList();
        List<String> triggerUserIdList = mendZhiMiMapper.selectAwardRecordByRuleCode("referral_before_2017_11_03");
        referralList.forEach(referralMap->{
            if (!triggerUserIdList.contains(referralMap.get("user_id").toString())) {
                AtsAwardRecord record = new AtsAwardRecord();
                record.setAwardId(IDGenerator.generatorId());
                record.setRuleCode("referral_before_2017_11_03");
                record.setAwardDesc("邀请2017年11月3日以前录入的被邀约人，转报成功并在2018年3月1号前缴清第一学期学费");
                record.setUserId(referralMap.get("p_id").toString());
                record.setTriggerUserId(referralMap.get("user_id").toString());
                record.setRuleType("2");
                record.setZhimiCount("50000");
                //添加赠送记录
                userRegisterMapper.saveAwardRecord(record);
                //更新账户并记录流水
                addAtsAccountSerial(referralMap.get("p_id").toString(), new BigDecimal(50000),
                        referralMap.get("order_no").toString(),
                        "邀请2017年11月3日以前录入的被邀约人，转报成功并在2018年3月1号前缴清第一学期学费");

                log.info("转报智米补送成功【p_id：" + referralMap.get("p_id").toString()
                        + "===std_name：" + referralMap.get("std_name").toString()
                        + "===id_card：" + referralMap.get("id_card").toString()
                        + "===mobile：" + referralMap.get("mobile").toString() + "】");
            }
        });
    }

    public void referralPidMend(){
        //List<Map<String, Object>> referralPidList =  mendZhiMiMapper.selectReferralByPidList();
        List<Map<String, Object>> referralPidList = new ArrayList<>();
        referralPidList.forEach(referralMap->{
            AtsAwardRecord record = new AtsAwardRecord();
            record.setAwardId(IDGenerator.generatorId());
            record.setRuleCode("referral_after_2017_11_03");
            record.setAwardDesc("邀请2017年11月3日以后录入的被邀约人，报读2019成教后转报了国家开放大学1：5补送智米");
            record.setUserId(referralMap.get("p_id").toString());
            record.setTriggerUserId(referralMap.get("user_id").toString());
            record.setRuleType("2");
            BigDecimal zhimiCount =new BigDecimal(referralMap.get("fee_amount").toString()).multiply(new BigDecimal(5));
            record.setZhimiCount(zhimiCount.toString());
            //添加赠送记录
            userRegisterMapper.saveAwardRecord(record);
            //更新账户并记录流水
            addAtsAccountSerial(referralMap.get("p_id").toString(), zhimiCount,
                    referralMap.get("order_no").toString(),
                    "邀请2017年11月3日以后录入的被邀约人，报读2019成教后转报了国家开放大学1：5补送智米");

            log.info("转报智米补送成功【p_id：" + referralMap.get("p_id").toString()
                    + "===std_name：" + referralMap.get("std_name").toString()
                    + "===id_card：" + referralMap.get("id_card").toString()
                    + "===mobile：" + referralMap.get("mobile").toString() + "】");
        });

        System.out.println("==============================总共补送："+referralPidList.size()+"条");
    }

    /**
     * @description 更新用户账户信息 ，并记录智米帐变流水
     * @param userId
     * @param amount
     */
    private void addAtsAccountSerial(String userId, BigDecimal amount, String mappingId, String ruleDesc) {
        AtsAccount account = userRegisterMapper.getAccountInfo(userId, FinanceConstants.ACC_TYPE_ZHIMI);
        BigDecimal accAmount = new BigDecimal(account.getAccAmount());
        AtsAccountSerial serial = new AtsAccountSerial();
        serial.setAccId(account.getAccId());
        serial.setAccType(FinanceConstants.ACC_TYPE_ZHIMI);
        serial.setAction(FinanceConstants.ACC_ACTION_IN);
        serial.setAmount(String.valueOf(amount));
        serial.setUserId(userId);
        serial.setExcDesc(ruleDesc + "，获得" + String.valueOf(amount) + "智米");
        serial.setAccSerialStatus(FinanceConstants.ACC_SERAIL_STATUS_SUCCESS);
        serial.setBeforeAmount(account.getAccAmount());
        serial.setAfterAmount(String.valueOf(accAmount.add(amount)));
        serial.setAccSerialNo(IDGenerator.generatorId());
        serial.setMappingId(mappingId);
        userRegisterMapper.saveSerial(serial);
        account.setAccAmount(String.valueOf(amount));
        userRegisterMapper.updateAccount(account);
    }

}
