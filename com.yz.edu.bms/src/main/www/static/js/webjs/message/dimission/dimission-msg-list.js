 var myDataTable;
        $(function () {
        	
        	

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/dimissionMsg/dimissionMsgList.do",
                            type: "post",
                            data: {
                                "createUser": function () {
                                    return $("#createUser")
                                        .val();
                                },
                                "msgType": function () {
                                    return $("#msgType")
                                        .val();
                                },
                                "empName": function () {
                                    return $("#empName")
                                        .val();
                                },
                                "stdName": function () {
                                    return $("#stdName")
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
                            "mData": "stdName"
                        }, {
                            "mData": "empName"
                        }, {
                            "mData": "createUser"
                        }, {
                            "mData": "sendTime"
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var msgType = row.msgType;
                                	var dom = '';
                                	if(msgType == '1'){
                                		dom = '微信';
                                	} else if (msgType == '2'){
                                		dom = '短信';
                                	}
                                    return dom;
                                },
                                "targets": 0
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    dom += row.mobile;
                                    dom += '/' + row.idCard;
                                    return dom;
                                },
                                "targets": 1
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var status = row.status;
                                    if('1' == status){
                                     	return '发送成功';
                                    }else{
                                    	return '发送失败';
                                    }
                                },
                                "targets": 2
                            }]
                    });
            
            $("select").select2({
                placeholder: "--请选择--",
                allowClear: true
            });
        });
        

        function charges_add() {
            var url = '/dimissionMsg/toSendMsg.do';
            layer_show('离职通知', url, 900, 600, function () {
                myDataTable.fnDraw(true);
            });

        }

        function searchItem() {
            myDataTable.fnDraw(true);
        }
