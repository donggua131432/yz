var myDataTable;
$(function() {
	//定义员工变量，以免重复获取
    var empIdJson=[];
    $.ajax({
         type: "POST",
         dataType : 'json',
         url: '/employ/list.do',
         success: function(data){
        	 empIdJson = data.body.data;
         }
    });
	//初始部门下拉框
 	_simple_ajax_select({
			selectId : "dpId",
			searchUrl : '/dep/selectList.do',
			sData : {},
			showText : function(item) {
				return item.dpName;
			},					
			showId : function(item) {
				return item.dpId;
			},
			placeholder : '--请选择部门--'
	});	
 	$("#dpId").append(new Option("", "", false, true));
 	
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/group/list.do',
					data : {
						"groupName" : function(){
							return $("#groupName").val();
						},
						"dpId" : function() {
							return $("#dpId").val();
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
					"mData" : "groupName"
				}, {
					"mData" : null
				}, {
					"mData" : "createUser"
				}, {
					"mData" : "createTime"
				}, {
					"mData" : null
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.dpId + '" name="campusIds"/>';
							},
							"targets" : 0
							},{
							"targets" : 1,
                        	"class":"text-l"
							},{
                    		 "targets" : 2,
                    		 "class":"text-l"
                    		},
							{
								"render" : function(data, type, row, meta) {
									 for(var i=0;i<empIdJson.length;i++){
						          		 if(empIdJson[i].empId == row.empId){
						          			 return empIdJson[i].empName;
						          		 }
						          	 }
									return "";
								},
								"targets" : 3
							} ,
							{
								"render" : function(data, type, row, meta) {
									return 1 == row.isStop ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
								},
								"targets" : 6
							} ,
							{
							"render" : function(data, type, row, meta) {
								var dom = '';
								if(row.isStop == 0){
									dom += '<a onClick="group_stop(this,\''+ row.groupId +'\')" href="javascript:;" title="停用" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>';
								}else if(row.isStop == 1){
									dom += '<a onClick="group_start(this,\''+ row.groupId +'\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>';
								}
								dom += '<a title="编辑" href="javascript:void(0)" onclick="group_edit(\'' + row.groupId + '\')" class="ml-5" style="text-decoration:none">';
								dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
							
								return dom;
							},
							//指定是第三列
							"targets" : 7
						} ]
			});

	});


/*管理员-部门招生组-添加*/
function group_add() {
	var url = '/group/edit.do' + '?exType=Add';
	layer_show('添加招生组', url, null, 510, function() {
		myDataTable.fnDraw(true);
	});
}
/*管理员-部门招生组-编辑*/
function group_edit(groupId) {
	var url = '/group/edit.do' + '?groupId='+ groupId +'&exType=UPDATE';
	layer_show('修改招生组', url, null, 510, function() {
		myDataTable.fnDraw(false);
	});
}

function searchGroup(){
	myDataTable.fnDraw(true);
}
/*管理员-停用*/
function group_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/group/groupBlock.do',
			data : {
				groupId : id,
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
function group_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		//此处请求后台程序，下方是成功后的前台处理……
		$.ajax({
			type : 'POST',
			url : '/group/groupBlock.do?groupId='+id+'&exType=START',
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