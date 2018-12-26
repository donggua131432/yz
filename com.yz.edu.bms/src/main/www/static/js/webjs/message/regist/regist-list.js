 var myDataTable;
        $(function () {

        	_init_select("mtpType", dictJson.mtpType, null);
        	_init_select("mtpStatus", dictJson.mtpStatus, null);
        	_init_select("msgChannel", dictJson.msgChannel, null);
            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/msgPub/list.do",
                            type: "post",
                            data: {
                                "createUserName": function () {
                                    return $("#createUser")
                                        .val();
                                },
                                "mtpType": function () {
                                    return $("#mtpType")
                                        .val();
                                },
                                "mtpStatus": function () {
                                    return $("#mtpStatus")
                                        .val();
                                },
                                "mspChannel": function () {
                                    return $("#mspChannel")
                                        .val();
                                },
                                "status": function () {
                                    return $("#status")
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
                                "render": function (data, type,
                                                    row, meta) {
                                    return '<input type="checkbox" value="' + row.mtpId + '" name="mtpIds"/>';
                                },
                                "targets": 0
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    var status = row.mtpStatus;
                                    if('1' == status || '5' == status){
	                                    dom += '<a title="编辑" href="javascript:void(0)" onclick="charges_edit(\''
	                                        + row.mtpId
	                                        + '\')" class="ml-5" style="text-decoration:none">';
	                                    dom += '<i class="iconfont icon-edit"></i></a>';
	                                    dom += ' &nbsp;';
                                    }
                                    dom += '<a title="删除" href="javascript:;" onclick="item_del(this,\''
                                        + row.mtpId
                                        + '\')" class="ml-5" style="text-decoration: none">';
                                    dom += '<i class="iconfont icon-shanchu"></i></a>';
                                    return dom;
                                },
                                "targets": 8
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var status = row.mtpStatus;
                                    if('1' == status){
                                     	return '<a class="tableBtn normal" onclick="toImport(\''+ row.mtpId + '\',\'IMPORT\')">配置</a>';
                                    }else{
                                    	return '<a class="tableBtn blue" onclick="toImport(\''+ row.mtpId + '\',\'CHECK\')">查看</a>';
                                    }
                                },
                                "targets": 7
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var status = row.mtpStatus;
                                	var dom = '';
                                    if('1' == status){
	                                    dom += '<a class="tableBtn normal" onclick="item_sub(this,\''+ row.mtpId + '\')">'+_findDict("mtpStatus",row.mtpStatus)+'</a>';
                                    } else if ('5' == status){
                                    	dom += _findDict("mtpStatus",row.mtpStatus);
                                    	if(row.remark){
	                                    	dom += '<br>原因：' + row.remark;
                                    	}
                                    } else {
                                    	dom += _findDict("mtpStatus",row.mtpStatus);
                                    }
                                    return dom;
                                },
                                "targets": 6
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    dom += row.createUser + '<br>';
                                    dom += row.createTime;
                                    return dom;
                                },
                                "targets": 5,"class":"text-l"
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                	dom += '发送人：' + row.sender + '<br>';
                                	dom += '计划发送时间：' + row.sendTime;
                                    return dom;
                                },
                                "targets": 4,"class":"text-l"
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict("msgChannel",row.msgChannel);
                                },
                                "targets": 3
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    
                                    dom += '消息标题：' + row.msgTitle + '<br>';
                                    dom += '消息名称：' + row.msgName + '<br>';
                                    dom += '消息编码：' + row.msgCode + '<br>';
                                    dom += '消息详情：' + row.msgContent;
                                    
                                    return dom;
                                },
                                "targets": 2,"class":"text-l"
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    
                                    return _findDict("mtpType",row.mtpType);
                                },
                                "targets": 1
                            }]
                    });
        });
        
        function toImport(mtpId,exType){
        	var url = '/msgPub/toStdList.do' + '?mtpId='
           		 + mtpId + "&exType=" + exType;
	        layer_show('配置学员', url, null, 510, function () {
	            myDataTable.fnDraw(false);
	        },true);
        }

        function charges_add() {
            var url = '/msgPub/toAdd.do';
            layer_show('添加消息', url, 900, 600, function () {
                myDataTable.fnDraw(true);
            });

        }

        function charges_edit(mtpId) {
            var url = '/msgPub/toEdit.do' + '?mtpId='
                + mtpId;
            layer_show('修改消息', url, null, 510, function () {
                myDataTable.fnDraw(false);
            });
        }
        
        function item_del(obj, id){
        	 layer.confirm('确认删除吗？', function (index) {
                 $.ajax({
                     type: 'POST',
                     url: '/msgPub/delete.do',
                     data: {
                         mtpId: id
                     },
                     dataType: 'json',
                     success: function (data) {
                         if (data.code == _GLOBAL_SUCCESS) {
                             layer.msg('删除成功!', {
                                 icon: 1,
                                 time: 1000
                             });
                         }
                         myDataTable.fnDraw(false);
                     }
                 });
             });
        }

        /*管理员-删除*/
        function item_sub(obj, id) {
            layer.confirm('确认提交吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/msgPub/submit.do',
                    data: {
                    	mtpId: id
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('提交成功!', {
                                icon: 1,
                                time: 1000
                            });
                        }
                        myDataTable.fnDraw(false);
                    }
                });
            });
        }

        function searchItem() {
            myDataTable.fnDraw(true);
        }

        function submits() {
            var chk_value = [];
            var $input = $("input[name=mtpIds]:checked");

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
            layer.confirm('确认提交吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/msgPub/submits.do',
                    data: {
                        mtpIds: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('提交成功!', {
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