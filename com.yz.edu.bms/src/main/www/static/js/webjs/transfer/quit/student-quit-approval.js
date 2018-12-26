$(function() {
	//原因
	$("#reason").val(_findDict("quitSchoolReason",quitSchoolInfo.reason));
	$("#scholarship").val(_findDict("scholarship",quitSchoolInfo.scholarship));
	$("#inclusionStatus").val(_findDict("inclusionStatus",quitSchoolInfo.inclusionStatus));
	$("#stdStage").val(_findDict("stdStage",quitSchoolInfo.stdStage));
	$("#checkStatus").val(_findDict("checkStatus",quitSchoolInfo.checkStatus));
	changeCheck(quitSchoolInfo.learnId);
});
function changeCheck(learnId) {
	$.post('/studentOut/findStudentInfoByGraStdId.do', {
		learnId : learnId||$("#learnId").val(),
	}, function(result) {
		if (!result.body) {
			$("#onUnvsId").val("");
			$("#unvsId").val("");
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
			
		}

	},"json");
}
function reject(checkStatus){
	var id = $("#id").val();
	var rejectReason = $("#rejectReason").val();
	var learnId = $("#learnId").val();
	var text = '';
	if(checkStatus == '3'){
		text = '确认要通过休学申请？';
	}else{
		text = '确认要驳回休学申请？';
	}
	layer.confirm(text, function(index) {
		$.ajax({
			type : 'POST',
			url : '/quitSchoolCheck/checkStudentQuitSchoolApply.do',
			data : {
				id : id,
				rejectReason : rejectReason,
				checkStatus : checkStatus,
				learnId : learnId
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					  layer.msg('操作成功！', {icon : 1, time : 2000},function(){
                          window.parent.myDataTable.fnDraw(false);
                          layer_close();
                      });
				}
			}
		});
	});
}