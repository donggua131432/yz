package com.yz.job.task;

import com.yz.constants.FinanceConstants;
import com.yz.constants.WechatMsgConstants;
import com.yz.generator.IDGenerator;
import com.yz.job.common.AbstractSimpleTask;
import com.yz.job.common.YzJob;
import com.yz.job.dao.TaskCardMapper;
import com.yz.job.dao.UserRegisterMapper;
import com.yz.job.model.AtsAccount;
import com.yz.job.model.AtsAccountSerial;
import com.yz.job.model.AtsAwardRecord;
import com.yz.model.WechatMsgVo;
import com.yz.redis.RedisService;
import com.yz.task.YzTaskConstants;
import com.yz.util.DateUtil;
import com.yz.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述: 任务卡
 * @作者: DuKai
 * @创建时间: 2018/6/8 16:33
 * @版本号: V1.0
 */
@Component(value = "taskCardTask")
@YzJob(taskDesc = YzTaskConstants.YZ_TASK_CARD_TASK, cron = "0 0/1 * * * ? *", shardingTotalCount = 1)
public class TaskCardTask extends AbstractSimpleTask {

    @Autowired
    private TaskCardMapper taskCardMapper;

    @Autowired
    private UserRegisterMapper userRegisterMapper;


    @Override
    public void executeOther() {
        System.out.println("开始执行任务卡状态修改任务！");
        List<Map<String,Object>> taskCardList = taskCardMapper.selectBdTaskCard();
        Date currentDate =  DateUtil.convertDateStrToDate(DateUtil.getNowDateAndTime(), "yyyy-MM-dd");
        taskCardList.forEach(taskCard->{
            Date startTime = DateUtil.convertDateStrToDate(taskCard.get("startTime").toString(), "yyyy-MM-dd");
            Date endTime = DateUtil.convertDateStrToDate(taskCard.get("endTime").toString(), "yyyy-MM-dd");
            String taskStatus = taskCard.get("taskStatus").toString();
            Map<String, Object> map = new HashMap<>();
            map.put("taskId",taskCard.get("taskId").toString());
            map.put("taskStatus",taskCard.get("taskStatus").toString());
            if((currentDate.after(startTime) || currentDate.equals(startTime)) && "2".equals(taskStatus)){
                map.put("taskStatus","3");
            }

            if(currentDate.after(endTime) && "3".equals(taskStatus)){
                map.put("taskStatus","4");
            }
            taskCardMapper.updateBdTaskCard(map);
        });

        //如果任务已经完成就赠送智米
        List<Map<String,Object>> usTaskCardList = taskCardMapper.selectUsTaskCard();
        usTaskCardList.forEach(map->{
            //赠送智米
            AtsAwardRecord record = new AtsAwardRecord();
            record.setAwardId(IDGenerator.generatorId());
            record.setRuleCode("TASK_CARD_AWARD");
            record.setAwardDesc(map.get("taskName").toString()+"任务完成");
            record.setUserId(map.get("userId").toString());
            record.setTriggerUserId(map.get("userId").toString());
            record.setRuleType("2");
            record.setZhimiCount(map.get("taskReward").toString());
            //添加赠送记录
            userRegisterMapper.saveAwardRecord(record);
            //更新账户并记录流水
            addAtsAccountSerial(map.get("userId").toString(),new BigDecimal(map.get("taskReward").toString()),map.get("taskId").toString(),
                    map.get("taskName").toString()+"任务完成");

            //修改赠送状态
            taskCardMapper.updateIsAward(map.get("id").toString());

            //推送完成任务提醒
            WechatMsgVo wechatVo = new WechatMsgVo();
            wechatVo.setTouser(map.get("openId").toString());
            wechatVo.setTemplateId(WechatMsgConstants.TEMPLATE_MSG_STUDENT_INFORM);
            wechatVo.addData("title", "您好！您领取任务已经完成");
            wechatVo.addData("msgName", map.get("taskName").toString());
            wechatVo.addData("code", "YZ");
            wechatVo.addData("content", "您成功邀请"+map.get("completeCount").toString()+"人报读，奖励任务智米"+map.get("taskReward").toString());
            wechatVo.setExt1("");
            wechatVo.setIfUseTemplateUlr(false);
            RedisService.getRedisService().lpush(YzTaskConstants.YZ_WECHAT_MSG_TASK, JsonUtil.object2String(wechatVo));
        });
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
