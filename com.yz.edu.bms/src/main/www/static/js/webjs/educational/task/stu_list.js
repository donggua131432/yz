var myDataTable;
$(function() {
	
	 _init_select("isChecked",[
	                	  	 {
	                	  		"dictValue":"1","dictName":"是"
	                	  	 		},
	                	  	 {
	                	  		"dictValue":"0","dictName":"否"
	                	  	 		}
	                	  	 ]);
	//初始化院校名称下拉框
	_simple_ajax_select({
				selectId : "unvsId",
				searchUrl : '/bdUniversity/findAllKeyValue.do',
				sData : {},
				showText : function(item) {
					return item.unvs_name;
				},					
				showId : function(item) {
					return item.unvs_id;
				},
				placeholder : '--请选择院校--'
			});
	$("#unvsId").append(new Option("", "", false, true));
	
	$("#unvsId").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
     });
    $("#grade").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
     });
	 $("#pfsnLevel").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
     });
	 $("#pfsnId").append(new Option("", "", false, true));
	 $("#pfsnId").select2({
            placeholder: "--请先选择院校--"
     });

	 _init_select("pfsnLevel",dictJson.pfsnLevel);
	 
	//初始化年级下拉框
	 _init_select("grade",dictJson.grade);
	
	//初始化院校类型下拉框
	_init_select("recruitType",dictJson.recruitType);
	 $("#recruitType").change(function() {
			_init_select({
				selectId : 'grade',
				ext1 : $(this).val()
			}, dictJson.grade);
	 });
	 _init_select("stdType", dictJson.stdType);
  	 _init_select('examDataCheck', [
 	  	   	                	  	 {
 	  	   	                	  		"dictValue":"2","dictName":"通过"
 	  	   	                	  	 		},
 	  	   	                	  	 {
 	  	    	   	                	 "dictValue":"3","dictName":"未通过"
 	  	    	   	                	   }
 	  	   	                	  	 ]);
  	 //1-待审核 2-未通过 3-通过
  	 _init_select('graduateDataCheck', [
  	     	                	  	 {
  	   	                	  		"dictValue":"1","dictName":"审核中"
  	   	                	  	 		},
  	   	                	  	 {
  	   	                	  		"dictValue":"2","dictName":"通过"
  	   	                	  	 		},
  	   	                	  	 {
  	    	   	                	 "dictValue":"3","dictName":"未通过"
  	    	   	                	   }
  	   	                	  	 ]);
  	 _init_select('graduateFinanceCheck', [
  	  	     	                	  	 {
  	  	  	   	                	  		"dictValue":"1","dictName":"审核中"
  	  	  	   	                	  	 		},
  	  	  	   	                	  	 {
  	  	  	   	                	  		"dictValue":"2","dictName":"通过"
  	  	  	   	                	  	 		},
  	  	  	   	                	  	 {
  	  	  	    	   	                	 "dictValue":"3","dictName":"未通过"
  	  	  	    	   	                	   }
  	  	  	   	                	  	 ]);
  	 
  	 _init_select('sg', dictJson.sg); //优惠分组
  	 _init_select('scholarship', dictJson.scholarship); //优惠类型
  	 _init_select('inclusionStatus',dictJson.inclusionStatus); //入围状态
  	 
  	 $("#sg").change(function() { //联动
		_init_select({
			selectId : 'scholarship',
			ext1 : $(this).val()
		}, dictJson.scholarship);
	 });
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/task/stuList.do",
					data: function (pageData) {
                        return searchData(pageData);
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
					"mData" : null
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
								return '<input type="checkbox" value="'+ row.learnId+';'+row.tutor + '" name="learnIds"/>';
							   },
							"targets" : 0
							},
							{"render" : function(data, type, row, meta) {
								  if(row.taskId == $("#taskId").val()){
									  return "是";
								  }
								  return "不是";
							   },
							   "targets" : 1
							},
							{"render" : function(data, type, row, meta) {
								   var dom ='';
		                        	dom = row.stdName;
		                        	if(row.stdType ==='2'){
		                        		dom += ' <sup style="color:#f00">外</sup>';
		                        	}
		                        	return dom;
								},
								"targets" : 2
							},
							{"render" : function(data, type, row, meta) {
								   var dom ='';
								   if(row.tutorName){
									   dom = row.tutorName;
			                        	if(row.tutorStatus ==='2'){
			                        		dom += ' <sup style="color:#f00">离</sup>';
			                        	} 
								    }
		                        	return dom;
								},
								"targets" : 3
							},
							{"render" : function(data, type, row, meta) {
								return row.grade+'级';
								},
								"targets" : 4
							},
							{
								"render" : function(data, type,
										row, meta) {

									var dom = '';

									dom += '['
											+ _findDict(
													"recruitType",
													row.recruitType)
											+ ']';
									dom += row.unvsName;
									dom += ':(' + row.pfsnCode
											+ ')';
									dom += row.pfsnName;
									dom += '['
											+ _findDict(
													"pfsnLevel",
													row.pfsnLevel)
											+ ']';
									dom +='('+row.grade+'级)';

									return dom;
								},
								"targets" : 5
							},
							{"render" : function(data, type, row, meta) {
								return _findDict(
										"stdStage",
										row.stdStage);
								},
								"targets" : 6
							}
				  ]
			});

	});

	function searchStudent(){
		var stdStageArray = new Array();

		$.each($("input[name='stdStage']:checked"), function() {
			stdStageArray.push($(this).val());
		});
		if (stdStageArray == '' || stdStageArray == null) {
			layer.msg('请选择学员状态！', {
				icon : 2,
				time : 2000
			});
			return false;
		}
		
		myDataTable.fnDraw(true);
	}
	
	function searchData(pageData) {
	    var stdStageArray = new Array();
	
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
	    if(stdStageArray == '' || stdStageArray == null){
            layer.msg('请选择学员状态！', {
                icon : 1,
                time : 3000
            });
            return false;
        }
	    return {
	    	unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
	    	recruitType : $("#recruitType").val() ? $("#recruitType").val() : '',
	    	grade : $("#grade").val() ? $("#grade").val() : '',
	    	pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
			stdName : $("#stdName").val() ? $("#stdName").val() : '',
			idCard : $("#idCard").val() ? $("#idCard").val() : '',
			mobile : $("#mobile").val() ? $("#mobile").val() : '',
			isChecked : $("#isChecked").val() ? $("#isChecked").val() : '',
			taskId : $("#taskId").val() ? $("#taskId").val() : '',
			examDataCheck : $("#examDataCheck").val() ? $("#examDataCheck").val() : '',
			graduateDataCheck : $("#graduateDataCheck").val() ? $("#graduateDataCheck").val() : '',
			graduateFinanceCheck : $("#graduateFinanceCheck").val() ? $("#graduateFinanceCheck").val() : '',
			tutorName : $("#tutorName").val() ? $("#tutorName").val() : '',
			taName : $("#taName").val() ? $("#taName").val() : '',
			address : $("#address").val() ? $("#address").val() : '',
			scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
			sg : $("#sg").val() ? $("#sg").val() : '',
			inclusionStatus : $("#inclusionStatus").val() ? $("#inclusionStatus").val() : '',
			stdType : $("#stdType").val() ? $("#stdType").val() : '',
			pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
			stdStage : stdStageArray ? stdStageArray.join(',') : '',
			start : pageData.start,
			length : pageData.length
		};
	}
	function addAllStu() {
	    var stdStageArray = new Array();
		
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
	    
        var url = '/studyActivity/addAllStu.do';
        var grade = $("#grade").val();
        if(grade ==''){
        	 layer.msg('至少要选择一个年级!!!', {
                 icon : 5,
                 time : 3000
             });
             return;
        }
        if(stdStageArray == '' || stdStageArray == null){
            layer.msg('请选择学员状态！', {
                icon : 1,
                time : 2000
            });
            return false;
        }
        layer.confirm('确认根据筛选条件全部添加吗？', function(index) {
            $.ajax({
                type : 'POST',
                url : url,
                data:{
					"unvsId" : function() {
						return $("#unvsId").val();
					},"recruitType" : function() {
						return $("#recruitType").val();
					},"grade" : function() {
						return $("#grade").val();
					},"pfsnId" : function() {
						return $("#pfsnId").val();
					},"stdName" : function() {
						return $("#stdName").val();
					},"idCard" : function() {
						return $("#idCard").val();
					},"mobile" : function() {
						return $("#mobile").val();
					},"isChecked" : function(){
						return $("#isChecked").val();
					},"taskId" : function(){
						return $("#taskId").val();
					},"examDataCheck" : function(){
						return $("#examDataCheck").val();
					},"graduateDataCheck" : function(){
						return $("#graduateDataCheck").val();
					},"graduateFinanceCheck" : function(){
						return $("#graduateFinanceCheck").val();
					},"tutorName" : function(){
						return $("#tutorName").val();
					},"taName" : function(){
						return $("#taName").val();
					},"address" : function(){
						return $("#address").val();
					},"scholarship" : function(){
						return $("#scholarship").val();
					},"sg" : function(){
						return $("#sg").val();
					},"inclusionStatus" : function(){
						return $("#inclusionStatus").val();
					},"stdType" : function(){
						return $("#stdType").val();
					},"pfsnLevel" : function (){
						return $("#pfsnLevel").val();
					},"stdStage" : function (){
						return stdStageArray
					}
				},
                dataType : 'json',
                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('添加成功!', {
                            icon : 1,
                            time : 2000
                        });
                    	myDataTable.fnDraw(true);
                    }
                }
            });
        });
    }
	
	function delAllStu() {
	    var stdStageArray = new Array();
		
	    $.each($("input[name='stdStage']:checked"),function(){
	        stdStageArray.push($(this).val());
	    });
		    
        var url = '/studyActivity/delAllStu.do';
        var grade = $("#grade").val();
        if(grade ==''){
        	 layer.msg('至少要选择一个年级!!!', {
                 icon : 5,
                 time : 3000
             });
             return;
        }
        if(stdStageArray == '' || stdStageArray == null){
            layer.msg('请选择学员状态！', {
                icon : 2,
                time : 2000
            });
            return false;
        }
        layer.confirm('确认根据筛选条件清除全部吗？', function(index) {
            $.ajax({
                type : 'POST',
                url : url,
                data:{
					"unvsId" : function() {
						return $("#unvsId").val();
					},"recruitType" : function() {
						return $("#recruitType").val();
					},"grade" : function() {
						return $("#grade").val();
					},"pfsnId" : function() {
						return $("#pfsnId").val();
					},"stdName" : function() {
						return $("#stdName").val();
					},"idCard" : function() {
						return $("#idCard").val();
					},"mobile" : function() {
						return $("#mobile").val();
					},"isChecked" : function(){
						return $("#isChecked").val();
					},"taskId" : function(){
						return $("#taskId").val();
					},"examDataCheck" : function(){
						return $("#examDataCheck").val();
					},"graduateDataCheck" : function(){
						return $("#graduateDataCheck").val();
					},"graduateFinanceCheck" : function(){
						return $("#graduateFinanceCheck").val();
					},"tutorName" : function(){
						return $("#tutorName").val();
					},"taName" : function(){
						return $("#taName").val();
					},"address" : function(){
						return $("#address").val();
					},"scholarship" : function(){
						return $("#scholarship").val();
					},"sg" : function(){
						return $("#sg").val();
					},"inclusionStatus" : function(){
						return $("#inclusionStatus").val();
					},"stdType" : function(){
						return $("#stdType").val();
					},"pfsnLevel" : function (){
						return $("#pfsnLevel").val();
					},"stdStage" : function (){
						return stdStageArray
					}
				},
                dataType : 'json',
                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('清除成功!', {
                            icon : 1,
                            time : 3000
                        });
                    	myDataTable.fnDraw(true);
                    }
                }
            });
        });
    }
	
	/*管理员-目标学员-添加*/
	function addStu() {
		var chk_value = [];
		$("input[name=learnIds]:checked").each(function() {
			chk_value.push($(this).val());
		});
		if(chk_value.length ==0){
			layer.msg('请选择要添加的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要添加目标学员吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/task/addStu.do',
				data : {
					idArray : chk_value,
					taskId : $("#taskId").val(),
					operType:"2"
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('添加成功!', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('添加成功！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}
	
    function delStu() {
		var chk_value = [];
		$("input[name=learnIds]:checked").each(function() {
			chk_value.push($(this).val().split(";")[0]);
		});
		if(chk_value.length ==0){
			layer.msg('请选择要清除的数据！', {
				icon : 2,
				time : 2000
			});
			return;
		}
		layer.confirm('确认要清除目标学员吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/task/delStu.do',
				data : {
					idArray : chk_value,
					taskId : $("#taskId").val()
				},
				dataType : 'json',
				success : function(data) {
					layer.msg('清除成功!', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
				error : function(data) {
					layer.msg('清除失败！', {
						icon : 1,
						time : 1000
					});
					myDataTable.fnDraw(true);
					$("input[name=all]").attr("checked", false);
				},
			});
		});
	}
    function stuImport(){
    	var url = '/task/stuImport.do'+ '?taskId='+$("#taskId").val();
    	layer_show('导入目标学员信息', url, null, null, function() {
    		myDataTable.fnDraw(true);
    	});
    }
    
	 function init_pfsn_select() {
	    	_simple_ajax_select({
				selectId : "pfsnId",
				searchUrl : '/baseinfo/sPfsn.do',
				sData : {
					sId :  function(){
						return $("#unvsId").val() ? $("#unvsId").val() : '';	
					},
					ext1 : function(){
						return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
					},
					ext2 : function(){
						return $("#grade").val() ? $("#grade").val() : '';
					}
				},
				showText : function(item) {
					var text = '(' + item.pfsnCode + ')' + item.pfsnName;
					text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
					return text;
				},
				showId : function(item) {
					return item.pfsnId;
				},
				placeholder : '--请选择专业--'
			});
			$("#pfsnId").append(new Option("", "", false, true));
	    }