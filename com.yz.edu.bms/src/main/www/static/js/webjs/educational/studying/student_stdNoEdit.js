$(function(){
	var grade= _findDict('grade', info.grade);
	var stdName=info.stdName;
	$("#stdName").html(stdName+' ['+grade+']');
	
    var text = unvsText(info);
	$("#unvsName").html(text);
	$("#stdNo").val(info.stdNo);
    
	$("#form-charges-add").validate({
		rules : {
			stdNo : {
				required : true,
				maxlength : 50	
			}
		},
		messages : {
			stdNo:{
                maxlength:'学籍编号最多可以输入50个字符'
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/jStudying/editStdNo.do",//请求url  
				success : function(data) { //提交成功的回调函数  
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

function unvsText(data) {
    var pfsnName = data.pfsnName;
    var unvsName = data.unvsName;
    var recruitType = data.recruitType;
    var pfsnCode = data.pfsnCode;
    var pfsnLevel = data.pfsnLevel;

    var text = '';
    if(recruitType) {
        _findDict("recruitType", recruitType).indexOf('成人')==-1?text +='[国开]':text +='[成教]'
    }
    if(unvsName) {
        text += unvsName + "<br>";
    }

    if(pfsnLevel) {
        _findDict("pfsnLevel", pfsnLevel).indexOf('高中')==-1?text +='[本科]':text +='[专科]'
    }
    if(pfsnName) {
        text += pfsnName;
    }
    if(pfsnCode) {
        text += "(" + pfsnCode + ")";
    }
   return text;
}
