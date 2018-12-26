var myDataTable;
    $(function () {

        $("select").select2({
            placeholder: '--请选择--'
        });

        $("#examYear").append(new Option("", "", false, true));
        $("#examCourse").append(new Option("", "", false, true));
        $("#examType").append(new Option("", "", false, true));

        //初始化年级下拉框
        _init_select("grade", dictJson.grade);

        //初始化专业层次下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel);

        //初始是否参考
        _init_select("isJoin", [{
            "dictValue": "1",
            "dictName": "是"
        }, {
            "dictValue": "0",
            "dictName": "否"
        }]);

        //初始是否查看
        _init_select("isRead", [{
            "dictValue": "1",
            "dictName": "是"
        }, {
            "dictValue": "0",
            "dictName": "否"
        }]);

        //初始考试年度
        $.ajax({
            type: "POST",
            dataType: "json", //数据类型
            url: '/studentExamGK/findExamYear.do',
            success: function (data) {
                var eyJson = data.body;
                if (data.code == '00') {
                    _init_select("examYear", eyJson);
                }
            }
        });

        //初始考试科目
        $.ajax({
            type: "POST",
            dataType: "json", //数据类型
            url: '/studentExamGK/findExamCourse.do',
            success: function (data) {
                var eyJson = data.body;
                if (data.code == '00') {
                    _init_select("examCourse", eyJson);
                }
            }
        });

        //初始考试方式
        $.ajax({
            type: "POST",
            dataType: "json", //数据类型
            url: '/studentExamGK/findExamType.do',
            success: function (data) {
                var eyJson = data.body;
                if (data.code == '00') {
                    _init_select("examType", eyJson);
                }
            }
        });



        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/studentExamGK/findAllStudentExamGK.do",
                type: "post",
                data: {
                    "stdName": function () {
                        return $("#stdName").val();
                    },
                    "idCard": function () {
                        return $("#idCard").val();
                    },
                    "tutorName": function () {
                        return $("#tutorName").val();
                    },
                    "grade": function () {
                        return $("#grade").val();
                    },
                    "pfsnName": function () {
                        return $("#pfsnName").val();
                    },
                    "pfsnLevel": function () {
                        return $("#pfsnLevel").val();
                    },
                    "examYear": function () {
                        return $("#examYear").val();
                    },
                    "examCourse": function () {
                        return $("#examCourse").val();
                    },
                    "examType": function () {
                        return $("#examType").val();
                    },
                    "isJoin": function () {
                        return $("#isJoin").val();
                    },
                    "isRead": function () {
                        return $("#isRead").val();
                    },
                    "examPlace": function () {
                        return $("#examPlace").val();
                    },
                    "examAddress": function () {
                        return $("#examAddress").val();
                    },
                    "examDate": function () {
                        return $("#examDate").val();
                    },
                    "examStartTime": function () {
                        if ($("#examTime").val()) {
                            return $("#examDate").val() + " " + $("#examTime").val().split("-")[0];
                        } else{
                            return '';
                        }
                    },
                    "examEndTime": function () {
                        if ($("#examTime").val()) {
                            return $("#examDate").val() + " " + $("#examTime").val().split("-")[1];
                        } else {
                           return '';
                        }
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
            columns: [
                {
                    "mData": null
                },
                {
                    "mData": "examYear"
                },
                {
                    "mData": null
                },
                {
                    "mData": "stdName"
                },
                {
                    "mData": null
                },
                {
                    "mData": "stdNo"
                },
                {
                    "mData": null
                },
                {
                    "mData": "examPlace"
                },
                {
                    "mData": "examAddress"
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                }
            ],
            "columnDefs": [
                {
                    "render": function (data, type,
                                        row, meta) {
                        return '<input type="checkbox" value="' + row.eigId + '" name="eigIds"/>';
                    },
                    "targets": 0
                },
                {
                    "render": function (data, type, row, meta) {
                        return _findDict('grade', row.grade);
                    },
                    "targets": 2
                },
                {
                    "render": function (data, type, row, meta) {
                        var text = '';
                        if (_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中") != -1) {
                            text += "[专科]";
                        } else {
                            text += "[本科]";
                        }
                        text+=row.pfsnName;
                        return text;
                    },
                    "targets": 4,"class":"text-l"
                },
                {
                    "render": function (data, type, row, meta) {
                        var str = "";
                        if (row.examCourse != null && row.examCourse !='') {
                            str += row.examCourse;
                        }
                        if (row.examType != null && row.examType !='') {
                            str += "[" + row.examType + "]";
                        }
                        return str;
                    },
                    "targets": 6,"class":"text-l"
                },{"targets" : 7,"class":"text-l"},{"targets" : 8,"class":"text-l"},
                {
                    "render": function (data, type, row, meta) {
                        if (row.examStartTime != null && row.examEndTime != null) {
                            var dateTime = row.examStartTime.replace('AM', '上午').replace('PM', '下午') + "-" + row.examEndTime;
                            var date=dateTime.substring(0,10);
                            var time=dateTime.substring(11);
                            return date+'<br>'+time
                        }
                        return '<p class="text-c">-</p>';
                    },
                    "targets": 9,"class":"text-l"
                },
                {
                    "render": function (data, type, row, meta) {
                        var str = "";
                        if (row.placeName != null && row.placeName!='') {
                            str += "[" + row.placeName + "]";
                        }
                        if (row.seatNum != null && row.seatNum!='') {
                            str += "[" + row.seatNum + "]";
                        }
                        return str;
                    },
                    "targets": 10
                },
                {
                    "render": function (data, type, row, meta) {
                        if (row.isRead == 1) {

                            return '<span class="label label-success radius">是</span>';
                        } else {
                            return '<span class="label label-danger radius">否</span>';
                        }
                    },
                    "targets": 11
                },
                {
                    "render": function (data, type, row, meta) {
                        if (row.isJoin == 1) {
                            return '<span class="label label-success radius">是</span>';
                        } else {
                            return '<span class="label label-danger radius">否</span>';
                        }
                    },
                    "targets": 12
                }
            ]
        });

    });

    // 批量改为缺考/参考
    function batchJoinEnable(status) {
        var chk_value = [];
        $("input[name=eigIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }
        var msg = status == "1" ? "参考" : "缺考";
        layer.confirm('确认要' + msg + '吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/studentExamGK/updateJoinStatus.do',
                data: {
                    eigIds: chk_value,
                    status: status
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                        layer.msg('已改为' + msg + '!', {
                            icon: status == "1" ? 6 : 5,
                            time: 1000
                        });
                    }
                }
            });
        });
    }

    //批量删除
    function delAll() {
        var chk_value = [];
        $("input[name=eigIds]:checked").each(function () {
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
                url: '/studentExamGK/deletes.do',
                data: {
                    eigIds: chk_value
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

    /* 国开学员考试导入*/
    function studentExamGKImport() {
        var url = '/studentExamGK/studentExamGKImport.do';
        layer_show('国开学员考试导入', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    function _search() {
        myDataTable.fnDraw(true);
    }

    function loadTime() {
        $.ajax({
            type: "POST",
            dataType: "json", //数据类型
            data: {date: $(this).val()},
            url: '/studentExamGK/findExamTime.do',
            success: function (data) {
                var eyJson = data.body;
                if (data.code == '00') {
                    _init_select("examTime", eyJson);
                }
            }
        });
    }