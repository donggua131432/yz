$(function() {
	var row = $("#resultInfo").val();
	var json = eval('(' + row + ')');
	initFeeDataDom(json);
});
function initFeeDataDom(row){
	var dom = '';
	dom += '<table class="table table-border table-bordered table-hover table-bg table-sort">';
	dom += '<thead>';
	dom += '<tr>';
	dom += '<th width="80">跟进人</th>';
	dom += '<th width="40">目标学员数</th>';
	dom += '<th width="40">完成数</th>';
	dom += '<th width="40">完成率</th>';
	dom += '</tr>';
	dom += '</thead><tbody>';

	for (var i = 0; i < row.length; i++) {
		var resultInfo = row[i];
		dom += '<tr>';
		dom += '<td>'+ resultInfo.empName+ '</td>';
		dom += '<td>'+ resultInfo.totalCount+ '</td>';
		dom += '<td>'+ resultInfo.finishCount+ '</td>';
		dom += '<td>'+ ((resultInfo.finishCount/resultInfo.totalCount)*100).toFixed(2)+ '%</td>';
		dom += '</tr>';
	}
	
	dom += '</tbody>';
	dom += '</table>';
	$("#taskInfoDiv").html(dom);
	
	if(row.length >0){
		$("#remind").show();
	}
}
function remindUnfinishedStudent(){
	var taskId = $("#taskId").val();
	$("#remindStu").val("正在提醒中").attr('disabled','disabled').css({opacity:'0.5'});
	layer.confirm('确认要提醒未完成的学员吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/studyActivity/remindUnfinishedStudent.do'+'?taskId='+taskId,
			dataType : 'json',
			success : function(data) {
				layer.msg('提醒成功！', {icon: 1, time: 3000}, function () {
                    layer_close();
                });
			},
			error : function(data) {
				 $("#remindStu").val("提醒未完成的学员").removeAttr('disabled').css({opacity:'1'});
				layer.msg('提醒失败！', {
					icon : 1,
					time : 3000
				});
			},
		});
	});
}