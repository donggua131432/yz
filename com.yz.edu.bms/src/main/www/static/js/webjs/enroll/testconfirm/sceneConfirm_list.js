 var myDataTable;
        $(function () {
        	
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
                placeholder: '--请选择院校--'
            });
            $("#unvsId").append(new Option("", "", false, true));
            
            //初始化专业层次下拉框
            _init_select("pfsnLevel", dictJson.pfsnLevel);
            
            _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
            
            _init_select("empStatus", dictJson.empStatus);
            
            //是否有考生号
            _init_select("isStdNo", [
                     {"dictValue" : "1","dictName" : "有"},
                     {"dictValue" : "0","dictName" : "无"}
                 ]);
            _init_select("isArrears", [
                                       {"dictValue" : "0","dictName" : "是"},
                                       {"dictValue" : "1","dictName" : "否"}
                                   ]);
            //有无加分
            _init_select("isExamScore", [
                     {"dictValue" : "1","dictName" : "有"},
                     {"dictValue" : "0","dictName" : "无"}
                 ]);
            
            _init_select("isAddScore", [
                     {"dictValue" : "1","dictName" : "有"},
                     {"dictValue" : "0","dictName" : "无"}
                 ]);
            
            _init_select("isEnroll", [
                     {"dictValue" : "1","dictName" : "已录取"},
                     {"dictValue" : "0","dictName" : "未录取"}
                 ]);
            
            _init_select("isSame", [
                     {"dictValue" : "1","dictName" : "相同"},
                     {"dictValue" : "0","dictName" : "不同"}
                 ]);
            //渲染下拉框
            _init_select('recruitType', dictJson.recruitType);
            _init_select('stdStage', dictJson.stdStage);
            _init_select('recruitStatus', dictJson.empStatus);
            _init_select('grade', dictJson.grade);
            _init_select('scholarship', dictJson.scholarship);
            //是否已完善资料
            _init_select("isTestCompleted", [
                {"dictValue" : "0","dictName" : "未完善"},
                {"dictValue" : "1","dictName" : "已完善"}
            ]);
            //是否已报名确认
            _init_select("isOk", [
                {"dictValue" : "0","dictName" : "未确认"},
                {"dictValue" : "1","dictName" : "已确认"}
            ]);
            //是否已通知打印准考证
            _init_select("isPrintNotice", [
                {"dictValue" : "0","dictName" : "未通知"},
                {"dictValue" : "1","dictName" : "已通知"}
            ]);
            //是否已通知参加考试
            _init_select("isExamNotice", [
                {"dictValue" : "0","dictName" : "未通知"},
                {"dictValue" : "1","dictName" : "已通知"}
            ]);
            //是否已参加考试签到
            _init_select("isExamSign", [
                {"dictValue" : "0","dictName" : "未签到"},
                {"dictValue" : "1","dictName" : "已签到"}
            ]);
	   		 _init_select('sg', dictJson.sg); //优惠分组
			 _init_select('scholarship', dictJson.scholarship);
	   		 _init_select("inclusionStatus",dictJson.inclusionStatus);
	   		 $("#sg").change(function() { //联动
	   			_init_select({
	   				selectId : 'scholarship',
	   				ext1 : $(this).val()
	   			}, dictJson.scholarship);
	   		 });
	   		 
	   		 $("#unvsId").change(function () {
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
                "ajax": {
                    url: "/sceneConfirm/findSceneConfirmStd.do",
                    type: "post",
                    data: function (pageData) {
                        pageData = $.extend({},{start:pageData.start, length:pageData.length},$("#confirmFrom").serializeObject());
                        return pageData;
                    }
                },
                "pageLength": 10,
                "pagingType": "full_numbers",
                "ordering": false,
                "searching": false,
                "lengthMenu": [10, 20,50,100],
                "createdRow": function (row, data, dataIndex) {
                    $(row).addClass('text-c');
                },
                "language": _my_datatables_language,
                columns: [
                    {"data": null},
                    {"data": null},
                    {"data": null},
                    {"data": null},
                    {"data": null},
                    {"data": "ta_name"},
                    {"data": null},
                    {"data": null},
                    {"data": null},
                    {"data": null},
                    {"data": "exam_no"},
                    {"data": null},
                    {"data": null},
                    {"data": null}
                ],
                "columnDefs": 
					[{
						"targets" : 0,
						"render" : function(data, type,row, meta) {
							var dom = '';
							dom = row.std_name;
							if (row.std_type === '2') {
								dom += ' <sup style="color:#f00">外</sup>';
							}
							return dom;
						}
					},
					{
						"targets" : 1,
						"render" : function(data, type,row, meta) {
							for (var i = 0; i < dictJson.grade.length; i++) {
								if (dictJson.grade[i].dictValue == row.grade) {
									return dictJson.grade[i].dictName;
								}
							}
							return '';
						}
					},
					{
						"targets" : 2,
						"class" : "text-l",
						"render" : function(data, type,row, meta) {
							var pfsnName = row.pfsn_name, unvsName = row.unvs_name, pfsnCode = row.pfsn_code, pfsnLevel = row.pfsn_level, recruitType = row.recruit_type, enrollPfsnName = row.enroll_pfsn_name, enrollUnvsName = row.enroll_unvs_name;
							var text = '';
							if (_findDict("recruitType",recruitType).indexOf("成人") != -1) {
								text += "[成教]";
							} else {
								text += "[国开]";
							}
							if (unvsName) {
								text += unvsName+ "<br/>";
							}
							if (_findDict("pfsnLevel",pfsnLevel).indexOf("高中") != -1) {
								text += "[专科]";
							} else {
								text += "[本科]";
							}
							if (pfsnName) {
								text += pfsnName;
							}
							if (pfsnCode) {
								text += "("+ pfsnCode+ ")";
							}
							
							return text ? text: '无';
						}
					},
					{
						"targets" : 3,
						"class" : "text-l",
						"render" : function(data, type, row, meta) {
							var enrollPfsnName = row.enroll_pfsn_name, enrollUnvsName = row.enroll_unvs_name, unvsName = row.unvs_name;
							var text = '';
							if (enrollUnvsName) {
								text += enrollUnvsName;
							}
							
							if (enrollPfsnName) {
								text += '<br/>'+ enrollPfsnName;
							}
							if (unvsName && enrollUnvsName) {
								if (unvsName != enrollUnvsName) {
									text = '<span>'+ text+ '</span>';
								}
							}
							return text ? text: '暂无录取信息';
						}
					},
					{
						"targets" : 4,
						"render" : function(data, type,row, meta) {
							return _findDict("stdStage",row.std_stage);
						}
					},
					{
						"targets" : 6,
						"render" : function(data, type,row, meta) {
							var scholarship = _findDict('scholarship',row.scholarship);
							if (row.inclusionStatus != null
									&& scholarship != null) {
								if (row.inclusionStatus == '4' || row.inclusionStatus == '5' || row.inclusionStatus == '6') {
									return scholarship + "<br/>["+ _findDict('inclusionStatus',row.inclusionStatus)+ "]";
								}
							}
							return scholarship;
						}
					},
					{
						"targets" : 7,
						"render" : function(data, type,row, meta) {
							var dom = '';
							if (row && row.fee_amount) {
								dom = '第一年学费<br/>';
								dom += '应:' + row.fee_amount+ "<br/>";
							}
							if (row && row.payable
									&& row.sub_order_status
									&& row.sub_order_status == '2') {
								dom += '<span>已:'+ row.payable+ "</span><br/>";
							} else if (row.fee_amount == null && row.payable == null) {
								dom += '暂无缴费信息';
							}
							return dom;
						}
					},
					{
						"targets" : 8,
						"render" : function(data, type,row, meta) {
							var dom = '';
							dom = row.recruit_name;
							if (row.emp_status === '2') {
								dom += ' <sup style="color:#f00">离</sup>';
							}
							return dom;
						}
					},
					{
						"targets" : 9,
						"render" : function(data, type,row, meta) {
							if (row.is_ok) {
								return row.is_ok == '0' ? "未确认": "已确认";
							} else {
								return "未确认";
							}
						}
					},
					{
						"targets" : 11,
						"render" : function(data, type,row, meta) {
							if (row.is_exam_sign) {
								return row.is_exam_sign == '0' ? "未签到": "已签到";
							} else {
								return "未签到";
							}
						}
					},
					{
						"sClass" : 'text-l',
						"targets" : 12,
						"render" : function(data, type,row, meta) {
							var dom = '';
							var addScore = row.points;
							if (!addScore) {
								addScore = 0;
							}
							var paparScore = 0;
							var totalScore = 0;
							for (var i = 0; i < row.scores.length; i++) {
								var score = row.scores[i];

								dom += score.courseName + ':'+ score.score+ '</br>';
								paparScore += parseFloat(score.score);
							}
							if (null != addScore && addScore != '') {
								totalScore = parseFloat(addScore) + paparScore;
							} else {
								totalScore = paparScore;
							}
							dom += '总考分:'+ paparScore + '<br/>';
							dom += '加分:'+ addScore+ '<br/>';
							dom += '总分:'+ totalScore;
							return dom;
						}
					},
					{
						"targets" : 13,
						"render" : function(data, type,row, meta) {
							var dom = '';
							dom = '<a title="编辑" href="javascript:void(0);" onclick="eidt(\''
									+ row.std_id
									+ '\', \''
									+ row.learn_id
									+ '\')" class="ml-5" style="text-decoration: none">';
							dom += '<i class="iconfont icon-edit"></i></a>';
							return dom;
						}
					} ]
			   });
			});

			function _search() {
				myDataTable.fnDraw(true);
			}

			/*现场确认学员-编辑*/
			function eidt(stdId, learnId) {
				var url = '/sceneConfirm/toUpdateSceneConfirmStd.do'
						+ '?stdId=' + stdId + '&learnId=' + learnId;
				layer_show('学员现场确认编辑', url, 1000, 500, function() {
					//                myDataTable.fnDraw(false);
				}, true);
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