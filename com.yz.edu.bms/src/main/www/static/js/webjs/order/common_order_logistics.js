$(function () {
		
       //console.log([[${orderInfo}]]);
       
        $("#checkStatus").text(showCheckStatus(checkStatus));

    });
    
    function showCheckStatus(checkStatus){
		var dom = '';
		if(checkStatus == 1){
			dom = '未结算';
		}else if(checkStatus ==2){
			dom = '已结算';
		}else{
			dom = '未知状态';
		}
		return dom;
    }
   
 
    //结算
    function checkOrder() {
        layer.confirm('确认要结算吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/order/checkOrderInfo.do',
                data: {
                    orderNo: $("#orderNos").val()
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('结算成功!', {icon: 6, time: 1000},function () {
                    	window.parent.myDataTable.fnDraw(false);
                        layer_close();
                    });
                },
                error: function (data) {
                    layer.msg('结算失败！', {
                        icon: 5,
                        time: 1000
                    });
                },
            });
        });
    }
    //顺丰下单
    function sfOrders() {
		var chk_value = [];
		chk_value.push($("#orderNos").val());
		if(chk_value == null || chk_value.length <= 0){
			layer.msg('未选择任何数据!', {
				icon : 5,
				time : 1000
			});
			return;
		}
		
		layer.confirm('确认下单吗？',function(index) {
			$.ajax({
				type : 'POST',
				url : '/order/sfOrders.do',
				data : {
					orderNos : chk_value
				},
				dataType : 'json',
				success : function(data) {
					if (data.code == _GLOBAL_SUCCESS) {
						layer.msg('顺丰下单成功!', {
							icon : 1,
							time : 1000
						});
						window.parent.myDataTable.fnDraw(false);
						
					}
				}
			});
		});
	}
    
    function printSfs(){
    	var chk_value = [];
		chk_value.push($("#orderNos").val());
		
		if(chk_value == null || chk_value.length <= 0){
			layer.msg('未选择任何数据!', {
				icon : 5,
				time : 1000
			});
			return;
		}
		var url='/order/sfPrints.do'+ '?orderNos[]='+chk_value;;
		window.open(url);
	}
    //发货
    function updateOrderLogisticsInfo() {
        $.ajax({
            type: 'POST',
            url: '/order/updateOrderLogisticsInfo.do',
            data: {
                logisticsId: $("#logisticsId").val(),
                transportNo: $("#transportNo").val(),
                logisticsName: '顺丰快递',
                orderNo: $("#orderNos").val()
            },
            dataType: 'json',
            success: function (data) {
                layer.msg('保存成功!', {icon: 6, time: 1000},function () {
                    window.parent.myDataTable.fnDraw(false);
                    layer_close();
                });
            },
            error: function (data) {
                layer.msg('保存失败！', {
                    icon: 5,
                    time: 1000
                });
            },
        });
    }