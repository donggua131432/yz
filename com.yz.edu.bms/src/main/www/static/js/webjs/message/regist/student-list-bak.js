var myDataTable;
        $(function () {
        	if(exType == 'CHECK'){
        		$("#importDiv").html("");
        	}

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: '/msgPub/stdList.do',
                            type: "post",
                            data: {
                            	mtpId : function (){
                            		return $("#mtpId").val();
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
                            "mData": "stdName"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var dom = '';
                                	
                                	dom += "["+_findDict("recruitType",row.recruitType)+"]";
                                	dom += row.unvsName + ":";
                                	dom += "(" + row.pfsnCode + ")";
                                	dom += row.pfsnName;
                                	dom += "[" + _findDict("pfsnLevel",row.pfsnLevel) + "]";
                                	dom += "(" + _findDict("grade",row.grade) + ")";
                                    return dom;
                                },
                                "targets": 1
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict("stdStage",row.stdStage);
                                },
                                "targets": 2
                            }]
                    });
        });
        
         function toImport(){
         	var mtpId = $("#mtpId").val();
         	var url = '/msgPub/toImport.do' + '?mtpId='
            		 + mtpId;
 	        layer_show('配置学员', url, null, 510, function () {
 	            myDataTable.fnDraw(true);
 	        });
         }