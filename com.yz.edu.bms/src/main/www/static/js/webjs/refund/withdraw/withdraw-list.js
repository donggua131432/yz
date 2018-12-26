var myDataTable;
$(function () {

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
                    url: "/withdraw/list.do",
                    type: "post",
                    data: {
                        "stdName": function () {
                            return $("#stdName").val();
                        },
                        "checkStatus": function () {
                            return $("#checkStatus").val();
                        },
                        "mobile": function () {
                            return $("#mobile").val();
                        },
                        "idCard": function () {
                            return $("#idCard").val();
                        },
                        "beginTime" : function (){
                        	return $("#beginTime").val();
                        },
                        "endTime" : function (){
                        	return $("#endTime").val();
                        },
                        "operBeginTime" : function (){
                        	return $("#operBeginTime").val();
                        },
                        "operEndTime" : function (){
                        	return $("#operEndTime").val();
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
                    "mData": "stdName"
                }, {
                    "mData": null
                }, {
                    "mData": "bankCard"
                }, {
                    "mData": null
                }, {
                    "mData": "mobile"
                }, {
                    "mData": "idCard"
                }, {
                    "mData": "amount"
                }, {
                    "mData": null
                }, {
                    "mData": null
                }, {
                    "mData": "empName"
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
                            return '<input type="checkbox" value="' + row.withdrawId + '" name="withdrawIds"/>';
                        },
                        "targets": 0
                    },
                    {
                        "render": function (data, type,
                                            row, meta) {
                            return _findDict("sex", row.sex);
                        },
                        "targets": 2,"class":"text-l"
                    },{
                        "targets": 3,"class":"text-l"
                    }, {
                        "render": function (data, type, row, meta) {
                            return _findDict("bankType", row.bankType);
                        },
                        "targets": 4,"class":"text-l"
                    },{
                        "targets": 5,"class":"text-l"
                    },{
                        "targets": 6,"class":"text-l"
                    },{
                        "render" : function(data, type, row, meta) {
                            var dateTime= row.applyTime;
                            if(!dateTime){
                                return '-'
                            }
                            var date=dateTime.substring(0,10)
                            var time=dateTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 8,"class":"text-l no-warp"
                    }, {
                        "render": function (data, type,
                                            row, meta) {
                        	var dom='';
                        	if('2' == row.checkStatus){
                        		dom +='待打款';
                        	}else if('3' == row.checkStatus){
                        		dom +='已打款';
                        	}else if('4' == row.checkStatus){
                        		dom +='已驳回';
                        	}
                            return dom;
                        },
                        "targets": 9
                    },
                    {
                        "render": function (data, type,row, meta) {
                        	if(row && 'null' !=row.empName && '' != row.empName){
                                var dateTime= row.updateTime;
                                if(!dateTime){
                                    return '-'
                                }
                                var date=dateTime.substring(0,10)
                                var time=dateTime.substring(11)
                                return date+'<br>'+time
                        	}
                        },
                        "targets": 11,"class":"text-l no-warp"
                    },
                    {
                        "render": function (data, type,
                                            row, meta) {
                            var dom = '';
                            if (null != row.remark && '' != row.remark) {
                                dom = '<span>' + row.remark + '</span>';
                            }
                            return dom;
                        },
                        "targets": 12,"class":"text-l"
                    },
                    {
                        "render": function (data, type,
                                            row, meta) {
                            var dom = '';
                            if ('2' == row.checkStatus) {
                                dom += '<a title="编辑" href="javascript:void(0)" onclick="check(\'' + row.withdrawId + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="iconfont icon-edit"></i></a>';
                            }
                            return dom;
                        },
                        "targets": 13
                    }]
            });
});


function check(id) {
    var url = '/withdraw/toCheck.do' + '?withdrawId=' + id;
    layer_show('审核提现', url, null, 510, function () {
        myDataTable.fnDraw(false);
    });
}


function searchItem() {
    myDataTable.fnDraw(true);
}

function checks() {
    var chk_value = [];
    var $input = $("input[name=withdrawIds]:checked");

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
	var url = '/withdraw/toSelectChecks.do';
	layer_show('批量完成提现审批', url, 500, 275, function() {
		myDataTable.fnDraw(false);
	});
}

function exportWithdraw(){
	$("#export-form").submit();
}