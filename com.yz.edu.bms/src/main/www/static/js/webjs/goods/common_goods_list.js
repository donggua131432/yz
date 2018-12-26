var myDataTable;
		$(function() {
			_init_select("goodsUse",dictJson.goodsUse);
			  //初始状态
	        _init_select("isAllow", [
	            {
	                "dictValue": "1", "dictName": "是"
	            },
	            {
	                "dictValue": "0", "dictName": "否"
	            }
	        ]);
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"bRetrieve": true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/goods/list.do",
							data : {
								"goodsName" : function(){
									return $("#goodsName").val();
								},
								"isAllow" : function(){
									return $("#isAllow").val();
								},
								"goodsUse" : function(){
									return $("#goodsUse").val();
								},
								"goodsType" : function(){
									return "1";
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
							"mData" : null
						}, {
							"mData" : "goodsName"
						}, {
							"mData" : "goodsCount"
						}, {
							"mData" : "costPrice"
						}, {
							"mData" : "originalPrice"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.goodsId + '" class="sendIdsCheckBox" name="goodsIds"/>';
									},
									"targets" : 0
									},
									{
										"render" : function(data, type, row, meta) {
											 if(row.annexUrl == null){
												 return "暂未上传照片"; 
											 }else{
												 return '<img style="width: 140px; height: 140px;" src="'+(_FILE_URL + row.annexUrl + "?" + Date.parse(new Date()))+'">'; 
											 }
										},
										"targets" : 1
									},
									{
										 "render" : function(data, type, row, meta) {
										    var dom = '';
										    var strs= new Array(); //定义一数组 
										    if(row.goodsUse !=null){
										    	 strs=row.goodsUse.split(";"); //字符分割 
										    }
										    for (i=0;i<strs.length ;i++ ) 
										    { 
										    	var goodsUse = strs[i];
												if (i == (strs.length - 1)) {
													dom += _findDict("goodsUse",goodsUse) + ' ';
													break;
												}
												dom += _findDict("goodsUse",goodsUse)+ '; ';
										    }
										    if (strs.length < 1) {
												dom = '无';
											}
											return dom;
										},
										"targets" : 6
									},
									{
										"render" : function(data, type, row, meta) {
											return 0 == row.isAllow ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
										},
										"targets" : 7
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
										dom += '<a title="编辑" href="javascript:void(0)" onclick="common_edit(\'' + row.goodsId + '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										
										return dom;
									},
									//指定是第三列
									"targets" : 8
								} ]
					});

            $('.checkAll').on('click',function () {
                if($(this).prop("checked")){
                    $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').each(function (i,e) {
                        if(!$(e).prop("checked")){
                            $(e).prop('checked',true)
                        }
                    })
                }else {
                    $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').prop('checked',false)
                }
            })
			});
	
	
		/*管理员-普通商品-添加*/
		function commonAdd() {
			var url = '/goods/toCommonEdit.do' + '?exType=Add';
			layer_show('添加普通商品', url, null, 510, function() {
				myDataTable.fnDraw(true);
			},true);
		}
		/*管理员-普通商品-编辑*/
		function common_edit(goodsId) {
			var url = '/goods/toCommonEdit.do' + '?goodsId='+ goodsId +'&exType=UPDATE';
			layer_show('修改普通商品', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
		}
		
		function searchGoods(){
			myDataTable.fnDraw(true);
		}
		
		//批量禁用
		function batchStop() {
			var chk_value = [];
			$("input[name=goodsIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value.length ==0){
				layer.msg('请选择要禁用的数据！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要禁用吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/goods/batchStopGoods.do',
					data : {
						goodsIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('已禁用!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('禁用失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}
		//批量启用
		function batchStart() {
			var chk_value = [];
			$("input[name=goodsIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value.length ==0){
				layer.msg('请选择要启用的数据！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要启用吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/goods/batchStartGoods.do',
					data : {
						goodsIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('已启用!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('启用失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}