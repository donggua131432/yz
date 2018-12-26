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
	
	var seats = examClass.seats;
	var restSeats = examClass.restSeats;
	
	$("#seats").text((seats * 1 - restSeats) + '/' + seats);
	
	var time = examClass.startTime.replace('AM', '上午').replace('PM', '下午') + "-" + examClass.endTime;
	$("#examTime").text(time);

	var pd = '';
	
	if(null != places && places.length > 0){
		for (var i = 0; i < places.length; i++) {
			var p = places[i];
			pd += '<tr class="text-c">';
			pd += '<td><input type="hidden" value="'+p.placeName+'" name="places['+i+'].placeName"/>'+p.placeName+'</td>';
			pd += '<td><input type="number" value="'+p.maxCount+'" name="places['+i+'].maxCount" class="input-text radius"  style="width:15px;text-align:center;"/></td>';
			pd += '</tr>';
		}
		pd += '<input class="btn btn-secondary-outline radius" onclick="addPlace()" type="button" value="+" style="position:absolute;right:23px;bottom:1px">';
		pd += '<input class="btn btn-warning-outline radius" onclick="delPlace()" type="button" value="-" style="position:absolute;right:-24px;bottom:1px">';
		
		$("#placeBody").html(pd);
	}
    
	$("#form-charges-add").validate({
		rules : {
			
		},
		messages : {
			
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/examClass/divide.do", //请求url  
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