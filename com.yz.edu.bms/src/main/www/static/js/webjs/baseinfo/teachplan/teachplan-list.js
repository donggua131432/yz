var myDataTable;
    $(function () {

        //初始化专业层次下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel);

        //初始化年度下拉框
        _init_select("year", dictJson.year);
        _init_select("grade", dictJson.grade);
        //初始化学期下拉框
        _init_select("semester", dictJson.semester);
       //初始化类型下拉框
        _init_select("thpType", dictJson.thpType);
        
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
		
		
        myDataTable = $('.table-sort').dataTable(
            {
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/teachPlan/findAllBdTeachPlan.do",
                    type: "post",
                    data: {
                        "unvsId": function () {
                            return $("#unvsId").val();
                        }, "pfsnCode": function () {
                            return $("#pfsnCode").val();
                        }, "pfsnId": function () {
                            return $("#pfsnId").val();
                        }, "thpName": function () {
                            return $("#thpName").val();
                        }, "thpCode": function () {
                            return $("#thpCode").val();
                        }, "pfsnLevel": function () {
                            return $("#pfsnLevel").val();
                        }, "year": function () {
                            return $("#year").val();
                        }, "semester": function () {
                            return $("#semester").val();
                        }, "grade" :function (){
                        	return $("#grade").val();
                        }, "thpType" :function (){
                        	return $("#thpType").val();
                        }
                    }
                },
                "pageLength": 10,
                "pagingType": "full_numbers",
                "ordering": false,
                "searching": false,
                "createdRow": function (row, data, dataIndex) {
                    $(row).addClass('text-c');
                },
                "language": _my_datatables_language,
                columns: [{
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": "thpName"
                }, {
                    "mData": "thpId"
                }, {
                    "mData": null
                }, {
                    "mData": "testSubject"
                }, {
                    "mData": null
                }, {
                    "mData": null
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            var recruitType;
                            var pfsnLevel;
                            for (var i = 0; i < dictJson.recruitType.length; i++) {
                                if (row.recruitType == dictJson.recruitType[i].dictValue)
                                    recruitType = dictJson.recruitType[i].dictName;
                            }
                            for (var i = 0; i < dictJson.pfsnLevel.length; i++) {
                                if (row.pfsnLevel == dictJson.pfsnLevel[i].dictValue)
                                    pfsnLevel = dictJson.pfsnLevel[i].dictName;
                            }

                            if(recruitType.indexOf("成人")!=-1){
                                recruitType = "[成教]";
                            }else {
                                recruitType = "[国开]";
                            }
                                if(pfsnLevel.indexOf("高中")!=-1){
                                    pfsnLevel = "[专科]";
                                }else {
                                    pfsnLevel = "[本科]";
                                }
                            return  recruitType  + row.unvsName + '</br>'  + pfsnLevel+'(' + row.pfsnName + ')'  + row.pfsnCode;
                        },
                        "targets": 1,
                        "class":"text-l"
                    },
                    {
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < dictJson.grade.length; i++) {
                                if (row.grade == dictJson.grade[i].dictValue)
                                    return dictJson.grade[i].dictName;
                            }
                            return '';
                        },
                        "targets": 2
                    },
                    {
                        "targets": 4,
                        "class":"text-l"
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var semester ='';
                            if(0 !=row.semester){
                            	 for (var i = 0; i < dictJson.semester.length; i++) {
                                     if (row.semester == dictJson.semester[i].dictValue)
                                         semester = dictJson.semester[i].dictName;
                                 }
                            }
                            return semester;
                        },
                        "targets": 3
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return _findDict("thpType", row.thpType);
                        },
                        "targets": 6
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            row = row.textbook;
                            for (var i = 0; i < row.length; i++) {
                                dom += row[i].textbookId + ": " + row[i].textbookName + "</br>";
                            }
                            return dom;
                        },
                        "targets": 8,
                        "class":"text-l"
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.thpId + '" name="thpIds"/>';
                        },
                        "targets": 0
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';

                            dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.thpId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="member_del(this,\'' + row.thpId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>';

                            return dom;
                        },
                        //指定是第三列
                        "targets": 9
                    }]
            });

    });
    
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
   
    /*用户-添加*/
    function member_add() {
        var url = '/teachPlan/edit.do' + '?exType=ADD';
        layer_show('添加教学计划', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /* 教学计划导入*/
    function profession_excel_import() {
        var url = '/teachPlan/teachPlanImport.do';
        layer_show('教学计划导入', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /* 教材导入*/
    function textbook_excel_import() {
        var url = '/teachPlan/textBookImport.do';
        layer_show('教材导入', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }
    /* 考试科目导入*/
    function testsubject_excel_import(){
    	var url = '/teachPlan/testSubjectImport.do';
        layer_show('考试科目导入', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /* “教学计划”导出*/
    function excel_export() {
        var tableSetings = myDataTable.fnSettings();
        var paging_length = tableSetings._iDisplayLength;//当前每页显示多少
        var page_start = tableSetings._iDisplayStart;//当前页开始
        $('<form method="post" action="' + '/teachPlan/export.do' + '">'
            + '<input type="text" name="length" value="' + paging_length + '"/>'
            + '<input type="text" name="start" value="' + page_start + '"/>'
            + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
            + '<input type="text" name="pfsnCode" value="' + $("#pfsnCode").val() + '"/>'
            + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
            + '<input type="text" name="thpName" value="' + $("#thpName").val() + '"/>'
            + '<input type="text" name="thpCode" value="' + $("#thpCode").val() + '"/>'
            + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
            + '<input type="text" name="year" value="' + $("#year").val() + '"/>'
            + '<input type="text" name="semester" value="' + $("#semester").val() + '"/>'
            + '<input type="text" name="thpType" value="' + $("#thpType").val() + '"/>'
            + '</form>').appendTo('body').submit().remove();
    }

    /*用户-编辑*/
    function member_edit(thpId) {
        var url = '/teachPlan/edit.do' + '?thpId=' + thpId + '&exType=UPDATE';
        layer_show('修改教学计划', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }

    /*用户-删除*/
    function member_del(obj, thpId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/teachPlan/deleteTeachPlanOne.do',
                data: {
                    id: thpId
                },
                dataType: 'json',
                success: function (data) {
                	if(data.code == _GLOBAL_SUCCESS){
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

    function delAll() {
        var chk_value = [];
        $("input[name=thpIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/teachPlan/deleteTeachPlan.do',
                data: {
                    idArray: chk_value
                },
                dataType: 'json',
                success: function (data) {
                	if(data.code == _GLOBAL_SUCCESS){
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

    function _search() {
        myDataTable.fnDraw(true);
    }