$(function() {
			$('.skin-minimal input').iCheck({
				checkboxClass : 'icheckbox-blue',
				radioClass : 'iradio-blue',
				increaseArea : '20%'
			});

			$("#form-stu-add").validate({
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
						timeout :300000,
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						url : '/task/upload.do', //请求url 
						success : function(data) { //提交成功的回调函数  
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('操作成功！', {icon : 1, time : 3000},function(){
									layer_close();
								});
							}
						}
					}); 
				}
			});
		});