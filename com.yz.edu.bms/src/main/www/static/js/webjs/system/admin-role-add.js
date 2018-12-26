function checkFuncList(funcId){
	var attr = global_attr;
	if($.inArray(funcId, attr) >= 0){
		return true;
	}
	return false;
};

$(function(){
	
	if($("#exType").val() == "UPDATE"){
		$(".permission-list input:checkbox").each(function(){
			$(this).attr("checked",checkFuncList($(this).val()));
		});
	}
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

//	设置表格第一列高度和行高
	$('.permission-list > dd > dl > dt').each(function(i,e){
	    var height=$(e).siblings('dd').outerHeight();
        $(e).css({"height":height,"line-height":height+'px'});
	});
    $('.permission-list > dt').each(function(i,e){
        var height=$(e).parent('.permission-list').outerHeight();
        $(e).css({"height":height-2,"line-height":height-2+'px'});
    });


	$("#form-admin-role-add").validate({	
		rules : {
			roleName : {
				remote : { //验证角色名称是否存在
					type : "POST",
					url : '/auth/validateRole.do', //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						roleName : function() {
							return $("#roleName").val();
						}
						
					}
				},
				required : true
			},
			roleCode : {
				remote : { //验证角色编码是否存在
					type : "POST",
					url : '/auth/validateRole.do', //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						roleCode : function() {
							return $("#roleCode").val();
						}
						
					}
				},
				required : true,
			},
			permissions : {
				required : true
			}

		},
		messages : {
			roleName : {
				remote : "角色名称已存在"
			},
			roleCode : {
				remote : "角色编码已存在"
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/auth/roleUpdate.do', //请求url  
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