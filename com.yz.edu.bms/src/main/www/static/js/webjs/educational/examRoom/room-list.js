var myDataTable;
					$(function() {
						
						

						$("#status").select2({
							placeholder : "--请选择--",
							allowClear : true
						});
						
						_init_area_select("provinceCode", "cityCode", "districtCode","440000");
						
						
						
						$('select').select2({
		                    placeholder : "--请选择--",
		                    allowClear : true,
		                    width : "59%"
		                });
						
						_simple_ajax_select({
							selectId : "epNameSearch",
							searchUrl : "/examRoom/sEpName.do",
							sData : {
								
							},
							showText : function(item) {
								var text = item;
								return text;
							},
							showId : function(item) {
								return item;
							},
							placeholder : '--请选择考场--'
						});
						myDataTable = $('.table-sort')
								.dataTable(
										{
                                            "processing": true,
											"serverSide" : true,
											"dom" : 'rtilp',
											"ajax" : {
												url : "/examRoom/list.do",
												type : "post",
												data : {
													"epName" : function() {
														return $("#epNameSearch")
																.val() ? $("#epNameSearch").val() : '';
													},
													"status" : function() {
														return $("#status")
																.val() ? $("#status").val() : '';
													},
													"epCode" : function() {
														return $("#epCodeSearch")
																.val() ? $("#epCodeSearch").val() : '';
													},
													"provinceCode" : function() {
														return $("#provinceCode")
																.val() ? $("#provinceCode").val() : '';
													},
													"cityCode" : function() {
														return $("#cityCode")
																.val() ? $("#cityCode").val() : '';
													},
													"districtCode" : function() {
														return $("#districtCode")
																.val() ? $("#districtCode").val() : '';
													},
													"address" : function() {
														return $("#addressSearch").val() ? $("#addressSearch").val() : '';
													}

												}
											},
											"pageLength" : 10,
											"pagingType" : "full_numbers",
											"ordering" : false,
											"searching" : false,
											"createdRow" : function(row, data,
													dataIndex) {
												$(row).addClass('text-c');
											},
											"language" : _my_datatables_language,
											columns : [ {
												"mData" : null
											}, {
												"mData" : "epCode"
											}, {
												"mData" : "epName"
											}, {
												"mData" : "province"
											}, {
												"mData" : "address"
											}, {
												"mData" : null
											}, {
												"mData" : "remark"
											}, {
												"mData" : null
											} ],
											"columnDefs" : [
													{
														"render" : function(
																data, type,
																row, meta) {
															return '<input type="checkbox" value="'+ row.epId + '" name="epIds"/>';
														},
														"targets" : 0
													},
													{
														"render" : function(
																data, type,
																row, meta) {
															var dom = '';
															if (row.status == '1') {
																dom += '<a onClick="ep_stop(\''
																		+ row.epId
																		+ '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
															} else if (row.status == '2') {
																dom += '<a onClick="ep_start(\''
																		+ row.epId
																		+ '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
															}
															dom += ' &nbsp;';
															dom += '<a title="编辑" href="javascript:void(0)" onclick="ep_edit(\''
																	+ row.epId
																	+ '\')" class="ml-5" style="text-decoration:none">';
															dom += '<i class="iconfont icon-edit"></i></a>';
															dom += ' &nbsp;';
															dom += '<a title="删除" href="javascript:;" onclick="ep_del(this,\''
																	+ row.epId
																	+ '\')" class="ml-5" style="text-decoration: none">';
															dom += '<i class="iconfont icon-shanchu"></i></a>';
															return dom;
														},
														"targets" : 7
													},
													{
														"render" : function(
																data, type,
																row, meta) {
															if (row.status == 1) {
																return '<span class="label label-success radius">已启用</span>';
															} else {
																return '<span class="label label-danger radius">已禁用</span>';
															}
														},
														"targets" : 5
													}]
										});
					});

					function ep_add() {
						var url = '/examRoom/toAdd.do';
						layer_show('添加考场', url, 900, 600, function() {
							myDataTable.fnDraw(true);
						},true);

					}

					function ep_edit(epId) {
						var url = '/examRoom/toUpdate.do' + '?epId='
								+ epId;
						layer_show('修改考场', url, null, 510, function() {
							myDataTable.fnDraw(false);
						},true);
					}

					/*管理员-删除*/
					function ep_del(obj, id) {
						layer.confirm('确认要删除吗？', function(index) {
							$.ajax({
								type : 'POST',
								url : '/examRoom/delete.do',
								data : {
									epId : id
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										layer.msg('已删除!', {
											icon : 1,
											time : 1000
										});
									}
									myDataTable.fnDraw(false);
								}
							});
						});
					}

					function ep_stop(epId) {
						layer.confirm('确认要停用吗？', function(index) {
							//此处请求后台程序，下方是成功后的前台处理……
							$.ajax({
								type : 'POST',
								url : '/examRoom/block.do',
								data : {
									epId : epId
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										myDataTable.fnDraw(false);
										layer.msg('已停用!', {
											icon : 5,
											time : 1000
										});
									}
								}
							});
						});
					}

					function ep_start(epId) {
						layer.confirm('确认要启用吗？', function(index) {
							//此处请求后台程序，下方是成功后的前台处理……
							$.ajax({
								type : 'POST',
								url : '/examRoom/start.do',
								data : {
									epId : epId
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										myDataTable.fnDraw(false);
										layer.msg('已启用!', {
											icon : 6,
											time : 1000
										});
									}
								}
							});

						});

					}
					function searchEps() {
						myDataTable.fnDraw(true);
					}
					
					function updateStatusBatch(type){
						
						var url = '';
						if('block' == type){
							url = '/examRoom/blocks.do';
						} else {
							url = '/examRoom/starts.do';
						}
						
						var chk_value = [];
						var $input = $("input[name=epIds]:checked");
					
						$input.each(function() {
							chk_value.push($(this).val());
						});
						if(chk_value == null || chk_value.length <= 0){
							layer.msg('未选择任何数据!', {
								icon : 5,
								time : 1000
							});
							return;
						}
						layer.confirm('确认要删除吗？',function(index) {
							$.ajax({
									type : 'POST',
									url : url,
									data : {
										epIds : chk_value
									},
									dataType : 'json',
									success : function(data) {
										if (data.code == _GLOBAL_SUCCESS) {
											layer.msg('操作成功!', {
												icon : 1,
												time : 1000
											});
											myDataTable.fnDraw(false);
											$("input[name=all]").attr("checked",false);
										}
									}
							});
						});
					}

					function delAll() {
						var chk_value = [];
						var $input = $("input[name=epIds]:checked");
					
						$input.each(function() {
							chk_value.push($(this).val());
						});
						if(chk_value == null || chk_value.length <= 0){
							layer.msg('未选择任何数据!', {
								icon : 5,
								time : 1000
							});
							return;
						}
						layer.confirm('确认要删除吗？',function(index) {
							$.ajax({
									type : 'POST',
									url : '/examRoom/deletes.do',
									data : {
										epIds : chk_value
									},
									dataType : 'json',
									success : function(data) {
										if (data.code == _GLOBAL_SUCCESS) {
											layer.msg('已删除!', {
												icon : 1,
												time : 1000
											});
											myDataTable.fnDraw(true);
											$("input[name=all]").attr("checked",false);
										}
									}
							});
						});
					}