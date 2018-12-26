function selectAllocation(){
	
	 var logisticsName = $("#logisticsName").val();
     if(logisticsName==''){
     	 layer.msg('请选择快递公司!', {
              icon : 5,
              time : 3000
          });
          return;
     }
     
	var chk_value = [];
	$(window.parent.document).find('#sendBookTable input[name=sendIdss]:checked').each(function() {
		chk_value.push($(this).val());
	});
	$(window.parent.document).find('#sendBookTable input[name=sendIdss]:checked').attr("checked", false);
	var url = "/studentSend/okOrder.do"
	layer.confirm('确认要改为已订？', function(index) {
        $.ajax({
            type : 'POST',
            url : url,
            data : {
            	idArray : chk_value,
            	logisticsName :$("#logisticsName").val()
			},
            dataType : 'json',
            success : function(data) {
                if (data.code == _GLOBAL_SUCCESS) {
                	layer.msg('已订成功！', {icon : 1, time : 3000},function(){
						layer_close();
					});
                    //myEnrolledDataTable.fnDraw(true);
                }
            },
            error : function(data) {
				layer.msg('已订失败！', {icon : 2, time : 3000},function(){
					layer_close();
				});
			},
        });
    }); 
}