var myDataTable;
			$(function() {
				 //初始化年级
				_init_select("grade",dictJson.grade);
			    //初始化专业层次
				_init_select("pfsnLevel",dictJson.pfsnLevel);
				 //初始化学期
				_init_select("semester",dictJson.semester);
				//初始化年度下拉框
				_init_select("year", dictJson.year);
				//初始是否启用
				_init_select("isAllow", [ {
					"dictValue" : "1",
					"dictName" : "是"
				}, {
					"dictValue" : "0",
					"dictName" : "否"
				} ]);
				//初始是否有课程资源
				_init_select("isResource", [ {
					"dictValue" : "1",
					"dictName" : "有"
				}, {
					"dictValue" : "0",
					"dictName" : "无"
				} ]);
				//初始化课程类型
				_init_select("courseType", dictJson.courseType);

				myDataTable = $('.table-sort').dataTable({
                    "processing": true,
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {      
						url : "/course/findAllCourse.do",
						type : "post",
						data : {
							"courseName" : function() {
								return $("#courseName").val();
							},
							"courseId" : function() {
								return $("#courseId").val();
							},
							"textbookName" : function() {
								return $("#textbookName").val();
							},
							"thpName" : function() {
								return $("#thpName").val();
							},
							"courseType" : function() {
								return $("#courseType").val();
							},
							"year" : function() {
								return $("#year").val();
							},
							"isAllow" : function() {
								return $("#isAllow").val();
							},
							"grade" : function() {
								return $("#grade").val();
							},
							"semester" : function() {
								return $("#semester").val();
							},
							"pfsnLevel" : function() {
								return $("#pfsnLevel").val();
							},
							"isResource" : function() {
								return $("#isResource").val();
							}
						}
					},
					"pageLength" : 10,
					"pagingType" : "full_numbers",
					"ordering" : false,
					"searching" : false,
					"createdRow" : function(row, data, dataIndex) {
						$(row).addClass('text-c');
						$(row).children('td').eq(5).attr(
								'style', 'text-align: left;');
						$(row).children('td').eq(6).attr(
								'style', 'text-align: left;');
						$(row).children('td').eq(8).attr(
								'style', 'text-align: left;');
					},
					"language" : _my_datatables_language,
					columns : [ {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : null
					} , {
						"mData" : null
					} , {
						"mData" : null
					}],
					"columnDefs" : [ {
						"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.courseId + '" name="courseIds"/>';
						},
						"targets" : 0
					}, {
						"render" : function(data, type, row, meta) {
							return _findDict("year", row.year);
						},
						"targets" : 1
					},{
						"render" : function(data, type, row, meta) {
							return row.courseId;
						},
						"targets" : 2
					},{
						"render" : function(data, type, row, meta) {
							return row.courseName;
						},
						"targets" : 3,
                        "class":"text-l"
					}, {
						"render" : function(data, type, row, meta) {
							return _findDict("courseType", row.courseType);
						},
						"targets" : 4
					},
					{
						"render" : function(data, type, row, meta) {
							row = row.textBook;
							var dom = '';
							for (var i = 0; i < row.length; i++) {
								dom += row[i].textbookId+": "+ row[i].textbookName+"</br>";
							}
							 return dom;
						},
						"targets" : 5
					}, {
						"render" : function(data, type, row, meta) { 
							var dom = '';
							row =row.teachPlan;
							for (var i = 0; i < row.length; i++) {
								var arr = row[i];
								if(null != arr){
	    							var recruitType=_findDict("recruitType", arr.recruitType);
	    							var pfsnLevel=_findDict("pfsnLevel", arr.pfsnLevel);
	                                    pfsnLevel= pfsnLevel.indexOf("高中")!=-1?"专科":"本科"
	    							var grade=_findDict("grade", arr.grade);
	    							var semester=_findDict("semester", arr.semester);
									dom += '['+grade+']'+'['+semester+']'+'['+arr.thpName+']'+arr.unvsName+':('+arr.pfsnName+')['+pfsnLevel+']';
									if(arr.testSubject){
										dom += '<span >['+arr.testSubject+']</span></br>';
									}else{
										dom += '</br>';
									}
								}
							}
							 return dom;
						},
						"targets" : 6
					}, {
						"render" : function(data, type, row, meta) {
							return row.stdtCount;
						},
						"targets" : 7
					}, {
						"render" : function(data, type, row, meta) {
							var dom = '';
							row =row.courseresource;
							for (var i = 0; i < row.length; i++) {
								var arr = row[i];
								if(null != arr){
									//dom+='<label class="form-label col-xs-4 col-sm-3">'+(i+1)+'：</label>';
									dom+='<a onclick="down(this);" href="javascript:;" courseResourceId="'+arr.resourceId+'"><span  class="c-blue" >'+(i+1)+': ' +arr.resourceName+'</span></a>';
									if(arr.isAllow=='0'){
										dom+='<span>&nbsp;[禁用]</span>';
									}
									
									dom+= '</br>';
								}
							}
							 return dom;
							
						},
						"targets" : 8
					},{
						"render" : function(data, type, row, meta) {
							return 1 == row.isAllow ? '<span class="label label-success radius">可选</span>' : '<span class="label radius">禁用</span>';
						},
						"targets" : 9
					},{
						"render" : function(data, type, row, meta) {
							var dom = '';

							dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.courseId + '\')" class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-edit"></i></a>';
							return dom;
						},
						//指定是第三列
						"targets" : 10
					} ]
				});
				
				
				// 文件下载
		    	 jQuery.download = function(url, method, resourceId){
		    	     jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
		    	                 '<input type="text" name="resourceId" value="'+resourceId+'"/>' + // 
		    	             '</form>')
		    	     .appendTo('body').submit().remove();
		    	 };

			});
			
			/*下载课程资源*/
			function down(e){
		    	 var courseResourceId = $(e).attr("courseResourceId");
		    	 $.download('/course/down.do','post',courseResourceId);
		     }
			
			
			/*用户-添加*/
			function member_add() {
				var url = '/course/edit.do' + '?exType=ADD';
				layer_show('添加课程信息', url, null, 510, function() {
					myDataTable.fnDraw(true);
				}, true);
			}
			
			/*用户-导入*/
			function excel_import() {
				var url = '/course/excelImport.do';
				layer_show('课程导入', url, null, 510, function() {
					myDataTable.fnDraw(true);
				});
			}
			
			/*用户-编辑*/
			function member_edit(courseId) {
				var url = '/course/edit.do' + '?courseId=' + courseId+ '&exType=UPDATE';
				layer_show('修改课程信息', url, null, 510, function() {

				}, true);
			}
			
			/*用户-查看*/
			function member_show(courseId) {
				var url = '/course/show.do' + '?courseId=' + courseId;
				layer_show('查看课程信息', url, null, 510, function() {
					myDataTable.fnDraw(false);
				});
			}
			
			/* “教学计划”导出*/
			function excel_export(){       
				var tableSetings=myDataTable.fnSettings();
				var paging_length=tableSetings._iDisplayLength;//当前每页显示多少  
				var page_start=tableSetings._iDisplayStart;//当前页开始  
			     $('<form method="post" accept-charset="UTF-8"  action="' + '/course/export.do' + '">'
			              +'<input type="text" name="length" value="'+paging_length+'"/>'
			              +'<input type="text" name="start" value="'+page_start+'"/>'
			              +'<input type="text" name="courseName" value="'+$("#courseName").val()+'"/>'
			              +'<input type="text" name="courseId" value="'+$("#courseId").val()+'"/>'
			              +'<input type="text" name="textbookName" value="'+$("#textbookName").val()+'"/>'
			              +'<input type="text" name="thpName" value="'+$("#thpName").val()+'"/>'
			              +'<input type="text" name="courseType" value="'+$("#courseType").val()+'"/>'
			              +'<input type="text" name="year" value="'+$("#year").val()+'"/>'
			              +'<input type="text" name="isAllow" value="'+$("#isAllow").val()+'"/>'
			              +'<input type="text" name="pfsnLevel" value="'+$("#pfsnLevel").val()+'"/>'
			              +'<input type="text" name="semester" value="'+$("#semester").val()+'"/>'
			              +'<input type="text" name="grade" value="'+$("#grade").val()+'"/>'
			              +'<input type="text" name="isResource" value="'+$("#isResource").val()+'"/>'
			              +'</form>').appendTo('body').submit().remove();
			}

			function _search() {
				myDataTable.fnDraw(true);
			}
			
			function exportCourse() {
                var url = '/course/exportList.do' ;
                layer_show('导出上课平台安排', url, null, 510, function() {
                }, true);
            }

			function exportTeachTaskBook() {
				var url = '/course/exportTeachTaskBook.do' ;
				layer_show('导出授课任务书', url, null, 400, function() {
				});
			}

			function delAll() {
                var chk_value = [];
                $("input[name=courseIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                if(chk_value == null || chk_value.length <= 0||chk_value=='[]'){
					layer.msg('未选择任何数据!', {
						icon : 5,
						time : 1000
					});
					return;
				}
				
                layer.confirm('确认要删除吗？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : '/course/deleteCourse.do',
                        data : {
                        	
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已删除!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTable.fnDraw(true);
                                $("input[name=all]").attr("checked", false);
                            }
                        }
                    });
                });
            }