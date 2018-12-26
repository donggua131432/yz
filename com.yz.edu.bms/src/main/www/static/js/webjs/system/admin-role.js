var myDataTable;
    $(function () {
        myDataTable = $('.table-sort').dataTable(
            {
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/auth/roleList.do",
                    data: {
                        roleName: function () {
                            return $("#roleName").val() ? $("#roleName").val() : '';
                        },
                        roleCode: function () {
                            return $("#roleCode").val() ? $("#roleCode").val() : '';
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
                    "mData": "roleId"
                }, {
                    "mData": "roleName"
                }, {
                    "mData": "roleCode"
                }, {
                    "mData": "description"
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            dom = '<a title="编辑" href="javascript:void(0)" onclick="admin_role_edit(\'' + row.roleId + '\')" class="ml-5" style="text-decoration:none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="admin_role_del(this,\'' + row.roleId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>';

                            return dom;
                        },
                        //指定是第三列
                        "targets": 4
                    }]
            });

    });


    /*管理员-角色-添加*/
    function admin_role_add() {
        var url = '/auth/roleEdit.do' + '?exType=Add';
        layer_show('添加角色', url, null, null, function () {
            myDataTable.fnDraw(true);
        }, true);
    }
    /*管理员-角色-编辑*/
    function admin_role_edit(roleId) {
        var url = '/auth/roleEdit.do' + '?roleId=' + roleId + '&exType=UPDATE';
        layer_show('修改角色', url, null, null, function () {
            myDataTable.fnDraw(false);
        }, true);
    }
    /*管理员-角色-删除*/
    function admin_role_del(obj, roleId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/auth/roleDelete.do',
                data: {
                    roleId: roleId
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已删除!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(false);
                },
                error: function (data) {
                    layer.msg('删除失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(false);
                },
            });
        });


    }

    function searchRole() {
        myDataTable.fnDraw(true);
    }