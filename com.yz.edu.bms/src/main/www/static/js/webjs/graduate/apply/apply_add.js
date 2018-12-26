$(function () {

        $("#condition").blur(function () {
            var condition = $("#condition").val();
            if (condition != '') {
                $.ajax({
                    type: "POST",
                    url: '/graduate/queryStudentByCondition.do',
                    data: {
                        "condition": condition
                    },
                    dataType: "json", //数据类型
                    success: function (data) {
                        if (data.body.learnId == null) {
                            $("#grade").val("");
                            $("#pfsnLevel").val("");
                            $("#unvsName").val("");
                            $("#pfsnName").val("");
                            $("#stdStage").val("");
                            $("#stdId").val("");
                            $("#learnId").val("");
                        } else {
                            //赋值
                            $("#grade").val(data.body.grade + '级');
                            $("#pfsnLevel").val(_findDict("pfsnLevel", data.body.pfsnLevel));
                            $("#unvsName").val(data.body.unvsName);
                            $("#pfsnName").val(data.body.pfsnName);
                            $("#stdStage").val(_findDict("stdStage", data.body.stdStage));
                            $("#stdId").val(data.body.stdId);
                            $("#learnId").val(data.body.learnId);
                        }

                    }
                });
            }

        });
        $("#form-apply-add").validate({

            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                if ($("#learnId").val() == '') {
                    layer.msg('请输入学员信息再发起申请！', {icon: 2, time: 1000});
                    return;
                }
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/graduate/apply.do', //请求url
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
    });