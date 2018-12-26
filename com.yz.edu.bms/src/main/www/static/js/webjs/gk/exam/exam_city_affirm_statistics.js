var myDataTable,columns,columnDefs;
$(function() {
	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});
	//国开考试年度	
	url = '/studyActivity/getExamYearForGK.do';
	$.ajax({
         type: "POST",
         dataType : "json", //数据类型  
         url: url+'?status=1',
         success: function(data){
      	     var eyJson = data.body;
      	     if(data.code=='00'){
      	    	_init_select("eyId",eyJson); 	 
      	     }
         }
    });	
});

function statistics(){
	 if($("#eyId").val() == ''){
         layer.msg('请先选择考试年度！', {
             icon : 1,
             time : 2000
         });
         return false;
     }

     addTr();
    // console.log(columns);
    // console.log(columnDefs);
	//初始化数据表格
     myDataTable = $('.table-sort').dataTable({
         "processing": true,
         "serverSide": true,
         "dom": 'rtilp',
         "ajax": {
             url: "/studentCityAffirmGK/statistics.do",
             type: "post",
             data: function (pageData) {
                 return searchData(pageData);
             }
         },
         "pageLength": 10,
         "pagingType": "full_numbers",
         "ordering": false,
         "searching": false,
         "createdRow": function (row, data, dataIndex) {
             $(row).addClass('text-c');
         },
         "language": _my_datatables_language,
         "destroy":true,
         "columns":columns,
         "columnDefs" :columnDefs
     });
	 
	 
}


function searchData(pageData) {
    var statGroupArray = new Array();
    $.each($("input[name='statGroup']:checked"),function(){
        statGroupArray.push($(this).val());
    });
    return {
    	eyId : $("#eyId").val() ? $("#eyId").val() : '',
    	statGroup : statGroupArray ? statGroupArray.join(','):'',
        start : pageData.start,
        length : pageData.length
    };
}


function addTr() {
    $("#statTable").empty();
    $("#statTable").html("<table class='table table-border table-bordered table-bg table-sort' id='tab'><thead><tr id='statTableTr' class='text-c'></tr></thead><tbody></tbody></table>");
    var tr = $("#statTableTr");
    columns = new Array();
    columnDefs = new Array();
    
    var statGroupArray = new Array();
    $.each($("input[name='statGroup']:checked"),function(){
        statGroupArray.push($(this).val());
    });
    tr.append("<th>考试城市</th>");
    var columnDataAll = {};
    columnDataAll['mData']='ecName';
    columns.push(columnDataAll);
    
    for(var i=0; i<statGroupArray.length; i++){
        if(statGroupArray[i] == 'bli.grade'){
            tr.append("<th width='90'>年级</th>");
            var columnData = {};
            columnData['mData']='';
            columns.push(columnData);            
            columnDefs.push({"render" : function(data, type, row, meta) {
            	return _findDict("grade", row.grade);
			},
			"targets" : columns.length-1
			});
        }
        if(statGroupArray[i] == 'u.unvs_name'){
            tr.append("<th>院校</th>")
            var columnData = {};
            columnData['mData']='unvsName';
            columns.push(columnData);
        }
        if(statGroupArray[i] == 'p.pfsn_level'){
            tr.append("<th>层次</th>")
            var columnData = {};
            columnData['mData']='';
            columns.push(columnData);
            columnDefs.push({"render" : function(data, type, row, meta) {
            	return _findDict("pfsnLevel", row.pfsnLevel);
			},
			"targets" : columns.length-1
			});
        }
        if(statGroupArray[i] == 'p.pfsn_id'){
            tr.append("<th>层业</th>")
            var columnData = {};
            columnData['mData']='pfsnName';
            columns.push(columnData);
        }
       
    }

    $("#statTableTr").append("<th>人数</th>");
    var columnDataAll = {};
    columnDataAll['mData']='number';
    columns.push(columnDataAll);

}