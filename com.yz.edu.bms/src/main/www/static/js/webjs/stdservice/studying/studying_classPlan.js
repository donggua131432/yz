var myDataTable;
$(function() {
 	
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/studying/showClassPlanData.do",
					data: function (pageData) {
	                      return search_data(pageData);
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
					"mData" : null
				}, {
					"mData" : null
				},{
					"mData" : "address"
				}, {
					"mData" : "stdtCount"
				}],
				"columnDefs" : [
			            {"render" : function(data, type, row, meta) {
							return row.courseId;
						   },
						"targets" : 0
						},
						{"render" : function(data, type, row, meta) {
								return row.courseName;
						   },
						   "targets" : 1
						},
						
						{"render" : function(data, type, row, meta) {
							
							return '<a class="stuName" onclick="show_Pfsn(\''+ row.courseId +'\')">'+ row.stdtCount||'0' +'</a>';
						   },
						   "targets" : 3
					     }
				  ]
			});

	});
	
	function _search() {
	    myDataTable.fnDraw(true);
	}
	 function search_data(pageData) {
         pageData = $.extend({},{start:pageData.start, length:pageData.length},$("#searchForm").serializeObject());
         return pageData;
     }

	//查看上课专业
	function show_Pfsn(courseId) {
	    var url = '/studying/showClassPlanPfsn.do'+ '?courseId=' + courseId;
	    layer_show('查看上课专业', url, null, null, function() {
	
	    },true);
	}
