var myDataTable,columns,columnDefs;
$(function() {
    //初始化院校名称下拉框
    _simple_ajax_select({
        selectId: "unvsId",
        searchUrl: '/bdUniversity/findAllKeyValue.do',
        sData: {},
        showText: function (item) {
            return item.unvs_name;
        },
        showId: function (item) {
            return item.unvs_id;
        },
        placeholder: '--请选择--'
    });
    $("#unvsId").append(new Option("", "", false, true));
    //初始化年级下拉框
    _init_select("grade", dictJson.grade);
    //初始化专业层次下拉框
    _init_select("pfsnLevel", dictJson.pfsnLevel);
    //初始化课程类型
	_init_select("textbookType", dictJson.courseType);

    //初始化学期
    _init_select("semester", dictJson.semester);
    //学员状态
    _init_select("stdStageQ", dictJson.stdStage);
    
    _init_select("empStatus", dictJson.empStatus);
    
    _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
    
    
    $("#unvsId").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
     });
    $("#grade").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
     });
	 $("#pfsnLevel").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
     });
	 $("#pfsnId").append(new Option("", "", false, true));
	 $("#pfsnId").select2({
            placeholder: "--请先选择院校--"
     });
    
    
    myDataTable = $('.table-sort').dataTable({
        "processing": true,
        "serverSide": true,
        "dom": 'rtilp',
        "pageLength": 10,
        "pagingType": "full_numbers",
        "ordering": false,
        "searching": false,
        "lengthMenu": [10,100,1000],
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "language": _my_datatables_language,
        "columns":columns
        });
    });
   
    function okOrder() {
       	 var statGroupArray = new Array();
       	 var checkVal=['bu.unvs_id','bli.pfsn_id','bup.pfsn_level','bli.grade'];
     $.each($("input[name='statGroup']:checked"),function(){
    	 if(checkVal.indexOf($(this).val())>-1){
    		 statGroupArray.push($(this).val());	 
    	 }
     });
    var url = '/bookStat/okOrder.do';
    if(statGroupArray == '' || statGroupArray == null){
        layer.msg('请选分组条件！', {
            icon : 1,
            time : 2000
        });
        return false;
    }

    layer.confirm('确认要改为已订？', function(index) {
        $.ajax({
            type : 'POST',
            url : url,
            data : {
            	statGroup : statGroupArray
            },
            dataType : 'json',
            success : function(data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    layer.msg('已订成功!', {
                        icon : 1,
                        time : 1000
                    });
                    myDataTable.fnDraw(true);
                }
            }
        });
    });
}

function _search_order() {
	 var statGroupArray = new Array(), stdStageArray = new Array();
     $.each($("input[name='statGroup']:checked"),function(){
         statGroupArray.push($(this).val());
     });

     $.each($("input[name='stdStage']:checked"),function(){
         stdStageArray.push($(this).val());
     });

     if($("#year").val() == '' && $("#grade").val() == ''){
         layer.msg('【招生年度】和【年级】必须至少指定一个查询条件！', {
             icon : 1,
             time : 2000
         });
         return false;
     }
     if(stdStageArray == '' || stdStageArray == null){
         layer.msg('请选择学员状态！', {
             icon : 1,
             time : 2000
         });
         return false;
     }
     if(statGroupArray == '' || statGroupArray == null){
         layer.msg('请选分组条件！', {
             icon : 1,
             time : 2000
         });
         return false;
     }

     myDataTable.fnDestroy(true);
     addTr();
     myDataTable = $('.table-sort').dataTable({
         "processing": true,
         "serverSide": true,
         "dom": 'rtilp',
         "ajax": {
             url: "/bookStat/bookStatList.do",
             type: "post",
             data: function (pageData) {
                 return searchData(pageData);
             }
         },
         "pageLength": 10,
         "pagingType": "full_numbers",
         "ordering": false,
         "searching": false,
         "lengthMenu": [10,100,1000],
         "createdRow": function (row, data, dataIndex) {
             $(row).addClass('text-c');
         },
         "language": _my_datatables_language,
         "columns":columns,
         "columnDefs" :columnDefs
     });
}

