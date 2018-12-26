$(function() {

          $.each(paymentInfos, function(index, data){
              var itemCode = data.itemCode;
              var itemName = getItemName(data.itemName,grade);
              var feeAmount = data.feeAmount ? data.feeAmount : '0.00';
              var offerAmount = data.offerAmount ? data.offerAmount : '0.00';
              var payable = data.payable ? data.payable : '0.00';
              var subOrderStatus = data.subOrderStatus;
              var dom = '<tr class="text-c">';
              dom += '<td>' + itemCode + ":" + itemName + '</td>';
              dom += '<td>' + feeAmount + '</td>';
              dom += '<td>' + offerAmount + '</td>';
              dom += '<td>' + payable + '</td>';
              if('1' == subOrderStatus) {
              	dom += '<td><i class="Hui-iconfont Hui-iconfont-close"></i></td>';
              } else {
             	 dom += '<td><i class="Hui-iconfont Hui-iconfont-xuanze"></i></td>';
              }
              dom += '</tr>';
                  $("#piTable tbody").append(dom);
          });
        var dom ='';
        dom += '<tr class="text-c"><td>合计</td>';
        dom += '<td>' + feeTotal + '</td>';
        dom += '<td>' + offerTotal + '</td>';
        dom += '<td>' + payableTotal + '</td>';
        dom += '<td></td></tr>';
        $("#piTable tbody").append(dom);
      });