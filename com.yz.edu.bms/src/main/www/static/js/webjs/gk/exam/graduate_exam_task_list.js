	var myDataTable;
	$(function() {
		$('select').select2({
			placeholder : "--请选择--",
			allowClear : true,
			width : "59%"
		});
		//初始状态
		_init_select("isPayreg",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
		_init_select("isTest",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
		_init_select("isPass",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
        _init_select("isRemark",[
            {"dictValue":"0","dictName":"否"},
            {"dictValue":"1","dictName":"是"}
        ]);
        _init_select("enrollSubject",[
			{"dictValue":"1","dictName":"大学英语B"},
			{"dictValue":"2","dictName":"计算机应用基础"}
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
		_init_select("notification",dictJson.notification);

	  	_simple_ajax_select({
			selectId : "taskId",
			searchUrl : '/studyActivity/findTaskInfo.do?taskType=14',
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
				url : '/studentGraduateExamGK/findStudentGraduateExamGK.do',
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
				{"mData" : "stdName"},
				{"mData" : "grade"},
				{"mData" : null},
				{"mData" : "schoolRoll"},
				{"mData" : "taskTitle"},
				{"mData" : null},
				{"mData" : null},
                {"mData" : "remark"},
				{"mData" : "tutor"},
				{"mData" : null}
			],
			"columnDefs" : [
					{"targets" : 2, "class" : "text-l", "render" : function(data, type, row, meta) {
						var  pfsnName = row.pfsnName,unvsName = row.unvsName,
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
					{"targets" : 5,"render" : function(data, type, row, meta) {
						return _findDict("notification", row.notification)
					}},
					{"targets" : 6,"render" : function(data, type, row, meta) {
						var dom = '';
						dom += '<table class="table table-border table-bordered radius">';
						dom += '<thead>';
						dom += '<tr>';
						dom += '<th class="td-s" width="80">考试县区</th>';
						dom += '<th class="td-s" width="40">缴费报名</th>';
						dom += '<th class="td-s" width="40">报名科目</th>';
						dom += '<th class="td-s" width="40">考试时间</th>';
						dom += '<th class="td-s" width="40">考试地点</th>';
						dom += '<th class="td-s" width="40">参考</th>';
						dom += '<th class="td-s" width="40">合格</th>';
						dom += '</tr>';
						dom += '</thead><tbody>';
						if(null != row.subinfos && row.subinfos.length > 0){
							for (var i = 0; i < row.subinfos.length; i++) {
								var subInfo = row.subinfos[i];
								
								var isPayregText,subjectText,isTestText,isPassText;
								if(subInfo.isPayreg && subInfo.isPayreg =='1'){
									isPayregText="<label class='label label-success radius'>是</label>";
								}else{
									isPayregText="<label class='label label-danger radius'>否</label>";
								}
				
								if(subInfo.enrollSubject && subInfo.enrollSubject =='2'){
									subjectText="计算机应用基础";
								}else if(subInfo.enrollSubject && subInfo.enrollSubject =='1'){
									subjectText="大学英语B";
								}else{
									subjectText="";
								}
								
								if(subInfo.isTest && subInfo.isTest =='1'){
									isTestText="<label class='label label-success radius'>是</label>";
								}else{
									isTestText="<label class='label label-danger radius'>否</label>";
								}
								
								if(subInfo.isPass && subInfo.isPass =='1'){
									isPassText="<label class='label label-success radius'>是</label>";
								}else{
									isPassText="<label class='label label-danger radius'>否</label>";
								}
								dom += '<tr>';
								dom += '<td class="td-s">'+ (subInfo.testArea||'')+ '</td>';
								dom += '<td class="td-s">'+ isPayregText+ '</td>';
								dom += '<td class="td-s">'+ subjectText+ '</td>';
								dom += '<td class="td-s">'+ (subInfo.testTime||'')+ '</td>';
								dom += '<td class="td-s">'+ (subInfo.testAddress||'')+ '</td>';
								dom += '<td class="td-s">'+ isTestText+ '</td>';
								dom += '<td class="td-s">'+ isPassText+ '</td>';
								dom += '</tr>';
							}
						}
						return dom;
					}},
					{"targets" : 9,"render" : function(data, type, row, meta) {
						var dom = '<a class="" href="javascript:;" title="编辑" onclick="editGraduateExamGK(\'' + row.followId + '\')"><i class="iconfont icon-edit"></i></a>';
						return dom;
					}}
			]
		});
	});

	/*编辑备注*/
	function editGraduateExamGK(id) {
		var url = '/studentGraduateExamGK/toEdit.do' + '?followId='+ id ;
		layer_show('修改备注', url, 600, 300, function () {
	    }, false);
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
	/**
	 * 同步学员统考科目信息
	 * @returns
	 */
	function synchronous(){
//		$.ajax({
//            type: 'POST',
//            url: '/studentGraduateExamGK/synchronous.do',
//            data : function(pageData){
//                pageData = $.extend({},$("#export-form").serializeObject());
//                return pageData;
//			},
//            dataType: 'json',
//            success: function (data) {
//                if (data.code == _GLOBAL_SUCCESS) { 
//                	layer.msg('订单同步成功!', {
//                        icon : 1,
//                        time : 1000
//                    });
//                    myDataTable.fnDraw(true);
//                }
//            }
//    	});
	}
	