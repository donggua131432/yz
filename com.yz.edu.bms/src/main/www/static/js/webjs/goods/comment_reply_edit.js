$(function () {

        $("#salesType").text(_findDict("salesType", varcommentInfo.salesType));
        $("#salesName").text(varcommentInfo.salesName);
        $("#commentContent").text(varcommentInfo.commentContent);
        $("#commentTime").text(varcommentInfo.commentTime)
        $("#commentStatus").text(_findDict("commentStatus", varcommentInfo.commentStatus));
        if (exType == 'LOOK') {
            $("#replyContent").text(varcommentInfo.replyContent);
            $("#replyTime").text(varcommentInfo.replyTime);
        }
        $("#form-reply-add").validate({
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: "/comment/commentReply.do", //请求url
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