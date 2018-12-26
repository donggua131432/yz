$(function () {
        $("#orderNo").text(varorderInfo.orderNo);
        $("#orderStatus").text(showOrderStatus(varorderInfo.orderStatus));
        $("#userName").text(varorderInfo.userName == null ? '无' : varorderInfo.userName);
        $("#orderTime").text(varorderInfo.orderTime);
        $("#goodsName").text(varorderInfo.goodsName);
        $("#unitPrice").text(varorderInfo.unitPrice);
        $("#goodsCount").text(varorderInfo.goodsCount);
        $("#transAmount").text(varorderInfo.transAmount);
        $("#takeUserName").text(varorderInfo.takeUserName== null ? '无' : varorderInfo.takeUserName);
        $("#address").text(varorderInfo.address==null ? '无' : varorderInfo.address);
        $("#takeMobile").text(varorderInfo.takeMobile== null ? '无' : varorderInfo.takeMobile);
        $("#freight").text(varorderInfo.freight== null ? '0' : varorderInfo.freight);
		
      
    });
    function showOrderStatus(orderStatus){
		return _findDict("saleOrderStatus",orderStatus);
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