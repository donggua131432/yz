package com.yz.controller.goods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yz.core.security.annotation.Log;
import com.yz.core.security.annotation.Rule;
import com.yz.core.security.manager.SessionUtil;
import com.yz.core.util.RequestUtil;
import com.yz.model.admin.BaseUser;
import com.yz.model.goods.GsCommentReply;
import com.yz.model.goods.GsGoodsCommentInfo;
import com.yz.service.goods.GsGoodsCommentService;
import com.yz.util.Assert;
/**
 * 运营管理--商品评论
 * @author lx
 * @date 2017年8月18日 上午11:53:39
 */
@Controller
@RequestMapping("/comment")
public class GsGoodsCommentController {

	@Autowired
	private GsGoodsCommentService gsGoodsCommentService;
	
	/**
	 * 跳转到评论页面
	 * @return
	 */
	@RequestMapping("/toList")
	@Rule("comment:query")
	public String toList() {
		return "/goods/goods_comment_list";
	}
	/**
	 * 分页商品评论数据
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@Rule("comment:query")
	@ResponseBody
	public Object goodsCommentList(@RequestParam(name = "start", defaultValue = "0") int start,
			@RequestParam(name = "length", defaultValue = "10") int length,
			@RequestParam(name = "goodsName",defaultValue= "") String goodsName) {
		return gsGoodsCommentService.getGsGoodsCommentInfo(start, length,goodsName);
	}
	
	/**
	 * 跳转到评论回复或者查看页面
	 * @param exType
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toCommentEdit")
	@Rule("comment:insert")
	public String toCommentEdit(@RequestParam(name = "exType", required = true) String exType, HttpServletRequest request,
			Model model) {
		GsGoodsCommentInfo commentInfo = new GsGoodsCommentInfo();
		if ("LOOK".equals(exType.trim().toUpperCase()) || "UPDATE".equals(exType.trim().toUpperCase())) {
			String commentId = RequestUtil.getString("commentId");
			Assert.hasText(commentId, "参数名称不能为空");
			//获取信息
			commentInfo =gsGoodsCommentService.getGsGoodsCommentDetailInfo(commentId);
		}
		model.addAttribute("commentInfo", commentInfo);
		model.addAttribute("exType", exType);
		return "/goods/comment_reply_edit";
	}
	/**
	 * 回复评论
	 * @param exType
	 * @param replyInfo
	 * @return
	 */
	@RequestMapping("/commentReply")
	@Rule("comment:insert")
	@Log
	@ResponseBody
	public Object commentReply(@RequestParam(name = "exType", required = true) String exType,GsCommentReply replyInfo) {
		BaseUser user = SessionUtil.getUser();
		//回复
		replyInfo.setUserId(user.getEmpId());
		replyInfo.setUserName(user.getUsername());
		
		gsGoodsCommentService.addGsCommentReply(replyInfo);
		return "success";
	}
	/**
	 * 单个删除评论
	 * @param commentId
	 * @return
	 */
	@RequestMapping("/singleDeleteComment")
	@Rule("comment:singleDel")
	@ResponseBody
	public Object singleDeleteComment(@RequestParam(name = "commentId", required = true) String commentId) {
		gsGoodsCommentService.singleDeleteGsGoodsComment(commentId);
		return "success";
	}
	/**
	 * 批量删除评论
	 * @param commentIds
	 * @return
	 */
	@RequestMapping("/batchDeleteComment")
	@Rule("comment:batchDel")
	@ResponseBody
	public Object batchDeleteComment(@RequestParam(name = "commentIds[]", required = true) String[] commentIds) {
		gsGoodsCommentService.batchDeleteGsGoodsComment(commentIds);
		return "success";
	}
	
	/**
	 * 批量通过
	 * @param commentIds
	 * @return
	 */
	@RequestMapping("/batchPassComment")
	@Rule("comment:batchPass")
	@ResponseBody
	public Object batchPassComment(@RequestParam(name = "commentIds[]", required = true) String[] commentIds) {
		gsGoodsCommentService.batchPassComment(commentIds);
		return "success";
	}
	/**
	 * 批量不通过
	 * @param commentIds
	 * @return
	 */
	@RequestMapping("/batchNoPassComment")
	@Rule("comment:batchNoPass")
	@ResponseBody
	public Object batchNoPassComment(@RequestParam(name = "commentIds[]", required = true) String[] commentIds) {
		gsGoodsCommentService.batchNoPassComment(commentIds);
		return "success";
	}
	/**
	 * 单个修改评论状态
	 * @param commentId
	 * @param commentStatus
	 * @return
	 */
	@RequestMapping("/singleUpdateCommentStatus")
	@Rule("comment:singleUpdate")
	@ResponseBody
	public Object singleUpdateCommentStatus(@RequestParam(name = "commentId", required = true) String commentId,
			@RequestParam(name = "commentStatus", required = true) String commentStatus) {
		gsGoodsCommentService.updateGoodsCommentStatus(commentStatus, commentId);
		return "success";
	}
}
