package com.yz.dao.oa;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yz.model.oa.OaGroupInfo;

/**
 * 部门招生组
 * @author lx
 * @date 2017年6月30日 下午5:27:42
 */
public interface OaGroupMapper {

	/**
	 * 所有的部门招生组
	 */
	public List<OaGroupInfo> selectAllGroupInfo(OaGroupInfo groupInfo);
	
	/**
	 * 根据部门招生组名字和所在部门查询招生组数量
	 */
	public int selectCountByGroupName(@Param("groupName") String groupName,@Param("dpId") String dpId);
	
	/**
	 * 新增部门招生组
	 * @param groupInfo
	 * @return
	 */
	public int insertGroupInfo(OaGroupInfo groupInfo);
	
	/**
	 * 获取某个部门招生组的详细
	 * @param groupId
	 * @return
	 */
	public OaGroupInfo getOaGroupInfo(String groupId);
	
	/**
	 * 修改部门招生组信息
	 * @param groupInfo
	 */
	public void updateOaGroupInfo(OaGroupInfo groupInfo);
	
	/**
	 * 启用或者停用部门招生组
	 * @param groupInfo
	 */
	public void groupBlock(OaGroupInfo groupInfo);
	/**
	 * 某个部门下的所有招生组信息
	 * @param dpId
	 * @return
	 */
	public List<Map<String, String>> findAllListByDpId(String dpId);
}
