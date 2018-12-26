
function reject(){
	var url = '/withdraw/toRejectReason.do?withdrawId=' + $("#withdrawId").val() + '&remark=' + $("#remark").val();
    	layer_show('驳回原因', url, 400, 180, function () {
    });
}

$(function(){
	
	$("#sex").text(_findDict("sex",sex));
	$("#bankType").text(_findDict("bankType",bankType));
	$("#bankAddress").text(_findProvinceName(provinceCode)+'-->'+_findCityName(provinceCode,cityCode))
	$("#form-charges-add").validate({
		rules : {
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/withdraw/check.do', //请求url  
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('审核成功！', {icon : 1, time : 1000},function(){
							layer_close();
						});
					}
				}
			})
		}
	});
});