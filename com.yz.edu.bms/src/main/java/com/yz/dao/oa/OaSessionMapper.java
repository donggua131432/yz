package com.yz.dao.oa;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.admin.SessionCampusInfo;
import com.yz.model.admin.SessionDpInfo;
import com.yz.model.admin.SessionEmpInfo;
import com.yz.model.admin.SessionGroupInfo;

public interface OaSessionMapper {

	List<SessionCampusInfo> getCampusList(String empId);

	List<SessionDpInfo> getDepartmentList(String empId);

	List<SessionDpInfo> getSubDpList(@Param("dpList") List<SessionDpInfo> dpList);

	List<SessionGroupInfo> getGroupList(String empId);

	List<SessionEmpInfo> getEmployeeList(@Param("groupList") List<SessionGroupInfo> groupList);

	List<String> getJtList(String empId);

}
