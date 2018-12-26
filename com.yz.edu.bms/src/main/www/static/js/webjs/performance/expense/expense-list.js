 var myDataTable;
        $(function () {

            $(".select").select2({
                placeholder: "--请选择--",
                allowClear: true
            });
            
            _init_select('empStatus',dictJson.empStatus);
            _init_campus_select("recruitCampus", "recruitDepartment", null, '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "processing": true,
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/monthExpense/list.do",
                            type: "post",
                            data: {
                                "recruitName": function () {
                                    return $("#recruitName").val() ? $("#recruitName").val() : '';
                                },
                                "empStatus": function () {
                                    return $("#empStatus").val() ? $("#empStatus").val() : '';
                                },
                                "recruitCampus": function () {
                                    return $("#recruitCampus").val() ? $("#recruitCampus").val() : '';
                                },
                                "recruitDepartment": function () {
                                    return $("#recruitDepartment").val() ? $("#recruitDepartment").val() : '';
                                },
                                "year": function () {
                                    return $("#year").val();
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
                            "mData": "empName"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": "dpName"
                        }, {
                            "mData": "campusName"
                        }, {
                            "mData": "year"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": "balance"
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var dom = '';
                                	if(row.jtIds){
                                		for (var i = 0; i < row.jtIds.length; i++) {
											var jtId = row.jtIds[i];
	                                		dom += _findDict("jtId",jtId) + ','; 
										}
                                	}
                                	if(dom){
                                		dom = dom.substring(0, dom.length - 1);
                                	}
                                    return dom;
                                },
                                "targets": 1
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict("empType",row.empType);
                                },
                                "targets": 2
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	if(row.empStatus == '1' || row.empStatus == '3'){
                                		return '是';
                                	} else {
                                		return '否';
                                	}
                                },
                                "targets": 3
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var dom = '';
                                	dom += '<a title="查看详情" onclick="toMyPerformance(\''+ row.empId +'\')"><p style="margin-bottom: 0;" class="c-blue">'+row.estimate+'</p></a>';
                                	return dom;
                                },
                                "targets": 7
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var dom = '';
                                	dom += '<a title="查看详情" onclick="toExpenseInfo(\''+ row.empId +'\',\''+ row.year +'\',\''+ row.expenseId +'\')"><p style="margin-bottom: 0;" class="c-blue">'+row.rendered+'</p></a>';
                                	return dom;
                                },
                                "targets": 8
                            }]
                    });
        });

        function charges_add() {
            var url = '/monthExpense/toInsert.do';
            layer_show('添加月度报销', url, 700, 450, function () {
                myDataTable.fnDraw(true);
            });
        }
        
        function toMyPerformance(empId){
        	var url = '/performance/toList.do' + '?empId=' + empId;
			layer_show('我的绩效', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
        }
        
        function toExpenseInfo(empId,year,expenseId){
        	var url = '/monthExpense/toInfoList.do' + '?empId=' + empId + '&year=' + year + '&expenseId=' + expenseId;
			layer_show('我的绩效', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
        }
        
        function searchItem(){
        	myDataTable.fnDraw(true);
        }
        