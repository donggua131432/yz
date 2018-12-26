$(function(){
	var exType = $("#exType").val();
	var url = '';
	if('ADD' == exType){
		url = '/msgPub/add.do';
	}else if('UPDATE' == exType){
		
		
		var mtpType = msg.mtpType;
		var msgChannel = msg.msgChannel;
		
		$("#msgChannelDiv").html('<span>'+_findDict("msgChannel",msgChannel)+'</span>');
		$("#mtpTypeDiv").html('<span>'+_findDict("mtpType",mtpType)+'</span>');
		url = '/msgPub/edit.do';
	}
	_init_select("mtpType", dictJson.mtpType, null);
	_init_select("msgChannel", dictJson.msgChannel, null);
	
	$("#form-charges-add").validate({
		rules : {
			mtpType : {
				required : true
			},
			msgChannel : {
				required : true
			},
			sendTime : {
				required : true
			},
			sender : {
				required : true
			},
			msgTitle : {
				required : true
			},
			msgName : {
				required : true
			},
			msgCode : {
				required : true
			},
			msgContent : {
				required : true
			}
		},
		messages : {
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : url, //请求url  
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 1000},function(){
							layer_close();
						});
					}
				}
			})
		}
	});
});