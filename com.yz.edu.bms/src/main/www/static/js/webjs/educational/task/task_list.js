var myDataTable;
        $(function () {

            //初始状态
            _init_select("isAllow", [
                {
                    "dictValue": "0", "dictName": "否"
                },
                {
                    "dictValue": "1", "dictName": "是"
                }
            ]);
            _init_select("isNeedCheck", [
                {
                    "dictValue": "0", "dictName": "否"
                },
                {
                    "dictValue": "1", "dictName": "是"
                }
            ]);
            myDataTable = $('.table-sort').dataTable(
                {
                    "processing": true,
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: '/task/list.do',
                        data: {
                            "taskTitle": function () {
                                return $("#taskTitle").val();
                            },
                            "creater": function () {
                                return $("#creater").val();
                            },
                            "isAllow": function () {
                                return $("#isAllow").val();
                            },
                            "isNeedCheck": function () {
                                return $("#isNeedCheck").val();
                            },
                            "startTimeStart": function () {
                                return $("#startTimeStart").val();
                            },
                            "startTimeEnd": function () {
                                return $("#startTimeEnd").val();
                            },
                            "endTimeStart": function () {
                                return $("#endTimeStart").val();
                            },
                            "endTimeEnd": function () {
                                return $("#endTimeEnd").val();
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
                        "mData": "taskTitle"
                    }, {
                        "mData": "taskContent"
                    }, {
                        "mData": "creater"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.taskId + '" name="taskIds"/>';
                            },
                            "targets": 0
                        }, {
                            "render": function (data, type, row, meta) {
                                if(!row.startTime){
                                    return '-'
                                }
                                var date=row.startTime.substring(0,10)
                                var time=row.startTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets": 4,"class":"text-l no-warp"
                        },{
                            "render": function (data, type, row, meta) {
                                if(!row.endTime){
                                    return '-'
                                }
                                var date=row.endTime.substring(0,10)
                                var time=row.endTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets": 5,"class":"text-l no-warp"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return 0 == row.isAllow ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return 0 == row.isNeedCheck ? '<span class="label radius">不需要</span>' : '<span class="label label-success radius">需要</span>';
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';

                                dom += '<a class="maincolor tableBtn normal" title="选择学员" href="javascript:;" onclick="choose_student(\'' + row.taskId + '\')">选择学员</a>';

                                return dom;
                            },
                            "targets": 8
                        },
                        {
                            "render": function (data, type, row, meta) {
                                if (row.totalCount == 0) {
                                    return "----------";
                                } else {
                                    var dom = '';
                                    dom += '总数:' + row.totalCount;
                                    dom += ',完成:' + row.finishCount;
                                    dom += ',完成率:' + ((row.finishCount / row.totalCount) * 100).toFixed(2) + '%';
                                    return dom;
                                }

                            },
                            "targets": 9
                        },

                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';

                                dom += '<a class="maincolor" href="javascript:;" title="编辑" onclick="task_edit(\'' + row.taskId + '\')"><i class="iconfont icon-edit"></i></a>';

                                return dom;
                            },
                            //指定是第三列
                            "targets": 10
                        }]
                });
        });

        /*管理员-教务任务-添加*/
        function task_add() {
            var url = '/task/toEdit.do' + '?exType=Add&v=1.0';
            layer_show('添加教务任务', url, null, null, function () {
                myDataTable.fnDraw(true);
            });
        }
        /*管理员-教务任务-编辑*/
        function task_edit(taskId) {
            var url = '/task/toEdit.do' + '?taskId=' + taskId + '&exType=UPDATE';
            layer_show('修改教务任务', url, null, null, function () {
                myDataTable.fnDraw(false);
            });
        }
        //选择学员
        function choose_student(taskId) {
            var url = '/task/toStu.do' + '?taskId=' + taskId;
            layer_show('选择分配学员', url, null, null, function () {
                myDataTable.fnDraw(false);
            }, true);
        }
        /*搜素*/
        function searchTask() {
            myDataTable.fnDraw(true);
        }

        /*用户-导入*/
        function excel_export() {
            var url = '/teacher/excelImport.do';
            layer_show('导入教师信息', url, null, 510, function () {
                myDataTable.fnDraw(true);
            });
        }

        /*批量删除*/
        function delAll() {
            var chk_value = [];
            $("input[name=taskIds]:checked").each(function () {
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