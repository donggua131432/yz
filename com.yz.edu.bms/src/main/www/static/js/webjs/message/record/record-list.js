var myDataTable;
        $(function () {

        	_init_select("mtpType", dictJson.mtpType, null);
        	_init_select("mtpStatus", dictJson.mtpStatus, null);
        	_init_select("msgChannel", dictJson.msgChannel, null);
        	$('select').select2({
	            placeholder: "--请选择--",
	            allowClear: true,
	            width: "59%"
	        });
            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/msgPub/recordList.do",
                            type: "post",
                            data: {
                                "receiver": function () {
                                    return $("#receiver")
                                        .val();
                                },
                                "sendStatus": function () {
                                    return $("#sendStatus")
                                        .val();
                                },
                                "mspChannel": function () {
                                    return $("#mspChannel")
                                        .val();
                                },
                                "startTime": function () {
                                    return $("#startTime")
                                        .val();
                                },
                                "endTime": function () {
                                    return $("#endTime")
                                        .val();
                                },
                                "tutorName" : function (){
                                	return $("#tutorName").val();
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
                            "mData": "operator"
                        }, {
                            "mData": null
                        }, {
                            "mData": "receiver"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": "sendTime"
                        }, {
                            "mData": "msgTitle"
                        }, {
                            "mData": "msgContent"
                        }, {
                            "mData": "tutorName"
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
                                    var msgChannel = row.msgChannel;
                                    if('1' == msgChannel){
                                    	dom += row.mobile;
                                    } else if ('2' == msgChannel){
                                    	dom += row.openId;
                                    } else if ('3' == msgChannel){
                                    	dom += row.dingding;
                                    } else if ('4' == msgChannel){
                                    	dom += row.mail;
                                    }
                                    return dom;
                                },
                                "targets": 2
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict("msgChannel",row.msgChannel);
                                },
                                "targets": 5
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var status = row.sendStatus;
                                    if('1' == status){
	                                    return '成功';
                                    }else{
                                    	return "失败";
                                    }
                                },
                                "targets": 6
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return "";
                                },
                                "targets": 4
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var dom = '';
                                	if(row.mtpId){

                                		 dom += '<a title="查看" href="javascript:void(0)" onclick="charges_view(\''
                                             + row.mtpId
                                             + '\')" class="ml-5" style="text-decoration:none">';
                                         dom += '<i class="iconfont icon-chakan"></i></a>';

                                	}
                                    return dom;
                                },
                                "targets": 11
                            }]
                    });
        });
        
        function toImport(mtpId,exType){
        	var url = '/msgPub/toStdList.do' + '?mtpId='
           		 + mtpId + "&exType=" + exType;
	        layer_show('配置学员', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        },true);
        }

        function charges_add() {
            var url = '/msgPub/toAdd.do';
            layer_show('添加消息', url, 900, 600, function () {
                myDataTable.fnDraw(true);
            });

        }

        function charges_view(mtpId){
        	var url = '/msgPub/toMsgView.do' + '?mtpId='
      		 + mtpId;
            layer_show('查看消息', url, 900, 600, function () {
                myDataTable.fnDraw(false);
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
        
        function exportFailedMsg(){
        	$("#export-form").submit();
        }