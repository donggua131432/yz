$(function(){
	
	$(".permission-list dt input:checkbox").click(function(){
		$(this).closest("dl").find("dd input:checkbox").prop("checked",$(this).prop("checked"));
	});
	$(".permission-list2 dt input:checkbox").click(function(){
		if($(this).prop("checked")){
			$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",true);
		} 
	});
	$(".permission-list2 dd input:checkbox").click(function(){
		var l =$(this).parent().parent().find("input:checked").length;
		var l2=$(this).parents(".permission-list").find(".permission-list2 dd").find("input:checked").length;
		if($(this).prop("checked")){
			$(this).closest("dl").find("dt input:checkbox").prop("checked",true);
			$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",true);
		}
		else{
			if(l==0){
				$(this).closest("dl").find("dt input:checkbox").prop("checked",false);
			}
			if(l2==0){
				$(this).parents(".permission-list").find("dt").first().find("input:checkbox").prop("checked",false);
			}
		}
	});
	
	$("#form-admin-permission-add").validate({
		rules : {
			funcName : {
				remote : { //验证角色名称是否存在
					type : "POST",
					url : '/auth/validateFunc.do', //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						funcName : function() {
							return $("#funcName").val();
						},
						oldFuncName : function(){
							return $("#oldFuncName").val();
						}
						
					}
				},
				required : true
			},
			funcCode : {
				remote : { //验证角色编码是否存在
					type : "POST",
					url : '/auth/validateFunc.do', //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						funcCode : function() {
							return $("#funcCode").val();
						},
						oldFuncCode : function() {
							return $("#oldFuncCode").val();
						}						
					}
				},
				required : true,
			}

		},
		messages : {
			funcName : {
				remote : "权限名称已存在"
			},
			funcCode : {
				remote : "权限编码已存在"
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/auth/permissionUpdate.do', //请求url  
				dataType : 'json',
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 1000},function(){
							layer_close();
						});
					}
				}
			})
		}
	});
	
	
});
