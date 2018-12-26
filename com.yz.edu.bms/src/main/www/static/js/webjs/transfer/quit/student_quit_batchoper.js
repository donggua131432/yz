$(function(){
	
	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});
	
});

function batchOper(checkStatus){
	var chk_value = [];
	var len=$(window.parent.document).find('#tab input[name=ids]:checked').length;

	$(window.parent.document).find('#tab input[name=ids]:checked').each(function() {
		chk_value.push($(this).val());
	});

    var url = '/quitSchoolCheck/batchCheck.do';
    layer.confirm('确认要批量完成'+len+'个学员的休息申请吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : url,
			data : {
				ids : chk_value,
				rejectReason :$("#remark").val(),
				checkStatus : checkStatus
			},
			dataType : 'json',
			success : function(data) {
                $(window.parent.document).find('#tab input[name=ids]:checked').attr("checked", false);
				layer.msg('操作成功！', {icon : 1, time : 3000},function(){
					layer_close();
				});
			},
			error : function(data) {
				layer.msg('操作失败！', {icon : 2, time : 3000},function(){
					layer_close();
				});
			},
		});
	});
}