$(function(){
	var unvs = unvsName + ":" + pfsnName + "[" + _findDict("pfsnLevel",pfsnLevel) + ']';
	$("#unvs").text(unvs);
	$("#grade").text(_findDict("grade",grade));
	$("#scholarship").text(_findDict("scholarship",scholarship));
	$("#inclusionStatus").text(_findDict("inclusionStatus",inclusionStatus));
	$("#schoolYear").text(_findDict("schoolYear",schoolYear));
	
	$("#form-charges-add").validate({
		rules : {
			schoolYear : {
				required : true
			},
			learnId : {
				required : true
			}
		},
		messages : {
			
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/turnIn/edit.do", //请求url  
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