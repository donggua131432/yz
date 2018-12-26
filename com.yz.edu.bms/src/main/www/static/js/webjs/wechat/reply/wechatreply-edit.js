
function wechatOnChange(){
	var msgType = $("#msgType").val();
	if('text' == msgType){
		$("#textBox").css("display", "block");
		$("#newsBox").css("display", "none");
	}else if('news' == msgType){
		$("#textBox").css("display", "none");
		$("#newsBox").css("display", "block");
	}
	
}

$(function(){
	
	var exType = $("#exType").val();
	var url = '';
	
	_init_select("msgType",dictJson.msgType,reply.msgType);
	
	
	
	_simple_ajax_select({
		selectId : "wechatId",
		searchUrl : '/wechatmenu/sPublic.do',
		sData : {
			
		},
		showText : function(item) {
			return item.pubName;
		},
		showId : function(item) {
			return item.pubId;
		},
		placeholder : '--请选择公众号--'
	});
	
	
	if('ADD' == exType){
		url = '/wechatreply/add.do';
		$("#textBox").css("display", "none");
		$("#newsBox").css("display", "none");
		
		
	}else if('UPDATE' == exType){
		
		url = '/wechatreply/edit.do';
		
		if('text' == reply.msgType){
			$("#textBox").css("display", "block");
			$("#newsBox").css("display", "none");
			
		}else if('news' == reply.msgType){
			$("#textBox").css("display", "none");
			$("#newsBox").css("display", "block");
		}
		
		
	}
	
	$("#form-wechatreply-add").validate({
		rules : {
			
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