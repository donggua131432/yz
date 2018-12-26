$(function() {
	//邀约人
	//邀约人
	_simple_ajax_select({
		selectId: "empId",
		searchUrl: '/invite_qrcode/getEmpList.do',
		sData: {},
		showText: function (item) {
			var empName=item.empName;
			if(item.empMobile!=null)
			empName+=" "+item.empMobile;
			return empName;
		},
		showId: function (item) {
			return item.empId;
		},
		placeholder: '--请选择邀约人--'
	});
	//邀约页面
	_init_select("inviteLinkUrl", dictJson.inviteQrType,$("#defaultUrla").val());
	//查看
	if("ADD"!=varExType){
		$("#submitbtn").hide();
		$("#remark").attr("readOnly",false);
		$("#empId").attr("disabled",true);
		$("#inviteLinkUrl").attr("disabled",true);
		$("#remark").val(inviteQrCodeInfo.remark);
		$("#inviteUrl").val(inviteQrCodeInfo.inviteUrl);
  	    var imgUrl=_FILE_URL+inviteQrCodeInfo.inviteQrcodeUrl;
  	    $("#qrcode_img").attr('src',imgUrl);
	}
	$("#form-member-add").validate({
                    rules : {
                        empUser : {
                            required : true,
                        },
                        inviteName : {
                            required : true,
                        }
                    },
                    messages : {
                        empUser : {
                            remote : "请选择邀约人"
                        },
                        inviteName : {
                            remote : "请选择邀约页面"
                        }
                    },
                    onkeyup : false,
                    focusCleanup : true,
                    success : "valid",
                    submitHandler : function(form) {
                        $(form).ajaxSubmit({
                            type : "post", //提交方式  
                            dataType : "json", //数据类型  
                            url : '/invite_qrcode/inviteQrCodeAdd.do', //请求url 
                            success : function(data) { //提交成功的回调函数  
                                if (data.code == _GLOBAL_SUCCESS) {
                                	if(data.body=="success"){
                                    layer.msg('操作成功！', {
                                        icon : 1,
                                        time : 1000
                                    },function () {
                                        layer_close();
                                    });
                                	}else{
                                		layer.msg('生成渠道ID失败,请确认该邀约人是否微信注册!', {
								            icon : 2,
								            time : 3000
								       		 });
                                	}
                                }
                            }
                        })
                    }
                });
});

$("#inviteLinkUrl").change(function(){
	setQrCode();
});
$("#empId").change(function(){
	setQrCode();
});
function setQrCode(){
	var typeName=$("#empId").val();
	var varInviteUrl=$("#inviteLinkUrl").val();
	 if(null!=typeName&&""!=typeName&&null != varInviteUrl && "" !=varInviteUrl){
	 	$("#inviteName").val( $("#inviteLinkUrl").find("option:selected").text());
	 	$("#empName").val( $("#empId").find("option:selected").text());
	 	$("#defaultUrl").val(varInviteUrl);
		 $.ajax({
           type: "POST",
           dataType : "json", //数据类型  
           data : {
				empUser : typeName,
				inviteUrl : varInviteUrl
			},
           url: "/invite_qrcode/generateQrcode.do",
           success: function(data){
           	 if (data.code == _GLOBAL_SUCCESS) {
		       	 if(data.body.channelId!=null){
			      	 $("#channelId").val(data.body.channelId);
			      	 $("#inviteQrcodeUrl").val(data.body.imgUrl);
			      	 $("#inviteUrl").val(data.body.url);
			      	 var imgUrl=_FILE_URL+data.body.imgUrl;
			      	 $("#qrcode_img").attr('src',imgUrl);
		       	 }else{
		       	 		layer.msg('生成渠道ID失败,请确认该邀约人是否微信注册!', {
			            icon : 2,
			            time : 3000
			       		 });
           	}
           }else{
           	 layer.msg('生成渠道ID失败,请确认该邀约人是否微信注册!', {
                icon : 2,
                time : 1000
            });
            
           }
           }
   		 }); 
	 }
}
