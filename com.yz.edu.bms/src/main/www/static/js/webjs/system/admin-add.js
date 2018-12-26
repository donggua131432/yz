function checkFuncList(roleId){
		var attr = global_attr;
		if($.inArray(roleId, attr) >= 0){
			return true;
		}
		return false;
	};
	
	function inputStaff(isStaff){
		if(isStaff == 1){
			var dom = '';
			dom +='<div class="row cl" id="staffInput">';
			dom +='<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>分配员工：</label>';
            dom +='<div class="formControls col-xs-8 col-sm-9">';
            dom +='<select class="select js-data-example-ajax" id="campusId" name="campusId" style="width:30%" required></select>&nbsp;'; 
            dom +='<select class="select" id="dpId" name="dpId" style="width:30%" required></select>&nbsp;';
            dom +='<select class="select" id="empId" name="empId" style="width:30%" required></select>';
            dom +='</div>';
            dom +='</div>';
			$("#staffSelect").append(dom);
		}else if(isStaff == 0){
			$("#staffInput").remove();
		}
	};
	
	$(function(){
		
		if($("#exType").val() == "UPDATE"){
			$(".permission-list-admin input:checkbox").each(function(){
				$(this).attr("checked",checkFuncList($(this).val()));
			});
		}
		
		$('#isStaff').on('ifChecked', function(event){  
			inputStaff(1);
			_init_campus_select_emp("campusId", "dpId", "empId",'/campus/selectAllList.do','/dep/selectAllList.do','/employ/selectAllList.do',global_campusId,global_dpId,global_empId);
		}); 
		$('#notStaff').on('ifChecked', function(event){  
			inputStaff(0);
		}); 
		
		$('.skin-minimal input').iCheck({
			checkboxClass: 'icheckbox-blue',
			radioClass: 'iradio-blue',
			increaseArea: '20%'
		});
		
		/* $.validator.addMethod("userName", function(value, element) {       
	         return this.optional(element) || /^[\u4e00-\u9fa5]{2,6}$|^[a-zA-Z]{4,12}$/gi.test(value);       
	    }, "输入2-4个汉字或者4-20个英文。"); */
		
		$("#form-admin-add").validate({
			rules : {
				userName : {
					remote : { //验证角色名称是否存在
						type : "POST",
						url : '/user/validateUser.do', //servlet
						data : {
							userName : function() {
								return $("#userName").val();
							},
							oldUserName : function(){
								return $("#oldUserName").val();
							}
							
						}
					},
					required : true
				},
				roleIds : {
					required : true
				},
				isStaff : {
					required : true
				},
				realName : {
					required : true
				}
			},
			messages : {
				userName : {
					remote : "用户名已存在"
				}
			},
			onkeyup : false,
			focusCleanup : true,
			success : "valid",
			submitHandler : function(form) {
				$(form).ajaxSubmit({
					type : "post", //提交方式  
					dataType : "json", //数据类型  
					url : '/user/userUpdate.do', //请求url  
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
		$("#checkBody").find('dd').each(function (i,e) {
			if($(e).find('span').html()=='超级管理员'){
	            $(this).remove()
	        }
	    });
		
		var isStaff = global_isStaff;
		inputStaff(isStaff);
		if($("#exType").val() == "UPDATE"){
			_init_campus_select_emp("campusId", "dpId", "empId",'/campus/selectAllList.do','/dep/selectAllList.do','/employ/selectAllList.do',global_campusId,global_dpId,global_empId);
		}
	});
	
