var myDataTable;
		$(function() {
			  //初始状态
	        _init_select("salesStatus", [
	            {
	                "dictValue": "1", "dictName": "离线"
	            },
	            {
	                "dictValue": "2", "dictName": "在线"
	            }
	        ]);
	        //初始订单状态
            _init_select("orderStatus", [
                                         { "dictValue": "1", "dictName": "待发货"},
                                         { "dictValue": "2", "dictName": "已发货"},
                                         { "dictValue": "3", "dictName": "已完成"}
                                         ]);
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/order/textBookList.do",
							data : {
								"goodsName" : function(){
									return $("#goodsName").val();
								},
								"orderNo" : function(){
									return $("#orderNo").val();
								},
								"transportNo" : function(){
									return $("#transportNo").val();
								},
								"orderStatus" : function (){
									return $("#orderStatus").val();
								},
								"goodsType" : function(){
									return "4";
								}
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
						}, {
							"mData" : null
						}, {
							"mData" : "orderTime"
						}, {
							"mData" : null
						}, {
							"mData" : "transportNo"
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
										    if(row.userName !=null){
										    	dom +='昵称：'+row.userName+'<br/>';
										    	dom +='手机：'+row.mobile;
										    }else{
										    	dom +='无';
										    }
											return dom;
										},
										"sClass":'text-l',
										"targets" : 7
									},
									{
										"render" : function(data, type, row, meta) {
										    var dom = '';
										    if(row.taskUserName !=null){
										    	dom +='收货人：'+row.taskUserName+'<br/>';
										    	dom +='联系手机：'+row.taskMobile +'<br/>';
										    	dom +='收货地址：'+row.address;
										    }else{
										    	dom +='无';
										    }
											return dom;
										},
										"sClass":'text-l',
										"targets" : 9
									} ,
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											if(row.orderStatus == 1){
												dom = '待发货';
											}else if(row.orderStatus ==2){
												dom = '已发货';
											}else if(row.orderStatus == 3){
												dom = '已完成';
											}else{
												dom = '未知状态';
											}
											return dom;
										},
										"targets" : 11
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
										dom += '<a title="编辑" href="javascript:void(0)" onclick="order_edit(\'' + row.orderNo + '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 12
								} ]
					});
	
			});
	
	
		/*管理员-普通商品-添加*/
		function exchange_add() {
			var url = '/exchange/toExchangeEdit.do' + '?exType=Add';
			layer_show('添加普通商品', url, null, null, function() {
				myDataTable.fnDraw(true);
			});
		}
		/*管理员-普通商品-编辑*/
		function order_edit(orderNo) {
			var url = '/order/toOrderEdit.do' + '?orderNo='+ orderNo +'&exType=UPDATE';
			layer_show('查看订单信息', url, null, null, function() {
				myDataTable.fnDraw(false);
			});
		}
		
		function searchOrders(){
			myDataTable.fnDraw(true);
		}