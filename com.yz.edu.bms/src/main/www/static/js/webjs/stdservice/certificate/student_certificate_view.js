$(function(){

            _init_select("checkStatus",[
                {"dictValue":"2","dictName":"通过"},
                {"dictValue":"3","dictName":"驳回"}
            ]);
            $("#grade").text(_findDict("grade",grade));
            $("#unvs").html(getUnvs());

            if(receiveType=='1'){
				var dom='';
				dom+=receiverName + receiverMobile+'<br/>';
				dom+=receiverAddress;
				$("#address").html(dom);
			}else{
                $("#address").css("color","red").html("（惠州市惠城区江北三新南路22号润宇豪庭三楼远智教育前台处，工作时间周一至周五（早上9：00-12:00，下午14:00-18:00），请至少提前一天致电4008336013预约领取时间）");
			}

			$("#checkStatus").change(function () {
				if($(this).val()=="2"){
				    $("#checkRemark").text("您的申请材料已正在受理，请耐心等候！")
				}else if($(this).val()=="3"){
                    $("#checkRemark").text("您申请的证明材料不在受理范围内")
				}else{
				    $("#checkRemark").text("");
				}
            })

            var url = '/certificate/editStuXuexinRemark.do';

			$("#form-xuexin-edit").validate({
                rules: {
                    checkStatus: {
                        required: true
                    },
                    checkRemark:{
                        required: true
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
									window.parent.myDataTable.fnDraw(false);
									layer_close();
								});
							}
						}
					})
				}
			});
		});

        function getUnvs() {
            var  text = '';
            if (unvsName) text += unvsName + '</br>';
            if (pfsnLevel) {
                if (_findDict("pfsnLevel", pfsnLevel).indexOf("高中") != -1) {
                    text += "[专科]";
                } else {
                    text += "[本科]";
                }
            }
            if (pfsnName) text += pfsnName;
            if (pfsnCode) text += "(" + pfsnCode + ")";
            return text ? text : '无';
        }