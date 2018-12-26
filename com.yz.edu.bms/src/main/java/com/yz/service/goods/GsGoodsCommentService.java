package com.yz.service.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.edu.paging.bean.Page;
import com.yz.edu.paging.common.PageHelper;
import com.yz.dao.goods.GsGoodsCommentMapper;
import com.yz.generator.IDGenerator;
import com.yz.model.common.IPageInfo;
import com.yz.model.goods.GsCommentReply;
import com.yz.model.goods.GsGoodsCommentInfo;

@Service
@Transactional
public class GsGoodsCommentService {

	@Autowired
	private GsGoodsCommentMapper gsGoodsCommentMapper;
	
	public IPageInfo<GsGoodsCommentInfo> getGsGoodsCommentInfo(int start, int length,String goodsName) {
		PageHelper.offsetPage(start, length);
		List<GsGoodsCommentInfo> resultList = gsGoodsCommentMapper.getGsGoodsCommentInfo(goodsName);
		return new IPageInfo<GsGoodsCommentInfo>((Page<GsGoodsCommentInfo>) resultList);
	}
    public GsGoodsCommentInfo getGsGoodsCommentDetailInfo(String commentId){
    	return gsGoodsCommentMapper.getGsGoodsCommentDetailInfo(commentId);
    }
	
	public void addGsCommentReply(GsCommentReply reply){
		reply.setReplyId(IDGenerator.generatorId());
		gsGoodsCommentMapper.addGsCommentReply(reply);
	}
	
	public void singleDeleteGsGoodsComment(String commentId){
		gsGoodsCommentMapper.deleteGsGoodsComment(commentId);
	}
	
	public void batchDeleteGsGoodsComment(String[] idArray){
		gsGoodsCommentMapper.batchDeleteGsGoodsComment(idArray);
	}
	
	public void updateGoodsCommentStatus(String commentStatus,String commentId){
		gsGoodsCommentMapper.updateGoodsCommentStatus(commentStatus, commentId);
	}
	
	public void batchPassComment(String[] idArray){
		gsGoodsCommentMapper.batchPassComment(idArray);
	}
	
	public void batchNoPassComment(String[] idArray){
		gsGoodsCommentMapper.batchNoPassComment(idArray);
	}
}
