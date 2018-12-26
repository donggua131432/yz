$(function() {
				$("#stdName").text("stdName");
				$("#idCard").text("idCard");
				$("#form-member-add").validate({
					rules : {
						
					},
					onkeyup : false,
					focusCleanup : true,
					success : "valid",
					submitHandler : function(form) {
						
						
						$(form).ajaxSubmit({
						   /*var reg =  new RegExp(/^\d{1,3}||^\d{1,3}(\.\d){1}?$/);
							var num = 1201.132123;
							*/
						
							url : "/studentScore/updateStudentScore.do",
							type : "POST",
							dataType : "json",
							success : function(data) {
								if(data.code == _GLOBAL_SUCCESS){
									layer.msg('操作成功！', {icon : 1, time : 1000},function(){
										layer_close();
									});
								}
							}
						});
					}
				});
				
				
				//初始化数据
				var dom = '';
				for (var i = 0; i < courses.length; i++) {
					var course = courses[i];
					dom += '<tr class="text-c">';
					dom += '<td>'+ course.subjectName + '<input type="hidden" name="scores['+ i +'].courseId" value="' + course.subjectId + '" />';
					dom += '<input type="hidden" name="scores['+ i +'].courseName" value="' + course.subjectName + '" />';
					dom += '</td>';
					dom += '<td>';
					
					var inputText = '<input type="number" class="input-text radius size-M" style="width:120px" name="scores[' + i + '].score" />';
					dom += inputText;
					dom += '</td>';
					dom += '</tr>';
					
				}
				$("#course").append(dom);
				$.each($("#course").find("input[type='number']"), function(i, data){
					$(data).rules('add', {required : true, range : [0,150], isStdScore : true});
				});
				
			});