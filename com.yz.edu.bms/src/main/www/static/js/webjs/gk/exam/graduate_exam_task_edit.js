$(function () {
    $("#form-base-info").validate({
        rules: {
            remark: {required: true, maxlength: 50}
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: '/studentGraduateExamGK/editRemark.do', //请求url
                success: function (data) {
                    //提交成功的回调函数
                    if ('00' == data.code) {
                        layer.msg('备注信息保存成功', {icon: 1, time: 1000}, function () {
                            window.parent.myDataTable.fnDraw(true);
                            layer_close();
                        });
                    }
                }
            });
        }
    });
});
