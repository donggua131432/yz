package com.yz.service.oa;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.conf.YzSysConfig;
import com.yz.constants.GlobalConstants;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.oa.OaCommissionMapper;
import com.yz.dao.oa.PerformanceMapper;
import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.model.admin.BaseUser;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.common.IPageInfo;
import com.yz.model.oa.OaCommission;
import com.yz.model.oa.OaCommissionQuery;
import com.yz.util.AmountUtil;
import com.yz.util.BigDecimalUtil;
import com.yz.util.StringUtil;

@Service
@Transactional
public class OaCommissionService {

	@Autowired
	private YzSysConfig yzSysConfig;

	@Autowired
	private OaCommissionMapper comMapper;

	@Autowired
	private OaExpenseService expenseService;

	@Autowired
	private PerformanceMapper perMapper;

	public Object selectComminssionByPage(int start, int length, OaCommissionQuery query) {
		PageHelper.offsetPage(start, length);
		List<OaCommission> list = comMapper.selectComminssionByPage(query);

		Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());

		Integer startYear = 0;
		Integer endYear = 0;

		String startTime = null;
		String endTime = null;

		List<String> grades = new ArrayList<String>();
		if (null != year) {
			startYear = year - 2;
			endYear = year - 1;
			startTime = startYear.toString() + "-11-01 00:00:00";
			endTime = endYear.toString() + "-10-31 23:59:59";
			grades = perMapper.selectGradeByYear(year.toString());

		}

		if (StringUtil.hasValue(query.getMonth())) {
			String queryYear = endYear.toString();
			int monthCount = Integer.valueOf(query.getMonth());
			if (monthCount >= 11 && monthCount <= 12) {
				queryYear = startYear.toString();
			}
			startTime = queryYear + "-" + query.getMonth() + "-01 00:00:00";
			endTime = queryYear + "-" + query.getMonth() + "-31 23:59:59";

			if ("11".equals(query.getMonth())) { // 特殊情况，11月份需要包含9、10月
				startTime = queryYear + "-" + "09" + "-01";
			}
		}

		if (null != list && list.size() > 0 && null != grades && grades.size() > 0) {

			for (OaCommission ex : list) {
				ex.setYear(year.toString());

				BaseUser user = new BaseUser();
				user.setEmpId(ex.getEmpId());
				List<SessionDpInfo> dpList = expenseService.getDepartmentList(ex.getEmpId());// 校监级别权限
				if (null != dpList && dpList.size() > 0) {
					user.setMyDpList(dpList);
					user.setUserLevel(GlobalConstants.USER_LEVEL_DEPARTMENT);

					String[] stdStages = { "3", "4", "10", "5", "6", "7", "8", "12" };

					BigDecimal reply = BigDecimal.ZERO;

					// 招生人数计算
					String recruitCount = expenseService
							.count(perMapper.selectRecruitCount(startTime, endTime, grades, user, stdStages));

					stdStages = new String[] { "10" };
					// 退学标准学员计算
					String outCount = expenseService
							.count(perMapper.selectRecruitCount(startTime, endTime, grades, user, stdStages));

					reply = BigDecimalUtil.multiply(BigDecimalUtil.substract(AmountUtil.str2Amount(recruitCount),
							AmountUtil.str2Amount(outCount)), AmountUtil.str2Amount("60"));

					ex.setOutCount(outCount);
					ex.setAmount(reply.toString());
					ex.setCount(recruitCount);
				} else {
					ex.setAmount("0");
					ex.setCount("0");
					ex.setOutCount("0");
				}

			}

		} else {
			list.clear();
		}
		return new IPageInfo<OaCommission>((Page<OaCommission>) list);
	}

	public Object myCommission(String empId) {

		Map<String, Object> result = new HashMap<String, Object>();

		List<HashMap<String, String>> performances = new LinkedList<HashMap<String, String>>();

		BaseUser user = null;
		if (StringUtil.hasValue(empId)) {
			user = expenseService.getUser(empId);
		} else {
			user = SessionUtil.getUser();
		}

		Date d2 = new Date();
		Date d1 = null;
		try {
			d1 = new SimpleDateFormat("yyyy-MM").parse("2017-11");// 定义起始日期
			// d2 = new SimpleDateFormat("yyyy-MM").parse("2018-10");// 定义起始日期
		} catch (ParseException e) {
			return null;
		} // 定义结束日期

		Calendar dd = Calendar.getInstance();// 定义日期实例

		dd.setTime(d1);// 设置日期起始时间

		// 剩余可报销金额
		BigDecimal allReply = BigDecimal.ZERO;

		while (dd.getTime().before(d2)) {// 判断是否到结束日期

			HashMap<String, String> per = new HashMap<String, String>();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

			String rMonth = dd.get(Calendar.MONTH) + 1 + "";

			String rYear = dd.get(Calendar.YEAR) + "";

			String month = sdf.format(dd.getTime());

			// 预计报销
			BigDecimal reply = BigDecimal.ZERO;
			// 已报销
			String replyed = "0.00";

			List<String> months = new ArrayList<String>();

			Integer year = Integer.valueOf(yzSysConfig.getRecruitYear());
			List<String> grades = new ArrayList<String>();
			if (null != year) {
				grades = perMapper.selectGradeByYear(year.toString());
			}

			if ("2017-11".equals(month)) {
				months.add("2017-09");
				months.add("2017-10");
			}
			months.add(month);

			String[] stdStages = { "3", "4", "10", "5", "6", "7", "8", "12" };
			// 招生标准人数计算
			String recruitCount = expenseService.count(perMapper.findRecruitStudents(months, user, stdStages, grades));

			stdStages = new String[] { "10" };
			// 退学标准学员计算
			String outCount = expenseService.count(perMapper.findRecruitStudents(months, user, stdStages, grades));

			BigDecimal sub = BigDecimalUtil.substract(AmountUtil.str2Amount(recruitCount),
					AmountUtil.str2Amount(outCount));
			reply = BigDecimalUtil.multiply(sub, AmountUtil.str2Amount("60"));

			replyed = perMapper.selectReplayedByRecruit(rMonth, user.getEmpId(), rYear);

			per.put("month", month);
			per.put("recruitCount", StringUtil.hasValue(recruitCount) ? recruitCount : "0");
			per.put("outCount", StringUtil.hasValue(outCount) ? outCount : "0");

			per.put("amount", reply.toString());

			// 计算总报销额
			allReply = BigDecimalUtil.add(allReply, BigDecimalUtil.substract(reply, AmountUtil.str2Amount(replyed)));

			dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1

			performances.add(per);
		}

		result.put("performances", performances);
		result.put("allReply", allReply.toString());

		return result;
	}

}
