$(function(){
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			
			_init_select("isExam",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			],dataInfo.isExam );
			
			
            var wish = '';
            wish += dataInfo.stdName +'   [' + dataInfo.grade + '级]';
            $("#stdName").text(wish);
            var txt = '';
            txt +=dataInfo.unvsName + ':(' + dataInfo.pfsnCode + ')'+dataInfo.pfsnName ;
            txt += '[' + _findDict('pfsnLevel', dataInfo.pfsnLevel) + ']';
            $("#unvsInfo").text(txt);
            $("#schoolRoll").text(dataInfo.schoolRoll||'');
            $("#tutorName").text(dataInfo.tutor||'');
            $("#reason").text(dataInfo.reason||'');
            
            var temp='';
            if(dataInfo.ifAffirm=='1'){
            	temp="是";
            }else {
            	temp="否";
            }
            $("#isAffirm").text(temp);
            
            
            
            var url = '/studentCityAffirmGK/editStuCityAffirmReason.do';
			$("#form-cityAffirm-edit").validate({
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
                var url = '/studentCityAffirmGK/resetTask.do';
                layer.confirm("是否确定重置任务",function () {
                    $.ajax({
                        type : "post", //提交方式
                        dataType : "json", //数据类型
                        url : url, //请求url
						data:{ affirmId:$("#affirmId").val(),taskId:$("#taskId").val(),learnId:$("#learnId").val()},
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