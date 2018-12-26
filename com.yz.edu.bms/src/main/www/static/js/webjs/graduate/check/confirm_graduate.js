$(function() {
	$("#stdName").text($("#stdNames").val());
})
function confirm_graduate(){
	 $.ajax({
         type: "POST",
         url: "/graduate/confirmGraduate.do",
         data:{
        	 "learnId":$("#learnId").val()
         },
         success: function(data){
        	 layer.msg('操作成功!', {icon : 1,time : 2000},function(){
					layer_close();
				});
			
         }
  });
}	