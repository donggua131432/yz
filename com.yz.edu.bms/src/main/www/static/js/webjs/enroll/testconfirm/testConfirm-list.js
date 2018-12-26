 var myDataTable;
    $(function () {

        //初始化院校类型下拉框
        _init_select("recruitType", dictJson.recruitType);

        //初始化学员状态下拉框
        _init_select("stdStage", dictJson.stdStage);

        //初始化全额类型下拉框
        _init_select("scholarship", dictJson.scholarship);

        //标签块
        $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");

        myDataTable = $('.table-sort').dataTable(
            {
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/testConfirm/findAllTestConfirm.do",
                    type: "post",
                    data: {
                        "recruitType": function () {
                            return $("#recruitType").val() ? $("#recruitType").val() : '';
                        }, "stdName": function () {
                            return $("#stdName").val() ? $("#stdName").val() : '';
                        }, "idCard": function () {
                            return $("#idCard").val() ? $("#idCard").val() : '';
                        }, "telephone": function () {
                            return $("#telephone").val() ? $("#telephone").val() : '';
                        }, "scholarship": function () {
                            return $("#scholarship").val() ? $("#scholarship").val() : '';
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
                    "mData": "stdName"
                }, {
                    "mData": null
                }, {
                    "mData": "empName"
                }, {
                    "mData": null
                }, {
                    "mData": "unvsName"
                }, {
                    "mData": null
                }, {
                    "mData": "campusName"
                }, {
                    "mData": null
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.tcId + '" name="tcIds"/>';
                        },
                        "targets": 0
                    }, {
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < dictJson.pfsnLevel.length; i++) {
                                if (dictJson.pfsnLevel[i].dictValue == row.pfsnLevel) {
                                    return dictJson.pfsnLevel[i].dictName;
                                }
                            }
                            return "";
                        },
                        "targets": 4,
                        "class":"text-l"
                    }, {
                        "targets": 5,
                        "class":"text-l"
                    },{
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < dictJson.stdStage.length; i++) {
                                if (dictJson.stdStage[i].dictValue == row.stdStage) {
                                    return dictJson.stdStage[i].dictName;
                                }
                            }
                            return "";
                        },
                        "targets": 6
                    }, {
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < dictJson.grade.length; i++) {
                                if (dictJson.grade[i].dictValue == row.grade) {
                                    return dictJson.grade[i].dictName;
                                }
                            }
                            return '';
                        },
                        "targets": 2
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';

                            dom = '<a title="进入确认" href="javascript:;" onclick="member_edit(\'' + row.learnId + '\')" class="tableBtn normal">';
                            dom += '进入确认</a>';

                            return dom;
                        },
                        //指定是第三列
                        "targets": 8
                    }]
            });

    });

    /*用户-添加*/
    function member_add() {
        var url = '/sysErrorMessage/edit.do' + '?exType=ADD';
        layer_show('添加系统错误码', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /*用户-编辑*/
    function member_edit(learnId) {
        var url = '/testConfirm/okConfirm.do' + '?learnId=' + learnId + '&exType=UPDATE';
        layer.confirm('是否确认？', {
            btn: ['确认', '取消'] //按钮
        }, function () {
            $.post(url, "", function () {
                layer.msg('成功!', {
                    icon: 1,
                    time: 1000
                });
                layer_close();
                myDataTable.fnDraw(false);
            });
        }, function () {
        }, "json");
    }

    /*用户-删除*/
    function member_del(obj, tcId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/sysErrorMessage/deleteSysErrorMessage.do',
                data: {
                    id: errorCode
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

    function delAll() {
        var chk_value = [];
        $("input[name=tcIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/sysErrorMessage/deleteAllSysErrorMessage.do',
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

    function _search() {
        myDataTable.fnDraw(true);
    }