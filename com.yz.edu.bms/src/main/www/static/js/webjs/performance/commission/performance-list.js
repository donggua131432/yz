 $(function () {
        	$.ajax({
                type: 'POST',
                url: '/commission/myCommission.do',
                data: {
                    empId : function(){
                    	return $("#empId").val();
                    }
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                    	printPerformance(data.body);
                    }
                }
            });
            
        });
	//标签块
        
        function printPerformance(data){
        	$("#allamount").text(data.allamount);
        	
        	var monthRecruit = 0;
        	var monthOut = 0;
        	
        	var allMonthRecruit = 0;
        	var allMonthOut = 0;
        	var allReplay = 0;
        	
        	var dom = '';
        	
        	for (var i = 0; i < data.performances.length; i++) {
        		
				var per = data.performances[i];
        		if(per.recruitCount){
        			monthRecruit = monthRecruit + (per.recruitCount * 1);
        		}
        		if(per.outCount){
        			monthOut = monthOut + (per.outCount * 1);
        		}
        		if(per.amount){
        			allReplay = allReplay + (per.amount * 1);
        		}
        		
				dom += '<tr class="text-c">';
				
				dom += '<td>';
				dom += per.month;
				dom += '</td>';
				
				dom += '<td>';
				dom += per.recruitCount;
				dom += '</td>';
				
				dom += '<td>';
				dom += per.outCount;
				dom += '</td>';
				
				dom += '<td>';
				dom += per.amount;
				dom += '</td>';
				
				dom += '<td>';
				dom +='<a title="查看学员" href="javascript:;" onclick="checkStudent(\''
                    + per.month + '\',\''+ $("#empId").val() +'\')" class="ml-5" style="text-decoration: none">';
                dom += '<i class="iconfont icon-chakan"></i></a>'
				dom += '</td>';
				
				dom += '</tr>';
			}
        	dom += '<tr class="text-c">';
			
			dom += '<td>';
			dom += '累计';
			dom += '</td>';
			
			dom += '<td>';
			dom += monthRecruit.toFixed(2);
			dom += '</td>';
			
			dom += '<td>';
			dom += monthOut.toFixed(2);
			dom += '</td>';
			
			dom += '<td>';
			dom += allReplay.toFixed(2);
			dom += '</td>';
			
			dom += '<td>';
			dom += '</td>';
			
			dom += '</tr>';
        	
        	$("#performanceInfo").html(dom);
        }
        
        function checkStudent(month,empId){
        	 var url = '/commission/toStudents.do' + '?empId=' + empId  + '&month=' + month;
	         layer_show('查看学员', url, null, 510, function () {
	         });
        }