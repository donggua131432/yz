$(function(){
	
	$("#orderNo").text(varorderInfo.orderNo);
	$("#orderStatus").text(varorderInfo.orderStatus);
	$("#userName").text(varorderInfo.userName);
	$("#orderTime").text(varorderInfo.orderTime);
	$("#goodsName").text(varorderInfo.goodsName);
	$("#transAmount").text(varorderInfo.transAmount);
	$("#startTime").text(varorderInfo.startTime);
	$("#address").text(varorderInfo.address);
	$("#endTime").text(varorderInfo.endTime);
	
	if(varorderInfo.memberList.length >0){
		var dom = '';
		for(var i=0;i<varorderInfo.memberList.length;i++){
			var mem = varorderInfo.memberList[i];
			dom += mem.name+':'+mem.mobile+'<br/>';
		}
		$("#memberList").html(dom);
	}
	
});