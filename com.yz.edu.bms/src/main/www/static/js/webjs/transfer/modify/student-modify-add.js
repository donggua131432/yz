$(function () {
    //学员
    _simple_ajax_select({
        selectId: "learnId",
        searchUrl: '/studentModify/findStudentInfo.do',
        sData: {},
        showText: function (item) {
            return item.stdName + " - " + item.grade + "级";
        },
        showId: function (item) {
            return item.learnId;
        },
        placeholder: '--学员--'
    });

    $("#newPfsnLevel").change();
    $("#newPfsnId").change();
    $("#newTaId").change(function () {
        if ($(this).val()) {
            scholarshipSelectChange();
        }
    });
    console.log($("#ifCanUpdate").val());
    //年级事件
    $("#learnId").change(function () {
        if ($("#learnId").select2('data')[0].text.indexOf("2018级") != -1) {
            $("#learnId").val('')
            layer.msg('暂时禁止18级学员异动提交!', {icon: 0, time: 2500}, function () {
                $("#learnId").val(null).trigger("change");
                $(".modifyContent input,.modifyContent select").val("");
                $("#stdStage").val("");
            });
            return
        }
        $.post('/studentModify/findStudentEnrollInfo.do', {
            learnId: $("#learnId").val(),
        }, function (data) {
            data = data.body;
            if (null != data) {
                $("#newUnvsId").val(null).trigger("change");
                $("#pfsnLevels2").val(null).trigger("change");
                $("#pfsnLevels2").attr("disabled", "disabled");
                $("#newPfsnId").val(null).trigger("change");
                $("#newPfsnId").attr("disabled", "disabled");
                $("#newTaId").val(null).trigger("change");
                $("#newTaId").attr("disabled", "disabled");
                //学员状态
                $("#stdStage").val(_findDict("stdStage", data.stdStage));
                //姓名
                $("#stdName").val(data.stdName);
                //身份证号码
                $("#idCard").val(data.idCard);
                //性别
                $("#sexv").val(_findDict("sex", data.sex));
                $("#sex").val(data.sex);
                //民族
                $("#nationv").val(_findDict("nation", data.nation));
                $("#nation").val(data.nation);
                //志愿
                $("#zyUnvsId").val(data.unvsName + "(" + data.pfsnCode + ")");
                $("#zyPfsnName").val("(" + data.pfsnCode + ")" + data.pfsnName + "[" + _findDict("grade", data.grade) + "]");
                $("#zyPfsnLevel").val(_findDict("pfsnLevel", data.pfsnLevel));
                $("#unvsId").val(data.unvsId);
                $("#pfsnId").val(data.pfsnId);
                //考区
                $("#taIdv").val(data.taName);
                $("#taId").val(data.taId);
                //优惠类型
                $("#scholarshipv").val(_findDict("scholarship", data.scholarship));
                $("#scholarship").val(data.scholarship);
                // var dictArray = [];
                // var oldScholarship = _findDict('scholarship', $("#scholarship").val());
                // dictArray.push({
                //     'dictValue': $("#scholarship").val(),
                //     'dictName': oldScholarship
                // });
                // _init_select("newScholarship", dictArray, $("#scholarship").val());

                $("#stdId").val(data.stdId);
                $("#grade").val(data.grade);
            }
        }, "json");
    });

    //性别
    _init_select("newSex", dictJson.sex);

    //民族
    _init_select("newNation", dictJson.nation);

    //院校
    _simple_ajax_select({
        selectId: "newUnvsId",
        searchUrl: '/baseinfo/sUnvs.do',
        sData: {},
        showText: function (item) {
            return item.unvsName;
        },
        showId: function (item) {
            return item.unvsId;
        },
        placeholder: '--请选择院校--'
    });

    //专业层次
    _init_select("pfsnLevels2", dictJson.pfsnLevel, "", "--请选择层次--");

    //初始化专业
    $("#newPfsnId").select2({
        placeholder: "--请选择专业--",
        allowClear: true
    });

    //专业层次事件
    $("#pfsnLevels2").change(function () {
        var pfsnLevels2 = $("#pfsnLevels2").val();
        $("#newPfsnId").val(null).trigger("change");
        $("#newTaId").attr("disabled", "disabled");
        $("#newTaId").val(null).trigger("change");
        $("#newPfsnId").removeAttr("disabled");
        flushPfns();
    });

    //院校事件
    $("#newUnvsId").change(function () {
        var newUnvsId = $("#newUnvsId").val();
        $("#pfsnLevels2").val(null).trigger("change");
        $("#newPfsnId").val(null).trigger("change");
        $("#newPfsnId").attr("disabled", "disabled");
        $("#newTaId").val(null).trigger("change");
        $("#newTaId").attr("disabled", "disabled");
        $("#pfsnLevels2").removeAttr("disabled");
    });

    //专业事件
    $("#newPfsnId").change(function () {
        var newPfsnId = $("#newPfsnId").val();
        $("#newTaId").val(null).trigger("change");
        $("#newTaId").removeAttr("disabled");

        $("#newScholarship").val(null).trigger("change");
        $("#newScholarship").removeAttr("disabled");
        //考区
        _simple_ajax_select({
            selectId: "newTaId",
            searchUrl: '/testArea/findBdTestAreaByPfsnId.do',
            sData: {
                pfsnId: $("#newPfsnId").val()
            },
            showText: function (item) {
                return item.taName;
            },
            showId: function (item) {
                return item.taId;
            },
            placeholder: '--请选择考区--'
        });
    });

    //_init_select("newScholarship", dictJson.scholarship, "", "---请选择优惠类型---");
    _init_select("newTaId", "", "", "---请选择考区---");
    _init_select("newScholarship", null, null);

    $("#form-member-add").validate({
        onkeyup: false,
        focusCleanup: true,
        rules: {
            stdId: {
                required: true
            },
            learnId: {
                required: true
            }
        },
        success: true,
        submitHandler: function (form) {
            if (!checkEnroll()) {
                return false;
            }
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: '/studentModify/insertStudentModify.do', //请求url
                success: function (data) { //提交成功的回调函数
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                            window.parent.myDataTable.fnDraw(true);
                            layer_close();
                        });
                    }
                }
            });
        }
    });


    $('#modify').on('click', '.btn-modify', function () {
        // console.log($("#learnId").val())
        if (!$("#learnId").val() || $("#learnId").val() == 'null') {
            layer.msg("请选择学员");
            return;
        }
        var calssName = $(this).data('classname');
        if (calssName) {
            $('.' + calssName).css('display', 'inline');
        }
        $(this).hide().siblings('div').css('display', 'inline');
    })
});

