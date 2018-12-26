var myDataTable;
$(function() {
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/purchasing/list.do",
					data : {
						"applyName" : function(){
							return $("#applyName").val();
						},
						"receiveName" : function(){
							return $("#receiveName").val();
						},
						"receiveMobile" : function(){
							return $("#receiveMobile").val();
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
					"mData" : "applyReason"
				}, {
					"mData" : "goodsName"
				}, {
					"mData" : "goodsNum"
				}, {
					"mData" : "goodsPrice"
				}, {
					"mData" : "totalPrice"
				}, {
					"mData" : null
				}, {
					"mData" : null
				}, {
					"mData" : "remark"
				},{
					"mData" : null
				}],
				"columnDefs" : [
				            {"render" : function(data, type, row, meta) {
								return '<input type="checkbox" value="'+ row.id + '" name="id"/>';
							},
							"targets" : 0
							},
							{
								"render" : function(data, type, row, meta) {
									var dom = '';
									dom +='姓&nbsp;&nbsp;&nbsp;名：'+(row.applyName==null?'无':row.applyName) +'</br>';
									dom +='手机号：'+(row.applyMobile == null?'无':row.applyMobile);
									
									return dom;
								},
								"sClass":'text-l',
								"targets" : 1
							},
							{
								 "render" : function(data, type, row, meta) {
									 
									var dom = '';
									dom +='姓&nbsp;&nbsp;&nbsp;名：'+(row.receiveName==null?'无':row.receiveName) +'</br>';
									dom +='手机号：'+(row.receiveMobile == null?'无':row.receiveMobile)+'</br>';
									dom +='收货地址：';
									if(row.provinceName){
										dom += row.provinceName;
									}
									if(row.cityName){
										dom += row.cityName;								
									}
									if(row.districtName){
										dom += row.districtName;
									}
									if(row.streetName){
										dom += row.streetName;
									}
									dom += row.address;
									
									return dom;
								},
								"sClass":'text-l',
								"targets" : 7
							},
							{
								 "render" : function(data, type, row, meta) {
									var dom = '';
									dom += '操&nbsp;作&nbsp;人：'+ (row.operUserName == null ? '无': row.operUserName)+ '</br>';
									var dateTime = row.operTime;
									if (!dateTime) {
										return '-'
									}
									var date = dateTime.substring(0, 10)
									var time = dateTime.substring(11)
									dom +='操作时间：'+date + '<br>' + time;
									return dom;
								},
								"sClass":'text-l',
								"targets" : 8
							},
							{
							"render" : function(data, type, row, meta) {
								var dom = '';
		                        dom += '<a title="查看详细" href="javascript:;" onclick="lookDetail(\'' + row.id + '\')" class="tableBtn normal" style="text-decoration: none">详细</a>';
		                        
								return dom;
							},
							//指定是第三列
							"targets" : 10,"sClass":'text-c',
						} ]
			});

});

function searchPurchasing(){
	myDataTable.fnDraw(true);
}


function addBuy() {
	var url = '/purchasing/toApply.do';
	layer_show('发起采购', url, null, 510, function() {
		myDataTable.fnDraw(true);
	},true);
}
function lookDetail(id){
	var url = '/purchasing/lookDetail.do?id='+id;
	layer_show('查看采购详细', url, null, 510, function() {
		myDataTable.fnDraw(true);
	},true);
}
