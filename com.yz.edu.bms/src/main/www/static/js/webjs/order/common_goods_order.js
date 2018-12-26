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
	        _init_select("orderStatus", dictJson.saleOrderStatus);
	        
	        _init_select("salesType", dictJson.salesType);
            
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/order/list.do",
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
							"mData" : "salesType"
						}, {
							"mData" : "goodsName"
						}, {
							"mData" : "unitPrice"
						}, {
							"mData" : "goodsCount"
						}, {
							"mData" : "transAmount"
						}, {
							"mData" : "jdPrice"
						}, {
							"mData" : "freight"
						}, {
							"mData" : "orderPrice"
						},{
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : "jdOrderCompleteTime"
						},{
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : "orderStatus"
						},{
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
									},{
										 "render" : function(data, type, row, meta) {
											    var dom = parseFloat(row.freight||0)+parseFloat(row.orderPrice||0);
											    
												return dom;
											},
											"sClass":'text-l',
											"targets" : 9
										},
									{
										 "render" : function(data, type, row, meta) {
										    var dom = '';
										    if(row.userName !=null){
										    	dom +='昵称：'+row.userName+'<br/>';
										    	dom +='手机：'+ (row.mobile == null ? '暂无' : row.mobile);
										    }else{
										    	dom +='无';
										    }
											return dom;
										},
										"sClass":'text-l',
										"targets" : 10
									},
									{
										 "render" : function(data, type, row, meta) {
											var dom = '';
											var dateTime = row.orderTime;
											if (!dateTime) {
												return '-'
											}
											var date = dateTime.substring(0, 10)
											var time = dateTime.substring(11)
											return date + '<br>' + time;
										},
										"sClass":'text-l',
										"targets" : 11
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
										"targets" : 13
									} ,
									{
										"render" : function(data, type,
												row, meta) {
											var dom = '';
											if(row.salesType!=''&& (row.salesType=='1' || row.salesType=='4')){
													dom += '<a href="javascript:void(0)" title="查看物流信息" onclick="show_jdlogistics(\'' + row.jdOrderId + '\')" class="ml-5"><p class="c-blue">'
													+ (row.jdOrderId||'') + '</p></a>';
											}else{
												dom += '<a  href="javascript:void(0)"  onclick="show_logistics(\'' + row.orderNo + '\')" class="ml-5" style="text-decoration:none">';
												dom += '<p class="c-blue">查看</p></a>';
											}
											
											return dom;
										},
										"targets" : 14
									},
									{
										"render" : function(data, type, row, meta) {
											return _findDict("saleOrderStatus",row.orderStatus);
										},
										"targets" : 15
									} ,
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											dom += '<a title="编辑" href="javascript:void(0)" onclick="order_edit(\'' + row.orderNo + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										   
											return dom;
										},
										//指定是第三列
										"targets" : 16
								} ]
					});
	
			});
	
	
		/*管理员-普通商品-编辑*/
		function order_edit(orderNo) {
			var url = '/order/toOrderEdit.do' + '?orderNo='+ orderNo +'&exType=UPDATE';
			layer_show('查看订单信息', url, null, 580, function() {
//				myDataTable.fnDraw(false);
			});
		}
		function show_logistics(orderNo){
			var url = '/order/toOrderLogistics.do' + '?orderNo='+ orderNo +'&exType=UPDATE';
			layer_show('查看物流信息', url, null, null, function() {
				myDataTable.fnDraw(false);
			});
		}
		/*京东物流*/
		function show_jdlogistics(orderNo){
			var url = '/jdorder/toJdOrderLogistics.do' + '?orderNo='+ orderNo ;
			layer_show('查看物流信息', url, null, null, function() {
				myDataTable.fnDraw(false);
			});
		}
		
		
		function searchOrders(){
			myDataTable.fnDraw(true);
			countAmount();
		}

		function exportOrder(){
			$("#export-form").submit();
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
		function countAmount(){
			$.ajax({
                type: 'POST',
                url: '/jdorder/listcount.do',
				data :{
					goodsName:$("#goodsName").val() ? $("#goodsName").val() : '',
	                orderNo: $("#orderNo").val() ? $("#orderNo").val() : '',
	                orderStatus: $("#orderStatus").val() ? $("#orderStatus").val() : '',
	                salesType: $("#salesType").val() ? $("#salesType").val() : '',
	                mobile:$("#mobile").val() ? $("#mobile").val() : '',
	                goodsType:"1",		
	                orderTimeEnd: $("#orderTimeEnd").val()? $("#orderTimeEnd").val():'',
	                orderTimeBegin: $("#orderTimeBegin").val()? $("#orderTimeBegin").val():'' ,
	                sfOrderTimeBegin: $("#sfOrderTimeBegin").val()?$("#sfOrderTimeBegin").val():'',
	                sfOrderTimeEnd: $("#sfOrderTimeEnd").val()?$("#sfOrderTimeEnd").val():'' 
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
		
		function searchData(data) {
            return {
				goodsName:$("#goodsName").val() ? $("#goodsName").val() : '',
                orderNo: $("#orderNo").val() ? $("#orderNo").val() : '',
                orderStatus: $("#orderStatus").val() ? $("#orderStatus").val() : '',
                salesType: $("#salesType").val() ? $("#salesType").val() : '',
                mobile:$("#mobile").val() ? $("#mobile").val() : '',
                goodsType:"1",		
                orderTimeEnd: $("#orderTimeEnd").val()?$("#orderTimeEnd").val():'',
                orderTimeBegin: $("#orderTimeBegin").val()?$("#orderTimeBegin").val():'' ,
                sfOrderTimeBegin: $("#sfOrderTimeBegin").val()?$("#sfOrderTimeBegin").val():'',
                sfOrderTimeEnd: $("#sfOrderTimeEnd").val()?$("#sfOrderTimeEnd").val():'' ,
                start: data.start,
                length: data.length
            };
      }