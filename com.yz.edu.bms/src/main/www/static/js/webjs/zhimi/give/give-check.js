function checkPass(reasonStatus) {
    if(reasonStatus=="3"){
        if($("#rejectDesc").val()==""){
            layer.msg('请在审核备注处填写驳回理由!', {icon: 5, time: 1000});
            return;
		}
	}
    var url = '/zhimi_give_check/check.do';
    
    $.ajax({
        url : url,
        dataType : 'json',
        type : 'post',
        data : {id:$("#id").val(), reasonStatus:reasonStatus, rejectDesc:$("#rejectDesc").val(), _web_token:$("#_web_token").val()},
        success : function(data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var msg="审核通过！";
                if(data.body=="reject") msg="已驳回";
                layer.msg(msg, {icon : 1,time : 1000}, function() {
                    window.parent.myDataTable.fnDraw(true);
                    layer_close();
                });
            }
        }
    });
}