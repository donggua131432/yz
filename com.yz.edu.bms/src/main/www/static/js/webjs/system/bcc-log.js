var myDataTable;
$(function() {
	
	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});

	myDataTable = $('#shTable').dataTable({
		"processing" : true,
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/sysLog/getBccLog.do",
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
			"mData" : "logId"
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : "updateTimeStr"
		}, {
			"mData" : null
		} ],
		"columnDefs" : [ {
			"render" : function(data, type, row, meta) {
				return user(row);
			},
			"targets" : 1,
			"class" : "text-l"
		}, {
			"render" : function(data, type, row, meta) {
				return address(row);
			},
			"targets" : 2,
			"class" : "text-l"
		}, {
			"render" : function(data, type, row, meta) {
				return interfaceInfo(row);
			},
			"targets" : 3,
			"class" : "text-l"
		}, {
			"render" : function(data, type, row, meta) {
				return result(row);
			},
			"targets" : 4,
			"class" : "text-l"
		}, {
			"render" : function(data, type, row, meta) {
				if ('1' == row.isSuccess) {
					dom = '<span class="label label-success radius">成功</span>';
				} else {
					dom = '<span class="label label-default radius">失败</span>';
				}
				return dom;
			},
			"targets" : 6
		} ]
	});

});

function showDetail(id, title) {
	layer.open({
		title : title,
		area : [ '800px', '300px' ],
		shadeClose : true,
		content : $("#" + id).html()
	});
}

function user(data) {
	var rn = data.name ? data.name : '无';
	var nn = data.nickname ? data.nickname : '无';
	var idCard = data.idCard ? data.idCard : '无';
	var yzCode = data.yzCode ? data.yzCode : '无';
	var mobile = data.mobile ? data.mobile : '无';
	var userId = data.userId ? data.userId : '无';

	var dom = '<ul>';
	dom += '<li>用户编号：' + userId + '</li>';
	dom += '<li>用户名称：' + rn + '</li>';
	dom += '<li>　身份证：' + idCard + '</li>';
	dom += '<li>　　昵称：' + nn + '</li>';
	dom += '<li>远智编号：' + yzCode + '</li>';
	dom += '<li>手机号码：' + mobile + '</li>';
	dom += '</ul>';

	return dom;
}

function address(data) {
	var ip = data.ipAddress ? data.ipAddress : '无';
	var mac = data.macAddress ? data.macAddress : '无';
	var dom = '<ul>';
	dom += '<li>&nbsp;　IP地址：' + ip + '</li>';
	dom += '<li>MAC地址：' + mac + '</li>';
	dom += '</ul>';

	return dom;
}

function interfaceInfo(data) {
	var itn = data.interfaceName ? data.interfaceName : '无';
	var itv = data.interfaceVersion ? data.interfaceVersion : '无';
	var sb = data.sysBelong ? data.sysBelong : '无';
	var dom = '<ul>';
	dom += '<li>接口名称：' + itn + '</li>';
	dom += '<li>接口版本：' + itv + '</li>';
	dom += '<li>所属系统：' + sb + '</li>';
	dom += '</ul>';

	return dom;
}
function result(data) {
	var inParam = data.postData ? data.postData : '无';

	var id = data.logId + '_inParam';

	var html = '<div id=' + data.logId + '_inParam' + ' style="display: none;">';
	html += '<div class="row cl mr-5 ml-5" style="display: inline;">';
	html += '<div class="mt-30 mr-10 ml-10 text-c">';
	html += inParam;
	html += '</div>';
	html += '</div>';
	html += '</div>';

	$("#hideMsg").append(html);

	var dom = '<ul>';

	dom += '<li>' + '<a title="查看请求参数" href="javascript:void(0);" onclick="showDetail(\'' + id + '\', \'请求参数\')" class="ml-5" style="text-decoration: none;color:blue;">'
			+ '查看请求参数>>></a></li>';

	if ('0' == data.isSuccess) {
		id = data.logId + '_ed';
		dom += '<li>' + '<a title="查看错误信息" href="javascript:void(0);" onclick="showDetail(\'' + id + '\', \'错误信息\')" class="ml-5" style="text-decoration: none;color:blue;">'
				+ '查看错误信息>>></a></li>';
		var ed = data.reason ? data.reason : '无';
		var em = '<div id=' + data.logId + '_ed' + ' style="display: none;">';
		em += '<div class="row cl mr-5 ml-5" style="display: inline;">';
		em += '<div class="mt-30 mr-10 ml-10 text-c">';
		em += ed;
		em += '</div>';
		em += '</div>';
		em += '</div>';

		$("#hideMsg").append(em);
	}

	dom += '</ul>';

	return dom;
}

function searchData(pageData) {
	return {
		yzCode : $("#yzCode").val() ? $("#yzCode").val() : '',
		mobile : $("#mobile").val() ? $("#mobile").val() : '',
		realName : $("#realName").val() ? $("#realName").val() : '',
		postData : $("#postData").val() ? $("#postData").val() : '',
		errorMsg : $("#errorMsg").val() ? $("#errorMsg").val() : '',
		ip : $("#ip").val() ? $("#ip").val() : '',
		mac : $("#mac").val() ? $("#mac").val() : '',
		interfaceName : $("#interfaceName").val() ? $("#interfaceName").val() : '',
		interfaceVersion : $("#interfaceVersion").val() ? $("#interfaceVersion").val() : '',
		sysBelong : $("#sysBelong").val() ? $("#sysBelong").val() : '',
		isSuccess : $("#isSuccess").val() ? $("#isSuccess").val() : '',
		startTimeStr : $("#startTime").val() ? $("#startTime").val() : '',
		endTimeStr : $("#endTime").val() ? $("#endTime").val() : '',
		start : pageData.start,
		length : pageData.length
	};
}

function refreshTable(tag) {
	myDataTable.fnDraw(tag);
	$("input[type='checkbox']").prop('checked', false);
}