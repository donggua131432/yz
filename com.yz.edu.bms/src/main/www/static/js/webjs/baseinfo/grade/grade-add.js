$(function() {
				_init_select("ext1", dictJson.recruitType);

				$('.skin-minimal input').iCheck({
					checkboxClass : 'icheckbox-blue',
					radioClass : 'iradio-blue',
					increaseArea : '20%'
				});
				
				$("#dictValue").change(function(){
					var id = $(this).val();
					$("#dictId").val('grade.' + id);
				});

				$("#form-grade").validate({
					rules : {
						dictName : {
							required : true
						},
						dictValue : {
							required : true,
							remote : { //验证用户名是否存在
                                type : "POST",
                                url : '/grade/validate.do', //servlet
                                data : {
                                    dictId : function() {
                                        return $("#dictId").val();
                                    }
                                }
                            }
						},
						ext1 : {
							required : true
						},
						orderNum :{
							required : true
						}
					},
					messages : {
						dictValue : {
                            remote : "年级信息已存在"
                        }
					},
					onkeyup : false,
					focusCleanup : true,
					success : "valid",
					submitHandler : function(form) {
						$(form).ajaxSubmit({
							type : "post", //提交方式  
							dataType : "json", //数据类型  
							url : '/grade/add.do', //请求url 
							success : function(data) { //提交成功的回调函数  
								if ('00' == data.code) {
                                    layer.msg('年级信息保存成功', {
                                        icon : 1,
                                        time : 1000
                                    }, function(){window.parent.myDataTable.fnDraw(true);layer_close();});
                                }
							}
						})
					}
				});
			});