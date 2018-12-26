var myDataTable;
		$(function() {
			//初始是否及格
		     _init_select("isPass",[
		  	 {
		  	 			"dictValue":"1","dictName":"是"
		  	 		},
		  	 {
		  	 			"dictValue":"2","dictName":"否"
		  	 		}
		  	 	]);
		 
		     //_init_select("year",dictJson.year);
		    //初始化年级下拉框
			 _init_select("grade",dictJson.grade);
			 _init_select("pfsnLevel", dictJson.pfsnLevel);
			 //初始化学期
			 _init_select("semester",dictJson.semester);
			 
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
			
			myDataTable = $('.table-sort').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/studentTScore/findAllStudentTScore.do",
							type : "post",
							data : {
								"grade" : function() {
									return $("#grade").val();
								},"unvsId" : function() {
									return $("#unvsId").val();
								}, "pfsnLevel" : function() {
									return $("#pfsnLevel").val();
								}, "pfsnId" : function() {
									return $("#pfsnId").val();
								}, "pfsnLevel" : function() {
									return $("#pfsnLevel").val();
								}, "stdName" : function() {
									return $("#stdName").val().trim();
								}, "mobile" : function() {
									return $("#mobile").val().trim();
								}, "idCard" : function() {
									return $("#idCard").val().trim();
								}, "schoolRoll" : function() {
									return $("#schoolRoll").val().trim();
								}, "isPass" : function() {
									return $("#isPass").val();
								}, "semester" : function() {
									return $("#semester").val();
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
							"mData" : null
						}, {
							"mData" : "stdName"
						}, {
							"mData" : null
						}, {
							"mData" : null
						},  {
							"mData" : "empName"
						},{
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : "ticketNo"
						}, {
							"mData" : null
						} ],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.learnId + '" name="learnIds"/>';
									},
									"targets" : 0
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";

										if(row.stdType =='2'){
                                            dom += '<p class="name-mark-box one-line">真实姓名：' + row.stdName + '<span class="name-mark mark-red">外</span></p>';
			                        	}else {
                                            dom +=row.stdName+"<br/>"
										}
										dom +="["+_findDict("grade",row.grade)+"]";
										_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?dom+="[成教]":dom+="[国开]";
										dom+=row.unvsName+":";
										dom+=row.pfsnName;
										dom+="["+_findDict("pfsnLevel",row.pfsnLevel)+"]";
										
										return dom;
									},
									"targets" : 1,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
										for (var i = 0; i < row.firstscores.length; i++) {
											var score = row.firstscores[i];
											dom += score.courseName + ':';
											if(score.isPass =='2'){
				                        		dom += '<span style="color:#f00">'+score.score+ '</span></br>';
				                        	}else{
				                        		dom += score.score+ '</br>';
				                        	}
										}
										return dom;
									},
									"targets" : 2,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
										for (var i = 0; i < row.secondscores.length; i++) {
											var score = row.secondscores[i];
											dom += score.courseName + ':';
											if(score.isPass =='2'){
				                        		dom += '<span style="color:#f00">'+score.score+ '</span></br>';
				                        	}else{
				                        		dom += score.score+ '</br>';
				                        	}
										}
										return dom;
									},
									"targets" : 3,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
										for (var i = 0; i < row.thirdscores.length; i++) {
											var score = row.thirdscores[i];
											dom += score.courseName + ':';
											if(score.isPass =='2'){
				                        		dom += '<span style="color:#f00">'+score.score+ '</span></br>';
				                        	}else{
				                        		dom += score.score+ '</br>';
				                        	}
										}
										return dom;
									},
									"targets" : 4,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
										for (var i = 0; i < row.fourscores.length; i++) {
											var score = row.fourscores[i];
											dom += score.courseName + ':';
											if(score.isPass =='2'){
				                        		dom += '<span style="color:#f00">'+score.score+ '</span></br>';
				                        	}else{
				                        		dom += score.score+ '</br>';
				                        	}
										}
										return dom;
									},
									"targets" : 5,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
										for (var i = 0; i < row.fivescores.length; i++) {
											var score = row.fivescores[i];
											dom += score.courseName + ':';
											if(score.isPass =='2'){
				                        		dom += '<span style="color:#f00">'+score.score+ '</span></br>';
				                        	}else{
				                        		dom += score.score+ '</br>';
				                        	}
										}
										return dom;
									},
									"targets" : 6,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
										for (var i = 0; i < row.sixscores.length; i++) {
											var score = row.sixscores[i];
											dom += score.courseName + ':';
											if(score.isPass =='2'){
				                        		dom += '<span style="color:#f00">'+score.score+ '</span></br>';
				                        	}else{
				                        		dom += score.score+ '</br>';
				                        	}
										}
										return dom;
									},
									"targets" : 7,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';

										dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.learnId + '\',\'' + row.idCard + '\',\'' + row.stdName + '\')" class="ml-5" style="text-decoration: none">';
										dom += '<i class="iconfont icon-edit f-18"></i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 8
								} ]
					});

		});

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
		/*用户-编辑*/
		function member_edit(learnId,idCard,stdName) {
			var url = '/studentTScore/edit.do' + '?learnId=' + learnId;
			layer_show('修改期末成绩', url, null, 800, function() {
				myDataTable.fnDraw(false);
			});
		}
		
		/*身份证-导入*/
        function excel_idCard_import() {
            var url = '/studentTScore/excelImportByIdCard.do';
            layer_show('期末成绩导入', url, null, 510, function() {
                myDataTable.fnDraw(true);
            });
        }
        
        /*学号-导入*/
        function excel_stdNo_import() {
            var url = '/studentTScore/excelImportByStdNo.do';
            layer_show('期末成绩导入', url, null, 510, function() {
                myDataTable.fnDraw(true);
            });
        }
		
        /*期末-生成成绩表*/
        function make_scores() {
            var url = '/studentTScore/makeScoreShow.do';
            layer_show('生成成绩表', url, null, 510, function() {
                myDataTable.fnDraw(false);
            }, true);
        }
        /* 期末成绩导出*/
        function query_excel_export() {
            var tableSetings = myDataTable.fnSettings();
            var paging_length = tableSetings._iDisplayLength;//当前每页显示多少
            var page_start = tableSetings._iDisplayStart;//当前页开始
            $('<form method="post" accept-charset="UTF-8" action="' + '/studentTScore/queryexport.do' + '">'
                + '<input type="text" name="length" value="' + paging_length + '"/>'
                + '<input type="text" name="start" value="' + page_start + '"/>'
                + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
                + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
                + '<input type="text" name="grade" value="' + $("#grade").val() + '"/>'
                + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
                + '<input type="text" name="stdName" value="' + $("#stdName").val() + '"/>'
                + '<input type="text" name="mobile" value="' + $("#mobile").val() + '"/>'
                + '<input type="text" name="idCard" value="' + $("#idCard").val() + '"/>'
                + '<input type="text" name="stdNo" value="' + $("#stdNo").val() + '"/>'
                + '<input type="text" name="isPass" value="' + $("#isPass").val() + '"/>'
                + '<input type="text" name="semester" value="' + $("#semester").val() + '"/>'
                + '</form>').appendTo('body').submit().remove();
        }
        
        /* 勾选期末成绩导出*/
        function check_excel_export() {
        	var url = '/studentTScore/checkexport.do';
        	 var chk_value = [];
             $("input[name=learnIds]:checked").each(function() {
                 chk_value.push($(this).val());
             });
             if (chk_value == null || chk_value.length <= 0) {
                 layer.msg('未选择任何数据!', {
                     icon : 5,
                     time : 1000
                 });
                 return;
             }
            $('<form method="post" action="' + '/studentTScore/checkexport.do' + '">'
                + '<input type="text" name="learnIdArray" value="' + chk_value + '"/>'
                + '</form>').appendTo('body').submit().remove();
        }

        /*导出成绩模板*/
        function template_excel_export() {
            var url = '/studentTScore/toTemplateExcel.do';
            layer_show('导出成绩模板', url, null, 400, function () {
			});
        }

		/*导出课程考试分析表*/
		function course_doc_export() {
			var url = '/studentTScore/toCourseDoc.do';
			layer_show('导出课程考试分析表', url, null, 400, function () {
			});
		}

		function _search(){
			myDataTable.fnDraw(true);
		}