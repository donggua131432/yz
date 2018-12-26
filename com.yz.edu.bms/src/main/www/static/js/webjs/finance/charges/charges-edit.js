function itemTypeChange(obj){
	var type = $(obj).val();
	if('2' == type || '4' == type || '5' == type){
		$("#yearItem").show();
	}else{
		$("#itemYear").val("");
		$("#yearItem").hide();
	}
}

$(function(){


	$("#yearItem").hide();
	var exType = $("#exType").val();
	var url = '';
	if('ADD' == exType){
		url = "/charges/add.do";
	}else if('UPDATE' == exType){
		url = "/charges/edit.do";
	}
	
	_init_checkbox("recruitTypes","recruitTypes",dictJson["recruitType"],recruits);

	_init_select("itemType", dictJson.itemType, itemType);
	
	
	_init_select("itemYear", dictJson.itemYear, itemYear);
	
	if('2' == itemType || '4' == itemType || '5' == itemType){
		if(itemYear != '' && itemYear != null){
			$("#yearItem").show();
		}
	}
	
	$("#form-charges-add").validate({
		rules : {
			itemCode : {
				remote : { //验证角色名称是否存在
					type : "POST",
					url : "/charges/validateItemCode.do", //servlet
					data : {
						itemCode : function() {
							return $("#itemCode").val();
						},
						exType : exType
					}
				},
				required : true
			},
			itemType : {
				required : true
			},
			itemName : {
				required : true
			},
			recruitTypes : {
				required : true
			},
			itemYear : {
				required : true
			}
		},
		messages : {
			itemCode : {
				remote : "科目编码已存在"
			}
		},
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
						    if('ADD' == exType){
                                window.parent.myDataTable.fnDraw(true);
							}else {
                                window.parent.myDataTable.fnDraw(false);
							}
							layer_close();
						});
					}
				}
			})
		}
	});
});