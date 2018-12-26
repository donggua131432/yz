$(function() {
	  //学员
	_simple_ajax_select({ 
		selectId : "learnId",
		searchUrl : '/quitSchool/findStudentInfo.do',
		sData : {},
		showText : function(item) {
			return item.stdName + " - " + item.grade + "级";
		},
		showId : function(item) {
			return item.learnId;
		},
		placeholder : '--学员--'
	});
    $("#learnId").append(new Option("","",false,true))
	//原因
	_init_select("reason", dictJson.quitSchoolReason);

	$("#learnId").change(function() {
		changeCheck();
	});

	$('.skin-minimal input').iCheck({
		checkboxClass : 'icheckbox-blue',
		radioClass : 'iradio-blue',
		increaseArea : '20%'
	});

	$("#form-member-add").validate({
		rules : {
			learnId : {
				required : true,
			},
			grade : {
				required : true,
			},
			reason : {
				required : true,
			},
			remark : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {

            if ($("#uploadfile-1").val() == '') {
                layer.msg('附件为必传！', {icon: 0, time: 1000});
                return;
            }

			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/quitSchool/addStudentQuit.do', //请求url 
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 1000},function(){
							window.parent.myDataTable.fnDraw(false);
                            layer_close();
						});
					}
				}
			});
		}
	});
});

function changeCheck(learnId) {
	$.post('/studentOut/findStudentInfoByGraStdId.do', {
		learnId : learnId||$("#learnId").val(),
	}, function(result) {
		if (!result.body) {
			$("#stdStage").val("");
			$("#unvsId").val("");
			$("#onUnvsId").val("");
			$("#unvsId").val("");
			$("#recruit").val("");
			$("#tutor").val("");
			$("#stdId").val("");
			$("#grade").val("");
		} else {
			result = result.body;
			if(result.stdStage){
				$("#stdStage").val(_findDict("stdStage", result.stdStage));
				$("#stdStageh").val(result.stdStage);
			}else{
				$("#stdStage,#stdStageh").val("")
			}
            var txt='';
            txt+=_findDict("recruitType", result.recruitType)?"[" + _findDict("recruitType", result.recruitType) + "]":'';
            txt+=result.unvsName?result.unvsName+":":'';
            txt+=result.pfsnName?result.pfsnName:'';
            txt+=_findDict("pfsnLevel", result.pfsnLevel)?"[" + _findDict("pfsnLevel", result.pfsnLevel) + "]":'';
			$("#unvsId").val(txt);
			
			if(result.stdStage == '5' ||result.stdStage == '6' || result.stdStage == '7'){
                txt='';
                txt+=_findDict("recruitType", result.bsarecruitType)?"[" + _findDict("recruitType", result.bsarecruitType) + "]":'';
                txt+=result.bsaunvsName?result.bsaunvsName+":":'';
                txt+=result.bsapfsnName?result.bsapfsnName:'';
                txt+=_findDict("pfsnLevel", result.bsapfsnLevel)?"[" + _findDict("pfsnLevel", result.bsapfsnLevel) + "]":'';
                $("#onUnvsId").val(txt);
			}else{	
				$("#onUnvsId").val("");
			}
			$("#recruit").val(result.oetName);
			$("#tutor").val(result.oerName);
			$("#stdId").val(result.stdId);
			$("#grade").val(result.grade);
			$("#recruitType").val(result.recruitType);
		}

	},"json");
}

function loadFile(img) {
    var filePath = img.value;
    var fileExt = filePath.substring(filePath.lastIndexOf("."))
        .toLowerCase();

    if (img.files && img.files[0]) {
    	var size=(img.files[0].size / 1024).toFixed(0);
		// 文件大小限制5m
    	if(size>1024*5){
    		layer.msg('文件最大为5M！',{icon:0})
            img.value = "";
            return;
		}
//                alert(img);
//                alert(img.files[0])
//         alert('你选择的文件大小' + (img.files[0].size / 1024).toFixed(0) + "kb");
//                var xx = img.files[0];
//                for (var i in xx) {
//                    alert(xx[i])
//                }
    }
}
