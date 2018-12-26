package com.yz.service.enroll;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.core.security.manager.SessionUtil;
import com.yz.dao.enroll.BdSceneConfirmMapper;
import com.yz.model.admin.BaseUser;
import com.yz.model.common.IPageInfo;
import com.yz.model.enroll.BdSceneConfirmQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @描述: 学员现场确认
 * @作者: DuKai
 * @创建时间: 2017/10/19 16:29
 * @版本号: V1.0
 */

@Service
@Transactional
public class BdSceneConfirmServiceImpl {
    @Autowired
    BdSceneConfirmMapper bdSceneConfirmMapper;


    /**
     * 分页现场确认学员信息
     * @param confirmQuery
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public IPageInfo querySceneConfirmStdPage(BdSceneConfirmQuery confirmQuery) {
        BaseUser user = SessionUtil.getUser();
        PageHelper.offsetPage(confirmQuery.getStart(), confirmQuery.getLength());
        List<Map<String,Object>> result = bdSceneConfirmMapper.findSceneConfirmStdPage(confirmQuery,user);
        return new IPageInfo((Page) result);
    }

    public Map<String, Object> querySceneConfirmStd(String stdId, String learnId) {
        Map<String,Object> result = bdSceneConfirmMapper.findSceneConfirmStd(stdId,learnId);
        return result;
    }

    public Map<String, Object> querySceneConfirm(String stdId, String learnId) {
        return bdSceneConfirmMapper.findSceneConfirm(stdId,learnId);
    }

    public void addSceneConfirm(Map map) {
        bdSceneConfirmMapper.addSceneConfirm(map);
    }

    public void updateSceneConfirm(Map map) {
        bdSceneConfirmMapper.updateSceneConfirm(map);
    }

    public List<Map<String, Object>> querySceneConfirmAllList(){
        return bdSceneConfirmMapper.selectSceneConfirmAllList();
    }

    public void updateTestProveUrl(Map map){
        bdSceneConfirmMapper.updateTestProveUrl(map);
    }


    public List<Map<String, Object>> queryDownloadProveList(){
        return bdSceneConfirmMapper.selectDownloadProveList();
    }

    public void addTestProveInfo(Map map){
        bdSceneConfirmMapper.insertTestProveInfo(map);
    }


}
