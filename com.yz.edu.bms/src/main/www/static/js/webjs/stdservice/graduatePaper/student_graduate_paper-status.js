$(function () {

        $("#gpId").val(getQueryString("gpId"));


        $("select").select2({
            placeholder:'--请选择--'
        })

        _init_select("paperDataStatus", [
            {"dictValue": "0", "dictName": "未收到"},
            {"dictValue": "1", "dictName": "收到"},
            {"dictValue": "2", "dictName": "收到不合格"},
            {"dictValue": "3", "dictName": "收到且合格"}
        ],getQueryString("paperDataStatus"));

        $("#form-room-assign-edit").validate({
            rules: {
                paperDataStatus: {
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
                    url: '/graduatePaper/updatePaperStatus.do',
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