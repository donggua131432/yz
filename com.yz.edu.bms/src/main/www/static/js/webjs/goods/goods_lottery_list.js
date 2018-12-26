var myDataTable;
		$(function() {
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/lottery/list.do",
							data : {
								"salesName" : function(){
									return $("#salesName").val();
								},
								"salesType" : function(){
									return "2";
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
							"mData" : "totalCount"
						}, {
							"mData" : null
						}, {
							"mData" : "salesPrice"
						}, {
							"mData" : "runCount"
						}, {
							"mData" : "winnerCount"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.salesId + '" name="salesIds"/>';
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
											if(row.planStatus == '3'){ //已结束
												return row.curCount;
											}else{
												return row.curCount -1;	
											}
										},
										"targets" : 4
									},
									{
										 "render" : function(data, type, row, meta) {
										    var dom = '';
										    if(row.startTime !=null){
										    	dom +='起：'+row.startTime+'<br/>';
										    	//dom +='至：'+row.endTime;
										    }else{
										    	dom +='无';
										    }
											return dom;
										},
										"targets" : 8
									},
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											if(row.activityStatus == 2){
												dom +='<span class="label radius">即将开始</span>';
											}else if(row.activityStatus == 3){
												dom +='<span class="label label-success radius">进行中</span>';
											}else if(row.activityStatus ==1){
												dom +='<span class="label radius">已结束</span>';
											}
											return dom;
										},
										"targets" : 9
									} ,
									{
										"render" : function(data, type, row, meta) {
											return 2 == row.salesStatus ? '<span class="label label-success radius">在线</span>' : '<span class="label radius">离线</span>';
										},
										"targets" : 10
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
										if(row.activityStatus ==3 && 2 == row.salesStatus){
											dom += '<a title="查看" href="javascript:void(0)" onclick="lottery_look(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe695;</i></a>';
										}else{
											dom += '<a title="编辑" href="javascript:void(0)" onclick="lottery_edit(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										}
										
										return dom;
									},
									//指定是第三列
									"targets" : 11
								} ]
					});
	
			});
	
	
		/*管理员-普通商品-添加*/
		function lottery_add() {
			var url = '/lottery/toLotteryEdit.do' + '?exType=Add';
			layer_show('添加普通商品', url, null, 510, function() {
				myDataTable.fnDraw(true);
			},true);
		}
		/*管理员-普通商品-编辑*/
		function lottery_edit(salesId) {
			var url = '/lottery/toLotteryEdit.do' + '?salesId='+ salesId +'&exType=UPDATE';
			layer_show('修改普通商品', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
		}
	
		function lottery_look(salesId) {
			var url = '/lottery/toLotteryEdit.do' + '?salesId='+ salesId +'&exType=LOOK';
			layer_show('修改普通商品', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
		}
		function searchLottery(){
			myDataTable.fnDraw(true);
		}
		//批量删除
		function batch_del() {
			var chk_value = [];
			$("input[name=salesIds]:checked").each(function() {
				chk_value.push($(this).val());
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
					url : '/lottery/batchDeleteLottery.do',
					data : {
						salesIds : chk_value
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