var selectData=[];
$(function(){

   
    var getUserInfoUrl = '/zhimi_give/getUserInfo.do';

    //选择用户
    _simple_ajax_select({
        selectId: "userId",
        searchUrl: getUserInfoUrl,
        sData: {},
        showText: function (item) {
            selectData.push(item);
            var result = "";
            if(item.yz_code!=null) result+=item.yz_code+"-";
            if(item.mobile!=null) result+=item.mobile+"-";
			if(item.cert_no!=null) result+=item.cert_no+"-";
            return result.substr(0,result.length-1);
        },
        showId: function (item) {
            return item.user_id;
        },
        placeholder: '--可搜索远智编号，手机号，身份证号--'
    });


    //赠送智米类型
    _init_select("zhimiType", [
        {"dictValue" : "1","dictName" : "进账"},
        {"dictValue" : "2","dictName" : "出账"}
    ]);



	 var url = '/zhimi_give/add.do';

	$("#form-charges-add").validate({
		rules : {
            userId : {required : true},
            zhimiType : {required : true},
            zhimiCount : {required : true, number : true, min:1, maxlength:8},
            reasonDesc : {required : true}
		},
        messages: {
            zhimiCount: {
                required: "请输入赠送智米",
                number: "必须是数字",
                min:"不能小于等于0",
                maxlength:"不能超过8位"
            }
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : url, //请求url
				success : function(data) { //提交成功的回调函数
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 1000},function(){
							window.parent.myDataTable.fnDraw(true);
							layer_close();
						});
					}
				}
			})
		}
	});
});


function selectChange(obj) {
    $.each(selectData,function (i,data) {
		if($(obj).val()==data["user_id"]){
            $("#nickName").text(data["nickname"]==null?"":data["nickname"]);
            $("#realName").text(data["real_name"]==null?"":data["real_name"]);
            $("#mobile").text(data["mobile"]==null?"":data["mobile"]);
            $("#yzCode").text(data["yz_code"]==null?"":data["yz_code"]);
            $("#certNo").text(data["cert_no"]==null?"":data["cert_no"]);
		}
    });
}