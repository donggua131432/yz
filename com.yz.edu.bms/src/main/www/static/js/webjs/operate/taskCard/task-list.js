var myDataTable;
			$(function() {
				_init_select("taskStatus",dictJson.taskStatus,null);
				
				$(".select").select2({
	                placeholder: "--请选择--",
	                allowClear: true
	            });
				myDataTable = $('.table-sort')
						.dataTable(
								{
									"serverSide" : true,
									"dom" : 'rtilp',
									"ajax" : {
										url : "/taskCard/list.do",
										type : "post",
										data : {
											"taskName": function () {
			                                    return $("#taskName")
			                                        .val();
			                                },
			                                "taskStatus": function () {
			                                    return $("#taskStatus")
			                                        .val();
			                                }
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
										"mData" : "taskName"
									}, {
										"mData" : null
									}, {
										"mData" : "startTime"
									}, {
										"mData" : "endTime"
									}, {
										"mData" : null
									}, {
										"mData" : null
									}, {
										"mData" : "getCount"
									}, {
										"mData" : "finishCount"
									}, {
										"mData" : null
									}, {
										"mData" : null
									} ],
									"columnDefs" : [
											{
												"render" : function(data, type,
														row, meta) {
													return '<input type="checkbox" value="'+ row.taskId + '" name="taskIds"/>';
												},
												"targets" : 0
											},
											{
												"render" : function(data, type,
														row, meta) {

													var dom = '';
													dom += _findDict('taskCardType',row.taskType);
													return dom;
												},
												"targets" : 2
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													dom += '需成功邀请' + row.taskTarget + '人';
													return dom;
												},
												"targets" : 5
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													dom += row.taskReward + '智米';
													return dom;
												},
												"targets" : 6
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													dom += _findDict('taskStatus',row.taskStatus);
													return dom;
												},
												"targets" : 9
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													var status = row.taskStatus;
														
													if('1' == status){
														if(compareDate(row.nowDate,row.startTime) && compareDate(row.endTime,row.nowDate)){
															dom += '<a title="发布" href="javascript:;" onclick="task_publish(this,\''
																+ row.taskId
																+ '\')" class="ml-5" style="text-decoration: none">';
															dom += '<i class="iconfont icon-qiyong"></i></a>';
														}
													}
													dom += '<a title="编辑" href="javascript:void(0)" onclick="task_edit(\''
															+ row.taskId
															+ '\')" class="ml-5" style="text-decoration:none">';
													dom += '<i class="iconfont icon-edit"></i></a>';
													if('1' == status){
														dom += '<a title="删除" href="javascript:;" onclick="task_del(this,\''
																+ row.taskId
																+ '\')" class="ml-5" style="text-decoration: none">';
														dom += '<i class="iconfont icon-shanchu"></i></a>';
													}
													return dom;
												},
												"targets" : 10
											} ]
								});
			});

			function task_add() {
				var url = "/taskCard/toAdd.do";
				layer_show('添加任务', url, 900, 600, function() {
					myDataTable.fnDraw(true);
				});

			}
			
			function compareDate(s1,s2){
			  return ((new Date(s1.replace(/-/g,"\/")))>=(new Date(s2.replace(/-/g,"\/"))));
			}

			function task_edit(taskId) {
				var url = "/taskCard/toEdit.do" + '?taskId=' + taskId;
				layer_show('修改任务', url, null, 510, function() {
					myDataTable.fnDraw(true);
				});
			}

			/*管理员-删除*/
			function task_del(obj, taskId) {
				layer.confirm('确认要删除吗？', function(index) {
					$.ajax({
						type : 'POST',
						url : "/taskCard/delete.do",
						data : {
							taskId : taskId
						},
						dataType : 'json',
						success : function(data) {
							if (data.code == _GLOBAL_SUCCESS) {
								layer.msg('已删除!', {
									icon : 1,
									time : 1000
								});
							}
							myDataTable.fnDraw(true);
						}
					});
				});
			}
			
			function task_publish(obj, taskId) {
				layer.confirm('确认发布吗？', function(index) {
					$.ajax({
						type : 'POST',
						url : "/taskCard/publish.do",
						data : {
							taskId : taskId
						},
						dataType : 'json',
						success : function(data) {
							if (data.code == _GLOBAL_SUCCESS) {
								layer.msg('已发布!', {
									icon : 1,
									time : 1000
								});
							}
							myDataTable.fnDraw(true);
						}
					});
				});
			}

			function searchBanner() {
				myDataTable.fnDraw(true);
			}
