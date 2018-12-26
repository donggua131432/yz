var modifyListTable;
$(function() {
    _init_select('modifyType', dictJson.modifyType);

    modifyListTable = $('#modifyListTable').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/allStudent/getModifyList.do",
            type : "post",
            data : function(pageData) {
                pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
                console.log('我的学员------'+pageData);
                return pageData;
            }
        },
        "pageLength" : 10,
        "pagingType" : "full_numbers",
        "ordering" : false,
        "searching" : false,
        "lengthMenu" : [ 10, 20 ],
        "language" : _my_datatables_language,
        "createdRow" : function(row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "columns" : [
            {"mData" : "modifyType" },
            {"mData" : "modifyText"},
            {"mData" : "createUser"},
            {"mData" : "createTime"},
            {"mData" : "isComplete"}
        ],
        "columnDefs" : [
            {"targets" : 0,"render" : function(data, type, row, meta) {
                return _findDict('modifyType', row.modifyType);
            }},
            { "targets" : 4,"render" : function(data, type, row, meta) {
                return '1' == row.isComplete ? '是' : '否';
            }}
        ]
    });
});

function searchModifyList() {
    modifyListTable.fnDraw(true);
}