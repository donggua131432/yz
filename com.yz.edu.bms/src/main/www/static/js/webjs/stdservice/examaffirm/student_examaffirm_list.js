var myDataTable;
		$(function() {
			$('select').select2({
				placeholder : "--请选择--",
				allowClear : true,
				width : "59%"
			});
			//初始状态
			_init_select("isAffirm",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("isRemark",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			_init_select("isSign",[
				{"dictValue":"1","dictName":"是"},
				{"dictValue":"0","dictName":"否"}
			]);
			//初始考试年度下拉框
			$.ajax({
				type: "POST",
				dataType : "json", //数据类型
				url: '/examAffirm/getExamYear.do?status=',
				success: function(data){
					var eyJson = data.body;
					if(data.code=='00'){
						_init_select("testYear",eyJson);
					}
				}
			});
			$("#reason").attr("disabled", "disabled");
			$("#testYear").change(function() {
				var selected = $(this).val();
				$.ajax({
					 type: "POST",
					 dataType : "json", //数据类型
					 url: '/examAffirm/getExamReason.do?eyId='+selected,
					 success: function(data){
						 var erJson = data.body;
						 if(data.code=='00'){
							$("#reason").removeAttr("disabled");
							_init_select("reason",erJson);
						 }
					 }
				});
			});

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
			//初始考场
			_simple_ajax_select({
				selectId: "placeId",
				searchUrl: '/examRoomAssign/findAllKeyValue.do',
				sData: {
				},
				showText: function (item) {
					return item.ep_name;
				},
				showId: function (item) {
					return item.ep_id;
				},
				placeholder: '--请选择考场--'
			});
			$("#placeId").append(new Option("", "", false, true));
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
			_init_area_select("provinceCode", "cityCode", "districtCode","440000");
			$("#districtCode").append("<option value=''>请选择</option>");

			myDataTable = $('.table-sort').dataTable({
				"processing": true,
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : '/examAffirm/getStuList.do',
					type : "post",
					data : function(pageData){
                        pageData = $.extend({},{start:pageData.start, length:pageData.length},$("#searchFrom").serializeObject());
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
					{"mData" : "testYear"},
					{"mData" : "stdName"},
					{"mData" : "grade"},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : null},
					{"mData" : "tutorName"},
					{"mData" : null},
					{"mData" : null}
				],
				"columnDefs" : [
					{"targets" : 3, "class" : "text-l", "render" : function(data, type, row, meta) {
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
					{"targets" : 4,"render" : function(data, type, row, meta) {
						return _findDict("stdStage", row.stdStage);
					}},
					{"targets" : 5,"class" : "text-l","render" : function(data, type, row, meta) {
						var dom = '';
						if(row && row.epName !=null){
							dom +=row.epName+'<br/>';
							dom +=row.startTime+'<br/>';
							dom +=row.endTime;
						}
						return dom;
					}},
					{"targets" : 6,"render" : function(data, type, row, meta) {
						var dom='';
						if(row && row.unconfirmedReason !=null){
							dom += row.unconfirmedReason;
						}
						return dom;
					}},
					{"targets" : 7,"render" : function(data, type, row, meta) {
						if(row.signStatus && row.signStatus=='0'){
							return "<label class='label label-danger radius'>未签到</label>";
						}else{
							return "<label class='label label-success radius'>已签到</label>";
						}
					}},
					{"targets" : 9,"render" : function(data, type, row, meta) {
						if(row.resetStatus && row.resetStatus=='0'){
							return "<label class='label label-danger radius'>未重置</label>";
						}else{
							return "<label class='label label-success radius'>已重置</label>";
						}
					}},
					{"targets" : 10,"render" : function(data, type, row, meta) {
						var dom = '<a class="" href="javascript:;" title="编辑" onclick="resetResult(\'' + row.affirmId + '\')"><i class="iconfont icon-edit"></i></a>';
						return dom;
					}}
				]
			});
		});

		/*重置*/
		function resetResult(affirmId) {
			var url = '/examAffirm/toEdit.do' + '?affirmId='+ affirmId +'&exType=UPDATE';
			layer_show('编辑', url, 780, 450, function() {
//				myDataTable.fnDraw(false);
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

function examaffirmExport(){
	layer.msg("正在导出Excel，请勿再次点击！")
    $("#searchFrom").submit();
}