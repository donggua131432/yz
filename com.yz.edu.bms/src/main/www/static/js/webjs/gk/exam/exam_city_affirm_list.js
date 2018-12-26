	var myDataTable;
	$(function() {
		$('select').select2({
			placeholder : "--请选择--",
			allowClear : true,
			width : "59%"
		});
		//初始状态
		_init_select("isView",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
		_init_select("isExam",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
		_init_select("isReset",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
        _init_select("isAffirm",[
            {"dictValue":"0","dictName":"否"},
            {"dictValue":"1","dictName":"是"}
        ]);
        
		_simple_ajax_select({
			selectId : "unvsId",
			searchUrl : '/bdUniversity/findAllKeyValue.do',
			sData : {},
			showText : function(item) {
				return item.unvs_name;
			},
			showId : function(item) {
				return item.unvs_id;
			},
			placeholder : '--请选择院校--'
		});
		$("#unvsId").append(new Option("", "", false, true));
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
		//初始化年级下拉框
		_init_select("grade",dictJson.grade);
		_init_select("pfsnLevel",dictJson.pfsnLevel);
		

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
		
		
		_simple_ajax_select({
			selectId : "ecId",
			searchUrl : '/studentCityAffirmGK/getExamCityForGK.do',
			sData : {status:"1"},
			showText : function(item) {
				return item.ecName;
			},
			showId : function(item) {
				return item.ecId;
			},
			placeholder : '--请选考场城市--'
		});
		$("#ecId").append(new Option("", "", false, true));
		
		
	  	_simple_ajax_select({
			selectId : "taskId",
			searchUrl : '/studyActivity/findTaskInfo.do?taskType=13',
			sData : {},
			showText : function(item) {
				return item.task_title;
			},					
			showId : function(item) {
				return item.task_id;
			},
			placeholder : '--请选择任务--'
		});
		$("#taskId").append(new Option("", "", false, true));
		
		myDataTable = $('.table-sort').dataTable({
			"processing": true,
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : '/studentCityAffirmGK/findStudentCityAffirmGK.do',
				type : "post",
				data : function(pageData){
                    pageData = $.extend({},{start:pageData.start, length:pageData.length},$("#export-form").serializeObject());
                    return pageData;
				}
			},
			"pageLength" : 10,
			"pagingType" : "full_numbers",
			"ordering" : false,
			"searching" : false,
			"createdRow" : function(row, data, dataIndex) {
				$(row).addClass('text-c');
			},
			"language" : _my_datatables_language,
			columns : [
				{"mData" : "examYear"},
				{"mData" : "grade"},
				{"mData" : "stdName"},
				{"mData" : "schoolRoll"},
				{"mData" : null},
				{"mData" : "ecName"},
				{"mData" : "reason"},
                {"mData" : "isAffrim"},
				{"mData" : "isExam"},
				{"mData" : "isReset"},
				{"mData" : "tutor"},
				{"mData" : null}
			],
			"columnDefs" : [
					{"targets" : 4, "class" : "text-l", "render" : function(data, type, row, meta) {
						var pfsnName = row.pfsnName,unvsName = row.unvsName,recruitType = row.recruitType,
							 pfsnCode = row.pfsnCode,pfsnLevel = row.pfsnLevel,text = '';
						
						if(unvsName) text += unvsName+'</br>';
						if(pfsnLevel) {
							if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
								text += "[专科]";
							}else {
								text += "[本科]";
							}
						}
						if(pfsnName) text += pfsnName;
						if(pfsnCode) text += "(" + pfsnCode + ")";
						return text ? text : '无';
					}},
					{"targets" : 7,"render" : function(data, type, row, meta) {
						if(row.isAffirm && row.isAffirm =='1'){
							return "<label class='label label-success radius'>已确认</label>";
						}else{
							return "<label class='label label-danger radius'>未确认</label>";
						}
					}},
					{"targets" : 8,"render" : function(data, type, row, meta) {
						if(row.isExam && row.isExam =='1'){
							return "<label class='label label-success radius'>是</label>";
						}else{
							return "<label class='label label-danger radius'>否</label>";
						}
					}},
					{"targets" : 9,"render" : function(data, type, row, meta) {
						if(row.isReset && row.isReset=='1'){
							return "<label class='label label-success radius'>是</label>";
						}else{
							return "<label class='label label-danger radius'>否</label>";
						}
					}},
					{"targets" : 11,"render" : function(data, type, row, meta) {
						var dom = '<a class="" href="javascript:;" title="编辑" onclick="editExamCityAffirm(\'' + row.affirmId + '\')"><i class="iconfont icon-edit"></i></a>';
						return dom;
					}}
			]
		});
	});

	/*编辑*/
	function editExamCityAffirm(id) {
		var url = '/studentCityAffirmGK/toEdit.do' + '?id='+ id ;
		layer_show('编辑', url, 880, 730, function() {
		});
	}

	/*搜素*/
	function searchResult(){
		myDataTable.fnDraw(true);
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
	//导出excel
    function gkCityAffirmExport(){
    	$("#export-form").submit();
    }
    //统计
    function toStatistics(){
    	var url = '/studentCityAffirmGK/toStatistics.do';
		layer_show('统计', url, 1200, 700, function() {
			//myDataTable.fnDraw(true);
		});
    }