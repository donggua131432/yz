package com.yz.service.graduate;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper; 
import com.yz.dao.graduate.GraduateApplyMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.graduate.BdGraduateRecordsInfo;
import com.yz.model.graduate.GraduateApplyInfo;
import com.yz.model.graduate.GraduateApplyQuery;
import com.yz.model.graduate.GraduateStudentInfo;
import com.yz.model.system.SysDict;
import com.yz.service.system.SysDictService;
/**
 * 毕业发起 & 学员毕业
 * @author lx
 * @date 2017年7月13日 下午2:53:03
 */
@Service
@Transactional
public class GraduateApplyService {
	
	private static final Logger log = LoggerFactory.getLogger(GraduateApplyService.class);
	
	@Autowired
	private SysDictService sysDictService;
	
	@Autowired
	private GraduateApplyMapper graduateApplyMappper;
	
	/**
	 * 发起毕业申请的学员信息列表
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryApplyInfosByPage(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<GraduateApplyInfo> applyInfos = graduateApplyMappper.queryGraduateApplyInfo(query);
		return new IPageInfo((Page) applyInfos);
	}
	/**
	 * 根据条件查询在读学员信息
	 * @param condition
	 * @param userId
	 * @return
	 */
	public GraduateStudentInfo queryStudentByCondition(String condition,String userId){
		GraduateStudentInfo studentInfo = graduateApplyMappper.queryStudentByCondition(condition,userId);
		if(null == studentInfo){
			studentInfo = new GraduateStudentInfo();
		}
		return studentInfo;
	}
	/**
	 * 新增毕业申请
	 * @param studentInfo
	 */
	public void insertGraduateApply(GraduateStudentInfo studentInfo){
		studentInfo.setGraduateId(IDGenerator.generatorId());
		graduateApplyMappper.insertGraduateApply(studentInfo);
		
		initGCheckTypes(studentInfo);
	}
	
	/**
	 * 初始化毕业审核类型信息
	 * @param baseInfo
	 */
	public void initGCheckTypes(GraduateStudentInfo studentInfo) {
		// 初始化毕业审核类型信息
		List<SysDict> agCheckTypes = sysDictService.getDicts("gCheckType");
		if (agCheckTypes != null && !agCheckTypes.isEmpty()) {
			List<BdGraduateRecordsInfo> recordsInfo = new ArrayList<BdGraduateRecordsInfo>();
			for (SysDict dict : agCheckTypes) {
				BdGraduateRecordsInfo info = new BdGraduateRecordsInfo();
				info.setGraduateId(studentInfo.getGraduateId());
				info.setgCheckType(dict.getDictValue());
				info.setCheckStatus("1");
				info.setCheckId(IDGenerator.generatorId());
				recordsInfo.add(info);
			}
			graduateApplyMappper.initGCheckTypes(recordsInfo);
			log.debug("---------------------------- 毕业发起[" + studentInfo.getGraduateId() + "]审核类型信息初始化成功");
		}
	}
	/**
	 * 删除
	 * @param idArray
	 */
	public void deleteGraduate(String[] idArray) {
		graduateApplyMappper.deleteGraduate(idArray);
	}
	
	/**
	 * 毕业学员的信息
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo queryGraduateStudentInfosByPage(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<GraduateApplyInfo> graduateDatas = graduateApplyMappper.queryGraduateStudentInfosByPage(query);
		return new IPageInfo((Page) graduateDatas);
	}
	/**
	 * 确认学员毕业
	 * @param learnId
	 */
	public void confirmGraduate(String learnId){
		graduateApplyMappper.confirmGraduate(learnId);
	}
}
