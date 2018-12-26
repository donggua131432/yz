$(function() {
				
			
				
				_init_select("ext1", dictJson.sg, sInfo.ext1);
				
				$("#dictId").val(sInfo.dictId);
				$("#dictValue").val(sInfo.dictValue);
				$("#dictName").val(sInfo.dictName);
				$("#orderNum").val(sInfo.orderNum);
				$("#description").val(sInfo.description);
				
				var isEnable = sInfo.isEnable;
				
				$("#isEnable-" + isEnable).prop("checked", "checked");

				$('.skin-minimal input').iCheck({
					checkboxClass : 'icheckbox-blue',
					radioClass : 'iradio-blue',
					increaseArea : '20%'
				});

				$("#form-grade").validate({
					rules : {
						dictName : {
							required : true,
							remote : { //验证用户名是否存在
                                type : "POST",
                                url : "/scholarship/validate.do",
                                data : {
                                    dictName : function() {
                                        return $("#dictName").val();
                                    },
                                    pId : sInfo.pId,
                                    oldName : sInfo.dictName
                                }
                            }
						},
						ext1 : {
							required : true
						},
                        orderNum : {
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
							url : "/scholarship/update.do", //请求url 
							success : function(data) { //提交成功的回调函数  
								if ('00' == data.code) {
                                    layer.msg('优惠类型信息保存成功', {
                                        icon : 1,
                                        time : 1000
                                    }, function(){layer_close();});
                                }
							}
						})
					}
				});
			});