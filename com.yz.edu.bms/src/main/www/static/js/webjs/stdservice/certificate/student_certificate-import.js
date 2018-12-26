$(function () {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });

        $("#form-member-add").validate({
            rules: {
                excelPath: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $("#import").val("正在导入").attr('disabled','disabled').css({opacity:'0.5'});
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: xtype=='xf'?'/certificate/upload.do':'/certificate/uploadZS.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('导入成功！', {icon: 1, time: 1000}, function () {
                                parent.myDataTable.fnDraw(true);
                                layer_close();
                            });
                        }else if(data.code=="E000001"){
                            $("#import").val("开始导入").removeAttr('disabled').css({opacity:'1'});
                        }
                    },
                    error:function () {
                        $("#import").val("开始导入").removeAttr('disabled').css({opacity:'1'});
                    }

                });
            }
        });
    });