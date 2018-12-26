$(function () {
        changeCheck();
        var stdName = global_stdName;
        var learnId = global_learnId;
        var stdId = global_stdId;
        var nameDom = '<span><a title="查看学员信息" onclick="toEidt(\''+ learnId +'\',\''+ stdId +'\')"><span class="c-blue">'+ stdName +'</span></a></span>';
        
        $("#stdNameBox").html(nameDom);
        
        $("#scholarship").val(_findDict('scholarship',scholarship));
        $("#inclusionStatus").val(_findDict('inclusionStatus',inclusionStatus));
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });

        $("#form-member-add").validate({
            rules: {
                outId: {
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
                    url: '/studentOutApproval/undoDirectorApproval.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        layer_close();
                    },
                    error: function (data) {
                        layer.msg('操作失败', {
                            icon: 1,
                            time: 1000
                        });
                        layer_close();
                    }
                });
            }
        });

    });
    /*用户-编辑*/
	function toEidt(learnId, stdId) {
		var url = '/studentBase/toEdit.do?learnId='
				+ learnId + '&stdId=' + stdId;
		layer_show('学员信息', url, null, null, function() {
			myDataTable.fnDraw(false);
		}, true);
	}
    function changeCheck() {
        $.post('/studentOutApproval/findStudentOutInfo.do', {
            learnId: $("#learnId").val()
        }, function (result) {
            result = result.body;
            $("#stdStage").val(_findDict("stdStage", result.stdStage));
            $("#stdStageh").val(result.stdStage);
            $("#unvsId").val(
                "[" + _findDict("recruitType", result.recruitType) + "]" + result.unvsName + ":" + result.pfsnName + "[" + _findDict("pfsnLevel", result.pfsnLevel) + "]");
            $("#onUnvsId").val(
                "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                + "]");
            $("#unvsId").val(
                "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                + "]");
            $("#recruit").val(result.oetName);
            $("#tutor").val(result.oerName);
            $("#learnIdd").val(result.learnId);
        }, "json");
    }