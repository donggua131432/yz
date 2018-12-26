package com.yz.service.salarykpi;

import com.yz.dao.salarykpi.YearKPIMapper;
import com.yz.model.salarykpi.StudentDetailQuery;
import com.yz.model.salarykpi.YearKPIQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jyt
 * @version 1.0
 */
@Service
public class YearKPIService {

    private static Logger log = LoggerFactory.getLogger(YearKPIService.class);

    @Autowired
    YearKPIMapper yearKPIMapper;

    /**
     * 年度绩效查询
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> findAllYearKPI(YearKPIQuery query) {
        List<Map<String, Object>> result = yearKPIMapper.findAllYearKPI(query);
        for (Map<String, Object> map : result) {
            Map<String, Object> m = new HashMap<String, Object>();
            // 惠州圆梦入围标准注册人数
            BigDecimal hzymRwCount = new BigDecimal(String.valueOf(map.get("hzymRwCount")));
            // 惠州圆梦非入标准注册人数
            BigDecimal hzymFrwCount = new BigDecimal(String.valueOf(map.get("hzymFrwCount")));
            // 东莞圆梦标准注册人数
            BigDecimal dgymCount = new BigDecimal(String.valueOf(map.get("dgymCount")));
            // 奖学金≧150分标准注册人数
            BigDecimal jxjRwCount = new BigDecimal(String.valueOf(map.get("jxjRwCount")));
            // 奖学金<150分标准注册人数 + 201703
            BigDecimal jxjFrwCount = new BigDecimal(String.valueOf(map.get("jxjFrwCount")));
            // 201703，201709国开注册人数
            BigDecimal gkCount = new BigDecimal(String.valueOf(map.get("gkCount")));
            // 考前冲刺标准注册人数
            BigDecimal kqccCount = new BigDecimal(String.valueOf(map.get("kqccCount")));
            // 汕尾肋学入围标准注册人数
            BigDecimal swzxRwCount = new BigDecimal(String.valueOf(map.get("swzxRwCount")));
            // 汕尾肋学非入围标准注册人数
            BigDecimal swzxFrwCount = new BigDecimal(String.valueOf(map.get("swzxFrwCount")));
            // 普通全额标准参考人数
            BigDecimal ptqeCkCount = new BigDecimal(String.valueOf(map.get("ptqeCkCount")));
            // 普通全额标准注册人数
            BigDecimal ptqeZcCount = new BigDecimal(String.valueOf(map.get("ptqeZcCount")));
            // 被推荐人标准参考人数
            BigDecimal empCount = new BigDecimal(String.valueOf(map.get("empCount")));
            // 减标准任务人数
            BigDecimal entryCount = new BigDecimal(String.valueOf(map.get("entryCount")));
            BigDecimal leaveCount = new BigDecimal(String.valueOf(map.get("leaveCount")));
            BigDecimal taskCount = entryCount.subtract(leaveCount);
            // 已发放工资
            BigDecimal salarySum = new BigDecimal(String.valueOf(map.get("salarySum")));
            // 已发放报销
            BigDecimal receiptSum = new BigDecimal(String.valueOf(map.get("receiptSum")));
            // 单生工齡奖励
            BigDecimal award = new BigDecimal(String.valueOf(map.get("award")));
            m.put("taskCount",taskCount);
            // 年度标准参考人数合计=惠州圆梦入围标准注册人数 + 惠州圆梦非入标准注册人数 + 东莞圆梦标准注册人数 + 奖学金≧150分标准注册人数 + 奖学金<150分标准注册人数 + 201703，201709国开注册人数 + 考前冲刺标准注册人数 + 汕尾肋学入围标准注册人数 + 汕尾肋学非入围标准注册人数 + 普通全额标准参考人数 + 被推荐人标准参考人数
            m.put("ckSum", hzymRwCount.add(hzymFrwCount).add(dgymCount).add(jxjRwCount).add(jxjFrwCount).add(gkCount).add(kqccCount).add(swzxRwCount).add(swzxFrwCount).add(ptqeCkCount).add(empCount));
            // 标准注册人数合计=惠州圆梦入围标准注册人数 + 惠州圆梦非入标准注册人数 + 东莞圆梦标准注册人数 + 奖学金≧150分标准注册人数 + 奖学金<150分标准注册人数 + 201703，201709国开注册人数 + 考前冲刺标准注册人数 + 汕尾肋学入围标准注册人数 + 汕尾肋学非入围标准注册人数 + 普通全额标准注册人数 - 减标准任务人数
            m.put("zcSum", hzymRwCount.add(hzymFrwCount).add(dgymCount).add(jxjRwCount).add(jxjFrwCount).add(gkCount).add(kqccCount).add(swzxRwCount).add(swzxFrwCount).add(ptqeZcCount).subtract(taskCount));
            // 标准单生绩效值=年度标准参考人数对应单生绩效值
            m.put("singleStandardKPIValue", yearKPIMapper.getKPIValue(Double.parseDouble(String.valueOf(m.get("ckSum")))));
            // 实际单生绩效=单生工齡奖励+标准单生绩效值
            BigDecimal singleStandardKPIValue = new BigDecimal(String.valueOf(m.get("singleStandardKPIValue")));
            m.put("singleRealKPIValue", award.add(singleStandardKPIValue));
            // 应发绩效
            BigDecimal zcSum = new BigDecimal(String.valueOf(m.get("zcSum")));
            BigDecimal singleRealKPIValue = new BigDecimal(String.valueOf(m.get("singleRealKPIValue")));
            m.put("KPIValue", zcSum.multiply(singleRealKPIValue));
            // 费用支出合计项(已发放工资+已发放报销)
            m.put("CostSum", salarySum.add(receiptSum));
            // 实发绩效=应发绩效-已发放工资-已发放报销
            BigDecimal KPIValue = new BigDecimal(String.valueOf(m.get("KPIValue")));
            BigDecimal CostSum = new BigDecimal(String.valueOf(m.get("CostSum")));
            m.put("finalKPIValue", KPIValue.subtract(CostSum));
            map.putAll(m);
        }
        return result;
    }

    /**
     * 学员明细查询
     *
     * @param query
     * @return
     */
    public List<Map<String, Object>> findAllStudentDetail(StudentDetailQuery query) {
        if(query.getQueryType().equals("gk")){
            return yearKPIMapper.gkStudentDetail(query.getRecruit());
        }

        if(query.getQueryType().equals("qtqeCk")){
            return yearKPIMapper.qtqeCkStudentDetail(query.getRecruit());
        }
        query.setScholarshipItems(query.getScholarship().split(","));
        query.setInclusionStatusItems(query.getInclusionStatus().split(","));
        List<Map<String, Object>> result = yearKPIMapper.findAllStudentDetail(query);
        return result;
    }
}
