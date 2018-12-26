$(function(){
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			
			
            var wish = '';
            wish += stdName +'   [' + grade + '级]';
            $("#stdName").text(wish);
            var txt = '';
            txt +=unvsName + ':(' + pfsnCode + ')'+pfsnName ;
            txt += '[' + _findDict('pfsnLevel', pfsnLevel) + ']';
            $("#unvsInfo").text(txt);
            $("#schoolRoll").text(schoolRoll);
            $("#stdNo").text(stdNo);
            $("#idCard").text(idCard);
            $("#stdStage").text(_findDict('stdStage', stdStage));  
            $("#tutorName").text(tutor);
            $("#logisticsNo").text(logisticsNo);
            $("#remark").text(remark);
           // $("#submitTime").text(submitTime);
            $("#submitTime").text(submitTime==null?'':new Date(submitTime).format("yyyy-MM-dd hh:mm:ss"));
            
            var temp='';
            if(isReceiveBook=='0'){
            	temp="否";
            }else if(isReceiveBook=='1'){
            	temp="是";
            }else{
            	temp="";
            }
            $("#isReceiveBook").text(temp);
            var temp='';
            if(isKnowTimetables=='0'){
            	temp="否";
            }else if(isKnowTimetables=='1'){
            	temp="是";
            }else{
            	temp="";
            }
            $("#isKnowTimetables").text(temp);
            var temp='';
            if(isKnowCourseType=='0'){
            	temp="否";
            }else if(isKnowCourseType=='1'){
            	temp="是";
            }else{
            	temp="";
            }
            $("#isKnowCourseType").text(temp);

            
            var url = '/lecture/editStuLectureRemark.do';
			$("#form-degree-edit").validate({
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式
						dataType : "json", //数据类型
						url : url, //请求url
						success : function(data) { //提交成功的回调函数
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('操作成功！', {icon : 1, time : 1000},function(){
									window.parent.myDataTable.fnDraw(false);
									layer_close();
								});
							}
						}
					})
				}
			});
			
			$("#admin-role-reset").click(function () {
                var url = '/lecture/resetTask.do';
                layer.confirm("是否确定重置任务",function () {
                    $.ajax({
                        type : "post", //提交方式
                        dataType : "json", //数据类型
                        url : url, //请求url
						data:{ lectureId:$("#lectureId").val(),taskId:$("#taskId").val(),learnId:$("#learnId").val()},
                        success : function(data) { //提交成功的回调函数
                            if(data.code == _GLOBAL_SUCCESS){
                                layer.msg('操作成功！', {icon : 1, time : 1000},function(){
                                    window.parent.myDataTable.fnDraw(false);
                                    layer_close();
                                });
                            }
                        }
                    })
                })
            });
			
			$("#admin-role-close").click(function () {
				window.parent.myDataTable.fnDraw(false);
                layer_close();
            })
		});