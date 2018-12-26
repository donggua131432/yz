function timeRule(){
	$.each($("#timeBody").find("input[type='text']"), function(i, data){
		$(data).rules('add', {required : true});
	});
}
	
function placeRule(){
	$.each($("#placeBody").find("input[type='number']"), function(i, data){
		$(data).rules('add', {required : true, range : [0,1000], digits : true});
	});
}	

$(function(){
	
	
	var exType = $("#exType").val();
	var url = '';
	

	
	if('ADD' == exType){
		url = "/examRoom/add.do";
	}else if('UPDATE' == exType){
		url = "/examRoom/update.do";
		var pd = '';
		
		for (var i = 0; i < places.length; i++) {
			var p = places[i];
			pd += '<tr class="text-c">';
			pd += '<td><input type="hidden" value="'+p.placeName+'" name="places['+i+'].placeName"/>'+p.placeName+'</td>';
			pd += '<td><input type="number" value="'+p.maxCount+'" name="places['+i+'].maxCount" class="input-text radius"  style="width:15px;text-align:center;"/></td>';
			pd += '</tr>';
		}
		$("#placeBody").html(pd);
		var td = '';
		
		for (var i = 0; i < times.length; i++) {
			var t = times[i];
			td += '<div style="margin-top:10px">';
			td += '日期：<input type="text" value="'+t.date+'" onfocus="WdatePicker({ dateFmt:\'yyyy-MM-dd\'})" id="date'+i+'" name="times['+i+'].date" class="input-text Wdate" style="width: 100px;" />';
			td += ' &nbsp; 时间段：<input type="text" value="'+t.startTime+'" onfocus="WdatePicker({ dateFmt:\'HH:mm\',minDate:\'#F{$dp.$D(\\\'date'+i+'\\\',{d:-1})}\'})" id="startTime'+i+'" name="times['+i+'].startTime" class="input-text Wdate" style="width: 100px;" /> -'; 
			td += ' <input type="text" value="'+t.endTime+'" onfocus="WdatePicker({ dateFmt:\'HH:mm\', minDate:\'#F{$dp.$D(\\\'startTime'+i+'\\\');}\'})" id="endTime'+i+'" name="times['+i+'].endTime" class="input-text Wdate" style="width: 100px;" />'; 
			td += '</div>';
		}
		td += '<input class="btn btn-secondary-outline radius" onclick="addTime()" type="button" value="+" style="position:absolute;right:23px;bottom:1px">';
		td += '<input class="btn btn-warning-outline radius" onclick="delTime()" type="button" value="-" style="position:absolute;right:-24px;bottom:1px">';
		$("#timeBody").html(td);
		//provinceCode = room.provinceCode;
	}
	var provinceCode = room.provinceCode;
	
    _init_area_select("provinceCode", "cityCode", "districtCode",room.provinceCode,room.cityCode,room.districtCode);
    $("#provinceCode").val(provinceCode);
    
    if(null == status){
		status = '1';
	}
    _init_radio_box("statusRadio", "status", dictJson.status, status);
    
	$("#form-charges-add").validate({
		rules : {
			epCode : {
				remote : { //验证角色名称是否存在
					type : "POST",
					url : "/examRoom/validateEpCode.do", //servlet
					data : {
						epCode : function() {
							return $("#epCode").val();
						},
						exType : exType
					}
				},
				required : true,
				maxlength : 10	
			},
			epName : {
				required : true,
				maxlength : 15	
			},
			provinceCode : {
				required : true
			},
			cityCode : {
				required : true
			},
			districtCode : {
				required : true
			},
			address : {
				required : true,
				maxlength : 50	
			},
			status : {
				required : true
			}
		},
		messages : {
			epCode : {
				remote : "考场编码已存在",
                maxlength:'考场编号最多可以输入10个字符'
			},
            epName:{
                maxlength:'考场名称最多可以输入15个字符'
			},
            address:{
                maxlength:'详细地址最多可以输入50个字符'
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
	
	placeRule();
	
	timeRule();
});

	function addPlace(){
		var i = $("#placeBody tr").length;
		var x = i+1;
		var dom = '';
		dom += '<tr class="text-c">';
		dom += '<td><input type="hidden" value="课室'+x+'" name="places['+i+'].placeName"/>课室'+x+'</td>';
		dom += '<td><input type="number" name="places['+i+'].maxCount" class="input-text radius"  style="width:15px;text-align:center;"/></td>';
		dom += '</tr>';
		
		$("#placeBody").append(dom);
		placeRule();
	}
	
	function delPlace(){
		var i = $("#placeBody tr").length;
		if(i==1){
			return;
		}
		$("#placeBody tr:last").remove();
		
	}
	
	function addTime(){
		var i = $("#timeBody div").length;
		var dom = '';
		dom += '<div style="margin-top:10px">';
		dom += '日期：<input type="text" onfocus="WdatePicker({ dateFmt:\'yyyy-MM-dd\'})" id="date'+i+'" name="times['+i+'].date" class="input-text Wdate" style="width: 100px;" />';
		dom += ' &nbsp; 时间段：<input type="text" onfocus="WdatePicker({ dateFmt:\'HH:mm\',minDate:\'#F{$dp.$D(\\\'date'+i+'\\\',{d:-1})}\'})" id="startTime'+i+'" name="times['+i+'].startTime" class="input-text Wdate" style="width: 100px;" /> -'; 
		dom += ' <input type="text" onfocus="WdatePicker({ dateFmt:\'HH:mm\', minDate:\'#F{$dp.$D(\\\'startTime'+i+'\\\');}\'})" id="endTime'+i+'" name="times['+i+'].endTime" class="input-text Wdate" style="width: 100px;" />'; 
		dom += '</div>';
		
		$("#timeBody").append(dom);
		timeRule();
		
	}
	
	function delTime(){
		var i = $("#timeBody div").length;
		if(i==1){
			return;
		}
		$("#timeBody div:last").remove();
	}
	