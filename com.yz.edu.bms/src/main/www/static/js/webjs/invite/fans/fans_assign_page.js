var empListTable,noQuitEmp = [];
$(function() {
  $.each(fansList, function(index, data){
      var dom = '',
       nickname = data.nickname ? data.nickname : '', name = data.name ? data.name : '',
       mobile = data.mobile ? data.mobile : '', idCard = data.idCard ? data.idCard : '',
       yzCode = data.yzCode ? data.yzCode : '', empId = data.empId ? data.empId : '',
       empStatus = data.empStatus ? data.empStatus : '';

      dom += "<tr class='text-c'>";
      dom += "<td>" + nickname + "</td>";
      dom += "<td>" + name + "</td>";
      dom += "<td>" + yzCode + "</td>";
      dom += "<td>" + mobile + "</td>";
      dom += "<td>" + idCard + "</td>";
      dom += "</tr>";
      $("#fansListTable").find("tbody").append(dom);
      $(".page-container").append("<input type='hidden' name='userIds' value='" + data.userId + "' />");


      if(empStatus=='1' && empId != '1193' && empId != '538' && empId != ''){
          noQuitEmp.push(name);
      }
  });

  empListTable = $('#empListTable').dataTable({
      "serverSide" : true,
      "dom" : 'rtilp',
      "ajax" : {
          url : '/fans/getEmpList.do',
          type : 'post',
          data : function(data) {
              data = $.extend({},{jtIds:varjtIds, start:data.start, length:data.length}, $("#searchForm").serializeObject());
              return data;
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
          {"mData" : "empName"},
          {"mData" : null},
          {"mData" : "mobile"},
          {"mData" : null},
          {"mData" : "campusName"},
          {"mData" : "dpName"},
          {"mData" : "groupName"},
          {"mData" : null}
      ],
      "columnDefs" : [
          {"targets" : 1,"render" : function(data, type, row, meta) {
              return _findDict("jobTitle", row.jtId);
          }},
          {"targets" : 3,"render" : function(data, type, row, meta) {
              return _findDict("empStatus", row.empStatus);
          }},
          {"targets" : 7,"render" : function(data, type, row, meta) {
              var dom = '';
              dom += '<div>'
              dom += '<input type="button" class="btn btn-primary radius" value="选择" onclick="ditribution(' +
                  '\'' + row.empId + '\',\'' + row.campusId + '\',\''+ row.campusManager + '\',' +
                  '\'' + row.dpId + '\',\'' + row.dpManager + '\');" />';
              dom += '</div>';
              return dom;
          }}
      ]
  });
});

function _search() {
  empListTable.fnDraw(true);
}

function ditribution(empId, campusId, campusManager, dpId, dpManager) {
    var dataParam={empId:empId, campusId:campusId, campusManager:campusManager, dpId:dpId, dpManager:dpManager};
    if(noQuitEmp.length>0 && varjtIds=='XJ'){
        layer.msg('【'+noQuitEmp.join(",")+'】学员的招生老师未离职，不允许分配给校监！', {icon: 5, time: 2000});
    }else{
        if(assignType && assignType=='no'){
            layer.confirm('确定分配?', function () {
                assignFlag(dataParam);
            })
        }else{
            layer.confirm('确定分配?', function () {
                layer.confirm('是否学校分配？', {btn: ['是', '否']},
                    function () {
                        dataParam.assignFlag='1';
                        assignFlag(dataParam);
                    }, function () {
                        dataParam.assignFlag='0';
                        assignFlag(dataParam);
                    }
                );
            });
        }
    }
}

function assignFlag(dataParam){
    var ids = '';
    $.each($("input[name='userIds']"), function(index, data){
        ids += $(this).val() + ",";
    });
    if (ids && ids.length > 0) {
        ids = ids.substring(0, ids.length - 1);
    }

    dataParam.userIds=ids;
    $.ajax({
        url : varUrl,
        dataType : 'json',
        type : 'post',
        data : dataParam,
        success : function(data) {
            if (data.code == _GLOBAL_SUCCESS) {
                layer.msg('分配成功', {
                    icon : 1,
                    time : 1000
                }, function() {
                    layer_close();
                });
            }
        }
    });
}
