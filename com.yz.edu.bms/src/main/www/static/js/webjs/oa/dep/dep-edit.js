$(function(){
	//定义负责人变量，以免重复获取
	var empIdJson;
    var empName;
	//初始负责人下拉框
	var url = '/employ/list.do'
 	_simple_ajax_select({
		selectId : "empId",
		searchUrl : url,
		sData : {},
		showText : function(item) {
			return item.empName;
		},					
		showId : function(item) {
			return item.empId;
		},
		placeholder : '--请选择负责人--'
	});
    $.ajax({
           type: "POST",
           dataType : "json", //数据类型  
           url: url,
           success: function(data){
          	 empIdJson = data.body.data;
          	 for(var i=0;i<empIdJson.length;i++){
          		 if(empIdJson[i].empId == $("#empIds").val()){
          			empName = empIdJson[i].empName;
 		                    $("#empId").append(new Option(empName, $("#empIds").val(), false, true));
          		 }
          	 }
           }
    });
    
    //定义校区变量，以免重复获取
	var campusIdJson;
    var campusName;
	//初始校区下拉框
	url = '/campus/selectList.do'
 	_simple_ajax_select({
		selectId : "campusId",
		searchUrl : url,
		sData : {},
		showText : function(item) {
			return item.campusName;
		},					
		showId : function(item) {
			return item.campusId;
		},
		placeholder : '--请选择校区--'
	});
    $.ajax({
           type: "POST",
           dataType : "json", //数据类型  
           url: url,
           success: function(data){
        	   campusIdJson = data.body.data;
          	 for(var i=0;i<campusIdJson.length;i++){
          		 if(campusIdJson[i].campusId == $("#campusIds").val()){
          			campusName = campusIdJson[i].campusName;
 		                    $("#campusId").append(new Option(campusName, $("#campusIds").val(), false, true));
          		 }
          	 }
           }
    });
	_init_checkbox("recruitTypes","recruitTypes",dictJson["recruitType"],varDepInfo.recruits);
	
	_init_checkbox("jdIds","jdIds",dictJson["jtId"],varDepInfo.jdIds);
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	url = '/dep/validateDpName.do'
	$("#form-dep-add").validate({
		rules : {
			dpName : {
				remote : { //验证部门名称是否存在
					type : "POST",
					url : url, //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						campusId : function (){
							return $("#campusIds").val();	
						},
						dpName : function() {
							return $("#dpName").val();
						},
						oldDpName : function (){
							return $("#oldDpName").val();
						}
					}
				},
				required : true
			},campusId:{
				required : true
			}
		},
		messages : {
		   dpName : {
			remote : "部门名称已存在"
		   }
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/dep/depUpdate.do', //请求url  
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