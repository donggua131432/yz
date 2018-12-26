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

function queryAllocation(){
	var datas={};
	$.each($(window.parent.document).find('#enrolledForm').serializeArray(),function(){
		datas[this.name]=this.value;
	});
	datas["fpCampusId"] = $("#campusId").val();
    var url = "/stdEnroll/queryAllocation.do";
    layer.confirm('确认要给选择的学员分配归属校区吗？', function(index) {
        $.ajax({
            type : 'POST',
            url : url,
            data: datas,
            dataType : 'json',
            success : function(data) {
                if (data.code == _GLOBAL_SUCCESS) {
                	layer.msg('分配成功！', {icon : 1, time : 3000},function(){
						layer_close();
					});
                    //myEnrolledDataTable.fnDraw(true);
                }
            }
        });
    }); 
}