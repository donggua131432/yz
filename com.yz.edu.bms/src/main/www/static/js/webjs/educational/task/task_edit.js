$(function(){
	
	
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	//初始考试年度下拉框
	url = "/examAffirm/getExamYear.do"
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("eyId",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });
	url = "/studyActivity/getGKExamYear.do"
		$.ajax({
	         type: "POST",
	         dataType : "json", //数据类型  
	         url: url,
	         success: function(data){
	      	     var eyJson = data.body;
	      	     if(data.code=='00'){
	      	    	_init_select("gkEyId",eyJson,$("#eyIds").val()); 	 
	      	     }
	         }
	    });
	
	//国开城市确认	
	url = '/studyActivity/getExamYearForGK.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("examEyIdGK",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });	
	
	//统考设置
	url = '/studyActivity/getGkUnifiedExam.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("gkUnifiedId",eyJson,$("#eyIds").val()); 	 
      	     }
         }
    });	
    if($("#exType").val() == "UPDATE"){

    	$("#taskContent").text(varTaskInfo.taskContent);
    	$("#endTimeInput").val(varTaskInfo.endTime);
    	$("#startTimeInput").val(varTaskInfo.startTime);
    	//显示创建人和创建时间
    	
    	var dom = '';
    	dom +='<br><div class="line" style="margin-left:200px;width:200px"></div><hr/>';   
		dom += '<div class="row cl" id="createUser">';
		dom += '<label class="form-label col-xs-4 col-sm-3"><span class="c-red"></span>创建人：</label>';
		dom += '<div class="formControls col-xs-8 col-sm-9">';
		dom += '<span>'+createUser+'</span>';
		dom += '</div>';
		dom += '</div>';
		
		dom += '<div class="row cl" id="createTime">';
		dom += '<label class="form-label col-xs-4 col-sm-3"><span class="c-red"></span>创建时间：</label>';
		dom += '<div class="formControls col-xs-8 col-sm-9">';
		dom += '<span>'+createTime+'</span>';
		dom += '</div>';
		dom += '</div>';
		
		
		$("#staffSelect").append(dom);
		
		if(ifCanOper && ifCanOper=='N'){
			 $("#eyId").attr("readonly", "readonly");
			 $("input[name='taskType']:not(:checked)").attr("disabled", "disabled");
		}
    }
	
	$("#form-task-add").validate({
		rules : {
			taskTitle : {
				required : true
			},
			startTime : {
				required : true
			},
			endTime : {
				required : true
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$("#startTime").val($("#startTimeInput").val());
			$("#endTime").val($("#endTimeInput").val());
			if($("input[name='taskType']:checked").val() =='4'){
				if($("#eyId").val()==''){
					layer.msg('考前确认必须选择考试年度！', {icon : 2, time : 4000});
					return;
				}
			}
			if($("input[name='taskType']:checked").val() =='5'){
				if($("#gkEyId").val()==''){
					layer.msg('国开考试通知必须选择考试年度！', {icon : 2, time : 4000});
					return;
				}
			}
			if($("input[name='taskType']:checked").val() =='13'){
				if($("#examEyIdGK").val()==''){
					layer.msg('国开考场城市确认任务必须选择考试年度！', {icon : 2, time : 4000});
					return;
				}
			}
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/task/taskUpdate.do", //请求url  
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