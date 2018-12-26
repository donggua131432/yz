var myDataTable;

		$(function() {

			myDataTable = $('.table-sort').dataTable({
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/invite_user/getAccount.do',
					type : "post",
					dataType : 'json',
					data : {
						userId : varUserId
					}
				},
				"pageLength" : 10,
				"pagingType" : "full_numbers",
				"ordering" : false,
				"searching" : false,
				"createdRow" : function(row, data, dataIndex) {
					$(row).addClass('text-c');
				},
				"language" : _my_datatables_language,
				columns : [ {
					"mData" : "accSerialNo"
				}, {
					"mData" : "accId"
				}, {
					"mData" : null
				}, {
					"mData" : "amount"
				}, {
					"mData" : "beforeAmount"
				}, {
					"mData" : "afterAmount"
				}, {
					"mData" : null
				}, {
					"mData" : "excDesc"
				}, {
					"mData" : "updateTime"
				} ],
				"columnDefs" : [ {
					"render" : function(data, type, row, meta) {

						var dom = '';
						var action = row.action;
						if ('1' == action) {
							dom += '进账';
						} else if ('2' == action) {
							dom += '出账';
						}
						return dom;
					},
					"targets" : 2
				}, {
					"render" : function(data, type, row, meta) {

						var dom = '';
						var accSerialStatus = row.accSerialStatus;
						if ('1' == accSerialStatus) {
							dom += '处理中';
						} else if ('2' == accSerialStatus) {
							dom += '成功';
						} else if ('3' == accSerialStatus) {
							dom += '失败';
						}
						return dom;
					},
					"targets" : 6
				} ]
			});
		});