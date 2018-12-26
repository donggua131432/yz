	  var myDataTable;
      $(function () {

  		_simple_ajax_select({
			selectId : "unvsId",
			searchUrl : '/bdUniversity/findAllKeyValue.do',
			sData : {},
			showText : function(item) {
				return item.unvs_name;
			},					
			showId : function(item) {
				return item.unvs_id;
			},
			placeholder : '--请选择院校--'
		});
		$("#unvsId").append(new Option("", "", false, true));
		_init_select("pfsnLevel",dictJson.pfsnLevel);
    	
		
		$("#unvsId").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
	     });
        $("#grade").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
	     });
		 $("#pfsnLevel").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
	     });
		 $("#pfsnId").append(new Option("", "", false, true));
		 $("#pfsnId").select2({
	            placeholder: "--请先选择院校--"
	     });
		
		_simple_ajax_select({
			selectId : "enrollUnvsId",
			searchUrl : '/bdUniversity/findAllKeyValue.do',
			sData : {},
			showText : function(item) {
				return item.unvs_name;
			},					
			showId : function(item) {
				return item.unvs_id;
			},
			placeholder : '--请选择院校--'
		});
		$("#enrollUnvsId").append(new Option("", "", false, true));
		
		
		//初始化年级下拉框
		 _init_select("grade",dictJson.grade);
		 
		 _init_select('recruitType', dictJson.recruitType);
		 $("#recruitType").change(function() {
				_init_select({
					selectId : 'grade',
					ext1 : $(this).val()
				}, dictJson.grade);

			});
		 _init_select('sg', dictJson.sg); //优惠分组
		 _init_select('scholarship', dictJson.scholarship);
   		 _init_select("inclusionStatus",dictJson.inclusionStatus);
   		 _init_select("stdType", dictJson.stdType);
   		 _init_select('stdStage', dictJson.stdStage);
   		 //_init_select('stdStage', dictJson.stdStage,"6");
   		
   		 $("#sg").change(function() { //联动
   			_init_select({
   				selectId : 'scholarship',
   				ext1 : $(this).val()
   			}, dictJson.scholarship);
   		 });
   		 
   		_init_select("isAssign",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
          myDataTable = $('.table-sort').dataTable({
              "processing": true,
              "serverSide": true,
              "dom": 'rtilp',
              "ajax": {
                  url: "/assign/findAssignList.do",
                  type: "post",
                  data: function (pageData) {
                      return search_data(pageData);
                  }
              },
              "pageLength": 10,
              "pagingType": "full_numbers",
              "ordering": false,
              "searching": false,
              "lengthMenu": [10, 20],
              "createdRow": function (row, data, dataIndex) {
                  $(row).addClass('text-c');
              },
              "language": _my_datatables_language,
              columns: [
                  {"mData": null},
                  {"mData": "stdNo"},
                  {"mData": "stdName"},
                  {"mData": null},
                  {"mData": null},
                  {"mData": "schoolRoll"},
                  {"mData": null},
                  {"mData": null}
              ],
              "columnDefs": [
                  {"targets": 0,"render": function (data, type, row, meta) {
                      return '<input type="checkbox" value="' + row.learnId + '" name="learnIds"/>';
                  }},{"targets":2,"class":"stuName"},
                  {"targets": 3,"render": function (data, type, row, meta) {
                      return _findDict('grade', row.grade);
                  }},
                  {"targets": 4,"render": function (data, type, row, meta) {
                      var text = unvsText(row);
                      return text ? text : '无';
                  },"class":"text-l"},
                  {"targets": 6,"render": function (data, type, row, meta) {
                      return _findDict('stdStage', row.stdStage);
                  }},
                  {"targets": 7,"render": function (data, type, row, meta) {
                	  var dom="";
                	  if(row.tutor)dom='是';
                	  else dom="否";
                      return dom;
                  }},
                  {"targets": 8,"render": function (data, type, row, meta) {
                      var dom = '';
                      dom = '<a title="分配" href="javascript:;" onclick="showAddPage(\'' + 1 + '\', \'' + row.learnId + '\')" class="tableBtn normal ml-5" style="text-decoration: none">分配</a>';
                      return dom;
                  }}
              ]
          });
      });

      var index;

      function showAddPage(type, learnId) {
          var url = '/assign/toAddPage.do' + '?addType=' + type;
          if (type == '1') {
              url += '&learnId=' + learnId;
          } else {
              var learnIds = '';
              if($("input[name='learnIds']:checked").length>1000){
            	  layer.msg('批量分配操作最多不能超过1000条！', {
                      icon: 5,
                      time: 1000
                  });
                  return false;
              }
              $("input[name='learnIds']:checked").each(function (index, data) {
                  var lId = $(this).val();

                  if (lId && 'undefined' != lId && 'null' != lId) {
                      learnIds += $(this).val() + ",";
                  }
              });
              if (learnIds && learnIds.length > 0) {
                  learnIds = learnIds.substring(0, learnIds.length - 1);
              } else {
                  layer.msg('请选择学员', {
                      icon: 5,
                      time: 1000
                  });
                  return false;
              }
              url += '&learnIds=' + learnIds;
          }
          layer_show('辅导员分配', url, 1200, 810, function () {
//              myDataTable.fnDraw(false);
          }, true);
      }
      
      //查询批量分配
      function queryDistribution(type){
    	 
    	  if($("#grade").val() == '' || $("#unvsId").val() == ''||$("#pfsnLevel").val() == ''){
              layer.msg('年级、院校、层次是必选的筛选条件，请先选择条件再批量分配！', {
                  icon : 1,
                  time : 2000
              });
              return false;
          }
    	 
    	  
    	  var url = '/assign/toAddPage.do' + '?addType=' + type;
    	  layer_show('辅导员分配', url, 1200, 810, function () {
//            myDataTable.fnDraw(false);
        }, true);
    	  
      }

      function _search() {
          myDataTable.fnDraw(true);
      }

      function search_data(pageData) {
          pageData = $.extend({},{start:pageData.start, length:pageData.length},$("#searchForm").serializeObject());
          return pageData;
      }

      //退学申请
      var leanId,outName;
      function student_out() {
          leanId=$("table input[name=learnIds]:checked:first").val();
          outName=$("table input[name=learnIds]:checked:first").parent().siblings('td.stuName').text();
          console.log(leanId);
          console.log(outName);
          var url = '/studentOut/edit.do' + '?exType=ADD';
          layer_show('添加退学学员', url, null, 510, function () {
              myDataTable.fnDraw(false);
          });
      }

      function unvsText(data) {
          var pfsnName = data.pfsnName;
          var unvsName = data.unvsName;
          var recruitType = data.recruitType;
          var pfsnCode = data.pfsnCode;
          var pfsnLevel = data.pfsnLevel;

          var text = '';
          if(recruitType) {
              if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                  text += "[成教]";
              }else {
                  text += "[国开]";
              }
          }
          if (unvsName) {
              text += unvsName + " ";
          }
          if(pfsnLevel) {
              if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                  text += "[专科]";
              }else {
                  text += "[本科]";
              }
          }
          if (pfsnName) {
              text += pfsnName;
          }
          if (pfsnCode) {
              text += "(" + pfsnCode + ")";
          }

          return text;
      }
      
		 function init_pfsn_select() {
		    	_simple_ajax_select({
					selectId : "pfsnId",
					searchUrl : '/baseinfo/sPfsn.do',
					sData : {
						sId :  function(){
							return $("#unvsId").val() ? $("#unvsId").val() : '';	
						},
						ext1 : function(){
							return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
						},
						ext2 : function(){
							return $("#grade").val() ? $("#grade").val() : '';
						}
					},
					showText : function(item) {
						var text = '(' + item.pfsnCode + ')' + item.pfsnName;
						text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
						return text;
					},
					showId : function(item) {
						return item.pfsnId;
					},
					placeholder : '--请选择专业--'
				});
				$("#pfsnId").append(new Option("", "", false, true));
		    }