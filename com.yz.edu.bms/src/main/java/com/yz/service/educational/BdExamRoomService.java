package com.yz.service.educational;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.educational.BdExamPlaceMapper;
import com.yz.exception.BusinessException;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.condition.common.SelectQueryInfo;
import com.yz.model.educational.BdExamPlace;

@Service
@Transactional
public class BdExamRoomService {

	@Autowired
	private BdExamPlaceMapper examMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object selectExamRoomByPage(int start, int length, BdExamPlace query) {
		PageHelper.offsetPage(start, length);
		List<BdExamPlace> list = examMapper.selectExamRoomByPage(query);
		return new IPageInfo((Page) list);
	}

	public void insertExamPlace(BdExamPlace exam) {
		exam.setEpId(IDGenerator.generatorId());
		examMapper.insertExamRoom(exam);
		/*
		 * for (BdPlaceTime t : exam.getTimes()) { String date = t.getDate();
		 * String startTime = date + " " +t.getStartTime(); String endTime =
		 * date + " " + t.getEndTime(); t.setStartTime(startTime);
		 * t.setEndTime(endTime); } for (BdPlaceInfo p : exam.getPlaces()) {
		 * p.setCreateUser(exam.getCreateUser());
		 * p.setCreateUserId(exam.getCreateUserId());
		 * p.setUpdateUser(exam.getUpdateUser());
		 * p.setUpdateUserId(exam.getUpdateUserId()); p.setEpId(exam.getEpId());
		 * examMapper.insertPlaceInfoAndTimes(p, exam.getTimes()); }
		 */
	}

	public BdExamPlace selectExamRoomById(String epId) {

		return examMapper.selectExamRoomById(epId);
	}

	public void updateBdExamPlace(BdExamPlace exam) {
		examMapper.updateByPrimaryKeySelective(exam);
		/*
		 * examMapper.deltePlaceInfoAndTimes(exam.getEpId()); for (BdPlaceTime t
		 * : exam.getTimes()) { String date = t.getDate(); String startTime =
		 * date + " " +t.getStartTime(); String endTime = date + " " +
		 * t.getEndTime(); t.setStartTime(startTime); t.setEndTime(endTime); }
		 * for (BdPlaceInfo p : exam.getPlaces()) {
		 * p.setCreateUser(exam.getCreateUser());
		 * p.setCreateUserId(exam.getCreateUserId());
		 * p.setUpdateUser(exam.getUpdateUser());
		 * p.setUpdateUserId(exam.getUpdateUserId()); p.setEpId(exam.getEpId());
		 * examMapper.insertPlaceInfoAndTimes(p, exam.getTimes()); }
		 */
	}

	public void updateStatus(String epId, String statusBlock) {
		examMapper.updateStatus(epId, statusBlock);
	}

	public void deleteExamRoom(String epId) {
		int count = examMapper.selectPlaceYearCountByEpId(epId);
		if (count > 0) {
			BdExamPlace p = examMapper.selectExamRoomById(epId);
			throw new BusinessException("E000110", new String[] { p.getEpName() }); // 考场{0}正在被使用，无法删除
		}
		String[] epIds = { epId };
		examMapper.deleteExamRoom(epIds);
	}

	public void deleteExamRoom(String[] epIds) {
		examMapper.deleteExamRoom(epIds);
	}

	public void updateStatus(String[] epIds, String statusBlock) {
		examMapper.updateStatusBatch(epIds, statusBlock);
	}

	public BdExamPlace selectExamRoomByCode(String epCode) {
		return examMapper.selectExamRoomByCode(epCode);
	}

	public Object getEmName(SelectQueryInfo sqInfo) {
		PageHelper.startPage(sqInfo.getPage(), sqInfo.getRows());
		return new IPageInfo((Page) examMapper.getEmNameList(sqInfo));
	}

}
