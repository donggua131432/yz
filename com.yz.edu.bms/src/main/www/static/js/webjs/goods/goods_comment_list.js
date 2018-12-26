var myDataTable;
		$(function() {
			_init_select("goodsUse2",dictJson.goodsUse);
			  //初始状态
	        _init_select("isAllow2", [
	            {
	                "dictValue": "0", "dictName": "是"
	            },
	            {
	                "dictValue": "1", "dictName": "否"
	            }
	        ]);
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/comment/list.do",
							data : {
								"goodsName" : function(){
									return $("#goodsName").val();
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
							"mData" : "commentId"
						}, {
							"mData" : null
						}, {
							"mData" : "salesName"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.commentId + '" name="commentIds"/>';
									},
									"targets" : 0
									},
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											var salesType = _findDict("salesType",row.salesType);
											dom  +=(salesType==null?'未知类型':salesType);
											return dom;
										},
										"targets" : 2
									},
									{
										 "render" : function(data, type, row, meta) {
											var dom = '';
											dom +='昵&nbsp;&nbsp;&nbsp;称：'+(row.userName==null?'无':row.userName) +'</br>';
											dom +='手机号：'+(row.mobile == null?'无':row.mobile);
											
											return dom;
										},
										"sClass":'text-l',
										"targets" : 4
									},
									{
										 "render" : function(data, type, row, meta) {
											var dom = '';
											dom += row.commentContent+'</br>';
											dom +='['+row.commentTime+']';
											
											return dom;
										},
										"targets" : 5,
                                        "class":"text-l"
									},
									{
										 "render" : function(data, type, row, meta) {
											var dom = '';
											dom +=  _findDict("commentStatus",row.commentStatus); 
											return dom;
										},
										"targets" : 6
									},
									{
										"render" : function(data, type, row, meta) {
											var dom = '';
											if(row.replyContent !=null){
												dom += row.replyContent+'</br>';
												dom +='['+row.replyTime+']';
											}
											
											return dom;
										},
										"targets" : 7
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
										if(row.commentStatus == '1'){
											//只有待审核的时候才出现  通过或不通过操作
											dom += '<a title="通过" href="javascript:void(0)" onclick="comment_pass(\'' + row.commentId + '\',\''+2+'\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe601;</i></a>';
											dom += '&nbsp;&nbsp;&nbsp;';
											dom += '<a title="不通过" href="javascript:void(0)" onclick="comment_pass(\'' + row.commentId + '\',\''+3+'\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe631;</i></a>';
										}
										dom += '&nbsp;&nbsp;&nbsp;';
										if(row.replyContent !=null){ //如果评论过了,只能查看
											dom += '<a title="查看" href="javascript:void(0)" onclick="comment_look(\'' + row.commentId + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe695;</i></a>';
										}else{
											dom += '<a title="编辑" href="javascript:void(0)" onclick="comment_edit(\'' + row.commentId + '\')" class="ml-5" style="text-decoration:none">';
											dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
										}
									    dom += '&nbsp;&nbsp;&nbsp;';
				                        dom += '<a title="删除" href="javascript:;" onclick="comment_del(this,\'' + row.commentId + '\')" class="ml-5" style="text-decoration: none">';
				                        dom += '<i class="Hui-iconfont f-18">&#xe609;</i></a>';
				                        
										return dom;
									},
									//指定是第三列
									"targets" : 8
								} ]
					});
	
		});
	
		/*管理员-普通商品-编辑*/
		function comment_edit(commentId) {
			var url = '/comment/toCommentEdit.do' + '?commentId='+ commentId +'&exType=UPDATE';
			layer_show('回复评论', url, null, 510, function() {
				myDataTable.fnDraw(false);
			});
		}
		function comment_look(commentId) {
			var url = '/comment/toCommentEdit.do' + '?commentId='+ commentId +'&exType=LOOK';
			layer_show('回复评论', url, null, 510, function() {
				myDataTable.fnDraw(false);
			});
		}
	
		function searchCampus(){
			myDataTable.fnDraw(true);
		}
		
		function comment_pass(commentId,commentStatus){
			var showMsg = '';
			if(commentStatus == '2'){
				showMsg = '确认要通过此评论吗？';
			}else if(commentStatus == '3'){
				showMsg = '确认要不通过此评论吗？';
			}
			layer.confirm(showMsg,function(index){
				//此处请求后台程序，下方是成功后的前台处理……
				$.ajax({
					type : 'POST',
					url : '/comment/singleUpdateCommentStatus.do',
					data : {
						commentId : commentId,
						commentStatus:commentStatus
					},
					dataType : 'json',
					success : function(data) {
						myDataTable.fnDraw(false);
						layer.msg('操作成功!',{icon: 1,time:1000});
					},
					error : function(data) {
						layer.msg('操作失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
			});
		}
		
		/*管理员-删除*/
		function comment_del(obj,id){
			layer.confirm('确认要删除此条评论吗？',function(index){
				//此处请求后台程序，下方是成功后的前台处理……
				$.ajax({
					type : 'POST',
					url : '/comment/singleDeleteComment.do',
					data : {
						commentId : id,
					},
					dataType : 'json',
					success : function(data) {
						myDataTable.fnDraw(false);
						layer.msg('删除成功!',{icon: 1,time:1000});
					},
					error : function(data) {
						layer.msg('删除失败！', {
							icon : 2,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
			});
		}
		//批量删除
		function batch_del() {
			var chk_value = [];
			$("input[name=commentIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value.length ==0){
				layer.msg('请选择要删除的数据！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/comment/batchDeleteComment.do',
					data : {
						commentIds : chk_value
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
							icon : 2,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}
		//批量通过
		function batch_pass() {
			var chk_value = [];
			$("input[name=commentIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value.length ==0){
				layer.msg('请选择要审批通过的数据！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要审核通过吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/comment/batchPassComment.do',
					data : {
						commentIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('已通过!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('通过失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}
		//批量不通过
		function batch_nopass() {
			var chk_value = [];
			$("input[name=commentIds]:checked").each(function() {
				chk_value.push($(this).val());
			});
			if(chk_value.length ==0){
				layer.msg('请选择要不通过的数据！', {
					icon : 2,
					time : 2000
				});
				return;
			}
			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/comment/batchNoPassComment.do',
					data : {
						commentIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						layer.msg('操作成功!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
					error : function(data) {
						layer.msg('操作失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					},
				});
			});
		}