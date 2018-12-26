var myDataTable;
var bool = false;  //加个锁
$(function() {
	
		$('.search select').select2({
	        placeholder: "--请选择--",
	        allowClear: true,
	        width: "59%"
	    });
	
	 	_init_select("isChecked",[
	                	  	 {
	                	  		"dictValue":"1","dictName":"是"
	                	  	 		}
	                	  	 /* ,
	                	  	 {
	                	  		"dictValue":"0","dictName":"否"
	                	  	 		} */
	                	  	 ]);
		//初始化院校名称下拉框
		_simple_ajax_select({
			selectId : "unvsId",
			searchUrl : '/baseinfo/sUnvs.do',
			sData : {
				
			},
			showText : function(item) {
				return item.unvsName;
			},					
			showId : function(item) {
				return item.unvsId;
			},
			placeholder : '--请选择院校--'
		});
		
		_init_campus_select("recruitCampus", "recruitDepartment", null, '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
		
		_simple_ajax_select({
			selectId : "pfsnId",
			searchUrl : '/baseinfo/sPfsn.do',
			sData : {
				sId : function(){
					return $("#unvsId").val();
				},
                ext1 : function() {
                    return $("#pfsnLevel").val();
                },
                ext2 : function() {
                    return $("#grade").val();
                }
			},
			showText : function(item) {
				 var text = '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']' + '-['
                 + _findDict('year', item.year) + ']';
		         text += item.pfsnName + '(' + item.pfsnCode + ')';
		         return text;
			},					
			showId : function(item) {
				return item.pfsnId;
			},
			placeholder : '--请选择专业--'
		});
		
		_simple_ajax_select({
            selectId : "taId",
            searchUrl : '/baseinfo/sTa.do',
            sData : {
                sId : function() {
                    return $("#pfsnId").val();
                }
            },
            showText : function(item) {
                var text = '[' + item.taCode + ']' + item.taName;
                return text;
            },
            showId : function(item) {
                return item.taId;
            },
            placeholder : '--请选择考区--'
        });
		
		
		$("#unvsId").append(new Option("", "", false, true));
	 	
		//初始化年级下拉框
		 _init_select("grade",dictJson.grade);
		
		//初始化院校类型下拉框
		_init_select("recruitType",dictJson.recruitType);
		
		_init_select("pfsnLevel",dictJson.pfsnLevel);
		
		_init_select("scholarship",dictJson.scholarship);
		
		_init_select("stdType",dictJson.stdType);
		
		_init_checkbox("stdStage","stdStages",dictJson.stdStage,null);


    myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/msgPub/stuList.do",
					data: function (data) {
                          return searchData(data);
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
					"mData" : "stdName"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : null
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.learnId+'" name="learnIds"/>';
							   },
							"targets" : 0
							},
							{"render" : function(data, type, row, meta) {
								  if(row.isChecked == "1"){
									  return '<i class="Hui-iconfont">&#xe6a7;</i>';
								  }
								  return '<i class="Hui-iconfont">&#xe6a6;</i>';
							   },
							   "targets" : 1
							},
							{"render" : function(data, type, row, meta) {
								return row.grade+'级';
								},
								"targets" : 3
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
								"targets" : 4
							},
							{"render" : function(data, type, row, meta) {
								return _findDict(
										"stdStage",
										row.stdStage);
								},
								"targets" : 5
							}
				  ]
			});
	});

	function searchDep(){
		myDataTable.fnDraw(true);
	}
	
	function delAllStu(){
		
		layer.confirm('确认要按条件删除学员吗？', function(index) {
			if(!bool){
	        	bool = true;//锁住
				var d = searchD();
				$.ajax({
					type : 'POST',
					url : '/msgPub/delAllMtpStu.do',
					data : d,
					asyn : false,
					dataType : 'json',
					success : function(data) {
						layer.msg('删除成功!', {
							icon : 1,
							time : 1000
						});
						bool = false;
						myDataTable.fnDraw(false);
					}
				});
			}
		});
	}
	
	function addAllStu(){
		
		layer.confirm('确认要按条件添加学员吗？', function(index) {
			if(!bool){
	        	bool = true;//锁住
				var d = searchD();
				$.ajax({
					type : 'POST',
					url : '/msgPub/addAllMtpStu.do',
					data : d,
					asyn : false,
					dataType : 'json',
					success : function(data) {
						layer.msg('添加成功!', {
							icon : 1,
							time : 1000
						});
						bool = false;
						myDataTable.fnDraw(false);
					}
				});
			}
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
			if(!bool){
	        	bool = true;//锁住
				$.ajax({
					type : 'POST',
					url : '/msgPub/addMtpStu.do',
					data : {
						learnIds : chk_value,
						mtpId : $("#mtpId").val()
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('添加成功!', {
							icon : 1,
							time : 1000
						});
						bool = false;
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					}
				});
			}
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
			if(!bool){
	        	bool = true;//锁住
				$.ajax({
					type : 'POST',
					url : '/msgPub/delStu.do',
					data : {
						learnIds : chk_value,
						mtpId : $("#mtpId").val()
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('清除成功!', {
							icon : 1,
							time : 1000
						});
						bool = false;
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					}
				});
			}
		});
	}
    function toImport(){
     	var mtpId = $("#mtpId").val();
     	var url = '/msgPub/toImport.do' + '?mtpId='
        		 + mtpId;
	        layer_show('配置学员', url, null, 510, function () {
	            myDataTable.fnDraw(false);
	        });
     }
    
    function searchD() {
        return {
			unvsId: $("#unvsId").val() ? $("#unvsId").val() : '',
			recruitType: $("#recruitType").val() ? $("#recruitType").val() : '',
			pfsnLevel: $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
			grade: $("#grade").val() ? $("#grade").val() : '',
			pfsnId: $("#pfsnId").val() ? $("#pfsnId").val() : '',
			stdName: $("#stdName").val() ? $("#stdName").val() : '',
			idCard: $("#idCard").val() ? $("#idCard").val() : '',
			mobile: $("#mobile").val() ? $("#mobile").val() : '',
			isChecked: $("#isChecked").val() ? $("#isChecked").val() : '',
			mtpId: $("#mtpId").val() ? $("#mtpId").val() : '',
			taId: $("#taId").val() ? $("#taId").val() : '',
			tutorName: $("#tutorName").val() ? $("#tutorName").val() : '',
			scholarship: $("#scholarship").val() ? $("#scholarship").val() : '',
			recruitCampus: $("#recruitCampus").val() ? $("#recruitCampus").val() : '',
			recruitDepartment: $("#recruitDepartment").val() ? $("#recruitDepartment").val() : '',
			recruitName: $("#recruitName").val() ? $("#recruitName").val() : '',
			stdType: $("#stdType").val() ? $("#stdType").val() : '',
			isArrearage: $("#isArrearage").val() ? $("#isArrearage").val() : '',
			hasScore: $("#hasScore").val() ? $("#hasScore").val() : '',
		 	mtpId: $("#mtpId").val() ? $("#mtpId").val() : '',
			stdStages: function(){
				var chk_value = [];
	            var $input = $("input[name=stdStages]:checked");
	            $input.each(function () {
	                chk_value.push($(this).val());
	            });
	            return chk_value;
			}
        };
    }
    
    
    function searchData(data) {
        return {
			unvsId: $("#unvsId").val() ? $("#unvsId").val() : '',
			recruitType: $("#recruitType").val() ? $("#recruitType").val() : '',
			pfsnLevel: $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
			grade: $("#grade").val() ? $("#grade").val() : '',
			pfsnId: $("#pfsnId").val() ? $("#pfsnId").val() : '',
			stdName: $("#stdName").val() ? $("#stdName").val() : '',
			idCard: $("#idCard").val() ? $("#idCard").val() : '',
			mobile: $("#mobile").val() ? $("#mobile").val() : '',
			isChecked: $("#isChecked").val() ? $("#isChecked").val() : '',
			mtpId: $("#mtpId").val() ? $("#mtpId").val() : '',
			taId: $("#taId").val() ? $("#taId").val() : '',
			tutorName: $("#tutorName").val() ? $("#tutorName").val() : '',
			scholarship: $("#scholarship").val() ? $("#scholarship").val() : '',
			recruitCampus: $("#recruitCampus").val() ? $("#recruitCampus").val() : '',
			recruitDepartment: $("#recruitDepartment").val() ? $("#recruitDepartment").val() : '',
			recruitName: $("#recruitName").val() ? $("#recruitName").val() : '',
			stdType: $("#stdType").val() ? $("#stdType").val() : '',
			isArrearage: $("#isArrearage").val() ? $("#isArrearage").val() : '',
			hasScore: $("#hasScore").val() ? $("#hasScore").val() : '',
		 	mtpId: $("#mtpId").val() ? $("#mtpId").val() : '',
			stdStages: function(){
				var chk_value = [];
	            var $input = $("input[name=stdStages]:checked");
	            $input.each(function () {
	                chk_value.push($(this).val());
	            });
	            return chk_value;
			},
									
            start: data.start,
            length: data.length
        };
    }
    
    function reset(){
    	_reset();
    	$("input[name=stdStages]").attr("checked", false);
    }