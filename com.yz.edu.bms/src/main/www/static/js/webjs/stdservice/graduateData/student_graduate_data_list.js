var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			//初始状态
			_init_select("ifSubmit",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("ifPass",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("ifMail",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
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

			//初始化院校类型下拉框
			_init_select("recruitType",dictJson.recruitType);
			_init_select('stdStage', dictJson.stdStage);
			_init_select("pfsnLevel",dictJson.pfsnLevel);
			$("#recruitType").change(function() {
				_init_select({
					selectId : 'grade',
					ext1 : $(this).val()
				}, dictJson.grade);
			});
			
		  	_simple_ajax_select({
				selectId : "taskId",
				searchUrl : '/studyActivity/findGraduateDataTaskInfo.do',
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
					url : '/graduateData/getDataList.do',
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
					{"mData" : null},
					{"mData" : "taskTitle"},
					{"mData" : "stdNo"},
					{"mData" : "schoolRoll"},
					{"mData" : "stdName"},
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : "noPassReason"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : "expressNo"},
					{"mData" : "tutorName"},
					{"mData" : "remark"},
					{"mData" : null}
				],
				"columnDefs" : [
		                {"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.id + '" name="ids"/>';
						},
						"targets" : 0
						},
						{"targets" : 6, "class" : "text-l", "render" : function(data, type, row, meta) {
							var pfsnName = row.pfsnName,unvsName = row.unvsName,recruitType = row.recruitType,
								 pfsnCode = row.pfsnCode,pfsnLevel = row.pfsnLevel,text = '';
							if (recruitType) {
								if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
									text += "[成教]";
								}else {
									text += "[国开]";
								}
							}
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
							return _findDict("stdStage", row.stdStage);
						}},
						{"targets" : 8,"render" : function(data, type, row, meta) {
							if(row.ifSubmit && row.ifSubmit=='1'){
								return "<label class='label label-success radius'>是</label>";
							}else{
								return "<label class='label  label-danger radius'>否</label>";
							}
						}},
						{"targets" : 9,"render" : function(data, type, row, meta) {
							if(row.ifPass && row.ifPass=='1'){
								return "<label class='label label-success radius'>是</label>";
							}else{
								return "<label class='label label-danger radius'>否</label>";
							}
						}},
						{"targets" : 11,"render" : function(data, type, row, meta) {
							if(row.ifMail && row.ifMail=='1'){
								return "<label class='label label-success radius'>是</label>";
							}else{
								return "<label class='label label-danger radius'>否</label>";
							}
						}},
						{   "targets" : 12,
							"sClass":'text-l',
							"render" : function(data, type, row, meta) {
						    var dom = '';
						    if(row.userName !=null){
						    	dom +='收件人：'+row.userName+'<br/>';
						    	dom +='联系手机：'+row.mobile +'<br/>';
						    	dom +='收件地址：'+row.address;
						    }
							return dom;
						}},
						{"targets" : 16,"render" : function(data, type, row, meta) {
							var dom = '<a class="" href="javascript:;" title="编辑" onclick="editGraduateData(\'' + row.id + '\')"><i class="iconfont icon-edit"></i></a>';
							return dom;
						}}
				]
			});
		});

		/*编辑*/
		function editGraduateData(id) {
			var url = '/graduateData/toEdit.do' + '?id='+ id +'&exType=UPDATE';
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
		
	    function stuGraduateDataImport() {
	        var url = '/graduateData/stuGraduateDataImport.do';
	        layer_show('毕业资料提交导入', url, null, 510, function () {
	            myDataTable.fnDraw(true);
	        });
	    }
	    
	    function exportStuGraduateData(){
        	$("#export-form").submit();
        }