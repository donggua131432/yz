$(function () {

        $("#qingshuId").val(getQueryString("id"));


        $("select").select2({
            placeholder:'--请选择--'
        })

        _init_select("semester", [
            {"dictValue": "1", "dictName": "第一学期"},
            {"dictValue": "2", "dictName": "第二学期"},
            {"dictValue": "3", "dictName": "第三学期"},
            {"dictValue": "4", "dictName": "第四学期"},
            {"dictValue": "5", "dictName": "第五学期"},
            {"dictValue": "6", "dictName": "第六学期"}
        ]);

        $("#form-room-assign-edit").validate({
            rules: {
                semester: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/qingshu/resetScore.do',
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                parent.myDataTable.fnDraw(true);
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    });

    function getQueryString(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }