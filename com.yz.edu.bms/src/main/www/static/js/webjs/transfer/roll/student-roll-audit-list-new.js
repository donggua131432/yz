		var excuteTable;
		var acceptTable;
		
$(function() {
			
		//标签块
		$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");

        //初始化年级下拉框
        _init_select("grade", dictJson.grade);
        _init_select("grade2", dictJson.grade);

        //初始化专业层次下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel);
        _init_select("pfsnLevel2", dictJson.pfsnLevel);

        //初始异动类型
        _init_select("changeType1", [{
            "dictValue": "1",
            "dictName": "个人信息异动"
        }, {
            "dictValue": "2",
            "dictName": "院校专业县区异动"
        }]);

        //初始异动类型
        _init_select("changeType2", [{
            "dictValue": "1",
            "dictName": "个人信息异动"
        }, {
            "dictValue": "2",
            "dictName": "院校专业县区异动"
        }]);


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
		 
        //初始化院校名称下拉框
        _simple_ajax_select({
            selectId: "unvsId2",
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
        $("#unvsId2").append(new Option("", "", false, true));
        $("#unvsId2").change(function () {
            $("#pfsnId2").removeAttr("disabled");
            init_pfsn_select_2();
	     });
        $("#grade2").change(function () {
            $("#pfsnId2").removeAttr("disabled");
            init_pfsn_select_2();
	     });
		 $("#pfsnLevel2").change(function () {
            $("#pfsnId2").removeAttr("disabled");
            init_pfsn_select_2();
	     });
		 $("#pfsnId2").append(new Option("", "", false, true));
		 $("#pfsnId2").select2({
	            placeholder: "--请先选择院校--"
	     });
        //初始化学员阶段
        _init_select("stdStage", dictJson.stdStage);
        _init_select("stdStage2", dictJson.stdStage);

        //初始化学员阶段
        _init_select("recruitType", dictJson.recruitType);
        _init_select("recruitType2", dictJson.recruitType);
		
	   acceptTable = $('#acceptTable').dataTable(
				{
                    "processing": true,
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {
						url : "/studentRoll/findStudentAccpetRollModifyNew.do",
						type : "post",
						data : {
                            "stdName": function () {
                                return $("#stdName").val();
                            }, "mobile": function () {
                                return $("#mobile").val();
                            }, "idCard": function () {
                                return $("#idCard").val();
                            }, "unvsId": function () {
                                return $("#unvsId").val();
                            }, "pfsnId": function () {
                                return $("#pfsnId").val();
                            }, "pfsnLevel": function () {
                                return $("#pfsnLevel").val();
                            }, "grade": function () {
                                return $("#grade").val();
                            }, "recruitType": function () {
                                return $("#recruitType").val();
                            }, "stdStage": function () {
                                return $("#stdStage").val();
                            }, "createStartTime": function () {
                                return $("#createStartTime").val();
                            }, "createEndTime": function () {
                                return $("#createEndTime").val();
                            }, "checkStartTime": function () {
                                return $("#checkStartTime").val();
                            }, "checkEndTime": function () {
                                return $("#checkEndTime").val();
                            }, "createUser": function () {
                                return $("#createUser").val();
                            }, "changeType": function () {
                                return $("#changeType1").val();
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
                    columns: [{
                        "mData": null
                    },
                    {
                        "mData": "stdName"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "createUser"
                    },  {
                        "mData": null
                    }, {
                        "mData": null
                    },{
                        "mData": null
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.modifyId + '" name="accept_modifyIds"  class="sendIdsCheckBox"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("grade", row.grade);
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom += row.unvsName+":";
                                dom += "(" + row.pfsnCode + ")" +row.pfsnName ;
                                _findDict("pfsnLevel", row.pfsnLevel).indexOf("高中") != -1 ? dom += "[5>高升专]" : dom += "[1>专升本]";
                                return dom;
                            },
                            "targets": 3,
                            "class": "text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("stdStage", row.stdStage);
                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("scholarship", row.scholarship);
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("inclusionStatus", row.inclusionStatus);
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = "";
                                if (null != row.newStdName) {
                                    dom += "姓名:[" + row.stdName + "]=>[" + row.newStdName + "]</br>";
                                }
                                if (null != row.newIdCard) {
                                    dom += "证件号:[" + row.idCard + "]=>[" + row.newIdCard + "]</br>";
                                }
                                if (null != row.newSex) {
                                    dom += "性别:[" + _findDict("sex", row.sex) + "]=>[" + _findDict("sex", row.newSex) + "]</br>";
                                }
                                if (null != row.newNation) {
                                    dom += "民族:[" + _findDict("nation", row.nation) + "]=>[" + _findDict("nation", row.newNation) + "]</br>";
                                }
                                if (null != row.nunvsName) {
                                    dom += "院校:[" + row.unvsName + "]=>[" + row.nunvsName + "]</br>";
                                }
                                if (null != row.newPfsnLevel && row.pfsnLevel!=row.newPfsnLevel) {
                                    dom += "层次:[" + _findDict("pfsnLevel",row.pfsnLevel) + "]=>[" + _findDict("pfsnLevel",row.newPfsnLevel) + "]</br>";
                                }
                                if (null != row.newPfsnId) {
                                    dom += "专业:[" + row.pfsnName + "]=>[" + row.npfsnName + "]</br>";
                                }
                                if (null != row.ntaName) {
                                    dom += "考试县区:[" + row.taName + "]=>[" + row.ntaName + "]</br>";
                                }
                                return dom;
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                                if (!dateTime) {
                                    return '-'
                                }
                                var date = dateTime.substring(0, 10)
                                var time = dateTime.substring(11)
                                return date + '<br>' + time
                            },
                            "targets": 9, "class": "text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '<label class="label label-danger radius">已驳回</label>';
                                if ("1" == row.checkOrder && "1" == row.checkStatus) {
                                    dom = '<label class="label label-secondary radius">待审核</label>';
                                }
                                if ("2" == row.checkOrder && "1" == row.checkStatus) {
                                    dom = '<label class="label label-secondary radius">待受理</label>';
                                }
                                if ("3" == row.checkOrder && "1" == row.checkStatus) {
                                    dom = '<label class="label label-secondary radius">待执行</label>';
                                }
                                if ("3" == row.checkOrder && "2" == row.checkStatus) {
                                    dom = '<label class="label label-success radius">已执行</label>';
                                }
                                return dom;
                            },
                            "targets": 10
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom = '<a title="审核" href="javascript:;" onclick="member_check(\'' + row.modifyId + '\',\'ACCPET\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-shenhe"></i></a>';
                                return dom;
                            },
                            //指定是第三列
                            "targets": 11
                        }]
				});
		
		excuteTable = $('#excuteTable').dataTable(
				{
                    "processing": true,
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {
						url : "/studentRoll/findStudentAuditRollModifyNew.do",
						type : "post",
                        data : {
                            "stdName": function () {
                                return $("#stdName2").val();
                            }, "mobile": function () {
                                return $("#mobile2").val();
                            }, "idCard": function () {
                                return $("#idCard2").val();
                            }, "unvsId": function () {
                                return $("#unvsId2").val();
                            }, "pfsnId": function () {
                                return $("#pfsnId2").val();
                            }, "pfsnLevel": function () {
                                return $("#pfsnLevel2").val();
                            }, "grade": function () {
                                return $("#grade2").val();
                            }, "recruitType": function () {
                                return $("#recruitType2").val();
                            }, "stdStage": function () {
                                return $("#stdStage2").val();
                            }, "createStartTime": function () {
                                return $("#createStartTime2").val();
                            }, "createEndTime": function () {
                                return $("#createEndTime2").val();
                            }, "checkStartTime": function () {
                                return $("#checkStartTime2").val();
                            }, "checkEndTime": function () {
                                return $("#checkEndTime2").val();
                            }, "createUser": function () {
                                return $("#createUser2").val();
                            }, "changeType": function () {
                                return $("#changeType2").val();
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
                    columns: [ {
                        "mData": null
                    }, {
                        "mData": "stdName"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "createUser"
                    },  {
                        "mData": null
                    }, {
                        "mData": null
                    },{
                        "mData": null
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.modifyId + '" name="excute_modifyIds" class="sendIdsCheckBox"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("grade", row.grade);
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom += row.unvsName+":";
                                dom += "(" + row.pfsnCode + ")" +row.pfsnName ;
                                _findDict("pfsnLevel", row.pfsnLevel).indexOf("高中") != -1 ? dom += "[5>高升专]" : dom += "[1>专升本]";
                                return dom;
                            },
                            "targets": 3,
                            "class": "text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("stdStage", row.stdStage);
                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("scholarship", row.scholarship);
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type, row, meta) {
                                return _findDict("inclusionStatus", row.inclusionStatus);
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = "";
                                if (null != row.newStdName) {
                                    dom += "姓名:[" + row.stdName + "]=>[" + row.newStdName + "]</br>";
                                }
                                if (null != row.newIdCard) {
                                    dom += "证件号:[" + row.idCard + "]=>[" + row.newIdCard + "]</br>";
                                }
                                if (null != row.newSex) {
                                    dom += "性别:[" + _findDict("sex", row.sex) + "]=>[" + _findDict("sex", row.newSex) + "]</br>";
                                }
                                if (null != row.newNation) {
                                    dom += "民族:[" + _findDict("nation", row.nation) + "]=>[" + _findDict("nation", row.newNation) + "]</br>";
                                }
                                if (null != row.nunvsName) {
                                    dom += "院校:[" + row.unvsName + "]=>[" + row.nunvsName + "]</br>";
                                }
                                if (null != row.newPfsnLevel && row.pfsnLevel!=row.newPfsnLevel) {
                                    dom += "层次:[" + _findDict("pfsnLevel",row.pfsnLevel) + "]=>[" + _findDict("pfsnLevel",row.newPfsnLevel) + "]</br>";
                                }
                                if (null != row.newPfsnId) {
                                    dom += "专业:[" + row.pfsnName + "]=>[" + row.npfsnName + "]</br>";
                                }
                                if (null != row.ntaName) {
                                    dom += "考试县区:[" + row.taName + "]=>[" + row.ntaName + "]</br>";
                                }
                                return dom;
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                                if (!dateTime) {
                                    return '-'
                                }
                                var date = dateTime.substring(0, 10)
                                var time = dateTime.substring(11)
                                return date + '<br>' + time
                            },
                            "targets": 9, "class": "text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '<label class="label label-danger radius">已驳回</label>';
                                if ("1" == row.checkOrder && "1" == row.checkStatus) {
                                    dom = '<label class="label label-secondary radius">待审核</label>';
                                }
                                if ("2" == row.checkOrder && "1" == row.checkStatus) {
                                    dom = '<label class="label label-secondary radius">待受理</label>';
                                }
                                if ("3" == row.checkOrder && "1" == row.checkStatus) {
                                    dom = '<label class="label label-secondary radius">待执行</label>';
                                }
                                if ("3" == row.checkOrder && "2" == row.checkStatus) {
                                    dom = '<label class="label label-success radius">已执行</label>';
                                }
                                return dom;
                            },
                            "targets": 10
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom = '<a title="审核" href="javascript:;" onclick="member_check(\'' + row.modifyId + '\',\'EXECUTE\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-shenhe"></i></a>';
                                return dom;
                            },
                            //指定是第三列
                            "targets": 11
                        }]
				});
            $('.checkAll').on('click',function () {
                if($(this).prop("checked")){
                    $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').each(function (i,e) {
                        if(!$(e).prop("checked")){
                            $(e).prop('checked',true)
                        }
                    })
                }else {
                    $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').prop('checked',false)
                }
            })
	});

