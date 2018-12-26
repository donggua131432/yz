function queryAllocation(){
	
	 var logisticsName = $("#logisticsName").val();
     if(logisticsName==''){
     	 layer.msg('请选择快递公司!', {
              icon : 5,
              time : 3000
          });
          return;
     }
     
	var datas={};
	$.each($(window.parent.document).find('#export-form').serializeArray(),function(){
		datas[this.name]=this.value;
	});
	datas["logisticsName"] = logisticsName;
	
	var url = "/studentSend/queryOkOrder.do"
	layer.confirm('确认要改为已订？', function(index) {
        $.ajax({
            type : 'POST',
            url : url,
            data: datas,
            dataType : 'json',
            success : function(data) {
                if (data.code == _GLOBAL_SUCCESS) {
                	layer.msg('已订成功！', {icon : 1, time : 3000},function(){
						layer_close();
					});
                    //myEnrolledDataTable.fnDraw(true);
                }
            }
        });
    }); 
}