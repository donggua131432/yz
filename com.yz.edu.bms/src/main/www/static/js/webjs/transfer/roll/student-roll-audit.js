$(function() {
				
				var exType = global_exType;
				var url = '/studentRoll/passApprovalNew.do';
				if(exType == 'CHECK'){
				    $("#checkOrder").val("1");
                    $("#subButton").val("审核");
                }
                else if(exType == 'ACCPET'){
                    $("#checkOrder").val("2");
                }
				else if(exType == 'EXECUTE'){
                    $("#checkOrder").val("3");
					$("#subButton").val("执行");
				}

                var ext = ".doc";
				//韩师，东理
                if ($("#unvsId").val() == 23 || $("#unvsId").val() == 29) {
                    ext = ".xls";
                }
                //国开大学
                if ($("#unvsId").val() != 46) {
                    $("#rollfile").attr("href", "../rolldoc/roll_" + $("#unvsId").val()+ ext);
                }
                //暂无文件
                if($("#unvsId").val()==47 || $("#unvsId").val()==50 || $("#unvsId").val()==52|| $("#unvsId").val()==54){
                    $("#rollfile").attr("href","javascript:;");
                }
				
				//年级
				$("#grade").val(_findDict("grade",$("#grade").val()));
				//层次
				$("#pfsnLevel").text(_findDict("pfsnLevel",$("#pfsnLevel").text()));
    			$("#zypfsnLevel").val(_findDict("pfsnLevel",$("#zypfsnLevel").val()));
                $("#zynpfsnLevel").text(_findDict("pfsnLevel",$("#zynpfsnLevel").text()));
				//专业  
				$("#pfsnName").val("("+$("#pfsnCodev").val()+")"+$("#pfsnNamev").val());
				//学员阶段
				$("#stdStage").text(_findDict("stdStage",$("#stdStage").text()));
                //优惠类型
                $("#scholarship").text(_findDict("scholarship", $("#scholarship").text()));
                //入围状态
                $("#inclusionStatus").text(_findDict("inclusionStatus", $("#inclusionStatus").text()));
				//性别
				$("#sexv").val(_findDict("sex",$("#sexv").val()));
                $("#newSex").text(_findDict("sex",$("#newSex").text()));
				$("#sex").val(_findDict("sex",$("#sex").val()));
				//民族
                $("#newNation").text(_findDict("nation",$("#newNation").text()));
				$("#nationv").val(_findDict("nation",$("#nationv").val()));
				$("#nation").val(_findDict("nation",$("#nation").val()));

   				 var checksFlag=false;
				$("#form-member-add").validate({
					onkeyup : false,
					focusCleanup : true,
					success : true,
					submitHandler : function(form) {
						if('2'==$('#checkStatus').val()){
                            if(checksFlag){
                                layer.msg('请勿重复提交!', {
                                    icon : 0,
                                    time : 1000
                                });
                                return
                            }
                            checksFlag=true;
							$(form).ajaxSubmit({
								type : "post", //提交方式  
								dataType : "json", //数据类型  
								url : url, //请求url 
								success : function(data) { //提交成功的回调函数  
									if(data.code == _GLOBAL_SUCCESS){
										layer.msg('操作成功！', {icon : 1, time : 1000},function(){
											layer_close();
                                            checksFlag=false;
										});
									}
								}
							});
						}else{
							layer.prompt({
								title : '填写驳回原因：',
								formType : 2
							}, function(text, index) {
								$('#reason').val(text);
								$(form).ajaxSubmit({
									type : "post", //提交方式  
									dataType : "json", //数据类型  
									url : url, //请求url 
									success : function(data) { //提交成功的回调函数  
										if(data.code == _GLOBAL_SUCCESS){
											layer.msg('操作成功！', {icon : 1, time : 1000},function(){
												layer_close();
											});
										}
									}
								});
							});
						}
					}
				});
			});