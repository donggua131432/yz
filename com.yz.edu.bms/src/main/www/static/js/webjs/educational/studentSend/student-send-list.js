var myDataTable;
            var myDataTableOrderBook;

//            点击学员姓名展示的异动信息数 【新生信息修改、常规操作】
            var modifyShowArr=["1","5"];

            $(function() {
                //标签块table-sortfinancial
                $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
                //初始化年级下拉框
                _init_select("grade", dictJson.grade);
                _init_select("gradeo", dictJson.grade);
                //初始地址类型
                _init_select("saType", dictJson.saType);
                //初始化年级下拉框
                _init_select("grades", dictJson.grade);
                //初始地址类型
                _init_select("saTypes", dictJson.saType);
                //初始审核状态
                 _init_select("year", dictJson.year);
                 _init_select("yearo", dictJson.year);
                 //是否外校
                 _init_select("stdType", dictJson.stdType);
               	 //快递公司
                 _init_select("logisticsName", dictJson.logisticsName);
                 
                _init_select("addressStatuso", [ {
                    "dictValue" : "4",
                    "dictName" : "待审核"
                }, {
                    "dictValue" : "5",
                    "dictName" : "审核通过"
                }, {
                    "dictValue" : "6",
                    "dictName" : "驳回"
                }  ]);
                
                //初始化专业层次下拉框
                _init_select("pfsnLevel", dictJson.pfsnLevel);
                
                //初始化课程类型
				_init_select("textbookType", dictJson.courseType);

                
                $("#addressStatus").select2({
                	placeholder : "--请选择--",
                	allowClear : true
                });
                
                //初始收件状态
                _init_select("receiveStatus", [ {
                    "dictValue" : "1",
                    "dictName" : "否"
                }, {
                    "dictValue" : "2",
                    "dictName" : "是"
                } ]);
                _init_select("receiveStatuso", [ {
                    "dictValue" : "1",
                    "dictName" : "否"
                }, {
                    "dictValue" : "2",
                    "dictName" : "是"
                } ]);
                _init_select("address", [ {
                    "dictValue" : "0",
                    "dictName" : "否"
                }, {
                    "dictValue" : "1",
                    "dictName" : "是"
                } ]);
                //初始化学期
                _init_select("semester", dictJson.semester);
                _init_select("semestero", dictJson.semester);
                _init_select("semesterd", dictJson.semester);
                //学员状态
                _init_select("stdStageo", dictJson.stdStage);
                //订书状态
                _init_select("orderBookStatuso", dictJson.receiveStatus);

                
                //初始化专业层次下拉框(地址审核)
                _init_select("pfsnLevelD", dictJson.pfsnLevel);
                
                //初始化课程类型(地址审核)
				_init_select("textbookTypeD", dictJson.courseType);
                
				//订书状态(地址审核)
                _init_select("orderBookStatusD", dictJson.receiveStatus);
                //是否有收获地址(地址审核)
                _init_select("addressD", [ {
                    "dictValue" : "0",
                    "dictName" : "否"
                }, {
                    "dictValue" : "1",
                    "dictName" : "是"
                } ]);
                
                _init_select("ifHaveBook", [ {
                    "dictValue" : "1",
                    "dictName" : "有教材"
                }, {
                    "dictValue" : "0",
                    "dictName" : "无教材"
                } ]);
                
                _init_select("ifNewRepeatBook", [ {
                    "dictValue" : "1",
                    "dictName" : "是"
                }, {
                    "dictValue" : "0",
                    "dictName" : "否"
                } ]);
                _init_select("ifNewRepeatBookD", [ {
                    "dictValue" : "1",
                    "dictName" : "是"
                }, {
                    "dictValue" : "0",
                    "dictName" : "否"
                } ]);
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
                $("#gradeo").change(function () {
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
                
                _simple_ajax_select({
                    selectId: "unvsIdo",
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
                $("#unvsIdo").append(new Option("", "", false, true));
                
                $("#unvsIdo").change(function () {
    	            $("#pfsnIdD").removeAttr("disabled");
    	            init_pfsn_select_d();
	    	     });
                $("#grade").change(function () {
    	            $("#pfsnIdD").removeAttr("disabled");
    	            init_pfsn_select_d();
	    	     });
	    		 $("#pfsnLevelD").change(function () {
    	            $("#pfsnIdD").removeAttr("disabled");
    	            init_pfsn_select_d();
	    	     });
    			 $("#pfsnIdD").append(new Option("", "", false, true));
    			 $("#pfsnIdD").select2({
    		            placeholder: "--请先选择院校--"
    		     });
                
                
                
                myDataTableOrderBook = $('.table-sort-order-book').dataTable({
                    "processing": true,
                    "serverSide" : true,
                    "dom" : 'rtilp',
                    "ajax" : {
                        url : "/studentSend/findAllStudentOrderBook.do",
                        type: "post",
                        data : {
                            "stdName" : function() {
                                return $("#stdNameo").val();
                            },
                            "mobile" : function() {
                                return $("#mobileo").val();
                            },
                            "idCard" : function() {
                                return $("#idCardo").val();
                            },
                            "unvsId" : function() {
                                return $("#unvsId").val();
                            },
                            "pfsnId" : function() {
                                return $("#pfsnId").val();
                            },
                            "batchId" : function() {
                                return $("#batchIdo").val();
                            },
                            "grade" : function() {
                                return $("#gradeo").val();
                            },
                            "addressStatus" : function() {
                                return $("#addressStatuso").val();
                            },
                            "semester" : function() {
                                return $("#semestero").val();
                            },
                            "stdStage" : function() {
                                return $("#stdStageo").val();
                            },
                            "orderBookStatus" : function() {
                                return $("#orderBookStatuso").val();
                            },
                            "receiveStatus" : function() {
                                return $("#receiveStatuso").val();
                            },
                          	"pfsnLevel" : function (){
                          		return $("#pfsnLevel").val();
                          	},
                          	"textbookType" : function (){
                          		return $("#textbookType").val();
                          	},
                          	"logisticsNo" : function (){
                          		return $("#logisticsNo").val();
                          	},
                          	"address" : function (){
                          		return $("#address").val();
                          	},
                          	"year" : function(){
                          		return $("#yearo").val();
                          	},
                          	"tutorName": function (){
                          		return $("#empNameo").val();
                          	},
                          	"stdType" : function (){
                          		return $("#stdType").val();
                          	},
                          	"ifHaveBook" : function (){
                          		return $("#ifHaveBook").val();
                          	},
                          	"logisticsName" : function (){
                          		return $("#logisticsName").val();
                          	},
                          	"ifNewRepeatBook" : function(){
                          		return $("#ifNewRepeatBook").val();
                          	},
                          	"recruitName" : function (){
                          		return $("#recruitNameo").val();
                          	},
                          	"startTime" : function (){
                          		return $("#startTime").val();
                          	},
                          	"endTime" : function (){
                          		return $("#endTime").val();
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
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    },{
                        "mData" : null
                    },{
                        "mData" : null
                    }  ],
                    "columnDefs" : [ {
                        "render" : function(data, type, row, meta) {
                            var dom = '';
                            if ("3" != row.addressStatus) {
                                dom = '<input type="checkbox" value="'+ row.sendId + '" name="sendIdss"/>';
                            }else {
                                dom = '<input type="checkbox" style="visibility:hidden " value="'+ row.sendId + '" name="sendIdss"/>';
                            }
                            return dom;
                        },
                        "targets" : 0
                    },{
                        "render" : function(data, type, row, meta) {
                            var dom ='';
                            var title='';

                            var grade=_findDict("grade", row.grade);
                            var pfsnName = row.pfsnName;
                            var unvsName = row.unvsName;
                            var pfsnCode = row.pfsnCode;
                            var pfsnLevel = row.pfsnLevel;
                            if(grade){
                                title += grade+'-';
                            }
                            if (unvsName) {
                                title += unvsName+'-';
                            }

                            if (pfsnLevel) {
                                title+=_findDict("pfsnLevel", row.pfsnLevel).indexOf('高中')!=-1?"[专科]":"[本科]";
                            }

                            if (pfsnName) {
                                title += pfsnName;
                            }

                            if (pfsnCode) {
                                title += "(" + pfsnCode + ")";
                            }

                        	dom = row.stdName;
                        	if(row.std_type ==='2'){
                        		dom += '<span class="name-mark mark-red">外</span>';
                        	}
                        	return '<a class="name-mark-box c-blue" onclick="toEidt(\''+ row.learnId +'\',\''+ row.std_id +'\')" title="'+title+'">'+ dom +'</a>';
                        },
                        "targets" : 1
                    },{
                        "render" : function(data, type, row, meta) {
                            return _findDict("grade", row.grade);
                        },
                        "targets" : 2
                    }, {
                        "render" : function(data, type, row, meta) {
                            return _findDict("stdStage", row.stdStage);
                        },
                        "targets" : 3
                    }, {
                        "render" : function(data, type, row, meta) {
                            return _findDict("semester", row.semester);
                        },
                        "targets" : 4
                    }, {
                        "render" : function(data, type, row, meta) {
                           /*  if (row.stdStage < 7 || row.stdStage == 12) {
                                return row.zempName;
                            } else {
                                return row.fempName;
                            } */
                        	return row.fempName;
                        },
                        "targets" : 5
                    }, {
                        "render" : function(data, type, row, meta) {
                            return row.textbookPfsncode;
                        },
                        "targets" : 6
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dom = "";
                            for (var i = 0; i < row.books.length; i++) {
                                var book = row.books[i];

                                dom += book.textbookId + " " + book.textbookName + "<br>";
                            }
                            return dom;
                        },
                        "targets" : 7,
                        "class":"text-l"
                    }, {
                        "render" : function(data, type, row, meta) {
                            return row.batchId;
                        },
                        "targets" : 8
                    },{
                        "render" : function(data, type, row, meta) {
                        	return _findDict("logisticsName", row.logisticsName);						
                        },
                        "targets" : 9
                    }, {
                        "render" : function(data, type, row, meta) {
                        	var dom = '';
							if(row.logisticsNo != null && row.logisticsNo != ''){
								
								if(!!row.logisticsName&&row.logisticsName.indexOf("jd")>=0){
									dom += '<a href="http://www.jdwl.com/order/search?waybillCodes='+row.logisticsNo+'" target="_blank" class="tableBtn normal" title="查看物流信息">' + row.logisticsNo + '</a>';
								}else{
									dom += '<a href="http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/'+row.logisticsNo+'" target="_blank" class="tableBtn normal" title="查看物流信息">' + row.logisticsNo + '</a>';
								}
							}
							return dom;
                        },
                        "targets" : 10
                    }, {
                        "render" : function(data, type, row, meta) {
                        	var txt='';
                            if(_findDict("orderBookStatus", row.orderBookStatus)=='已发'){
                                txt+='<span class="label label-success radius">已发</span>'
                            	var dateTime = row.sendDate;
								var date = dateTime.substring(0, 10)
								var time = dateTime.substring(11)
                                txt+='<br/>'+date + '<br>' + time;
                            }else if(_findDict("orderBookStatus", row.orderBookStatus)=='未定'){
                                txt+='<span class="label radius">未订</span>'
                            }else {
                                txt+='<span class="label label-secondary  radius">已订未发</span>'
                            }
                            return txt
                        },
                        "targets" : 11
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dom = '';

                            if ("4" == row.addressStatus) {
                                dom = '<label class="label label-secondary radius">待审核</label>';
                            }
                            if ("5" == row.addressStatus) {
                                dom = '<label class="label label-success radius">审核通过</label>';
                            }
                            if ("6" == row.addressStatus) {
                            	var rejectUser,rejectTime,rejectReason;
                                rejectUser=row.rejectUser?row.rejectUser:'无';
                                rejectTime=row.rejectTime?new Date(row.rejectTime).format('yyyy-MM-dd'):'无';
                                rejectReason=row.rejectReason?row.rejectReason:'无';
                                var rejecTxt="驳回人："+rejectUser+"&#10;"+"驳回时间："+rejectTime+"&#10;"+"驳回原因："+rejectReason;
                                dom = '<label class="label label-danger radius" style="cursor: pointer" title="'+rejecTxt+'">驳回</label>';
                            }
                            return dom;
                        },
                        "targets" : 12
                    }, {
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
                        "targets" : 13
                    }, {
                        "render" : function(data, type, row, meta) {
                            return row.remark;
                        },
                        "targets" : 14
                    } ]
                });

                $(".HuiTab span:last-child").on('click',function () {
                    if(!myDataTable){
                        myDataTable = $('.table-sort').dataTable({
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/studentSend/findAllStudentSendSeviEd.do",
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
                                        return $("#unvsIdo").val();
                                    },
                                    "pfsnId" : function() {
                                        return $("#pfsnIdD").val();
                                    },
                                    "addressStatus" : function() {
                                        return $("#addressStatus").val();
                                    },
                                    "saType" : function() {
                                        return $("#saType").val();
                                    },
                                    "pfsnLevel" : function (){
                                        return $("#pfsnLevelD").val();
                                    },
                                    "textbookType" : function (){
                                        return $("#textbookTypeD").val();
                                    },
                                    "address" : function (){
                                        return $("#addressD").val();
                                    },
                                    "batchId" : function() {
                                        return $("#batchIdD").val();
                                    },
                                    "semester" : function() {
                                        return $("#semesterd").val();
                                    },
                                    "orderBookStatus" : function() {
                                        return $("#orderBookStatusD").val();
                                    },
                                    "year" : function (){
                                        return $("#year").val();
                                    },
                                    "recruitName" : function (){
                                        return $("#recruitName").val();
                                    },
                                    "tutorName" : function (){
                                        return $("#tutorName").val();
                                    },
                                  	"ifNewRepeatBook" : function(){
                                  		return $("#ifNewRepeatBookD").val();
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
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : "unvsName"
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
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            } ],
                            "columnDefs" : [ {
                                "render" : function(data, type, row, meta) {
                                    var dom = '';
                                    if ("6" != row.addressStatus && "5" != row.addressStatus) {
                                        dom = '<input type="checkbox" value="'+ row.sendId + '" name="sendIds" class="sendIdsCheckBox"/>';
                                    }
                                    return dom;
                                },
                                "targets" : 0
                            },{
                                "render" : function(data, type, row, meta) {
                                    var dom ='';
                                    var title='';

                                    var grade=_findDict("grade", row.grade);
                                    var pfsnName = row.pfsnName;
                                    var unvsName = row.unvsName;
                                    var pfsnCode = row.pfsnCode;
                                    var pfsnLevel = row.pfsnLevel;
                                    if(grade){
                                        title += grade+'-';
                                    }
                                    if (unvsName) {
                                        title += unvsName+'-';
                                    }

                                    if (pfsnLevel) {
                                        title+=_findDict("pfsnLevel", row.pfsnLevel).indexOf('高中')!=-1?"[专科]":"[本科]";
                                    }

                                    if (pfsnName) {
                                        title += pfsnName;
                                    }

                                    if (pfsnCode) {
                                        title += "(" + pfsnCode + ")";
                                    }

                                    dom+='<a title="'+title+'" class="c-blue name-mark-box" onclick="toEidt(\''+ row.learn_id +'\',\''+ row.std_id +'\')">'+ row.stdName;
                                    if(row.std_type ==='2'){
                                        dom += '<span class="name-mark mark-red">外</span>';
                                    }
                                    dom+='</a>'
                                    return dom
                                },
                                "targets" : 1,"class":"no-warp"
                            }, {
                                "render" : function(data, type, row, meta) {
                                    return _findDict("grade", row.grade);
                                },
                                "targets" : 2
                            }, {
                                "render" : function(data, type, row, meta) {
                                    return _findDict("semester", row.semester);
                                },
                                "targets" : 5
                            }, {
                                "targets" : 3,
                                "class":"text-l"
                            },{
                                "render" : function(data, type, row, meta) {
                                    var dom='';
                                    dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf('高中')!=-1?"[专科]":"[本科]";
                                    dom+=row.pfsnName + '(' + row.pfsnCode + ')';
                                    return dom
                                },
                                "targets" : 4,
                                "class":"text-l"
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
                                "targets" : 8,
                                "class":"text-l"
                            }, {
                                "render" : function(data, type, row, meta) {
                                    return row.address;
                                },
                                "targets" : 9,
                                "class":"text-l"
                            }, {
                                "render" : function(data, type, row, meta) {
                                    var type = row.textbookType;
                                    var dom = '';
                                    dom += _findDict("textbookType",type) + '地址';
                                    return dom;
                                },
                                "targets" : 10
                            }, {
                                "render" : function(data, type, row, meta) {
                                    var dom='';
                                    dom = row.recruitName
                                    if(row.empStatus=='2'){
                                        dom += '<span class="name-mark-out">离</span>';
                                    }
                                    return dom;
                                },
                                "targets" : 11,"class":"no-warp"
                            }, {
                                "render" : function(data, type, row, meta) {
                                    var dom='';
                                    dom = row.tutorName
                                    if(row.tutorStatus=='2'){
                                        dom += '<span class="name-mark-out">离</span>';
                                    }
                                    return dom;
                                },
                                "targets" : 12,"class":"no-warp"
                            }, {
                                "render" : function(data, type, row, meta) {
                                	if(row.createTime){
                               		     var dateTime=new Date(row.createTime).format("yyyy-MM-dd hh:mm:ss");
                                         var date=dateTime.substring(0,10)
                                         var time=dateTime.substring(11)
                                         return date+"<br>"+time;
                                	}else{
                                		return '未更新';
                                	}
                                  
                                },
                                "targets" : 13,"class":"no-warp text-l"
                            }, {
                                "render" : function(data, type, row, meta) {
                                    var dom = '';
                                    if ("4" == row.addressStatus) {
                                        dom = '<label class="label label-secondary radius">待审核</label>';
                                    }
                                    if ("5" == row.addressStatus) {
                                        dom = '<label class="label label-success radius">审核通过</label>';
                                    }
                                    if ("6" == row.addressStatus) {
                                    	var rejectUser,rejectTime,rejectReason;
                                        rejectUser=row.rejectUser?row.rejectUser:'无';
                                        rejectTime=row.rejectTime?new Date(row.rejectTime).format('yyyy-MM-dd'):'无';
                                        rejectReason=row.rejectReason?row.rejectReason:'无';
                                        var rejecTxt="驳回人："+rejectUser+"&#10;"+"驳回时间："+rejectTime+"&#10;"+"驳回原因："+rejectReason;
                                        dom = '<label class="label label-danger radius" style="cursor: pointer" title="'+rejecTxt+'">驳回</label>';
                                    }
                                    return dom;
                                },
                                "targets" : 14
                            }, {
                                "render" : function(data, type, row, meta) {
                                    var dom = '';
                                    if ("6" != row.addressStatus && "5" != row.addressStatus) {
                                        dom = '<a title="通过" href="javascript:;" onclick="pass(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
                                        dom += '<i class="iconfont icon-queren"></i></a>';
                                        dom += '<a title="驳回" href="javascript:;" onclick="rejected(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
                                        dom += '<i class="iconfont icon-weiwancheng"></i></a>';
                                    }
                                    return dom;
                                },
                                //指定是第三列
                                "targets" : 15
                            } ]
                        });
                    }
                })


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
            /*确认收货*/
            function okReceive(sendId) {
                var url = '/studentSend/okSend.do';
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
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });
            }

            /*批量确认收货*/
            function okAll() {
                var url = '/studentSend/okSend.do';
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
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });
            }

            /*通过审核*/
            function pass(sendId) {
                var url = '/studentSend/passEducation.do';
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
                                myDataTableOrderBook.fnDraw(true);
                                myDataTable.fnDraw(false);
                            }

                        }
                    });
                });
            }

            function rejectedAll() {
                var url = '/studentSend/rejectedEducation.do';
                var chk_value = [];
                $("input[name=sendIds]:checked").each(function() {
                    chk_value.push($(this).val());
                });

                $(".other-reason").hide();

                if (chk_value == null || chk_value.length <= 0||chk_value=="") {
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
	                              myDataTable.fnDraw(true);
	                          }
	                      }
	                  });
	                      layer.close(index);
                    },
                    area:['500px','300px'],
                    content:$("#rejectContent")
                });
            }

            function passAll() {
                var url = '/studentSend/passEducation.do';
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
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });
            }
            /*驳回*/
            function rejected(sendId) {
                var url = '/studentSend/rejectedEducation.do';
                var chk_value = [];
                chk_value.push(sendId);
                $(".other-reason").hide();
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
	                              myDataTable.fnDraw(true);
	                          }
	                      }
	                  });
                        layer.close(index)
                    },
                    area:['500px','300px'],
                    content:$("#rejectContent")
                });
            }

            function okOrder() {
                
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
                
                var url = '/studentSend/toSelectLogistics.do';
    			layer_show('勾选改为已订', url, 500, 300, function() {
    				 myDataTableOrderBook.fnDraw(true);
    			});

                /*
                var url = '/studentSend/okOrder.do';
                layer.confirm('确认要改为已订？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已订成功!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });*/
            }

            function noOrder() {
                var url = '/studentSend/noOrder.do';
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

                layer.confirm('确认要改为未订？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('未订成功!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });
            }

            function okReceive() {
                var url = '/studentSend/okReceive.do';
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
                layer.confirm('确认收件？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已通过确认!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });
            }

            function _search_order() {
                myDataTableOrderBook.fnDraw(true);
            }
            function _search_address() {
                myDataTable.fnDraw(true);
            }
            
            function queryOkOrder() {
                
                var unvsId = $("#unvsId").val();
                var semester = $("#semestero").val();
                console.log(semester);
                if(unvsId =='' || semester==''){
                	 layer.msg('请选择院校以及学期!!!', {
                         icon : 5,
                         time : 3000
                     });
                     return;
                }
				
                var url = '/studentSend/toQueryLogistics.do';
    			layer_show('筛选改为已订', url, 500, 300, function() {
    				myDataTableOrderBook.fnDraw(true);
    			});
    			
    			
                /*
                var url = '/studentSend/queryOkOrder.do';
                layer.confirm('确认要改为已订？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                        	"stdName" : function() {
                                return $("#stdNameo").val();
                            },
                            "mobile" : function() {
                                return $("#mobileo").val();
                            },
                            "idCard" : function() {
                                return $("#idCardo").val();
                            },
                            "unvsId" : function() {
                                return $("#unvsId").val();
                            },
                            "pfsnId" : function() {
                                return $("#pfsnId").val();
                            },
                            "batchId" : function() {
                                return $("#batchIdo").val();
                            },
                            "grade" : function() {
                                return $("#gradeo").val();
                            },
                            "addressStatus" : function() {
                                return $("#addressStatuso").val();
                            },
                            "semester" : function() {
                                return $("#semestero").val();
                            },
                            "stdStage" : function() {
                                return $("#stdStageo").val();
                            },
                            "orderBookStatus" : function() {
                                return $("#orderBookStatuso").val();
                            },
                            "receiveStatus" : function() {
                                return $("#receiveStatuso").val();
                            },
                          	"pfsnLevel" : function (){
                          		return $("#pfsnLevel").val();
                          	},
                          	"textbookType" : function (){
                          		return $("#textbookType").val();
                          	},
                          	"logisticsNo" : function (){
                          		return $("#logisticsNo").val();
                          	},
                          	"address" : function (){
                          		return $("#address").val();
                          	},
                          	"year" : function(){
                          		return $("#yearo").val();
                          	},
                          	"tutorName": function (){
                          		return $("#empNameo").val();
                          	}
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('已订成功!', {
                                    icon : 1,
                                    time : 1000
                                });
                                myDataTableOrderBook.fnDraw(false);
                                myDataTable.fnDraw(false);
                            }
                        }
                    });
                });*/
            }  
            

            function exportExe() {
                 var unvsId = $("#unvsId").val();
                 if(unvsId ==''){
                 	 layer.msg('请选择院校!!!', {
                          icon : 5,
                          time : 3000
                      });
                      return;
                 }
                 $("#export-form").submit();
            }
//            var HuitabIndex=7
        	/*用户-编辑*/
			function toEidt(learnId, stdId) {
				var url = '/studentBase/toEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId+'&tabName=modify';
                console.log(url);
                layer_show('学员信息', url, null, null, function() {
					myDataTableOrderBook.fnDraw(false);
					 myDataTable.fnDraw(false);
				}, true);
			}
        	
        	
        	 /*筛选 刷新教材*/
			 function queryRefreshTextbookData() {
	                var url = '/studentSend/queryRefreshTextbookData.do';
	                var unvsId = $("#unvsId").val();
	                var semester = $("#semestero").val();
	                console.log(semester);
	                if(unvsId =='' || semester==''){
	                	 layer.msg('请选择院校以及学期!!!', {
	                         icon : 5,
	                         time : 3000
	                     });
	                     return;
	                }

	                layer.confirm('确认要刷新教材？', function(index) {
	                    $.ajax({
	                        type : 'POST',
	                        url : url,
	                        data : {
	                        	"stdName" : function() {
	                                return $("#stdNameo").val();
	                            },
	                            "mobile" : function() {
	                                return $("#mobileo").val();
	                            },
	                            "idCard" : function() {
	                                return $("#idCardo").val();
	                            },
	                            "unvsId" : function() {
	                                return $("#unvsId").val();
	                            },
	                            "pfsnId" : function() {
	                                return $("#pfsnId").val();
	                            },
	                            "batchId" : function() {
	                                return $("#batchIdo").val();
	                            },
	                            "grade" : function() {
	                                return $("#gradeo").val();
	                            },
	                            "addressStatus" : function() {
	                                return $("#addressStatuso").val();
	                            },
	                            "semester" : function() {
	                                return $("#semestero").val();
	                            },
	                            "stdStage" : function() {
	                                return $("#stdStageo").val();
	                            },
	                            "orderBookStatus" : function() {
	                                return $("#orderBookStatuso").val();
	                            },
	                            "receiveStatus" : function() {
	                                return $("#receiveStatuso").val();
	                            },
	                          	"pfsnLevel" : function (){
	                          		return $("#pfsnLevel").val();
	                          	},
	                          	"textbookType" : function (){
	                          		return $("#textbookType").val();
	                          	},
	                          	"logisticsNo" : function (){
	                          		return $("#logisticsNo").val();
	                          	},
	                          	"address" : function (){
	                          		return $("#address").val();
	                          	},
	                          	"year" : function(){
	                          		return $("#yearo").val();
	                          	},
	                          	"tutorName": function (){
	                          		return $("#empNameo").val();
	                          	}
	                        },
	                        dataType : 'json',
	                        success : function(data) {
	                            if (data.code == _GLOBAL_SUCCESS) {
	                                layer.msg('刷新成功!', {
	                                    icon : 1,
	                                    time : 1000
	                                });
	                                myDataTableOrderBook.fnDraw(false);
	                                myDataTable.fnDraw(false);
	                            }
	                        }
	                    });
	                });
	         }
			 function selectRefreshTextbookData() {
	                var url = '/studentSend/selectRefreshTextbookData.do';
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

	                layer.confirm('确认要刷新教材？', function(index) {
	                    $.ajax({
	                        type : 'POST',
	                        url : url,
	                        data : {
	                            idArray : chk_value
	                        },
	                        dataType : 'json',
	                        success : function(data) {
	                            if (data.code == _GLOBAL_SUCCESS) {
	                                layer.msg('刷新成功!', {
	                                    icon : 1,
	                                    time : 1000
	                                });
	                                myDataTableOrderBook.fnDraw(false);
	                                myDataTable.fnDraw(false);
	                            }
	                        }
	                    });
	                });
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
								return $("#gradeo").val() ? $("#gradeo").val() : '';
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
			 
			 function init_pfsn_select_d() {
			    	_simple_ajax_select({
						selectId : "pfsnIdD",
						searchUrl : '/baseinfo/sPfsn.do',
						sData : {
							sId :  function(){
								return $("#unvsIdo").val() ? $("#unvsIdo").val() : '';	
							},
							ext1 : function(){
								return $("#pfsnLevelD").val() ? $("#pfsnLevelD").val() : '';
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
					$("#pfsnIdD").append(new Option("", "", false, true));
			    }
			 
			 function queryUpdateBatchId() {
	                var url = '/studentSend/toQueryUpdateBatch.do';
	                var semester = $("#semestero").val();
	                if(semester==''){
	                	 layer.msg('请选择学期!!!', {
	                         icon : 5,
	                         time : 3000
	                     });
	                     return;
	                }
	                layer_show('批量修改订书批次', url, 500, 300, function() {
	                	 myDataTableOrderBook.fnDraw(false);
                         myDataTable.fnDraw(false);
	    			});
	         }
			 
			 function selectUpdateBatchId(){
					var chk_value = [];
					$("input[name=sendIdss]:checked").each(function() {
						chk_value.push($(this).val());
					});
					if (chk_value.length == 0) {
						layer.msg('请选择要修改的发书数据！', {
							icon : 2,
							time : 2000
						});
						return;
					}
					var url = '/studentSend/toSelectUpdateBatchId.do';
					layer_show('批量修改订书批次', url, 500, 300, function() {
						 myDataTableOrderBook.fnDraw(false);
                         myDataTable.fnDraw(false);
					});
				}
			 function importOrderBook(){
				    var url = '/studentSend/toImportOrderBook.do';
			        layer_show('已订书名单导入', url, null, 510, function () {
			        	 myDataTableOrderBook.fnDraw(false);
                         myDataTable.fnDraw(false);
			        });
			 }
			 
			 function importTextbookPfsncode(){
				    var url = '/studentSend/toImportTextbookPfsncode.do';
			        layer_show('教材专业编码导入', url, null, 510, function () {
			        	 myDataTableOrderBook.fnDraw(false);
                      myDataTable.fnDraw(false);
			        });
			 }