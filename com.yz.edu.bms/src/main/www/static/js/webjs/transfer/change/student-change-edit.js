$(function () {
    // findChangeGradeDict();
    //选择学员下拉框加载
    _simple_ajax_select({
        selectId: "slearnId",
        searchUrl: '/studentChange/findStudentInfo.do',
        sData: {},
        showText: function (item) {
            return item.stdName + " - " + item.grade + "级";
        },
        showId: function (item) {
            return item.learnId;
        },
        placeholder: '--学员--'
    });
    window.setInterval(showTime, 1000);

    // 设置层次
    _init_select("pfsnLevel", dictJson.pfsnLevel, $("#preventPfsnLevel").val());
    $("#pfsnId").select2({
        placeholder: "--请先选择院校--"
    });
    $("#taId").select2({
        placeholder: "--请先选专业--"
    });
    $("#grade").select2({
        placeholder: "--请先选年级--"
    });
    $("#scholarship").select2({
        placeholder: "--请先选年级--"
    });
    // 设置院校、专业、地区下拉框
    initWishSelects({
        unvsSelectId: 'unvsId',
        pfsnSelectId: 'pfsnId',
        taSelectId: 'taId',
        unvsSearchUrl: '/baseinfo/sUnvs.do',
        pfsnSearchUrl: '/baseinfo/sPfsn.do',
        taSearchUrl: '/baseinfo/sTa.do'
    });

    $("#pfsnLevel").on('change', function () {
        scholarshipSelectChange()
    });

    $("#taId").on('change', function () {
        feeTablesChange();
        scholarshipSelectChange();
    });

    $("#scholarship").on('change', function () {
        feeTablesChange();
    });

    $("#slearnId").on('change', function () {
        studentFind();
    });

    $('.skin-minimal input').iCheck({
        checkboxClass: 'icheckbox-blue',
        radioClass: 'iradio-blue',
        increaseArea: '20%'
    });

    $("#form-member-add").validate({
        rules: {
            pfsnLevel: {required: true},
            grade: {required: true},
            unvsId: {required: true},
            pfsnId: {required: true},
            taId: {required: true},
            scholarship: {required: true},
            oldStdStage: {required: true}
        },
        messages: {},
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: '/studentChange/addBdStudentChange.do', //请求url
                success: function (data) { //提交成功的回调函数
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                            layer_close();
                        });
                    }
                }
            })
        }
    });

    // 如果是查看转报信息
    if ($("#exType").val() == "UPDATE") {
        // 获取原报读信息
        $.ajax({
            type: 'POST',
            url: '/studentChange/findTransferByStdId.do',
            data: {
                learnId: $('#oldLearnId').val()
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    var oldInfo = data.body, recruitText = '';
                    if (oldInfo.empName) {
                        if (oldInfo.recruitCampusName) {
                            recruitText += '[' + oldInfo.recruitCampusName + '] — '
                        }
                        if (oldInfo.recruitDpName) {
                            recruitText += '[' + oldInfo.recruitDpName + '] — '
                        }
                        recruitText += oldInfo.empName + '(' + _findDict('empStatus', oldInfo.empStatus) + ')';
                        if(oldInfo.assignFlag=="1") recruitText+=" — 学校分配";
                        $("#oldRecruit").val(recruitText);
                    } else {
                        $("#oldRecruit").val('无');
                    }

                    // 设置原报读信息内容
                    $("#oldIdCard").val(oldInfo.idCard);
                   // $("#oldRecruit").val(oldInfo.empName);
                    $("#oldTaName").val(oldInfo.taName);
                    $("#oldInclusionStatus").val(oldInfo.inclusionStatus ? _findDict('inclusionStatus', oldInfo.inclusionStatus) : "--");
                    $("#oldGrade").val(_findDict("grade", $("#oldGrade").val()));
                    $("#oldScholarship").val(_findDict("scholarship", $("#oldScholarship").val()));
                    $("#oldPfsnLevel").val(_findDict("pfsnLevel", $("#oldPfsnLevel").val()));
                    $("#oldStdStage").val(_findDict("stdStage", $("#oldStdStage").val()));

                    //？？为什么设置两遍？？
                    //$("#oldRecruit").val($("#oldRecruit").val());
                    //$("#oldTaName").val($("#oldTaName").val());
                }
            }
        });

        // 设置转报内容显示值并禁用
        $("#pfsnLevel").prop("disabled", true);
        $("#unvsId").append(new Option($("#preventUnvsName").val(), $("#preventUnvsId").val(), true, true)).prop("disabled", true);
        $("#pfsnId").append(new Option($("#preventPfsnName").val(), $("#preventPfsnId").val(), true, true)).prop("disabled", true);
        $("#taId").append(new Option($("#preventTaName").val(), $("#preventTaId").val(), true, true)).change().prop("disabled", true);
        $("#grade").append(new Option($("#preventGrade").val(), $("#preventGrade").val(), true, true)).prop("disabled", true);
        $("#slearnId").append(new Option($("#stdName").val(), $("#stdName").val(), true, true)).prop("disabled", true);

        var preventScholarshipId= $("#preventScholarship").val()
        var preventScholarship = _findDict('scholarship', preventScholarshipId);
        $("#scholarship").append(new Option(preventScholarship, preventScholarshipId, true, true)).prop("disabled", true);
        $("#subButton").html("");


        var campusName = '', dpName = '', empStatus = '', recruitText = '', recruitName = recruitEmpInfo.recruitName;
        if (recruitName) {
            campusName = recruitEmpInfo.recruitCampusName;
            dpName = recruitEmpInfo.recruitDpName;
            empStatus = recruitEmpInfo.recruitStatus;
            if (campusName) {
                recruitText += '[' + campusName + '] — '
            }
            if (dpName) {
                recruitText += '[' + dpName + '] — '
            }
            recruitText += recruitName + '(' + _findDict('empStatus', empStatus) + ')';
            if(recruitEmpInfo.assignFlag=="1") recruitText+=" — 学校分配";
            $("#recruitName").text(recruitText);
        } else {
            $("#recruitName").text('无');
        }
        //if(recruitEmpInfo.assignFlag=="1") $("#assignFlag").attr("checked","true");
    }

});

