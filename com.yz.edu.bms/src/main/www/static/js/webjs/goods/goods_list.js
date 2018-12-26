$(function() {
	//标签块
      $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
      $('#tab_common').load('/goods/toCommon.do');
	 
      $(".tabBar").find("span").click(function() {
    	 
          var id = $(this).attr('name');
          if ('common' == id) {
        	  $('#tab_common').load('/goods/toCommon.do'); 
          } else if ('activity' == id) {
        	  $('#tab_activity').load('/goods/toActivity.do');
          } else if ('course' == id) {
              $('#tab_course').load('/goods/toCourseList.do');
          } else if ('textbook' == id) {
        	  $('#tab_textbook').load('/goods/toTextBookList.do');
          }
      }); 
    
  });