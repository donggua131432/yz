$(function () {
        $("#postTime").val(new Date($("#postTime").val()).format('yyyy-MM-dd hh:mm:ss'));
        
       
        var nameDom = '<span><a title="查看学员信息" onclick="toEidt(\''+ learnId +'\',\''+ stdId +'\')"><span class="c-blue">'+ stdName +'</span></a></span>';
        
        $("#stdNameBox").html(nameDom);
        
        $("#scholarship").val(_findDict('scholarship',scholarship));
        $("#inclusionStatus").val(_findDict('inclusionStatus',inclusionStatus));
        
        changeCheck();
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
                if ('2' == $('#checkStatus').val()) {
                    $(form).ajaxSubmit({
                        type: "post", //提交方式
                        dataType: "json", //数据类型
                        url: '/studentOutApproval/passDirectorApproval.do', //请求url
                        success: function (data) { //提交成功的回调函数
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                    layer_close();
                                });
                            }
                        }
                    });
                } else {
                    layer.prompt({
                        title: '填写驳回原因：',
                        formType: 2
                    }, function (text, index) {
                        $('#reason').val(text);
                        $(form).ajaxSubmit({
                            type: "post", //提交方式
                            dataType: "json", //数据类型
                            url: '/studentOutApproval/passDirectorApproval.do', //请求url
                            success: function (data) { //提交成功的回调函数
                                if (data.code == _GLOBAL_SUCCESS) {
                                    layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                        layer_close();
                                    });
                                }
                            }
                        });
                    });
                }
            }
        });
    });
    function changeCheck() {
        $.post('/studentOutApproval/findStudentOutInfo.do', {
            learnId: $("#learnId").val()
        }, function (result) {
            result = result.body;
            $("#stdStage").val(_findDict("stdStage", result.stdStage));
            $("#stdStageh").val(result.stdStage);
            $("#unvsId").val(
                "[" + _findDict("recruitType", result.recruitType) + "]" + result.unvsName + ":" + result.pfsnName + "[" + _findDict("pfsnLevel", result.pfsnLevel) + "]");
            $("#onUnvsId")
                .val(
                    "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                    + "]");
            $("#unvsId")
                .val(
                    "[" + _findDict("recruitType", result.bsarecruitType) + "]" + result.bsaunvsName + ":" + result.bsapfsnName + "[" + _findDict("pfsnLevel", result.bsapfsnLevel)
                    + "]");
            $("#recruit").val(result.oerName);
            $("#tutor").val(result.oetName);
            $("#learnIdd").val(result.learnId);
        }, "json");
    }
    
    /*用户-编辑*/
	function toEidt(learnId, stdId) {
		var url = '/studentBase/toEdit.do' + '?learnId='
				+ learnId + '&stdId=' + stdId;
		layer_show('学员信息', url, null, null, function() {
			myDataTable.fnDraw(false);
		}, true);
	}