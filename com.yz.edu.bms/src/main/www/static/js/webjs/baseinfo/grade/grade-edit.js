$(function() {
				_init_select("ext1", dictJson.recruitType, gInfo.ext1);
				$("#dictId").val(gInfo.dictId);
				$("#dictValue").val(gInfo.dictValue);
				$("#dictName").val(gInfo.dictName);
				$("#orderNum").val(gInfo.orderNum);
				$("#description").text(gInfo.description);
				
				var isEnable = gInfo.isEnable;
				
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
						},
						dictValue : {
							required : true,
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
							url : "/grade/update.do", //请求url 
							success : function(data) { //提交成功的回调函数  
								if ('00' == data.code) {
                                    layer.msg('年级信息保存成功', {
                                        icon : 1,
                                        time : 1000
                                    }, function(){window.parent.myDataTable.fnDraw(false);layer_close();});
                                }
							}
						})
					}
				});
			});