var myDataTable;
    $(function () {
    	
    	$('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
    	//初始角色下拉框
     	_simple_ajax_select({
    			selectId : "roleId",
    			searchUrl : '/auth/selectList.do',
    			sData : {},
    			showText : function(item) {
    				return item.roleName;
    			},					
    			showId : function(item) {
    				return item.roleId;
    			},
    			placeholder : '--请选择角色--'
    	});	
    	
     	$("#roleId").append(new Option("", "", false, true));
     	
    
    	
        myDataTable = $('.table-sort').dataTable(
            {
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/user/userList.do",
                    type: "post",
                    dataType: 'json',
                    data: {
                        realName: function () {
                            return $("#realName").val() ? $("#realName").val() : '';
                        },
                        userName: function () {
                            return $("#userName").val() ? $("#userName").val() : '';
                        },
                        isBlock: function () {
                            return $("#isBlock").val() ? $("#isBlock").val() : '';
                        },
                        roleId: function () {
                            return $("#roleId").val() ? $("#roleId").val() : '';
                        },
                        isStaff : function (){
                        	 return $("#isStaff").val() ? $("#isStaff").val() : '';
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
                    "mData": "userId"
                }, {
                    "mData": "realName"
                }, {
                    "mData": "userName"
                }, {
                    "mData": "description"
                }, {
                    "mData": "isStaff"
                }, {
                    "mData": "isBlock"
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            if (row.isBlock == '0') {
                                dom += '<a onClick="admin_stop(this,\'' + row.userId + '\')" href="javascript:;" title="停用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                            } else if (row.isBlock == '1') {
                                dom += '<a onClick="admin_start(this,\'' + row.userId + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                            }
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="编辑" href="javascript:void(0)" onclick="admin_edit(\'' + row.userId + '\')" class="ml-5" style="text-decoration:none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a style="text-decoration:none" class="ml-5" onClick="changePassword(\'' + row.userId + '\')" href="javascript:;" title="修改密码"><i class="Hui-iconfont f-18">&#xe63f;</i></a>';
                            dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="admin_del(this,\'' + row.userId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>';

                            return dom;
                        },
                        "targets": 6
                    }, {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            if (row.isBlock == '0') {
                                dom = '<span class="label label-success radius">已启用</span>';
                            } else if (row.isBlock == '1') {
                                dom = '<span class="label label-default radius">已禁用</span>';
                            }
                            return dom;
                        },
                        "targets": 5
                    }, {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            if (row.isStaff == '0') {
                                dom = '否';
                            } else if (row.isStaff == '1') {
                                dom = '是';
                            }
                            return dom;
                        },
                        "targets": 4
                    }, {
                        "render": function (data, type, row, meta) {
                            var dom = '';
                            for (var i = 0; i < row.roles.length; i++) {
                                var role = row.roles[i];
                                if (i == (row.roles.length - 1)) {
                                    dom += role.roleName + '';
                                    break;
                                }
                                dom += role.roleName + ',';

                            }
                            if (row.roles.length < 1) {
                                dom = '无';
                            }

                            return dom;
                        },
                        "targets": 3
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
    /*管理员-增加*/
    function admin_add() {
        var url = '/user/userEdit.do' + '?exType=ADD';
        layer_show('添加用户', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }
    /*管理员-删除*/
    function admin_del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/user/userDelete.do',
                data: {
                    userId: id
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

    /*管理员-编辑*/
    function admin_edit(userId) {
        var url = '/user/userEdit.do' + '?userId=' + userId + '&exType=UPDATE';
        layer_show('修改用户', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }
    /*管理员-停用*/
    function admin_stop(obj, id) {
        layer.confirm('确认要停用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/user/userBlock.do',
                data: {
                    userId: id,
                    exType: 'BLOCK'
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('已停用!', {icon: 5, time: 1000});
                    }
                }
            });
        });
    }

    /*管理员-启用*/
    function admin_start(obj, id) {
        layer.confirm('确认要启用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/user/userBlock.do',
                data: {
                    userId: id,
                    exType: 'START'
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('已启用!', {icon: 6, time: 1000});
                    }
                }
            });


        });

    }
    function searchAdmin() {
        myDataTable.fnDraw(true);
    }

    /*密码-修改*/
    function changePassword(id) {
        var url = '/user/toEditPwd.do' + '?userId=' + id;
        layer_show('修改密码', url, 600, 240, function () {
            myDataTable.fnDraw(false);
        });
    }