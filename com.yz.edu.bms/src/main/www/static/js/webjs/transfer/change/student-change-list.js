var myDataTable;
$(function () {
    myDataTable = $('.table-sort').dataTable({
        "processing": true,
        "serverSide": true,
        "dom": 'rtilp',
        "ajax": {
            url: "/studentChange/findBdStudentChange.do",
            type: "post",
            data: {
                "stdName": function () {
                    return $("#stdName").val();
                }, "phone": function () {
                    return $("#phone").val();
                }, "idCard": function () {
                    return $("#idCard").val();
                }
            }
        },
        "pageLength": 10,
        "pagingType": "full_numbers",
        "ordering": false,
        "searching": false,
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "language": _my_datatables_language,
        columns: [
            {"mData": null},
            {"mData": "stdName"},
            {"mData": null},
            {"mData": null},
            {"mData": "updateUser"},
            {"mData": "reason"},
            {"mData": null}
        ],
        "columnDefs": [
            {"targets": 0,"render": function (data, type, row, meta) {
                return '<input type="checkbox" value="' + row.changeId + '" name="changeIds"/>';
            } },
            {"targets": 2,"class":"text-l","render": function (data, type, row, meta) {
                var stdStage = row.stdStage;
                row = row.old;
                var dom = _findDict("grade", row.grade) + "[" + _findDict("scholarship", row.scholarship) + "]" + row.unvsName + ":" + row.pfsnName + "[" + _findDict("pfsnLevel", row.pfsnLevel) + "]--->" + _findDict("stdStage", stdStage);
                return dom;
             }},
            {"targets": 3,"class":"text-l", "render": function (data, type, row, meta) {
                row = row.prevent;
                var dom = _findDict("grade", row.grade) + "[" + _findDict("scholarship", row.scholarship) + "]" + row.unvsName + ":" + row.pfsnName + "[" + _findDict("pfsnLevel", row.pfsnLevel) + "]--->" + _findDict("stdStage", row.stdStage);
                return dom;
            }},
            {"targets": 6, "render": function (data, type, row, meta) {
                var dom = '';
                dom = '<a title="查看" href="javascript:;" onclick="member_edit(\'' + row.changeId + '\')" class="ml-5" style="text-decoration: none">';
                dom += '<i class="iconfont icon-chakan"></i></a>';
                return dom;
            }}
        ]
    });
});

/*用户-添加*/
function member_add() {
    var url = '/studentChange/edit.do' + '?exType=ADD';
    layer_show('添加转报', url, null, 510, function () {
        myDataTable.fnDraw(true);
    }, true);
}

/*用户-查看*/
function member_edit(changeId) {
    var url = '/studentChange/edit.do' + '?changeId=' + changeId + '&exType=UPDATE';
    layer_show('查看转报', url, null, 510, function () {
        myDataTable.fnDraw(false);
    }, true);
}

function _search() {
    myDataTable.fnDraw(true);
}