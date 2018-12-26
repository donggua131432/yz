$(function(){
	
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			$("#testYear").text(testYear);
			var wish = '';
			wish += stdName +'---[' + grade + ']['+_findDict('recruitType', recruitType)+']'+ unvsName + '(' + pfsnCode + ')';
			wish += '[' + _findDict('pfsnLevel', pfsnLevel) + ']';
			$("#stdName").text(wish);
			$("#stdStage").text(_findDict('stdStage', stdStage));
			$("#signStatus").text(signStatus);
			$("#tutorName").text(tutorName);
			$("#idCard").text(idCard);
			if(epName){
				$("#resetA").show();
				$("#affirmResult").text(epName+'---'+startTime+'/'+endTime);
				$("#affirmTime").text(affirmTime);
			}

			
             var examReasonUrl = '/examAffirm/getExamReason.do';
             var changeReasonUrl = '/examAffirm/changeUnconfirmeReason.do';
            
            //初始考试年度下拉框
			$.ajax({
				 type: "POST",
				 dataType : "json", //数据类型
				 url: examReasonUrl +'?eyId='+$("#eyId").val(),
				 success: function(data){
					 var erJson = data.body;
					 if(data.code=='00'){
						_init_select("erId",erJson,$("#erIds").val());
					 }
				 }
			});

			$("#form-affirm-add").validate({
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式
						dataType : "json", //数据类型
						url : changeReasonUrl, //请求url
						success : function(data) { //提交成功的回调函数
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('操作成功！', {icon : 1, time : 3000},function(){
									window.parent.myDataTable.fnDraw(false);
									layer_close();
								});
							}
						}
					})
				}

			});
		});
		//重置操作
		function resetResult() {
            
			var url = '/examAffirm/resetResult.do';
			layer.confirm('确定要重置该学员的确认信息吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : url,
					data : {
						affirmId : $("#affirmId").val(),
						taskId   : $("#taskId").val(),
						learnId  : $("#learnId").val()
					},
					dataType : 'json',
					success : function(data) {
						if (data.code == _GLOBAL_SUCCESS) {
							layer.msg('重置成功！', {icon : 1, time : 3000},function(){
								layer_close();
							});
						}
					}
				});
			});
		}