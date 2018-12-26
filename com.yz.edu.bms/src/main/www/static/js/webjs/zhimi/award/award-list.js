var myDataTable;
$(function() {

    $("#isAllow").select2({
        placeholder : "--请选择--",
        allowClear : true
    });
    $("#isMutex").select2({
        placeholder : "--请选择--",
        allowClear : true
    });
    $("#isRepeat").select2({
        placeholder : "--请选择--",
        allowClear : true
    });
    _init_select("ruleGroup", dictJson.ruleGroup);
    myDataTable = $('.table-sort').dataTable(
            {
                "serverSide" : true,
                "dom" : 'rtilp',
                "ajax" : {
                    url : "/award/getList.do",
                    type : "post",
                    data : function(pageData) {
                        return search_data(pageData);
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
                },{
                    "mData" : "ruleCode"
                }, {
                    "mData" : "ruleDesc"
                }, {
                    "mData" : "zhimiCount"
                }, {
                    "mData" : "expCount"
                }, {
                    "mData" : "updateUser"
                }, {
                    "mData" : null
                }, {
                    "mData" : null
                }, {
                    "mData" : null
                }, {
                    "mData" : "sort"
                }, {
                    "mData" : "updateUser"
                },{
                    "mData" : null
                }, {
                    "mData" : "createUser"
                }, {
                    "mData" : null
                }, {
                    "mData" : null
                }],
                "columnDefs" : [
					{
						"render" : function(data, type, row, meta) {
							return _findDict("ruleGroup",row.ruleGroup);
						},
						"targets" : 0
					} ,
                    {
                        "render" : function(data, type, row, meta) {
                            var dateTime= row.startTime;
                            if(!dateTime){
                                return '-'
                            }
                            var date=dateTime.substring(0,10)
                            var time=dateTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 5,"class":"text-l no-warp"
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dateTime= row.endTime;
                            if(!dateTime){
                                return '-'
                            }
                            var date=dateTime.substring(0,10)
                            var time=dateTime.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 6,"class":"text-l no-warp"
                    },{
                        "render" : function(data, type, row, meta) {
                            var isMutex = row.isMutex;

                            if ('0' == isMutex) {
                                return '<span class="label label-success radius">是</span>';
                            } else {
                                return '<span class="label label-danger radius">否</span>';
                            }
                        },
                        "targets" : 7
                    },{
                        "render" : function(data, type, row, meta) {
                            var isRepeat = row.isRepeat;

                            if ('0' == isRepeat) {
                                return '<span class="label label-success radius">是</span>';
                            } else {
                                return '<span class="label label-danger radius">否</span>';
                            }
                        },
                        "targets" : 8
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
                        "targets" : 11,"class":"text-l no-warp"
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
                        "targets" : 13,"class":"text-l no-warp"
                    },{
                        "render" : function(data, type, row, meta) {
                            var isAllow = row.isAllow;

                            if ('1' == isAllow) {
                                return '<span class="label label-success radius">已启用</span>';
                            } else {
                                return '<span class="label label-danger radius">已禁用</span>';
                            }
                        },
                        "targets" : 14
                    },{
                            "render" : function(data, type, row, meta) {
                                var dom = '';
                                if (row.isAllow == '1') {
                                    dom += '<a onClick="toggle(\'' + row.ruleCode
                                            + '\', \'0\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                } else if (row.isAllow == '0') {
                                    dom += '<a onClick="toggle(\'' + row.ruleCode
                                            + '\', \'1\')" href="javascript:;" title="起用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                }
                                dom += ' &nbsp;';
                                dom += '<a title="编辑" href="javascript:void(0)" onclick="edit(\'UPDATE\',\'' + row.ruleCode
                                        + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="iconfont icon-edit"></i></a>';

                                return dom;
                            },
                            "targets" : 15
                        } ]
            });
});

var index;

function edit(type, ruleCode) {
    var url = '';
    var title = '';
    if ('ADD' == type) {
        url = '/award/toAdd.do';
        title = '添加智米赠送规则';
        layer_show(title, url, null, 780, function() {
            myDataTable.fnDraw(true);
        });
    } else if ('UPDATE' == type) {
        url = '/award/toUpdate.do' + '?ruleCode=' + ruleCode;
        title = '更新智米赠送规则';
        layer_show(title, url, null, 780, function() {
            myDataTable.fnDraw(false);
        });
    } else {
        return false;
    }

}

function toggle(ruleCode, isAllow) {

    var msg = '';
    if (isAllow == '1') {
        msg = '启用成功';
    } else {
        msg = '禁用成功';
    }

    $.ajax({
        type : 'POST',
        url : '/award/toggle.do',
        data : {
            'ruleCode' : ruleCode,
            'isAllow' : isAllow
        },
        dataType : 'json',
        success : function(data) {
            if (_GLOBAL_SUCCESS == data.code) {
                layer.msg(msg, {
                    icon : 1,
                    time : 1000
                }, function() {
                    myDataTable.fnDraw(false);
                });
            }
        }
    });
}

function _search() {
    myDataTable.fnDraw(true);
}

function search_data(pageData) {
    var data = {
        ruleCode : $("#ruleCode").val() ? $("#ruleCode").val() : '',
        ruleDesc : $("#ruleDesc").val() ? $("#ruleDesc").val() : '',
        zhimiCount : $("#zhimiCount").val() ? $("#zhimiCount").val() : '',
        isAllow : $("#isAllow").val() ? $("#isAllow").val() : '',
        ruleGroup : $("#ruleGroup").val() ? $("#ruleGroup").val() : '',
        startTime : $("#startTime").val() ? $("#startTime").val() : '',
        endTime : $("#endTime").val() ? $("#endTime").val() : '',
        isMutex : $("#isMutex").val() ? $("#isMutex").val() : '',
        isRepeat : $("#isRepeat").val() ? $("#isRepeat").val() : '',		
        start : pageData.start,
        length : pageData.length
    };

    return data;
}