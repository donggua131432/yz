	var myDataTable;
    $(function () {

        //初始辅导书发放状态
        _init_select("kpiYear", [{
            "dictValue": "2018",
            "dictName": "2018年"
        }]);

        _init_select("empStatus", dictJson.empStatus);

        //校区-部门-组 联动
        _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');

        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/yearkpi/findAllYearKPI.do",
                type: "post",
                data: {
                    "empName": function () {
                        return $("#empName").val();
                    },
                    "kpiYear": function () {
                        return $("#kpiYear").val();
                    },
                    "empStatus": function () {
                        return $("#empStatus").val();
                    },
                    "campusId": function () {
                        return $("#campusId").val();
                    },
                    "dpId": function () {
                        return $("#dpId").val();
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
                "mData": null
            }, {
                "mData": "campusName"
            }, {
                "mData": "dpName"
            }, {
                "mData": "empName"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": "taskCount"
            }, {
                "mData": "singleRealKPIValue"
            }, {
                "mData": "zcSum"
            }, {
                "mData": "KPIValue"
            }, {
                "mData": "CostSum"
            }, {
                "mData": "finalKPIValue"
            }, {
                "mData": null
            }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        return  "2018年";
                    },
                    "targets": 0
                },
                {
                    "render": function (data, type, row, meta) {
                        return  _findDict("jtId", row.jtId)
                    },
                    "targets": 4
                },
                {
                    "render": function (data, type, row, meta) {
                        return  _findDict("empType", row.empType)
                    },
                    "targets": 5
                },
                {
                    "render": function (data, type, row, meta) {
                        return  _findDict("empStatus", row.empStatus)
                    },
                    "targets": 6
                },
                {
                    "render": function (data, type, row, meta) {
                        var dom = '';
                        dom += '<a title="详情" href="javascript:void(0)" onclick="yearKPIDetail(\''
                            + encodeURIComponent(JSON.stringify(row))
                            + '\')" class="ml-5" style="text-decoration:none">';
                        dom += '<i class="iconfont icon-edit"></i></a>';
                        return dom;
                    },
                    "targets": 13
                }
            ]
        });

    });
    
    function yearKPIDetail(row) {
        $('#row').val(decodeURIComponent(row));
        var url = '/yearkpi/detail.do';
        layer_show('详情', url, null, null, function () {

        },true);
    }

    function _search() {
        myDataTable.fnDraw(true);
    }