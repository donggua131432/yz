$(function () {

        $("select").select2({
            placeholder:'--请选择--'
        })

        _init_select("isQualified", [
            {"dictValue": "0", "dictName": "不合格"},
            {"dictValue": "1", "dictName": "合格"}
        ],isQualified);

        _init_select("receiveStatus", [
            {"dictValue": "0", "dictName": "未收到"},
            {"dictValue": "1", "dictName": "已收到"}
        ],receiveStatus);

        if('[[${collects.isQualified}]]'=='0'){
            $("#unqualified").show();
        }

        $("#grade").text(_findDict("grade",grade));

        $("#unvs").html(getUnvs());

        $("#isQualified").change(function () {

            if($(this).val()=='0') {
                $("#unqualified").show();
            }else {
                $("#unqualified").hide();
            }
        })

        $("#form-room-assign-edit").validate({
            rules: {
                receiveStatus: {
                    required: true
                },
                unqualifiedReason:{
                    required:!$("#unqualified").is(':visible')
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/collect/feedback.do',
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                parent.myDataTable.fnDraw(false);
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    });

    function getUnvs() {
        var text = '';
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