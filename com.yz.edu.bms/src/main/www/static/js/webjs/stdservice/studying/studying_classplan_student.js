var myDataTable;
$(function() {
 	
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/studying/showClassPlanStudentData.do",
					data:{
						
						"pfsnId" : function() {
							return $("#pfsnId").val();
						},
						"unvsId" : function() {
							return $("#unvsId").val();
						},
						"grade" : function() {
							return $("#grade").val();
						}
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
					"mData" : "tutorName"
				}, {
					"mData" : "stdName"
				}, {
					"mData" : "idCard"
				}],
				"columnDefs" : [
			            {"render" : function(data, type, row, meta) {
			            	return _findDict('grade', row.grade);
						    },
						"targets" : 0
						},
						{"render" : function(data, type, row, meta) {
							
							var pfsnName = row.pfsnName;
			                var unvsName = row.unvsName;
			                var recruitType = row.recruitType;
			                var pfsnCode = row.pfsnCode;
			                var pfsnLevel = row.pfsnLevel;
			                var text = '';
			                if(recruitType) {
			                    if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
			                        text += "[成教]";
			                    }else {
			                        text += "[国开]";
			                    }
			                }
			                if(unvsName) {
			                    text += unvsName + "</br>";
			                }

			                if(pfsnLevel) {
			                    if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
			                        text += "[专科]";
			                    }else {
			                        text += "[本科]";
			                    }
			                }
			                if(pfsnName) {
			                    text += pfsnName;
			                }
			                if(pfsnCode) {
			                    text += "(" + pfsnCode + ")";
			                }
			               return text;
						   },
						   "targets" : 1
						},
						
						{"render" : function(data, type, row, meta) {
							return row.tutorName;
						   },
						   "targets" : 2
					     }
				  ]
			});

	});