var myDataTable;
	$(function() {
		myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/class/list.do",
					data : {
						"courseName" : function(){
							return $("#courseName").val();
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
					"mData" : "classId"
				}, {
					"mData" : "courseName"
				}, {
					"mData" : "className"
				}, {
					"mData" : "startTime"
				}, {
					"mData" : "endTime"
				}, {
					"mData" : "mins"
				}, {
					"mData" : "classPlace"
				}, {
					"mData" : "teacher"
				}, {
					"mData" : "mobile"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}],
				"columnDefs" : [
		            {"render" : function(data, type, row, meta) {
						return '<input type="checkbox" value="'+ row.classId + '" name="classIds"/>';
					},
					"targets" : 0
					},
					{
						"render" : function(data, type, row, meta) {
							var dom = '查看';
							return dom;
						},
						"targets" : 10
					} ,
					{
						"render" : function(data, type, row, meta) {
							return 2 ;
						},
						"targets" : 11
					} ,
					{
					"render" : function(data, type, row, meta) {
						var dom = '';
						if(row.activityStatus == '0'){
							dom += '<a title="编辑" href="javascript:void(0)" onclick="exchange_edit(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
							dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
						}else{
							dom += '<a title="查看" href="javascript:void(0)" onclick="exchange_look(\'' + row.salesId + '\')" class="ml-5" style="text-decoration:none">';
							dom += '<i class="Hui-iconfont f-18">&#xe695;</i></a>';
						}
						
						return dom;
					},
					//指定是第三列
					"targets" : 12
				} ]
			});

		});


	/*管理员-普通商品-添加*/
	function addCourse() {
		var url = '/class/toClassEdit.do' + '?exType=Add';
		layer_show('添加课程班级', url, null, null, function() {
			myDataTable.fnDraw(true);
		});
	}
	/*管理员-普通商品-编辑*/
	function exchange_edit(salesId) {
		var url = '/exchange/toExchangeEdit.do' + '?salesId='+ salesId +'&exType=UPDATE';
		layer_show('修改普通商品', url, null, 510, function() {
			myDataTable.fnDraw(true);
		},true);
	}
	/*查看*/
	function exchange_look(salesId) {
		var url = '/exchange/toExchangeEdit.do' + '?salesId='+ salesId +'&exType=LOOK';
		layer_show('修改普通商品', url, null, 510, function() {
			myDataTable.fnDraw(true);
		},true);
	}


	function searchCourse(){
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
				url : '/exchange/batchDeleteExchange.do',
				data : {
					salesIds : chk_value
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