var myDataTable;
    $(function() {

		$('select').select2({
			placeholder : "--请选择--",
			allowClear : true,
			width : "59%"
		});
    	_simple_ajax_select({
			selectId : "taskIds",
			searchUrl : '/studyActivity/findAllTaskInfo.do',
			sData : {},
			showText : function(item) {
				return item.task_title;
			},					
			showId : function(item) {
				return item.task_id;
			},
			placeholder : '--请选择任务--'
		});
		$("#taskIds").append(new Option("", "", false, true));
		
    	  //初始状态
	   _init_select("isAllow",[
	  	 {
	  	 			"dictValue":"0","dictName":"否"
	  	 		},
	  	 {
	  	 			"dictValue":"1","dictName":"是"
	  	 		}
	  	 ]);
	    _init_select("isNeedCheck",[
	  	 {
	  	 			"dictValue":"0","dictName":"否"
	  	 		},
	  	 {
	  	 			"dictValue":"1","dictName":"是"
	  	 		}
	  	 ]);
	   myDataTable = $('.table-sort').dataTable(
			{
                "processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/studyActivity/list.do',
					data : {
						"taskId" : function (){
							return $("#taskIds").val();
						},
						"creater" : function(){
							return $("#creater").val();
						},
						"isAllow" : function(){
							return $("#isAllow").val();
						},
						"isNeedCheck" : function(){
							return $("#isNeedCheck").val();
						},
						"startTimeStart" : function() {
							return $("#startTimeStart").val();
						},
						"startTimeEnd" : function() {
							return $("#startTimeEnd").val();
						},
						"endTimeStart" : function() {
							return $("#endTimeStart").val();
						},
						"endTimeEnd" : function() {
							return $("#endTimeEnd").val();
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
					"mData" : "taskTitle"
				}, {
					"mData" : "taskContent"
				}, {
					"mData" : "creater"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : "totalCount"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.taskId + '" name="taskIdss"/>';
							},
							"targets" : 0
							},
							{
								"render" : function(data, type, row, meta) {
									return 0 == row.isAllow ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
								},
								"targets" : 6
							},{
                                "targets" : 1,
                                "class":"text-l"
                            },{
                                "targets" : 2,
                                "class":"text-l"
                             },{"render" : function(data, type, row, meta) {
                                     if(!row.startTime){
                                         return '-'
                                     }
                                     var date=row.startTime.substring(0,10)
                                     var time=row.startTime.substring(11)
                                     return date+'<br>'+time
                                },
                                "targets" : 4,"class":"text-l no-warp"
                            },{"render" : function(data, type, row, meta) {
                                   if(!row.endTime){
                                       return '-'
                                   }
                                   var date=row.endTime.substring(0,10)
                                   var time=row.endTime.substring(11)
                                   return date+'<br>'+time
                               },
                        "targets" : 5,"class":"text-l no-warp"
                    },{
								"render" : function(data, type, row, meta) {
									var dom = '';
									if(row.issuer == $("#userId").val()){
										dom +='<a class="tableBtn blue" title="选择学员" href="javascript:;" onclick="choose_student(\''+row.taskId+'\')">选择学员</a>';
									}else{
										dom +='<a class="tableBtn normal" title="查看学员" href="javascript:;" onclick="look_student(\''+row.taskId+'\')">查看学员</a>';
									}
									return dom;
								},
								"targets" : 8
							} ,
							{
								"render" : function(data, type, row, meta) {
									var dom = '';
									dom +='<a class="tableBtn normal" title="查看结果" href="javascript:;" onclick="look_result(\''+row.taskId+'\')">查看结果</a>';
									return dom;
								},
								"targets" : 9
							} ,
							{
								"render" : function(data, type, row, meta) {
									var dom = '';
									dom +='<a class="tableBtn normal" title="导出未完成" href="javascript:;" onclick="exportUnfinished(\''+row.taskId+'\')">导出未完成</a>';
									return dom;
								},
								"targets" : 10
							} ,
							{
							"render" : function(data, type, row, meta) {
								var dom = '';
								if(row.issuer == $("#userId").val()){
									dom += '<a class="maincolor" title="编辑" href="javascript:;" onclick="task_edit(\'' + row.taskId + '\')"><i  class="iconfont icon-edit"></i></a>&nbsp;&nbsp;';
								}else{
									dom += '<a class="maincolor" title="查看" href="javascript:;" onclick="task_look(\'' + row.taskId + '\')"><i  class="iconfont icon-chakan"></i></a>&nbsp;&nbsp;';
								}
								if(row.isAllow == 0){
									dom += '<a class="maincolor" title="启用" href="javascript:;" onclick="task_start(\'' + row.taskId + '\')"><i  class="iconfont icon-qiyong"></i></a>';
								}else{
									dom += '<a class="maincolor" title="停用" href="javascript:;" onclick="task_stop(\'' + row.taskId + '\')"><i  class="iconfont icon-tingyong"></i></a>';
								}
								
							
								return dom;
							},
							//指定是第三列
							"targets" : 11
						} ]
			});
     });
    
    /*管理员-教务任务-添加*/
    function task_add() {
    	var url = '/studyActivity/toEdit.do' + '?exType=Add';
    	layer_show('添加学服任务', url, null, null, function() {
    		myDataTable.fnDraw(true);
    	});
    }
    /*管理员-教务任务-编辑*/
    function task_edit(taskId) {
    	var url = '/studyActivity/toEdit.do' + '?taskId='+ taskId +'&exType=UPDATE';
    	layer_show('修改学服任务', url, null, null, function() {
    		myDataTable.fnDraw(false);
    	});
    }
    function task_look(taskId) {
    	var url = '/studyActivity/toEdit.do' + '?taskId='+ taskId +'&exType=LOOK';
    	layer_show('修改学服任务', url, null, null, function() {
    		myDataTable.fnDraw(false);
    	});
    }
    //选择学员
    function choose_student(taskId){
    	var url = '/studyActivity/toStu.do' + '?taskId='+ taskId+'&exType=CHOOSE';
    	layer_show('选择分配学员', url, null, null, function() {
    		myDataTable.fnDraw(false);
    	},true);
    }
    //查看学员
    function look_student(taskId){
    	var url = '/studyActivity/toStu.do' + '?taskId='+ taskId+'&exType=LOOK';
    	layer_show('选择分配学员', url, null, null, function() {
//    		myDataTable.fnDraw(false);
    	},true);
    }
    //查看结果
    function look_result(taskId){
    	var url = '/studyActivity/getStatistics.do' + '?taskId='+ taskId;
    	layer_show('完成情况', url, null, null, function() {
//    		myDataTable.fnDraw(false);
    	});
    }
    //导出未完成的
    function exportUnfinished(taskId){
    	$("#taskId").val(taskId);
    	$("#export-form").submit();
    }
    
    /*搜素*/
    function searchTask(){
    	myDataTable.fnDraw(true);
    }
    
    /*管理员-启用*/
    function task_start(id){
    	layer.confirm('确认要启用吗？',function(index){
    		//此处请求后台程序，下方是成功后的前台处理……
    		$.ajax({
    			type : 'POST',
    			url : '/studyActivity/activityBlock.do?taskId='+id+'&exType=START',
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
    
    /*管理员-禁用*/
    function task_stop(id){
    	layer.confirm('确认要禁用吗？',function(index){
    		//此处请求后台程序，下方是成功后的前台处理……
    		$.ajax({
    			type : 'POST',
    			url : '/studyActivity/activityBlock.do?taskId='+id+'&exType=STOP',
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
    
	/*用户-导入*/
	function excel_export() {
		var url = '/teacher/excelImport.do';
		layer_show('导入教师信息', url, null, 510, function() {
			myDataTable.fnDraw(true);
		});
	}
	
	/*批量删除*/
    function delAll() {
        var chk_value = [];
        $("input[name=taskIdss]:checked").each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/task/deletes.do',
                data: {
                    taskIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已删除!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                    }
                }
            });
        });
    }