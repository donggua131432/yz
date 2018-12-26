 $(function () {

        //初始化年度下拉框
        _init_select("year", dictJson.year);

        $("#form-room-assign-edit").validate({
            rules: {
                year: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $("#save").val("正在生成").attr('disabled','disabled').css({opacity:'0.5'});
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/live/gen.do',
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                layer_close();
                            });
                        } else if(data.code=="E000001"){
                            $("#save").val("保存").removeAttr('disabled').css({opacity:'1'});
                        }
                    },
                    error:function () {
                        $("#save").val("保存").removeAttr('disabled').css({opacity:'1'});
                    }
                })
            }
        });
    });