var myDataTable;
    $(function () {


        var arr = [];
        var barWidth;
        //初始学员状态
        _init_select("stdStage", dictJson.stdStage);
        //初始审批状态
        _init_select("checkStatus", [
            {
                "dictValue": "0", "dictName": "未完成"
            },
            {
                "dictValue": "1", "dictName": "完成"
            }
        ]);

        $('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });

        myDataTable = $('.table-sort').dataTable({
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/refund/list.do",
                type: "post",
                data: {
                    "stdName": function () {
                        return $("#stdName").val() ? $("#stdName").val() : '';
                    },
                    "mobile": function () {
                        return $("#mobile").val() ? $("#mobile").val() : '';
                    },
                    "idCard": function () {
                        return $("#idCard").val() ? $("#idCard").val() : '';
                    },
                    "stdStage": function () {
                        return $("#stdStage").val() ? $("#stdStage").val() : '';
                    },
                    "checkStatus": function () {
                        return $("#checkStatus").val() ? $("#checkStatus").val() : '';
                    }
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "fnDrawCallback": function () {
                for (var i = 0; i < arr.length; i++) {
                    if (!barWidth)
                        barWidth = $("#" + arr[i].id).width();
                    var progress = new Progress({
                        bar: {},
                        item: {},
                        barWidth: 0,
                        itemCount: 2,
                        itemWidth: 0,
                        processWidth: 0,
                        curProcessWidth: 0,
                        step: 1,
                        curStep: 0,
                        triggerStep: 1,
                        change: false,
                        animation: false,
                        speed: 1000,
                        stepEasingForward: "easeOutCubic",
                        stepEasingBackward: "easeOutElastic",
                        width: 504
                    }, arr[i].id, {
                        step: arr[i].steps,
                        animation: true,
                        change: true,
                        isCheck: arr[i].isCheck,
                        width: barWidth
                    });
                    progress.init();
                }
            },
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            columns: [{
                "mData": null
            }, {
                "mData": "stdName"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": "demurrageAmount"
            }, {
                "mData": "refundAmount"
            }, {
                "mData": "cashAmount"
            }, {
                "mData": "remark"
            }, {
                "mData": null
            }, {
                "mData": null
            }],
            "columnDefs": [{
                "render": function (data, type, row, meta) {
                    return '<input type="checkbox" value="' + row.refundId + '" name="refundIds"/>';
                },
                "targets": 0
            }, {
                "render": function (data, type, row, meta) {
                    return _findDict("grade", row.grade);
                },
                "targets": 2
            }, {
                "render": function (data, type, row, meta) {
                    var dom = '';
                    dom += _findDict("recruitType", row.recruitType).indexOf("成人")!=-1? "[成教]" : "[国开]";
                    dom += row.unvsName + "<br>";
                    dom += _findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1? "[专科]" : "[本科]";
                    dom += row.pfsnName;
                    return dom;
                },
                "targets": 3, "class": "text-l"
            }, {
                "render": function (data, type, row, meta) {
                    return _findDict("scholarship", row.scholarship);
                },
                "targets": 4
            }, {
                "render": function (data, type, row, meta) {
                    return _findDict("stdStage", row.stdStage);
                },
                "targets": 5
            }, {"targets" : 9,"class":"text-l"},{

                "render": function (data, type, row, meta) {
                    var dateTime = row.createTime;
                    if (!dateTime) {
                        return '-'
                    }
                    var date = dateTime.substring(0, 10)
                    var time = dateTime.substring(11)
                    return date + '<br>' + time
                },
                "targets": 10, "class": "text-l"
            }, {
                "render": function (data, type, row, meta) {
                    var isCheck = true;
                    var temp = row;
                    row = row.checks;
                    var step = 2;
                    var timestamp = uuid();
                    var stepBar = "stepBar" + timestamp;
                    var dom = '<div id="' + stepBar + '" class="ui-stepBar-wrap">'
                        + '<div class="ui-stepBar">'
                        + '<div class="ui-stepProcess"></div>'
                        + '</div>'
                        + '<div class="ui-stepInfo-wrap">'
                        + '<table class="ui-stepLayout" border="0" cellpadding="0" cellspacing="0">'
                        + '<tr >'
                        + '<td class="ui-stepInfo"  style="border: 0px">'
                        + '<a class="ui-stepSequence">1</a>'
                        + '<p class="ui-stepName">提交:' + temp.createUser + '</p>'
                        + '</td>';
                    for (var i = 0; i < row.length; i++) {
                        var empName;
                        if (row[i].empName == null) {
                            empName = "";
                        } else {
                            empName = row[i].empName;
                        }

                        if (row[i].checkStatus == 4) {
                            isCheck = false;
                        } else if (row[i].checkStatus == 3) {
                            step++;
                        }

                        dom += '<td class="ui-stepInfo" style="border: 0px">';
                        if (row[i].checkStatus == 4) {
                            dom += '<a class="ui-stepSequence" title="查看驳回原因" onclick="undoClick(\'' + row[i].reason + '\')">' + (i + 2) + '</a>';
                        } else {
                            dom += '<a class="ui-stepSequence">' + (i + 2) + '</a>';
                        }
                        dom += '<p class="ui-stepName">' + _findDict("jtId", row[i].jtId) + ":" + empName + '</p>';
                        dom += '</td>';
                    }
                    dom += '<td class="ui-stepInfo" style="border: 0px">'
                        + '<a class="ui-stepSequence">' + (row.length + 2) + '</a>'
                        + '<p class="ui-stepName">完成</p>'
                        + '</td>'
                        + '</tr>'
                        + '</table>'
                        + '</div>'
                        + '</div>';
                    arr.push({"steps": step, "isCheck": isCheck, "id": stepBar});
                    return dom;
                },
                //指定是第三列
                "targets": 11
            }]
        });

    });

    /*用户-添加*/
    function member_add() {
        var url = '/refund/edit.do' + '?exType=ADD';
        layer_show('添加退费(提现)', url, null, 630, function () {
            myDataTable.fnDraw(true);
        });
    }


    function cancelRefund() {
        var chk_value = [];
        $("input[name=refundIds]:checked").each(function () {
            chk_value.push($(this).val());
        });
        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认撤销未审核申请吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/refund/cancelRefund.do',
                data: {
                    refundIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('撤销成功!', {
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
    function uuid() {
        return Math.random().toString(36).substring(3, 8)
    }

    function undoClick(reason) {
        if (null == reason || reason == '' || 'null' == reason) {
            reason = "无";
        }
        layer.alert(reason);
    }