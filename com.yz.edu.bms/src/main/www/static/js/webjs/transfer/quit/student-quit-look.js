$(function() {
	//原因
	$("#reason").val(_findDict("quitSchoolReason",quitSchoolInfo.reason));
	$("#scholarship").val(_findDict("scholarship",quitSchoolInfo.scholarship));
	$("#inclusionStatus").val(_findDict("inclusionStatus",quitSchoolInfo.inclusionStatus));
	$("#stdStage").val(_findDict("stdStage",quitSchoolInfo.stdStage));
	$("#oldStdStage").val(_findDict("stdStage",quitSchoolInfo.oldStdStage));
	$("#checkStatus").val(_findDict("checkStatus",quitSchoolInfo.checkStatus));
	$("#remark").val(quitSchoolInfo.remark);
	$("#rejectReason").text(quitSchoolInfo.rejectReason ==null?'':quitSchoolInfo.rejectReason);
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