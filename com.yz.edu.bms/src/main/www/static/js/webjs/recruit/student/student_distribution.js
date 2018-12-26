var empListTable;
$(function() {
  empListTable = $('#empListTable').dataTable({
      "serverSide" : true,
      "dom" : 'rtilp',
      "ajax" : {
          url : "/recruit/getEmpList.do",
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
              dom += '<div>';
              dom += '<input type="button" class="submit btn btn-primary radius" value="选择" onclick="ditribution(\'' + row.empId + '\',\''
                      + row.campusId + '\',\'' + row.dpId + '\',\'' + row.empName + '\',\'' + row.campusName + '\',\'' + row.dpName + '\',\''
                      + row.campusManager + '\',\'' + row.empStatus + '\');" />';
              dom += '</div>';
              return dom;
          }}
      ]
  });
});

function _search() {
  empListTable.fnDraw(true);
}

function ditribution(empId, campusId, dpId, empName, campusName, dpName, campusManager, empStatus) {
  var dataParam={empId:empId, campusId:campusId, dpId:dpId, empName:empName, campusName:campusName, dpName:dpName, campusManager:campusManager, empStatus:empStatus};
  layer.confirm('确定分配？', function() {
      layer.confirm('是否学校分配？',{btn: ['是','否']},
          function() {
              dataParam.assignFlag='1';
              assignFlag(dataParam);
          },function () {
              dataParam.assignFlag='0';
              assignFlag(dataParam);
          }
      );
  });
}

function assignFlag(dataParam){
    var empInfoName = '';
    if (dataParam.empName) {
        if (dataParam.campusName && 'null' != dataParam.campusName)  empInfoName += '[' + dataParam.campusName + '] — ';
        if (dataParam.dpName && 'null' != dataParam.dpName) empInfoName += '[' + dataParam.dpName + '] — ';
        empInfoName += dataParam.empName + '(' + _findDict('empStatus', dataParam.empStatus) + ')';
        if(dataParam.assignFlag=='1') empInfoName +=" — 学校分配";
    } else {
        empInfoName = '无';
    }

    var submitData = {
        learnId : $("#learnId").val()
    };

    if ("ZSLS" == $("#jtIds").val()) {
        submitData.recruit = dataParam.empId;
        submitData.recruitDpId = dataParam.dpId;
        submitData.recruitCampusId = dataParam.campusId;
        submitData.recruitCampusManager = dataParam.campusManager;
        submitData.assignFlag = dataParam.assignFlag;
        parent.$("#recruitName").text(empInfoName);
    } else {
        submitData.tutor = dataParam.empId;
        submitData.tutorDpId = dataParam.dpId;
        submitData.tutorCampusId = dataParam.campusId;
        submitData.tutorCampusManager = dataParam.campusManager;
        parent.$("#tutorName").text(empInfoName);
    }

    $.ajax({
        url : '/recruit/distribution.do',
        dataType : 'json',
        type : 'post',
        data : submitData,
        success : function(data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var result = data.body;
                if(result=="success"){
                    layer.msg("分配成功", {icon : 1,time : 1000}, function() {
                        layer_close();
                    });
                }else{
                    layer.msg("选择成功", {icon : 1,time : 1000}, function() {
                        parent.$("#assignFlag").val(dataParam.assignFlag);
                        parent.$("#recruit").val(dataParam.empId);
                        parent.$("#recruitDpId").val(dataParam.dpId);
                        parent.$("#recruitCampusId").val(dataParam.campusId);
                        parent.$("#recruitCampusManager").val(dataParam.campusManager);
                        layer_close();
                    });
                }
            }
        }
    });
}