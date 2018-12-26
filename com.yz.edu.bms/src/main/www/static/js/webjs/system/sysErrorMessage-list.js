var myDataTable;
		$(function() {
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/sysErrorMessage/findAllSysErrorMessage.do",
							type : "post",
							data : {
								"errorCode" : function() {
									return $("#errorCode").val();
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
						"language" : {
							"emptyTable" : "没有检索到数据！",
							"zeroRecords" : "没有检索到数据！",
							"lengthMenu" : "每页显示 _MENU_ 条"
						},
						columns : [ {
							"mData" : null
						}, {
							"mData" : "errorCode"
						}, {
							"mData" : "sysMsg"
						}, {
							"mData" : "appMsg"
						}, {
							"mData" : "description"
						}, {
							"mData" : null
						} ],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.errorCode + '" name="errorCodes"/>';
									},
									"targets" : 0
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';

										dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.errorCode + '\')" class="ml-5" style="text-decoration: none">';
										dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
										dom += '&nbsp;&nbsp;&nbsp;';
										dom += '<a title="删除" href="javascript:;" onclick="member_del(this,\'' + row.errorCode + '\')" class="ml-5" style="text-decoration: none">';
										dom += '<i class="Hui-iconfont">&#xe6e2;</i></a>';

										return dom;
									},
									//指定是第三列
									"targets" : 5
								} ]
					});

		});

		/*用户-添加*/
		function member_add() {
			var url = '/sysErrorMessage/edit.do' + '?exType=ADD';
			layer_show('添加系统错误码', url, null, 510, function() {
				myDataTable.fnDraw(true);
			});
		}
		
		/*用户-编辑*/
		function member_edit(errorCode) {
			var url = '/sysErrorMessage/edit.do' + '?errorCode=' + errorCode + '&exType=UPDATE';
			layer_show('修改系统错误码', url, null, 510, function() {
				myDataTable.fnDraw(true);
			});
		}
		
		/*用户-删除*/
		function member_del(obj, errorCode) {
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/sysErrorMessage/deleteSysErrorMessage.do',
					data : {
						id : errorCode
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('已删除!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(true);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('删除失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(true);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}

		function delAll() {
			var chk_value = [];
			$("input[name=errorCodes]:checked").each(function() {
				chk_value.push($(this).val());
			});

			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/sysErrorMessage/deleteAllSysErrorMessage.do',
					data : {
						idArray : chk_value
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('已删除!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(true);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('删除失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(true);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}
		
		function search(){
			myDataTable.fnDraw(true);
		}