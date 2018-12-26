$(function () {
    var stdStage = enroll.stdStage;
    if ('1' == stdStage) {
        var pfsnLevelBox = '';
        pfsnLevelBox += '<select class="select" id="pfsnLevel" name="pfsnLevel" style="width: 100%">';
        pfsnLevelBox += '</select>';
        $("#pfsnLevelBox").html(pfsnLevelBox);

        var firstBox = '';
        firstBox += '<select class="select" id="unvsId" name="unvsId" style="width: 30%"></select>';
        firstBox += '<select id="pfsnId" name="pfsnId" style="width: 30%" disabled="disabled"></select>';
        firstBox += '<select class="select" id="taId" name="taId" style="width: 30%" disabled="disabled"></select>';
        $("#firstBox").html(firstBox);

        var secondBox = '';
        secondBox += '<select class="select" id="secUnvsId" name="secUnvsId" style="width: 30%"></select>';
        secondBox += '<select id="secPfsnId" name="secPfsnId" style="width: 30%" disabled="disabled"></select>';
        secondBox += '<select class="select" id="secTaId" name="secTaId" style="width: 30%" disabled="disabled"></select>';
        $("#secondBox").html(secondBox);

        var scholarshipBox = '';
        scholarshipBox += '<select class="select" id="scholarship" name="scholarship" style="width: 100%">';
        scholarshipBox += '</select>';
        $("#scholarshipBox").html(scholarshipBox);

        var grade = enroll.grade;
        var gradeBox = '';
        gradeBox += '<input type="text" class="input-text"  id="grade" value="' + grade + '" disabled="disabled">';
        $("#gradeBox").html(gradeBox);
        /* $("#pfsnLevel").rules("add", {required: true});
        $("#unvsId").rules("add", {required: true});
        $("#pfsnId").rules("add", {required: true});
        $("#taId").rules("add", {required: true}); */


        var unvsSearchUrl = '/baseinfo/sUnvs.do', pfsnSearchUrl = '/baseinfo/sPfsn.do',
            taSearchUrl = '/baseinfo/sTa.do',
            unvsSearchUrl = '/baseinfo/sUnvs.do', pfsnSearchUrl = '/baseinfo/sPfsn.do',
            taSearchUrl = '/baseinfo/sTa.do',
            unvsName = enroll.unvsName, unvsId = enroll.unvsId,
            unvsSearchUrl = '/baseinfo/sUnvs.do', pfsnSearchUrl = '/baseinfo/sPfsn.do', unvsName = enroll.secUnvsName,
            unvsId = enroll.secUnvsId


        $("#pfsnLevel").change(function () {
            init_pfsn_select("unvsId", "pfsnId");
            init_ta_select("pfsnId", "taId");
            scholarshipSelectChange();
            initWishSelects({
                unvsSelectId: 'unvsId',
                pfsnSelectId: 'pfsnId',
                taSelectId: 'taId',
                unvsSearchUrl: unvsSearchUrl,
                pfsnSearchUrl: pfsnSearchUrl,
                taSearchUrl: taSearchUrl
            });
        });

        initWishSelects({
            unvsSelectId: 'unvsId',
            pfsnSelectId: 'pfsnId',
            taSelectId: 'taId',
            unvsSearchUrl: unvsSearchUrl,
            pfsnSearchUrl: pfsnSearchUrl,
            taSearchUrl: taSearchUrl,
            unvsName: unvsName,
            unvsId: unvsId
        });

        initWishSelects({
            unvsSelectId: 'secUnvsId',
            pfsnSelectId: 'secPfsnId',
            taSelectId: 'secTaId',
            unvsSearchUrl: unvsSearchUrl,
            pfsnSearchUrl: pfsnSearchUrl,
            unvsName: unvsName,
            unvsId: unvsId
        });

        _init_select("pfsnLevel", dictJson.pfsnLevel, enroll.pfsnLevel);

        $("#pfsnId").change(scholarshipSelectChange);
        $("#taId").change(scholarshipSelectChange);
        
        $("#scholarshipBox").on('change','#scholarship',function(){
            feeTablesChange();
        });
        
        $("#pfsnId").select2({
            placeholder: "--请先选择第一志愿院校--"
        });
        $("#taId").select2({
            placeholder: "--请先选择第一志愿专业--"
        });
        $("#secPfsnId").select2({
            placeholder: "--请先选择第二志愿院校--"
        });
        $("#secTaId").select2({
            placeholder: "--请先选择第二志愿专业--"
        });
        $("#scholarship").select2({
            placeholder: "--请选择收费标准--"
        });
        var scholarship = enroll.scholarship;
        $("#scholarship").append("<option value='" + scholarship + "' selected='selected'>" + _findDict('scholarship', scholarship) + "</option>");
        $("#unvsId").append("<option value='" + enroll.unvsId + "' selected='selected'>" + enroll.unvsName + "</option>");
        $("#pfsnId").append("<option value='" + enroll.pfsnId + "' selected='selected'>" + enroll.pfsnName + "</option>");
        $("#taId").append("<option value='" + enroll.taId + "' selected='selected'>" + enroll.taName + "</option>");
        if (enroll.secPfsnId) {
            $("#secPfsnId").append("<option value='" + enroll.secPfsnId + "' selected='selected'>" + enroll.secPfsnName + "</option>");
        }
        if (enroll.secTaId) {
            $("#secTaId").append("<option value='" + enroll.secTaId + "' selected='selected'>" + enroll.secTaId + "</option>");
        }
        //console.log('111111111111');
    } else {
        $("#pfsnLevel").text(_findDict('pfsnLevel', enroll.pfsnLevel));
        $("#grade").text(_findDict('grade', enroll.grade));

        var wish = '';
        if (enroll.unvsName) {
            wish += enroll.unvsName || '' + '(' + enroll.unvsCode || '' + ') / ';
        }
        if (enroll.pfsnLevel) {
            wish += '[' + _findDict('pfsnLevel', enroll.pfsnLevel) + ']' + '-';
        }
        if (enroll.grade) {
            wish += '[' + _findDict('grade', enroll.grade) + ']';
        }
        if (enroll.pfsnName) {
            wish += enroll.pfsnName + '(' + enroll.pfsnCode + ')  ';
        }
        if (enroll.taName) {
            wish += '/ ' + enroll.taName || '' + '(' + enroll.taCode + ')';
        }

        $("#wish").text(wish);

        var secWishText = '';
        if (enroll.secUnvsName) {
            secWishText += enroll.secUnvsName + '(' + enroll.secUnvsCode + ') / ';
            secWishText += '[' + _findDict('pfsnLevel', enroll.pfsnLevel) + ']' + '-[' + _findDict('grade', enroll.grade) + ']';
            secWishText += enroll.secPfsnName + '(' + enroll.secPfsnCode + ') / ';
            secWishText += enroll.secTaName + '(' + enroll.secTaCode + ')';
        }

        if (secWishText) {
            $("#sec_wish").text(secWishText);
        } else {
            $("#sec_wish").text('无');
        }

        $("#scholarship").text(_findDict('scholarship', enroll.scholarship));

    }
    _init_select("enrollType", dictJson.enrollType, enroll.enrollType);

    _init_radio_box('bpTypeBox', 'bpType', dictJson.bpType, enroll.bpType);

    $("#points").val(enroll.points);

    var inclusionStatus = enroll.inclusionStatus;
    if (inclusionStatus != null) {
        $("#scholarshipBox").append('&nbsp;&nbsp;<span>[' + _findDict('inclusionStatus', enroll.inclusionStatus) + ']</span>');
    }

    var rulesArray = {
        pfsnId: {required: true},
        unvsId: {required: true},
        scholarship: {required: true},
        taId: {required: true}
    };
    if ($("#recruitType").val() === '1') {
        rulesArray.bpTypeBox = {required: true, maxlength: 25};
        rulesArray.points = {required: true};
    }

    $("#form-recruit-info").validate({
        rules: rulesArray,
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {

            var updateRecruitUrl = '/' + updateUrl;

            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: updateRecruitUrl, //请求url
                success: function (data) { //提交成功的回调函数
                    if ('00' == data.code) {
                        layer.msg('学员报读信息保存成功', {
                            icon: 1,
                            time: 1000
                        });
                    }
                }
            });
        }
    });

    var feeInfo = feeInfo_global.feeInfo;
    if (feeInfo) {
        $("#offerRemark").text(feeInfo_global.offerRemark ? feeInfo_global.offerRemark : '无');
    }

    $("#fee_total").text(feeInfo_global.feeTotal);
    var feeList = feeInfo_global.feeList;
    if (feeList) {
        $.each(feeList, function (index, data) {

            var _feeAmount = data.amount;
            if (_feeAmount) {
                var dom = '<tr class="text-l">';
                var itemName = getItemName(data.itemName, $("#grade").text());
                dom += '<td>' + data.itemCode + ':' + itemName + '</td>';
                dom += '<td>' + data.amount + '</td>';
                dom += '<td>' + data.discount + '</td>';
                dom += '<td>' + data.payable + '</td>';
                dom += '</tr>';
                $("#feeTable").find("tbody").append(dom);
            }
        });
    } else {
        $("#feeTable").find("tbody").append('<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>');
    }

    $("#stdNo").text(recruitEmpInfo.stdNo ? recruitEmpInfo.stdNo : '无');
    $("#schollRoll").text(recruitEmpInfo.schollRoll ? recruitEmpInfo.schollRoll : '无');
    $("#stdStage").text(_findDict('stdStage', recruitEmpInfo.stdStage));


    var campusName = '';
    var dpName = '';
    var empStatus = '';
    var recruitText = '';
    var recruitName = recruitEmpInfo.recruitName;
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
        $("#recruitName").text(recruitText);
    } else {
        $("#recruitName").text('无');
    }

    var tutorText = '';
    var tutorName = recruitEmpInfo.tutorName;
    if (tutorName) {
        campusName = recruitEmpInfo.tutorCampusName;
        dpName = recruitEmpInfo.tutorDpName;
        empStatus = recruitEmpInfo.tutorStatus;
        if (campusName) {
            tutorText += '[' + campusName + '] — '
        }
        if (dpName) {
            tutorText += '[' + dpName + '] — '
        }
        tutorText += tutorName + '(' + _findDict('empStatus', empStatus) + ')';
        $("#tutorName").text(tutorText);
    } else {
        $("#tutorName").text('无');
    }
    if (parseInt(recruitEmpInfo.stdStage) >= 6) {
        $("#tutorName").siblings("input").show();
    } else {
        $("#tutorName").siblings("input").hide();
    }
    $("#createTime").text(recruitEmpInfo.createTime);
});

