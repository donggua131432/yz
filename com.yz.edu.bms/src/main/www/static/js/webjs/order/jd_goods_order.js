var myDataTable;
		$(function() {
			
	        //初始订单状态
            _init_select("orderStatus", [{ "dictValue": "3", "dictName": "已完成"},{ "dictValue": "5", "dictName": "已拒收"}]);
            myDataTable = $('.table-sort').dataTable(
					{
						"processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/jdorder/list.do",
							data : function (data) {
	                            return searchData(data);
	                        }
						},
						"pageLength" : 10,
						"pagingType" : "full_numbers",
						"ordering" : false,
						"searching" : false,
						"createdRow" : function(row, data, dataIndex) {
							$(row).addClass('text-c');
						},
						"language" : _my_datatables_language,
						"drawCallback": function( settings ) {
	                    	countAmount();
	                    },
						columns : [ {
							"mData" : null
						}, {
							"mData" : "orderNo"
						}, {
							"mData" : null
						}, {
							"mData" : "goodsName"
						}, {
							"mData" : "unitPrice"
						}, {
							"mData" : "goodsCount"
						}, {
							"mData" : "transAmount"
						},{
							"mData" : "jdPrice"
						},{
							"mData" : "orderPrice"
						}, {
							"mData" : "freight"
						}, {
							"mData" : "orderTime"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.orderNo + '" name="orderNos"/>';
									},
									"targets" : 0
									},
									{
										"render" : function(data, type, row, meta) {
											return _findDict("salesType",row.salesType);
										},
										"targets" : 2
									},
									
									{
										"render" : function(data, type, row, meta) {
										    var dom = '';
										    if(row.takeUserName !=null){
										    	dom +='收货人：'+row.takeUserName+'<br/>';
										    	dom +='联系手机：'+row.takeMobile +'<br/>';
										    	dom +='收货地址：'+row.address;
										    }else{
										    	dom +='无';
										    }
											return dom;
										},
										"sClass":'text-l',
										"targets" : 11
									} ,
									
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											if(row.orderStatus == 3){
												dom = '已完成';
											}else if(row.orderStatus == 5){
												dom = '已拒收';
											}else{
												dom = '未知状态';
											}
											return dom;
										},
										"targets" : 12
									} ,
									
									
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
										dom += '<a title="详情" href="javascript:void(0)" onclick="order_edit(\'' + row.orderNo + '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
									    
										return dom;
									},
									//指定是第三列
									"targets" : 13
								} ]
					});
	
		});
		
		function searchOrders(){
			myDataTable.fnDraw(true);
			countAmount();
		}
		function exportJdOrder(){
			$("#export-form").submit();
		}
		function order_edit(orderNo) {
			var url = '/jdorder/tojdOrderEdit.do' + '?orderNo='+ orderNo;
			layer_show('查看订单信息', url, null, null, function() {
			});
		}
		function searchData(data) {
	            return {
	                orderNo: $("#orderNo").val() ? $("#orderNo").val() : '',
	                orderStatus: $("#orderStatus").val() ? $("#orderStatus").val() : '',
	                salesType: '1',
	                orderTimeEnd: $("#orderTimeEnd").val()?$("#orderTimeEnd").val():'',
	                orderTimeBegin: $("#orderTimeBegin").val()?$("#orderTimeBegin").val():'' ,	                
	                start: data.start,
	                length: data.length
	            };
	      }
		
		function countAmount(){
			$.ajax({
                type: 'POST',
                url: '/jdorder/listcount.do',
                data : function (data) {
                    return searchData(data);
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {     	
                    	var data=data.body;
                        $("#balance").text(data.balance?data.balance:'0');
                        $("#swOrderNum").text(data.orderNum?data.orderNum:'0');
                        $("#swTotalAmount").text(data.totalAmount?data.totalAmount:'0');
                        $("#stkbalance").text(data.stkbalance?data.stkbalance:'0');
                        $("#stkOrderNum").text(data.stkOrderNum?data.stkOrderNum:'0');
                        $("#stkTotalAmount").text(data.stkTotalAmount?data.stkTotalAmount:'0');
                    }
                }
        	});
		}
		/**同步订单**/
		function synchronousJd(){
        	$.ajax({
                type: 'POST',
                url: '/jdorder/synchronousJdOrder.do',
                data :{
                    
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) { 
                    	layer.msg('订单同步成功!', {
                            icon : 1,
                            time : 1000
                        });
                        myDataTable.fnDraw(true);
                    	
                    }
                }
        	});
        }