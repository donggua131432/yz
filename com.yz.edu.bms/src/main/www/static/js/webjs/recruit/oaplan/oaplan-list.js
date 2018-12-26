
var myMonthDataTable;
var myYearDataTable;
//标签块
$.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
$(function () {

    //初始化拉框
    /*  var parent = $(":input[name='dpId']");
     for (var i = 0; i < dictJson.dpName.length; i++) {
     var option = $("<option>").text(dictJson.dpName[i].dictName).val(dictJson.dpName[i].dictValue);
     if($(":input[name='dpId']").val() == dictJson.dpName[i].dictValue)
     option.attr("selected","true");
     parent.append(option);
     } */

    //初始化数据表格
    myMonthDataTable = $('#monthTable').dataTable({
        "serverSide": true,
        "dom": 'rtilp',
        "ajax": {
            url: "/recruit/list.do",
            type: "post",
            data: {
                "planType": '1',
                "timeSlot": function () {
                    return $("#timeSlot").val() ? $("#timeSlot").val() : '';
                },
                "dpId": function () {
                    return $("#dpId").val() ? $("#dpId").val() : '';
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
            "mData": "timeSlot"
        }, {
            "mData": null
        }, {
            "mData": "dpName"
        }, {
            "mData": "campusName"
        }, {
            "mData": "targetCount"
        }, {
            "mData": "realCount"
        }, {
            "mData": null
        }, {
            "mData": "todayIncr"
        }, {
            "mData": null
        }],
        "columnDefs": [{
            "render": function (data, type, row, meta) {
                return '<input type="checkbox" value="' + row.planId + '" name="monthPlanId"/>';
            },
            "targets": 0
        }, {
            "render": function (data, type, row, meta) {
                return row.percent + '%';
            },
            "targets": 7
        }, {
            "render": function (data, type, row, meta) {
                var dom = '';

                dom = '<a title="编辑" href="javascript:;" onclick="plan_edit(\'' + row.planId + '\',1)" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
                dom += '&nbsp;&nbsp;&nbsp;';
                dom += '<a title="删除" href="javascript:;" onclick="plan_del(this,\'' + row.planId + '\',1)" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont f-18">&#xe609;</i></a>';

                return dom;
            },
            //指定是第三列
            "targets": 9
        }]
    });


    myYearDataTable = $('#yearTable').dataTable({
        "serverSide": true,
        "dom": 'rtilp',
        "ajax": {
            url: "/recruit/list.do",
            type: "post",
            data: {
                "planType": '2',
                "timeSlot": function () {
                    return $("#timeSlot2").val() ? $("#timeSlot2").val() : '';
                },
                "dpId": function () {
                    return $("#dpId2").val() ? $("#dpId2").val() : '';
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
            "mData": "dpName"
        }, {
            "mData": "campusName"
        }, {
            "mData": null
        }, {
            "mData": "targetCount"
        }, {
            "mData": "realCount"
        }, {
            "mData": null
        }, {
            "mData": null
        }],
        "columnDefs": [{
            "render": function (data, type, row, meta) {
                return '<input type="checkbox" value="' + row.planId + '" name="yearPlanId"/>';
            },
            "targets": 0
        }, {
            "render": function (data, type, row, meta) {
                return row.percent + '%';
            },
            "targets": 6
        }, {
            "render": function (data, type, row, meta) {
                var dom = '';

                dom = '<a title="编辑" href="javascript:;" onclick="plan_edit(\'' + row.planId + '\',2)" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
                dom += '&nbsp;&nbsp;&nbsp;';
                dom += '<a title="删除" href="javascript:;" onclick="plan_del(this,\'' + row.planId + '\',1)" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont f-18">&#xe609;</i></a>';

                return dom;
            },
            //指定是第三列
            "targets": 7
        }]
    });
});

/*用户-添加*/
function plan_add(type) {
    var url = '/recruit/toEdit.do' + '?exType=ADD&planType=' + type;
    layer_show('添加计划', url, null, 580, function () {
        drawTable(type);
    });
}

/*用户-编辑*/
function plan_edit(planId, type) {
    var url = '/recruit/toEdit.do' + '?planId=' + planId + '&exType=UPDATE&planType=' + type;
    layer_show('修改计划', url, null, 580, function () {
        drawTable(type);
    });
}

/*用户-删除*/
function plan_del(obj, planId, type) {
    layer.confirm('确认要删除吗？', function (index) {
        $.ajax({
            type: 'POST',
            url: '/recruit/delete.do',
            data: {
                planId: planId
            },
            dataType: 'json',
            success: function (data) {
                layer.msg('已删除!', {
                    icon: 1,
                    time: 1000
                });
                drawTable(type);
                $("input[name=all]").attr("checked", false);
            },
            error: function (data) {
                layer.msg('删除失败！', {
                    icon: 1,
                    time: 1000
                });
                drawTable(type);
                $("input[name=all]").attr("checked", false);
            },
        });
    });
}

function delAll(type) {
    var chk_value = [];
    var $input = null;
    if (type == 1) {
        $input = $("input[name=monthPlanId]:checked");
    } else if (type == 2) {
        $input = $("input[name=yearPlanId]:checked");
    }

    $input.each(function () {
        chk_value.push($(this).val());
    });

    layer.confirm('确认要删除吗？', function (index) {
        $.ajax({
            type: 'POST',
            url: '/recruit/deletePlans.do',
            data: {
                planIds: chk_value
            },
            dataType: 'json',
            success: function (data) {
                layer.msg('已删除!', {
                    icon: 1,
                    time: 1000
                });
                drawTable(type);
                $("input[name=all]").attr("checked", false);
            },
            error: function (data) {
                layer.msg('删除失败！', {
                    icon: 1,
                    time: 1000
                });
                drawTable(type);
                $("input[name=all]").attr("checked", false);
            }
        });
    });
}

function drawTable(type) {
    if (type == 1) {
        myMonthDataTable.fnDraw(true);
    } else {
        myYearDataTable.fnDraw(true);
    }
}

function searchMonth() {
    myMonthDataTable.fnDraw(true);
}
function searchYear() {
    myYearDataTable.fnDraw(true);
}