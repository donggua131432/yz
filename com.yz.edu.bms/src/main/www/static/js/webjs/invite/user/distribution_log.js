var myDataTable;

		$(function() {
			myDataTable = $('.table-sort').dataTable({
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/invite_user/getLogs.do",
					type : "post",
					data : {
						'userId' :varUserId
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
					{"mData" : "empChange"},
					{"mData" : "dpChange"},
					{"mData" : "campusChange"},
					{"mData" : null},
					{"mData" : "drName"},
					{"mData" : "createUser"},
					{"mData" : "createTimeStr"}
				],
				"columnDefs" : [
					{"targets" : 3,"render" : function(data, type, row, meta) {
							var i = _findDict('drType', row.drType);
							return i ? i : '无';
					}}
				]
			});
		});