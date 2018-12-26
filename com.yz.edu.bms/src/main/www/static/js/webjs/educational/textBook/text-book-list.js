var myDataTable;
			$(function() {
				
				$('select').select2({
                    placeholder : "--请选择--",
                    allowClear : true,
                    width : "59%"
                });
				//初始化课程类型
				_init_select("textbookType", dictJson.courseType);
				_init_select("isBook",[
			  	 {
			  	 			"dictValue":"1","dictName":"是"
			  	 		},
			  	 {
			  	 			"dictValue":"0","dictName":"否"
			  	 		}
			  	 	]);

				myDataTable = $('.table-sort').dataTable({
                    "processing": true,
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {            
						url : "/textBook/findAllTextBook.do",
						type : "post",
						data : {        
							"textbookName" : function() {
								return $("#textbookName").val();
							},
							"textbookId" : function() {
								return $("#textbookId").val();
							},
							"isbn" : function() {
								return $("#isbn").val();
							},
							"author" : function() {
								return $("#author").val();
							},
							"publisher" : function() {
								return $("#publisher").val();
							},
							"textbookType" : function() {
								return $("#textbookType").val();
							},
							"isBook" : function() {
								return $("#isBook").val();
							},
							"alias" : function (){
								return $("#alias").val();
							}
						}
					},
					"pageLength" : 10,
					"pagingType" : "full_numbers",
					"ordering" : false,
					"searching" : false,
					"createdRow" : function(row, data, dataIndex) {
						$(row).addClass('text-c');
						$(row).children('td').eq(9).attr(
								'style', 'text-align: left;');
					},
					"language" : _my_datatables_language,
					columns : [ {
						"mData" : null
					}, {
						"mData" : 'textbookId'
					}, {
						"mData" : 'textbookName'
					}, {
						"mData" : null
					}, {
						"mData" : 'alias'
					}, {
						"mData" : 'isbn'
					}, {
						"mData" : null
					}, {
						"mData" : null
					}],
					"columnDefs" : [ {
						"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.textbookId + '" name="textbookIds"/>';
						},
						"targets" : 0
					},{
                        "targets" : 2,
                        "class":"text-l"
                    },{
						"render" : function(data, type, row, meta) {
							return _findDict("textbookType", row.textbookType);
						},
						"targets" : 3
					},{
						"render" : function(data, type, row, meta) { 
							var dom = '';
							row = row.professional;
                            if(row){
								for (var i = 0; i < row.length; i++) {
									pfsn = row[i];
                                    var recruitType=_findDict("recruitType", pfsn.recruitType)+'';
                                    var pfsnLevel=_findDict("pfsnLevel", pfsn.pfsnLevel)+'';
                                    (recruitType.indexOf('成人')==-1)?dom +='[国开]':dom +='[成教]';

                                    dom +="("+_findDict("grade", pfsn.grade)+")"+pfsn.unvsName+'<br>';
                                    (pfsnLevel.indexOf('高中')==-1)?dom +='[本科]':dom +='[专科]';

                                    dom +=pfsn.pfsnName+"("+pfsn.pfsnCode+")"

//									dom += "["+_findDict("recruitType", pfsn.recruitType)+"]"
//								       +pfsn.unvsName+":("+pfsn.pfsnCode+")"
//								       +pfsn.pfsnName+"["+_findDict("pfsnLevel", pfsn.pfsnLevel)+"]"
//								       +"("+_findDict("grade", pfsn.grade)+")</br>";
								}								
							}
							return dom;
						},
						"targets" : 6,
                        "class":"text-l no-warp"
					},{
						"render" : function(data, type, row, meta) {
							return 1 == row.isBook ? '<span class="label label-success radius">是</span>' : '<span class="label radius">否</span>';
						},
						"targets" : 7
					},{
						"render" : function(data, type, row, meta) {
							var dom = '';

							dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.textbookId + '\')" class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-edit"></i></a>';
							return dom;
						},
						//指定是第三列
						"targets" : 8
					} ]
				});

			});
			/*用户-添加*/
			function member_add() {
				var url = '/textBook/edit.do' + '?exType=ADD';
				layer_show('添加教材', url, null, 510, function() {
					myDataTable.fnDraw(true);
				});
			}
			
			/*用户-导入*/
			function excel_import() {
				var url = '/textBook/excelImport.do';
				layer_show('教材导入', url, null, 510, function() {
					myDataTable.fnDraw(true);
				});
			}
			
			/*教材导出*/
			 function excel_export() {
            	$("#export-form").submit();
             }
			
			/*用户-编辑*/
			function member_edit(textbookId) {
				var url = '/textBook/edit.do' + '?textbookId=' + textbookId+ '&exType=UPDATE';
				layer_show('修改教材', url, null, 510, function() {

				});
			}
			
			function _search() {
				myDataTable.fnDraw(true);
			}
			
			function delAll() {
				var chk_value = [];
				$("input[name=textbookIds]:checked").each(function() {
					chk_value.push($(this).val());
				});

				layer.confirm('确认要删除吗？', function(index) {
					$.ajax({
						type : 'POST',
						url : '/textBook/deletePublisher.do',
						data : {
							idArray : chk_value
						},
						dataType : 'json',
						success : function(data) {
							layer.msg('已删除!', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(false);
							$("input[name=all]").attr("checked", false);
						},
						error : function(data) {
							layer.msg('删除失败！', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(false);
							$("input[name=all]").attr("checked", false);
						},
					});
				});
			}