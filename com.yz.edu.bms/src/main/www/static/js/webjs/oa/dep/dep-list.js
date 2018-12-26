var myDataTable;
$(function() {
	
	//初始校区下拉框
 	_simple_ajax_select({
			selectId : "campusId",
			searchUrl : '/campus/selectList.do',
			sData : {},
			showText : function(item) {
				return item.campusName;
			},					
			showId : function(item) {
				return item.campusId;
			},
			placeholder : '--请选择校区--'
	});	
 	$("#campusId").append(new Option("", "", false, true));
 	
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/dep/list.do",
					data : {
						"dpName" : function(){
							return $("#dpName").val();
						},
						"campusId" : function() {
							return $("#campusId").val();
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
					"mData" : "dpName"
				}, {
					"mData" : "campusName"
				}, {
					"mData" : "empName"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.dpId + '" name="campusIds"/>';
							},
							"targets" : 0
							},
							{"render" : function(data, type, row, meta) {
								 if(row.isRecruit == 1){
									 return '是';
								 }else{
									 return '否';
								 }
								},
								"targets" : 4
							},
							{"render" : function(data, type, row, meta) {
								 if(row.isParticipate == 1){
									 return '是';
								 }else{
									 return '否';
								 }
								},
								"targets" : 5
							},
							{
								"render" : function(
										data, type,
										row, meta) {
									var dom = '';
									for (var i = 0; i < row.recruitTypes.length; i++) {
										var recruitType = row.recruitTypes[i];
										if (i == (row.recruitTypes.length - 1)) {
											dom += _findDict(
													"recruitType",
													recruitType)
													+ ' ';
											break;
										}
										dom += _findDict(
												"recruitType",
												recruitType)
												+ '; ';

									}
									if (row.recruitTypes.length < 1) {
										dom = '无';
									}
									return dom;
								},
								"targets" : 6
							},
							{
								"render" : function(data, type, row, meta) {
									return 1 == row.isStop ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
								},
								"targets" : 7
							} ,
							{
							"render" : function(data, type, row, meta) {
								var dom = '';
								if(row.isStop == '0'){
									dom += '<a onClick="dep_stop(this,\''+ row.dpId +'\')" href="javascript:;" title="停用" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>';
								}else if(row.isStop == '1'){
									dom += '<a onClick="dep_start(this,\''+ row.dpId +'\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>';
								}
								dom += '<a title="编辑" href="javascript:void(0)" onclick="dep_edit(\'' + row.dpId + '\')" class="ml-5" style="text-decoration:none">';
								dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
							
								return dom;
							},
							//指定是第三列
							"targets" : 8
						} ]
			});

	});


/*管理员-部门-添加*/
function dep_add() {
	var url = '/dep/edit.do' + '?exType=Add';
	layer_show('添加部门', url, null, 510, function() {
		myDataTable.fnDraw(true);
	});
}
/*管理员-部门-编辑*/
function dep_edit(dpId) {
	var url = '/dep/edit.do' + '?dpId='+ dpId +'&exType=UPDATE';
	layer_show('修改部门', url, null, 510, function() {
		myDataTable.fnDraw(false);
	});
}

function searchDep(){
	myDataTable.fnDraw(true);
}
/*管理员-停用*/
function dep_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/dep/depBlock.do',
			data : {
				dpId : id,
				exType : 'BLOCK'
			},
			dataType : 'json',
			success : function(data) {
				myDataTable.fnDraw(false);
				layer.msg('已停用!',{icon: 5,time:1000});
			},
			error : function(data) {
				layer.msg('停用失败！', {
					icon : 1,
					time : 1000
				});
				myDataTable.fnDraw(false);
			},
		});
	});
}

/*管理员-启用*/
function dep_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/dep/depBlock.do'+'?dpId='+id+'&exType=START',
			dataType : 'json',
			success : function(data) {
				myDataTable.fnDraw(false);
				layer.msg('已启用!', {icon: 6,time:1000});
			},
			error : function(data) {
				layer.msg('启用失败！', {
					icon : 1,
					time : 1000
				});
				myDataTable.fnDraw(false);
			},
		});
		
	
	});
	
}