function refresh(sys, type){
	    $.ajax({
          type: 'POST',
          url: '/sysCache/refresh.do',
          data: {
              sysBelong: sys,
              refreshType: type
          },
          dataType: 'json',
          success: function (data) {
              if(_GLOBAL_SUCCESS == data.code) {
                  layer.msg('刷新调用成功', {
                      icon: 1,
                      time: 1000
                  });
              }
          }
      });
	}
	 //发短信
  	function sendSms(){
  		layer.confirm('确认要发送短信给学员吗？',function(index){
  			//此处请求后台程序，下方是成功后的前台处理……
  			$.ajax({
  				type : 'POST',
  				url : '/recruit/sendSmsToStudent.do',
  				dataType : 'json',
  				success : function(data) {
  					myDataTable.fnDraw(true);
  					layer.msg('发送成功!',{icon: 5,time:1000});
  				},
  				error : function(data) {
  					layer.msg('发送失败！', {
  						icon : 1,
  						time : 1000
  					});
  					myDataTable.fnDraw(true);
  				},
  			});
  		});
  	} 