/*用户-审核*/
function member_check(modifyId,exType) {
	var url = '/studentRoll/audit.do' + '?modifyId=' + modifyId + '&exType=' + exType;
	layer_show('学籍信息异动审核', url, null, 700, function() {

		if(exType=="ACCPET"){
            acceptTable.fnDraw(true);
        }else {
            excuteTable.fnDraw(true);
        }
	});
}

function delAll() {
	var chk_value = [];
	$("input[name=modifyIds]:checked").each(function() {
		chk_value.push($(this).val());
	});

	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : '/studentModify/deleteStudentModify.do',
			data : {
				idArray : chk_value
			},
			dataType : 'json',
			success : function(data) {
				if (data.code == _GLOBAL_SUCCESS) {
					layer.msg('已删除!', {
						icon : 1,
						time : 1000
					});
					excuteTable.fnDraw(false);
					acceptTable.fnDraw(false);
					$("input[name=all]").attr("checked", false);
				}
			}
		});
	});
}


    var checksFlag=false;
    function acceptbatchPass() {

        var chk_value = [];
        $("input[name=accept_modifyIds]:checked").each(function() {
            chk_value.push($(this).val());
        });
        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认要受理吗？', function(index) {
            if(checksFlag){
                layer.msg('请勿重复提交!', {
                    icon : 0,
                    time : 1000
                });
                return
            }
            checksFlag=true;
            $.ajax({
                type : 'POST',
                url : '/studentRoll/batchPassApprovalNew.do',
                data : {
                    idArray : chk_value,
                    checkStatus:2,
                    checkOrder:2
                },
                dataType : 'json',

                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已审核成功!', {
                            icon : 1,
                            time : 1000
                        });
                        acceptTable.fnDraw(false);
                        excuteTable.fnDraw(true);
                        checksFlag=false;
                        $("input[name=all]").attr("checked", false);
                    }
                }
            });
        });
    }

        var passFlag=false;
        function excutebatchPass() {

            var chk_value = [];
            $("input[name=excute_modifyIds]:checked").each(function() {
                chk_value.push($(this).val());
            });
            if (chk_value == null || chk_value.length <= 0) {
                layer.msg('未选择任何数据!', {
                    icon: 5,
                    time: 1000
                });
                return;
            }

            layer.confirm('确认要执行吗？', function(index) {
                if(passFlag){
                    layer.msg('请勿重复提交!', {
                        icon : 0,
                        time : 1000
                    });
                    return
                }
                passFlag=true;
                $.ajax({
                    type : 'POST',
                    url : '/studentRoll/batchPassApprovalNew.do',
                    data : {
                        idArray : chk_value,
                        checkStatus:2,
                        checkOrder:3
                    },
                    dataType : 'json',

                    success : function(data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('已审核成功!', {
                                icon : 1,
                                time : 1000
                            });
                            excuteTable.fnDraw(false);
                            passFlag=false;
                            $("input[name=all]").attr("checked", false);
                        }
                    }
                });
            });
        }

function excuteSearch() {
	excuteTable.fnDraw(true);
}
function acceptSearch() {
	acceptTable.fnDraw(true);
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
		
function init_pfsn_select_2() {
	_simple_ajax_select({
		selectId : "pfsnId2",
		searchUrl : '/baseinfo/sPfsn.do',
		sData : {
			sId :  function(){
				return $("#unvsId2").val() ? $("#unvsId2").val() : '';	
			},
			ext1 : function(){
				return $("#pfsnLevel2").val() ? $("#pfsnLevel2").val() : '';
			},
			ext2 : function(){
				return $("#grade2").val() ? $("#grade2").val() : '';
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
	$("#pfsnId2").append(new Option("", "", false, true));
}