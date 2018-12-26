$(function () {
	
	if ((isSuper && isSuper == '1')|| (isXJ && isXJ == '1')) {
		$("#tab_demo_bar").append("<span id=\"is_xj\">我的团队绩效</span>");
	} else {
		$("#tab_demo_bar").html('');
	}
	$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
	
	$.ajax({
        type: 'POST',
        url: '/performance/myPerformance.do',
        data: {
            empId : function(){
            	return $("#empId").val();
            }
        },
        dataType: 'json',
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
            	printPerformance(data.body,'1');
            }
        }
    });
	
	_simple_ajax_select({
        selectId: "underEmpId",
        searchUrl: '/performance/sUnderEmpId.do',
        sData: {
            
        },
        showText: function (item) {
            var text = item.empName;
            return text;
        },
        showId: function (item) {
            return item.empId;
        },
        placeholder: '--请选择招生老师--'
    });
	
	
    
});


function printPerformance(data,type){
	
	var empId = $("#empId").val();
	var divId = 'performanceInfo';
	if('2' == type){
		empId = $("#underEmpId").val();
		divId = 'performanceInfoTeam';
	}
	
	$("#allReply").text(data.allReply);
	
	var monthRecruit = 0;
	var monthOut = 0;
	var replay = 0;
	var replayed = 0;
	
	var allMonthRecruit = 0;
	var allMonthOut = 0;
	var allReplay = 0;
	var allReplayed = 0;
	
	var dom = '';
	
	for (var i = 0; i < data.performances.length; i++) {
		
		var per = data.performances[i];
		if(per.recruitCount){
			monthRecruit = monthRecruit + (per.recruitCount * 1);
		}
		if(per.outCount){
			monthOut = monthOut + (per.outCount * 1);
		}
		if(per.reply){
			replay = replay + (per.reply * 1);
		}
		if(per.replyed){
			replayed = replayed + (per.replyed * 1);
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
		dom += per.reply;
		dom += '</td>';
		
		dom += '<td>';
		dom += per.replyed;
		dom += '</td>';
		
		dom += '<td>';
		dom +='<a title="查看学员" href="javascript:;" onclick="checkStudent(\''
            + per.month + '\',\''+ empId +'\')" class="ml-5" style="text-decoration: none">';
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
	dom += replay.toFixed(2);
	dom += '</td>';
	
	dom += '<td>';
	dom += replayed.toFixed(2);
	dom += '</td>';
	
	dom += '<td>';
	dom += '</td>';
	
	dom += '</tr>';
	
	$('#'+divId).html(dom);
	
	
}

function underEmpIdChange(){
	var underEmpId = $("#underEmpId").val();
	if(underEmpId){
		$.ajax({
	        type: 'POST',
	        url: '/performance/myPerformance.do',
	        data: {
	            empId : function(){
	            	return $("#underEmpId").val();
	            }
	        },
	        dataType: 'json',
	        success: function (data) {
	            if (data.code == _GLOBAL_SUCCESS) {
	            	printPerformance(data.body,'2');
	            }
	        }
	    });
	} else {
		$("#performanceInfoTeam").html("");
	}
}

function checkStudent(month,empId){
	 var url = '/performance/toStudents.do?empId=' + empId  + '&month=' + month;
     layer_show('查看学员', url, null, 510, function () {
     });
}