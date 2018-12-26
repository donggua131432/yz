$(function() {
			$('.skin-minimal input').iCheck({
				checkboxClass : 'icheckbox-blue',
				radioClass : 'iradio-blue',
				increaseArea : '20%'
			});

			$("#form-member-add").validate({
				rules : {
					errorCode : {
						remote : { //验证用户名是否存在
							type : "POST",
							url : '/sysErrorMessage/validate.do', //servlet
							data : {
								exType : function() {
									return $("#exType").val();
								},
								errorCode : function() {
									return $("#errorCode").val();
								}
							}
						},
						required : true
					},
					sysMsg : {
						required : true,
					},
					appMsg : {
						required : true,
					}

				},
				messages : {
					errorCode : {
						remote : "参数名称已存在"
					}
				},
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						url : '/sysErrorMessage/editSysErrorMessage.do', //请求url  
						success : function(data) { //提交成功的回调函数  
							layer_close();
						},
						error : function(data) {
							layer.msg('操作失败', {
								icon : 1,
								time : 1000
							});
							layer_close();
						}
					})
				}
			});
		});