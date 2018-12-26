//变量
    var learnId;
    var stdName;
    var stdId;
    $(function () {
        $("#stdName").text($("#stdNames").val());
        $("#dataCheckStatus").text(_findDict("saStatus", $("#dataCheckStatus").text()));
        $("#pictureCheckStatus").text(_findDict("saStatus", $("#pictureCheckStatus").text()));
        $("#paperCheckStatus").text(_findDict("saStatus", $("#paperCheckStatus").text()));
        $("#scoreCheckStatus").text(_findDict("saStatus", $("#scoreCheckStatus").text()));
        $("#feeCheckStatus").text(_findDict("saStatus", $("#feeCheckStatus").text()));
        learnId = $("#learnId").val();
        stdName = $("#stdNames").val();
        stdId = $("#stdId").val();
    })
    function check_pass(checkStatus) {
   	 var remark = $("#remark").val();
        if (remark.length < 10) {
            layer.msg('至少输入10个字!');
            return;
        }
        $.ajax({
            type: "POST",
            url: "/check/checkAffirm.do",
            data: {
                "graduateId": $("#graduateId").val(),
                "remark": $("#remark").val(),
                "learnId": $("#learnId").val(),
                "checkStatus": checkStatus
            },
            success: function (data) {
                layer.msg('操作成功!', {icon: 1, time: 2000}, function () {
                    layer_close();
                });

            }
        });
    }

    function check_data() {
        var checkId = $("#dataCheckId").val();
        var url = '/check/checkData.do' + '?checkId=' + checkId + '&learnId=' + learnId + '&stdName=' + stdName + '&exType=LOOK';
        layer_show('毕业 资料核查', url, 1000, 610);
    }
    function check_picture() {
        var checkId = $("#pictureCheckId").val();
        var url = '/check/checkPicture.do' + '?checkId=' + checkId + '&learnId=' + learnId + '&stdName=' + stdName + '&exType=LOOK';
        layer_show('毕业头像采集核查', url, 1000, 610);
    }
    function check_paper() {
        var checkId = $("#paperCheckId").val();
        var url = '/check/checkPaper.do' + '?checkId=' + checkId + '&learnId=' + learnId + '&stdName=' + stdName + '&exType=LOOK';
        layer_show('毕业 论文核查', url, 1000, 610);
    }
    function check_score() {
        var checkId = $("#scoreCheckId").val();
        var url = '/check/checkScore.do' + '?checkId=' + checkId + '&learnId=' + learnId + '&stdId=' + stdId + '&stdName=' + stdName + '&exType=LOOK';
        layer_show('毕业 期末成绩核查', url, 1000, 610);
    }
    function check_fee() {
        var checkId = $("#feeCheckId").val();
        var url = '/check/checkFee.do' + '?checkId=' + checkId + '&learnId=' + learnId + '&stdName=' + stdName + '&exType=LOOK';
        layer_show('毕业 学费清缴核查', url, 1000, 610);
    }