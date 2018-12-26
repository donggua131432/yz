$(function() {
				 if(idArray)
				 for (var i = 0; i < idArray.length; i++) {
					 $("#form-member-add").append('<input type="hidden" value="'+idArray[i]+'" name="idArray"/>');
				 }
				 //教师
					$("#empId").select2({
						placeholder : "请选择教师",
						allowClear : true
					});
				//校区
				_simple_ajax_select({
					selectId : "campusId",
					searchUrl : "/classPlan/findCampusByName.do",
					sData : {},
					showText : function(item) {
						return item.campusName;
					},
					showId : function(item) {
						return item.campusId;
					},
					placeholder : '请选择校区'
				});
				
				$("#campusId").change(function(){
				  //教师
					_simple_ajax_select({
						selectId : "empId",
						searchUrl : "/classPlan/findEmployeeByName.do",
						sData : {campusId:$("#campusId").val()},
						showText : function(item) {
							return item.empName+"--->"+(null==item.empMobile?"无":item.empMobile);
						},
						showId : function(item) {
							return item.empId;
						},
						placeholder : '请选择教师'
					});
				});
				
				$('.skin-minimal input').iCheck({
					checkboxClass : 'icheckbox-blue',
					radioClass : 'iradio-blue',
					increaseArea : '20%'
				});

				$("#form-member-add").validate({
					rules : {
						courseName : {
							required : true,
						},
						courseType : {
							required : true,
						},
						year : {
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
						var urls  = "/classPlan/distributionTeacher.do";
						$(form).ajaxSubmit({
							type : "post", //提交方式  
							dataType : "json", //数据类型  
							url : urls, //请求url 
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

			function del(e) {
				$(e).parent().remove();
			}