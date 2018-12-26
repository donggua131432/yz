var myDataTable;
			$(function() {
				_init_select('ext1', dictJson.recruitType);

				$('select').select2({
					placeholder : "--请选择--",
					allowClear : true,
					width : "59%"
				});

				myDataTable = $('.table-sort').dataTable({
                    "processing": true,
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {
						url : "/grade/getList.do",
						type : "post",
						data : function(pageData) {
							return searchData(pageData);
						}
					},

					"pageLength" : 10,
					"pagingType" : "full_numbers",
					"ordering" : false,
					"searching" : false,
					"lengthMenu" : [ 10, 20 ],
					"createdRow" : function(row, data, dataIndex) {
						$(row).addClass('text-c');
					},
					"language" : _my_datatables_language,
					columns : [ {
						"mData" : null
					}, {
						"mData" : null
					}, {
						"mData" : "dictName"
					}, {
						"mData" : null
					}, {
						"mData" : "orderNum"
					}, {
						"mData" : null
					}, {
						"mData" : "updateUser"
					}, {
						"mData" : null
					}, {
						"mData" : "createUser"
					}, {
						"mData" : null
					} ],
					"columnDefs" : [ {
						"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.dictId + '" name="dictId" />';
						},
						"targets" : 0
					}, {
						"render" : function(data, type, row, meta) {
							return _findDict('recruitType', row.ext1);
						},
						"targets" : 1
					}, {
						"render" : function(data, type, row, meta) {
							if ('1' == row.isEnable) {
								dom = '<span class="label label-success radius">启用</span>';
							} else {
								dom = '<span class="label label-default radius">禁用</span>';
							}
							return dom;
						},
						"targets" : 3
					}, {
                        "render" : function(data, type, row, meta) {
                            if(!row.updateTime){
                                return '-'
                            }
                            var date=row.updateTime.substring(0,10)
                            var time=row.updateTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 5,"class":"text-l"
                    },{
                        "render" : function(data, type, row, meta) {
                            if(!row.createTime){
                                return '-'
                            }
                            var date=row.createTime.substring(0,10)
                            var time=row.createTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 7,"class":"text-l"
                    },{
						"render" : function(data, type, row, meta) {
							var dom = '';
							dom = '<a title="编辑" href="javascript:;" onclick="edit(\'' + row.dictId + '\')" class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-edit"></i></a>';
							if('1' == row.isEnable) {
    							dom += '<a title="禁用" href="javascript:;" onclick="enable(\'' + row.dictId + '\',\'0\')" class="ml-5" style="text-decoration: none">';
    							dom += '<i class="iconfont icon-tingyong"></i></a>';
							} else {
								dom += '<a title="启用" href="javascript:;" onclick="enable(\'' + row.dictId + '\',\'1\')" class="ml-5" style="text-decoration: none">';
  								dom += '<i class="iconfont icon-wancheng"></i></a>';
							}
							return dom;
						},
						//指定是第三列
						"targets" : 9
					} ]
				});

			});

			/*用户-编辑*/
			function refresh_cache() {
				$.ajax({
					type : 'POST',
					url : '/grade/refresh.do',
					dataType : 'json',
					success : function(data) {
						if ('00' == data.code) {
							layer.msg("刷新调用成功！", {
								icon : 1,
								time : 1000
							});
						}
					},
				});
			}

			function showAddPage() {
				var url = '/grade/toAdd.do';
				layer_show('新增年级', url, 800, 360, function() {
//					refreshTable(true);
				});
			}

			function edit(dictId) {
				var url = '/grade/toUpdate.do' + '?dictId=' + dictId;
				layer_show('修改年级', url, 800, 360, function() {
					refreshTable(false);
				});
			}

			function searchData(pageData) {
				return {
					dictName : $("#dictName").val() ? $("#dictName").val() : '',
					ext1 : $("#ext1").val() ? $("#ext1").val() : '',
					start : pageData.start,
					length : pageData.length
				};
			}

			function batch(ie) {
				var dictIds = '';
				$.each($("input[name='dictId']:checked"), function(index, data) {
					dictIds += $(this).val() + ',';
				});

				if (dictIds) {
				    dictIds = dictIds.substring(0, dictIds.length - 1);
					$.ajax({
						type : 'POST',
						url : '/grade/batch.do',
						data : {
							dictIds : dictIds,
							isEnable : ie
						},
						dataType : 'json',
						success : function(data) {
							if ('00' == data.code) {
								var text = '';
								if ('1' == ie) {
									text = '批量启用成功';
								} else {
									text = '批量禁用成功';
								}
								layer.msg(text, {
									icon : 1,
									time : 1000
								});
                                myDataTable.fnDraw(false);
							}
						},
					});
				} else {
					layer.msg("请选择年级信息", {
						icon : 5,
						time : 2000
					});
				}
			}

			function enable(id, ie) {
				$.ajax({
					type : 'POST',
					url : '/grade/enable.do',
					data : {
						dictId : id,
						isEnable : ie
					},
					dataType : 'json',
					success : function(data) {
						if ('00' == data.code) {
							var text = '';
							if ('0' == ie) {
								text = '禁用成功';
							} else {
								text = '启用成功';
							}
							layer.msg(text, {
								icon : 1,
								time : 1000
							}, function(){myDataTable.fnDraw(false);});
						}
					},
				});
			}
			
			function refreshTable(tag) {
//			    myDataTable.fnDraw(tag);
			    $("input[type='checkbox']").prop('checked', false);
			}