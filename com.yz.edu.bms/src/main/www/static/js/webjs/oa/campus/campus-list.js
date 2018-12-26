var myDataTable;
		$(function() {
			
			myDataTable = $('.table-sort').dataTable(
					{
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/campus/list.do",
							data : {
								"campusName" : function(){
									return $("#campusName").val();
								},
								"financeNo" : function(){
									return $("#financeNo").val();
								},
								"address" : function(){
									return $("#address").val();
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
							"mData" : "campusName"
						}, {
							"mData" : "financeNo"
						}, {
							"mData" : "empName"
						}, {
							"mData" : "telephone"
						}, {
							"mData" : null
						}, {
							"mData" : "address"
						}, {
							"mData" : "isForeign"
						}],
						"columnDefs" : [
						            {"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.campusId + '" name="campusIds"/>';
									},
									"targets" : 0
									},
									{
										"render" : function(data, type, row, meta) {
											return getValue(row.provinceId)+"-"+getValue(row.cityId)+"-"+getValue(row.areaId);
										},
										"targets" : 5
									},{
                                		"targets" : 6,
                                		"class":"text-l"
									},
									 {"render" : function(data, type, row, meta) {
										 if(row.isForeign == 1){
											 return '是';
										 }else{
											 return '否';
										 }
										},
										"targets" : 7
									},
									{
										"render" : function(data, type, row, meta) {
											return 1 == row.isStop ? '<span class="label radius">不可用</span>' : '<span class="label label-success radius">可用</span>';
										},
										"targets" : 8
									} ,
									{
									"render" : function(data, type, row, meta) {
										var dom = '';
										if(row.isStop == '0'){
											dom += '<a onClick="campus_stop(this,\''+ row.campusId +'\')" href="javascript:;" title="停用" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>';
										}else if(row.isStop == '1'){
											dom += '<a onClick="campus_start(this,\''+ row.campusId +'\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>';
										}
										dom += '<a title="编辑" href="javascript:void(0)" onclick="campus_edit(\'' + row.campusId + '\')" class="ml-5" style="text-decoration:none">';
										dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
									
										return dom;
									},
									//指定是第三列
									"targets" : 9
								} ]
					});
	
			});
	
	
		/*管理员-校区-添加*/
		function campus_add() {
			var url = '/campus/edit.do' + '?exType=Add';
			layer_show('添加校区', url, null, 510, function() {
				myDataTable.fnDraw(true);
			});
		}
		/*管理员-校区-编辑*/
		function campus_edit(campusId) {
			var url = '/campus/edit.do' + '?campusId='+ campusId +'&exType=UPDATE';
			layer_show('修改校区', url, null, 510, function() {
				myDataTable.fnDraw(false);
			});
		}
		
		//根据编码获取省市区相应值
		function getValue(key){
			for(var i=0;i<pcdJson.length;i++){
				if(key == pcdJson[i].provinceCode){
					return pcdJson[i].provinceName;
				}
				var city = pcdJson[i].city;
				for(var o = 0;o<city.length;o++){
					if(key == city[o].cityCode){
						return city[o].cityName;
					}
					var district = city[o].district;
					for(var p = 0;p<district.length;p++){
						if(key == district[p].districtCode){
							return district[p].districtName;
						}
					}
				}
			}
			return "";
		}
		function searchCampus(){
			myDataTable.fnDraw(true);
		}
		/*管理员-停用*/
		function campus_stop(obj,id){
			layer.confirm('确认要停用吗？',function(index){
				//此处请求后台程序，下方是成功后的前台处理……
				$.ajax({
					type : 'POST',
					url : '/campus/campusBlock.do',
					data : {
						campusId : id,
						exType : 'BLOCK'
					},
					dataType : 'json',
					success : function(data) {
						myDataTable.fnDraw(false);
						layer.msg('已停用!',{icon: 5,time:1000});
					},
					error : function(data) {
						layer.msg('停用失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
			});
		}

		/*管理员-启用*/
		function campus_start(obj,id){
			layer.confirm('确认要启用吗？',function(index){
				//此处请求后台程序，下方是成功后的前台处理……
				$.ajax({
					type : 'POST',
					url : '/campus/campusBlock.do'+'?campusId='+id+'&exType=START',
					dataType : 'json',
					success : function(data) {
						myDataTable.fnDraw(false);
						layer.msg('已启用!', {icon: 6,time:1000});
					},
					error : function(data) {
						layer.msg('启用失败！', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
					},
				});
				
				
			
			});
			
		}