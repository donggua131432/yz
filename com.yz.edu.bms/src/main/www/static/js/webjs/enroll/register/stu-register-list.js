 //----------------------------------------------First-------------------------------------------------------
        //报考院校
        _simple_ajax_select({
            selectId: "unvsIdFirst",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        //录取院校
        _simple_ajax_select({
            selectId: "unvsIdAdmitFirst",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        
        _simple_ajax_select({
            selectId: "pfsnIdAdmitFirst",
            searchUrl: '/unvsProfession/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return "("+item.pfsn_code+")"+item.pfsn_name;
            },
            showId: function (item) {
                return item.pfsn_id;
            },
            placeholder: '--请选择专业--'
        });
        
        
        $("#unvsIdFirst").change(function () {
            $("#pfsnIdFirst").removeAttr("disabled");
            init_pfsn_select_first();
	     });
        $("#gradeFirst").change(function () {
            $("#pfsnIdFirst").removeAttr("disabled");
            init_pfsn_select_first();
	     });
		 $("#pfsnLevelFirst").change(function () {
            $("#pfsnIdFirst").removeAttr("disabled");
            init_pfsn_select_first();
	     });
		 $("#pfsnIdFirst").append(new Option("", "", false, true));
		 $("#pfsnIdFirst").select2({
	            placeholder: "--请先选择院校--"
	     });
        //招生部门
        _init_campus_select("campusIdFirst", "dpIdFirst", "groupIdFirst", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
        //根据数据字典初始化下拉框
        _init_select('recruitTypeFirst', dictJson.recruitType);
        _init_select('stdStageFirst', dictJson.stdStage);
        _init_select('recruitStatusFirst', dictJson.empStatus);
        _init_select('gradeFirst', dictJson.grade);
        _init_select('scholarshipFirst', dictJson.scholarship);
        _init_select('pfsnLevelFirst', dictJson.pfsnLevel);
        _init_select('stdTypeFirst', dictJson.stdType);
        //是否已报名确认
        _init_select("isOkFirst", [
            {"dictValue" : "0","dictName" : "未确认"},
            {"dictValue" : "1","dictName" : "已确认"}
        ]);
        //是否有考生号
        _init_select("isExamNoFirst", [
            {"dictValue" : "1","dictName" : "有"},
            {"dictValue" : "0","dictName" : "无"}
        ]);
        //是否已参加考试签到
        _init_select("isExamSignFirst", [
            {"dictValue" : "0","dictName" : "未签到"},
            {"dictValue" : "1","dictName" : "已签到"}
        ]);
        //录取状态
        _init_select("isAdmitFirst", [
            {"dictValue" : "1","dictName" : "已录取"},
            {"dictValue" : "2","dictName" : "未录取"}
        ]);
        //有无成绩
        _init_select("isScoreFirst", [
            {"dictValue" : "0","dictName" : "无"},
            {"dictValue" : "1","dictName" : "有"}
        ]);
        //有无加分
        _init_select("isAddScoreFirst", [
            {"dictValue" : "0","dictName" : "无"},
            {"dictValue" : "1","dictName" : "有"}
        ]);
        //录入与报读是否一致
        _init_select("isAdmitEnrollFirst", [
            {"dictValue" : "0","dictName" : "否"},
            {"dictValue" : "1","dictName" : "是"}
        ]);

        //----------------------------------------------Second------------------------------------------------------
        //报考院校
        _simple_ajax_select({
            selectId: "unvsIdSecond",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        //录取院校
        _simple_ajax_select({
            selectId: "unvsIdAdmitSecond",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        _simple_ajax_select({
            selectId: "pfsnIdAdmitSecond",
            searchUrl: '/unvsProfession/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return "("+item.pfsn_code+")"+item.pfsn_name;
            },
            showId: function (item) {
                return item.pfsn_id;
            },
            placeholder: '--请选择专业--'
        });
        

        $("#unvsIdSecond").change(function () {
            $("#pfsnIdSecond").removeAttr("disabled");
            init_pfsn_select_second();
	     });
        $("#gradeSecond").change(function () {
            $("#pfsnIdSecond").removeAttr("disabled");
            init_pfsn_select_second();
	     });
		 $("#pfsnLevelSecond").change(function () {
            $("#pfsnIdSecond").removeAttr("disabled");
            init_pfsn_select_second();
	     });
		 $("#pfsnIdSecond").append(new Option("", "", false, true));
		 $("#pfsnIdSecond").select2({
	            placeholder: "--请先选择院校--"
	     });
        //招生部门
        _init_campus_select("campusIdSecond", "dpIdSecond", "groupIdSecond", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
        //根据数据字典初始化下拉框
        _init_select('recruitTypeSecond', dictJson.recruitType);
        _init_select('stdStageSecond', dictJson.stdStage);
        _init_select('recruitStatusSecond', dictJson.empStatus);
        _init_select('gradeSecond', dictJson.grade);
        _init_select('scholarshipSecond', dictJson.scholarship);
        _init_select('pfsnLevelSecond', dictJson.pfsnLevel);
        _init_select('stdTypeSecond', dictJson.stdType);
        //是否已报名确认
        _init_select("isOkSecond", [
            {"dictValue" : "0","dictName" : "未确认"},
            {"dictValue" : "1","dictName" : "已确认"}
        ]);
        //是否有考生号
        _init_select("isExamNoSecond", [
            {"dictValue" : "1","dictName" : "有"},
            {"dictValue" : "0","dictName" : "无"}
        ]);
        //是否已参加考试签到
        _init_select("isExamSignSecond", [
            {"dictValue" : "0","dictName" : "未签到"},
            {"dictValue" : "1","dictName" : "已签到"}
        ]);
        //录取状态
        _init_select("isAdmitSecond", [
            {"dictValue" : "1","dictName" : "已录取"},
            {"dictValue" : "2","dictName" : "未录取"}
        ]);
        //有无成绩
        _init_select("isScoreSecond", [
            {"dictValue" : "0","dictName" : "无"},
            {"dictValue" : "1","dictName" : "有"}
        ]);
        //有无加分
        _init_select("isAddScoreSecond", [
            {"dictValue" : "0","dictName" : "无"},
            {"dictValue" : "1","dictName" : "有"}
        ]);
        //录入与报读是否一致
        _init_select("isAdmitEnrollSecond", [
            {"dictValue" : "0","dictName" : "否"},
            {"dictValue" : "1","dictName" : "是"}
        ]);

        //----------------------------------------------Third-------------------------------------------------------
        //报考院校
        _simple_ajax_select({
            selectId: "unvsIdThird",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        //录取院校
        _simple_ajax_select({
            selectId: "unvsIdAdmitThird",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        _simple_ajax_select({
            selectId: "pfsnIdAdmitThird",
            searchUrl: '/unvsProfession/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return "("+item.pfsn_code+")"+item.pfsn_name;
            },
            showId: function (item) {
                return item.pfsn_id;
            },
            placeholder: '--请选择专业--'
        });
        
        $("#unvsIdThird").change(function () {
            $("#pfsnIdThird").removeAttr("disabled");
            init_pfsn_select_third();
	     });
        $("#gradeThird").change(function () {
            $("#pfsnIdThird").removeAttr("disabled");
            init_pfsn_select_third();
	     });
		 $("#pfsnLevelThird").change(function () {
            $("#pfsnIdThird").removeAttr("disabled");
            init_pfsn_select_third();
	     });
		 $("#pfsnIdThird").append(new Option("", "", false, true));
		 $("#pfsnIdThird").select2({
	            placeholder: "--请先选择院校--"
	     });
        //招生部门
        _init_campus_select("campusIdThird", "dpIdThird", "groupIdThird", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
        //根据数据字典初始化下拉框
        _init_select('recruitTypeThird', dictJson.recruitType);
        _init_select('stdStageThird', dictJson.stdStage);
        _init_select('recruitStatusThird', dictJson.empStatus);
        _init_select('gradeThird', dictJson.grade);
        _init_select('scholarshipThird', dictJson.scholarship);
        _init_select('pfsnLevelThird', dictJson.pfsnLevel);
        _init_select('stdTypeThird', dictJson.stdType);
        //是否已报名确认
        _init_select("isOkThird", [
            {"dictValue" : "0","dictName" : "未确认"},
            {"dictValue" : "1","dictName" : "已确认"}
        ]);
        //是否有考生号
        _init_select("isExamNoThird", [
            {"dictValue" : "1","dictName" : "有"},
            {"dictValue" : "0","dictName" : "无"}
        ]);
        //是否已参加考试签到
        _init_select("isExamSignThird", [
            {"dictValue" : "0","dictName" : "未签到"},
            {"dictValue" : "1","dictName" : "已签到"}
        ]);
        //录取状态
        _init_select("isAdmitThird", [
            {"dictValue" : "1","dictName" : "已录取"},
            {"dictValue" : "2","dictName" : "未录取"}
        ]);
        //有无成绩
        _init_select("isScoreThird", [
            {"dictValue" : "0","dictName" : "无"},
            {"dictValue" : "1","dictName" : "有"}
        ]);
        //有无加分
        _init_select("isAddScoreThird", [
            {"dictValue" : "0","dictName" : "无"},
            {"dictValue" : "1","dictName" : "有"}
        ]);
        //录入与报读是否一致
        _init_select("isAdmitEnrollThird", [
            {"dictValue" : "0","dictName" : "否"},
            {"dictValue" : "1","dictName" : "是"}
        ]);


		var firstGradeDataTable;
		var secondGradeDataTable;
		var thirdGradeDataTable;
		//标签块
		$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
		$(function() {
			//初始化数据表格
			firstGradeDataTable = $('#firstGrade').dataTable({
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/stuRegister/list.do",
					type : "post",
					data : function(data){
                        data = $.extend({},{registerTimer:'1', start:data.start, length:data.length},$("#firstForm").serializeObject());
						return data;
					}
				},
				"pageLength" : 10,
				"pagingType" : "full_numbers",
				"ordering" : false,
				"searching" : false,
				"createdRow" : function(row, data, dataIndex){
					$(row).addClass('text-c');
				},
				"language" : _my_datatables_language,
				columns : [
				    {"mData" : null},
					{"mData" : "std_no"},
					{"mData" : "std_name"},
					{"mData" : null},
                    {"mData" : null},
					{"mData" : "school_roll"},
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : null}
				],
				"columnDefs" : [
					{"targets" : 0,"render" : function(data, type, row, meta) {
						return '<input type="checkbox" value="' + row.learn_id + '" name="firstGradeId"/>';
					}},
                    {"targets" : 1,"render" : function(data, type, row, meta) {
                        var dom = '';
                        if(null == row.std_no || '' == row.std_no){
                            dom = '无';
                        }else{
                            dom = row.std_no;
                        }
                        return dom;
                    }},
                    {"targets" : 3,"class":"text-l","render" : function(data, type, row, meta) {
                        var dom = '';
                        if (row.recruit_type != null) {
                            if(_findDict("recruitType", row.recruit_type).indexOf("成人")!=-1){
                                dom += "[成教]";
                            }else {
                                dom += "[国开]";
                            }
						}
                        if (row.unvs_name != null) dom += row.unvs_name+"</br>";
                        if (row.pfsn_level != null){
                            if(_findDict("pfsnLevel", row.pfsn_level).indexOf("高中")!=-1){
                                dom += "[专科]";
                            }else {
                                dom += "[本科]";
                            }
						}
                        if (row.pfsn_name != null) dom += row.pfsn_name;
                        if (row.pfsn_code != null) dom += '(' + row.pfsn_code+ ')';
                        return dom;
                    }},
                    {"targets" : 4,"class":"text-l","render" : function(data, type, row, meta) {
                        var dom = '';
                        //if (row.recruit_type != null) dom += '['+ _findDict("recruitType",row.recruit_type)+ ']';
                        if (row.unvs_name_admit != null) dom += row.unvs_name_admit+"</br>";
                        if (row.pfsn_level_admit != null){
                            if(_findDict("pfsnLevel", row.pfsn_level_admit).indexOf("高中")!=-1){
                                dom += "[专科]";
                            }else {
                                dom += "[本科]";
                            }
						};
                        if (row.pfsn_name_admit != null) dom += row.pfsn_name_admit;
                        if (row.pfsn_code_admit != null) dom += '(' + row.pfsn_code_admit+ ')';
                        return dom;
                    }},
                    { "targets" : 5,"render" : function(data, type, row, meta) {
                        var dom = '';
                        if(null == row.school_roll || '' == row.school_roll){
                            dom = '无';
                        }else{
                            dom = row.school_roll;
                        }
                        return dom;
                    }},
					{"targets" : 7,"render" : function(data, type, row, meta) {
						return _findDict("stdStage", row.std_stage);
					}},
					{"targets" : 8,"render" : function(data, type, row, meta) {
						var dom = '<a class="tableBtn normal" onclick="regist(\'' + row.learn_id + '\',\'1\')">注册</a>';
						return dom;
					}}
				]
			});

			//初始化数据表格
			secondGradeDataTable = $('#secondGrade').dataTable({
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/stuRegister/list.do",
					type : "post",
					data : function(data){
                        data = $.extend({},{registerTimer:'2',start:data.start, length:data.length},$("#secondForm").serializeObject());
						return data;
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
				columns : [
					{"mData" : null},
					{"mData" : "std_no"},
					{"mData" : "std_name"},
					{"mData" : null},
                    {"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null}
				],
				"columnDefs" : [
					{"targets" : 0,"render" : function(data, type, row, meta) {
						return '<input type="checkbox" value="' + row.learn_id + '" name="secondGradeId"/>';
					}},
                    {"targets" : 1,"render" : function(data, type, row, meta) {
                        var dom = '';
                        if(null == row.std_no || '' == row.std_no){
                            dom = '无';
                        }else{
                            dom = row.std_no;
                        }
                        return dom;
                    }},
                    {"targets" : 3,"class":"text-l","render" : function(data, type, row, meta) {
                        var dom = '';
                        if (row.recruit_type != null){
                            if(_findDict("recruitType", row.recruit_type).indexOf("成人")!=-1){
                                dom += "[成教]";
                            }else {
                                dom += "[国开]";
                            }
						};
                        if (row.unvs_name != null) dom += row.unvs_name+"</br>";
                        if (row.pfsn_level != null){
                            if(_findDict("pfsnLevel", row.pfsn_level).indexOf("高中")!=-1){
                                dom += "[专科]";
                            }else {
                                dom += "[本科]";
                            }
						}
                        if (row.pfsn_name != null) dom += row.pfsn_name;
                        if (row.pfsn_code != null) dom += ':(' + row.pfsn_code+ ')';

                        return dom;
                    }},
                    {"targets" : 4,"class":"text-l","render" : function(data, type, row, meta) {
                        var dom = '';
                        //if (row.recruit_type != null) dom += '['+ _findDict("recruitType",row.recruit_type)+ ']';
                        if (row.unvs_name_admit != null) dom += row.unvs_name_admit+'</br>';
                        if (row.pfsn_level_admit != null){
                            if(_findDict("pfsnLevel", row.pfsn_level_admit).indexOf("高中")!=-1){
                                dom += "[专科]";
                            }else {
                                dom += "[本科]";
                            }
						} ;
                        if (row.pfsn_name_admit != null) dom += row.pfsn_name_admit;
                        if (row.pfsn_code_admit != null) dom += '(' + row.pfsn_code_admit+ ')';

                        return dom;
                    }},
                    {"targets" : 5,"render" : function(data, type, row, meta) {
                        var dom = '';
                        if(null == row.school_roll || '' == row.school_roll){
                            dom = '无';
                        }else{
                            dom = row.school_roll;
                        }
                        return dom;
                    }},
                    {"targets" : 6,"render" : function(data, type, row, meta) {
                        var dom = row.grade + '级';
                        return dom;
                    }},
					{"targets" : 7,"render" : function(data, type, row, meta) {
						return _findDict("stdStage",row.std_stage);
					}},
					{"targets" : 8,"render" : function(data, type, row, meta) {
						var dom = '<a class="tableBtn normal" onclick="regist(\''+row.learn_id+ '\',\'2\')">注册</a>';
						return dom;
					}}
				]
			});

			//初始化数据表格
			thirdGradeDataTable = $('#thirdGrade').dataTable({
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/stuRegister/list.do",
					type : "post",
					data : function(data){
                        data = $.extend({},{registerTimer:'3',start:data.start, length:data.length},$("#thirdForm").serializeObject());
						return data;
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
				columns : [
					{"mData" : null},
					{"mData" : null},
					{"mData" : "std_name"},
					{"mData" : null},
                    {"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null}
				],
				"columnDefs" : [
					{"targets" : 0,"render" : function(data, type, row, meta) {
						return '<input type="checkbox" value="' + row.learn_id + '" name="thirdGradeId"/>';
					}},
                    {"targets" : 1,"render" : function(data, type, row, meta) {
                        var dom = '';
                        if(null == row.std_no || '' == row.std_no){
                            dom = '无';
                        }else{
                            dom = row.std_no;
                        }
                        return dom;
                    }},
                    {"targets" : 3,"class":"text-l","render" : function(data, type, row, meta) {
                        var dom = '';
                        if (row.recruit_type != null){
                            if(_findDict("recruitType", row.recruit_type).indexOf("成人")!=-1){
                                dom += "[成教]";
                            }else {
                                dom += "[国开]";
                            }
						}
                        if (row.unvs_name != null) dom += row.unvs_name+'</br>';
                        if (row.pfsn_level != null){
                            if(_findDict("pfsnLevel", row.pfsn_level).indexOf("高中")!=-1){
                                dom += "[专科]";
                            }else {
                                dom += "[本科]";
                            }
						};
                        if (row.pfsn_name != null) dom += row.pfsn_name;
                        if (row.pfsn_code != null) dom += '(' + row.pfsn_code+ ')';

                        return dom;
                    }},
                    {"targets" : 4,"class":"text-l","render" : function(data, type, row, meta) {
                        var dom = '';
                        //if (row.recruit_type != null) dom += '['+ _findDict("recruitType",row.recruit_type)+ ']';
                        if (row.unvs_name_admit != null) dom += row.unvs_name_admit+'</br>';
                        if (row.pfsn_level_admit != null){
                            if(_findDict("pfsnLevel", row.pfsn_level_admit).indexOf("高中")!=-1){
                                dom += "[专科]";
                            }else {
                                dom += "[本科]";
                            }
						};
                        if (row.pfsn_name_admit != null) dom += row.pfsn_name_admit;
                        if (row.pfsn_code_admit != null) dom += '(' + row.pfsn_code_admit+ ')';

                        return dom;
                    }},
                    {"targets" : 5,"render" : function(data, type, row, meta) {
                        var dom = '';
                        if(null == row.school_roll || '' == row.school_roll){
                            dom = '无';
                        }else{
                            dom = row.school_roll;
                        }
                        return dom;
                    }},
                    {"targets" : 6,"render" : function(data, type, row, meta) {
                        var dom = row.grade + '级';
                        return dom;
                    }},
					{"targets" : 7,"render" : function(data, type, row, meta) {
						return _findDict("stdStage",row.std_stage);
					}},

					{"targets" : 8,"render" : function(data, type, row, meta) {
						var dom = '<a class="tableBtn normal" onclick="regist(\''+ row.learn_id+ '\',\'3\')">注册</a>';
						return dom;
					}}
				]
			});
		});

		function registerBatch(type) {
			var chk_value = [];
			var $input = null;
			var grade = type;

			if (type == 1) {
				$input = $("input[name=firstGradeId]:checked");
			} else if (type == 2) {
				$input = $("input[name=secondGradeId]:checked");
			} else if (type == 3) {
				$input = $("input[name=thirdGradeId]:checked");
			}

			$input.each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value == null || chk_value.length <= 0){
				layer.msg('未选择任何数据!', {
					icon : 5,
					time : 1000
				});
				return;
			}
			layer.confirm('确认要注册吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/stuRegister/registerBatch.do',
					data : {
						learnIds : chk_value,
						grade : type
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('注册成功!', {
							icon : 1,
							time : 1000
						});
						drawTable();
						$("input[name=all]").attr("checked", false);
					}
				});
			});
		}

		function regist(id,grade) {
			var url = '/stuRegister/toRegister.do' + '?learnId=' + id +'&grade=' + grade ;
			layer_show('注册学员', url, null, 600, function() {
				firstGradeDataTable.fnDraw(false);
				secondGradeDataTable.fnDraw(false);
				thirdGradeDataTable.fnDraw(false);
			}, true);
		}

		function drawTable(){
			firstGradeDataTable.fnDraw(true);
			secondGradeDataTable.fnDraw(true);
			thirdGradeDataTable.fnDraw(true);
		}

		function searchRegister(type) {
			if (type == 1) {
				firstGradeDataTable.fnDraw(true);
			} else if (type == 2) {
				secondGradeDataTable.fnDraw(true);
			} else {
				thirdGradeDataTable.fnDraw(true);
			}
		}

		function register_import() {
			var url = '/stuRegister/toExcelImport.do';
			layer_show('高校学号导入', url, null, 510, function() {
				firstGradeDataTable.fnDraw(true);
				secondGradeDataTable.fnDraw(true);
				thirdGradeDataTable.fnDraw(true);
			});
		}

         function register_stdno_import() {
             var url = '/stuRegister/toExcelStdNoImport.do';
             layer_show('学籍编号导入', url, null, 510, function() {
                 firstGradeDataTable.fnDraw(true);
                 secondGradeDataTable.fnDraw(true);
                 thirdGradeDataTable.fnDraw(true);
             });
         }

		//退学申请
		function stduent_out() {
			var url = '/studentOut/edit.do' + '?exType=ADD';
			layer_show('添加退学学员', url, null, 510, function() {
				myDataTable.fnDraw(true);
			});
		}
		
		//第一年
		function init_pfsn_select_first() {
			    	_simple_ajax_select({
						selectId : "pfsnIdFirst",
						searchUrl : '/baseinfo/sPfsn.do',
						sData : {
							sId :  function(){
								return $("#unvsIdFirst").val() ? $("#unvsIdFirst").val() : '';	
							},
							ext1 : function(){
								return $("#pfsnLevelFirst").val() ? $("#pfsnLevelFirst").val() : '';
							},
							ext2 : function(){
								return $("#gradeFirst").val() ? $("#gradeFirst").val() : '';
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
					$("#pfsnIdFirst").append(new Option("", "", false, true));
			    }
		
		//第二年
		function init_pfsn_select_second() {
			    	_simple_ajax_select({
						selectId : "pfsnIdSecond",
						searchUrl : '/baseinfo/sPfsn.do',
						sData : {
							sId :  function(){
								return $("#unvsIdSecond").val() ? $("#unvsIdSecond").val() : '';	
							},
							ext1 : function(){
								return $("#pfsnLevelSecond").val() ? $("#pfsnLevelSecond").val() : '';
							},
							ext2 : function(){
								return $("#gradeSecond").val() ? $("#gradeSecond").val() : '';
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
					$("#pfsnIdSecond").append(new Option("", "", false, true));
			    }
		
		//第三年
		function init_pfsn_select_third() {
			    	_simple_ajax_select({
						selectId : "pfsnIdThird",
						searchUrl : '/baseinfo/sPfsn.do',
						sData : {
							sId :  function(){
								return $("#unvsIdThird").val() ? $("#unvsIdThird").val() : '';	
							},
							ext1 : function(){
								return $("#pfsnLevelThird").val() ? $("#pfsnLevelThird").val() : '';
							},
							ext2 : function(){
								return $("#gradeThird").val() ? $("#gradeThird").val() : '';
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
					$("#pfsnIdThird").append(new Option("", "", false, true));
			    }