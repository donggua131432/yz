function addPlace(){
	var i = $("#placeBody tr").length;
	var x = i+1;
	var dom = '';
	dom += '<tr class="text-c">';
	dom += '<td><input type="text" name="reasons['+i+'].reason" class="input-text radius" maxlength="20" style="width:400px;"/></td>';
	dom += '</tr>';
	
	$("#placeBody").append(dom);
}

function delPlace(){
	var i = $("#placeBody tr").length;
	if(i==1){
		return;
	}
	$("#placeBody tr:last").remove();
	
}

function addSubject(){
	var i = $("#subjectBody tr").length;
	var x = i+1;
	var dom = '';
	
	
	dom += '<tr class="text-c">';
	dom += '<td>';
	dom += '<select class="select" id="grade-'+i+'" name="subjects['+i+'].grade" style="width: 150px">';
	dom += '</select> &nbsp; ';
	dom += '<select class="select" id="semester-'+i+'" name="subjects['+i+'].semester" style="width: 150px">';
	dom += '</select>';
	dom += '</td>';
	dom += '</tr>';
	
	$("#subjectBody").append(dom);
	_init_select("grade-"+i,dictJson.grade);
	_init_select("semester-"+i,dictJson.semester);
	
}

function delSubject(){
	var i = $("#subjectBody tr").length;
	if(i==1){
		return;
	}
	$("#subjectBody tr:last").remove();
	
}

$(function(){
	var exType = $("#exType").val();
	
	
	_init_radio_box("statusRadio", "status", dictJson.status, varStatus);
	
	
	var ueditorTips = UE.getEditor('tips');
	ueditorTips.addListener("ready", function () {
        // editor准备好之后才可以使用
        if(varTips !=null){
        	ueditorTips.setContent(varTips);	
        }
    });
	var url = '';
	if('ADD' == exType){
		url = "/examYear/add.do";
		_init_select("grade-0",dictJson.grade);
		_init_select("semester-0",dictJson.semester);
	}else if('UPDATE' == exType){
		url = "/examYear/update.do";
		
		var pDom = '';
		if(null != reasons && reasons.length > 0){
			
			for (var i = 0; i < reasons.length; i++) {
				var reason = reasons[i].reason;
				var erId = reasons[i].erId;
				pDom += '<tr class="text-c">';
				pDom += '<td><input type="text" name="reasons['+ i +'].reason" value="'+ reason +'" maxlength="40" class="input-text radius"  style="width:400px;"/>';
				pDom += '<input type="hidden" name="reasons['+ i +'].erId" value='+ erId +' /></td>';
				pDom += '</tr>';
				
			}
		} else {
			pDom += '<tr class="text-c">';
			pDom += '<td><input type="text" name="reasons[0].reason" class="input-text radius" maxlength="40" style="width:400px;"/></td>';
			pDom += '</tr>';
		}
		
		if(null != subjects && subjects.length > 0){
			$("#subjectBody").html('');
			for (var i = 0; i < subjects.length; i++) {
				var grade = subjects[i].grade;
				var semester = subjects[i].semester;
				var dDom = '';
				dDom += '<tr class="text-c">';
				dDom += '<td>';
				dDom += '<select class="select" id="grade-'+i+'" name="subjects['+i+'].grade" style="width: 150px">';
				dDom += '</select> &nbsp; ';
				dDom += '<select class="select" id="semester-'+i+'" name="subjects['+i+'].semester" style="width: 150px">';
				dDom += '</select>';
				dDom += '</td>';
				dDom += '</tr>';
				$("#subjectBody").append(dDom);
				_init_select("grade-"+i,dictJson.grade,grade);
				_init_select("semester-"+i,dictJson.semester,semester);
			}
			
		} else {
			var dDom = '';
			dDom += '<tr class="text-c">';
			dDom += '<td>';
			dDom += '<select class="select" id="grade-0" name="subjects[0].grade" style="width: 150px">';
			dDom += '</select> &nbsp; ';
			dDom += '<select class="select" id="semester-0" name="subjects[0].semester" style="width: 150px">';
			dDom += '</select>';
			dDom += '</td>';
			dDom += '</tr>';
			$("#subjectBody").html('');
			$("#subjectBody").append(dDom);
			_init_select("grade-0",dictJson.grade);
			_init_select("semester-0",dictJson.semester);
		}
		
		$("#placeBody").html(pDom);
		
	}
	
	
	$("#form-charges-add").validate({
		rules : {
			examYear : {
				remote : { //验证角色名称是否存在
					type : "POST",
					url :"/examYear/validateYear.do", //servlet
					data : {
						examYear : function() {
							return $("#examYear").val();
						},
						exType : function(){
							return $("#exType").val();
						},
						eyId : function(){
							return $("#eyId").val();
						}
					}
				},
				required : true,
				maxlength : 15
			},
			reasons : {
				required : true
			},
			status : {
				required : true
			}
		},
		messages : {
			examYear : {
				remote : "考试年度已存在"
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
							layer_close();
						});
					}
				}
			})
		}
	});
});