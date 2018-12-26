package com.yz.dao.oa;

import java.util.List;

import com.yz.model.oa.RecruiterInfo;

/**
 * 招生老师管理
 * @author lx
 * @date 2017年7月3日 下午12:08:28
 */
public interface RecruiterMapper {

	/**
	 * 所有的招生老师
	 */
	public List<RecruiterInfo> selectAllRecruiterInfo(RecruiterInfo recruiterInfo);
}
