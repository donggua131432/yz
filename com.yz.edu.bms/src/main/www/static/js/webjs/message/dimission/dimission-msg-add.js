$(function(){
	
	 $("select").select2({
         placeholder: "--请选择--",
         allowClear: true
     });
	 
	 $('input').iCheck({
			checkboxClass : 'icheckbox-blue',
			radioClass : 'iradio-blue'
		});
	
	_simple_ajax_select({
		selectId : "empId",
		searchUrl : '/dimissionMsg/dimissionEmp.do',
		sData : {
		},
		showText : function(item) {
			var text = item.empName +' '+ item.mobile + ' ' + item.dpName + ' ' + item.campusName ;
			return text;
		},
		showId : function(item) {
			return item.empId;
		},
		placeholder : '--请选择--'
	});
	
	$("#form-charges-add").validate({
		rules : {
			empId : {
				required : true
			},
			msgType : {
				required : true
			}
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
				url : '/dimissionMsg/dimissionMsg.do', //请求url  
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

	function empChange(){
		var empId = $("#empId").val();
		if(empId){
			
			$.ajax({
	            type: "POST",
	            url: '/dimissionMsg/empStudents.do',
	            data: {
	                empId: empId
	            },
	            dataType: 'json',
	            success: function (data) {
	                if (data.code == _GLOBAL_SUCCESS) {
	                    var stds = data.body;
	                    var dom = '';
	                    for (var i = 0; i < stds.length; i++) {
	                        var std = stds[i];
	                        
	                        dom += '<tr>';
	                        dom += '<td>' + std.stdName + '</td>';
	                        dom += '<td>'+ std.mobile +'</td>';
	                        dom += '<td>'+ std.idCard +'</td>';
	                        dom += '</tr>';
	                    }
	                    $("#stds").html(dom);
	                }
	            }
	        });
			
		} else {
			 $("#stds").html("");
		}
	}