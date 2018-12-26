 var myDataTable;
    $(function () {

        //初始辅导书发放状态
        _init_select("courseType", [{
            "dictValue": "FD",
            "dictName": "辅导课"
        }, {
            "dictValue": "XK",
            "dictName": "学科课程"
        }]);

        //初始化年级下拉框
        _init_select("grade", dictJson.grade);

        //初始化学员阶段下拉框
        _init_select("stdStage", dictJson.stdStage);

        $('#stdStage').closest('.f-l').hide();
        $('#startTime').closest('.f-l').hide();
        $('#endTime').closest('.f-l').hide();


        $("#courseType").change(function () {
            if($(this).val()=="FD"){
                $('#stdStage').closest('.f-l').show();
                $('#startTime').closest('.f-l').show();
                $('#endTime').closest('.f-l').show();
            }else {
                $("#startTime").val("");
                $("#endTime").val("");
                $('#stdStage').closest('.f-l').hide();
                $('#startTime').closest('.f-l').hide();
                $('#endTime').closest('.f-l').hide();
            }
        })

    });

    function initTab() {
        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/course/findExportCourse.do",
                type: "post",
                data: {
                    "grade": function () {
                        return $("#grade").val();
                    },
                    "courseType": function () {
                        return $("#courseType").val();
                    },
                    "stdStage": function () {
                        return $("#stdStage").val();
                    },
                    "startTime": function () {
                        return $("#startTime").val();
                    },
                    "endTime": function () {
                        return $("#endTime").val();
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
                "mData": "idCard"
            }, {
                "mData": "stdPassword"
            }, {
                "mData": "stdName"
            }, {
                "mData": "sex"
            }, {
                "mData": null
            }, {
                "mData": "checkTime"
            }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        if ($("#courseType").val() == "FD") {
                            return row.grade + "级" + row.pfsnLevel
                        } else if ($("#courseType").val() == "XK") {
                            return row.grade + "级" + row.pfsnLevel + row.unvsName + row.pfsnName
                        }
                        else {
                            return "";
                        }
                    },
                    "targets": 4
                }
            ]
        });
    }

    function _search() {
        if ($("#grade").val() == "") {
            layer.msg("请选择年级！", {icon: 5, time: 1000});
            return;
        }
        if ($("#courseType").val() == "") {
            layer.msg("请选择课程类型！", {icon: 5, time: 1000});
            return;
        }

        if(!myDataTable){
            initTab()
        }else {
            myDataTable.fnDraw(true);
        }
    }

    /* 导出上课平台安排*/
    function excel_export(){
        if ($("#grade").val() == "") {
            layer.msg("请选择年级！", {icon: 5, time: 1000});
            return;
        }
        if ($("#courseType").val() == "") {
            layer.msg("请选择课程类型！", {icon: 5, time: 1000});
            return;
        }

        $('<form method="post" action="' + '/course/exportCourse.do' + '">'
            +'<input type="text" name="courseType" value="'+$("#courseType").val()+'"/>'
            +'<input type="text" name="grade" value="'+$("#grade").val()+'"/>'
            +'<input type="text" name="stdStage" value="'+$("#stdStage").val()+'"/>'
            +'<input type="text" name="startTime" value="'+$("#startTime").val()+'"/>'
            +'<input type="text" name="endTime" value="'+$("#endTime").val()+'"/>'
            +'</form>').appendTo('body').submit().remove();
    }