function checkEnroll() {
    var newUnvsId = $("#newUnvsId").val();
    var pfsnLevels2 = $("#pfsnLevels2").val();
    var newPfsnId = $("#newPfsnId").val();
    var newTaId = $("#newTaId").val();
    var newScholarship = $("#newScholarship").val();
    if (newUnvsId || pfsnLevels2 || newPfsnId || newTaId) {
        return checkEmpty(newUnvsId, pfsnLevels2, newPfsnId, newTaId, newScholarship);
    } else {
        return true;
    }

}

function checkEmpty(newUnvsId, pfsnLevels2, newPfsnId, newTaId, newScholarship) {
    if (!newUnvsId) {
        layer.msg('院校不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!pfsnLevels2) {
        layer.msg('专业层次不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!newPfsnId) {
        layer.msg('专业不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!newTaId) {
        layer.msg('考区不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    if (!newScholarship) {
        layer.msg('优惠类型不能为空！', {
            icon: 2,
            time: 1000
        });
        return false;
    }
    return true;
}

function flushPfns() {
    //专业
    _simple_ajax_select({
        selectId: "newPfsnId",
        searchUrl: '/unvsProfession/searchProfessionJson.do',
        sData: {
            unvsId: $("#newUnvsId").val(),
            pfsnLevel: $("#pfsnLevels2").val(),
            grade: $("#grade").val()
        },
        showText: function (item) {
            return item.text;
        },
        showId: function (item) {
            return item.id;
        },
        placeholder: '--请选择专业--'
    });
}

function scholarshipSelectChange() {
    var oldScholarship= $("#scholarship").val();
    // 是否有修改优惠类型权限
    var ifCanUpdate=$("#ifCanUpdate").val();

    // 如果有权限
    if(ifCanUpdate=='true'){
        _init_select("newScholarship", dictJson.scholarship, oldScholarship);
    }else {
        var name = _findDict('scholarship', oldScholarship);
        _init_select("newScholarship", [{'dictValue': oldScholarship,'dictName': name}], oldScholarship);
        $("#newScholarship").attr('disabled','true');
    }
    return
    $.ajax({
        url: '/recruit/getScholarships.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#newPfsnId").val(),
            taId: $("#newTaId").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var dictArray = [];
                var oldScholarship = $("#scholarship").val();

                if (data.body) {
                    $.each(data.body, function (index, s) {
                        var name = _findDict('scholarship', s);
                        // 有修改优惠类型权限的加载全部，没有则只加载存在的老优惠类型
                        if(ifCanUpdate=='true'){
                            dictArray.push({
                                'dictValue': s,
                                'dictName': name
                            });
                        }else {
                            if(name==oldScholarship){
                                dictArray.push({
                                    'dictValue': s,
                                    'dictName': name
                                });
                            }
                        }
                    });
                }
                var sv;
                if (dictArray.length > 0) {
                    sv = dictArray[0].dictValue;
                }
                _init_select("newScholarship", dictArray, sv);
            }
        }
    });
}