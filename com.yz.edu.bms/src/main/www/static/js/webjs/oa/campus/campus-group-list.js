var myDataTable;
$(function() {
    _init_select('campusGroupId', dictJson.campusGroup);
    _simple_ajax_select({
        selectId : "campusId",
        searchUrl : '/campus/selectList.do',
        sData : {},
        showText : function(item) {
            return item.campusName;
        },
        showId : function(item) {
            return item.campusId;
        },
        placeholder : '--请选择校区--'
    });

    myDataTable = $('.table-sort').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/campusGroup/campusGroupList.do",
            type: "post",
            data : function(pageData) {
                pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
                return pageData;
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
        columns : [
            {"mData" : null},
            {"mData" : null},
            {"mData" : "campusName"},
            {"mData" : "createUser"},
            {"mData" : "createTime"},
            {"mData" : null}
        ],
        "columnDefs" : [
            {"targets" : 0, "render" : function(data, type, row, meta) {
                return '<input type="checkbox" value="'+ row.id + '" name="ids"/>';
            }},
            {"targets" : 1,"render" : function(data, type, row, meta) {
                return _findDict("campusGroup", row.campusGroupId);
            }},
            {"targets" : 5, "render" : function(data, type, row, meta) {
                var dom = '<a title="编辑" href="javascript:void(0);" onclick="campusGroupEdit(\'' + row.id + '\')" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';
                dom += '&nbsp;&nbsp;&nbsp;';
                dom += '<a title="删除" href="javascript:void(0);" onclick="campusGroupDel(this,\'' + row.id + '\')" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont">&#xe6e2;</i></a>';
                return dom;
            }}
        ]
    });
});

function searchCampus(){
    myDataTable.fnDraw(true);
}


/*校区组-添加*/
function addCampusGroup() {
    var url = '/campusGroup/toEditCampusGroup.do' + '?exType=Add';
    layer_show('添加校区', url, 500, 300, function () {
        myDataTable.fnDraw(true);
    });
}

/*校区组-编辑*/
function campusGroupEdit(id) {
    var url = '/campusGroup/toEditCampusGroup.do' + '?id=' + id + '&exType=UPDATE';
    layer_show('修改校区', url, 500, 300, function () {
        myDataTable.fnDraw(false);
    });
}

/*校区组-删除*/
function campusGroupDel(obj, id) {
    layer.confirm('确认要删除吗？', function (index) {
        $.ajax({
            type: 'POST',
            url: '/campusGroup/deleteCampusGroup.do',
            data: {
                id: id
            },
            dataType: 'json',
            success: function (data) {
                layer.msg('已删除!', {icon: 1,time: 1000});
                myDataTable.fnDraw(false);
                $("input[name=all]").attr("checked", false);
            },
            error: function (data) {
                layer.msg('删除失败！', {icon: 1,time: 1000});
                myDataTable.fnDraw(false);
                $("input[name=all]").attr("checked", false);
            }
        });
    });
}

/*校区组-批量删除*/
function delCampusGroupAll() {
    var chk_value = [];
    $("input[name=ids]:checked").each(function () {
        chk_value.push($(this).val());
    });
    if (chk_value == null || chk_value.length <= 0) {
        layer.msg('未选择任何数据!', {
            icon : 5,
            time : 1000
        });
        return;
    }
    layer.confirm('确认要删除吗？', function (index) {
        $.ajax({
            type: 'post',
            url: '/campusGroup/deleteCampusGroupBatch.do',
            data: {
                ids: chk_value
            },
            dataType: 'json',
            success: function (data) {
                layer.msg('已删除!', {icon: 1, time: 1000});
                myDataTable.fnDraw(true);
                $("input[name=all]").attr("checked", false);
            },
            error: function (data) {
                layer.msg('删除失败！', {icon: 1, time: 1000});
                myDataTable.fnDraw(true);
                $("input[name=all]").attr("checked", false);
            }
        });
    });
}



