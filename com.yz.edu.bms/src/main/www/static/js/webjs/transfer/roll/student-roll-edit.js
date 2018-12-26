$(function () {
        if ("SHOW" == $("#exType").val()) {
            $("#updateSubmit").hide();
        }
        //院校
        _simple_ajax_select({
            selectId: "newUnvsId",
            searchUrl: '/bdUniversity/searchUniversity.do',
            sData: {},
            showText: function (item) {
                return item.text;
            },
            showId: function (item) {
                return item.id;
            },
            placeholder: '--请选择院校--'
        });

        var check1 = global_check1;
        var check2 = global_check2;
        if (check1 != null && check1 != '') {
            $("#reason").val(check1);
        } else if (check2 != null && check2 != '') {
            $("#reason").val(check2);
        }


        //学员阶段
        $("#stdStage").text(_findDict("stdStage", $("#stdStage").text()));

        //性别
        $("#sexv").val(_findDict("sex", $("#sexv").val()));
        $("#newSex").text(_findDict("sex", $("#newSex").text()));

        $("#newNation").text(_findDict("nation", $("#newNation").text()));
        $("#nationv").val(_findDict("nation", $("#nationv").val()));

        $("#pfsnLevelv").val(_findDict("pfsnLevel", $("#pfsnLevelv").val()));
        $("#pfsnLevel").text(_findDict("pfsnLevel", $("#pfsnLevel").text()));

        $("#newScholarship").text(_findDict("scholarship", $("#newScholarship").text()));

        $("#grade").val(_findDict("grade", $("#grade").val()));

    });