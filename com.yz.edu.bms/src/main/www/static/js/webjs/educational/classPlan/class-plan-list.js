 var myDataTable;
            $(function() {

            	 $('select').select2({
                     placeholder : "--请选择--",
                     allowClear : true,
                     width : "59%"
                 });
                //初始化年度下拉框
                _init_select("year", dictJson.year);
                //初始化课程类型
                _init_select("courseType", dictJson.courseType);
                //上课方式
                _init_select("cpType", dictJson.cpType, $("#cpType").val());
                //校区
                _simple_ajax_select({
                    selectId : "placeId",
                    searchUrl : '/classPlan/findCampusByName.do',
                    sData : {},
                    showText : function(item) {
                        return item.campusName;
                    },
                    showId : function(item) {
                        return item.campusId;
                    },
                    placeholder : '请选择校区'
                });
                $("#placeId").append(new Option("", "", false, true));
                
                myDataTable = $('.table-sort').dataTable(
                        {
                            "processing": true,
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/classPlan/findAllClassPlan.do",
                                type : "post",
                                data : {
                                    "empName" : function() {
                                        return $("#empName").val();
                                    },
                                    "courseName" : function() {
                                        return $("#courseName").val();
                                    },
                                    "pfsnName" : function() {
                                        return $("#pfsnName").val();
                                    },
                                    "courseType" : function() {
                                        return $("#courseType").val();
                                    },
                                    "year" : function() {
                                        return $("#year").val();
                                    },
                                    "placeId" : function() {
                                        return $("#placeId").val() == null ? "" : $("#placeId").val();
                                    },
                                    "cpType" : function() {
                                        return $("#cpType").val();
                                    },
                                    "startTime" : function() {
                                        return $("#startTime").val();
                                    },
                                    "endTime" : function() {
                                        return $("#endTime").val();
                                    }
                                }
                            },
                            "pageLength" : 10,
                            "pagingType" : "full_numbers",
                            "ordering" : false,
                            "searching" : false,
                            "createdRow" : function(row, data, dataIndex) {
                                $(row).addClass('text-c');
                                $(row).children('td').eq(9).attr('style', 'text-align: left;');
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
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            } ],
                            "columnDefs" : [
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return '<input type="checkbox" value="'+ row.cpId + '" name="cpIds"/>';
                                        },
                                        "targets" : 0
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("year", row.year);
                                        },
                                        "targets" : 1
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("courseType", row.courseType);
                                        },
                                        "targets" : 2
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return row.courseName;
                                        },
                                        "targets" : 3,
                                        "class":"text-l"
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return row.empName;
                                        },
                                        "targets" : 4
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return row.campusName;
                                        },
                                        "targets" : 5
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("cpType", row.cpType);
                                        },
                                        "targets" : 6
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	var dom = '';
                                        	if(row.startTime){
                                        		dom += new Date(row.startTime).format("yyyy-MM-dd hh:mm:ss").split(" ")[0];	
                                        	}
                                            return dom;
                                        },
                                        "targets" : 7
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	var dom = '';
                                        	if(row.startTime&&row.endTime){
                                        		dom += new Date(row.startTime).format("yyyy-MM-dd hh:mm").split(" ")[1] + "  至  "
                                                + new Date(row.endTime).format("yyyy-MM-dd hh:mm").split(" ")[1];
                                        	}
                                            return dom;
                                        },
                                        "targets" : 8
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';
                                            row = row.professional;
                                            for (var i = 0; i < row.length; i++) {
                                                _findDict("recruitType", row[i].recruitType).indexOf("成人")!=-1?dom+="[成教]":dom+="[国开]";
                                                dom += row[i].unvsName + "<br>"
                                                _findDict("pfsnLevel", row[i].pfsnLevel).indexOf("高中")!=-1?dom+="[专科]":dom+="[本科]"
                                                dom+="(" + _findDict("grade", row[i].grade) + ")"+row[i].pfsnName+"(" + row[i].pfsnCode + ")"
                                                dom+="<br>"
                                            }
                                            return dom;
                                        },
                                        "targets" : 9
                                    }, {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';

                                            dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.cpId + '\')" class="ml-5" style="text-decoration: none">';
                                            dom += '<i class="iconfont icon-edit"></i></a>';
                                            return dom;
                                        },
                                        //指定是第三列
                                        "targets" : 10
                                    } ]
                        });

            });
            /*用户-添加*/
            function member_add() {
                var url = '/classPlan/edit.do' + '?exType=ADD';
                layer_show('添加课程安排', url, null, 510, function() {
                    myDataTable.fnDraw(true);
                });
            }

            /*用户-生成课表*/
            function make_schedule() {
                var url = '/classPlan/makeScheduleShow.do';
                layer_show('生成课表', url, null, 510, function() {
                    myDataTable.fnDraw(false);
                }, true);
            }

            /*用户-编辑*/
            function member_edit(cpId) {
                var url = '/classPlan/edit.do' + '?cpId=' + cpId + '&exType=UPDATE';
                layer_show('修改课程安排', url, null, 510, function() {

                });
            }

            /*用户-导入*/
            function excel_import() {
                var url = '/classPlan/excelImport.do';
                layer_show('课程安排导入', url, null, 510, function() {
                    myDataTable.fnDraw(true);
                });
            }
            
            /*用户-导入*/
            function qingshu_import() {
                var url = '/classPlan/qingshuImport.do';
                layer_show('课程安排导入', url, null, 510, function() {
                    myDataTable.fnDraw(true);
                });
            }
			
           
            /*课程安排-导出*/
            function query_excel_export() {
                var tableSetings = myDataTable.fnSettings();
                var paging_length = tableSetings._iDisplayLength;//当前每页显示多少
                var page_start = tableSetings._iDisplayStart;//当前页开始
                $('<form method="post" accept-charset="UTF-8" action="' + '/classPlan/queryexport.do' + '">'
                    + '<input type="text" name="length" value="' + paging_length + '"/>'
                    + '<input type="text" name="start" value="' + page_start + '"/>'
                    + '<input type="text" name="empName" value="' + $("#empName").val() + '"/>'
                    + '<input type="text" name="courseName" value="' + $("#courseName").val() + '"/>'
                    + '<input type="text" name="pfsnName" value="' + $("#pfsnName").val() + '"/>'
                    + '<input type="text" name="courseType" value="' + $("#courseType").val() + '"/>'
                    + '<input type="text" name="year" value="' + $("#year").val() + '"/>'
                    + '<input type="text" name="placeId" value="' + $("#placeId").val() + '"/>'
                    + '<input type="text" name="cpType" value="' + $("#cpType").val() + '"/>'
                    + '<input type="text" name="startTime" value="' + $("#startTime").val() + '"/>'
                    + '<input type="text" name="endTime" value="' + $("#endTime").val() + '"/>'
                    + '</form>').appendTo('body').submit().remove();
            }
            
            /* 勾选课程安排-导出*/
            function check_excel_export() {
            	var url = '/classPlan/checkexport.do';
            	 var chk_value = [];
                 $("input[name=cpIds]:checked").each(function() {
                     chk_value.push($(this).val());
                 });
                 if (chk_value == null || chk_value.length <= 0||chk_value=="") {
                     layer.msg('未选择任何数据!', {
                         icon : 5,
                         time : 1000
                     });
                     return;
                 }
                $('<form method="post" action="' + url + '">'
                    + '<input type="text" name="idArray[]" value="' + chk_value + '"/>'
                    + '</form>').appendTo('body').submit().remove();
            }
            
            
            
            function _search() {
                myDataTable.fnDraw(true);
            }
            function distribution_teacher() {
                var chk_value = [];
                $("input[name=cpIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                
                if (chk_value == null || chk_value.length <= 0||chk_value=="") {
                    layer.msg('未选择任何数据!', {
                        icon : 5,
                        time : 1000
                    });
                    return;
                }
                var url = '/classPlan/toDistribution.do' + '?idArray[]=' + chk_value;
                layer_show('分配老师', url, null, 510, function() {
                    myDataTable.fnDraw(false);
                });
            }
            function delAll() {
                var chk_value = [];
                $("input[name=cpIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });

                layer.confirm('确认要删除吗？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : '/classPlan/deleteClassPlan.do',
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