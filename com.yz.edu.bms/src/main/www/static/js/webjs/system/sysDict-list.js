	var myDataTable;
    $(function () {
        myDataTable = $('.table-sort').dataTable({
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/sysDict/findAllSysDict.do",
                type: "post",
                data: {
                    "dictId": function () {
                        return $("#dictId").val();
                    },
                    "dictName": function () {
                        return $("#dictName").val();
                    }
                }
            },
            "pageLength" : 10,
            "pagingType" : "full_numbers",
            "ordering" : false,
            "searching" : false,
            "lengthMenu" : [ 10, 20 ],
            "createdRow" : function(row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language" : _my_datatables_language,
            columns: [{
                "mData": null
            }, {
                "mData": "dictId"
            }, {
                "mData": "dictName"
            }, {
                "mData": "dictValue"
            }, {
                "mData": "orderNum"
            }, {
                "mData": null
            }],
            "columnDefs": [{
                "render": function (data, type, row, meta) {
                    return '<input type="checkbox" value="' + row.dictId + '" name="dictIds"/>';
                },
                "targets": 0
            }, {
                "render": function (data, type, row, meta) {
                    var dom = '';

                    dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.dictId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
                    dom += '&nbsp;&nbsp;&nbsp;';
                    dom += '<a title="删除" href="javascript:;" onclick="member_del(this,\'' + row.dictId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="Hui-iconfont">&#xe6e2;</i></a>';

                    return dom;
                },
                //指定是第三列
                "targets": 5
            }]
        });

    });

    /*用户-添加*/
    function member_add() {
        var url = '/sysDict/edit.do' + '?exType=ADD';
        layer_show('添加字典', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /*用户-编辑*/
    function member_edit(dictId) {
        var url = '/sysDict/edit.do' + '?dictId=' + dictId + '&exType=UPDATE';
        layer_show('修改字典', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }

    /*用户-删除*/
    function member_del(obj, dictId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/sysDict/deleteSysDict.do',
                data: {
                    id: dictId
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已删除!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(false);
                    $("input[name=all]").attr("checked", false);
                },
                error: function (data) {
                    layer.msg('删除失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(false);
                    $("input[name=all]").attr("checked", false);
                },
            });
        });
    }

    function delAll() {
        var chk_value = [];
        $("input[name=dictIds]:checked").each(function () {
            chk_value.push($(this).val());
        });
        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon : 5,
                time : 1000
            });
            return;
        }
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/sysDict/deleteAllSysDict.do',
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

    function search() {
        myDataTable.fnDraw(true);
    }