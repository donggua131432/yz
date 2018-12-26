$(function () {
        //学员
        _simple_ajax_select({
            selectId: "learnId",
            searchUrl: '/studentRoll/findStudentInfo.do',
            sData: {},
            showText: function (item) {
                return item.stdName + " - " + item.grade + "级";
            },
            showId: function (item) {
                return item.learnId;
            },
            placeholder: '--学员--'
        });

        //院校
        _simple_ajax_select({
            selectId: "newUnvsId",
            searchUrl: '/bdUniversity/searchUniversity.do',
            sData: {},
            showText: function (item) {
                return item.text;
            },
            showId: function (item) {
                return item.id;
            },
            placeholder: '--请选择院校--'
        });
        //专业层次
        _init_select("pfsnLevels2", dictJson.pfsnLevel, "", "--请选择层次--");

        //年级事件
        $("#learnId").change(function () {
//            if($("#learnId").select2('data')[0].text.indexOf("2018级")!=-1){
//                layer.msg('暂时禁止18级学员异动提交!', {icon: 0, time: 2500}, function () {
//                    $("#learnId").val(null).trigger("change");
//                    $(".modifyContent input,.modifyContent select").val("");
//                    $("#stdStage").val("");
//                });
//                return
//            }
            $.post('/studentRoll/findStudentEnrollInfo.do', {
                learnId: $("#learnId").val()
            }, function (data) {
                data = data.body;
                $("#newUnvsId").val(null).trigger("change");
                $("#pfsnLevels2").val(null).trigger("change");
                $("#pfsnLevels2").attr("disabled", "disabled");
                $("#newPfsnId").val(null).trigger("change");
                $("#newPfsnId").attr("disabled", "disabled");
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
                $("#stdId").val(data.stdId);
                $("#grade").val(data.grade);
            }, "json");
        });

        //性别
        _init_select("newSex", dictJson.sex);

        //民族
        _init_select("newNation", dictJson.nation);


        //初始化专业
        $("#newPfsnId").select2({
            placeholder: "--请选择专业--",
            allowClear: true
        });

        //专业层次事件
        $("#pfsnLevels2").change(function () {
            var pfsnLevels2 = $("#pfsnLevels2").val();
            $("#newPfsnId").val(null).trigger("change");
            $("#newPfsnId").removeAttr("disabled");
            flushPfns();
        });

        //院校事件
        $("#newUnvsId").change(function () {
            var newUnvsId = $("#newUnvsId").val();
            $("#pfsnLevels2").val(null).trigger("change");
            $("#newPfsnId").val(null).trigger("change");
            $("#newPfsnId").attr("disabled", "disabled");
            $("#pfsnLevels2").removeAttr("disabled");
        });


        $("#form-member-add").validate({
            onkeyup: false,
            focusCleanup: true,
            rules: {
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
                    url: '/studentRoll/insertStudentRollModify.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                layer_close();
                            });
                        }
                    }
                });
            }
        });
    });

    function checkEnroll() {
        var newUnvsId = $("#newUnvsId").val();
        var pfsnLevels2 = $("#pfsnLevels2").val();
        var newPfsnId = $("#newPfsnId").val();
        if (newUnvsId || pfsnLevels2 || newPfsnId) {
            return checkEmpty(newUnvsId, pfsnLevels2, newPfsnId);
        } else {
            return true;
        }

    }

    function checkEmpty(newUnvsId, pfsnLevels2, newPfsnId) {
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