$(function() {
				
	var exType = global_exType;;
	var url = '/studentRoll/acceptRoll.do';
	
	if(exType == 'EXECUTE'){
		var updateUser = global_updateUser;
		$("#acceptUser").val(updateUser);
		$("#subButton").val("执行");
		url = '/studentRoll/passApproval.do';
	}
	
	//年级
	$("#grade").val(_findDict("grade",$("#grade").val()));
	//层次
	$("#pfsnLevel").val(_findDict("pfsnLevel",$("#pfsnLevel").val()));
	//专业  
	$("#pfsnName").val("("+$("#pfsnCodev").val()+")"+$("#pfsnNamev").val());
	//学员阶段
	$("#stdStage").val(_findDict("stdStage",$("#stdStage").val()));
	//性别
	$("#sexv").val(_findDict("sex",$("#sexv").val()));
	$("#sex").val(_findDict("sex",$("#sex").val()));
	//民族
	$("#nationv").val(_findDict("nation",$("#nationv").val()));
	$("#nation").val(_findDict("nation",$("#nation").val()));
	//优惠类型
	$("#scholarshipv").val(_findDict("scholarship",$("#scholarshipv").val()));
	$("#scholarship").val(_findDict("scholarship",$("#scholarship").val()));
	 
	$("#form-member-add").validate({
		onkeyup : false,
		focusCleanup : true,
		success : true,
		submitHandler : function(form) {
			if('2'==$('#checkStatus').val()){
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