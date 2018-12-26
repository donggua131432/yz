var sg_myDataTable;
$(function() {
	_init_select('_isSuccess', dictJson.logStatus);

	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});

	sg_myDataTable = $('#sgTable').dataTable({
		"processing" : true,
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/sysLog/getBmsLog.do",
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
				return _func(row);
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
				if ('1' == row.logStatus) {
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
	  title: title,
	  area: ['500px', '300px'],
	  shadeClose : true,
	  content: $("#" + id).html()
	});     
}

function user(data) {
	var cn = data.campusName ? data.campusName : '无';
	var dn = data.dpName ? data.dpName : '无';
	var en = data.empName ? data.empName : '无';
	var yzCode = data.yzCode ? data.yzCode : '无';
	var mobile = data.mobile ? data.mobile : '无';
	var userId = data.userId ? data.userId : '无';
	var un = data.userName ? data.userName : '无';

	var dom = '<ul>';
	dom += '<li>用户编号：' + userId + '</li>';
	dom += '<li>登录名称：' + un + '</li>';
	dom += '<li>所属校区：' + cn + '</li>';
	dom += '<li>所属部门：' + dn + '</li>';
	dom += '<li>员工姓名：' + en + '</li>';
	dom += '<li>远智编号：' + yzCode + '</li>';
	dom += '<li>手机号码：' + mobile + '</li>';
	dom += '</ul>';

	return dom;
}
function address(data) {
	var ip = data.ip ? data.ip : '无';
	var mac = data.mac ? data.mac : '无';
	var dom = '<ul>';
	dom += '<li>&nbsp;　IP地址：' + ip + '</li>';
	dom += '<li>MAC地址：' + mac + '</li>';
	dom += '</ul>';

	return dom;
}
function _func(data) {
	var method = data.method ? data.method : '无';
	var requestPath = data.requestPath ? data.requestPath : '无';
	var fncName = data.fncName ? data.fncName : '无';
	var dom = '<ul>';
	dom += '<li>请求方法：' + method + '</li>';
	dom += '<li>请求地址：' + requestPath + '</li>';
	dom += '<li>功能名称：' + fncName + '</li>';
	dom += '</ul>';

	return dom;
}
function result(data) {
	var inParam = data.inParam ? data.inParam : '无';
	
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

	dom += '<li>' +
	'<a title="查看请求参数" href="javascript:void(0);" onclick="showDetail(\'' + id + '\', \'请求参数\')" class="ml-5" style="text-decoration: none;color:blue;">'
			+ '查看请求参数>>></a></li>';

	if ('2' == data.logStatus) {
		id = data.logId + '_ed';
		dom += '<li>' + '<a title="查看错误信息" href="javascript:void(0);" onclick="showDetail(\'' + id + '\', \'错误信息\')" class="ml-5" style="text-decoration: none;color:blue;">'
				+ '查看错误信息>>></a></li>';
		var ed = data.exceptionDetail ? data.exceptionDetail : '无';
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

function searchData_sg(pageData) {
	return {
		userName : $("#_userName").val() ? $("#_userName").val() : '',
		yzCode : $("#_yzCode").val() ? $("#_yzCode").val() : '',
		mobile : $("#_mobile").val() ? $("#_mobile").val() : '',
		realName : $("#_realName").val() ? $("#_realName").val() : '',
		dpName : $("#dpName").val() ? $("#dpName").val() : '',
		campusName : $("#campusName").val() ? $("#campusName").val() : '',
		funcName : $("#funcName").val() ? $("#funcName").val() : '',
		postData : $("#_postData").val() ? $("#_postData").val() : '',
		errorMsg : $("#_errorMsg").val() ? $("#_errorMsg").val() : '',
		ip : $("#_ip").val() ? $("#_ip").val() : '',
		mac : $("#_mac").val() ? $("#_mac").val() : '',
		isSuccess : $("#_isSuccess").val() ? $("#_isSuccess").val() : '',
		startTimeStr : $("#_startTime").val() ? $("#_startTime").val() : '',
		endTimeStr : $("#_endTime").val() ? $("#_endTime").val() : '',
		start : pageData.start,
		length : pageData.length
	};
}

function refreshTable_sg(tag) {
	sg_myDataTable.fnDraw(tag);
	$("input[type='checkbox']").prop('checked', false);
}