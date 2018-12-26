var modifyListTable;
$(function() {
  modifyListTable = $('#modifyListTable').dataTable({
    "serverSide" : true,
    "dom" : 'rtilp',
    "ajax" : {
      url : "/recruiter/getModifyList.do",
      type : "post",
      data : {
        empId : varEmpId
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
    "columns" : [ {
      "mData" : "modifyId"
    }, {
      "mData" : "modifyText"
    }, {
      "mData" : "createUser"
    }, {
      "mData" : "createTime"
    }, {
      "mData" : "effectTime"
    }, {
      "mData" : null
    } ],
    "columnDefs": [{
        "render": function (data, type, row, meta) {
            var dom = '';
            if ('1' === row.status) {
                dom = '待执行';
            } else if('2' === row.status) {
                dom = '已完成';
            }
            return dom;
        },
        "targets": 5
    }]
});
});