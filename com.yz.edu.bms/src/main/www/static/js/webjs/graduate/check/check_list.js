 $(function() {
	//标签块
      $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
      $('#tab_data').load('/check/toData.do');
	 
      $(".tabBar").find("span").click(function() {
    	 
          var id = $(this).attr('name');
          if ('data' == id) {
        	  $('#tab_data').load('/check/toData.do'); 
          } else if ('picture' == id) {
        	  $('#tab_picture').load('/check/toPicture.do');
          } else if ('score' == id) {
              $('#tab_score').load('/check/toScore.do');
          } else if ('paper' == id) {
        	  $('#tab_paper').load('/check/toPaper.do');
          }else if ('fee' == id) {
              $('#tab_fee').load('/check/toFee.do');
          }
      }); 
    
  });