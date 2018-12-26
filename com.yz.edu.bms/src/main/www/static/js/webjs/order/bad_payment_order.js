var myDataTable;
$(function() {
	myDataTable = $('.table-sort').dataTable({
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/bad_payment_order/getList.do",
			type : "post",
			data : function(pageData) {
				pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
				return pageData;
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
		columns : [
			{"mData" : null},
			{"mData" : "payNo"},
			{"mData" : "transNo"},
			{"mData" : "amount"},
			{"mData" : null},
			{"mData" : "dealType"},
			{"mData" : "errorMsg"},
			{"mData" : "executeCount"},
			{"mData" : "lastExecuteDate"}
		],
		"columnDefs" : [
			{ "targets" : 0,"render" : function(data, type, row, meta) {
				return '<input type="checkbox" value="'+ row.id + '" name="Ids"/>';
			}},
			{"targets" : 4,"class":"text-l","render" : function(data, type, row, meta) {
				 for (var i = 0; i < dictJson.paymentType.length; i++) {
                    if (row.payType == dictJson.paymentType[i].dictValue)
                        return dictJson.paymentType[i].dictName;
                }
				return "";
			}}
		]
	});
});

function _search() {
	myDataTable.fnDraw(true);
}

//批量执行
function batchExecute() {
	var chk_value = [];
                $("input[name=Ids]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                if(chk_value.length==0)
                return;
	layer.confirm('您确定要批量执行吗？', {icon: 0, title:'温馨提示',btn: ['确定','取消'],offset: '36%'}, function(index){
		$.ajax({
                        type : 'POST',
                        url : '/bad_payment_order/batchExecute.do',
                        data : {
                            badPaymentIds : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            layer.msg('已执行!', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=Ids]").attr("checked", false);
                        },
                        error : function(data) {
                            layer.msg('执行失败！', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=Ids]").attr("checked", false);
                        },
                    });
                layer.close(index);
            });
}