$(function () {
        $("#form-change-password").validate({
            rules: {
                newpassword: {
                    required: true,
                    maxlength: 16,
                    minlength:8,
                    regexPassword: true,
                    same: true,
                },
                newpassword2: {
                    required: true,
                    equalTo: "#newpassword"
                },
                password: {
                    required: false,
                }
            },
            errorPlacement: function(error, element) { //错误信息位置设置方法
                error.appendTo( element.parent().next() ); //这里的element是录入数据的对象
            },
            messages: {
                password: '请输入原密码',
                newpassword: {
                    required: '请输入新密码',
                    regexPassword: '密码为8-16位包含数字与大小写哦',
                    same: '新密码不能与原密码一样'
                },
                newpassword2: {
                    required: "请输入确认新密码",
                    equalTo: "两次输入密码不一致"
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/user/userPwd.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            if ($('#newpassword') != '123456') {
                                window.localStorage.isDefaultPsw = '0'
                            }
                            layer.msg('修改成功！请重新登录', {icon: 1, time: 2000}, function () {
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    })
    function same(pwd) {
        var oldPwd = $("#password").val();
        if (oldPwd == pwd) {
            return false;
        } else {
            return true;
        }
    }
    jQuery.validator.addMethod("same", function (value, element) {
        return this.optional(element) || same(value);
    }, "新密码不能与原密码重复");
    jQuery.validator.addMethod("regexPassword", function (value, element) {
        return this.optional(element) || /^.*(?=.{8,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/.test(value);
    }, "密码需要包含数字和大小写");
