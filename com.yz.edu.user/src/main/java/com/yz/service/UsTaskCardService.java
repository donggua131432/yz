package com.yz.service;

import com.yz.dao.BdsLearnMapper;
import com.yz.dao.StudentMpFlowMapper;
import com.yz.dao.UsBaseInfoMapper;
import com.yz.dao.UsTaskCardMapper;
import com.yz.model.UsBaseInfo;
import com.yz.model.UsTaskCard;
import com.yz.model.UsTaskCardDetail;
import com.yz.model.communi.Body;
import com.yz.util.DateUtil;
import com.yz.util.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @描述: 任务卡
 * @作者: DuKai
 * @创建时间: 2018/6/7 17:22
 * @版本号: V1.0
 */
@Service
public class UsTaskCardService {


    @Autowired
    private UsTaskCardMapper taskCardMapper;

    @Autowired
    private BdsLearnMapper bdsLearnMapper;

    @Autowired
    private UsBaseInfoMapper usBaseInfoMapper;


    public void addUsTaskCard(Body body){
        UsTaskCard usTaskCard = new UsTaskCard();
        usTaskCard.setUserId(body.getString("userId"));
        usTaskCard.setTaskId(body.getString("taskId"));
        usTaskCard.setTaskCount(body.getString("taskCount"));

        taskCardMapper.insertUsTaskCard(usTaskCard);
    }

    public List<Map<String, Object>> getReleaseTaskCard(String userId){
        return taskCardMapper.selectReleaseTaskCard(userId);
    }


    public List<Map<String, Object>> getUsTaskCardDetail(Map map){
        return taskCardMapper.selectUsTaskCardDetail(map);
    }

    //记录任务完成进度
    public void addUsTaskCardDetail(String userId,String[] itemCodes, String learnId){
        UsBaseInfo usBaseInfo = usBaseInfoMapper.selectByPrimaryKey(userId);
        if(usBaseInfo != null){
            String pId = usBaseInfo.getpId();
            if(StringUtil.hasValue(pId)){
                Date regTime= DateUtil.convertDateStrToDate(usBaseInfo.getRegTime(), "yyyy-MM-dd");
                String recruitType = bdsLearnMapper.selectRecruitTypeByLearnId(learnId);
                //获取任务信息
                List<Map<String, Object>> receiveList = taskCardMapper.selectReceiveTaskCard(pId);
                if(receiveList!=null && !receiveList.isEmpty()){
                    for (int i=0; i<receiveList.size(); i++){
                        int count = taskCardMapper.selectUsTaskCardDetailCount(pId,receiveList.get(i).get("taskId").toString(),userId);
                        //一个任务只能有一个对应的邀约关系
                        if(count <= 0){
                            //判断是否在活动时间内
                            Date startTime = DateUtil.convertDateStrToDate(receiveList.get(i).get("startTime").toString(), "yyyy-MM-dd");
                            Date endTime = DateUtil.convertDateStrToDate(receiveList.get(i).get("endTime").toString(), "yyyy-MM-dd");
                            if((regTime.after(startTime) || regTime.equals(startTime)) && (regTime.before(endTime) || regTime.equals(endTime))){
                                UsTaskCardDetail usTaskCardDetail = new UsTaskCardDetail();
                                usTaskCardDetail.setUserId(pId);
                                usTaskCardDetail.setTriggerUserId(userId);
                                usTaskCardDetail.setTaskId(receiveList.get(i).get("taskId").toString());
                                //成教
                                if("1".equals(recruitType) && ArrayUtils.contains(itemCodes,"Y0")){
                                    addUsTaskCardDetail(receiveList.get(i),usTaskCardDetail);
                                }
                                //国开
                                if("2".equals(recruitType) && ArrayUtils.contains(itemCodes,"Y1")){
                                    addUsTaskCardDetail(receiveList.get(i),usTaskCardDetail);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addUsTaskCardDetail(Map<String, Object> map, UsTaskCardDetail usTaskCardDetail){
        int taskCount = Integer.parseInt(map.get("taskCount").toString());
        int completeCount = Integer.parseInt(map.get("completeCount").toString());
        int completeStatus = Integer.parseInt(map.get("completeStatus").toString());
        //任务未完成更新记录
        if(completeStatus == 0){
            //记录邀约进度
            taskCardMapper.insertUsTaskCardDetail(usTaskCardDetail);
            completeCount = completeCount+1;
            if(completeCount==taskCount){
                map.put("completeStatus","1");
                map.put("completeTime",DateUtil.getNowDateAndTime());
            }
            map.put("completeCount",completeCount);
            taskCardMapper.updateUsTaskCard(map);
        }
    }
}
