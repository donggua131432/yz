$(function(){
    _init_select({"selectId":"campusGroupId"}, dictJson.campusGroup,varOaCampusGroup.campusGroupId);

    //定义校区变量，以免重复获取
    var campusIdJson;
    var campusName;
    //初始校区下拉框
    url = '/campus/selectList.do';
    _simple_ajax_select({
        selectId : "campusId",
        searchUrl : url,
        sData : {},
        showText : function(item) {
            return item.campusName;
        },
        showId : function(item) {
            return item.campusId;
        },
        placeholder : '--请选择校区--'
    });
    $.ajax({
        type: "POST",
        dataType : "json", //数据类型
        url: url,
        success: function(data){
            campusIdJson = data.body.data;
            for(var i=0;i<campusIdJson.length;i++){
                if(campusIdJson[i].campusId == varOaCampusGroup.campusId){
                    campusName = campusIdJson[i].campusName;
                    $("#campusId").append(new Option(campusName, $("#campusIds").val(), false, true));
                }
            }
        }
    });

	$("#form-campus-add").validate({
		rules : {
			campusGroupId : {required : true},
			campusId : {required : true}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式
				dataType : "json", //数据类型
				url : '/campusGroup/editCampusGroup.do', //请求url
				success : function(data) {
					//提交成功的回调函数
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