   var myDataTable;
        $(function () {

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "processing": true,
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/monthExpense/infoList.do",
                            type: "post",
                            data: {
                               year : function(){
                            	   return $("#year").val();
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
                            "mData": "empName"
                        }, {
                            "mData": "year"
                        }, {
                            "mData": "month"
                        }, {
                            "mData": "amount"
                        }, {
                            "mData": "createTime"
                        }, {
                            "mData": "remark"
                        }],
                        "columnDefs": [
                           ]
                    });
        });

       