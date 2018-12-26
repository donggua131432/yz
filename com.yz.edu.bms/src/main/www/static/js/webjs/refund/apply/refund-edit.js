$(function () {

        //学员
        _simple_ajax_select({
            selectId: "learnId",
            searchUrl: '/refund/findStudentInfo.do',
            sData: {},
            showText: function (item) {
                return item.stdName + " - " + item.grade + "级" + "(" + _findDict("stdStage", item.stdStage) + ")";
            },
            showId: function (item) {
                return item.learnId;
            },
            placeholder: '--学员--'
        });


        $("#learnId").val(null).trigger("change");
        $("#learnId").change(function () {
            changeCheck();
        });

        $("#form-member-add").validate({
            rules: {
                learnId: {
                    required: true,
                },
                refundAmount: {
                    required: true,
                    min: 1,
                    max: function () {
                        return parseFloat($("#accAmount").val());
                    },
                    isStdFee: true
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/refund/refundApply.do', //请求url
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
    function changeCheck() {
        $.post('/refund/findStudentInfoByGraStdId.do', {
            learnId: $("#learnId").val()
        }, function (result) {
            if (!result.body) {
                $("#stdStage").val("");
                $("#unvsId").val("");
                $("#onUnvsId").val("");
                $("#unvsId").val("");
                $("#recruit").val("");
                $("#tutor").val("");
                $("#stdId").val("");
                $("#accAmount").val('');
                $("#grade").val("");
                $("#idCard").text("")
                $("#inclusionStatus").text("");
                $("#scholarship").text("");
            } else {
                result = result.body;
                $("#stdStage").val(_findDict("stdStage", result.stdStage));
                $("#stdStageh").val(result.stdStage);
                $("#unvsId").val(
                    "[" + _findDict("recruitType", result.recruitType) + "]" + result.unvsName + ":" + result.pfsnName + "[" + _findDict("pfsnLevel", result.pfsnLevel) + "]");
                $("#onUnvsId").val(
                    "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                    + "]");
                $("#unvsId").val(
                    "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                    + "]");
                $("#recruit").val(result.oerName);
                $("#tutor").val(result.oetName);
                $("#stdId").val(result.stdId);
                $("#grade").val(result.grade);
                $("#accAmount").val(result.accAmount);
                $("#idCard").text(result.idCard);
                $("#inclusionStatus").text(_findDict("inclusionStatus", result.inclusionStatus));
                $("#scholarship").text(_findDict("scholarship", result.scholarship));
            }

        }, "json");
    }