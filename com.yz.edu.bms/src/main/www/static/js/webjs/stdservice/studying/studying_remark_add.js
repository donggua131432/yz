var myDataTable;
$(function() {
	
	$("#form-member-add").validate({
        rules : {
        	content : {
                required : true,
            }
        },
        messages : {
            errorCode : {
                remote : "参数名称已存在"
            }
        },
        onkeyup : false,
        focusCleanup : true,
        success : "valid",
        submitHandler : function(form) {
            var  urls = '/studying/insertRemark.do';
            $(form).ajaxSubmit({
                type : "post", //提交方式  
                dataType : "json", //数据类型  
                url : urls, //请求url 
                success : function(data) { //提交成功的回调函数
                    layer.msg('提交成功', {
                        icon : 1,
                        time : 1000
                    },function () {
                    	 window.parent.myDataTable.fnDraw(false);
                         layer_close();
                    });
                },
                error : function(data) {
                    layer.msg('提交失败', {
                        icon : 5,
                        time : 1000
                    },function () {
                        layer_close();
                    });
                }
            })
        }
    });
	
	

	});