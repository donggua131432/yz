var myDataTable;
$(function() {

	//初始化年级下拉框
	 _init_select("grade",dictJson.grade);
	//初始招生类型下拉框
	_init_select("recruitType",dictJson.recruitType);
 	
	myDataTable = $('.table-sort').dataTable(
			{
                "processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/taskfollow/getTaskList.do",
					data:{
						"unvsName" : function() {
							return $("#unvsName").val();
						},"recruitType" : function() {
							return $("#recruitType").val();
						},"grade" : function() {
							return $("#grade").val();
						},"pfsnName" : function() {
							return $("#pfsnName").val();
						},"stdName" : function() {
							return $("#stdName").val();
						},"idCard" : function() {
							return $("#idCard").val();
						},"mobile" : function() {
							return $("#mobile").val();
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
				}, {
					"mData" : null
				}, {
					"mData" : null
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
						{"render" : function(data, type, row, meta) {
								return row.grade+'级';
						   },
						   "targets" : 5
					     },
						{
							"render" : function(data, type,row, meta) {

								var dom = '';

                                if(_findDict("recruitType", row.recruitType).indexOf("成人")!=-1){
                                    dom += "[成教]";
                                }else {
                                    dom += "[国开]";
                                }
								dom += row.unvsName+'</br>';

                                if(_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1){
                                    dom += "[专科]";
                                }else {
                                    dom += "[本科]";
                                }
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
						},{"render" : function(data, type, row, meta) {
							  return ''
							},
							"targets" : 11
						},
						{"render" : function(data, type, row, meta) {
							var dom = '';
							dom += '<a class="tableBtn normal" href="javascript:;" title="手动完成" onclick="finishTask(\'' + row.taskId+'\',\''+row.learnId+ '\')">手动完成</a>';
							
							return dom;
						  },
						  "targets" : 12
					}
				  ]
			});

	});

	function searchTask(){
		myDataTable.fnDraw(true);
	}

	function finishTask(taskId,learnId){
		layer.confirm('确认要完成吗？',function(index){
    		//此处请求后台程序，下方是成功后的前台处理……
    		$.ajax({
    			type : 'POST',
    			url : '/taskfollow/finishTask.do'+'?taskId='+taskId+'&learnId='+learnId,
    			dataType : 'json',
    			success : function(data) {
    				myDataTable.fnDraw(false);
    				layer.msg('已完成!', {icon: 6,time:1000});
    			},
    			error : function(data) {
    				layer.msg('失败！', {
    					icon : 1,
    					time : 1000
    				});
    				myDataTable.fnDraw(false);
    			},
    		});
    		
    	
    	});
	}
    function batchFinish() {
		var chk_value = [];
		$("input[name=taskIds]:checked").each(function() {
			var data={'taskId':$(this).val().split(";")[0],'learnId':$(this).val().split(";")[1]}
			chk_value.push(data);
		});
		if(chk_value.length ==0){
			layer.msg('请选择要操作的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要完成吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/taskfollow/batchFinish.do',
				data : {
					taskIds : JSON.stringify(chk_value)
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('已完成!', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('操作失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}