function gradeChange() {
    init_pfsn_select("unvsId", "pfsnId");
    // 清空地区选中项，触发改变事件
    $("#taId").val(null).trigger("change");
    // 设置地区下拉框
    init_ta_select("pfsnId", "taId");
    // 设置优惠类型下拉框
    var scholarshipArr = []
    if ($("#grade").val() == '201809') {
        scholarshipArr = [{"dictValue": "1", "dictName": "普通全额", "dictId": "scholarship.1"}]
    } else {
        scholarshipArr = [{"dictValue": "23", "dictName": "圆梦奖学金", "dictId": "scholarship.23"}]
    }
    _init_select("scholarship", scholarshipArr, $("#preventScholarship").val());
}

function scholarshipSelectChange() {
    if($("#exType").val() == "UPDATE"){
        var preventScholarshipId= $("#preventScholarship").val()
        var preventScholarship = _findDict('scholarship', preventScholarshipId);
        console.log(preventScholarship);
        $("#scholarship").append(new Option(preventScholarship, preventScholarshipId, true, true)).prop("disabled", true);
        feeTablesChange();
        return
    }

    var oldScholarship= $("#oldScholarship").val();
    // 是否有修改优惠类型权限
    var ifCanUpdate=$("#ifCanUpdate").val();

    $.ajax({
        url: '/recruit/getScholarships.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#pfsnId").val(),
            taId: $("#taId").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var dictArray = [];
                if (data.body) {
                    $.each(data.body, function (index, s) {
                        var name = _findDict('scholarship', s);
                            dictArray.push({
                                'dictValue': s,
                                'dictName': name
                            });
                    });
                }
                var sv;
                if (dictArray.length > 0) {
                    sv = dictArray[0].dictValue;
                }
                _init_select("scholarship", dictArray, sv);
            }
            feeTablesChange();
        }
    });
}

function findChangeGradeDict(grade) {
    var isSuper = $("#isSuper").val();

    // 跟超级管理员有什么关系？？
    // $("#grade").empty();
    // if (isSuper != '1') {
    //     $("#grade").append("<option value='201809'>201809</option>");
    //     $("#grade").select2({
    //         placeholder: "--请选择年级--",
    //         allowClear: true,
    //         multiple: false
    //     });
    //     return;
    // }
    var groupList = dictJson['grade'];
    $.ajax({
        type: 'POST',
        url: '/studentChange/findStudentGrade.do',
        async: false,
        data: {
            grade: grade
        },
        dataType: 'json',
        success: function (data) {
            for (var i = 0; i < data.body.length; i++) {
                var tarGrade = data.body[i];
                // 成教限制201809，国开限制2019报读
                if (tarGrade == '201809' || tarGrade == '2019') {
                    $("#grade").append("<option value='" + tarGrade + "' index=" + i + ">" + tarGrade + "级" + "</option>");
                }
            }
            $("#grade").select2({
                placeholder: "--请选择年级--",
                allowClear: true,
                multiple: false
            });
            $("#grade").change();
        }
    });
    return '';
}

