$(function () {
        $("#stdName").text($("#stdNames").val());
        $("#checkStatus").text(_findDict("saStatus", $("#checkStatus").text()));
    })
    function check_pass(checkStatus) {
        var remark = $("#remark").val();
        if (remark.length < 10) {
            layer.msg('至少输入10个字!', {icon: 1, time: 2000}, function () {
                layer_close();
            });
            return;
        }
        $.ajax({
            type: "POST",
            url: "/check/checkStatus.do",
            data: {
                "checkId": $("#checkId").val(),
                "remark": $("#remark").val(),
                "checkStatus": checkStatus
            },
            success: function (data) {
                layer.msg('操作成功!', {icon: 1, time: 2000}, function () {
                    layer_close();
                });

            }
        });
    }