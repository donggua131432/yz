package com.yz.dao.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yz.model.goods.GsCommentReply;
import com.yz.model.goods.GsGoodsCommentInfo;

public interface GsGoodsCommentMapper {

	public List<GsGoodsCommentInfo> getGsGoodsCommentInfo(@Param("goodsName") String goodsName);
	
	public GsGoodsCommentInfo getGsGoodsCommentDetailInfo(String salesId);
	
	public void addGsCommentReply(GsCommentReply reply);
	
	public void deleteGsGoodsComment(String commentId);
	
	public void batchDeleteGsGoodsComment(@Param("ids") String[] ids);
	
	public void updateGoodsCommentStatus(@Param("commentStatus") String commentStatus,@Param("commentId") String commentId);
	
	public void batchPassComment(@Param("ids") String[] ids);
	
	public void batchNoPassComment(@Param("ids") String[] ids);
}
