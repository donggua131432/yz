 $(function () {
    	$("#orderNo").text(varOrderNo);
    	var result=JSON.parse(vartrack);

        if(result==null){
    		$("#logistics").text("暂无物流信息！");
    	}
        result.reverse();
    	$.each(result, function(index, data){
            	var dom = "";
            if(index=='0'){
                dom += '<div class="item active">';
            }else {
                dom += '<div class="item">';
            }
		       	dom += '<div class="remark">' + data.content + ' --'+data.operator+'</div>';
		       	dom += '<div class="time">' + data.msgTime + '</div>';
		       	dom += '</div>';
		        $("#logistics").append(dom);
	    });
    });
    