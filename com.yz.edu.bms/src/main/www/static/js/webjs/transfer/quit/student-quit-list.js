	var myDataTable;
	$(function() {
		var arr = [];
		var barWidth;
		 //初始化年级下拉框
        _init_select("grade", dictJson.grade);
		//初始化原因
		_init_select("reason", dictJson.quitSchoolReason);
		_init_select("recruitType", dictJson.recruitType);
        _init_select("pfsnLevel", dictJson.pfsnLevel);
        _init_select('sg', dictJson.sg); //优惠分组
     	_init_select('scholarship', dictJson.scholarship); //优惠类型
     	_init_select('inclusionStatus',dictJson.inclusionStatus); //入围状态
     	
     	//初始审批状态
	    _init_select("checkStatus",[
	  	 {
	  	 			"dictValue":"2","dictName":"待审核"
	  	 		},
	  	 {
	  	 			"dictValue":"3","dictName":"审核通过"
	  	 		},
	  	 {
	  	 			"dictValue":"4","dictName":"驳回"
	  	 		}
	  	 	]);
	   $('select').select2({
           placeholder : "--请选择--",
           allowClear : true,
           width : "59%"
       });
		
	   $("#sg").change(function() { //联动
   		_init_select({
   			selectId : 'scholarship',
   			ext1 : $(this).val()
   		}, dictJson.scholarship);
   	   });	
	   
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
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : "/quitSchool/findAllBdStudentQuit.do",
				type : "post",
				data : {
					"stdName" : function() {
						return $("#stdName").val();
					},
					"mobile" : function() {
						return $("#mobile").val();
					},
					"idCard" : function() {
						return $("#idCard").val();
					},
					"reason" : function() {
						return $("#reason").val();
					},
					"checkStatus" : function() {
						return $("#checkStatus").val();
					},
					"grade" : function() {
						return $("#grade").val();
					},
					"pfsnLevel" : function() {
						return $("#pfsnLevel").val();
					},
					"pfsnId" : function() {
						return $("#pfsnId").val();
					},
					"recruitName" : function() {
						return $("#recruitName").val();
					},
					"tutorName" : function() {
						return $("#tutorName").val();
					},
					"recruitType" : function() {
						return $("#recruitType").val();
					},
					"sg" : function() {
						return $("#sg").val();
					},
					"scholarship" : function() {
						return $("#scholarship").val();
					},
					"inclusionStatus" : function() {
						return $("#inclusionStatus").val();
					},
					"unvsId" : function() {
						return $("#unvsId").val();
					},
					"stdNo" : function (){
						return $("#stdNo").val();
					},
					"applyUserName" :function (){
						return $("#applyUserName").val();
					},
					"applyTimeBegin" : function (){
						return $("#applyTimeBegin").val();
					},
					"applyTimeEnd" : function (){
						return $("#applyTimeEnd").val();
					}
				}
			},
			"pageLength" : 10,
			"pagingType" : "full_numbers",
			"ordering" : false,
			"searching" : false,
			"createdRow" : function(row, data, dataIndex) {
				$(row).addClass('text-c');
				$(row).children('td').eq(6).attr(
						'style', 'text-align: left;');
			},
			"language" : _my_datatables_language,
			columns : [ {
				"mData" : null
			}, {
				"mData" : "stdName"
			}, {
				"mData" : null
			}, {
				"mData" : null
			}, {
				"mData" : null
			}, {
				"mData" : "stdNo"
			}, {
				"mData" : null
			}, {
				"mData" : null
			}, {
				"mData" : null
			}, {
				"mData" : null
			} , {
				"mData" : "applyUserName"
			} , {
				"mData" : null
			} , {
				"mData" : null
			} 
			],
			"columnDefs" : [ {
				"render" : function(data, type, row, meta) {
					if(row.checkStatus !='3'){
						return '<input type="checkbox" value="'+ row.id + '" name="ids"/>';	
					}
					return null;
				},
				"targets" : 0
			}, {
				"render" : function(data, type, row, meta) {
					return _findDict("grade", row.grade);
				},
				"targets" : 2
			}, {
				"render" : function(data, type, row, meta) {
				    var text='';
                    if(_findDict("recruitType", row.recruitType).indexOf('成人')!=-1){
                        text+='[成教]'
                    }else {
                        text+='[国开]'
                    }
                    text+=row.unvsName+'<br>'
                    if(_findDict("pfsnLevel", row.pfsnLevel).indexOf('高中')!=-1){
                        text+='[专科]'
                    }else {
                        text+='[本科]'
                    }
                    text+=row.pfsnName
					return text
				},
				"targets" : 3,
                "class":"text-l"
			}, {
				"render" : function(data, type, row, meta) {
                    var scholarship= _findDict('scholarship', row.scholarship);
                    if(row.inclusionStatus!=null && scholarship!=null ){
                        if(row.inclusionStatus == '4' || row.inclusionStatus=='5' || row.inclusionStatus=='6') {
                            return scholarship + "<br/>[" + _findDict('inclusionStatus', row.inclusionStatus) + "]";
                        }
                    }
                    return scholarship;
				},
				"targets" : 4
			}, {
				"render" : function(data, type, row, meta) {
					return _findDict("stdStage", row.oldStdStage);
				},
				"targets" : 6
			}, {
				"render" : function(data, type, row, meta) {
					return _findDict("stdStage", row.stdStage);
				},
				"targets" : 7
			}, {
				"render" : function(data, type, row, meta) {
					return _findDict("quitSchoolReason", row.reason);
				},
				"targets" : 8
			}, {
				"render" : function(data, type, row, meta) {
					var dateTime = row.applyTime;
                    if(!dateTime){
                        return '-'
                    }
                    var date=dateTime.substring(0,10)
                    var time=dateTime.substring(11,19)
                    return date+'<br>'+time
				},
				"targets" : 9,"class":"text-l"
			}, {
				"render" : function(data, type, row, meta) {
					return _findDict("checkStatus", row.checkStatus);
				},
				"targets" : 11
			}
			,{
				"render" : function(data, type, row, meta) {
					var dom = '';
						dom = '<a title="查看" href="javascript:;" onclick="lookQuit(\''+ row.id+ '\')" class="ml-5" style="text-decoration: none">';
					    dom += '查看</a>';
					return dom;
				},
				//指定是第10列是操作列
				"targets" : 12
			} 
			]
		});

	});
	
	function lookQuit(id){
		var url = '/quitSchool/lookQuitSchool.do' + '?id='+id;
		layer_show('查看休学学员信息', url, null, 700, function() {
		});
	}

	/*用户-休学*/
	function studentQuit() {
		var url = '/quitSchool/edit.do' + '?exType=ADD';
		layer_show('申请休学学员', url, null, 550, function() {
		});
	}
	
	function cancelApply() {
		var chk_value = [];
		$("input[name=ids]:checked").each(function() {
			chk_value.push($(this).val());
		});
		if(chk_value == null || chk_value.length <= 0){
			layer.msg('未选择任何数据!', {
				icon : 5,
				time : 1000
			});
			return;
		}

		layer.confirm('确认要取消未审批申请？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/quitSchool/cancelApply.do',
				data : {
					ids : chk_value
				},
				dataType : 'json',
				success : function(data) {
					if (data.code == _GLOBAL_SUCCESS) {
						layer.msg('取消成功!', {
							icon : 1,
							time : 1000
						});
						myDataTable.fnDraw(false);
						$("input[name=all]").attr("checked", false);
					}
				}
			});
		});
	}

function _search() {
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