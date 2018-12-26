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
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/exchange/list.do",
							data : {
								"salesName" : function(){
									return $("#salesName").val();
								},
								"salesStatus" : function(){
									return $("#salesStatus").val();
								},
								"salesType" : function(){
									return "1";
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
							"mData" : null
						}, {
							"mData" : "salesName"
						}, {
							"mData" : "goodsCount"
						}, {
							"mData" : "costPrice"
						}, {
							"mData" : "originalPrice"
						}, {
							"mData" : "salesPrice"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.salesId + '" name="salesIds" data-goodsId="'+row.goodsId+'"/>';
									},
									"targets" : 0
									},
									{
										"render" : function(data, type, row, meta) {
											 if(row.annexUrl == null){
												 return "暂未上传照片"; 
											 }else{
												 return '<img style="width: 140px; height: 140px;" src="'+(_FILE_URL + row.annexUrl + "?" + Date.parse(new Date()))+'">'; 
											 }
										},
										"targets" : 1
									},
									{
										 "render" : function(data, type, row, meta) {
										    var dom = '';
										    if(row.startTime !=null){
										    	dom +='起：'+row.startTime+'<br/>';
										    	dom +='至：'+row.endTime;
										    }else{
										    	dom +='无';
										    }
											return dom;
										},
										"targets" : 7
									},
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											if(row.activityStatus == 0){
												dom +='<span class="label radius">即将开始</span>';
											}else if(row.activityStatus == 1){
												dom +='<span class="label label-success radius">进行中</span>';
											}else if(row.activityStatus == 2){
												dom +='<span class="label radius">已结束</span>';
											}
											return dom;
										},
										"targets" : 8
									} ,
									{
										"render" : function(data, type, row, meta) {
											return 2 == row.salesStatus ?'<span class="label label-success radius">在线</span>':'<span class="label radius">离线</span>';
										},
										"targets" : 9
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
									   /*if(2 == row.salesStatus && row.activityStatus == '1'){
											dom += '<a title="查看" href="javascript:void(0)" onclick="exchange_look(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe695;</i></a>';
										}else{
											dom += '<a title="编辑" href="javascript:void(0)" onclick="exchange_edit(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										} */
										dom += '<a title="编辑" href="javascript:void(0)" onclick="exchange_edit(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 10
								} ]
					});
	
			});
	
	
		/*管理员-普通商品-添加*/
		function exchange_add() {
			var url = '/exchange/toExchangeEdit.do' + '?exType=Add';
			layer_show('添加普通商品', url, null, 510, function() {
				myDataTable.fnDraw(true);
			},true);
		}
		/*管理员-普通商品-编辑*/
		function exchange_edit(salesId) {
			var url = '/exchange/toExchangeEdit.do' + '?salesId='+ salesId +'&exType=UPDATE';
			layer_show('修改普通商品', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
		}
		/*查看*/
		function exchange_look(salesId) {
			var url = '/exchange/toExchangeEdit.do' + '?salesId='+ salesId +'&exType=LOOK';
			layer_show('修改普通商品', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
		}
	
		function exchangeExport(){
        	$("#export-form").submit();
        }
		function searchExchange(){
			myDataTable.fnDraw(true);
		}
		//批量删除
		function batch_del() {
			var chk_value = [];
			var chk_goodsIds=[];
			$("input[name=salesIds]:checked").each(function() {
				chk_value.push($(this).val());			
				chk_goodsIds.push($(this).attr("data-goodsId"));
			});
			if(chk_value.length ==0){
				layer.msg('请选择要删除的数据！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/exchange/batchDeleteExchange.do',
					data : {
						salesIds : chk_value,
						goodsIds : chk_goodsIds
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('已删除!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('删除失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}