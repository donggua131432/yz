$(function(){
	
	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});
	
	//初始考试年度下拉框
	url = "/stdEnroll/getHomeCampusInfo.do"
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?isStop=0',
         success: function(data){
      	     var campusJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("campusId",campusJson); 	 
      	     }
         }
    });
});

function selectAllocation(){
	var chk_value = [];
	$(window.parent.document).find('#enrolledTable input[name=enrolledLearnIds]:checked').each(function() {
		chk_value.push($(this).val());
	});
	$(window.parent.document).find('#enrolledTable input[name=enrolledLearnIds]:checked').attr("checked", false);
    var url = "/stdEnroll/selectAllocation.do";
    layer.confirm('确认要给选择的学员分配归属校区吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : url,
			data : {
				idArray : chk_value,
				homeCampusId :$("#campusId").val()
			},
			dataType : 'json',
			success : function(data) {
				layer.msg('分配成功！', {icon : 1, time : 3000},function(){
					layer_close();
				});
				//myEnrolledDataTable.fnDraw(true);
				//$("input[name=checkAll]").attr("checked", false);
			},
			error : function(data) {
				layer.msg('分配失败！', {icon : 2, time : 3000},function(){
					layer_close();
				});
				//myEnrolledDataTable.fnDraw(true);
				//$("input[name=checkAll]").attr("checked", false);
			},
		});
	});
}