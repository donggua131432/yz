$(function() {
				$("#stdName").text("stdName");
				$("#idCard").text("idCard");
				$("#stdStage").text(_findDict('stdStage',"learninfo.stdStage" ));
				$("#pfsnLevel").text(_findDict('pfsnLevel',"learninfo.pfsnLevel" ));
				//初始化学期
				_init_select("semester",dictJson.semester);
				
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
						
							url : "/studentTScore/updateStudentTScore.do",
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
				
				$("#semester").change(function () {
					var url = "/studentTScore/findStudentTScoreBySemester.do";
					//Ajax调用处理
					$.ajax({
						type : "POST",
						url : url,
						data : {
							learnId : $("#learnId").val(),
							semester : $("#semester").val()
						},
						dataType : 'json',
						success : function(data) {
							if(data.code == _GLOBAL_SUCCESS){
								//初始化数据
								var dom = '';
								var courses = data.body;;
								for (var i = 0; i < courses.length; i++) {
									course = courses[i];
									dom += '<tr class="text-c">';
									dom += '<td>'+ course.courseName + '<input type="hidden" name="scores['+ i +'].courseId" value="' + course.courseId + '" />';
									dom += '<input type="hidden" name="scores['+ i +'].courseName" value="' + course.courseName + '" />';
									dom += '</td>';
									dom += '<td>';
									var inputText = '<input type="number" class="input-text radius size-M" style="width:50px" name="scores[' + i + '].score" value="' + course.score + '" />';
									dom += inputText;
									dom += '</td>';
									
									dom += '<td>';
									var inputText = '<input type="number" class="input-text radius size-M" style="width:50px" name="scores[' + i + '].totalmark" value="' + course.totalmark + '" />';
									dom += inputText;
									dom += '</td>';
									
									dom += '<td>';
									var inputText = '<input type="text" class="input-text radius size-M" style="width:60px" name="scores[' + i + '].teacher" value="' + (course.teacher==null?"":course.teacher) + '" />';
									dom += '<input type="hidden" name="scores['+ i +'].teacherId" value="' + course.teacherId + '" />';
									dom += inputText;
									dom += '</td>';
									
									
									dom += '</tr>';
									
								}
								$("#course").html(dom);
									
							}
						}
					});
					
					
					
		        });
				
				
				$.each($("#course").find("input[type='number']"), function(i, data){
					$(data).rules('add', {required : true, range : [0,150], isStdScore : true});
				});
				
			});