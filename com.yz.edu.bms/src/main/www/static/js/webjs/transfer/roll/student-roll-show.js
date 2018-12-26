$(function () {

        var check = global_check;
        var check1 = global_check1;
        var check2 = global_check2;
        if (check != null && check != '') {
            $("#reason").val(check);
        } else if (check1 != null && check1 != '') {
            $("#reason").val(check1);
        } else if (check2 != null && check2 != '') {
            $("#reason").val(check2);
        }


        //学员阶段
        $("#stdStage").text(_findDict("stdStage", $("#stdStage").text()));

        //优惠类型
        $("#scholarship").text(_findDict("scholarship", $("#scholarship").text()));

        //入围状态
        $("#inclusionStatus").text(_findDict("inclusionStatus", $("#inclusionStatus").text()));

        //性别
        $("#sexv").val(_findDict("sex", $("#sexv").val()));
        $("#newSex").text(_findDict("sex", $("#newSex").text()));

        $("#newNation").text(_findDict("nation", $("#newNation").text()));
        $("#nationv").val(_findDict("nation", $("#nationv").val()));

        $("#pfsnLevel").text(_findDict("pfsnLevel", $("#pfsnLevel").text()));
        $("#zypfsnLevel").val(_findDict("pfsnLevel",$("#zypfsnLevel").val()));
        $("#zynpfsnLevel").text(_findDict("pfsnLevel",$("#zynpfsnLevel").text()));

        $("#grade").val(_findDict("grade", $("#grade").val()));

        var ext = ".doc";
        //韩师，东理
        if ($("#unvsId").val() == 23 || $("#unvsId").val() == 29) {
            ext = ".xls";
        }
        //国开大学
        if ($("#unvsId").val() != 46) {
            $("#rollfile").attr("href", "../rolldoc/roll_" + $("#unvsId").val()+ ext);
        }
        //暂无文件
        if($("#unvsId").val()==47 || $("#unvsId").val()==50 || $("#unvsId").val()==52|| $("#unvsId").val()==54){
            $("#rollfile").attr("href","javascript:;");
        }

    });