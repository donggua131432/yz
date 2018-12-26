 var myDataTable;

//            点击学员姓名展示的异动信息数
            var modifyShowArr=["1","2"];
            $(function() {
                _init_select('grade', dictJson.grade);
                _init_select('recruitType', dictJson.recruitType);
                _init_select('stdType', dictJson.stdType);
                _init_select('pfsnLevel', dictJson.pfsnLevel);
                _init_select('scholarship', dictJson.scholarship);
                _init_select('stdStage',dictJson.stdStage);
                _init_select('empStatus', dictJson.empStatus);
                $("#recruitType").change(function() {
          					_init_select({
          						selectId : 'grade',
          						ext1 : $(this).val()
          					}, dictJson.grade);
          				});
                _init_select('sg', dictJson.sg); //优惠分组
	           	 _init_select("inclusionStatus",dictJson.inclusionStatus);
	           	$("#sg").change(function() { //联动
	       			_init_select({
	       				selectId : 'scholarship',
	       				ext1 : $(this).val()
	       			}, dictJson.scholarship);
	       		 });
	            //是否分配辅导老师
                _init_select(    "isTutor", [
                    {"dictValue" : "0","dictName" : "否"},
                    {"dictValue" : "1","dictName" : "是"}
                ]);
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
        		 
                _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
                //考试县区
                _simple_ajax_select({
                    selectId: "taId",
                    searchUrl: '/testArea/findAllTestArea.do',
                    sData: {},
                    showText: function (item) {
                        return item.taName;
                    },
                    showId: function (item) {
                        return item.taId;
                    },
                    placeholder: '--请选择--'
                });
                //归属校区
                var url = '/stdEnroll/getHomeCampusInfo.do';
               	$.ajax({
                        type: "POST",
                        dataType : "json", //数据类型  
                        url: url+'?isStop=0',
                        success: function(data){
                     	     var campusJson = data.body;
                     	     if(data.code=='00'){
                     	    	_init_select("homeCampusId",campusJson); 	 
                     	     }
                        }
                   });
        		_simple_ajax_select({
        			selectId : "pfsnId",
        			searchUrl : '/baseinfo/sPfsn.do',
        			sData : {
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
                
                myDataTable = $('.table-sort').dataTable(
                        {
                            "processing": true,
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/jStudying/getStudyingList.do",
                                type : "post",
                                data : function(pageData) {
                                    return search_data(pageData);
                                }
                            },
                            "pageLength" : 10,
                            "pagingType" : "full_numbers",
                            "ordering" : false,
                            "searching" : false,
                            "lengthMenu" : [ 10, 20 ],
                            "createdRow" : function(row, data, dataIndex) {
                                $(row).addClass('text-c');
                            },
                            "language" : _my_datatables_language,
                            columns : [ {
                                "mData" : null
                            }, {
                                "mData" : null
                            },{
                                "mData" : "stdNo"
                            },  {
                                "mData" : "schoolRoll"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : "tutorName"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }],
                            "columnDefs" : [
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return '<input type="checkbox" value="'+ row.learnId + '" name="learnIds"/>';
                                        },
                                        "targets" : 0
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	var stdNo=row.stdNo||'--';
                                        	var dom = '';
                							dom = '<a title="编辑" href="javascript:;" onclick="toEidtStdNo(\''+ row.learnId +'\',\''+ row.stdNo +'\')">'+stdNo+'</a>';
                                            return dom;
                                        },
                                        "targets" : 2
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	var schoolRoll=row.schoolRoll||'--';
                                        	var dom = '';
                							dom = '<a title="编辑" href="javascript:;" onclick="toEidtSchoolRoll(\''+ row.learnId +'\',\''+ row.schoolRoll +'\')">'+schoolRoll+'</a>';
                                            return dom;
                                        },
                                        "targets" : 3
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	var dom='';
                                            dom +='<a class="c-blue name-mark-box" title="查看学员信息" href="javascript:;" onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\')">' + row.stdName  ;
                                            if(row.stdType=='2'){
                                                dom += '<span class="name-mark mark-red">外</span>';
                                            }
                                            dom+='</a>'
                                            return dom
                                        },
                                        "targets" : 1,"class":"no-warp"
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict('grade', row.grade);
                                        },
                                        "targets" : 4
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var text = unvsText(row);
                                            return text ? '<a class="stuName" title="查看课表" onclick="make_schedule(\''+row.grade+'\' ,\''+row.pfsnLevel+'\',\''+row.unvsId+'\' ,\''+row.pfsnId+'\',\''+row.unvsName+'\',\''+row.pfsnName+'\'  )">'+text +'</a>': '无';
                                        },
                                        "targets" : 5,
                                        "class":"text-l no-warp"
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict('stdStage', row.stdStage);
                                        },
                                        "targets" : 7
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	if(row && row.stdStage=="7"){
                                        		return "有学籍";
                                        	}else{
                                        		return "无学籍";	
                                        	}
                                        },
                                        "targets" : 8
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';
                                            dom += '<span style="color:blue;">&nbsp;&nbsp;&nbsp;共：' + row.amount + '</span><br/>';
                                            dom += '<span style="color:black;">&nbsp;&nbsp;&nbsp;已：' + row.paid + '</span><br/>';
                                            dom += '<span>&nbsp;&nbsp;&nbsp;欠：' + row.debt + '</span>';
                                            return dom;
                                        },
                                        "targets" : 9,
                                        "class" : "text-l"
                                    }]
                        });
            });

            var index;

            function showPaymentDetail(learnId, stdId) {
                var url = '/jStudying/showPaymentDetail.do' + '?learnId=' + learnId + '&stdId=' + stdId;
                layer_show('缴费明细', url, 1200, null, function() {
                    myDataTable.fnDraw(false);
                });
            }

