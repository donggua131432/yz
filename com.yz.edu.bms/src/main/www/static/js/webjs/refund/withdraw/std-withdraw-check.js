$(function(){
	
	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});
	
});

function selectCheck(){
	var chk_value = [];
	var len=$(window.parent.document).find('#tab input[name=withdrawIds]:checked').length;

	$(window.parent.document).find('#tab input[name=withdrawIds]:checked').each(function() {
		chk_value.push($(this).val());
	});

    var url = '/withdraw/checks.do';
    layer.confirm('确认要批量完成'+len+'个学员的提现审批吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : url,
			data : {
				withdrawIds : chk_value,
				remark :$("#remark").val()
			},
			dataType : 'json',
			success : function(data) {
                $(window.parent.document).find('#tab input[name=withdrawIds]:checked').attr("checked", false);
				layer.msg('操作成功！', {icon : 1, time : 3000},function(){
					layer_close();
				});
				//myEnrolledDataTable.fnDraw(true);
				//$("input[name=checkAll]").attr("checked", false);
			},
			error : function(data) {
				layer.msg('操作失败！', {icon : 2, time : 3000},function(){
					layer_close();
				});
				//myEnrolledDataTable.fnDraw(true);
				//$("input[name=checkAll]").attr("checked", false);
			},
		});
	});
}