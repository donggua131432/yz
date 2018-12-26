var myDataTable;
            var myDataTableSend;
            $(function() {
                //标签块table-sortfinancial
                $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
                //初始化年级下拉框
                _init_select("grade", dictJson.grade);
                //初始地址类型
                _init_select("saType", dictJson.saType);
                //初始化年级下拉框
                _init_select("grades", dictJson.grade);
                //初始地址类型
                _init_select("saTypes", dictJson.saType);
                
                _init_select("recruitType",dictJson.recruitType);
                $("#recruitType").change(function() {
    				_init_select({
    					selectId : 'grade',
    					ext1 : $(this).val()
    				}, dictJson.grade);
    			});
                //初始审核状态
                _init_select("addressStatus", [ {
                    "dictValue" : "1",
                    "dictName" : "待审核"
                }, {
                    "dictValue" : "4",
                    "dictName" : "审核通过"
                }, {
                    "dictValue" : "3",
                    "dictName" : "班主任驳回"
                }, {
                    "dictValue" : "6",
                    "dictName" : "教务驳回"
                }  ]);
                //初始收件状态
                _init_select("receiveStatus", [ {
                    "dictValue" : "1",
                    "dictName" : "否"
                }, {
                    "dictValue" : "2",
                    "dictName" : "是"
                } ]);
                
                _init_select("finishStatus", [ {
                    "dictValue" : "0",
                    "dictName" : "否"
                }, {
                    "dictValue" : "1",
                    "dictName" : "是"
                } ]);
                _init_select("ifSendBook",[ {
                    "dictValue" : "0",
                    "dictName" : "否"
                }, {
                    "dictValue" : "1",
                    "dictName" : "是"
                } ]);
                
                //初始审核状态
                _init_select("addressStatuss", [ {
                    "dictValue" : "1",
                    "dictName" : "待审核"
                }, {
                    "dictValue" : "4",
                    "dictName" : "审核通过"
                }, {
                    "dictValue" : "3",
                    "dictName" : "班主任驳回"
                }, {
                    "dictValue" : "6",
                    "dictName" : "教务驳回"
                }  ]);
                _init_select("finishStatuss", [ {
                    "dictValue" : "0",
                    "dictName" : "否"
                }, {
                    "dictValue" : "1",
                    "dictName" : "是"
                } ]);
                //初始化学期
                _init_select("semester", dictJson.semester);
                _init_select("semesterD", dictJson.semester);
                //初始化专业层次下拉框
                _init_select("pfsnLevel", dictJson.pfsnLevel);
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
    			 
    			  _init_select("recruitTypes",dictJson.recruitType);
                  $("#recruitTypes").change(function() {
      				_init_select({
      					selectId : 'grades',
      					ext1 : $(this).val()
      				}, dictJson.grade);
      			 });
    			 _init_select("pfsnLevels", dictJson.pfsnLevel);
                 //初始化院校名称下拉框
                 _simple_ajax_select({
                     selectId: "unvsIds",
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
                 $("#unvsIds").append(new Option("", "", false, true));
                 
                 $("#unvsIds").change(function () {
     	            $("#pfsnIds").removeAttr("disabled");
     	            init_pfsn_select_s();
 	    	     });
                 $("#grades").change(function () {
     	            $("#pfsnIds").removeAttr("disabled");
     	            init_pfsn_select_s();
 	    	     });
 	    		 $("#pfsnLevels").change(function () {
     	            $("#pfsnIds").removeAttr("disabled");
     	            init_pfsn_select_s();
 	    	     });
     			 $("#pfsnIds").append(new Option("", "", false, true));
     			 $("#pfsnIds").select2({
     		            placeholder: "--请先选择院校--"
     		     });
   			  	_simple_ajax_select({
   					selectId : "taskId",
   					searchUrl : '/task/findAddressAffirmTaskInfo.do',
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
   				
                myDataTableSend = $('.table-sort-send').dataTable(
                        {
                            "processing": true,
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/studentSend/findAllOkSend.do",
                                type : "post",
                                data : {
                                    "stdName" : function() {
                                        return $("#stdNames").val();
                                    },
                                    "mobile" : function() {
                                        return $("#mobiles").val();
                                    },
                                    "idCard" : function() {
                                        return $("#idCards").val();
                                    },
                                    "grade" : function() {
                                        return $("#grades").val();
                                    },
                                    "ifSendBook" : function() {
                                        return $("#ifSendBook").val();
                                    },
                                    "unvsId" : function() {
                                        return $("#unvsIds").val();
                                    },
                                    "pfsnId" : function() {
                                        return $("#pfsnIds").val();
                                    },
                                    "pfsnLevel" : function() {
                                        return $("#pfsnLevels").val();
                                    },
                                    "tutor" : function() {
                                        return $("#tutors").val();
                                    },
                                    "recruitType" : function() {
                                        return $("#recruitTypes").val();
                                    },
                                    "receiveStatus" : function() {
                                        return $("#receiveStatus").val();
                                    },
                                    "semester" : function() {
                                        return $("#semester").val();
                                    },
                                    "finishStatus" : function (){
                                    	return $("#finishStatuss").val();
                                    },
                                    "logisticsNo" : function (){
                                    	return $("#logisticsNo").val();
                                    }
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
                            columns : [ {
                                "mData" : null
                            }, {
                                "mData" : "stdNo"
                            }, {
                                "mData" : "stdName"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : "logisticsName"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : "sendPeople"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            } ],
                            "columnDefs" : [
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';
                                            if ("3" != row.addressStatus) {
                                                dom = '<input type="checkbox" value="'+ row.sendId + '" class="sendIdsCheckBox" name="sendIdss"/>';
                                            }
                                            return dom;
                                        },
                                        "targets" : 0
                                    },{
                                    "render" : function(data, type, row, meta) {
                                        var dom = '';
                                        if(row.logisticsNo != null && row.logisticsNo != ''){
                                            dom += '<a href="http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/'+row.logisticsNo+'" class="tableBtn normal" target="_blank" title="查看物流信息">' + row.logisticsNo + '</a>';
                                        }
                                        return dom;
                                    },
                                    "targets" : 3
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                        	return _findDict("logisticsName", row.logisticsName);						
                                        },
                                        "targets" : 4
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findProvinceName(row.provinceCode) + _findCityName(row.provinceCode, row.cityCode)
                                                    + _findDistrictName(row.provinceCode, row.cityCode, row.districtCode) + row.address;
                                        },
                                        "targets" : 5,
                                        "class":"text-l"
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("saType", row.saType);
                                        },
                                        "targets" : 6
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                          	if(row.sendDate) {
                                          	    var dateTime=new Date(row.sendDate).format("yyyy-MM-dd hh:mm:ss");
                                                var date=dateTime.substring(0,10)
                                                var time=dateTime.substring(11)
                                            	return date+"<br>"+time;
                                          	} else {
                                          	  return '无';
                                          	}
                                        },
                                        "targets" : 7,"class":"text-l no-warp"
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';
                                            if ("1" == row.receiveStatus) {
                                                dom = '<label class="label label-danger radius">否</label>';
                                            }
                                            if ("2" == row.receiveStatus) {
                                                dom = '<label class="label label-success radius">是</label>';
                                            }
                                            return dom;
                                        },
                                        "targets" : 9
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';
                                            if ("1" == row.receiveStatus) {
                                                dom = '<a title="手动确认收件" href="javascript:;" onclick="okReceive(\'' + row.sendId
                                                        + '\')" class="tableBtn normal" style="text-decoration: none">手动确认收件</a>';
                                            }
                                            return dom;
                                        },
                                        //指定是第三列
                                        "targets" : 10
                                    } ]
                        });

                myDataTable = $('.table-sort').dataTable({
                    "processing": true,
                    "serverSide" : true,
                    "dom" : 'rtilp',
                    "ajax" : {
                        url : "/studentSend/findAllStudentSendSevi.do",
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
                            "grade" : function() {
                                return $("#grade").val();
                            },
                            "idCard" : function() {
                                return $("#idCard").val();
                            },
                            "unvsId" : function() {
                                return $("#unvsId").val();
                            },
                            "pfsnId" : function() {
                                return $("#pfsnId").val();
                            },
                            "pfsnLevel" : function() {
                                return $("#pfsnLevel").val();
                            },
                            "addressStatus" : function() {
                                return $("#addressStatus").val();
                            },
                            "tutor" : function() {
                                return $("#tutor").val();
                            },
                            "saType" : function() {
                                return $("#saType").val();
                            },
                            "recruitType" : function (){
                            	return $("#recruitType").val();
                            },
                            "finishTimeStart" : function(){
                            	return $("#finishTimeStart").val();
                            },
                            "finishTimeEnd" : function (){
                            	return $("#finishTimeEnd").val();
                            },
                            "finishStatus" : function (){
                            	return $("#finishStatus").val();
                            },
                            "taskId" : function (){
                            	return $("#taskId").val();
                            },
                            "semester" : function (){
                            	return $("#semesterD").val();
                            }
                            
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
                        "mData" : "userName"
                    }, {
                        "mData" : "mobile"
                    }, {
                        "mData" : null
                    }, {
                        "mData" : "address"
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : "addRemark"
                    }, {
                        "mData" : null
                    }  ],
                    "columnDefs" : [ {
                        "render" : function(data, type, row, meta) {
                            var dom = '';
                            if ("4" != row.addressStatus && "6" != row.addressStatus) {
                                dom = '<input type="checkbox" value="'+ row.sendId + '" name="sendIds"/>';
                            }
                            return dom;
                        },
                        "targets" : 0
                    }, {
                        "render" : function(data, type, row, meta) {
                            return _findDict("grade", row.grade);
                        },
                        "targets" : 2
                    }, {
                        "render" : function(data, type, row, meta) {
                            return _findDict("semester", row.semester);
                        },
                        "targets" : 3
                    }, {
                        "render" : function(data, type, row, meta) {
                            return row.taskTitle;
                        },
                        "targets" : 4
                    }, {
                        "render" : function(data, type, row, meta) {
                        	var provinceName = "";
                        	var cityName = "";
                        	var districtName = "";
                        	
                        	if(null != row.provinceName && '' != row.provinceName){
                        		provinceName = row.provinceName;
                        	}
                        	if(null != row.cityName && '' != row.cityName){
                        		cityName = row.cityName;
                        	}
                        	if(null != row.districtName && '' != row.districtName){
                        		districtName = row.districtName;
                        	}
                        	
                            dom = '';
                            if (row.provinceName != null) {
                                dom += '' + provinceName + cityName + districtName;
                            }
                            return dom;
                        },
                        "targets" : 7
                    },{
                        "render" : function(data, type, row, meta) {
                            if(row.addressUpdateTime) {
                                var dateTime=new Date(row.addressUpdateTime).format("yyyy-MM-dd hh:mm:ss");
                                var date=dateTime.substring(0,10)
                                var time=dateTime.substring(11)
                                return date+"<br>"+time;
                            } else {
                                return '未更新';
                            }
                        },
                        "targets" : 9,"class":"text-l no-warp"
                    }, {
                        "render" : function(data, type, row, meta) {
                            if(row.createTime) {
                                var dateTime=new Date(row.createTime).format("yyyy-MM-dd hh:mm:ss");
                                var date=dateTime.substring(0,10)
                                var time=dateTime.substring(11)
                                return date+"<br>"+time;
                            } else {
                                return '无';
                            }
                        },
                        "targets" :10,"class":"text-l no-warp"
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dom = '';
                            if ("1" == row.addressStatus) {
                                dom = '<label class="label label-secondary radius">待审核</label>';
                            }
                            if ("4" == row.addressStatus) {
                                dom = '<label class="label label-success radius">审核通过</label>';
                            }
                            if ("3" == row.addressStatus) {
                                var rejectUser,rejectTime,rejectReason;
                                rejectUser=row.rejectUser?row.rejectUser:'无';
                                rejectTime=row.rejectTime?new Date(row.rejectTime).format('yyyy-MM-dd'):'无';
                                rejectReason=row.rejectReason?row.rejectReason:'无';
                                var rejecTxt="驳回人："+rejectUser+"&#10;"+"驳回时间："+rejectTime+"&#10;"+"驳回原因："+rejectReason;
                                dom = '<label class="label label-danger radius" style="cursor: pointer" title="'+rejecTxt+'">班主任驳回</label>';
                            }
                            if ("6" == row.addressStatus) {
                            	 var rejectUser,rejectTime,rejectReason;
                                 rejectUser=row.rejectUser?row.rejectUser:'无';
                                 rejectTime=row.rejectTime?new Date(row.rejectTime).format('yyyy-MM-dd'):'无';
                                 rejectReason=row.rejectReason?row.rejectReason:'无';
                                 var rejecTxt="驳回人："+rejectUser+"&#10;"+"驳回时间："+rejectTime+"&#10;"+"驳回原因："+rejectReason;
                                 dom = '<label class="label label-warning radius" style="cursor: pointer" title="'+rejecTxt+'">教务驳回</label>';
                            }
                            return dom;
                        },
                        "targets" : 11
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dom = '';

                            if ("1" == row.addressStatus) {
                                dom = '<a title="通过" href="javascript:;" onclick="pass(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-wancheng"></i></a>';
                            }
                            if ("3" != row.addressStatus && "6" != row.addressStatus && "4" != row.addressStatus) {
                                dom += '<a title="驳回" href="javascript:;" onclick="rejected(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-weiwancheng"></i></a>';
                            }
                            if ("3" == row.addressStatus || "6" == row.addressStatus) {
                                dom += '<a title="修改地址" href="javascript:;" onclick="updateAddress(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="Hui-iconfont Hui-iconfont-edit2"></i></a>';
                            }
                            dom += '<a title="添加备注" href="javascript:;" onclick="addRemark(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            return dom;
                        },
                        //指定是第三列
                        "targets" : 13
                    } ]
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
                });

            });
            /*确认收货*/
            function okReceive(sendId) {
                var url = '/studentSend/okSendServ.do';
                var chk_value = [];
                chk_value.push(sendId);
                layer.confirm('确定要确认？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已确认!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTableSend.fnDraw(true);
                            }
                        }
                    });
                });
            }

            /*批量确认收货*/
            function okAll() {
                var url = '/studentSend/okSendServ.do';
                var chk_value = [];
                $("input[name=sendIdss]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                if (chk_value == null || chk_value.length <= 0) {
                    layer.msg('未选择任何数据!', {
                        icon : 5,
                        time : 1000
                    });
                    return;
                }
                layer.confirm('确定要确认？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已确认!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTableSend.fnDraw(true);
                            }
                        }
                    });
                });
            }

            /*通过审核*/
            function pass(sendId) {
                var url = '/studentSend/pass.do';
                var chk_value = [];
                chk_value.push(sendId);
                layer.confirm('确认要通过？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已通过审核!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });
            }

            function rejectedAll() {
                var url = '/studentSend/rejected.do';
                var chk_value = [];
                $("input[name=sendIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                if (chk_value == null || chk_value.length <= 0) {
                    layer.msg('未选择任何数据!', {
                        icon : 5,
                        time : 1000
                    });
                    return;
                }
               //驳回原因下拉框
                var data = [{ id: 0, text: '地址不完整' }, { id: 1, text: '地址含特殊符号' }, { id: 2, text: '非最新地址' }, { id: 3, text: '其他' }];
                $("#rejectSelect").select2({
                    data: data,
                    placeholder:'请选择驳回理由',
                    allowClear:true,
                    width : "250px"
                });
                $("#rejectSelect").val(null).trigger("change");
                $("#otherReject").prop('disabled',true).val('');
                $("#rejectSelect").change(function () {
                    $("#otherReject").prop('disabled',true).val('');
                    $("#rejectReason").val($("#rejectSelect").find("option:selected").text());
                    var res=$(this).val();
                    if(res==3){
                        $(".other-reason").show();
                        $("#otherReject").prop('disabled',false)
                    }else {
                        $(".other-reason").hide();
                    }
                });

                layer.open({
                    type:1,
                    btn: ['确定'],
                    yes: function(index, layero){
						//确定提交驳回信息回调
                        var otherReject=$("#otherReject").val()
                        var res=$("#rejectSelect").val();
                        if(!res){
                            layer.msg('请选择驳回原因!', {
                                icon : 0,
                                time : 1000
                            });
                            return;
                        }
                      
                        if(res==3&&otherReject==''){
                            layer.msg('请输入其他驳回原因!', {
                                icon : 0,
                                time : 1000
                            });
                            return;
                        } 
                        if(res == 3){
                            if(otherReject.length>100){
                                layer.msg('最大支持100个字符!', {
                                    icon : 2,
                                    time : 2000
                                });
                                return;
                            }
                        	$("#rejectReason").val(otherReject);
                        }
                       
	                      $.ajax({
	                     type : 'POST',
	                      url : url,
	                      data : {
	                          idArray : chk_value,
	                          type:res,
	                          reason:$("#rejectReason").val()
	                      },
	                      dataType : 'json',
	                      success : function(data) {
	                          if (data.code == _GLOBAL_SUCCESS) {
	                             layer.msg('已驳回!', {
	                                  icon : 1,
	                                  time : 1000
	                              });
	                              myDataTable.fnDraw(false);
	                          }
	                      }
	                  });
	                      layer.close(index)
                    },
                    area:['500px','300px'],
                    content:$("#rejectContent")
                });
            }
            function passAll() {
                var url = '/studentSend/pass.do';
                var chk_value = [];
                $("input[name=sendIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });
                if (chk_value == null || chk_value.length <= 0) {
                    layer.msg('未选择任何数据!', {
                        icon : 5,
                        time : 1000
                    });
                    return;
                }
                layer.confirm('确认要通过？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已通过审核!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTable.fnDraw(true);
                            }
                        }
                    });
                });
            }

            /*驳回*/
            function rejected(sendId) {
                var url = '/studentSend/rejected.do';
                var chk_value = [];
                chk_value.push(sendId);
                $(".other-reason").hide();
                //驳回原因下拉框
                var data = [{ id: 0, text: '地址不完整' }, { id: 1, text: '地址含特殊符号' }, { id: 2, text: '非最新地址' }, { id: 3, text: '其他' }];
                $("#rejectSelect").select2({
                    data: data,
                    placeholder:'请选择驳回理由',
                    allowClear:true,
                    width : "250px"
                });
                $("#rejectSelect").val(null).trigger("change");
                $("#otherReject").prop('disabled',true).val('');
                $("#rejectSelect").change(function () {
                    $("#otherReject").prop('disabled',true).val('');
                    $("#rejectReason").val($("#rejectSelect").find("option:selected").text());
                    var res=$(this).val();
                    if(res==3){
                        $(".other-reason").show()
                        $("#otherReject").prop('disabled',false)
                    }else {
                        $(".other-reason").hide()
                    }
                });

                layer.open({
                    type:1,
                    btn: ['确定'],
                    yes: function(index, layero){
						//确定提交驳回信息回调
                        var otherReject=$("#otherReject").val()
                        var res=$("#rejectSelect").val();
                        if(!res){
                            layer.msg('请选择驳回原因!', {
                                icon : 0,
                                time : 1000
                            });
                            return;
                        }
                      
                        if(res==3&&otherReject==''){
                            layer.msg('请输入其他驳回原因!', {
                                icon : 0,
                                time : 1000
                            });
                            return;
                        } 
                        if(res == 3){
                        	if(otherReject.length>100){
                       		 layer.msg('最大支持100个字符!', {
                                    icon : 2,
                                    time : 2000
                                });
                                return;
                       		}
                        	$("#rejectReason").val(otherReject);
                        }
                       
	                      $.ajax({
	                     type : 'POST',
	                      url : url,
	                      data : {
	                          idArray : chk_value,
	                          type:res,
	                          reason:$("#rejectReason").val()
	                      },
	                      dataType : 'json',
	                      success : function(data) {
	                          if (data.code == _GLOBAL_SUCCESS) {
	                             layer.msg('已驳回!', {
	                                  icon : 1,
	                                  time : 1000
	                              });
	                              myDataTable.fnDraw(false);
	                          }
	                      }
	                  });
                        layer.close(index)
                    },
                    area:['500px','300px'],
                    content:$("#rejectContent")
                });

//                layer.confirm('确认要驳回？', function(index) {
//                    $.ajax({
//                        type : 'POST',
//                        url : url,
//                        data : {
//                            idArray : chk_value
//                        },
//                        dataType : 'json',
//                        success : function(data) {
//                            if (data.code == _GLOBAL_SUCCESS) {
//                                layer.msg('已驳回!', {
//                                    icon : 1,
//                                    time : 1000
//                                });
//                                myDataTable.fnDraw(true);
//                            }
//                        }
//                    });
//                });
            }
            
            function submitReject() {

            }
            
            function importSendServ(){
            	var url = '/studentSend/toServImport.do';
				layer_show('课程导入', url, null, 510, function() {
					myDataTable.fnDraw(true);
				});
            }

            function _search() {
                myDataTable.fnDraw(true);
            }
            function _searchs() {
                myDataTableSend.fnDraw(true);
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
            
            function init_pfsn_select_s() {
		    	_simple_ajax_select({
					selectId : "pfsnIds",
					searchUrl : '/baseinfo/sPfsn.do',
					sData : {
						sId :  function(){
							return $("#unvsIds").val() ? $("#unvsIds").val() : '';	
						},
						ext1 : function(){
							return $("#pfsnLevels").val() ? $("#pfsnLevels").val() : '';
						},
						ext2 : function(){
							return $("#grades").val() ? $("#grades").val() : '';
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
				$("#pfsnIds").append(new Option("", "", false, true));
		    }
            
            /*添加备注*/
            function addRemark(sendId) {
            	$("#addRemark").val('');
                var url = '/studentSend/addRemark.do';
                layer.open({
                    type:1,
                    btn: ['确定'],
                    yes: function(index, layero){
                        var addRemark=$("#addRemark").val();
                        if(addRemark.length>20){
                      		 layer.msg('最大支持20个字符!', {
                                   icon : 2,
                                   time : 2000
                               });
                               return;
                      		}
	                   
							$.ajax({
								type : 'POST',
								url : url,
								data : {
									sendId : sendId,
									addRemark : addRemark
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										layer.msg('添加成功!', {
											icon : 1,
											time : 1000
										});
										myDataTable.fnDraw(false);
									}
								}
							});
						layer.close(index)
					},
					area : [ '500px', '300px' ],
					content : $("#addRemarkContent")
				});
			}
            /*修改地址*/
            function updateAddress(sendId) {
            	$("#detailAddress").val('');
            	$("#takeUserName").val('');
            	$("#takeMobile").val('');
            	/*初始化省市区*/

            	_init_area_select("provinceCode", "cityCode", "districtCode",'','','','1');
                $("#districtCode").select2("val", " ");
                var url = '/studentSend/updateAddress.do';
                layer.open({
                    type:1,
                    btn: ['确定'],
                    yes: function(index, layero){
	                        var detailAddress=$("#detailAddress").val();
	                        if(detailAddress.length>50){
	                      		 layer.msg('最大支持50个字符!', {
	                                   icon : 2,
	                                   time : 2000
	                               });
	                               return;
	                        }
	                        
							$.ajax({
								type : 'POST',
								url : url,
								data : {
									sendId : sendId,
									takeUserName : $("#takeUserName").val(),
									takeMobile : $("#takeMobile").val(),
									provinceCode : $("#provinceCode").val(),
									cityCode : $("#cityCode").val(),
									districtCode : $("#districtCode").val(),
									detailAddress : detailAddress
								},
								dataType : 'json',
								success : function(data) {
									if (data.code == _GLOBAL_SUCCESS) {
										layer.msg('添加成功!', {
											icon : 1,
											time : 1000
										});
										myDataTable.fnDraw(false);
									}
								}
							});
						layer.close(index)
					},
					area : [ '800px', '400px' ],
					content : $("#addressContent")
				});
			}