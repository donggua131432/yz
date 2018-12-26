function empIdChange(){
	var empId = $("#empId").val();
	if(empId){
		$("#year").removeAttr("disabled");
		$("#year").val(null).trigger("change");
		$("#month").val(null).trigger("change");
		$("#month").attr("disabled", "disabled");
		$.ajax({
	          type: "POST",
	          url: "/monthExpense/balance.do",
	          data: {
	              empId : function(){
	              	return $("#empId").val();
	              }
	          },
	          dataType: 'json',
	          success: function (data) {
	              if (data.code == _GLOBAL_SUCCESS) {
	                 var balance = data.body;
	                 $("#balance").val(balance);
	              }
	          }
	      });
	}else{
		$("#year").attr("disabled", "disabled");
	}
}

function yearChange(){
	var year = $("#year").val();
	if(year){
		$("#month").removeAttr("disabled");
		$("#month").val(null).trigger("change");
	}else{
		$("#month").attr("disabled", "disabled");
	}
}

$(function(){
	_init_select("year",dictJson.year);
	_init_select("month",dictJson.month);
	_simple_ajax_select({
        selectId: "empId",
        searchUrl:"/monthExpense/recruitList.do",
        sData: {
            
        },
        showText: function (item) {
        	var dpName="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        	if(item.dpName){
        		dpName=item.dpName +"&nbsp;&nbsp;-&nbsp;&nbsp;";
        	}
            var text = item.empName+"&nbsp;&nbsp;&nbsp;"+dpName+item.campusName+"&nbsp;&nbsp;-&nbsp;&nbsp;"+item.empStatus;
            return text;
        },
        showId: function (item) {
            return item.empId;
        },
        placeholder: '--请选择--'
    });
	
	$("#form-charges-add").validate({
		rules : {
			year : {
				required : true
			},
			month : {
				required : true
			},
			empId : {
				required : true
			},
			amount : {
				required : true,
				min : 0 
			}
		},
		messages : {
			
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			$(form).ajaxSubmit({
				type : "post", // 提交方式
				dataType : "json", // 数据类型
				url : "/monthExpense/insert.do", // 请求url
				success : function(data) { // 提交成功的回调函数
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
