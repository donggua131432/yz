$(function() {
			$('.skin-minimal input').iCheck({
				checkboxClass : 'icheckbox-blue',
				radioClass : 'iradio-blue',
				increaseArea : '20%'
			});

			$("#form-member-add").validate({
				rules : {
					excelPath : {
						required : true,
					}
				},
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						url : '/msgPub/addStd.do', //请求url  
						success : function(data) { //提交成功的回调函数  
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('导入成功！', {icon : 1, time : 1000},function(){
									layer_close();
								});
							}
						}
					}); 
				}
			});
		});