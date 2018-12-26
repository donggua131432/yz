 var myDataTable;
        $(function () {

            $("#status").select2({
                placeholder: "--请选择--",
                allowClear: true
            });

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "processing": true,
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/charges/list.do",
                            type: "post",
                            data: {
                                "itemName": function () {
                                    return $("#itemName")
                                        .val();
                                },
                                "status": function () {
                                    return $("#status")
                                        .val();
                                },
                                "itemCode": function () {
                                    return $("#itemCode")
                                        .val();
                                }

                            }
                        },
                        "pageLength": 10,
                        "pagingType": "full_numbers",
                        "ordering": false,
                        "searching": false,
                        "createdRow": function (row, data,
                                                dataIndex) {
                            $(row).addClass('text-c');
                        },
                        "language": _my_datatables_language,
                        columns: [{
                            "mData": null
                        }, {
                            "mData": "itemName"
                        }, {
                            "mData": "itemCode"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": "remark"
                        }, {
                            "mData": "createUser"
                        }, {
                            "mData": "createTime"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return '<input type="checkbox" value="' + row.itemCode + '" name="itemCodes"/>';
                                },
                                "targets": 0
                            },{"targets": 1,"class":"text-l"},
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    if (row.status == '1') {
                                        dom += '<a onClick="item_stop(\''
                                            + row.itemCode
                                            + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                    } else if (row.status == '2') {
                                        dom += '<a onClick="item_start(\''
                                            + row.itemCode
                                            + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                    }
                                    dom += ' &nbsp;';
                                    dom += '<a title="编辑" href="javascript:void(0)" onclick="charges_edit(\''
                                        + row.itemCode
                                        + '\')" class="ml-5" style="text-decoration:none">';
                                    dom += '<i class="iconfont icon-edit"></i></a>';
                                    dom += ' &nbsp;';
                                    dom += '<a title="删除" href="javascript:;" onclick="item_del(this,\''
                                        + row.itemCode
                                        + '\')" class="ml-5" style="text-decoration: none">';
                                    dom += '<i class="iconfont icon-shanchu"></i></a>';
                                    return dom;
                                },
                                "targets": 10
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    if (row.status == 1) {
                                        return '<span class="label label-success radius">已启用</span>';
                                    } else {
                                        return '<span class="label label-danger radius">已禁用</span>';
                                    }
                                },
                                "targets": 9
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict(
                                        "itemType",
                                        row.itemType);
                                },
                                "targets": 3
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    if (row.itemYear != null && row.itemYear != '') {
                                        return _findDict("itemYear", row.itemYear);
                                    } else {
                                        return '/';
                                    }
                                },
                                "targets": 5
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    for (var i = 0; i < row.recruitTypes.length; i++) {
                                        var recruitType = row.recruitTypes[i];
                                        if (i == (row.recruitTypes.length - 1)) {
                                            dom += _findDict(
                                                    "recruitType",
                                                    recruitType)
                                                + ' ';
                                            break;
                                        }
                                        dom += _findDict(
                                                "recruitType",
                                                recruitType)
                                            + ', ';

                                    }
                                    if (row.recruitTypes.length < 1) {
                                        dom = '无';
                                    }
                                    return dom;
                                },
                                "targets": 4,
                                "class":"text-l"
                            }]
                    });
        });

        function charges_add() {
            var url = '/charges/toAdd.do';
            layer_show('添加财务科目', url, null, 380, function () {

            });

        }

        function charges_edit(itemCode) {
            var url = '/charges/toEdit.do' + '?itemCode='
                + itemCode;
            layer_show('修改财务科目', url, null, 510, function () {
//                myDataTable.fnDraw(false);
            });
        }

        /*管理员-删除*/
        function item_del(obj, id) {
            layer.confirm('确认要删除吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/charges/delete.do',
                    data: {
                        itemCode: id
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('已删除!', {
                                icon: 1,
                                time: 1000
                            });
                        }
                        myDataTable.fnDraw(false);
                    }
                });
            });
        }

        function item_stop(itemCode) {
            layer.confirm('确认要停用吗？', function (index) {
                //此处请求后台程序，下方是成功后的前台处理……
                $.ajax({
                    type: 'POST',
                    url: '/charges/block.do',
                    data: {
                        itemCode: itemCode,
                        exType: 'BLOCK'
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            myDataTable.fnDraw(false);
                            layer.msg('已停用!', {
                                icon: 5,
                                time: 1000
                            });
                        }
                    }
                });
            });
        }

        function item_start(itemCode) {
            layer.confirm('确认要启用吗？', function (index) {
                //此处请求后台程序，下方是成功后的前台处理……
                $.ajax({
                    type: 'POST',
                    url: '/charges/block.do',
                    data: {
                        itemCode: itemCode,
                        exType: 'START'
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            myDataTable.fnDraw(false);
                            layer.msg('已启用!', {
                                icon: 6,
                                time: 1000
                            });
                        }
                    }
                });

            });

        }
        function searchItem() {
            myDataTable.fnDraw(true);
        }

        function delAll() {
            var chk_value = [];
            var $input = $("input[name=itemCodes]:checked");

            $input.each(function () {
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
                    url: '/charges/deleteFeeItems.do',
                    data: {
                        itemCodes: chk_value
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