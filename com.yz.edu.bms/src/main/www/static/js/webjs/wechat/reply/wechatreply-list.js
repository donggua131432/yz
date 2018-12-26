var myDataTable;
$(function() {
	
	$("#status").select2({
		placeholder : "--请选择--",
		allowClear : true
	});
	
	_init_select("msgType",dictJson.msgType,null);

	myDataTable = $('.table-sort')
			.dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/wechatreply/list.do",
							type : "post",
							data : {
								"msgType" : function(){
									return $("#msgType").val();
								},
								"status" : function(){
									return $("#status").val();
								},
								"keyword" : function(){
									return $("#keyword").val();
								},
							}
						},
						"pageLength" : 10,
						"pagingType" : "full_numbers",
						"ordering" : false,
						"searching" : false,
						"createdRow" : function(row, data,
								dataIndex) {
							$(row).addClass('text-c');
						},
						"language" : _my_datatables_language,
						columns : [ {
							"mData" : "keyword"
						}, {
							"mData" : "wechatId"
						}, {
							"mData" : null
						}, {
							"mData" : "content"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						} ],
						"columnDefs" : [
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if(row.articles != null){
											for (var i = 0; i < row.articles.length; i++) {
												var a = row.articles[i];
												dom += a.url + '<br>';
											}
										}
										return dom;
									},
									"targets" : 7
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if(row.articles != null){
											for (var i = 0; i < row.articles.length; i++) {
												var a = row.articles[i];
												dom += a.picUrl + '<br>';
											}
										}
										return dom;
									},
									"targets" : 6
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if(row.articles != null){
											for (var i = 0; i < row.articles.length; i++) {
												var a = row.articles[i];
												dom += a.description + '<br>';
											}
										}
										return dom;
									},
									"targets" : 5
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if(row.articles != null){
											for (var i = 0; i < row.articles.length; i++) {
												var a = row.articles[i];
												dom += a.title + '<br>';
											}
											return dom;
										}
									},
									"targets" : 4
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										return _findDict("msgType",row.msgType);
									},
									"targets" : 2
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if (row.status == '1') {
											dom += '<a onClick="reply_stop(\''
													+ row.replyId
													+ '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
										} else if (row.status == '2') {
											dom += '<a onClick="reply_start(\''
													+ row.replyId
													+ '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
										}
										dom += ' &nbsp;';
										dom += '<a title="编辑" href="javascript:void(0)" onclick="reply_edit(\''
												+ row.replyId
												+ '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="iconfont icon-edit"></i></a>';
										dom += ' &nbsp;';
										dom += '<a title="删除" href="javascript:;" onclick="reply_del(\'' + row.replyId + '\')" class="ml-5" style="text-decoration: none">';
										dom += '<i class="iconfont icon-shanchu"></i></a>';
										return dom;
									},
									"targets" : 9
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										if (row.status == '1') {
											return '<span class="label label-success radius">已启用</span>';
										} else {
											return '<span class="label label-danger radius">已禁用</span>';
										}
									},
									"targets" : 8
								}]
					});
});

function reply_add() {
	var url = '/wechatreply/toAdd.do';
	layer_show('添加关键字', url, 900, 600, function() {
		myDataTable.fnDraw(true);
	});

}

function reply_del(replyId) {
	var url = '/wechatreply/delete.do';
	layer.confirm('确认删除关键字吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : url,
			data : {
				replyId : replyId
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					myDataTable.fnDraw(false);
					layer.msg('删除成功!', {
						icon : 5,
						time : 1000
					});
				}
			}
		});
	});

}

function reply_edit(id) {
	var url = '/wechatreply/toEdit.do' + '?replyId='
			+ id;
	layer_show('修改关键字', url, null, 510, function() {
		myDataTable.fnDraw(false);
	});
}


function reply_stop(id) {
	layer.confirm('确认要停用吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatreply/block.do',
			data : {
				replyId : id,
				exType : 'BLOCK'
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					myDataTable.fnDraw(false);
					layer.msg('已停用!', {
						icon : 5,
						time : 1000
					});
				}
			}
		});
	});
}

function reply_start(id) {
	layer.confirm('确认要启用吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatreply/block.do',
			data : {
				replyId : id,
				exType : 'START'
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					myDataTable.fnDraw(false);
					layer.msg('已启用!', {
						icon : 6,
						time : 1000
					});
				}
			}
		});

	});

}
function searchwechatreply() {
	myDataTable.fnDraw(true);
}