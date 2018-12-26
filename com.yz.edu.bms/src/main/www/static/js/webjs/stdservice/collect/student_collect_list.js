var myDataTable;
    $(function () {
        $('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
        //初始状态
        _init_select("receiveStatus", [
            {"dictValue": "0", "dictName": "未收到"},
            {"dictValue": "1", "dictName": "已收到"}
        ]);
        _init_select("isQualified", [
            {"dictValue": "0", "dictName": "不合格"},
            {"dictValue": "1", "dictName": "合格"}
        ]);
        _init_select("isRemark", [
            {"dictValue": "0", "dictName": "无备注"},
            {"dictValue": "1", "dictName": "有备注"}
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

        _simple_ajax_select({
            selectId: "taskId",
            searchUrl: '/studyActivity/findTaskInfo.do?taskType=11',
            sData: {},
            showText: function (item) {
                return item.task_title;
            },
            showId: function (item) {
                return item.task_id;
            },
            placeholder: '--请选择任务--'
        });
        $("#taskId").append(new Option("", "", false, true));

        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: '/collect/findAllCollectList.do',
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
                {"mData": "stdNo"},
                {"mData": "schoolRoll"},
                {"mData": null},
                {"mData": null},
                {"mData": "taskTitle"},
                {"mData": "tutor"},
                {"mData": null},
                {"mData": "unqualifiedReason"},
                {"mData": null},
                {"mData": "remark"},
                {"mData": null}
            ],
            "columnDefs": [
                {
                    "targets": 3, "render": function (data, type, row, meta) {
                    return _findDict("grade", row.grade);
                }
                },
                {
                    "targets": 4, "class": "text-l", "render": function (data, type, row, meta) {
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
                    "targets": 7, "render": function (data, type, row, meta) {
                    if (row.isQualified && row.isQualified == '1') {
                        return "<label class='label label-success radius'>合格</label>";
                    }  if (row.isQualified && row.isQualified == '0') {
                        return "<label class='label  label-danger radius'>不合格</label>";
                    }else {
                        return ''
                    }
                }
                },
                {
                    "targets": 9, "render": function (data, type, row, meta) {
                    if (row.receiveStatus && row.receiveStatus == '1') {
                        return "<label class='label label-success radius'>已收到</label>";
                    }  else {
                        return "<label class='label label-danger radius'>未收到</label>";
                    }
                }
                },
                {
                    "targets": 11, "render": function (data, type, row, meta) {
                    var dom = '';

                    dom = '<a title="资料登记反馈" href="javascript:;" onclick="feedback(\'' + row.ctId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-shenhe"></i></a>';

                    dom += '<a title="添加备注" href="javascript:;" onclick="addRemark(\'' + row.ctId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-edit"></i></a>';
                    return dom
                }
                }
            ]
        });
    });

    /*资料登记反馈*/
    function feedback(id) {
        var url = '/collect/toFeedback.do' + '?id=' + id;
        layer_show('资料登记反馈', url, null, 600, function () {
        });
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


    function collectExport() {
        $("#export-form").submit();
    }

    /*添加备注*/
    function addRemark(ctId) {
        $("#addRemark").val('');
        var url = '/collect/addRemark.do';
        layer.open({
            type: 1,
            btn: ['确定'],
            yes: function (index, layero) {
                var addRemark = $("#addRemark").val();
                if (addRemark.length > 20) {
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
                        ctId: ctId,
                        addRemark: addRemark
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
            content: $("#addRemarkContent")
        });
    }