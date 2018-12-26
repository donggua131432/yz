$(function() {
	//标签块
      $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
	  //修改
	  var empId = $("#topEmpId").val();
      var exType = $("#topExType").val();
      if(empId !=''){
    	  $('#tab_base').load('/teacher/toBase.do', {
        	  'empId' : empId,
        	  'exType' : exType
          }); 
      }else{
    	  $('#tab_base').load('/teacher/toBase.do', {
        	  'empId' : varEmpId,
        	  'exType' : varexType
          });
      }
      
      $(".tabBar").find("span").click(function() {
    	  var empId = $("#topEmpId").val();
          var exType = $("#topExType").val();
          var id = $(this).attr('name');
          if ('base' == id) {
        	  if(empId !=''){ //新增之后变成修改
        		  $('#tab_base').load('/teacher/toBase.do', {
                	  'empId' : empId,
                	  'exType' : exType
                  }); 
        	  }else{
        		  $('#tab_base').load('/teacher/toBase.do', {
                	  'empId' : varEmpId,
                	  'exType' : varexType
                  });  
        	  }
          }else if ('annex' == id) {
        	  if(empId !=''){
        		  $('#tab_annex').load('/teacher/toAnnexList.do', {
                      'empId' : empId
                  });
        	  }else{
        		  $('#tab_annex').load('/teacher/toAnnexList.do', {
                      'empId' : varEmpId
                  });
        	  }
          }
      }); 
    
  });