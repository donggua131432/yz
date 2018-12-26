$(function(){

	_init_campus_select("campusId", "dpId", "groupId",'/campus/selectAllList.','/dep/selectAllList.','/group/selectAllList.',varJobInfo.campusId,varJobInfo.dpId,varJobInfo.groupId);
	if(varJobInfo.dpJdIdList.length>0){
		$("#jdIdDiv").show();	
		jdIdJson = varJobInfo.dpJdIdList;
    	var jdId = [];
    	for (var i = 0; i < jdIdJson.length; i++) {
    		var depTitle = jdIdJson[i];
    		var data = {'dictValue':depTitle.jtId,'dictName':_findDict("jtId",depTitle.jtId)}
    		jdId.push(data);
    	}
		_init_checkbox("jdIds","jdIds",jdId,varJobInfo.jdIds);
	}
	//选择部门时,加载部门对应的职称
	 var jdIdJson = null;
	$("#dpId").change(function() {
		$("#jdIds").empty();  
		$("#jdIdDiv").show();
		var selected = $(this).val();
		//根据部门id加载职称
		 $.ajax({
		     type: "POST",
	         url: '/dep/findDepTitle.do',
	         data:{
	        	 dpId:selected
	         },
	         dataType : 'json',
	         success: function(data){
	        	jdIdJson = data.body; 
	        	var jdId = [];
	        	for (var i = 0; i < jdIdJson.length; i++) {
	        		var depTitle = jdIdJson[i];
	        		var data = {'dictValue':depTitle.jtId,'dictName':_findDict("jtId",depTitle.jtId)}
	        		jdId.push(data);
	        	}
				_init_checkbox("jdIds","jdIds",jdId,varJobInfo.jdIds);
	         }
	    });
		
	});
	
	_init_select("empStatus",dictJson.empStatus,varJobInfo.empStatus);
	
	if(varJobInfo.yzId !=null){
		$("#yzId").attr({"disabled":true});
		$("#ifNeedRelate").val("1");
	}
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#form-job-add").validate({
		rules : {
			campusId : {
				required : true
			},
			dpId : {
				required : true
			},
			yzId : {
				remote : { //验证远智编码是否存在
					type : "POST",
					url : '/recruiter/validateYzCode.do', //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						yzCode : function() {
							return $("#yzId").val();
						},
						empId : function (){
							return $("#empId").val();
						}
					}
				},
				required : true
			},
			recruitCode : {
				remote : { //验证招生编码是否存在
					type : "POST",
					url : '/recruiter/validateRecruitCode.do', //servlet
					data : {
						exType : function() {
							return $("#exType").val();
						},
						recruitCode : function() {
							return $("#recruitCode").val();
						},
						empId : function (){
							return $("#empId").val();
						}
					}
				},
				required : true
			},
			entryDate : {
				required : true
			},
			pactStartTime : {
				required : true
			},
			pactEndTime : {
				required : true
			}
		},
		messages : {
		   yzId : {
			 remote : "员工远智编码不存在或者不能重复"
		   },
		   recruitCode : {
		     remote : "员工招生编码不能重复"
		   }
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : '/recruiter/jobUpdate.do', //请求url  
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {
							icon : 1,
							time : 1000
							});
						}
					}
			    });
			}
	   });
	
});
