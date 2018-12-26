$(function(){
	$("#form-change-password").validate({
		rules:{
			newpassword:{
				required:true,
				minlength:6,
				maxlength:16
			},
			newpassword2:{
				required:true,
				minlength:6,
				maxlength:16,
				equalTo: "#newpassword"
			},
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			$(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : '/user/editPwd.do', //请求url
				success : function(data) { //提交成功的回调函数
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('修改成功！', {icon : 1, time : 1000},function(){
							layer_close();
						});
					}
				}
			})
		}
	});
});