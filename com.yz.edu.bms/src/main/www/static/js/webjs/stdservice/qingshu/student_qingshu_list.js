var myDataTable;
    $(function () {
        $('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
        //初始状态
        _init_select("genericScore", [
            {"dictValue": "0", "dictName": "低于30分"},
            {"dictValue": "1", "dictName": "30分—59分"},
            {"dictValue": "1", "dictName": "大于等于60分"}
        ]);
        _init_select("finalScore", [
            {"dictValue": "0", "dictName": "低于30分"},
            {"dictValue": "1", "dictName": "30分—59分"},
            {"dictValue": "2", "dictName": "大于等于60分"}
        ]);
        _init_select("summaryScore", [
            {"dictValue": "0", "dictName": "低于30分"},
            {"dictValue": "1", "dictName": "30分—59分"},
            {"dictValue": "2", "dictName": "大于等于60分"}
        ]);
        _init_select("confirmStatus", [
            {"dictValue": "0", "dictName": "待确认"},
            {"dictValue": "1", "dictName": "不会操作"},
            {"dictValue": "2", "dictName": "已学会操作"}
        ]);
        _init_select("isRemark", [
            {"dictValue": "0", "dictName": "无备注"},
            {"dictValue": "1", "dictName": "有备注"}
        ]);
        _init_select("semester", [
            {"dictValue": "1", "dictName": "第一学期"},
            {"dictValue": "2", "dictName": "第二学期"},
            {"dictValue": "3", "dictName": "第三学期"},
            {"dictValue": "4", "dictName": "第四学期"},
            {"dictValue": "5", "dictName": "第五学期"},
            {"dictValue": "6", "dictName": "第六学期"}
        ]);
        
        $("#genericScore").change(function () {
            if($(this).val() && $("#semester").val()==""){
                layer.msg("请选择学期");
                $(this).val("").trigger('change');

            }
            if($(this).val()) {
                $("#finalScore").val("").trigger('change');
                $("#summaryScore").val("").trigger('change');
            }
        });

        $("#finalScore").change(function () {
            if($(this).val() && $("#semester").val()==""){
                layer.msg("请选择学期");
                $(this).val("").trigger('change');

            }
            if($(this).val()){
                $("#genericScore").val("").trigger('change');
                $("#summaryScore").val("").trigger('change');
            }
        });

        $("#summaryScore").change(function () {
            if($(this).val() && $("#semester").val()==""){
                layer.msg("请选择学期");
                $(this).val("").trigger('change');

            }
            if($(this).val()) {
                $("#finalScore").val("").trigger('change');
                $("#genericScore").val("").trigger('change');
            }
        });
        
       $("#semester").change(function () {
           if($(this).val()==""){
               $("#genericScore").val("").trigger('change');
               $("#summaryScore").val("").trigger('change');
               $("#finalScore").val("").trigger('change');
           }
       })


        _simple_ajax_select({
            selectId: "unvsId",
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
        //初始化年级下拉框
        _init_select("grade", dictJson.grade);

        //初始化院校类型下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel);

        _simple_ajax_select({
            selectId: "taskId",
            searchUrl: '/studyActivity/findTaskInfo.do?taskType=12',
            sData: {},
            showText: function (item) {
                return item.task_title;
            },
            showId: function (item) {
                return item.task_id;
            },
            placeholder: '--请选择任务--'
        });
        $("#taskId").append(new Option("", "", false, true));

        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: '/qingshu/findAllQingshuList.do',
                type: "post",
                data: function (pageData) {
                    pageData = $.extend({}, {
                        start: pageData.start,
                        length: pageData.length
                    }, $("#export-form").serializeObject());
                    return pageData;
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
            columns: [
                {"mData": "stdName"},
                {"mData": "schoolRoll"},
                {"mData": null},
                {"mData": null},
                {"mData": "taskTitle"},
                {"mData": "tutor"},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": "remark"},
                {"mData": null}
            ],
            "columnDefs": [
                {
                    "targets": 2, "render": function (data, type, row, meta) {
                    return _findDict("grade", row.grade);
                }
                },
                {
                    "targets": 3, "class": "text-l", "render": function (data, type, row, meta) {
                    var pfsnName = row.pfsnName, unvsName = row.unvsName, recruitType = row.recruitType,
                        pfsnCode = row.pfsnCode, pfsnLevel = row.pfsnLevel, text = '';
                    if (recruitType) {
                        if (_findDict("recruitType", recruitType).indexOf("成人") != -1) {
                            text += "[成教]";
                        } else {
                            text += "[国开]";
                        }
                    }
                    if (unvsName) text += unvsName + '</br>';
                    if (pfsnLevel) {
                        if (_findDict("pfsnLevel", pfsnLevel).indexOf("高中") != -1) {
                            text += "[专科]";
                        } else {
                            text += "[本科]";
                        }
                    }
                    if (pfsnName) text += pfsnName;
                    if (pfsnCode) text += "(" + pfsnCode + ")";
                    return text ? text : '无';
                }
                },
                {
                    "targets": 6, "render": function (data, type, row, meta) {
                    if (!row.firstCourse) return '';
                    var dom = "";
                    for (var i = 0; i < row.firstCourse.length; i++) {
                        var course = row.firstCourse[i];
                        dom += '<a class="c-blue"'+getRedStyle(course.genericScore,course.finalScore,course.summaryScore,1)+ 'href="javascript:;" onclick="courseScore(\'' + course.courseName + '\',' + course.genericScore + ',' + course.finalScore + ',' + course.summaryScore + ')">' + (i + 1) + '：' + course.courseName + '</a><br/>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 7, "render": function (data, type, row, meta) {
                    if (!row.secondCourse) return '';
                    var dom = "";
                    for (var i = 0; i < row.secondCourse.length; i++) {
                        var course = row.secondCourse[i];
                        dom += '<a class="c-blue"'+getRedStyle(course.genericScore,course.finalScore,course.summaryScore,2)+ 'href="javascript:;" onclick="courseScore(\'' + course.courseName + '\',' + course.genericScore + ',' + course.finalScore + ',' + course.summaryScore + ')">' + (i + 1) + '：' + course.courseName + '</a><br/>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 8, "render": function (data, type, row, meta) {
                    if (!row.thirdCourse) return '';
                    var dom = "";
                    for (var i = 0; i < row.thirdCourse.length; i++) {
                        var course = row.thirdCourse[i];
                        dom += '<a class="c-blue"'+getRedStyle(course.genericScore,course.finalScore,course.summaryScore,3)+'href="javascript:;" onclick="courseScore(\'' + course.courseName + '\',' + course.genericScore + ',' + course.finalScore + ',' + course.summaryScore + ')">' + (i + 1) + '：' + course.courseName + '</a><br/>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 9, "render": function (data, type, row, meta) {
                    if (!row.fourCourse) return '';
                    var dom = "";
                    for (var i = 0; i < row.fourCourse.length; i++) {
                        var course = row.fourCourse[i];
                        dom += '<a class="c-blue"'+getRedStyle(course.genericScore,course.finalScore,course.summaryScore,4)+ 'href="javascript:;" onclick="courseScore(\'' + course.courseName + '\',' + course.genericScore + ',' + course.finalScore + ',' + course.summaryScore + ')">' + (i + 1) + '：' + course.courseName + '</a><br/>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 10, "render": function (data, type, row, meta) {
                    if (!row.fiveCourse) return '';
                    var dom = "";
                    for (var i = 0; i < row.fiveCourse.length; i++) {
                        var course = row.fiveCourse[i];
                        dom += '<a class="c-blue"'+getRedStyle(course.genericScore,course.finalScore,course.summaryScore,5)+ 'href="javascript:;" onclick="courseScore(\'' + course.courseName + '\',' + course.genericScore + ',' + course.finalScore + ',' + course.summaryScore + ')">' + (i + 1) + '：' + course.courseName + '</a><br/>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 11, "render": function (data, type, row, meta) {
                    if (!row.sixCourse) return '';
                    var dom = "";
                    for (var i = 0; i < row.sixCourse.length; i++) {
                        var course = row.sixCourse[i];
                        dom += '<a class="c-blue"'+getRedStyle(course.genericScore,course.finalScore,course.summaryScore,6)+ 'href="javascript:;" onclick="courseScore(\'' + course.courseName + '\',' + course.genericScore + ',' + course.finalScore + ',' + course.summaryScore + ')">' + (i + 1) + '：' + course.courseName + '</a><br/>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 12, "render": function (data, type, row, meta) {
                    if (row.confirmStatus && row.confirmStatus == '2') {
                        return "<label class='label label-success radius'>已学会操作</label>";
                    }
                    if (row.confirmStatus && row.confirmStatus == '1') {
                        return "<label class='label  label-danger radius'>不会操作</label>";
                    } else {
                        return "<label class='label radius'>待确认</label>";
                    }
                }
                },
                {
                    "targets": 14, "render": function (data, type, row, meta) {
                    var dom = '';

                    dom = '<a title="重置学员成绩" href="javascript:;" onclick="reset(\'' + row.qingshuId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-shenhe"></i></a>';

                    dom += '<a title="添加备注" href="javascript:;" onclick="addRemark(\'' + row.qingshuId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-edit"></i></a>';
                    return dom
                }
                }
            ]
        });
    });

    /*重置*/
    function reset(id) {
        var url = '/qingshu/toReset.do' + '?id=' + id;
        layer_show('重置学员成绩', url, null, 300, function () {
        });
    }

    /*搜素*/
    function searchResult() {
        myDataTable.fnDraw(true);
    }

    function init_pfsn_select() {
        _simple_ajax_select({
            selectId: "pfsnId",
            searchUrl: '/baseinfo/sPfsn.do',
            sData: {
                sId: function () {
                    return $("#unvsId").val() ? $("#unvsId").val() : '';
                },
                ext1: function () {
                    return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
                },
                ext2: function () {
                    return $("#grade").val() ? $("#grade").val() : '';
                }
            },
            showText: function (item) {
                var text = '(' + item.pfsnCode + ')' + item.pfsnName;
                text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
                return text;
            },
            showId: function (item) {
                return item.pfsnId;
            },
            placeholder: '--请选择专业--'
        });
        $("#pfsnId").append(new Option("", "", false, true));
    }


    /*添加备注*/
    function addRemark(qingshuId) {
        $("#addRemark").val('');
        var url = '/qingshu/addRemark.do';
        layer.open({
            type: 1,
            btn: ['确定'],
            yes: function (index, layero) {
                var addRemark = $("#addRemark").val();
                if (addRemark.length > 20) {
                    layer.msg('最大支持20个字符!', {
                        icon: 2,
                        time: 2000
                    });
                    return;
                }

                $.ajax({
                    type: 'POST',
                    url: url,
                    data: {
                        qingshuId: qingshuId,
                        addRemark: addRemark
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('添加成功!', {
                                icon: 1,
                                time: 1000
                            });
                            myDataTable.fnDraw(false);
                        }
                    }
                });
                layer.close(index)
            },
            area: ['500px', '300px'],
            content: $("#addRemarkContent")
        });
    }

    //课程分数
    function courseScore(title, genericScore, finalScore, summaryScore) {
        $("#gScore").text(genericScore);
        $("#fScore").text(finalScore);
        $("#sScore").text(summaryScore);
        layer.open({
            type: 1,
            title: title,
            area: ['300px', '230px'],
            content: $("#courseScore")
        });
    }

    function getRedStyle(genericScore, finalScore, summaryScore,semester) {
        var redstyle = ' ';
        if($("#genericScore").val()!="" &&  genericScore < 60 && $("#semester").val()==semester){
            redstyle = 'style="color: #f00 !important;"';
        }
        if($("#finalScore").val()!="" &&  finalScore < 60 && $("#semester").val()==semester){
            redstyle = 'style="color: #f00 !important;"';
        }
        if($("#summaryScore").val()!="" &&  summaryScore < 60 && $("#semester").val()==semester){
            redstyle = 'style="color: #f00 !important;"';
        }
        return redstyle;
    }

    function scoreImport() {
        var url = '/qingshu/scoreImport.do';
        layer_show('青书学堂上课跟进分数导入', url, null, 510, function () {

        });
    }