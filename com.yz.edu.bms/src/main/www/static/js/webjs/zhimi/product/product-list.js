var myDataTable;
$(function() {

    $("#isAllow").select2({
        placeholder : "--请选择--",
        allowClear : true
    });

    myDataTable = $('.table-sort').dataTable(
            {
                "serverSide" : true,
                "dom" : 'rtilp',
                "ajax" : {
                    url : "/product/getList.do",
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
                    "mData" : "productId"
                }, {
                    "mData" : "productName"
                }, {
                    "mData" : "productDesc"
                }, {
                    "mData" : "price"
                }, {
                    "mData" : "zhimi"
                }, {
                    "mData" : "updateUser"
                }, {
                    "mData" : null
                }, {
                    "mData" : "createUser"
                }, {
                    "mData" : null
                }, {
                    "mData" : null
                }, {
                    "mData" : "sort"
                }, {
                    "mData" : null
                } ],
                "columnDefs" : [
                    {
                        "targets" : 2,
                        "class" : "text-l"
                    },{
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
                    },{
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
                                var isAllow = row.isAllow;

                                if ('1' == isAllow) {
                                    return '<span class="label label-success radius">已启用</span>';
                                } else {
                                    return '<span class="label label-danger radius">已禁用</span>';
                                }
                            },
                            "targets" : 9
                        },
                        {
                            "render" : function(data, type, row, meta) {
                                var dom = '';
                                if (row.isAllow == '1') {
                                    dom += '<a onClick="toggle(\'' + row.productId
                                            + '\', \'0\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                } else if (row.isAllow == '0') {
                                    dom += '<a onClick="toggle(\'' + row.productId
                                            + '\', \'1\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                }
                                dom += ' &nbsp;';
                                dom += '<a title="编辑" href="javascript:void(0)" onclick="edit(\'UPDATE\',\'' + row.productId
                                        + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="iconfont icon-edit"></i></a>';

                                return dom;
                            },
                            "targets" : 11
                        } ]
            });
});

function edit(type, productId) {
    var url = '';
    var title = '';
    if ('ADD' == type) {
        url = '/product/toAdd.do';
        title = '添加智米充值产品';
        layer_show(title, url, null, 420, function() {
            myDataTable.fnDraw(true);
        });
    } else if ('UPDATE' == type) {
        url = '/product/toUpdate.do' + '?productId=' + productId;
        title = '更新智米充值产品';
        layer_show(title, url, null, 420, function() {
            myDataTable.fnDraw(false);
        });
    } else {
        return false;
    }


}

function toggle(productId, isAllow) {

    var msg = '';
    if (isAllow == '1') {
        msg = '启用成功';
    } else {
        msg = '禁用成功';
    }

    $.ajax({
        type : 'POST',
        url : '/product/toggle.do',
        data : {
            'productId' : productId,
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
        productId : $("#productId").val() ? $("#productId").val() : '',
        productName : $("#productName").val() ? $("#productName").val() : '',
        isAllow : $("#isAllow").val() ? $("#isAllow").val() : '',
        start : pageData.start,
        length : pageData.length
    };

    return data;
}