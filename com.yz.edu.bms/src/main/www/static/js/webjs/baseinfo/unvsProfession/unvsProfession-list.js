 //定义院校变量，以免重复获取
    var unvsIdJson;
    //院校名称
    $.ajax({
        type: "POST",
        url: "/bdUniversity/findAllKeyValue.do",
        dataType: 'json',
        success: function (data) {
            unvsIdJson = data.body.data;
        }
    });
    //初始化专业大类下拉框
    _init_select("pfsnCata", dictJson.pfsnCata);

    //初始化年度下拉框
    _init_select("year", dictJson.year);

    //初始化年级下拉框
    _init_select("grade", dictJson.grade);

    //初始化专业层次下拉框
    _init_select("pfsnLevel", dictJson.pfsnLevel);

    //初始化授课方式下拉框
    _init_select("teachMethod", dictJson.teachMethod);

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
    //初始状态
    _init_select("isAllow", [
        {
            "dictValue": "1", "dictName": "是"
        },
        {
            "dictValue": "0", "dictName": "否"
        }
    ]);

    //初始状态
    _init_select("testSubject", [
        {
            "dictValue": "1", "dictName": "是"
        },
        {
            "dictValue": "2", "dictName": "否"
        }
    ]);


    var myDataTable;
    $(function () {
        myDataTable = $('.table-sort').dataTable(
            {
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/unvsProfession/findAllUnvsProfession.do",
                    type: "post",
                    data: {
                        "year": function () {
                            return $("#year").val();
                        }, "grade": function () {
                            return $("#grade").val();
                        }, "unvsId": function () {
                            return $("#unvsId").val();
                        }, "pfsnCata": function () {
                            return $("#pfsnCata").val();
                        }, "pfsnCode": function () {
                            return $("#pfsnCode").val();
                        }, "pfsnName": function () {
                            return $("#pfsnName").val();
                        }, "teachMethod": function () {
                            return $("#teachMethod").val();
                        }, "isAllow": function () {
                            return $("#isAllow").val();
                        }, "passScore": function () {
                            return $("#passScore").val();
                        }, "testSubject": function () {
                            return $("#testSubject").val();
                        }, "pfsnLevel": function () {
                            return $("#pfsnLevel").val();
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
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < unvsIdJson.length; i++) {
                                if (unvsIdJson[i].unvs_id == row.unvsId) {
                                    return unvsIdJson[i].unvs_name;
                                }
                            }
                            return "";
                        },
                        "targets": 2,
                        "class":"text-l"
                    }, {
                        "render": function (data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.pfsnId + '" name="pfsnIds"/>';
                        },
                        "targets": 0
                    }, {
                        "render": function (data, type, row, meta) {
                            return "(" + row.pfsnCode + ")" + row.pfsnName;
                        },
                        "targets": 5,
                        "class":"text-l"
                    }, {
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < dictJson.teachMethod.length; i++) {
                                if (dictJson.teachMethod[i].dictValue == row.teachMethod) {
                                    return dictJson.teachMethod[i].dictName;
                                }
                            }
                            return "";
                        },
                        "targets": 6
                    }, {
                        "render": function (data, type, row, meta) {
                            var testSubjecth = row.testSubject;
                            
                            return testSubjecth;
                        },
                        "targets": 7
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
                        "render": function (data, type, row, meta) {
                            for (var i = 0; i < dictJson.pfsnCata.length; i++) {
                                if (dictJson.pfsnCata[i].dictValue == row.pfsnCata) {
                                    return dictJson.pfsnCata[i].dictName;
                                }
                            }
                            return "";
                        },
                        "targets": 3
                    }, {
                        "render": function (data, type, row, meta) {
                            return 1 == row.isAllow ? '<span class="label label-success radius">可选</span>' : '<span class="label radius">不可选</span>';
                        },
                        "targets": 8
                    }, {
                        "render": function (data, type, row, meta) {
                            var year = _findDict('year', row.year);
                            var grade = _findDict('grade', row.grade);
                            var txt = '';
                            if(year) {
                                txt = year;
                            }
                            if(grade) {
                                txt += '_' + grade;
                            }
                            return txt;
                        },
                        "targets": 1
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';

                            dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.pfsnId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                           /*  dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="member_del(this,\'' + row.pfsnId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>'; */

                            return dom;
                        },
                        //指定是第三列
                        "targets": 9
                    }]
            });

    });

    /*用户-添加*/
    function member_add() {
        var url = '/unvsProfession/edit.do' + '?exType=ADD';
        layer_show('添加大学专业', url, null, 500, function () {
//            myDataTable.fnDraw(true);
        });
    }

    /*用户-编辑*/
    function member_edit(pfsnId) {
        var url = '/unvsProfession/edit.do' + '?pfsnId=' + pfsnId + '&exType=UPDATE';
        layer_show('修改大学专业', url, null, 500, function () {
//            myDataTable.fnDraw(true);
        });
    }

    /*用户-导入*/
    function excel_export() {
        var url = '/unvsProfession/excelImport.do';
        layer_show('导入大学专业', url, null, 500, function () {
            myDataTable.fnDraw(true);
        });
    }

    /*用户-删除*/
    function member_del(obj, pfsnId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/unvsProfession/deleteUnvsProfession.do',
                data: {
                    id: pfsnId
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

    function opendown_All() {
        var chk_value = [];
        $("input[name=pfsnIds]:checked").each(function () {
            chk_value.push($(this).val());
        });
        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon : 5,
                time : 1000
            });
            return;
        }
        layer.confirm('请选择禁用还是启用', {
            btn: ['启用', '禁用'] //按钮
        }, function (index) {
            $.ajax({
                type: 'POST',
                url: '/unvsProfession/opendownAllUnvsProfession.do' + '?exType=OPEN',
                data: {
                    idArray: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已启用!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
                error: function (data) {
                    layer.msg('启用失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
            });
        }, function (index) {
            $.ajax({
                type: 'POST',
                url: '/unvsProfession/opendownAllUnvsProfession.do' + '?exType=DOWN',
                data: {
                    idArray: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已禁用!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
                error: function (data) {
                    layer.msg('禁用失败！', {
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
        $("input[name=pfsnIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/unvsProfession/deleteAllUnvsProfession.do',
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