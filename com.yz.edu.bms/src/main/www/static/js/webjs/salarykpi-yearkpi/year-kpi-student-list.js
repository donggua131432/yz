var myDataTable;
    $(function () {

        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/yearkpi/findAllStudentDetail.do",
                type: "post",
                data: {
                    "queryType": function () {
                        return queryType;
                    },
                    "recruit": function () {
                        return recruit;
                    },
                    "scholarship": function () {
                        return scholarship;
                    },
                    "inclusionStatus": function () {
                        return inclusionStatus;
                    }
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            columns: [{
                "mData": "stdName"
            }, {
                "mData": "idCard"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": "taName"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": "standardPerson"
            }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        return _findDict("grade", row.grade)
                    },
                    "targets": 2
                },
                {
                    "render": function (data, type, row, meta) {
                        var pfsnLevel = "";
                        if (row.pfsnLevel == '1') {
                            pfsnLevel = "1>专科升本科类";
                        } else if (row.pfsnLevel == '5') {
                            pfsnLevel = "5>高中起点高职高专";
                        }
                        var txt = ""
                        if (row.unvsName != null) {
                            txt += row.unvsName
                        }
                        if (row.pfsnName != null) {
                            txt += ":" + row.pfsnName;
                        }
                        if (pfsnLevel != "") {
                            txt += "[" + pfsnLevel + "]"
                        }
                        return txt;
                    },
                    "targets": 3
                },
                {
                    "render": function (data, type, row, meta) {
                        return _findDict("scholarship", row.scholarship)
                    },
                    "targets": 5
                },
                {
                    "render": function (data, type, row, meta) {
                        return _findDict("inclusionStatus", row.inclusionStatus)
                    },
                    "targets": 6
                }
            ]
        });

    });

    function _search() {
        myDataTable.fnDraw(true);
    }