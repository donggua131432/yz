$(function(){
	
	var exType = $("#exType").val();
	var url = '';
	
	if('ADD' == exType){
		url = '/taskCard/add.do';
		$("#startTime").val(nowDate);
	}else if('UPDATE' == exType){
		url = '/taskCard/edit.do';
		if('1' != taskStatus) {
			$("#taskName").attr("disabled",true);
			$("#taskType").attr("disabled",true);
			$("#endTime").attr("disabled",true);
			$("#taskTarget").attr("disabled",true);
			$("#taskReward").attr("disabled",true);
		}
	}
	
	_init_select('taskType',dictJson.taskCardType,taskType);
	_init_radio_box('isOverlap', 'isOverlap', dictJson.isOverlap, isOverlap);
	
	$(".select").select2({
        placeholder: "--请选择--",
        allowClear: true
    });
	$("#form-banner-add").validate({
		rules : {
			taskName : {
				required : true,
				maxlength : 10
			},
			taskType : {
				required : true
			},
			startTime : {
				required : true
			},
			endTime : {
				required : true
			},
			taskTarget : {
				required : true,
				min : 1,
				max : 1000000
			},
			taskReward : {
				required : true,
				min : 100,
				max : 1000000
			}
		},
		messages: {
			taskReward : {
				min : '奖励智米不少于100'
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
							layer_close();
						});
					}
				}
			})
		}
	});
});

