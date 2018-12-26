package com.yz.service.graduate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.graduate.GraduateCheckMapper;
import com.yz.model.common.IPageInfo;
import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.graduate.BdCheckResultShowInfo;
import com.yz.model.graduate.BdGraduateApprovalInfo;
import com.yz.model.graduate.BdGraduateCollectInfo;
import com.yz.model.graduate.BdGraduateRecordsInfo;
import com.yz.model.graduate.BdStudentPaperInfo;
import com.yz.model.graduate.BdStudentScoreInfo;
import com.yz.model.graduate.CheckDataBaseInfo;
import com.yz.model.graduate.EducationalTaskInfo;
import com.yz.model.graduate.FeeCheckDataInfo;
import com.yz.model.graduate.GraduateApplyQuery;
/**
 * 毕业审核
 * @author lx
 * @date 2017年7月13日 下午2:53:12
 */
@Service
@Transactional
public class GraduateCheckService {
	
	
	@Autowired
	private GraduateCheckMapper graduateCheckMapper;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryGraduateDataCheckByPage(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<CheckDataBaseInfo> graduateDatas = graduateCheckMapper.queryGraduateDataCheckByPage(query);
		return new IPageInfo((Page) graduateDatas);
	}
	public List<EducationalTaskInfo> getEducationalTaskInfo(String learnId){
		return graduateCheckMapper.getEducationalTaskInfo(learnId);
	}
	
	public void checkStatus(BdGraduateRecordsInfo recordInfo){
		graduateCheckMapper.checkStatus(recordInfo);
	}
	
	public BdStudentScoreInfo getBdStudentScoreInfo(String learnId,String stdId){
		BdStudentScoreInfo scoreInfo = new BdStudentScoreInfo();
		scoreInfo.setFirstSemester(graduateCheckMapper.queryScoreBaseInfo(learnId, stdId, "1"));
		scoreInfo.setSecondSemester(graduateCheckMapper.queryScoreBaseInfo(learnId, stdId, "2"));
		scoreInfo.setThirdSemester(graduateCheckMapper.queryScoreBaseInfo(learnId, stdId, "3"));
		scoreInfo.setFourthSemester(graduateCheckMapper.queryScoreBaseInfo(learnId, stdId, "4"));
		scoreInfo.setFifthSemester(graduateCheckMapper.queryScoreBaseInfo(learnId, stdId, "5"));
		scoreInfo.setSixthSemester(graduateCheckMapper.queryScoreBaseInfo(learnId, stdId, "6"));
		
		return scoreInfo;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo queryGraduatePaperCheckByPage(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<CheckDataBaseInfo> graduateDatas = graduateCheckMapper.queryGraduatePaperCheckByPage(query);
		return new IPageInfo((Page) graduateDatas);
	}
	
	public BdStudentPaperInfo getBdStudentPaperInfo(String learnId){
		return graduateCheckMapper.getBdStudentPaperInfo(learnId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryFeeCheckDataInfoByPage(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<FeeCheckDataInfo> graduateDatas = graduateCheckMapper.queryFeeCheckDataInfoByPage(query);
		return new IPageInfo((Page) graduateDatas);
	}
	
	public List<BdStdPayInfoResponse> selectPayInfo(String learnId){
		return graduateCheckMapper.selectPayInfo(learnId);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo queryBdGraduateApprovalInfo(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<BdGraduateApprovalInfo> approvalInfos = graduateCheckMapper.queryBdGraduateApprovalInfo(query);
		return new IPageInfo((Page) approvalInfos);
	}
	
	
	public BdCheckResultShowInfo getBdCheckResultShowInfo(String graduateId){
		BdCheckResultShowInfo showInfo = new BdCheckResultShowInfo();
		showInfo.setDataResultInfo(graduateCheckMapper.getBdCheckResultInfo(graduateId, "1"));
		showInfo.setPictureResultInfo(graduateCheckMapper.getBdCheckResultInfo(graduateId, "2"));
		showInfo.setPaperResultInfo(graduateCheckMapper.getBdCheckResultInfo(graduateId, "3"));
		showInfo.setScoreResultInfo(graduateCheckMapper.getBdCheckResultInfo(graduateId, "4"));
		showInfo.setFeeResultInfo(graduateCheckMapper.getBdCheckResultInfo(graduateId, "5"));
		return showInfo;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public IPageInfo queryGraduatePictureCheckByPage(int start, int length,GraduateApplyQuery query) {
		PageHelper.offsetPage(start, length);
		List<CheckDataBaseInfo> graduateDatas = graduateCheckMapper.queryGraduatePictureCheckByPage(query);
		return new IPageInfo((Page) graduateDatas);
	}
	
	public BdGraduateCollectInfo getBdGraduateCollectInfo(String learnId){
		return graduateCheckMapper.getBdGraduateCollectInfo(learnId);
	}
	
	public BdGraduateRecordsInfo getBdGraduateRecordsInfo(String checkId){
		return graduateCheckMapper.getBdGraduateRecordsInfo(checkId);
	}
	
	public void checkAffirm(String graduateId,String checkStatus,String remark,String learnId){
		graduateCheckMapper.checkAffirm(graduateId,checkStatus,remark);
		if(checkStatus.equals("3")){ //改变学员状态
			graduateCheckMapper.updateStdStageByLearnId(learnId);
		}
	}
	public String selectGrade(String learnId) {
		return graduateCheckMapper.selectGradeByLearnId(learnId);
	}
	
}
