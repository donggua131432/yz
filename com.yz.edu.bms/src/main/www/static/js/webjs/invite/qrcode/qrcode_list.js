var myDataTable;
$(function() {
	//邀约人
	_simple_ajax_select({
		selectId: "invitations",
		searchUrl: '/invite_qrcode/getEmpList.do',
		sData: {},
		showText: function (item) {
			var empName=item.empName;
			if(item.empMobile!=null)
			empName+=" "+item.empMobile;
			return empName;
		},
		showId: function (item) {
			return item.empId;
		},
		placeholder: '--请选择邀约人--'
	});

	myDataTable = $('.table-sort').dataTable({
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/invite_qrcode/getList.do",
			type : "post",
			data : function(pageData) {
				pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
				return pageData;
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
		columns : [
			{"mData" : null},
			{"mData" : "empName"},
			{"mData" : null},
			{"mData" : "inviteUrl"},
			{"mData" : "inviteName"},
			{"mData" : "lookCount"},
			{"mData" : "registerCount"},
			{"mData" : "reChargeCount"},
			{"mData" : "remark"},
			{"mData" : "createUser"},
			{"mData" : "createTime"},
			{"mData" : null}
		],
		"columnDefs" : [
			{ "targets" : 0,"render" : function(data, type, row, meta) {
				return '<input type="checkbox" value="'+ row.channelId + '" name="userIds"/>';
			}},
			{ "targets" : 1,"render" : function(data, type, row, meta) {
				return row.empName;
			}},
			{"targets" : 2,"class":"text-l","render" : function(data, type, row, meta) {
				return '<img src="' + _FILE_URL + row.inviteQrcodeUrl + '" onerror="nofind(this)"/>';
			}},
			{"targets" : 11,"render" : function(data, type, row, meta) {
				var dom = '';
				dom += '<a style="padding: 2px 5px;margin-bottom: 5px;display: inline-block" class="tableBtn normal" title="查看" href="javascript:void(0);" onclick="look(\''+row.channelId+'\')" class="ml-5">';
				dom += '查看</a><br/>';
				dom += '<a style="padding: 2px 5px;margin-bottom: 5px;display: inline-block" class="tableBtn normal" title="下载" href="javascript:void(0);" onclick="down(\''+row.inviteQrcodeUrl+'\');return false;" class="ml-5" >';
				dom += '下载</a>';
				return dom;
			}}
		]
	});
});

function _search() {
	myDataTable.fnDraw(true);
}
//新增推广二维码
function add() {
	layer_show('新增推广二维码', '/invite_qrcode/addInviteQrCode.do',600, 500, function() {
				myDataTable.fnDraw(true);
			}, false);
}
//删除
function del() {
	var chk_value = [];
                $("input[name=userIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                if(chk_value.length==0){
                   layer.msg('请选择要删除的二维码!', {
                                icon : 0,
                                time : 1000
                            });
                return;  
                }
	layer.confirm('您确定要删除吗？', {icon: 0, title:'温馨提示',btn: ['确定','取消'],offset: '36%'}, function(index){
		$.ajax({
                        type : 'POST',
                        url : '/invite_qrcode/inviteQrCodeDel.do',
                        data : {
                            channelIds : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            layer.msg('已删除!', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=userIds]").attr("checked", false);
                        },
                        error : function(data) {
                            layer.msg('删除失败！', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=userIds]").attr("checked", false);
                        },
                    });
                layer.close(index);
            });
}
function look(channelId){
	layer_show('新增推广二维码', '/invite_qrcode/lookInviteQrCode.do?channelId='+channelId,600, 500, function() {
				myDataTable.fnDraw(true);
			}, false);
}

function down(url){
	var src=_FILE_URL+url;
    fff.href = src;
    fff.download = "testImg.jpg";
    fff.click();
}
