var pId = true;
var dictName = true;
var dictValue = true;
var orderNum = true;

$(function() {
    $('.skin-minimal input').iCheck({
        checkboxClass : 'icheckbox-blue',
        radioClass : 'iradio-blue',
        increaseArea : '20%'
    });

    isUpOrAdd();

    $("#form-member-add").validate({
        rules : {
            dictId : {
                remote : { //验证用户名是否存在
                    type : "POST",
                    url : '/sysDict/validate.do', //servlet
                    data : {
                        exType : function() {
                            return $("#exType").val();
                        },
                        dictId : function() {
                            return $("#dictId").val();
                        }
                    }
                },
                required : true
            },
            dictName : {
                required : 1 == 1 ? true : false,
            },
            dictValue : {
                required : dictValue,
            },
            orderNum : {
                required : orderNum,
            },
            pId : {
                required : pId,
            }
        },
        messages : {
            dictId : {
                remote : "参数名称已存在"
            }
        },
        onkeyup : false,
        focusCleanup : true,
        success : "valid",
        submitHandler : function(form) {
            $(form).ajaxSubmit({
                type : "post", //提交方式  
                dataType : "json", //数据类型  
                url : $("#exType").val() == "UPDATE" ? '/sysDict/editSysDict.do' : '/sysDict/insertSysDict.do', //请求url 
                success : function(data) { //提交成功的回调函数  
                	var opare = $("#exType").val();
                    if ("UPDATE" == opare) {
                    	layer_close();
                    }else{
                    	layer.msg('操作成功', {
                        icon : 1,
                        time : 1000
                        });
                    	$(".input-text").val("");
                    	$("select").val(null).trigger("change");
                    }
                },
                error : function(data) {
                    layer.msg('操作失败', {
                        icon : 1,
                        time : 1000
                    });
                }
            })
        }
    });
});

//根据当前操作显示相应页面
function isUpOrAdd() {
    var opare = $("#exType").val();
    if ("UPDATE" == opare) {
        $("#parent").hide();
        $("#isParent").hide();
        if ($("#dictValue").val() == '') {
            $("#dictVal").hide();
            $("#order").hide();
            dictValue = false;
            orderNum = false;
        }
    } else {
        //获取所有父级元素并渲染
		_simple_ajax_select({
			selectId : "pId",
			searchUrl : '/sysDict/getParents.do',
			sData : {},
			showText : function(item) {
				return item.dictName;
			},
			showId : function(item) {
				return item.dictId;
			},
			placeholder : '--父级--'
		});
       /*  $.ajax({
            url : '[[@{/sysDict/getParents.do}]]',
            data : {},
            dataType : 'json',
            success : function(data) {
                data = data.body;
                var parent = $("#pId");
                for (var i = 0; i < data.length; i++) {
                    var option = $("<option>").text(data[i].dictName).val(data[i].dictId)
                    parent.append(option);
                }
            }
        }); */

    }
}