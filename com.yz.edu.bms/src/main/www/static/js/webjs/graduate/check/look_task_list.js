$(function () {
        var row = $("#taskInfo").val();
        var json = eval('(' + row + ')');
        initFeeDataDom(json);
        $("#stdName").text($("#stdNames").val());
        $("#checkStatus").text(_findDict("saStatus", $("#checkStatus").text()));
    });

    function check_pass(checkStatus) {
        //验证审核批注的长度
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
                "remark": remark,
                "checkStatus": checkStatus
            },
            success: function (data) {
                layer.msg('操作成功!', {icon: 1, time: 2000}, function () {
                    layer_close();
                });

            }
        });
    }
    function initFeeDataDom(row) {
        var dom = '';
        dom += '<p>学员【<span id="stdName"></span>】的毕业教务任务核准</p>';
        dom += '<table class="table table-border table-bordered table-hover table-bg table-sort">';
        dom += '<thead>';
        dom += '<tr>';
        dom += '<th width="80">任务名称</th>';
        dom += '<th width="40">任务简述</th>';
        dom += '<th width="40">创建人</th>';
        dom += '<th width="40">开始时间</th>';
        dom += '<th width="40">截止时间</th>';
        dom += '<th width="40">是否完成</th>';
        dom += '</tr>';
        dom += '</thead><tbody>';

        for (var i = 0; i < row.length; i++) {
            var taskInfo = row[i];
            dom += '<tr>';
            dom += '<td>' + taskInfo.taskTitle + '</td>';
            dom += '<td>' + taskInfo.taskContent + '</td>';
            dom += '<td>' + taskInfo.pubName + '</td>';
            dom += '<td>' + taskInfo.startTime + '</td>';
            dom += '<td>' + taskInfo.endTime + '</td>';
            var status = taskInfo.taskStatus == '0' ? '未完成' : '已完成';
            dom += '<td>';
            dom += '<span>' + status + '</span>';
            dom += '</td>';
            dom += '</tr>';

        }
        dom += '</tbody>';
        dom += '</table>';
        $("#taskInfoDiv").html(dom);
    }