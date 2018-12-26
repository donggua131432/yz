var myDataTable;
            var pfsnName;
			$(function() {

				//初始化学期下拉框
				_init_select("semester",dictJson.semester,$("#semester").val());

				//初始化类型下拉框
				_init_select("thpType",dictJson.thpType,$("#thpType").val());
				
				//初始化年级下拉框
				 _init_select("grade",dictJson.grade,$("#grades").val())
				//院校
				_simple_ajax_select({
					selectId : "unvsIds2",
					searchUrl : '/bdUniversity/searchUniversity.do',
					sData : {},
					showText : function(item) {
						return item.text;
					},					
					showId : function(item) {
						return item.id;
					},
					placeholder : '--请选择院校--'
				});

				//专业层次
				_init_select("pfsnLevels2",dictJson.pfsnLevel,$("#pfsnLevel").val());
				
				$("#pfsnIds2").select2({
					placeholder : "--请选择专业--",
					allowClear : true
				});	
			     
				//初始化专业
				var showPfsnName = '(' + $("#pfsnCode").val() + ')' + $("#pfsnName").val();
				showPfsnName += '[' + _findDict('pfsnLevel', $("#pfsnLevel").val()) + ']';
				$("#pfsnIds2").append(new Option(showPfsnName, $("#pfsnId").val(), false, true));

				//初始院校
				$("#unvsIds2").append(new Option($("#unvsName").val(), $("#unvsId").val(), false, true));

				$('.skin-minimal input').iCheck({
					checkboxClass : 'icheckbox-blue',
					radioClass : 'iradio-blue',
					increaseArea : '20%'
				});
				$("#unvsIds2").change(function () {
	    	        init_pfsn_select();
		    	});
	            $("#grade").change(function () {
	    	        init_pfsn_select();
		    	});
		    	$("#pfsnLevels2").change(function () {
	    	          init_pfsn_select();
		    	});
				$("#form-member-add").validate({
					rules : {
					    pfsnId : {
							required : true,
						},
						thpName : {
							required : true,
						},
						semester : {
							required : true,
						},
						unvsId : {
							required : true,
						},
						pfsnLevel : {
							required : true,
						},
						grade:{
							required : true,
						}
					},
					messages : {
						errorCode : {
							remote : "参数名称已存在"
						}
					},
					onkeyup : false,
					focusCleanup : true,
					success : "valid",
					submitHandler : function(form) {
						$(form).ajaxSubmit({
							type : "post", //提交方式  
							dataType : "json", //数据类型  
							url : $("#exType").val() == "UPDATE" ? '/teachPlan/editTeachPlan.do' : '/teachPlan/insertTeachPlan.do', //请求url  
							success : function(data) { //提交成功的回调函数  
								layer_close();
							},
							error : function(data) {
								layer.msg('操作失败', {
									icon : 1,
									time : 1000
								});
								layer_close();
							}
						})
					}
				});
			});
			
			function init_pfsn_select(){
				//专业
				_simple_ajax_select({
					selectId : "pfsnIds2",
					searchUrl : '/baseinfo/sPfsn.do',
					sData : {
						sId :  function(){
							return $("#unvsIds2").val() ? $("#unvsIds2").val() : '';	
						},
						ext1 : function(){
							return $("#pfsnLevels2").val() ? $("#pfsnLevels2").val() : '';
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
				$("#pfsnIds2").append(new Option("", "", false, true));
			}