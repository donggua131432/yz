var myDataTable,myDataTableExam
  $(function() {
	  //标签块table-sortfinancial
      $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
      //初始化年度下拉框
      _init_select("year", dictJson.year);
      //初始化年级下拉框
      _init_select("grade", dictJson.grade);
      //初始化专业层次下拉框
      _init_select("pfsnLevel", dictJson.pfsnLevel);
      
      _init_select("semester", dictJson.semester);
      
      _init_select("examYear", dictJson.year);
      //初始化院校名称下拉框
      _simple_ajax_select({
          selectId: "unvsId",
          searchUrl: '/bdUniversity/findAllKeyValue.do',
          sData: {},
          showText: function (item) {
              return item.unvs_name;
          },
          showId: function (item) {
              return item.unvs_id;
          },
          placeholder: '--请选择--'
      });
      $("#unvsId").append(new Option("", "", false, true));
      myDataTable = $('.table-sort').dataTable(
  			{
  				"serverSide" : true,
  				"dom" : 'rtilp',
  				"ajax" : {
  					url : "/courseStat/courseStatList.do",
  					type : "post",
  					data : {
  						"unvsId" : function (){
  							return $("#unvsId").val();
  						},
  						"pfsnName" : function (){
  							return $("#pfsnName").val();
  						},
  						"pfsnLevel" : function (){
  							return $("#pfsnLevel").val();
  						},
  						"semester" : function (){
  							return $("#semester").val();
  						},
  						"year" : function (){
  							return $("#year").val();
  						},
  						"grade" : function (){
  							return $("#grade").val();
  						}
  					}
  				},
  				"pageLength" : 10,
  				"pagingType" : "full_numbers",
  				"ordering" : false,
  				"searching" : false,
  				"createdRow" : function(row, data, dataIndex) {
  					$(row).addClass('text-c');
  				},
  				"language" : _my_datatables_language,

  				columns : [ {
  					"mData" : "grade"
  				}, {
  					"mData" : "unvsName"
  				}, {
  					"mData" : null
  				}, {
  					"mData" : null
  				}, {
  					"mData" : "pfsnName"
  				}, {
  					"mData" : "year"
  				}, {
  					"mData" : "peopleNum"
  				}, {
  					"mData" : null
  				}, {
  					"mData" : null
  				}],
  				"columnDefs" : [{"targets" : 1,"class":"text-l"},
  				            {"render" : function(data, type, row, meta) {
  								return _findDict("semester", row.semester);
  							},
  							"targets" : 2
  							},
  							{"render" : function(data, type, row, meta) {
  								 return _findDict("pfsnLevel", row.pfsnLevel);
  								},
  								"targets" : 3,"class":"text-l"
  							},{"targets" : 4,"class":"text-l"},
  							{"render" : function(data, type, row, meta) {
  									var dom = '';
  	                                row = row.testSubjectMap;
  	                                if(row && row.length >0){
  	                                	 for (var i = 0; i < row.length; i++) {
  	                                		if(row[i].testSubject){
  	                                			dom += row[i].testSubject+"</br>";	
  	                                		}
  	 	                                    
  	 	                                }
  	                                }
  	                               
  									return dom;
  								},
  								"sClass":'text-l',
  								"targets" : 7
  							},
  							{"render" : function(data, type, row, meta) {
									var dom = '';
	                                row = row.courseMap;
	                                if(row && row.length >0){
	                                	 for (var i = 0; i < row.length; i++) {
	                                		if(row[i].courseName){
	                                			dom += row[i].courseName+"</br>";	
	                                		}
	 	                                    
	 	                                }
	                                }
	                               
									return dom;
								},
								"sClass":'text-l',
								"targets" : 8
							}
  					   ]
  			});
      
      myDataTableExam = $('.table-sort-exam').dataTable(
    			{
    				"serverSide" : true,
    				"dom" : 'rtilp',
    				"ajax" : {
    					url : "/courseStat/examStatList.do",
    					type : "post",
    					data : {
    						"testSubject" : function (){
    							return $("#testSubject").val();
    						},
    						"courseName" : function (){
    							return $("#courseName").val();
    						},
    						"examName" : function (){
    							return $("#examName").val();
    						},
    						"examYear" : function (){
    							return $("#examYear").val();
    						}
    					}
    				},
    				"pageLength" : 10,
    				"pagingType" : "full_numbers",
    				"ordering" : false,
    				"searching" : false,
    				"createdRow" : function(row, data, dataIndex) {
    					$(row).addClass('text-c');
    				},
    				"language" : _my_datatables_language,

    				columns : [ {
    					"mData" : "testSubject"
    				},{
    					"mData" : "courseName"
    				},  {
    					"mData" : null
    				}, {
    					"mData" : "peopleNum"
    				}],
    				"columnDefs" : [
                                {"targets" : 0,"class":"text-l"},
    				            {"render" : function(data, type, row, meta) {
    								return "["+row.year+"]"+row.testSubject;
    							},
    							"targets" : 2,"class":"text-l"
    							}
    					   ]
    			});
  });
  function exportCourseStat(){
	if($("#year").val()==""){
		layer.msg('请选择考试年度再导出！', {
            icon : 1,
            time : 2000
        });
        return;
	}
  	$("#export-form").submit();
  }
  function searchStatList() {
  	myDataTable.fnDraw(true);
  } 
  function searchExamStatList() {
	  	myDataTableExam.fnDraw(true);
  } 
  function exportExamStat(){
	  	$("#export-form-exam").submit();
  }
  
  /* 考试科目导入*/
  function testsubject_excel_import(){
  	var url = '/teachPlan/testSubjectImport.do';
      layer_show('考试科目导入', url, null, 510, function () {
          myDataTable.fnDraw(true);
      });
  }