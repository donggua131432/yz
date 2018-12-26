 var myDataTable;
 var auditCount=0;
 var amountCount=0;
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
                            url: "/commission/getStudents.do",
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
                        "footerCallback": function ( row, data, start, end, display ) {
                            var api = this.api(), data;
                 
                            // Remove the formatting to get integer data for summation
                            var intVal = function ( i ) {
                                return typeof i === 'string' ?
                                    i.replace(/[\$,]/g, '')*1 :
                                    typeof i === 'number' ?
                                        i : 0;
                            };
                            
                            // Total over this page
                            spageTotal = api
                                .column( 10, { page: 'current'} )
                                .data()
                                .reduce( function (a, b) {
                                	console.log(a + '---' + typeof b);
                                    return intVal(a) + intVal(b);
                                }, 0 );
                 
                            // Update footer
                            $( api.column( 10 ).footer() ).html(
                                spageTotal
                            );
                 
                 
                            // Total over this page
                            pageTotal = api
                                .column( 8, { page: 'current'} )
                                .data()
                                .reduce( function (a, b) {
                                    return intVal(a) + intVal(b);
                                }, 0 );
                 
                            // Update footer
                            $( api.column( 8 ).footer() ).html(
                                pageTotal.toFixed(2)
                            );
                         
                           
                        },
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
                            "mData": null
                        }, {
                            "mData": "empName"
                        }, {
                            "mData": "audit"
                        }, {
                            "mData": "recruitAmount"
                        }, {
                            "mData": "deduct"
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
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict('scholarship',row.scholarship);
                                },
                                "targets": 6
                            }]
                    });
        });