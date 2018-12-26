package com.yz.service.educational;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.educational.BdExamYearMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.educational.BdExamReason;
import com.yz.model.educational.BdExamYear;
import com.yz.model.educational.BdYearSubject;
import com.yz.util.StringUtil;

@Service
@Transactional
public class BdExamYearService {

	@Autowired
	private BdExamYearMapper examMapper;

	public Object selectExamYearByPage(int start, int length) {
		PageHelper.offsetPage(start, length);
		List<BdExamYear> list = examMapper.selectExamYearByPage();
		return new IPageInfo<BdExamYear>((Page<BdExamYear>) list);
	}

	public void insertExamYear(BdExamYear exam) {
		if (null == exam.getReasons() || exam.getReasons().size() <= 0) {

			throw new BusinessException("E000106"); // 未确认原因不能为空
		} else {
			List<BdExamReason> reason = new ArrayList<BdExamReason>();
			for (int i = 0; i < exam.getReasons().size(); i++) {
				String re = exam.getReasons().get(i).getReason();
				if (StringUtil.hasValue(re)) {
					BdExamReason r = new BdExamReason();
					r.setReason(re);
					r.setErId(IDGenerator.generatorId());
					reason.add(r);
				}
			}
			if (null == reason || reason.size() <= 0) {
				throw new BusinessException("E000106"); // 未确认原因不能为空
			}
			exam.setReasons(reason);
		}
		if (null == exam.getSubjects() || exam.getSubjects().size() <= 0) {

			throw new BusinessException("E000108"); // 未确认原因不能为空
		} else {
			List<BdYearSubject> subjects = new ArrayList<BdYearSubject>();
			for (int i = 0; i < exam.getSubjects().size(); i++) {
				String g = exam.getSubjects().get(i).getGrade();
				String s = exam.getSubjects().get(i).getSemester();
				BdYearSubject r = new BdYearSubject();
				r.setYsId(IDGenerator.generatorId());
				if (StringUtil.hasValue(g) && StringUtil.hasValue(s)) {
					r.setGrade(g);
					r.setSemester(s);
					subjects.add(r);
				}
			}
			if (null == subjects || subjects.size() <= 0) {
				throw new BusinessException("E000108"); // 未确认原因不能为空
			}
			exam.setSubjects(subjects);
		}
		exam.setEyId(IDGenerator.generatorId());
		exam.setStatus("1");
		examMapper.insertExamYear(exam);
		examMapper.insertExamReasons(exam);
		examMapper.insertExamSubject(exam);
	}

	public BdExamYear selectExamYearById(String eyId) {
		return examMapper.selectExamYearById(eyId);
	}

	public void updateBdExamYear(BdExamYear exam) {
		if (null == exam.getReasons() || exam.getReasons().size() <= 0) {

			throw new BusinessException("E000106"); // 未确认原因不能为空
		} else {
			examMapper.deleteExamReason(exam.getEyId());
			List<BdExamReason> reason = exam.getReasons();
			if (null == reason || reason.size() <= 0) {
				throw new BusinessException("E000106"); // 未确认原因不能为空
			}
			for (BdExamReason re : reason) {
				String reasonStr = re.getReason();
				String erId = re.getErId();
				String eyId = exam.getEyId();
				re.setEyId(eyId);

				if (StringUtil.hasValue(reasonStr)) {
					if(!StringUtil.hasValue(erId)){
						erId = IDGenerator.generatorId();
					}
					re.setErId(erId);
					// 修改原因
					examMapper.insertExamReason(re);
				} else {
					// 删除原因
					examMapper.deleteExamReasonByErId(erId);
				}
				
			}
			/*
			 * for (int i = 0; i < exam.getReasons().size(); i++) {
			 * 
			 * String re = exam.getReasons().get(i).getReason();
			 * if(StringUtil.hasValue()){
			 * 
			 * } if (StringUtil.hasValue(re)) { BdExamReason r = new
			 * BdExamReason(); r.setReason(re); reason.add(r);
			 * 
			 * } }
			 */
			exam.setReasons(reason);
		}

		if (null == exam.getSubjects() || exam.getSubjects().size() <= 0) {

			throw new BusinessException("E000108"); // 未确认原因不能为空
		} else {
			List<BdYearSubject> subjects = new ArrayList<BdYearSubject>();
			for (int i = 0; i < exam.getSubjects().size(); i++) {
				String g = exam.getSubjects().get(i).getGrade();
				String s = exam.getSubjects().get(i).getSemester();
				BdYearSubject r = new BdYearSubject();
				r.setYsId(IDGenerator.generatorId());
				if (StringUtil.hasValue(g) && StringUtil.hasValue(s)) {
					r.setGrade(g);
					r.setSemester(s);
					subjects.add(r);
				}
			}
			if (null == subjects || subjects.size() <= 0) {
				throw new BusinessException("E000108"); // 未确认原因不能为空
			}
			exam.setSubjects(subjects);
		}
		examMapper.updateExam(exam);
		// examMapper.insertExamReason(exam);
		examMapper.deleteExamSubject(exam.getEyId());
		examMapper.insertExamSubject(exam);

	}

	public void updateYearStatus(String eyId, String statusBlock) {
		examMapper.updateExamYearStatus(eyId, statusBlock);
	}

	public void deleteExamYear(String eyId) {
		int count = examMapper.selectPlaceYearCount(eyId);
		if (count > 0) {
			BdExamYear y = examMapper.selectExamYearById(eyId);
			throw new BusinessException("E000109", new String[] { y.getExamYear() }); // 年度{0}正在被使用，无法删除
		}
		examMapper.deleteExamYear(eyId);
		examMapper.deleteExamReason(eyId);
		examMapper.deleteExamSubject(eyId);
	}

	public void deleteExamYears(String[] eyIds) {
		examMapper.deleteExamYears(eyIds);
		examMapper.deleteExamReasons(eyIds);
		examMapper.deleteExamSubjects(eyIds);

	}

	public int selectExamYearCount(String examYear) {
		return examMapper.selectExamYearCount(examYear);
	}

}
