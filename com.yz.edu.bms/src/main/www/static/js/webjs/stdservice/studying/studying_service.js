var myDataTable;
$(function() {
 	
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/studying/showServicesData.do",
					data:{
						"learnId" : function() {
							return $("#learnId").val();
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
					"mData" : "taskTitle"
				}, {
					"mData" : "stdNo"
				}, {
					"mData" : "stdName"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : "empName"
				}],
				"columnDefs" : [
			            {"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.taskId+';'+row.learnId + '" name="taskIds"/>';
						   },
						"targets" : 0
						},
						{"render" : function(data, type, row, meta) {
								return row.taskType;
						   },
						   "targets" : 1
						},
						{
                            "targets" : 2,"class":"text-l"
						},
						{"render" : function(data, type, row, meta) {
								return row.grade+'级';
						   },
						   "targets" : 5
					     },
						{
							"render" : function(data, type,row, meta) {

								var dom = '';

								dom += _findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
								dom += row.unvsName+"<br>";

								dom += _findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]"
                                dom += row.pfsnName;
                                dom += '(' + row.pfsnCode + ')';
								return dom;
							},
							"targets" : 6,
                            "class":"text-l"
						},
						{"render" : function(data, type, row, meta) {
							return _findDict(
									"stdStage",
									row.stdStage);
							},
							"targets" : 7
						},
						{"render" : function(data, type, row, meta) {
								return '';
							},
							"targets" : 8
						},
						{"render" : function(data, type, row, meta) {
							return 0 == row.taskStatus ? '<span class="label radius">未完成</span>' : '<span class="label label-success radius">已完成</span>';
							},
							"targets" : 10
						}
				  ]
			});

	});
