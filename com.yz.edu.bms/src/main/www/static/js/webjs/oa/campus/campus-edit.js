//市缓存变量
var cityCache;
var districtCache;
//县区缓存变量
var districtCache;
$(function(){
	//定义院校变量，以免重复获取
	var empIdJson;
    var empName;
	//初始负责人下拉框
 	_simple_ajax_select({
			selectId : "empId",
			searchUrl : '/employ/list.do',
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
    	   dataType : "json", //数据类型  
           type: "POST",
           url: "/employ/list.do",
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
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	 var zipCode = _init_area_select("provinceId", "cityId", "areaId", varCampusInfo.provinceId, varCampusInfo.cityId,varCampusInfo.areaId);

	$("#form-campus-add").validate({
		rules : {
			campusName : {
				remote : { //验证校区名称是否存在
					type : "POST",
					url : '/campus/validateCampus.do', //servlet
					dataType: "json",           //接受数据格式   
					data : {
						exType : function() {
							return $("#exType").val();
						},
						campusName : function() {
							return $("#campusName").val();
						},
						oldCampusName : function(){
							return $("#oldCampusName").val();
						}
					}
				},
				required : true
			},
			financeNo : {
				remote : { //验证财务代码是否存在
					type : "POST",
					url : '/campus/validateFinanceNo.do', //servlet
					dataType: "json",           //接受数据格式   
					data : {
						exType : function() {
							return $("#exType").val();
						},
						financeNo : function() {
							return $("#financeNo").val();
						},
						oldFinanceNo : function (){
							return $("#oldFinanceNo").val();
						}
					}
				},
				required : true
			}
		},
		messages : {
		   campusName : {
			  remote : "校区名称已存在"
		   },
		   financeNo : {
			  remote : "校区财务代码已存在"
		   }
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/campus/campusUpdate.do', //请求url  
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