var myDataTable;
        $(function () {

            //初始化院校名称下拉框
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

            //初始化年级下拉框
            _init_select("grade", dictJson.grade);

            //初始化院校类型下拉框
            _init_select("recruitType", dictJson.recruitType);

            myDataTable = $('.table-sort').dataTable(
                {
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/check/approvalList.do",
                        data: {
                            "unvsId": function () {
                                return $("#unvsId").val();
                            }, "recruitType": function () {
                                return $("#recruitType").val();
                            }, "grade": function () {
                                return $("#grade").val();
                            }, "pfsnName": function () {
                                return $("#pfsnName").val();
                            }, "stdName": function () {
                                return $("#stdName").val();
                            }, "idCard": function () {
                                return $("#idCard").val();
                            }, "mobile": function () {
                                return $("#mobile").val();
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
                    }, {
                        "mData": null
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.graduateId + '" name="graduateIds"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom += '<a class="maincolor" href="javascript:;" onclick="std_look(\'' + row.learnId + '\',\'' + row.stdId + '\')">' + row.stdName + '</a>';
                                return dom;
                            },
                            "targets": 1
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return row.grade + '级';
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom+=_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                                dom += row.unvsName+"<br>";
                                dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                dom += row.pfsnName;
                                dom += '(' + row.pfsnCode + ')';
                                return dom;
                            },
                            "targets": 3,"class":"text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("saStatus", row.dataStatus);
                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("saStatus", row.pictureStatus);
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("saStatus", row.paperStatus);
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("saStatus", row.scoreStatus);
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("saStatus", row.feeStatus);
                            },
                            "targets": 8
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("stdStage", row.stdStage);
                            },
                            "targets": 9
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("saStatus", row.status);
                            },
                            "targets": 10
                        },

                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';

                                dom += '<a class="tableBtn normal" href="javascript:;" onclick="check_data(\'' + row.graduateId + '\',\'' + row.stdName + '\',\'' + row.learnId + '\',\'' + row.stdId + '\')">进入核查</a>';

                                return dom;
                            },
                            "targets": 11
                        }
                    ]
                });

        });

        function searchDep() {
            myDataTable.fnDraw(true);
        }

        /*用户-编辑*/
        function check_data(graduateId, stdName, learnId, stdId) {
            var url = '/check/resultApproval.do' + '?graduateId=' + graduateId + '&stdName=' + stdName + '&learnId=' + learnId + '&stdId=' + stdId;
            layer_show('毕业核准', url, 1000, 610, function () {
                myDataTable.fnDraw(true);
            });
        }

        function delAll() {
            var chk_value = [];
            $("input[name=graduateIds]:checked").each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value.length == 0) {
                layer.msg('请选择要删除的数据！', {
                    icon: 2,
                    time: 2000
                });
                return;
            }
            layer.confirm('确认要删除吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/graduate/deleteGraduate.do',
                    data: {
                        idArray: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        layer.msg('已删除!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(true);
                        $("input[name=all]").attr("checked", false);
                    },
                    error: function (data) {
                        layer.msg('删除失败！', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(true);
                        $("input[name=all]").attr("checked", false);
                    },
                });
            });
        }