function queryAllocation(){
	var datas={};
	$.each($(window.parent.document).find('#export-form').serializeArray(),function(){
		datas[this.name]=this.value;
	});
	datas["updateBatchId"] = $("#batchId").val();
    var url = "/studentSend/queryUpdateBatch.do";
    layer.confirm('确认要给选择的数据修改订书批次吗？', function(index) {
        $.ajax({
            type : 'POST',
            url : url,
            data: datas,
            dataType : 'json',
            success : function(data) {
                if (data.code == _GLOBAL_SUCCESS) {
                	layer.msg('修改成功！', {icon : 1, time : 3000},function(){
						layer_close();
					});
                }
            }
        });
    }); 
}