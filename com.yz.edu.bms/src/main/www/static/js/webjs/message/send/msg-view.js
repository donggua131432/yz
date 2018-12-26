$(function(){
	var exType = $("#exType").val();
	
	
	var mtpType = msg.mtpType;
	var msgChannel = msg.msgChannel;
	
	$("#msgChannelDiv").html('<span>'+_findDict("msgChannel",msgChannel)+'</span>');
	$("#mtpTypeDiv").html('<span>'+_findDict("mtpType",mtpType)+'</span>');
	url = '/msgPub/check.do';
	_init_select("mtpType", dictJson.mtpType, null);
	_init_select("msgChannel", dictJson.msgChannel, null);
	
	
});