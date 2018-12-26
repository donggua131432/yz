$(function(){
	//初始课程下拉框
	var url = "/class/salesList.do"
 	_simple_ajax_select({
		selectId : "salesId",
		searchUrl : url,
		sData : {},
		showText : function(item) {
			return item.salesName;
		},					
		showId : function(item) {
			return item.salesId;
		},
		placeholder : '--请选择课程--'
	});
    $.ajax({
           type: "POST",
           dataType : "json", //数据类型  
           url: url,
           success: function(data){
          	var salesIdJson = data.body.data;
          	 for(var i=0;i<salesIdJson.length;i++){
          		 if(salesIdJson[i].salesId == $("#salesIds").val()){
          			var salesName = salesIdJson[i].salesName;
 		                    $("#salesId").append(new Option(salesName, $("#salesIds").val(), false, true));
          		 }
          	 }
           }
    });
    $("#endTime").blur(function(){
    	if($("#startTime").val() ==''){
    		layer.msg('请选择开始时间！', {icon : 2, time : 1000});
    	}
    	if($("#startTime").val() >$("#endTime").val()){
    		layer.msg('结束时间必须大于开始时间！', {icon : 2, time : 1000});
        }
    });
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$("#form-class-add").validate({
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/class/classUpdate.do", //请求url  
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