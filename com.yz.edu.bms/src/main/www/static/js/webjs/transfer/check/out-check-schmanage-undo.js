/*用户-编辑*/
function toEidt(learnId, stdId) {
	var url = '/studentBase/toEdit.do?learnId='
			+ learnId + '&stdId=' + stdId;
	layer_show('学员信息', url, null, null, function() {
		myDataTable.fnDraw(false);
	}, true);
}
    function initItemDom() {
    	var row=globalrow;
        var dom = '';
        dom += '<tbody>';
        for (var i = 0; i < row.length; i++) {
            var payInfo = row[i];

            dom += '<tr>';
            var itemName = getItemName(payInfo.itemName,$("#grade").val());
            var index=0;
            for(var a=0;a<row.length;a++){
            	if(row[a].subOrderNo==payInfo.subOrderNo && row[a].itemCode!=payInfo.itemCode)
            	{
            		dom += '<td>' + payInfo.itemCode + ':' + itemName + '</br>'
            		+ row[a].itemCode + ':' + row[a].itemName +'</td>';
            		break;
            	}else{
            		index++;
            	}
            }
            if(index==row.length){
            	dom += '<td>' + payInfo.itemCode + ':' + itemName + '</td>';
            }
           dom += '<td>' + payInfo.payable + '</td>';
            dom += '<td>' + payInfo.xjAmount + '</td>';
            dom += '<td>' + payInfo.zmAmount + '</td>';
            dom += '<td>' + payInfo.zlAmount + '</td>';
            dom += '<td>' + payInfo.yhqAmount + '</td>';
            dom += '<td>' + '<input type="hidden" value="' + payInfo.itemCode + '" name="itemCode" id="itemCode" />' + '<input type="number" class="input-text radius" value="' + payInfo.refundAmount + '" name="items[' + i + '].refundAmount" id="refundAmount" readonly="readonly"/>' + '</td>';
            dom += '</tr>';
        }
        dom += '</tbody>';
        $("#feeItem").append(dom);
    }
    $(function () {
        initItemDom();
        
        var stdName = globalstdName;
        var learnId = globalstdName;
        var stdId = globalstdId;
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
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/studentOutApproval/undoSchoolManagedApproval.do', //请求url
                    success: function (data) { //提交成功的回调函数
                    	if(data.code == _GLOBAL_SUCCESS){
							layer.msg('操作成功！', {icon : 1, time : 1000},function(){
								layer_close();
							});
						}
                    }
                })
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