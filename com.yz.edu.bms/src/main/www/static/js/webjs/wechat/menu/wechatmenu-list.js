var myDataTable;
$(function() {
	myDataTable = $('.table-sort')
			.dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/wechatmenu/list.do",
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
							"mData" : null
						}, {
							"mData" : "menuTitle"
						}, {
							"mData" : "pName"
						}, {
							"mData" : "menuContent"
						}, {
							"mData" : "weight"
						}, {
							"mData" : "pubName"
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
										return '<input type="checkbox" value="'+ row.id + '" name="ids"/>';
									},
									"targets" : 0
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										var dom = '';
										if (row.isAllow == '1') {
											dom += '<a onClick="wechatmenu_stop(\''
													+ row.id
													+ '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
										} else if (row.isAllow == '0') {
											dom += '<a onClick="wechatmenu_start(\''
													+ row.id
													+ '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
										}
										dom += ' &nbsp;';
										dom += '<a title="编辑" href="javascript:void(0)" onclick="wechatmenu_edit(\''
												+ row.id
												+ '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="iconfont icon-edit"></i></a>';
										return dom;
									},
									"targets" : 8
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
									"targets" : 7
								},
								{
									"render" : function(
											data, type,
											row, meta) {
										return _findDict("eventType",row.eventType);
									},
									"targets" : 6
								}]
					});
});

function wechatmenu_add() {
	var url = '/wechatmenu/toAdd.do';
	layer_show('添加菜单', url, 750, 460, function() {
		myDataTable.fnDraw(true);
	});

}

function wechatmenu_refresh() {
	layer.confirm('确认刷新公众号菜单吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatmenu/refreshWechatMenu.do',
			data : {
				
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					myDataTable.fnDraw(false);
					layer.msg('刷新成功!', {
						icon : 6,
						time : 1000
					});
				}
			}
		});
	});
}

function wechatmenu_edit(id) {
	var url = '/wechatmenu/toEdit.do' + '?id='
			+ id;
	layer_show('修改菜单', url, 750, 460, function() {
		myDataTable.fnDraw(false);
	});
}


function wechatmenu_stop(id) {
	layer.confirm('确认要停用吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatmenu/block.do',
			data : {
				id : id,
				exType : 'BLOCK'
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					myDataTable.fnDraw(false);
					layer.msg('已停用!', {
						icon : 1,
						time : 1000
					});
				}
			}
		});
	});
}

function wechatmenu_start(id) {
	layer.confirm('确认要启用吗？', function(index) {
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/wechatmenu/block.do',
			data : {
				id : id,
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
function searchwechatmenu() {
	myDataTable.fnDraw(true);
}
