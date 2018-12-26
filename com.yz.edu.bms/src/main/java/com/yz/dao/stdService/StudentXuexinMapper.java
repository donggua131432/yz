package com.yz.dao.stdService;

import com.yz.model.admin.BaseUser;
import com.yz.model.stdService.StudentXuexinInfo;
import com.yz.model.stdService.StudentXuexinQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentXuexinMapper {
    List<StudentXuexinInfo> findAllXuexinList(@Param("queryInfo") StudentXuexinQuery query, @Param("user") BaseUser user);

    int updateRemark(@Param("xuexinId") String xuexinId, @Param("remark") String remark);

    int resetTask(@Param("xuexinId") String xuexinId, @Param("taskId") String taskId, @Param("learnId") String learnId);

    List<Map<String, Object>> getNonExistsStudent(@Param("studentXuexinInfoList")List<StudentXuexinInfo> studentXuexinInfoList);

    void insert(@Param("studentXuexinInfoList")List<StudentXuexinInfo> studentXuexinInfoList,@Param("user") BaseUser user);
    
    public void singleAddStuXueXinInfo(StudentXuexinInfo xuexinInfo);
    
	public void aloneDelStuXueXinInfo(@Param("learnId") String learnId,@Param("taskId") String taskId);
	
	public void addStuXueXinInfo(@Param("list") List<StudentXuexinInfo> list);
	
	public void delStuXueXinInfo(@Param("ids") String[] ids,@Param("taskId") String taskId);
}
