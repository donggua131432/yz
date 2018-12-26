var myDataTable;
$(function() {


	myDataTable = $('.table-sort')
			.dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/wechatpub/list.do",
							type : "post",
							data : {

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
							"mData" : "pubId"
						}, {
							"mData" : "pubName"
						}, {
							"mData" : null
						}, {
							"mData" : "appId"
						}, {
							"mData" : "appSecret"
						}, {
							"mData" : "welcome"
						}, {
							"mData" : "accessToken"
						}, {
							"mData" : "topUrl"
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
										return _findDict("pubType",row.pubType);
									},
									"targets" : 2
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if (row.isAllow == '1') {
											dom += '<a onClick="wechatpub_stop(\''
													+ row.pubId
													+ '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
										} else if (row.isAllow == '0') {
											dom += '<a onClick="wechatpub_start(\''
													+ row.pubId
													+ '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
										}
										dom += ' &nbsp;';
										dom += '<a title="编辑" href="javascript:void(0)" onclick="wechatpub_edit(\''
												+ row.pubId
												+ '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="iconfont icon-edit"></i></a>';
										return dom;
									},
									"targets" : 9
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										if (row.isAllow == '1') {
											return '<span class="label label-success radius">已启用</span>';
										} else {
											return '<span class="label label-danger radius">已禁用</span>';
										}
									},
									"targets" : 8
								} ]
					});
});

function wechatpub_add() {
	var url = '/wechatpub/toAdd.do';
	layer_show('添加公众号', url, 900, 600, function() {
		myDataTable.fnDraw(true);
	});

}

function wechatpub_edit(pubId) {
	var url = '/wechatpub/toEdit.do' + '?pubId='
			+ pubId;
	layer_show('修改公众号信息', url, null, 510, function() {
		myDataTable.fnDraw(false);
	});
}

/*管理员-删除*/
function wechatpub_del(obj, id) {
	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : '/wechatpub/delete.do',
			data : {
				pubId : id
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					layer.msg('已删除!', {
						icon : 1,
						time : 1000
					});
				}
				myDataTable.fnDraw(false);
			}
		});
	});
}

function wechatpub_stop(pubId) {
	layer.confirm('确认要停用吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatpub/block.do',
			data : {
				pubId : pubId,
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

function wechatpub_start(pubId) {
	layer.confirm('确认要启用吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatpub/block.do',
			data : {
				pubId : pubId,
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
function searchWechatpub() {
	myDataTable.fnDraw(true);
}
