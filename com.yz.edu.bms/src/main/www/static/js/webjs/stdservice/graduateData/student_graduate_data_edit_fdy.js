$(function(){
	
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			$('.skin-minimal input').iCheck({
				checkboxClass: 'icheckbox-blue',
				radioClass: 'iradio-blue',
				increaseArea: '20%'
			});
			$('.ifSubmit').iCheck('disable'); 
			$(".ifSubmit").closest('.radio-box').css('color','#999');
			var wish = '';
			wish += stdName +'   [' + grade + '级]';
			$("#stdName").text(wish);
			var txt = '';
			txt +=unvsName + ':(' + pfsnCode + ')'+pfsnName ;
			txt += '[' + _findDict('pfsnLevel', pfsnLevel) + ']';
			$("#unvsInfo").text(txt);
			$("#schoolRoll").text(schoolRoll);
			$("#stdNo").text(stdNo);
			$("#idCard").text(idCard);
			$("#stdStage").text(_findDict('stdStage', stdStage));
			$("#tutorName").text(tutorName);
			if(userName!=null){
				$("#expressInfo").text(userName+'-'+address+'-'+mobile);	
			}
			$("#remark").text(remark);
			
            var editGraduateUrl = '/graduateData/editStuGraduateData.do';
           
            $("#ifSubmit").val(ifSubmit);
            $("#ifMail").val(ifMail);
            $("#ifPass").val(ifPass);
            $("#noPassReason").val(noPassReason);
            $("#expressNo").val(expressNo);
			$("#form-graduate-add").validate({
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					if($("#noPassReason").val().length >'30'){
						layer.msg('不合格原因长度过大！', {icon : 2, time : 4000});
						return;
					}
					if($("#expressNo").val().length >'18'){
						layer.msg('快递单号长度过大！', {icon : 2, time : 4000});
						return;
					}
					$(form).ajaxSubmit({
						type : "post", //提交方式
						dataType : "json", //数据类型
						url : editGraduateUrl, //请求url
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