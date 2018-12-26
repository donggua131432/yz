 		var myDataTable;
        $(function () {

            $("#status").select2({
                placeholder: "--请选择--",
                allowClear: true
            });

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "processing": true,
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/performance/getStudents.do",
                            type: "post",
                            data: {
                            	month : function(){
                            		return $("#month").val();	
                            	},
                            	empId : function(){
                            		return $("#empId").val();	
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
                            "mData": "idCard"
                        }, {
                            "mData": "grade"
                        }, {
                            "mData": "unvsName"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": "taName"
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                	var dom = '';
                                	dom += '[' + _findDict('pfsnLevel',row.pfsnLevel) + ']' + row.pfsnName + '(' + row.pfsnCode + ')';
                                    return dom;
                                },
                                "targets": 4
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                   	dom += _findDict('grade',row.grade);
                                    return dom;
                                },
                                "targets": 2
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict('stdStage',row.stdStage);
                                },
                                "targets": 5
                            }]
                    });
        });
