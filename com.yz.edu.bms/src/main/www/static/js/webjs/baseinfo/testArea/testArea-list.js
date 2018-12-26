var myDataTable;
    $(function () {
        //初始化考区类型下拉框
        _init_select("taType", dictJson.taType);

        //初始状态
        _init_select("isAllow", [
            {
                "dictValue": "1", "dictName": "是"
            },
            {
                "dictValue": "0", "dictName": "否"
            }
        ]);

        //初始化省市区
        var rprZipCode = _init_area_select("provinceId", "cityId", "areaId");

        myDataTable = $('.table-sort').dataTable(
            {
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',      
                "ajax": {
                    url: "/testArea/findAllTestArea.do",
                    type: "post",
                    data: {
                        "provinceId": function () {
                            return $("#provinceId").val();
                        },
                        "cityId": function () {
                            return $("#cityId").val();
                        },
                        "areaId": function () {
                            return $("#areaId").val();
                        },
                        "isAllow": function () {
                            return $("#isAllow").val();
                        }
                    }
                },
//                "pageLength": 10,
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
                    "mData": null
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
                    "mData": null
                }],
                "columnDefs": [
                    {
                        "render": function (data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.taId + '" name="taIds"/>';
                        },
                        "targets": 0
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return getValue(row.provinceId);
                        },
                        "targets": 1
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return getValue(row.cityId);
                        },
                        "targets": 2
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return getValue(row.areaId);
                        },
                        "targets": 3
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return parseInt(row.taCode);
                        },
                        "targets": 5
                    },
                    {
                        "render": function (data, type, row, meta) {
                            return 0 == row.isAllow ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
                        },
                        "targets": 6
                    },
                    {
                        "render": function (data, type, row, meta) {
                            var dom = '';

                            dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.taId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            /* dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="member_del(this,\'' + row.taId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>'; */

                            return dom;
                        },
                        //指定是第三列
                        "targets": 7
                    }]
            });

    });

    /*用户-添加*/
    function member_add() {
        var url = '/testArea/edit.do' + '?exType=ADD';
        layer_show('添加考试县区', url, 700,360, function () {
//            myDataTable.fnDraw(true);
        });
    }

    /*用户-编辑*/
    function member_edit(taId) {
        var url = '/testArea/edit.do' + '?taId=' + taId + '&exType=UPDATE';
        layer_show('修改考试县区', url, 700, 360, function () {
//            myDataTable.fnDraw(false);
        });
    }

    /*用户-导入*/
    function excel_import() {
        var url = '/testArea/excelImport.do';
        layer_show('导入考试县区', url, 600, 300, function () {
            myDataTable.fnDraw(true);
        });
    }

    /*用户-删除*/
    function member_del(obj, taId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/testArea/deleteTestArea.do',
                data: {
                    id: taId
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已删除!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
                error: function (data) {
                    layer.msg('删除失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
            });
        });
    }

    function delAll() {
        var chk_value = [];
        $("input[name=taIds]:checked").each(function () {
            chk_value.push($(this).val());
        });

        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/testArea/deleteAllTestArea.do',
                data: {
                    idArray: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    layer.msg('已删除!', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
                error: function (data) {
                    layer.msg('删除失败！', {
                        icon: 1,
                        time: 1000
                    });
                    myDataTable.fnDraw(true);
                    $("input[name=all]").attr("checked", false);
                },
            });
        });
    }

    function _search() {
        myDataTable.fnDraw(true);
    }

    //根据编码获取省市区相应值
    function getValue(key) {
        for (var i = 0; i < pcdJson.length; i++) {
            if (key == pcdJson[i].provinceCode) {
                return pcdJson[i].provinceName;
            }
            var city = pcdJson[i].city;
            for (var o = 0; o < city.length; o++) {
                if (key == city[o].cityCode) {
                    return city[o].cityName;
                }
                var district = city[o].district;
                for (var p = 0; p < district.length; p++) {
                    if (key == district[p].districtCode) {
                        return district[p].districtName;
                    }
                }
            }
        }
        return "";
    }