var timeTemp = $("#createTime").val();
function showTime() {
    if (!timeTemp) {
        $("#createTime").val(new Date().format('yyyy-MM-dd hh:mm:ss'));
    } else {
        $("#createTime").val(new Date($("#createTime").val()).format('yyyy-MM-dd hh:mm:ss'));
    }
}

function initWishSelects(config) {
    // 设置第一志愿院校下拉框
    _simple_ajax_select({
        selectId: config.unvsSelectId,
        searchUrl: config.unvsSearchUrl,
        sData: {},
        showText: function (item) {
            var text = '[' + _findDict('recruitType', item.recruitType) + ']';
            text += item.unvsName + '(' + item.unvsCode + ')';
            return text;
        },
        showId: function (item) {
            return item.unvsId;
        },
        placeholder: '--请选择院校--'
    });

    // 设置第一志愿院校下拉框改变事件
    $("#" + config.unvsSelectId).change(function () {
        $("#" + config.pfsnSelectId).removeAttr("disabled");
        // 设置第一志愿专业下拉框
        init_pfsn_select(config.unvsSelectId, config.pfsnSelectId);
    });

    // 设置第一志愿专业下拉框改变事件
    $("#" + config.pfsnSelectId).change(function () {
        $("#" + config.taSelectId).removeAttr("disabled");
        // 设置第一志愿地区下拉框
        init_ta_select(config.pfsnSelectId, config.taSelectId);
    });
}

var feeTablesChange = function () {
    $.ajax({
        url: '/studentChange/showFeeList.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#pfsnId").val(),
            taId: $("#taId").val(),
            scholarship: $("#scholarship").val(),
            unvsId: $("#unvsId").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                if (null == data.body || null == data.body.feeInfo) {
                    $('#feeTable tbody').html("");
                    return;
                }
                var feeList = data.body.feeList;
                var feeInfo = data.body.feeInfo;
                if (feeList) {
                    $('#feeTable tbody').empty();
                    for (var i = 0; i < feeList.length; i++) {
                        var info = feeList[i];
                        var dom = "<tr class='text-c'>";
                        var itemName = getItemName(info.itemName, $("#grade").val());
                        dom += "<td>" + info.itemCode + ":" + itemName + "</td>";
                        dom += "<td>" + info.amount + "</td>";
                        dom += "<td>" + info.discount + "</td>";
                        dom += "<td>" + (parseFloat(info.amount) - parseFloat(info.discount).toFixed(2)) + "</td>";
                        dom += "</tr>";
                        $('#feeTable tbody').append(dom);
                    }
                }
                if (feeInfo) {
                    $("#feeId").val(feeInfo.feeId);
                    $("#offerId").val(feeInfo.offerId);
                    $("#feeName").text(feeInfo.feeName ? feeInfo.feeName : '无');
                    $("#offerName").text(feeInfo.offerName ? feeInfo.offerName : '无');
                    $("#offerRemark").text(feeInfo.offerRemark ? feeInfo.offerRemark : '无');
                }
            }
        }
    });
};

function init_ta_select(pfsnSelectId, taSelectId) {
    _simple_ajax_select({
        selectId: taSelectId,
        searchUrl: '/baseinfo/sTa.do',
        sData: {
            sId: function () {
                return $("#" + pfsnSelectId).val();
            }
        },
        showText: function (item) {
            var text = '[' + item.taCode + ']' + item.taName;
            return text;
        },
        showId: function (item) {
            return item.taId;
        },
        placeholder: '--请选择考区--'
    });
}

