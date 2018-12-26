
$(function(){
	
	
	var url = '';
	
	if('ADD' == exType){
		url = '/wechatpub/add.do';
	}else if('UPDATE' == exType){
		url = '/wechatpub/edit.do';
	}
	
	
	_init_select("pubType", dictJson.pubType, wechat.pubType);
	
	$("#form-wechatpub-add").validate({
		rules : {
			pubId : {
				required : true
			},
			pubName : {
				required : true
			},
			pubType : {
				required : true
			},
			appId : {
				required : true
			},
			appSecret : {
				required : true
			}
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