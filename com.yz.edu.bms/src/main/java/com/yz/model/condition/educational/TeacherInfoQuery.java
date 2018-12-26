package com.yz.model.condition.educational;

import com.yz.model.common.PageCondition;
/**
 * 教师管理-查询条件
 * @author lx
 * @date 2017年7月11日 下午2:33:39
 */
public class TeacherInfoQuery extends PageCondition {
	
	    private String campusId;
	    private String empName;
	    private String finishMajor;
	    private String teach;
	    private String mobile;
	    private String workPlace;
		public String getCampusId() {
			return campusId;
		}
		public void setCampusId(String campusId) {
			this.campusId = campusId;
		}
		public String getEmpName() {
			return empName;
		}
		public void setEmpName(String empName) {
			this.empName = empName;
		}
		public String getFinishMajor() {
			return finishMajor;
		}
		public void setFinishMajor(String finishMajor) {
			this.finishMajor = finishMajor;
		}
		public String getTeach() {
			return teach;
		}
		public void setTeach(String teach) {
			this.teach = teach;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getWorkPlace() {
			return workPlace;
		}
		public void setWorkPlace(String workPlace) {
			this.workPlace = workPlace;
		}
}
