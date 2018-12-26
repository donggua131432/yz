var myDataTable;
$(function() {
	_init_select('paymentStatus', dictJson.paymentStatus);
    myDataTable = $('.table-sort').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/product/getProductRecords.do",
            type : "post",
            data : function(pageData) {
                return search_data(pageData);
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
        columns : [ {
            "mData" : "recordsNo"
        }, {
            "mData" : null
        }, {
            "mData" : "amount"
        }, {
            "mData" : "zhimi"
        }, {
            "mData" : null
        }, {
            "mData" : "remark"
        }, {
            "mData" : "outSerialNo"
        }, {
            "mData" : "createTime"
        }, {
            "mData" : "completeTime"
        } ],
        "columnDefs" : [ {
            "render" : function(data, type, row, meta) {
                return user(row);
            },
            "targets" : 1,
            "class" : "text-l no-warp"
        },{
            "targets" : 5,
            "class" : "text-l"
        },{
            "targets" : 6,
            "class" : "text-l"
        },{
            "targets" : 7,
            "class" : "no-warp"
        },{
            "targets" : 8,
            "class" : "no-warp"
        },{
            "render" : function(data, type, row, meta) {
                return _findDict("paymentStatus", row.paymentStatus);
            },
            "targets" : 4
        } ]
    });
});

function _search() {
    myDataTable.fnDraw(true);
}

function search_data(pageData) {
    var data = {
   		mobile : $("#mobile").val(),
   		realName : $("#realName").val(),
   		nickname : $("#nickname").val(),
   		yzCode : $("#yzCode").val(),
   		productName : $("#productName").val(),
   		outSerialNo : $("#outSerialNo").val(),
   		paymentStatus : $("#paymentStatus").val(),
   		startTime : $("#startTime").val(),
   		endTime : $("#endTime").val(),
        start : pageData.start,
        length : pageData.length
    };

    return data;
}

function user(data) {
    var yzCode = data.yzCode ? data.yzCode : '';
    var name = data.realName ? data.realName : '';
    var mobile = data.mobile ? data.mobile : '';
    var idCard = data.idCard ? data.idCard : '';
    var nickname = data.nickname ? data.nickname : '';
    var dom = '<ul>';
    dom += '<li>　　昵称：' + nickname + '</li>';
    dom += '<li>远智编号：' + yzCode + '</li>';
    dom += '<li>真实姓名：' + name + '</li>';
    dom += '<li>　手机号：' + mobile + '</li>';
    dom += '<li>身份证号：' + idCard + '</li>';
    dom += '</ul>';
    
    return dom;
 }