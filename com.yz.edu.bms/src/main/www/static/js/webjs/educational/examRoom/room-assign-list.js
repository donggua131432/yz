var myDataTable;
    $(function () {

        //初始省、市、区
        _init_area_select("provinceCode", "cityCode", "districtCode","440000");

        $("#districtCode").append("<option value=''>请选择</option>");
        $("#eyId").append("<option value=''>请选择</option>");

        //初始考场
        _simple_ajax_select({
            selectId: "placeId",
            searchUrl: '/examRoomAssign/findAllKeyValue.do',
            sData: {
            },
            showText: function (item) {
                return item.ep_name;
            },
            showId: function (item) {
                return item.ep_id;
            },
            placeholder: '--请选择考场--'
        });
//        $("#placeId").append(new Option("", "", false, true));

        //初始是否启用
        _init_select("status", [{
            "dictValue": "1",
            "dictName": "启用"
        }, {
            "dictValue": "2",
            "dictName": "禁用"
        }]);

        //初始考试年度
        $.ajax({
            type: "POST",
            dataType : "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=',
            success: function(data){
                var eyJson = data.body;
                if(data.code=='00'){
                    _init_select("eyId",eyJson);
                }
            }
        });

        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/examRoomAssign/findAllExamRoomAssign.do",
                type: "post",
                data: {
                    "pyCode": function () {
                        return $("#pyCode").val();
                    },
                    "placeId": function () {
                        return $("#placeId").val();
                    },
                    "address": function () {
                        return $("#address").val();
                    },
                    "provinceCode": function () {
                        return $("#provinceCode").val();
                    },
                    "cityCode": function () {
                        return $("#cityCode").val();
                    },
                    "districtCode": function () {
                        return $("#districtCode").val();
                    },
                    "eyId": function () {
                        return $("#eyId").val();
                    },
                    "seats": function () {
                        return $("#seats").val();
                    },
                    "restSeats": function () {
                        return $("#restSeats").val();
                    },
                    "startTime": function () {
                        return $("#startTime").val();
                    },
                    "endTime": function () {
                        return $("#endTime").val();
                    },
                    "status": function () {
                        return $("#status").val();
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
                    "mData": "examYear"
                },
                {
                    "mData": "pyCode"
                },
                {
                    "mData": null
                },
                {
                    "mData": "epName"
                },
                {
                    "mData": null
                },
                {
                    "mData": "seats"
                },
                {
                    "mData": "restSeats"
                },
                {
                    "mData": null
                },
                {
                    "mData": "remark"
                },
                {
                    "mData": null
                }],
            "columnDefs": [
                {
                    "render": function (data, type,
                                        row, meta) {
                        return '<input type="checkbox" value="' + row.pyId + '" name="pyIds"/>';
                    },
                    "targets": 0
                },
                {
                    "render": function (data, type, row, meta) {
                        return row.provinceName + row.cityName + row.districtName + row.address;
                    },
                    "targets": 3
                },
                {
                    "render": function (data, type, row, meta) {
                        var dateTime=row.startTime.replace('AM', '上午').replace('PM', '下午') + "-" + row.endTime;
                        if(!dateTime){
                            return '-'
                        }
                        var date=dateTime.substring(0,10)
                        var time=dateTime.substring(11)
                        return date+'<br>'+time
                    },
                    "targets": 5,"class":"text-l"
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                        if (row.status == 1) {
                            return '<span class="label label-success radius">已启用</span>';
                        } else {
                            return '<span class="label label-danger radius">已禁用</span>';
                        }
                    },
                    "targets": 8
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                        var dom = '';
                        if (row.status == '1') {
                            dom += '<a onClick="assignEnable(\''
                                + row.pyId
                                + '\',\''
                                + '2'
                                + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                        } else if (row.status == '2') {
                            dom += '<a onClick="assignEnable(\''
                                + row.pyId
                                + '\',\''
                                + '1'
                                + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                        }
                        dom += ' &nbsp;';
                        dom += '<a title="编辑" href="javascript:void(0)" onclick="assignEdit(\''
                            + row.pyId
                            + '\')" class="ml-5" style="text-decoration:none">';
                        dom += '<i class="iconfont icon-edit"></i></a>';
                        return dom;
                    },
                    "targets": 10
                }
            ]
        });

    });

    /*考场安排-新增*/
    function addAssign() {
        var url = '/examRoomAssign/toAdd.do';
        layer_show('新增考场安排', url, null, null, function () {
            myDataTable.fnDraw(true);
        },true);
    }

    /*考场安排-编辑*/
    function assignEdit(pyId) {
        var url = '/examRoomAssign/toEdit.do' + '?pyId=' + pyId;
        layer_show('编辑考场安排', url, null, null, function () {
            myDataTable.fnDraw(false);
        },true);
    }

    // 状态更新
    function assignEnable(pyId, status) {
        var arrays = [];
        arrays.push(pyId);
        var msg = status == "1" ? "启用" : "禁用";
        layer.confirm('确认要' + msg + '吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/examRoomAssign/updateStatus.do',
                data: {
                    pyIds: arrays,
                    status: status
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('已' + msg + '!', {
                            icon: status == "1" ? 6 : 5,
                            time: 1000
                        });
                    }
                }
            });
        });
    }

    // 批量状态更新
    function batchAssignEnable(status) {
        var chk_value = [];
        $("input[name=pyIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }
        var msg = status == "1" ? "启用" : "禁用";
        layer.confirm('确认要' + msg + '吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/examRoomAssign/updateStatus.do',
                data: {
                    pyIds: chk_value,
                    status: status
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                        layer.msg('已' + msg + '!', {
                            icon: status == "1" ? 6 : 5,
                            time: 1000
                        });
                    }
                }
            });
        });
    }

    //批量删除
    function delAll() {
        var chk_value = [];
        $("input[name=pyIds]:checked").each(function () {
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
                url: '/examRoomAssign/deletes.do',
                data: {
                    pyIds: chk_value
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
    
    function assignExport() {
        layer.msg("正在导出Excel，请勿再次点击！")
        $("#reset").submit();
    }