function showDistrictBox(type) {
    var toEmpListUrl = '/recruit/toEmpList.do';
    layer_show('学员分配', toEmpListUrl + '?type=' + type + '&learnId=' + enroll.learnId, null, null, function () {
    }, true);
}

function scholarshipSelectChange() {
    var getScholarshipsUrl = '/recruit/getScholarships.do';
    $.ajax({
        url: getScholarshipsUrl,
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
                    var recruitType = $("#recruitType").val();
                    $.each(data.body, function (index, s) {
                        if (s) {
                            var name = _findDict('scholarship', s);
                         
                                dictArray.push({
                                    'dictValue': s,
                                    'dictName': name
                                });
                      
                        }
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

var feeTablesChange = function () {
    $('#feeTable tbody').empty();
    $("#fee_total").text("0.00");
    $("#offerRemark").text('');
    var showFeeListUrl = '/recruit/showFeeList.do';
    $.ajax({
        url: showFeeListUrl,
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#pfsnId").val(),
            taId: $("#taId").val(),
            scholarship: $("#scholarship").val(),
            recruitType: $("#recruitType").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                if (data.body) {
                    $('#feeTable tbody').empty();
                    $("#fee_total").text("0.00");
                    var feeList = data.body.feeList;
                    var feeInfo = data.body.feeInfo;
                    var feeTotal = data.body.feeTotal;
                    if (feeList) {
                        for (var i = 0; i < feeList.length; i++) {
                            var info = feeList[i];
                            var _feeAmount = parseFloat(info.amount);
                            var dom = "<tr class='text-l'>";
                            var itemName = getItemName(info.itemName, $("#grade").val());
                            dom += "<td>" + info.itemCode + ":" + itemName + "</td>";
                            dom += "<td>" + info.amount + "</td>";
                            dom += "<td>" + info.discount + "</td>";
                            dom += "<td>" + info.payable + "</td>";
                            dom += "</tr>";
                            if (_feeAmount > 0) {
                                $('#feeTable tbody').append(dom);
                            }
                        }
                        $("#fee_total").text(feeTotal);
                    } else {
                        $('#feeTable tbody').append('<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty">' +
                            '<div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>');
                    }
                    if (feeInfo) {
                        $("#feeId").val(feeInfo.feeId);
                        $("#offerId").val(feeInfo.offerId);
                        $("#offerRemark").text(feeInfo.offerRemark ? feeInfo.offerRemark : '无');
                    }
                }
            }
        }
    });
};

function initWishSelects(config) {
    _simple_ajax_select({
        selectId: config.unvsSelectId,
        searchUrl: config.unvsSearchUrl,
        sData: {
            ext1: $("#recruitType").val()
        },
        showText: function (item) {
            var text = '[' + _findDict('recruitType', item.recruitType) + ']';
            text += item.unvsName + '(' + item.unvsCode + ')';
            return text;
        },
        showId: function (item) {
            return item.unvsId;
        },
        checkedInfo: {
            name: config.unvsName,
            value: config.unvsId
        },
        placeholder: '--请选择院校--'
    });

    $("#" + config.unvsSelectId).change(function () {
        $("#" + config.pfsnSelectId).removeAttr("disabled");
        init_pfsn_select(config.unvsSelectId, config.pfsnSelectId);
    });

    $("#" + config.pfsnSelectId).change(function () {
        $("#" + config.taSelectId).removeAttr("disabled");
        init_ta_select(config.pfsnSelectId, config.taSelectId);
    });
}

function init_pfsn_select(unvsSelectId, pfsnSelectId) {
    var sPfsnUrl = '/baseinfo/sPfsn.do';
    _simple_ajax_select({
        selectId: pfsnSelectId,
        searchUrl: sPfsnUrl,
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
            var text = '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']' + '-['
                + _findDict('year', item.year) + ']';
            text += item.pfsnName + '(' + item.pfsnCode + ')';
            return text;
        },
        showId: function (item) {
            return item.pfsnId;
        },
        placeholder: '--请选择专业--'
    });
}

function init_ta_select(pfsnSelectId, taSelectId) {
    var sTaUrl = '/baseinfo/sTa.do';
    _simple_ajax_select({
        selectId: taSelectId,
        searchUrl: sTaUrl,
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