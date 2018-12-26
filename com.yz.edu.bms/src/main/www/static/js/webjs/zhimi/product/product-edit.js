$(function() {
    var action = '';
    for(var i = 1; i<= count; i++) {
       $("#sort").append("<option value=" + i + ">" + i + "</option>");
    }
    
    var ruleCodePattern = null;
    
    if('UPDATE' == type) {
        action = '/product/update.do';
        
        $("#productId").val(product.productId);
        $("#productName").val(product.productName);
        $("#price").val(product.price);
        $("#zhimi").val(product.zhimi);
        $("#productDesc").val(product.productDesc);
        
        var sort = product.sort;
        $("#oldSort").val(sort);
        
        $("#sort option[value=" + sort + "]").attr("selected", "selected");
        
        var isAllow = product.isAllow;
        $("#is_allow_" + isAllow).attr('checked', 'checked');
        
    } else if('ADD' == type) {
        action = '/product/add.do';
        $("#is_allow_1").attr('checked', 'checked');
        
        $("#sort option[value=" + count + "]").attr("selected", "selected");
    } else {
        return false;
    }
    
    $('.skin-minimal input').iCheck({
        checkboxClass : 'icheckbox-blue',
        radioClass : 'iradio-blue',
        increaseArea : '20%'
    });

    $("#product-form").validate({
        rules : {
            productName : {
                remote : { //验证用户名是否存在
                    type : "POST",
                    url : '/product/validate.do', //servlet
                    data : {
                        'productId' : $("#productId").val(),
                        'productName' : function() {
                            return $("#productName").val();
                        }
                    }
                },
                required : true
            },
            price : {
                required : true,
                isFloatGtZero : true
            },
            zhimi : {
                required : true,
                isDigits : true
            },
            isAllow : {
                required : true
            }
        },
        messages : {
            productName : {
                remote : "产品名称已存在，请重新输入！"
            }
        },
        onkeyup : false,
        focusCleanup : true,
        success : "valid",
        submitHandler : function(form) {
            $(form).ajaxSubmit({
                type : "post", //提交方式  
                dataType : "json", //数据类型  
                url : action, //请求url 
                success : function(data) { //提交成功的回调函数  
                    var msg = '';
                	if('UPDATE' == type) {
                	    msg = '更新成功';
                	} else if('ADD' == type) {
                	    msg = '添加成功';
                	}
                	
                	if(_GLOBAL_SUCCESS == data.code) {
                	    layer.msg(msg, {
                            icon : 1,
                            time : 1000
                          }, function(){
                              layer_close();
                          });
                	}
                }
            });
        }
    });
});