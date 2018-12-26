var url = '';	// 请求地址
var deal = '';// 处理类型
$(function() {
	
	var exType = exType_global;
	
	var dealType = dealType_global;
	
	if('DIRECTOR' == dealType){		//校监
		deal = 'Director';
	}else if('FINANCIAL' == dealType){	//财务
		deal = 'Financial';
	}else if('PRINCIPAL' == dealType){	//校办
		deal = 'Principal';
	}
	
	if('CANCEL' == exType){
		url = '/refundCheck/' + 'undo'+deal+'Approval.do';
		var dom = '<input class="btn btn-primary radius" type="submit" onclick="$(\'#checkStatus\').val(\'2\')" value="&nbsp;&nbsp;撤销审核&nbsp;&nbsp;">';
		$("#subDiv").html(dom);
	}else if('CHECK' == exType){
		url = '/refundCheck/' + 'pass'+deal+'Approval.do';
	}
	changeCheck();
	$('.skin-minimal input').iCheck({
		checkboxClass : 'icheckbox-blue',
		radioClass : 'iradio-blue',
		increaseArea : '20%'
	});

	$("#form-member-add").validate({
		rules : {
			outId : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			if('4'==$('#checkStatus').val()){
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
			}else{
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
			}
		}
	});
});
function changeCheck() {
	$.post('/refundCheck/findStudentInfo.do', {
		learnId : $("#learnId").val()
	}, function(result) {
		result = result.body;
		$("#stdStage").text(_findDict("stdStage", result.stdStage));
		$("#unvsId").text(
				"[" + _findDict("recruitType", result.recruitType) + "]" + result.unvsName + ":" + result.pfsnName + "[" + _findDict("pfsnLevel", result.pfsnLevel) + "]");
		$("#onUnvsId")
				.text(
						"[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
								+ "]");
		$("#unvsId")
				.text(
						"[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
								+ "]");
		$("#recruit").text(result.oerName != null ? result.oerName : '');
		$("#tutor").text(result.oetName != null ? result.oetName : '');
        $("#idCard").text(result.idCard);
        $("#inclusionStatus").text(_findDict("inclusionStatus", result.inclusionStatus));
        $("#scholarship").text(_findDict("scholarship", result.scholarship));
	},"json");
}