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
                "processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/teacher/list.do',
					data : {
						"campusId" : function() {
							return $("#campusId").val();
						},
						"empName" : function(){
							return $("#empName").val();
						},
						"finishMajor" : function(){
							return $("#finishMajor").val();
						},
						"teach" : function() {
							return $("#teach").val();
						},
						"workPlace" : function() {
							return $("#workPlace").val();
						},
						"mobile" : function() {
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
					"mData" : "empName"
				}, {
					"mData" : "age"
				}, {
					"mData" : "campusName"
				}, {
					"mData" : "finishMajor"
				}, {
					"mData" : "teach"
				}, {
					"mData" : "workPlace"
				}, {
					"mData" : "address"
				}, {
					"mData" : "mobile"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.empId + '" name="empIds"/>';
							},
							"targets" : 0
							},
							{
								"render" : function(data, type, row, meta) {
									var dom = '';
									dom +='每课时：'+(row.hourFee==null?'0':row.hourFee) +'</br>';
									dom +='其它：'+(row.otherFee==null?'0':row.otherFee);
									return dom;
								},
								"sClass":'text-l',
								"targets" : 9
							},{
                                "targets" : 4,
                                "class":"text-l"
                            },{
                                "targets" : 5,
                                "class":"text-l"
                            },{
                               "targets" : 6,
                               "class":"text-l"
                           },{
                               "targets" : 7,
                               "class":"text-l"
                           },{
								"render" : function(data, type, row, meta) {
									return "点击导出";
								},
								"targets" : 10
							} ,
							{
								"render" : function(data, type, row, meta) {
									return "点击导出";
								},
								"targets" : 11
							} ,
							
							{
							"render" : function(data, type, row, meta) {
								var dom = '';
							
								dom += '<a title="编辑" href="javascript:void(0)" onclick="teacher_edit(\'' + row.empId + '\')" class="ml-5" style="text-decoration:none">';
								dom += '<i class="iconfont icon-edit"></i></a>';
							
								return dom;
							},
							//指定是第三列
							"targets" : 12
						} ]
			});
     });
    
    /*管理员-教师管理-添加*/
    function teacher_add() {
    	var url = '/teacher/toEdit.do' + '?exType=Add';
    	layer_show('添加教师', url, null, null, function() {
    		myDataTable.fnDraw(true);
    	},true);
    }
    /*管理员-教师管理-编辑*/
    function teacher_edit(empId) {
    	var url = '/teacher/toEdit.do' + '?empId='+ empId +'&exType=UPDATE';
    	layer_show('修改教师', url, null, null, function() {
    		myDataTable.fnDraw(false);
    	},true);
    }
    /*搜素*/
    function searchTeacher(){
    	myDataTable.fnDraw(true);
    }
    
	/*用户-导入*/
	function excel_import() {
		var url = '/teacher/excelImport.do';
		layer_show('导入教师信息', url, null, 510, function() {
			myDataTable.fnDraw(true);
		});
	}
	
	function excel_export(){
		$("#export-form").submit();
	}
	
 /*教师推荐表导出*/
 function recommend_export() {
     var url = '/teacher/toRecommendExport.do';
     layer_show('教师推荐表导出', url, null, 410, function() {
     });
 }
 
 function teacher_delete() {
     var chk_value = [];
     $("input[name=empIds]:checked").each(function() {
         chk_value.push($(this).val());
     });
     if (chk_value == null || chk_value.length <= 0) {
			layer.msg('请选择要删除的数据！', {
				icon : 2,
				time : 2000
			});
			return;
	 }
     layer.confirm('确认要删除吗？', function(index) {
         $.ajax({
             type : 'POST',
             url : '/teacher/deleteTeacher.do',
             data : {
             	
                 idArray : chk_value
             },
             dataType : 'json',
             success : function(data) {
                 if (data.code == _GLOBAL_SUCCESS) {
                     layer.msg('已删除!', {
                         icon : 1,
                         time : 1000
                     });
                     myDataTable.fnDraw(true);
                     $("input[name=all]").attr("checked", false);
                 }
             }
         });
     });
 }