function addTr() {
    $("#statTable").empty();
    $("#statTable").html("<table class='table table-border table-bordered table-hover table-bg table-sort'><thead><tr id='statTableTr' class='text-c'></tr></thead><tbody></tbody></table>");
    var tr = $("#statTableTr");
    columns = new Array();
    columnDefs = new Array();
    columnDefs.push({
        "targets": 2,"class":"text-l"
    })
    var statGroupArray = new Array();
    $.each($("input[name='statGroup']:checked"),function(){
        statGroupArray.push($(this).val());
    });
    for(var i=0; i<statGroupArray.length; i++){
        if(statGroupArray[i] == 'bu.unvs_id'){
            tr.append("<th width='150'>院校名称</th>")
            var columnData = {};
            columnData['mData']='unvsName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bli.pfsn_id'){
            tr.append("<th>专业名称</th>")
            var columnData = {};
            columnData['mData']='pfsnName';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bup.pfsn_level'){
            tr.append("<th>专业层次</th>")
            var columnData = {};
            columnData['mData']='pfsnLevel';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bli.grade'){
            tr.append("<th>年级</th>")
            var columnData = {};
            columnData['mData']='grade';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bss.semester'){
            tr.append("<th>学期</th>")
            var columnData = {};
            columnData['mData']='semester';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bup.year'){
            tr.append("<th>年度</th>")
            var columnData = {};
            columnData['mData']='year';
            columns.push(columnData);
        }

        if(statGroupArray[i] == 'bsi.std_id'){
            tr.append("<th>学员</th>")
            var columnData = {};
            columnData['mData']='stdName';
            columns.push(columnData);
            tr.append("<th>身份证</th>")
            var columnData1 = {};
            columnData1['mData']='idCard';
            columns.push(columnData1);
        }

        if(statGroupArray[i] == 'bli.std_stage'){
            tr.append("<th>学员状态</th>")
            var columnData = {};
            columnData['mData']='stdStage';
            columns.push(columnData);
            columnDefs.push({"render" : function(data, type, row, meta) {
            	return _findDict("stdStage", row.stdStage);
			},
			"targets" : columns.length-1
			});
            console.log(columns.length);
        }
    }

    tr.append("<th>教材编码</th>");
    var columnData1 = {};
    columnData1['mData']='alias';
    columns.push(columnData1);
    
    tr.append("<th>教材名称</th>");
    var columnData2 = {};
    columnData2['mData']='textbookName';
    columns.push(columnData2);
    
    tr.append("<th>单价</th>");
    var columnData3 = {};
    columnData3['mData']='price';
    columns.push(columnData3);
    
    tr.append("<th>数量</th>");
    var columnData4 = {};
    columnData4['mData']='orderNum';
    columns.push(columnData4);
    
    tr.append("<th>总价</th>");
    var columnData5 = {};
    columnData5['mData']='totalPrice';
    columns.push(columnData5);
}

function searchData(pageData) {
    var statGroupArray = new Array(), stdStageArray = new Array();
    $.each($("input[name='statGroup']:checked"),function(){
        statGroupArray.push($(this).val());
    });

    $.each($("input[name='stdStage']:checked"),function(){
        stdStageArray.push($(this).val());
    });

    return {
    	stdName : $("#stdName").val() ? $("#stdName").val() : '',
    	idCard : $("#idCard").val() ? $("#idCard").val() : '',
        unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
        pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
        pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
        grade : $("#grade").val() ? $("#grade").val() : '',
      	semester : $("#semester").val() ? $("#semester").val() : '',
      	stdStageQ : $("#stdStageQ").val() ? $("#stdStageQ").val() : '',
        zempName : $("#zempName").val() ? $("#zempName").val() : '',
        empStatus : $("#empStatus").val() ? $("#empStatus").val() : '',
        campusId : $("#campusId").val() ? $("#campusId").val() : '',
        dpId : $("#dpId").val() ? $("#dpId").val() : '',
        stdStage :stdStageArray ? stdStageArray.join(','):'',
        statGroup : statGroupArray ? statGroupArray.join(','):'',
        start : pageData.start,
        length : pageData.length
    };
}

function exportBookStat(){
	  var statGroupArray = new Array(), stdStageArray = new Array();
      $.each($("input[name='statGroup']:checked"),function(){
          statGroupArray.push($(this).val());
      });

      $.each($("input[name='stdStage']:checked"),function(){
          stdStageArray.push($(this).val());
      });
      if(statGroupArray == '' || statGroupArray == null){
          layer.msg('请选分组条件！', {
              icon : 1,
              time : 2000
          });
          return false;
      }
      $("#stdStage").val(stdStageArray);
      $("#statGroup").val(statGroupArray);
  	 $("#export-form").submit();
}

function init_pfsn_select() {
	_simple_ajax_select({
		selectId : "pfsnId",
		searchUrl : '/baseinfo/sPfsn.do',
		sData : {
			sId :  function(){
				return $("#unvsId").val() ? $("#unvsId").val() : '';	
			},
			ext1 : function(){
				return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
			},
			ext2 : function(){
				return $("#grade").val() ? $("#grade").val() : '';
			}
		},
		showText : function(item) {
			var text = '(' + item.pfsnCode + ')' + item.pfsnName;
			text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
			return text;
		},
		showId : function(item) {
			return item.pfsnId;
		},
		placeholder : '--请选择专业--'
	});
	$("#pfsnId").append(new Option("", "", false, true));
}