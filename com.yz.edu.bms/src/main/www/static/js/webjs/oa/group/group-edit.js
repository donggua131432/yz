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
    
    //定义部门变量，以免重复获取
	var dpIdJson;
    var dpName;
	//初始校区下拉框
	url = '/dep/selectList.do'
 	_simple_ajax_select({
			selectId : "dpId",
			searchUrl : url,
			sData : {},
			showText : function(item) {
				return item.dpName;
			},					
			showId : function(item) {
				return item.dpId;
			},
			placeholder : '--请选择部门--'
		});
    $.ajax({
           type: "POST",
           dataType : "json", //数据类型  
           url: url,
           success: function(data){
        	   dpIdJson = data.body.data;
          	 for(var i=0;i<dpIdJson.length;i++){
          		 if(dpIdJson[i].dpId == $("#dpIds").val()){
          			dpName = dpIdJson[i].dpName;
 		                    $("#dpId").append(new Option(dpName, $("#dpIds").val(), false, true));
          		 }
          	 }
           }
    });
    if($("#exType").val() == "UPDATE"){
    	//显示创建人和创建时间
    	var dom = '';
    	dom +='<br><div class="line" style="margin-left:200px;width:200px"></div><br>';   
		dom += '<div class="row cl" id="createUser">';
		dom += '<label class="form-label col-xs-4 col-sm-3"><span class="c-red"></span>创建人：</label>';
		dom += '<div class="formControls col-xs-8 col-sm-9">';
		dom += '<input type="text" class="input-text" readonly=true value="'+ (createUser == null ?'':createUser) + '" placeholder="" id="createUser" name="createUser">';
		dom += '</div>';
		dom += '</div>';
		
		dom += '<div class="row cl" id="createTime">';
		dom += '<label class="form-label col-xs-4 col-sm-3"><span class="c-red"></span>创建时间：</label>';
		dom += '<div class="formControls col-xs-8 col-sm-9">';
		dom += '<input type="text" class="input-text" readonly=true value="'+ createTime + '" placeholder="" id="createTime" name="createTime">';
		dom += '</div>';
		dom += '</div>';
		
		$("#staffSelect").append(dom);
    }
	url = '/group/validateGroupName.do'
	
	$("#form-group-add").validate({
		rules : {
			groupName : {
				remote : { //验证部门名称是否存在
					type : "POST",
					url : url, //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						groupName : function() {
							return $("#groupName").val();
						},
						dpId : function(){
							return $("#dpId").val();
						},
						oldGroupName : function (){
							return $("#oldGroupName").val();
						}
						
					}
				},
				required : true
			},
			dpId : {
				remote : { //验证部门名称是否存在
					type : "POST",
					url : url, //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						groupName : function() {
							return $("#groupName").val();
						},
						dpId : function(){
							return $("#dpId").val();
						},
						oldGroupName : function (){
							return $("#oldGroupName").val();
						}
						
					}
				},
				required : true
			}
		},
		messages : {
		   groupName : {
			 remote : "部门招生组名称已存在"
		   },
		   dpId : {
		     remote : "部门招生组名称已存在"
		   }
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/group/groupUpdate.do', //请求url  
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
