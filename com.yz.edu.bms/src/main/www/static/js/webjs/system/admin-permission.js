$("#funcType").select2({
        placeholder: "--请选择--",
        allowClear: true
    });

    var myDataTable;
    $(function () {
        myDataTable = $('.table-sort').dataTable(
            {
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/auth/permissionList.do",
                    type: "post",
                    dataType: 'json',
                    data: {
                        "funcName": function () {
                            return $("#funcName").val();
                        },
                        "funcType": function () {
                            return $("#funcType").val();
                        },
                        "pId": function () {
                            return $("#pId").val();
                        },
                        "funcCode": function () {
                            return $("#funcCode").val();
                        },
                        "funcId": function () {
                            return $("#funcId").val();
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
                    "mData": "funcId"
                }, {
                    "mData": "funcName"
                }, {
                    "mData": "funcCode"
                }, {
                    "mData": "funcUrl"
                }, {
                    "mData": "funcType"
                }, {
                    "mData": "pId"
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            dom = '<a title="编辑" href="javascript:void(0)" onclick="admin_permission_edit(\'' + row.funcId + '\')" class="ml-5" style="text-decoration:none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="admin_permission_del(this,\'' + row.funcId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>';

                            return dom;
                        },
                        //指定是第三列
                        "targets": 6
                    }, {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            if (row.funcType == 1) {
                                dom = '<span>一级菜单</span>';
                            } else if (row.funcType == 2) {
                                dom = '<span>二级菜单</span>';
                            } else if (row.funcType == 3) {
                                dom = '<span>权限节点</span>';
                            }
                            return dom;
                        },
                        //指定是第三列
                        "targets": 4
                    }]
            });

    });
    /*
     参数解释：
     title	标题
     url		请求的url
     id		需要操作的数据id
     w		弹出层宽度（缺省调默认值）
     h		弹出层高度（缺省调默认值）
     */
    /*管理员-权限-添加*/
    function admin_permission_add() {
        var url = '/auth/permissionEdit.do' + '?exType=Add';
        layer_show('添加权限', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }
    /*管理员-权限-编辑*/
    function admin_permission_edit(funcId) {
        var url = '/auth/permissionEdit.do' + '?funcId=' + funcId + '&exType=UPDATE';
        layer_show('修改权限', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }

    /*管理员-权限-删除*/
    function admin_permission_del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/auth/funcDelete.do',
                data: {
                    funcId: id
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已删除!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                    }
                }
            });
        });
    }
    function searchFunc() {
        myDataTable.fnDraw(true);
    }