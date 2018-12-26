var sg_myDataTable;
			$(function() {
				_init_select('recruitType', dictJson.recruitType);

				$('select').select2({
					placeholder : "--请选择--",
					allowClear : true,
					width : "59%"
				});

				sg_myDataTable = $('#sgTable').dataTable({
                    "processing": true,
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {
						url : "/scholarship/getSGList.do",
						type : "post",
						data : function(pageData) {
							return searchData_sg(pageData);
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
						"mData" : "dictId"
					}, {
						"mData" : "dictName"
					}, {
						"mData" : "dictValue"
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
							return '<input type="checkbox" value="'+ row.dictId + '" name="_dictId" />';
						},
						"targets" : 0
					}, {
						"render" : function(data, type, row, meta) {
							if ('1' == row.isEnable) {
								dom = '<span class="label label-success radius">启用</span>';
							} else {
								dom = '<span class="label label-default radius">禁用</span>';
							}
							return dom;
						},
						"targets" : 4
					}, {
                        "render" : function(data, type, row, meta) {
                            var dateTime= row.updateTime;
                            if(!dateTime){
                                return '-'
                            }
                            var date=dateTime.substring(0,10)
                            var time=dateTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 6,"class":"text-l no-warp"
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dateTime= row.createTime;
                            if(!dateTime){
                                return '-'
                            }
                            var date=dateTime.substring(0,10)
                            var time=dateTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 8,"class":"text-l no-warp"
                    },{
						"render" : function(data, type, row, meta) {
							var dom = '';
							dom = '<a title="编辑" href="javascript:;" onclick="edit_sg(\'' + row.dictId + '\')" class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-edit"></i></a>';
							if('1' == row.isEnable) {
    							dom += '<a title="禁用" href="javascript:;" onclick="enable_sg(\'' + row.dictId + '\',\'0\')" class="ml-5" style="text-decoration: none">';
    							dom += '<i class="iconfont icon-tingyong"></i></a>';
							} else {
								dom += '<a title="启用" href="javascript:;" onclick="enable_sg(\'' + row.dictId + '\',\'1\')" class="ml-5" style="text-decoration: none">';
  								dom += '<i class="iconfont icon-wancheng"></i></a>';
							}
							return dom;
						},
						//指定是第三列
						"targets" : 10
					} ]
				});
			});

			/*用户-编辑*/
			function refresh_cache_sg() {
				$.ajax({
					type : 'POST',
					url : '/scholarship/refresh.do',
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

			function showAddPage_sg() {
				var url = '/scholarship/toSGAdd.do';
				layer_show('新增分组', url, 800, 500, function() {
					refreshTable_sg(true);
				});
			}

			function edit_sg(dictId) {
				var url = '/scholarship/toSGUpdate.do' + '?dictId=' + dictId;
				layer_show('修改分组', url, 800, 500, function() {
					refreshTable_sg(false);
				});
			}

			function searchData_sg(pageData) {
				return {
					dictName : $("#_dictName").val() ? $("#_dictName").val() : '',
					start : pageData.start,
					length : pageData.length
				};
			}

			function batch_sg(ie) {
				var dictIds = '';
				$.each($("input[name='_dictId']:checked"), function(index, data) {
						dictIds += $(this).val() + ',';
				});

				if (dictIds && $.trim(dictIds) != '') {
				    dictIds = dictIds.substring(0, dictIds.length - 1);
					$.ajax({
						type : 'POST',
						url : '/scholarship/batch.do',
						data : {
							dictIds : dictIds,
							isEnable : ie
						},
						dataType : 'json',
						success : function(data) {
							if ('00' == data.code) {
								var text = '';
								if ('1' == ie) {
									text = '批量禁用成功';
								} else {
									text = '批量启用成功';
								}
								layer.msg(text, {
									icon : 1,
									time : 1000
								});
								refreshTable_sg(false);
							}
						},
					});
				} else {
					layer.msg("请选择分组信息", {
						icon : 5,
						time : 2000
					});
				}
			}

			function enable_sg(id, ie) {
				$.ajax({
					type : 'POST',
					url : '/scholarship/enable.do',
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
							}, function(){refreshTable_sg(false);});
						}
					},
				});
			}
			
			function refreshTable_sg(tag) {
			    sg_myDataTable.fnDraw(tag);
			    $("input[type='checkbox']").prop('checked', false);
			}