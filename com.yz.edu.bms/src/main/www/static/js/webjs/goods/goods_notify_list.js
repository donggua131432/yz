var myDataTable;
		$(function() {
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/notify/list.do",
							data : {
								"goodsName" : function(){
									return $("#goodsName").val();
								},
								"isAllow" : function(){
									return $("#isAllow").val();
								},
								"goodsUse" : function(){
									return $("#goodsUse").val();
								},
								"goodsType" : function(){
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
							"mData" : "notifyId"
						}, {
							"mData" : null
						}, {
							"mData" : "salesName"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.notifyId + '" name="notifyIds"/>';
									},
									"targets" : 0
									},
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											var salesType = _findDict("salesType",row.salesType);
											dom  +=(salesType==null?'未知类型':salesType);
											return dom;
										},
										"targets" : 2
									},
									{
										 "render" : function(data, type, row, meta) {
											var dom = '';
											dom +='昵      称：'+(row.nickName==null?'无':row.nickName) +'</br>';
											dom +='手机号：'+(row.mobile == null?'无':row.mobile);
											
											return dom;
										},
										"sClass":'text-l',
										"targets" : 4
									},
									{
										"render" : function(data, type, row, meta) {
											return 1 == row.isNotify ? '<span class="label radius">已提醒</span>' : '<span class="label label-success radius">待提醒</span>';
										},
										"targets" : 5
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
									    dom += '&nbsp;&nbsp;&nbsp;';
				                        dom += '<a title="删除" href="javascript:;" onclick="notify_del(this,\'' + row.notifyId + '\')" class="ml-5" style="text-decoration: none">';
				                        dom += '<i class="Hui-iconfont f-18">&#xe609;</i></a>';
				                        
										return dom;
									},
									//指定是第三列
									"targets" : 6
								} ]
					});
	
		});
	
		function searchNotify(){
			myDataTable.fnDraw(true);
		}
		
		/*管理员-删除*/
		function notify_del(obj,id){
			layer.confirm('确认要删除此条提醒吗？',function(index){
				//此处请求后台程序，下方是成功后的前台处理……
				$.ajax({
					type : 'POST',
					url : '/notify/singleDeleteSalesNotify.do',
					data : {
						notifyId : id,
					},
					dataType : 'json',
					success : function(data) {
						myDataTable.fnDraw(false);
						layer.msg('删除成功!',{icon: 5,time:1000});
					},
					error : function(data) {
						layer.msg('删除失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
			});
		}
		//批量删除
		function batch_del() {
			var chk_value = [];
			$("input[name=notifyIds]:checked").each(function() {
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
					url : '/notify/batchDeleteSalesNotify.do',
					data : {
						notifyIds : chk_value
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