$(function(){
	
	var cashDataTable;
	
	var detailUrl = "/stdFee/allSerials.do";
	
	cashDataTable = $('.table-sort-all').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : detailUrl,
					type : "post",
					dataType : 'json',
					data : {
						stdId : function(){
							return $("#stdId").val();
						},
						userId : function(){
							return $("#userId").val();
						}
					}
				},
				"pageLength" : 10,
				"pagingType" : "full_numbers",
				"ordering" : false,
				"searching" : false,
				"createdRow" : function(row, data,
						dataIndex) {
					$(row).addClass('text-c');
				},
				"language" : _my_datatables_language,
				columns : [ {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : "amount"
				}, {
					"mData" : "beforeAmount"
				}, {
					"mData" : "afterAmount"
				}, {
					"mData" : "updateTime"
				} ], 
				"columnDefs" : [
						{
							"render" : function(data, type,
									row, meta) {
								
								var dom = '';
								var action = row.action;
								if('1' == action){
									dom += '进账';
								}else if('2' == action){
									dom += '出账';
								}
								return dom;
							},
							"targets" : 1
						},
						{
							"render" : function(data, type,
									row, meta) {
								
								return _findDict("transType",row.transType);
							},
							"targets" : 0
						}]
			});
	
	
});