//            function showContacts(learnId) {
//                var url = '/jStudying/showContacts.do' + '?learnId=' + learnId;
//                layer_show('联系方式', url, 1200, null, function() {
//                    myDataTable.fnDraw(false);
//                },true);
//            }

            /*查看学员信息*/
            function toEidt(learnId, stdId) {
                var url = '/studentBase/toEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId;
                layer_show('学员信息', url, null, null, function() {
                    myDataTable.fnDraw(false);
                }, true);
            }

            function showServices(learnId) {
                //layer.msg('功能开发中');
                var url = '/jStudying/showServices.do' + '?learnId=' + learnId;
                layer_show('服务记录', url, 1200, null, function() {
                    myDataTable.fnDraw(false);
                }); 
            }

            /*编辑学籍编号*/
            function toEidtStdNo(learnId, stdNo) {
                var url = '/jStudying/toEditStdNo.do' + '?learnId=' + learnId ;
                layer_show('学籍编号', url, 510, 510, function() {
                    myDataTable.fnDraw(false);
                }, true);
            }

            /*编辑学号*/
            function toEidtSchoolRoll(learnId, schoolRoll) {
                var url = '/jStudying/toEditSchoolRoll.do' + '?learnId=' + learnId
                layer_show('学号', url, 510, 510, function() {
                    myDataTable.fnDraw(false);
                }, true);
            }

            function _search() {
                myDataTable.fnDraw(true);
            }

            function search_data(pageData) {
                var data = {
                    stdName : $("#stdName").val() ? $("#stdName").val() : '',
                    idCard : $("#idCard").val() ? $("#idCard").val() : '',  
                    mobile : $("#mobile").val() ? $("#mobile").val() : '',  
                    grade : $("#grade").val() ? $("#grade").val() : '',
                    pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
                    unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
                    pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
                    recruitType : $("#recruitType").val() ? $("#recruitType").val() : '',
                    campusId : $("#campusId").val() ? $("#campusId").val() : '',
                    dpId : $("#dpId").val() ? $("#dpId").val() : '',
                    recruitName : $("#recruitName").val() ? $("#recruitName").val() : '',
                    tutorName : $("#tutorName").val() ? $("#tutorName").val() : '',
                    stdNo : $("#stdNo").val() ? $("#stdNo").val() : '',
                    stdType : $("#stdType").val() ? $("#stdType").val() : '',
                    schoolRoll : $("#schoolRoll").val() ? $("#schoolRoll").val() :'',
                    sg: $("#sg").val() ? $("#sg").val() : '',
                    scholarship : $("#scholarship").val() ? $("#scholarship").val() :'',
                    inclusionStatus :$("#inclusionStatus").val() ? $("#inclusionStatus").val() :'',	
                    homeCampusId :$("#homeCampusId").val() ? $("#homeCampusId").val() :'',	
                    taId :$("#taId").val() ? $("#taId").val() :'',	
                    workPlace : $("#workPlace").val() ? $("#workPlace").val() : '',	
                    address : $("#address").val() ? $("#address").val() : '',	
                    stdStage : $("#stdStage").val() ? $("#stdStage").val() : '',
                    empStatus : $("#empStatus").val() ? $("#empStatus").val() : '',	
                    isTutor: $("#isTutor").val() ? $("#isTutor").val() : '',	
                    startBirthday: $("#startBirthday").val() ? $("#startBirthday").val() : '',	
                    endBirthday: $("#endBirthday").val() ? $("#endBirthday").val() : '',	
                    start : pageData.start,
                    length : pageData.length
                };

                return data;
            }

            function changeRemark(remark) {
                var temp = $(remark).attr("isCompleted") == '1' ? '0' : '1';
                $.ajax({
                    type : 'POST',
                    url : '/jStudying/changeRemark.do',
                    data : {
                        lrId : $(remark).attr("id"),
                        isCompleted : temp
                    },
                    dataType : 'json',
                    success : function(data) {
                        if (_GLOBAL_SUCCESS == data.code) {
                            $(remark).attr("isCompleted", temp);
                            if (temp == '1') {
                                $(remark).attr("class", "label label-success radius ml-5");
                            } else {
                                $(remark).attr("class", "label label-default radius ml-5");
                            }
                        }
                    }
                });
            }

            function register_import() {
                var url = '/stuRegister/toExcelImport.do';
                layer_show('高校学号导入', url, null, 510, function() {
                    myDataTable.fnDraw(true);
                });
            }

             function register_stdno_import() {
                 var url = '/stuRegister/toExcelStdNoImport.do';
                 layer_show('学籍编号导入', url, null, 510, function() {
                     myDataTable.fnDraw(true);
                 });
             }
            
            function wbstudent_import() {
                var url = '/jStudying/excelImport.do';
                layer_show('外校区学员导入', url, null, 510, function() {
                    myDataTable.fnDraw(true);
                });
            }
            
            //退学申请
            var leanId,outName;
            function student_out() {
                leanId=$("table input[name=learnIds]:checked:first").val();
                outName=$("table input[name=learnIds]:checked:first").parent().siblings('td').find(".stuName").text();
                var url = '/studentOut/edit.do' + '?exType=ADD';
                layer_show('添加退学学员', url, null, 510, function() {
                    myDataTable.fnDraw(false);
                });
            }
            /*链接到课表*/
            function make_schedule(grade,pfsnLevel,unvsId,pfsnId,unvsName,pfsnName) {
                pUnvsName=unvsName;
                pPfsnName=pfsnName
            	var courseType="XK";
            	var semester="1";
            	var url = '/classPlan/makeScheduleShow.do'+ '?courseType=' + courseType + '&grade=' + grade + '&pfsnLevel='+pfsnLevel+'&unvsId='+unvsId+'&pfsnId='+pfsnId+'&semester='+semester;
                layer_show('生成课表', url, null, 510, function() {
                    myDataTable.fnDraw(false);
                }, true);
            }
            function unvsText(data) {
                var pfsnName = data.pfsnName;
                var unvsName = data.unvsName;
                var recruitType = data.recruitType;
                var pfsnCode = data.pfsnCode;
                var pfsnLevel = data.pfsnLevel;

                var text = '';
                if(recruitType) {
                    _findDict("recruitType", recruitType).indexOf('成人')==-1?text +='[国开]':text +='[成教]'
                }
                if(unvsName) {
                    text += unvsName + "<br>";
                }

                if(pfsnLevel) {
                    _findDict("pfsnLevel", pfsnLevel).indexOf('高中')==-1?text +='[本科]':text +='[专科]'
                }
                if(pfsnName) {
                    text += pfsnName;
                }
                if(pfsnCode) {
                    text += "(" + pfsnCode + ")";
                }
               return text;
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

            /* excel导出*/
            function query_excel_export() {
                var vaild=false;
                $.each($('#searchForm').serializeArray(),function () {
                    if(this['value']){
                        vaild=true;
                        return false;
                    }
                });
                if(!vaild){
                    layer.msg("至少要有一个查询条件！");
                    return;
                }
                $('<form method="get" accept-charset="UTF-8" action="' + '/jStudying/exportStudying.do' + '">'
                    + '<input type="text" name="stdName" value="' + $("#stdName").val() + '"/>'
                    + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
                    + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
                    + '<input type="text" name="idCard" value="' + $("#idCard").val() + '"/>'
                    + '<input type="text" name="mobile" value="' + $("#mobile").val() + '"/>'
                    + '<input type="text" name="grade" value="' + $("#grade").val() + '"/>'
                    + '<input type="text" name="recruitType" value="' + $("#recruitType").val() + '"/>'
                    + '<input type="text" name="dpId" value="' + $("#dpId").val() + '"/>'
                    + '<input type="text" name="recruitName" value="' + $("#recruitName").val() + '"/>'
                    + '<input type="text" name="tutorName" value="' + $("#tutorName").val() + '"/>'
                    + '<input type="text" name="schoolRoll" value="' + $("#schoolRoll").val() + '"/>'
                    + '<input type="text" name="stdType" value="' + $("#stdType").val() + '"/>'
                    + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
                    + '<input type="text" name="empName" value="' + $("#empName").val() + '"/>'
                    + '<input type="text" name="sg" value="' + $("#sg").val() + '"/>'
                    + '<input type="text" name="scholarship" value="' + $("#scholarship").val() + '"/>'
                    + '<input type="text" name="inclusionStatus" value="' + $("#inclusionStatus").val() + '"/>'
                    + '<input type="text" name="campusId" value="' + $("#campusId").val() + '"/>'
                    + '<input type="text" name="workPlace" value="' + $("#workPlace").val() + '"/>'
                    + '<input type="text" name="address" value="' + $("#address").val() + '"/>'
                    + '<input type="text" name="homeCampusId" value="' + $("#homeCampusId").val() + '"/>'
                    + '<input type="text" name="taId" value="' + $("#taId").val() + '"/>'
                    + '<input type="text" name="stdStage" value="' + $("#stdStage").val() + '"/>'
                    + '<input type="text" name="empStatus" value="' + $("#empStatus").val() + '"/>'
                    + '<input type="text" name="isTutor" value="' + $("#isTutor").val() + '"/>'
                    + '<input type="text" name="startBirthday" value="' + $("#startBirthday").val() + '"/>'
                    + '<input type="text" name="endBirthday" value="' + $("#endBirthday").val() + '"/>'
                    + '</form>').appendTo('body').submit().remove();
            }

            function query_excel_gk_export() {
                var vaild=false;
                $.each($('#searchForm').serializeArray(),function () {
                    if(this['value']){
                        vaild=true;
                        return false;
                    }
                });
                if(!vaild){
                    layer.msg("至少要有一个查询条件！");
                    return;
                }
                $('<form method="get" accept-charset="UTF-8" action="' + '/jStudying/exportStudyingGK.do' + '">'
                    + '<input type="text" name="stdName" value="' + $("#stdName").val() + '"/>'
                    + '<input type="text" name="unvsId" value="' + $("#unvsId").val() + '"/>'
                    + '<input type="text" name="pfsnId" value="' + $("#pfsnId").val() + '"/>'
                    + '<input type="text" name="idCard" value="' + $("#idCard").val() + '"/>'
                    + '<input type="text" name="mobile" value="' + $("#mobile").val() + '"/>'
                    + '<input type="text" name="grade" value="' + $("#grade").val() + '"/>'
                    + '<input type="text" name="recruitType" value="' + $("#recruitType").val() + '"/>'
                    + '<input type="text" name="dpId" value="' + $("#dpId").val() + '"/>'
                    + '<input type="text" name="recruitName" value="' + $("#recruitName").val() + '"/>'
                    + '<input type="text" name="tutorName" value="' + $("#tutorName").val() + '"/>'
                    + '<input type="text" name="schoolRoll" value="' + $("#schoolRoll").val() + '"/>'
                    + '<input type="text" name="stdType" value="' + $("#stdType").val() + '"/>'
                    + '<input type="text" name="pfsnLevel" value="' + $("#pfsnLevel").val() + '"/>'
                    + '<input type="text" name="empName" value="' + $("#empName").val() + '"/>'
                    + '<input type="text" name="sg" value="' + $("#sg").val() + '"/>'
                    + '<input type="text" name="scholarship" value="' + $("#scholarship").val() + '"/>'
                    + '<input type="text" name="inclusionStatus" value="' + $("#inclusionStatus").val() + '"/>'
                    + '<input type="text" name="campusId" value="' + $("#campusId").val() + '"/>'
                    + '<input type="text" name="workPlace" value="' + $("#workPlace").val() + '"/>'
                    + '<input type="text" name="address" value="' + $("#address").val() + '"/>'
                    + '<input type="text" name="homeCampusId" value="' + $("#homeCampusId").val() + '"/>'
                    + '<input type="text" name="taId" value="' + $("#taId").val() + '"/>'
                    + '<input type="text" name="stdStage" value="' + $("#stdStage").val() + '"/>'
                    + '<input type="text" name="empStatus" value="' + $("#empStatus").val() + '"/>'
                    + '<input type="text" name="isTutor" value="' + $("#isTutor").val() + '"/>'
                    + '<input type="text" name="startBirthday" value="' + $("#startBirthday").val() + '"/>'
                    + '<input type="text" name="endBirthday" value="' + $("#endBirthday").val() + '"/>'
                    + '</form>').appendTo('body').submit().remove();
            }