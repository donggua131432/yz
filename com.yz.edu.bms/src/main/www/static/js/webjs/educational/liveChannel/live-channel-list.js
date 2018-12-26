var myDataTable;
    $(function () {

        //初始化年度下拉框
        _init_select("year", dictJson.year);

        //初始化课程类型
        _init_select("courseType", dictJson.courseType);

        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/live/findAllLiveChannel.do",
                type: "post",
                data: {
                    "channelId": function () {
                        return $("#channelId").val();
                    },
                    "channelName": function () {
                        return $("#channelName").val();
                    },
                    "courseName": function () {
                        return $("#courseName").val();
                    },
                    "year": function () {
                        return $("#year").val();
                    },
                    "courseType": function () {
                        return $("#courseType").val();
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
            columns: [
                {
                    "mData": null
                },
                {
                    "mData": "channelId"
                },
                {
                    "mData": "channelPassword"
                },
                {
                    "mData": "channelName"
                },
                {
                    "mData": "courseId"
                },
                {
                    "mData": "courseName"
                },
                {
                    "mData": "year"
                },
                {
                    "mData": null
                },
                {
                    "mData": "empName"
                },
                {
                    "mData": null
                }],
            "columnDefs": [
                {
                    "render": function (data, type,
                                        row, meta) {
                        return '<input type="checkbox" value="' + row.lcId + '" name="lcIds"/>';
                    },
                    "targets": 0
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                        return _findDict('courseType', row.courseType);
                    },
                    "targets": 7
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                        var dom = '';
                        dom += '<a title="编辑" href="javascript:void(0)" onclick="liveChannelEdit(\''
                            + row.lcId
                            + '\')" class="ml-5" style="text-decoration:none">';
                        dom += '<i class="iconfont icon-edit"></i></a>';
                        return dom;
                    },
                    "targets": 9
                }
            ]
        });

    });

    /*直播频道-编辑*/
    function liveChannelEdit(lcId) {
        var url = '/live/toEdit.do' + '?lcId=' + lcId;
        layer_show('直播频道编辑', url, null, 400, function () {
            myDataTable.fnDraw(false);
        });
    }

    /*频道生成*/
    function gen() {
        var url = '/live/toGen.do';
        layer_show('频道生成', url, null, 400, function () {
            myDataTable.fnDraw(false);
        });
    }

    /*导出上课记录*/
    function recordExport() {
        var url = '/live/toRecordExport.do';
        layer_show('导出上课记录', url, null, 400, function () {
        });
    }

    /* 直播频道新增*/
    function add() {
        var url = '/live/toAdd.do';
        layer_show('直播频道新增', url, null, 400, function () {
            myDataTable.fnDraw(false);
        });
    }

    //批量删除
    function delAll() {
        var chk_value = [];
        $("input[name=lcIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/live/deletes.do',
                data: {
                    lcIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已删除!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                    }
                }
            });
        });
    }

    function _search() {
        myDataTable.fnDraw(true);
    }