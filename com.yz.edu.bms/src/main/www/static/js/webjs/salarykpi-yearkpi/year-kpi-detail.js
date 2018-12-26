$(function () {
        //console.log(JSON.parse($(window.parent.document).find('#row').val()))
        var data = JSON.parse($(window.parent.document).find('#row').val());
        $("#empCount").text(data.empCount);
        $("#empName").val(data.empName);
        $("#idCard").val(data.idCard);
        $("#campusName").val(data.campusName);
        $("#dpName").val(data.dpName);
        $("#jtId").val(_findDict("jtId", data.jtId));
        $("#empType").val(_findDict("empType", data.empType));
        $("#empStatus").val(_findDict("empStatus", data.empStatus));
        $("#hzymRwCount").text(data.hzymRwCount);
        $("#hzymFrwCount").text(data.hzymFrwCount);
        $("#taskCount").text(data.taskCount);

        $("#dgymCount").text(data.dgymCount);
        $("#award").text(data.award);

        $("#jxjRwCount").text(data.jxjRwCount);
        $("#singleStandardKPIValue").text(data.singleStandardKPIValue);

        $("#jxjFrwCount").text(data.jxjFrwCount);
        $("#singleRealKPIValue").text(data.singleRealKPIValue);

        $("#jxjFrwCount").text(data.jxjFrwCount);
        $("#singleRealKPIValue").text(data.singleRealKPIValue);

        $("#gkCount").text(data.gkCount);
        $("#KPIValue").text(data.KPIValue);

        $("#swzxRwCount").text(data.swzxRwCount);
        $("#receiptSum").text(data.receiptSum);

        $("#kqccCount").text(data.kqccCount);
        $("#salarySum").text(data.salarySum);

        $("#swzxFrwCount").text(data.swzxFrwCount);
        $("#finalKPIValue").text(data.finalKPIValue);

        $("#ptqeZcCount").text(data.ptqeZcCount);
        $("#zcSum").text(data.zcSum);
        $("#ckSum").text(data.ckSum);
        $("#ptqeCkCount").text(data.ptqeCkCount);
    });

    function studentDetail(queryType,scholarship,inclusionStatus) {
        var data = JSON.parse($(window.parent.document).find('#row').val());
        var url = '/yearkpi/studentDetail.do';
        url+='?queryType=' + queryType + '&recruit=' + data.recruit+'&scholarship='+scholarship+'&inclusionStatus='+inclusionStatus;
        layer_show('学员明细', url, 1200, 700, function () {

        });
    }