function studentFind(slearnId) {
    if (!$("#slearnId").val()) {
        $(".modifyContent input,.modifyContent select").val("");
        return;
    }
    $.ajax({
        type: 'POST',
        url: '/studentChange/findTransferByStdId.do',
        data: {
            learnId: $("#slearnId").val() || slearnId
        },
        dataType: 'json',
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                result = data.body;
                var dom = "";

                $("#stdId").val(result.stdId);

                var history = $("#history");
                if (null != history) {
                    history.children().remove();
                }
                var grade = 0;
                if (null != result) {
                    grade = result.grade;
                    var empName = result.empName ?　result.empName : "", recruitText = "";
                    if(empName != ""){
                        recruitText = '['+result.recruitCampusName+'] — ['+result.recruitDpName+']—'+empName+'('+_findDict('empStatus', result.empStatus)+')';
                        if(result.assignFlag=="1") recruitText += " — 学校分配";
                    }

                    $("#oldLearnId").val(result.learnId);
                    dom += '<div class="row cl" style="margin: 0px;padding: 0px;">' + '   <label class="form-label col-xs-4 col-sm-3">年级：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">' + '     <input type="text" class="input-text" value="'
                        + _findDict('grade', result.grade)
                        + '" id="oldGrade" style="border: 0;" readonly="readonly">'
                        + '   </div><div id="oldGradeNo" type="hidden" value="' + result.grade + '"/>'
                        + ' </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">身份证号：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text" value="'
                        + result.idCard
                        + '" id="oldIdCard" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">优惠类型：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text" value="'
                        + _findDict("scholarship", result.scholarship)
                        + '" id="oldScholarship" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">入围状态：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text" value="'
                        + _findDict("inclusionStatus", result.inclusionStatus)
                        + '" id="oldInclusionStatus" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">层次：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text" value="'
                        + _findDict("pfsnLevel", result.pfsnLevel)
                        + '" id="oldPfsnLevel" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">院校：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text"  value="' + result.unvsName + '"  id="oldUnvsName" style="border: 0;" readonly="readonly"'
                        + '   </div>'
                        + ' </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">专业：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text"  value="' + result.pfsnName + '" id="oldPfsnName" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">学员阶段：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '<input type="hidden" name="oldStdStage" value="' + result.stdStage + '" />'
                        + '     <input type="text" class="input-text" value="'
                        + _findDict("stdStage", result.stdStage)
                        + '" id="oldStdStage" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">招生老师：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text" value="'
                        + recruitText
                        + '" id="oldTaName" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' <div class="row cl"  style="margin: 0px;padding: 0px;">'
                        + '   <label class="form-label col-xs-4 col-sm-3">考试县区：</label>'
                        + '   <div class="formControls col-xs-8 col-sm-9">'
                        + '     <input type="text" class="input-text" value="'
                        + result.taName
                        + '" id="oldTaName" style="border: 0;" readonly="readonly">'
                        + '   </div>'
                        + ' </div>';

                    $("#recruitName").text(recruitText);
                    $("#recruit").val(result.recruit);
                    $("#recruitDpId").val(result.recruitDpId);
                    $("#recruitCampusId").val(result.recruitCampusId);
                    $("#recruitCampusManager").val(result.recruitCampusManager);
                    $("#assignFlag").val(result.assignFlag);

                }
                history.append(dom);
                findChangeGradeDict(grade);
            }
        }
    });
}

function init_pfsn_select(unvsSelectId, pfsnSelectId) {
    _simple_ajax_select({
        selectId: pfsnSelectId,
        searchUrl: '/baseinfo/sPfsn.do',
        sData: {
            sId: function () {
                return $("#" + unvsSelectId).val();
            },
            ext1: function () {
                return $("#pfsnLevel").val();
            },
            ext2: function () {
                return $("#grade").val();
            }
        },
        showText: function (item) {
            var text = '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']' + '-[' + _findDict('year', item.year) + ']';
            text += item.pfsnName + '(' + item.pfsnCode + ')';
            return text;
        },
        showId: function (item) {
            return item.pfsnId;
        },
        placeholder: '--请选择专业--'
    });
}

function feeStandard() {
    $.ajax({
        url: '/studentChange/showFeeList.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#pfsnId").val(),
            taId: $("#taId").val(),
            scholarship: $("#scholarship").val(),
            unvsId: $("#unvsId").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                if (null == data.body || null == data.body.feeInfo) {
                    $('#feeTable tbody').html("");
                    return;
                }
                var feeList = data.body.feeList;
                var feeInfo = data.body.feeInfo;
                if (feeList) {
                    $('#feeTable tbody').empty();
                    for (var i = 0; i < feeList.length; i++) {
                        var info = feeList[i];
                        var dom = "<tr class='text-c'>";
                        var itemName = getItemName(info.itemName, $("#grade").val());
                        dom += "<td>" + info.itemCode + ":" + itemName + "</td>";
                        dom += "<td>" + info.amount + "</td>";
                        dom += "<td>" + info.discount + "</td>";
                        dom += "<td>" + (parseFloat(info.amount) - parseFloat(info.discount).toFixed(2)) + "</td>";
                        dom += "</tr>";
                        $('#feeTable tbody').append(dom);
                    }
                }
                if (feeInfo) {
                    $("#feeId").val(feeInfo.feeId);
                    $("#offerId").val(feeInfo.offerId);
                    $("#feeName").text(feeInfo.feeName ? feeInfo.feeName : '无');
                    $("#offerName").text(feeInfo.offerName ? feeInfo.offerName : '无');
                    $("#offerRemark").text(feeInfo.offerRemark ? feeInfo.offerRemark : '无');
                }
            }
        }
    });
}


function showDistrictBox(type) {
    var toEmpListUrl = '/recruit/toEmpList.do';
    layer_show('学员分配', toEmpListUrl + '?type=' + type + '&learnId=' + learnId, null, null, function () {
    }, true);
}