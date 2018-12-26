package com.yz.dao.invite;

import com.yz.model.invite.UsStudentRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsStudentRemarkMapper {

    List<UsStudentRemark> selectUsStudentRemark(@Param(value = "userId") String userId, @Param(value = "stdId") String stdId);

    int insertUsStudentRemark(UsStudentRemark usStudentRemark);

    int updateUsStudentRemark(UsStudentRemark usStudentRemark);

    int deleteUsStudentRemarkById(@Param(value = "userId") String userId, @Param(value = "stdId") String stdId);
}