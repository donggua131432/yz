var myDataTable;
					$(function() {


						myDataTable = $('.table-sort')
								.dataTable(
										{
											"serverSide" : true,
											"dom" : 'rtilp',
											"ajax" : {
												url : "/banner/list.do",
												type : "post",
												data : {

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
												"mData" : null
											}, {
												"mData" : "bannerName"
											}, {
												"mData" : "redirectUrl"
											}, {
												"mData" : "sort"
											}, {
												"mData" : null
											}, {
												"mData" : null
											} ],
											"columnDefs" : [
													{
														"render" : function(
																data, type,
																row, meta) {
															return '<input type="checkbox" value="'+ row.bannerId + '" name="bannerIds"/>';
														},
														"targets" : 0
													},
													{
														"render" : function(
																data, type,
																row, meta) {
															
															var dom = '';
															var urlPath = _FILE_URL + row.bannerUrl + '?' + Date.parse(new Date());
															dom += '<img src="'+urlPath+'" height="100" width="250" />';
															return dom;
														},
														"targets" : 1
													},
													{
														"render" : function(
																data, type,
																row, meta) {
															var dom = '';
															if (row.isAllow == '1') {
																dom += '<a onClick="banner_stop(\''
																		+ row.bannerId
																		+ '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
															} else if (row.isAllow == '0') {
																dom += '<a onClick="banner_start(\''
																		+ row.bannerId
																		+ '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
															}
															dom += ' &nbsp;';
															dom += '<a title="编辑" href="javascript:void(0)" onclick="banner_edit(\''
																	+ row.bannerId
																	+ '\')" class="ml-5" style="text-decoration:none">';
															dom += '<i class="iconfont icon-edit"></i></a>';
															dom += ' &nbsp;';
															dom += '<a title="删除" href="javascript:;" onclick="banner_del(this,\''
																	+ row.bannerId
																	+ '\')" class="ml-5" style="text-decoration: none">';
															dom += '<i class="iconfont icon-shanchu"></i></a>';
															return dom;
														},
														"targets" : 6
													},
													{
														"render" : function(
																data, type,
																row, meta) {
															if (row.isAllow == '1') {
																return '<span class="label label-success radius">已启用</span>';
															} else {
																return '<span class="label label-danger radius">已禁用</span>';
															}
														},
														"targets" : 5
													} ]
										});
					});

					function banner_add() {
						var url = '/banner/toAdd.do';
						layer_show('添加Banner', url, 900, 600, function() {
							myDataTable.fnDraw(true);
						});

					}

					function banner_edit(bannerId) {
						var url = '/banner/toEdit.do' + '?bannerId='
								+ bannerId;
						layer_show('修改Banner', url, null, 510, function() {
							myDataTable.fnDraw(true);
						});
					}

					/*管理员-删除*/
					function banner_del(obj, id) {
						layer.confirm('确认要删除吗？', function(index) {
							$.ajax({
								type : 'POST',
								url : '/banner/delete.do',
								data : {
									bannerId : id
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										layer.msg('已删除!', {
											icon : 1,
											time : 1000
										});
									}
									myDataTable.fnDraw(true);
								}
							});
						});
					}

					function banner_stop(bannerId) {
						layer.confirm('确认要停用吗？', function(index) {
							//此处请求后台程序，下方是成功后的前台处理……
							$.ajax({
								type : 'POST',
								url : '/banner/block.do',
								data : {
									bannerId : bannerId,
									exType : 'BLOCK'
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										myDataTable.fnDraw(true);
										layer.msg('已停用!', {
											icon : 5,
											time : 1000
										});
									}
								}
							});
						});
					}

					function banner_start(bannerId) {
						layer.confirm('确认要启用吗？', function(index) {
							//此处请求后台程序，下方是成功后的前台处理……
							$.ajax({
								type : 'POST',
								url : '/banner/block.do',
								data : {
									bannerId : bannerId,
									exType : 'START'
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										myDataTable.fnDraw(true);
										layer.msg('已启用!', {
											icon : 6,
											time : 1000
										});
									}
								}
							});

						});

					}
					function searchBanner() {
						myDataTable.fnDraw(true);
					}

					function delAll() {
						var chk_value = [];
						var $input = $("input[name=bannerIds]:checked");

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
														url : '/banner/deleteBanners.do',
														data : {
															bannerIds : chk_value
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