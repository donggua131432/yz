function selectAllocation(){
	var chk_value = [];
	$(window.parent.document).find('#sendBookTable input[name=sendIdss]:checked').each(function() {
		chk_value.push($(this).val());
	});
	console.log(chk_value);
	$(window.parent.document).find('#sendBookTable input[name=sendIdss]:checked').attr("checked", false);
    var url = "/studentSend/selectUpdateBatchId.do";
    layer.confirm('确认要给选择数据修改订书批次吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : url,
			data : {
				idArray : chk_value,
				updateBatchId :$("#batchId").val()
			},
			dataType : 'json',
			success : function(data) {
				layer.msg('修改成功！', {icon : 1, time : 3000},function(){
					layer_close();
				});
			},
			error : function(data) {
				layer.msg('修改失败！', {icon : 2, time : 3000},function(){
					layer_close();
				});
			},
		});
	});
}
