package com.yz.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yz.dao.system.SysDictMapper;
import com.yz.model.system.SysDict;

/**
 * 
 * Description: 字典业务
 * 
 * @Author: 倪宇鹏
 * @Version: 1.0
 * @Create Date: 2017年5月9日.
 *
 */
@Service
public class SysDictService {
	
	@Value("${cacheFile.dict}")
	public String filePath;

	@Autowired
	private SysDictMapper dictMapper;

	/**
	 * 查询字典
	 * 
	 * @param groupId
	 *            分类ID
	 * @return
	 */
	public List<SysDict> getDicts(String groupId) {
		return dictMapper.selectByPid(groupId);
	}

	/**
	 * 查询字典
	 * 
	 * @param dictId
	 *            字典ID
	 * @return
	 */
	public SysDict getDict(String dictId) {
		return dictMapper.selectByPrimaryKey(dictId);
	}

	/**
	 * 修改
	 * 
	 * @param dict
	 */
	public void updateDict(SysDict dict) {
		dictMapper.updateByPrimaryKeySelective(dict);
	}

	/**
	 * 删除
	 * 
	 * @param dictId
	 */
	public void deleteDict(String dictId) {
		dictMapper.delete(dictId);
	}

	/**
	 * 增加
	 * 
	 * @param dict
	 */
	public void addDict(SysDict dict) {
		dictMapper.insertSelective(dict);
	}

	/**
	 * 或者群组IDS
	 * 
	 * @return
	 */
	public List<String> getGroupIds() {
		return dictMapper.getGroupIds();
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<SysDict> getAll() {
		return dictMapper.selectAll();
	}

	/**
	 * 获取所有（不包括父级）
	 * 
	 * @return
	 */
	public List<SysDict> getAllButParent() {
		return dictMapper.selectAllButParent();
	}

	/**
	 * 或者字典集合
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Map<String, String>> getDictsMap(String groupId) {
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		List<SysDict> list = this.getDicts(groupId);
		if (null != list && !list.isEmpty()) {
			for (SysDict sysDict : list) {
				map = new HashMap<String, String>();
				map.put("dictId", sysDict.getDictId());
				map.put("dictValue", sysDict.getDictValue());
				map.put("dictName", sysDict.getDictName());
				resultList.add(map);
			}

		}
		return resultList;
	}


	/**
	 * 逻辑删除记录
	 * 
	 * @param dictMaps
	 * @return
	 */
	public void deleteAllSysDict(String[] ids) {
		// TODO Auto-generated method stub
		dictMapper.deleteAllSysDict(ids);
	}

	/**
	 * 查询所有符合记录
	 * 
	 * @param dictMaps
	 * @return
	 */
	public List<SysDict> selectAll(SysDict sysDict) {
		return dictMapper.selectAllBySysDict(sysDict);
	}

	public List<SysDict> getParents(String sName) {
		return dictMapper.getParents(sName);
	}


}
