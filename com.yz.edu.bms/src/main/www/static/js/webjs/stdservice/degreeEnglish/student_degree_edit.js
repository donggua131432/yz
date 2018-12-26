$(function(){
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			
			_init_select("isSceneConfirm",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			],isSceneConfirm );
			
			
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
            $("#isEnroll").text(_findDict('enrollresult', isEnroll));
            $("#enrollNo").text(enrollNo);
            $("#tutorName").text(tutor);
            $("#remark").text(remark);
            $("#score").text(score);
            
            
            var url = '/degree/editStuDegreeRemark.do';
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
                var url = '/degree/resetTask.do';
                layer.confirm("是否确定重置任务",function () {
                    $.ajax({
                        type : "post", //提交方式
                        dataType : "json", //数据类型
                        url : url, //请求url
						data:{ degreeId:$("#degreeId").val(),taskId:$("#taskId").val(),learnId:$("#learnId").val()},
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