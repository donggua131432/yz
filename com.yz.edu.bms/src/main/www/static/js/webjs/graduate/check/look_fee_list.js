 $(function () {

        var row = $("#feeInfo").val();
        
        var json = eval('(' + row + ')');
        initPayInfoDom(json,grade);
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
    function initPayInfoDom(row,grade) {
        var dom = '';
        dom += '<table class="table table-border table-bordered table-hover table-bg table-sort">';
        dom += '<thead>';
        dom += '<tr><th scope="col" colspan="6">学员【<span id="stdName"></span>】 的学费核准</th></tr>'
        dom += '<tr>';
        dom += '<th width="80">科目</th>';
        dom += '<th width="40">应缴</th>';
        dom += '<th width="40">缴费状态</th>';
        dom += '</tr>';
        dom += '</thead><tbody>';

        var amount = 0.00;
        for (var i = 0; i < row.length; i++) {
            var payInfo = row[i];
            dom += '<tr>';
            var itemName = getItemName(payInfo.itemName,grade);
            dom += '<td>'
                + payInfo.itemCode
                + ':'
                + itemName
                + '</td>';
            dom += '<td>'
                + payInfo.payable
                + '</td>';
            var status = payInfo.subOrderStatus;
            dom += '<td>';
            dom += '<span>' + _findDict("orderStatus", status) + '</span>';
            dom += '</td>';
            dom += '</tr>';
            amount = amount
                + parseFloat(payInfo.payable);
        }
        dom += '<tr >';
        dom += '<td>合计：</td>';
        dom += '<td>' + amount
            + '</td>';
        dom += '<td></td>';
        dom += '</tr></tbody>';
        dom += '</table>';
        $("#payInfoDiv").html(dom);
    }