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
                            url: "/examYear/list.do",
                            type: "post",
                            data: {

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
                            "mData": "examYear"
                        }, {
                            "mData": "createUser"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return '<input type="checkbox" value="' + row.eyId + '" name="eyIds"/>';
                                },
                                "targets": 0
                            },{
                                "render" : function(data, type, row, meta) {
                                    if(!row.createTime){
                                        return '-'
                                    }
                                    var date=row.createTime.substring(0,10)
                                    var time=row.createTime.substring(11)
                                    return date+'<br>'+time
                                },
                                "targets" : 3,"class":"text-l"
                            }, {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    if (row.status == '1') {
                                        dom += '<a onClick="item_stop(\''
                                            + row.eyId
                                            + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                    } else if (row.status == '2') {
                                        dom += '<a onClick="item_start(\''
                                            + row.eyId
                                            + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                    }
                                    dom += ' &nbsp;';
                                    dom += '<a title="编辑" href="javascript:void(0)" onclick="charges_edit(\''
                                        + row.eyId
                                        + '\')" class="ml-5" style="text-decoration:none">';
                                    dom += '<i class="iconfont icon-edit"></i></a>';
                                    dom += ' &nbsp;';
                                    dom += '<a title="删除" href="javascript:;" onclick="item_del(this,\''
                                        + row.eyId
                                        + '\')" class="ml-5" style="text-decoration: none">';
                                    dom += '<i class="iconfont icon-shanchu"></i></a>';
                                    return dom;
                                },
                                "targets": 5
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
                                "targets": 4
                            }]
                    });
        });

        function charges_add() {
            var url = '/examYear/toAdd.do';
            layer_show('添加考试年度', url, 900, 600, function () {
                myDataTable.fnDraw(true);
            },true);

        }

        function charges_edit(eyId) {
            var url = '/examYear/toUpdate.do' + '?eyId='
                + eyId;
            layer_show('修改考试年度', url, null, 510, function () {
                myDataTable.fnDraw(false);
            },true);
        }

        /*管理员-删除*/
        function item_del(obj, id) {
            layer.confirm('确认要删除吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/examYear/delete.do',
                    data: {
                        eyId: id
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

        function item_stop(eyId) {
            layer.confirm('确认要停用吗？', function (index) {
                //此处请求后台程序，下方是成功后的前台处理……
                $.ajax({
                    type: 'POST',
                    url: '/examYear/block.do',
                    data: {
                        eyId: eyId
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

        function item_start(eyId) {
            layer.confirm('确认要启用吗？', function (index) {
                //此处请求后台程序，下方是成功后的前台处理……
                $.ajax({
                    type: 'POST',
                    url: '/examYear/start.do',
                    data: {
                        eyId: eyId
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
            var $input = $("input[name=eyIds]:checked");

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
                    url: '/examYear/deletes.do',
                    data: {
                        eyIds: chk_value
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