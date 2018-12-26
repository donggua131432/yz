package com.yz.dao.graduate;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.finance.stdfee.BdStdPayInfoResponse;
import com.yz.model.graduate.BdCheckResultInfo;
import com.yz.model.graduate.BdGraduateApprovalInfo;
import com.yz.model.graduate.BdGraduateCollectInfo;
import com.yz.model.graduate.BdGraduateRecordsInfo;
import com.yz.model.graduate.BdStudentPaperInfo;
import com.yz.model.graduate.CheckDataBaseInfo;
import com.yz.model.graduate.EducationalTaskInfo;
import com.yz.model.graduate.FeeCheckDataInfo;
import com.yz.model.graduate.GraduateApplyInfo;
import com.yz.model.graduate.GraduateApplyQuery;
import com.yz.model.graduate.GraduateStudentInfo;
import com.yz.model.graduate.ScoreBaseInfo;

/**
 * 毕业审核
 * @author lx
 * @date 2017年7月12日 下午12:32:11
 */
public interface GraduateCheckMapper {

	/********1.毕业资料核查 start**********/
    public List<CheckDataBaseInfo> queryGraduateDataCheckByPage(GraduateApplyQuery query);
	
	public void deleteGraduate(@Param("ids") String[] ids);
	
	public List<EducationalTaskInfo> getEducationalTaskInfo(@Param("learnId") String learnId);
	
	public void checkStatus(BdGraduateRecordsInfo recordInfo);
	
	/********1.毕业资料核查 end**********/
	
	/********2.毕业图像核查 start**********/
	public List<CheckDataBaseInfo> queryGraduatePictureCheckByPage(GraduateApplyQuery query);
	public BdGraduateCollectInfo getBdGraduateCollectInfo(@Param("learnId") String learnId);
	/********2.毕业图像核查 end**********/
	
	/********3.期末成绩核查 start**********/
	public List<ScoreBaseInfo> queryScoreBaseInfo(@Param("learnId") String learnId,@Param("stdId") String stdId,@Param("semester") String semester);
	/********3.期末成绩核查 end**********/
	
	/********4.论文成绩核查 start**********/
	public List<CheckDataBaseInfo> queryGraduatePaperCheckByPage(GraduateApplyQuery query);
	public BdStudentPaperInfo getBdStudentPaperInfo(@Param("learnId") String learnId);
	/********4.论文成绩核查 end**********/
	
	/********5.毕业学费清缴 start**********/
	public List<FeeCheckDataInfo> queryFeeCheckDataInfoByPage(GraduateApplyQuery query);
	public List<BdStdPayInfoResponse> selectPayInfo(@Param("learn_id") String learnId);
	/********5.毕业学费清缴 end**********/
	
	
	//毕业核准
	public List<BdGraduateApprovalInfo> queryBdGraduateApprovalInfo(GraduateApplyQuery query);
	
	public BdCheckResultInfo getBdCheckResultInfo(@Param("graduateId") String graduateId, @Param("gCheckType") String gCheckType);
	
	public BdGraduateRecordsInfo getBdGraduateRecordsInfo(@Param("checkId") String checkId);
	
	public void checkAffirm(@Param("graduateId") String graduateId,@Param("checkStatus") String checkStatus,@Param("remark") String remark);
	
	public void updateStdStageByLearnId(@Param("learnId") String learnId);

	public String selectGradeByLearnId(String learnId);
	
	
	
	
}
