$(function() {
	//标签块
      $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
	  //修改
	  var empId = $("#topEmpId").val();
      var exType = $("#topExType").val();
      if(empId !=''){
    	  $('#tab_base').load('/recruiter/toBase.do', {
        	  'empId' : empId,
        	  'exType' : exType
          }); 
      }else{
    	  $('#tab_base').load('/recruiter/toBase.do', {
        	  'empId' :varEmpId,
        	  'exType' : varExType
          });
      }
      
      $(".tabBar").find("span").click(function() {
    	  var empId = $("#topEmpId").val();
          var exType = $("#topExType").val();
          var id = $(this).attr('name');
          if ('base' == id) {
        	  if(empId !=''){ //新增之后变成修改
        		  $('#tab_base').load('/recruiter/toBase.do', {
                	  'empId' : empId,
                	  'exType' : exType
                  }); 
        	  }else{
        		  $('#tab_base').load('/recruiter/toBase.do', {
                	  'empId' :varEmpId,
                	  'exType' : varExType
                  });  
        	  }
          } else if ('job' == id) {
        	  if(empId !=''){
        		  $('#tab_job').load('/recruiter/toJob.do', {
                      'empId' : empId,
                      'exType' : exType
                  });
        	  }else{
        		  $('#tab_job').load('/recruiter/toJob.do', {
                      'empId' :varEmpId,
                      'exType' : varExType
                  }); 
        	  }
             
          } else if ('perfChange' == id) {
        	  if(empId !=''){
        		  $('#tab_perfChange').load('/recruiter/toPerfChange.do', {
                      'empId' : empId,
                      'exType' : exType
                  });
        	  }else{
        		  $('#tab_perfChange').load('/recruiter/toPerfChange.do', {
                      'empId' :varEmpId,
                      'exType' : varExType
                  });
        	  }
              
          } else if ('annex' == id) {
        	  if(empId !=''){
        		  $('#tab_annex').load('/recruiter/toAnnexList.do', {
                      'empId' : empId
                  });
        	  }else{
        		  $('#tab_annex').load('/recruiter/toAnnexList.do', {
                      'empId' :varEmpId
                  });
        	  }
          }else if ('modify' == id) {
        	  if(empId !=''){
        		  $('#tab_modify').load('/recruiter/toModify.do', {
                      'empId' : empId
                  }); 
        	  }else{
        		  $('#tab_modify').load('/recruiter/toModify.do', {
                      'empId' :varEmpId
                  }); 
        	  }
          }
      }); 
    
  });