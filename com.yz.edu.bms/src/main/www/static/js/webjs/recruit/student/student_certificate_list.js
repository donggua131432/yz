var myDataTable;
    $(function () {
        $('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
        //初始状态
        _init_select("applyType", [
            {"dictValue": "6", "dictName": "报读证明"}
        ]);

        _init_select("isSend", [
            {"dictValue": "0", "dictName": "未邮寄"},
            {"dictValue": "1", "dictName": "已邮寄"}
        ]);

        _init_select("receiveType", [
            {"dictValue": "1", "dictName": "邮寄"},
            {"dictValue": "2", "dictName": "自取"}
        ]);

        _init_select("checkStatus", [
            {"dictValue": "0", "dictName": "审核中"},
            {"dictValue": "1", "dictName": "已初审"},
            {"dictValue": "2", "dictName": "已受理"},
            {"dictValue": "3", "dictName": "已驳回"}
        ]);

        _simple_ajax_select({
            selectId: "unvsId",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        $("#unvsId").append(new Option("", "", false, true));
        $("#unvsId").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
        });
        $("#grade").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
        });
        $("#pfsnLevel").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
        });
        $("#pfsnId").append(new Option("", "", false, true));
        $("#pfsnId").select2({
            placeholder: "--请先选择院校--"
        });
        //初始化年级下拉框
        _init_select("grade", dictJson.grade);

        //初始化院校类型下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel);

        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: '/certificate/findAllListZS.do',
                type: "post",
                data: function (pageData) {
                    pageData = $.extend({}, {
                        start: pageData.start,
                        length: pageData.length
                    }, $("#export-form").serializeObject());
                    return pageData;
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
                {"mData": "stdName"},
                {"mData": "schoolRoll"},
                {"mData": null},
                {"mData": null},
                {"mData": "recruit"},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null}
            ],
            "columnDefs": [
                {
                    "targets": 2, "render": function (data, type, row, meta) {
                    return _findDict("grade", row.grade);
                }
                },
                {
                    "targets": 3, "class": "text-l", "render": function (data, type, row, meta) {
                    var pfsnName = row.pfsnName, unvsName = row.unvsName, recruitType = row.recruitType,
                        pfsnCode = row.pfsnCode, pfsnLevel = row.pfsnLevel, text = '';
                    if (recruitType) {
                        if (_findDict("recruitType", recruitType).indexOf("成人") != -1) {
                            text += "[成教]";
                        } else {
                            text += "[国开]";
                        }
                    }
                    if (unvsName) text += unvsName + '</br>';
                    if (pfsnLevel) {
                        if (_findDict("pfsnLevel", pfsnLevel).indexOf("高中") != -1) {
                            text += "[专科]";
                        } else {
                            text += "[本科]";
                        }
                    }
                    if (pfsnName) text += pfsnName;
                    if (pfsnCode) text += "(" + pfsnCode + ")";
                    return text ? text : '无';
                }
                },
                {
                    "targets": 5, "render": function (data, type, row, meta) {
                    if (row.applyType && row.applyType == '6') {
                        return "报读证明";
                    }
                }
                },
                {
                    "targets": 6,"render":function (data, type, row, meta) {
                        var dom='';
                        dom+="<p class='overflowEllipsis'>"+row.applyPurpose+"</p>";
                        return dom
                    }
                },
                {
                    "targets": 7, "render": function (data, type, row, meta) {
                    if (row.createTime) {
                        var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                        if (!dateTime) {
                            return '-'
                        }
                        var date = dateTime.substring(0, 10)
                        var time = dateTime.substring(11)
                        return date + '<br>' + time;
                    } else {
                        return '';
                    }
                }
                },
                {
                    "targets": 8, "render": function (data, type, row, meta) {
                    if (row.isSend && row.isSend == '1') {
                        return "<label class='label label-success radius'>是</label>";
                    } else {
                        return "<label class='label label-danger radius'>否</label>";
                    }
                }
                },
                {
                    "targets": 9, class: "text-l", "render": function (data, type, row, meta) {
                    var dom = '';
                    if (row.receiverName && row.receiverName != '') {
                        dom += "收件人：" + row.receiverName + "<br/>";
                    }
                    if (row.receiverMobile && row.receiverMobile != '') {
                        dom += "手机：" + row.receiverMobile + "<br/>";
                    }
                    if (row.receiverAddress && row.receiverAddress != '') {
                        dom += row.receiverAddress + "<br/>";
                    }
                    return dom;
                }
                },
                {
                    "targets": 10, class: "text-l", "render": function (data, type, row, meta) {
                    var dom = '';
                    if(row.expressNo) {
                        dom += '<a href="http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/' + row.expressNo + '" target="_blank" class="tableBtn normal" title="查看物流信息">'
                            + row.expressNo + '</a>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 11, "render": function (data, type, row, meta) {
                    if (row.receiveType && row.receiveType == '1') {
                        return "邮寄";
                    } else {
                        return "自取";
                    }
                }
                },
                {
                    "targets": 12, "render": function (data, type, row, meta) {
                    var dom = '<label class="label label-danger radius">已驳回</label>';
                    if ("1" == row.checkOrder && "1" == row.checkStatus) {
                        dom = '<label class="label radius">审核中</label>';
                    }
                    if ("2" == row.checkOrder && "1" == row.checkStatus) {
                        dom = '<label class="label label-secondary radius">已初审</label>';
                    }
                    if ("2" == row.checkOrder && "2" == row.checkStatus) {
                        dom = '<label class="label label-success radius">已受理</label>';
                    }
                    return dom;
                }
                },
                {
                    "targets": 13, "render": function (data, type, row, meta) {
                    var dom = '';
                    if(row.applyType=='6' && row.checkOrder=='2' && row.checkStatus == '2'){
                        dom += '<a title="打印在读证明" href="javascript:;" onclick="print(\'' + row.learnId + '\')" class="ml-5" style="text-decoration: none">';
                        dom += '<i class="iconfont icon-daochu"></i></a>';
                    }

                    dom += '<a title="查看详情" href="javascript:;" onclick="view(\'' + row.certId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-chakan"></i></a>';

                    if (row.checkOrder == '1' && row.checkStatus != '3') {
                        dom += '<a title="审核" href="javascript:;" onclick="check(\'' + row.certId + '\')" class="ml-5" style="text-decoration: none">';
                        dom += '<i class="iconfont icon-shenhe"></i></a>';
                    }
                    if (row.checkOrder == '2' && row.checkStatus == '2') {
                        dom += '<a title="发货" href="javascript:;" onclick="addExpressNo(\'' + row.certId + '\')" class="ml-5" style="text-decoration: none">';
                        dom += '<i class="iconfont icon-edit"></i></a>';
                    }
                    return dom
                }
                }
            ]
        });
    });

    /*审核*/
    function check(id) {
        var url = '/certificate/toEdit.do' + '?certId=' + id;
        layer_show('审核申请', url, 880, 730, function () {
        });
    }

    /*查看详情*/
    function view(id) {
        var url = '/certificate/viewZS.do' + '?certId=' + id;
        layer_show('申请详情', url, 880, 730, function () {
        });
    }
    
    /*打印在读证明*/
    function print(learnId) {
        var url = '/certificate/getStudyInfoZS.do' + '?learnId=' + learnId;
        var printUrl = '/certificate/printViewZS.do'+ '?learnId=' + learnId;
        window.open(printUrl);
    }

    /*搜素*/
    function searchResult() {
        myDataTable.fnDraw(true);
    }

    function init_pfsn_select() {
        _simple_ajax_select({
            selectId: "pfsnId",
            searchUrl: '/baseinfo/sPfsn.do',
            sData: {
                sId: function () {
                    return $("#unvsId").val() ? $("#unvsId").val() : '';
                },
                ext1: function () {
                    return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
                },
                ext2: function () {
                    return $("#grade").val() ? $("#grade").val() : '';
                }
            },
            showText: function (item) {
                var text = '(' + item.pfsnCode + ')' + item.pfsnName;
                text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
                return text;
            },
            showId: function (item) {
                return item.pfsnId;
            },
            placeholder: '--请选择专业--'
        });
        $("#pfsnId").append(new Option("", "", false, true));
    }


    /*发货*/
    function addExpressNo(certId) {
        $("#addExpressNo").val('');
        var url = '/certificate/deliverZS.do';
        layer.open({
            type: 1,
            btn: ['确定'],
            title: '发货设置',
            yes: function (index, layero) {
                var addExpressNo = $("#addExpressNo").val();
                if (addExpressNo == '') {
                    layer.msg("请填写快递单号");
                    return;
                }
                if (addExpressNo.length > 20) {
                    layer.msg('最大支持20个字符!', {
                        icon: 2,
                        time: 2000
                    });
                    return;
                }

                $.ajax({
                    type: 'POST',
                    url: url,
                    data: {
                        certId: certId,
                        addExpressNo: addExpressNo
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('添加成功!', {
                                icon: 1,
                                time: 1000
                            });
                            myDataTable.fnDraw(false);
                        }
                    }
                });
                layer.close(index)
            },
            area: ['500px', '300px'],
            content: $("#addExpressNoContent")
        });
    }

    function certificateImport() {
        var url = '/certificate/certificateImportZS.do';
        layer_show('报读证明申请导入', url, null, 510, function () {

        });
    }

    function certificateExport() {
        $("#export-form").submit();
    }