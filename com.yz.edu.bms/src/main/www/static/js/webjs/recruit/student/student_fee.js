  var feeListTable;
  $(function() {
      var grade = grade_global;
      var queryUrl = '/' + queryUrl_global;
      feeListTable = $('#feeListTable').dataTable({
          "serverSide" : true,
          "dom" : 'rtilp',
          "ajax" : {
              url : queryUrl,
              type : "post",
              data : {
                  learnId : learnId_global
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
              {"mData" : "payable"},
              {"mData" : null},
              {"mData" : null}
          ],
          "columnDefs" : [
              {"targets" : 0, "render" : function(data, type, row, meta) {
                  var itemName = getItemName(row.itemName,grade);
                  return row.itemCode + ':' + itemName;
              }},
              {"targets" : 2, "render" : function(data, type, row, meta) {
                  return _findDict('orderStatus', row.subOrderStatus);
              }},
              {"targets" : 3, "render" : function(data, type, row, meta) {
                  var dom = '<table class="table table-border table-bordered table-hover table-bg table-sort">'
                  dom += '<thead>';
                  dom += '<tr class="text-l">';
                  dom += '<th index="0" width="60">流水号</th>';
                  dom += '<th index="1" width="40">交易金额</th>';
                  dom += '<th index="2" width="40">支付方式</th>';
                  dom += '<th index="3" width="60">支付时间</th>';
                  dom += '<th index="4" width="60">审核人</th>';
                  dom += '<th index="5" width="60">审核时间</th>';
                  dom += '<th index="6" width="40">流水状态</th>';
                  dom += '</tr>';
                  dom += '</thead>';
                  dom += '<tbody>';
                  if(row.serialInfo && row.serialInfo.length > 0) {
                      dom += createTbody(row.serialInfo);
                  } else {
                      dom += '<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有流水信息</div></td></tr>';
                  }
                  dom += '</tbody>';
                  dom += '</table>';
                  return dom;
              }}
          ]
      });
  });

  function search() {
      myDataTable.fnDraw(true);
  }

  function createTbody(serialList) {
      var text = '';
      $.each(serialList, function(index, data){
          text += '<tr class="text-l">';
          text += '<td>' + (data.serialNo ? data.serialNo : '') + '</td>';
          text += '<td>' + (data.amount ? data.amount : '') + '</td>';
          text += '<td>' + _findDict('paymentType', data.paymentType) + '</td>';
          text += '<td>' + (data.payTime ? data.payTime : '') + '</td>';
          text += '<td>' + (data.checkUser ? data.checkUser : '') + '</td>';
          text += '<td>' + (data.checkTime ? data.checkTime : '') + '</td>';
          text += '<td>' + (_findDict('serialStatus', data.serialStatus) ? _findDict('serialStatus', data.serialStatus) : '状态异常') + '</td>';
          text += '</tr>';
      });
      return text;
  }