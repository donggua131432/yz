$(function(){
	
	$("#orderNo").text(varorderInfo.orderNo);
	$("#orderStatus").text(varorderInfo.orderStatus);
	$("#userName").text(varorderInfo.userName);
	$("#orderTime").text(varorderInfo.orderTime);
	$("#goodsName").text(varorderInfo.goodsName);
	$("#unitPrice").text(varorderInfo.unitPrice);
	$("#goodsCount").text(varorderInfo.goodsCount);
	$("#transAmount").text(varorderInfo.transAmount);
	$("#takeUserName").text(varorderInfo.takeUserName);
	$("#address").text(varorderInfo.address);
	$("#takeMobile").text(varorderInfo.takeMobile);
	
});
/*管理员-停用*/
function updateOrderStatus(){
	layer.confirm('确认要发货吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/order/updateOrderStatus.do',
			data : {
				orderNo : $("#orderNos").val()
			},
			dataType : 'json',
			success : function(data) {
				layer.msg('已发货!',{icon: 5,time:1000});
			},
			error : function(data) {
				layer.msg('发货失败！', {
					icon : 1,
					time : 1000
				});
			},
		});
	});
}

function updateOrderLogisticsInfo(){
	$.ajax({
		type : 'POST',
		url : '/order/updateOrderLogisticsInfo.do',
		data : {
			logisticsId : $("#logisticsId").val(),
			transportNo : $("#transportNo").val(),
			logisticsName : $("logisticsName").val()
		},
		dataType : 'json',
		success : function(data) {
			layer.msg('保存成功!',{icon: 5,time:1000});
		},
		error : function(data) {
			layer.msg('保存失败！', {
				icon : 1,
				time : 1000
			});
		},
	});
}