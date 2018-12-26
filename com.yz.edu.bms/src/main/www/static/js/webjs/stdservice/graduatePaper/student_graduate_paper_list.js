	var myDataTable;
    $(function () {
        $('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
        //初始状态
        _init_select("checkStatus", [
            {"dictValue": "0", "dictName": "未完成"},
            {"dictValue": "1", "dictName": "审核中"},
            {"dictValue": "2", "dictName": "审核成功"},
            {"dictValue": "3", "dictName": "审核不通过"}
        ]);
        _init_select("isUpload", [
            {"dictValue": "0", "dictName": "未上传"},
            {"dictValue": "1", "dictName": "已上传"}
        ]);
        _init_select("isView", [
            {"dictValue": "0", "dictName": "未查看"},
            {"dictValue": "1", "dictName": "已查看"}
        ]);
        _init_select("isRemark", [
            {"dictValue": "0", "dictName": "无备注"},
            {"dictValue": "1", "dictName": "有备注"}
        ]);
        _init_select("hasCheck", [
            {"dictValue": "0", "dictName": "待审核文档"}
        ]);

        _init_select("paperDataStatus", [
            {"dictValue": "0", "dictName": "未收到"},
            {"dictValue": "1", "dictName": "收到"},
            {"dictValue": "2", "dictName": "收到不合格"},
            {"dictValue": "3", "dictName": "收到且合格"}
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
            searchUrl: '/studyActivity/findTaskInfo.do?taskType=9',
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
                url: '/graduatePaper/findAllPaperList.do',
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
                {"mData": null},
                {"mData": null},
                {"mData": "schoolRoll"},
                {"mData": "taskTitle"},
                {"mData": "paperNo"},
                {"mData": null},
                {"mData": "paperTitle"},
                {"mData": "tutor"},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": null},
                {"mData": "remark"},
                {"mData": null}
            ],
            "columnDefs": [
                {
                    "targets": 1, "render": function (data, type, row, meta) {
                    return _findDict("grade", row.grade);
                }
                },
                {
                    "targets": 2, "class": "text-l", "render": function (data, type, row, meta) {
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
                    "targets": 6, "render": function (data, type, row, meta) {
                        //判读是否是韩山师范的，韩山师范可查看详情
                        if(row.guideTeacher){
                            if (row.unvsId === '29') {
                                return row.guideTeacher + '<br>(<a title="查看指导老师" href="javascript:;" onclick="openGuideTeacherInfo(\'' + row.guideTeacher + '\',\'' + row.guideTeacherEmail + '\',\'' + row.guideTeacherPhone + '\')">查看详情</a>)';
                            } else {
                                return row.guideTeacher;
                            }
                        }
                        return "";
                    }
                },
                {
                    "targets": 9, "render": function (data, type, row, meta) {
                    if (row.isUpload && row.isUpload == '1') {
                        return "<label class='label label-success radius'>是</label>";
                    } else {
                        return "<label class='label  label-danger radius'>否</label>";
                    }
                }
                },
                {
                    "targets": 10, "render": function (data, type, row, meta) {
                        if (row.isView && row.isView == '1') {
                            return "<label class='label label-success radius'>是</label>";
                        } else {
                            return "<label class='label  label-danger radius'>否</label>";
                        }
                    }
                },
                {
                    "targets": 11, "render": function (data, type, row, meta) {
                    if (row.paperDataStatus && row.paperDataStatus == '0') {
                        return "<label class='label label-danger radius'>未收到</label>";
                    }
                    else if (row.paperDataStatus && row.paperDataStatus == '1') {
                        return "<label class='label label-success radius'>收到</label>";
                    }
                    else if (row.paperDataStatus && row.paperDataStatus == '2') {
                        return "<label class='label label-danger radius'>收到不合格</label>";
                    }
                    else if (row.paperDataStatus && row.paperDataStatus == '3') {
                        return "<label class='label label-success radius'>收到且合格</label>";
                    }
                    else {
                        return "";
                    }
                }
                },
                {
                    "targets": 12, "render": function (data, type, row, meta) {
                    if (row.checkStatus && row.checkStatus == '1') {
                        return "<label class='label radius'>审核中</label>";
                    } else if (row.checkStatus && row.checkStatus == '2') {
                        return "<label class='label label-success radius'>审核通过</label>";
                    }
                    else if (row.checkStatus && row.checkStatus == '3') {
                        return "<label class='label label-danger radius'>审核不通过</label>";
                    } else {
                        return "<label class='label radius'>未完成</label>";
                    }
                }
                },
                {
                    "targets": 13, "render": function (data, type, row, meta) {
                    var dom = '';

                    dom = '<a title="查看详情" href="javascript:;" onclick="view(\'' + row.learnId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-chakan"></i></a>';
                    return dom;
                }
                },
                {
                    "targets": 14, "class": "text-l", "render": function (data, type, row, meta) {
                    if (!row.attachments) return '';
                    var dom = "";
                    for (var i = 0; i < row.attachments.length; i++) {
                        var attachment = row.attachments[i];
                        dom += '<a href='+ _FILE_URL+ attachment.attachmentUrl+' style="color:#069">' +(i+1)+':'+ attachment.attachmentName+ (attachment.paperUploadType==0?'[校]':'')+ "</a><br/>";
                    }
                    return dom;
                }
                },
                {
                    "targets": 16, "render": function (data, type, row, meta) {
                    var dom = '';

                    if(row.unvsId !== '29'){//韩师关闭审核按钮
                        dom += '<a title="资料审核" href="javascript:;" onclick="pass(\'' + row.learnId + '\')" class="ml-5" style="text-decoration: none">';
                        dom += '<i class="iconfont icon-shenhe"></i></a>';
                    }

                    dom += '<a title="上传" href="javascript:;" data-learnid=' + row.learnId + ' class="ml-5 upload" style="position: relative;display: inline-block;text-decoration: none;">';
                    dom += '<i class="iconfont icon-daoru"></i></a>';

                    dom += '<a title="添加备注" href="javascript:;" onclick="addRemark(\'' + row.gpId + '\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-edit"></i></a>';

                    dom += '<a title="纸质资料状态" href="javascript:;" onclick="addPaperDataStatus(\'' + row.gpId + '\',\''+row.paperDataStatus+'\')" class="ml-5" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-edit"></i></a>';
                    return dom
                }
                }
            ],
            "drawCallback": function (settings) {
                $('#tab .upload').each(function () {
                    initUploader($(this), $(this).data('learnid'),$(this).data('unvsid'))
                })

            }
        });
    });

    /*编辑*/
    function editXuexin(id) {
        var url = '/xuexin/toEdit.do' + '?id=' + id;
        layer_show('编辑', url, 880, 730, function () {
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

    function paperImport() {
        var url = '/graduatePaper/paperImport.do';
        layer_show('毕业论文及报告提交导入', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    function paperExport() {
        $("#export-form").submit();
    }
    
    function pass(learnId) {
        var url = '/graduatePaper/attachmentList.do?learnId='+learnId ;
        layer_show('资料审核', url, null, 510, function() {
        },true);
    }

    function view(learnId) {
        var url = '/graduatePaper/commentView.do?learnId='+learnId ;
        layer_show('查看详情', url, 800, 510, function() {
        });
    }

    /*添加备注*/
    function addRemark(gpId) {
        $("#addRemark").val('');
        var url = '/graduatePaper/addRemark.do';
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
                        gpId: gpId,
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

    /*添加纸质资料状态*/
    function addPaperDataStatus(gpId,paperDataStatus) {
        var url = '/graduatePaper/paperDataStatus.do?gpId='+gpId+'&paperDataStatus='+paperDataStatus;
        layer_show('纸质资料状态', url, null, 400, function () {

        });
    }



    function initUploader($pick, learnId) {
        var uploader = WebUploader.create({// 上传控件
            auto: true,
            swf: '/js/webuploader/Uploader.swf',
            server: '/graduatePaper/webuploader.do?learnId='+learnId,
            pick: $pick,
            fileNumLimit: 1,
            accept: {// 只允许文档文件
                title: 'doc',
                extensions: 'doc,docx,xls,xlsx,ppt,pptx'
            }
        });
        uploader.on('fileQueued', function (file) { // 添加文件到队列
            var $list = $('#fileList');
            var $li = $('<div id="' + file.id + '" data-path="" class="file-item thumbnail"><a href="javascript:;" class="cancel">&times;</a></div>');
            $list.append($li);
            uploader.makeThumb(file, function (error, src) {// 创建缩略图
                if (error) {
                    $li.append('<span>不能预览</span>');
                    return;
                }
                $li.append('<img src="' + src + '"/>');
            }, 100, 100);
            $li.on('click', '.cancel', function () {// 删除图片
                uploader.removeFile(file);
                $li.remove();
            });
        }).on('uploadProgress', function (file, percentage) { // 文件进度
            var $li = $('#' + file.id);
            var $percent = $li.find('.progress');
            if (!$percent.length) {// 避免重复创建
                $percent = $('<div class="progress"></div>').appendTo($li);
            }
            $percent.text('已上传：' + parseInt(percentage * 100) + '%');
        }).on('uploadSuccess', function (file, response) {
            if(response._raw){
                var $li = $('#' + file.id);
                layer.msg("上传成功")
                myDataTable.fnDraw(false);
            }else {
                layer.msg("无权访问");
                myDataTable.fnDraw(false);
            }
        }).on('uploadError', function (file) {
            layer.msg("上传失败")
        }).on('error', function (type) {
            console.log(type)
            if (type == 'Q_EXCEED_NUM_LIMIT') {
                layer.msg('最多只能上传1个文档！', {icon: 2, time: 1000});
            }
            if (type == 'Q_TYPE_DENIED') {
                layer.msg('只能上传文档格式文件！', {icon: 2, time: 1000})
            }
        });
    }

    /*打开指导老师详情窗口*/
    function openGuideTeacherInfo(guideTeacher,guideTeacherEmail,guideTeacherPhone) {
        $('#guideTeacher2').html(guideTeacher);
        $('#guideTeacherEmail2').html(guideTeacherEmail);
        $('#guideTeacherPhone2').html(guideTeacherPhone);
        layer.open({
            type: 1,
            title: '指导老师信息',
            btn: ['确定'],
            area: ['400px', '250px'],
            content: $("#guideTeacherInfo")
        });
    }
