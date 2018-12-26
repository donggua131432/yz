$(function () {
        $("#orderNo").text(varOrderInfo.orderNo);
        $("#orderStatus").text(showOrderStatus(varOrderInfo.orderStatus));
        $("#userName").text(varOrderInfo.userName == null ? '无' : varOrderInfo.userName);
        $("#orderTime").text(varOrderInfo.orderTime);
        $("#goodsName").text(varOrderInfo.goodsName);
        $("#jdPrice").text(varOrderInfo.jdPrice);
        $("#goodsCount").text(varOrderInfo.goodsCount);
        $("#orderPrice").text(varOrderInfo.orderPrice);
        $("#takeUserName").text(varOrderInfo.takeUserName== null ? '无' : varOrderInfo.takeUserName);
        $("#address").text(varOrderInfo.address==null ? '无' : varOrderInfo.address);
        $("#takeMobile").text(varOrderInfo.takeMobile== null ? '无' : varOrderInfo.takeMobile);
        $("#freight").text(varOrderInfo.freight== null ? '0' : varOrderInfo.freight);
		
       

    });
    function showOrderStatus(orderStatus){
		var dom = '';
		if(orderStatus == 3){
			dom = '已完成';
		}else if(orderStatus ==5){
			dom = '已拒收';
		}else{
			dom = '未知状态';
		}
		return dom;
    }
    
    
    var downloadPDFFlag=true;
    function pdf_export() {
    	//下载文件名
        var fileName=$("#orderNo").text();
		//体验优化
        $("#downloadPDF").html('<i class="iconfont icon-daochu"></i> 导出中...');
        $(".pdfItem").css('display','block');
        
        if(downloadPDFFlag){
			//拼串，渲染页面
            
             var tab1=$("#pdfExportDiv").html();
             var content='<div class="pdfItem" >'+tab1+'</div>'

             $('.pdfContentDiv').html(content);
          
             $('#btn').css('display','none');
             downloadPDFFlag=false;
         }

         $('.pdfItem').width(2080).height(3100).tableExport({type:'pdf',
             fileName:fileName,
             jspdf: {orientation: 'p',
                 margins: {left:10, top:10},
                 autotable:false
             }
         },function () {
        	 $('#btn').css('display','block');
             $('.pdfItem').css("display","none");
             $("#downloadPDF").html('<i class="iconfont icon-daochu"></i> 导出PDF');
         });
  
    }