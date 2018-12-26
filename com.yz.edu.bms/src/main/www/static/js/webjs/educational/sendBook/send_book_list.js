var myDataTableOrderBook;
            
            var fsdentity = '[[${fsdentity';
            if(fsdentity=="sfwbfs"){
            	$("#jdOrders").hide();
            	$("#queryJdOrders").hide();
            	$("#printJds").hide();
            }else if(fsdentity=="jdwbfs"){
            	$("#sfOrders").hide();
            	$("#querySfOrders").hide();
            	$("#printSfs").hide();
            }else{
            	$("#jdOrders").show();
            	$("#queryJdOrders").show();
            	$("#printJds").show();
            	$("#sfOrders").show();
            	$("#querySfOrders").show();
            	$("#printSfs").show();
            }
            
            $(function() {
                //标签块table-sortfinancial
                $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
                //初始化年级下拉框
                _init_select("gradeo", dictJson.grade);
            
                //初始化学期
                _init_select("semestero", dictJson.semester);
                //初始化专业层次下拉框
                _init_select("pfsnLevel", dictJson.pfsnLevel);
                
                //初始化课程类型
				_init_select("textbookType", dictJson.courseType);
                
				//快递公司
                _init_select("logisticsName", dictJson.logisticsName);
                //订书状态
                _init_select("orderBookStatuso", [ {
                    "dictValue" : "2",
                    "dictName" : "已定未发"
                }, {
                    "dictValue" : "3",
                    "dictName" : "已发"
                } ]);
                
                _init_select("ifOrder", [ {
                    "dictValue" : "1",
                    "dictName" : "已下单"
                }, {
                    "dictValue" : "0",
                    "dictName" : "未下单"
                } ]);
                
                _init_select("ifNewRepeatBook", [ {
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
        		 
                myDataTableOrderBook = $('.table-sort-order-book').dataTable({
                    "processing": true,
                    "serverSide" : true,
                    "dom" : 'rtilp',
                    "ajax" : {
                        url : "/sendBooks/findNeedSendBooks.do",
                        type : "post",
                        data : {
                            "userName" : function() {
                                return $("#userName").val();
                            },
                            "mobile" : function() {
                                return $("#mobileo").val();
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
                            "semester" : function() {
                                return $("#semestero").val();
                            },
                            "orderBookStatus" : function() {
                                return $("#orderBookStatuso").val();
                            },
                            "pfsnLevel" : function (){
                            	return $("#pfsnLevel").val();
                            },
                            "textbookType" : function (){
                            	return $("#textbookType").val();
                            },
                            "beginTime" : function (){
                            	return $("#beginTime").val();
                            },
                            "endTime" : function (){
                            	return $("#endTime").val();
                            },
                            "logisticsNo" : function (){
                            	return $("#logisticsNo").val();
                            },
                            "logisticsName" : function (){
                            	return $("#logisticsName").val();
                            },
                            "checkTimeEnd" : function (){
                            	return $("#checkTimeEnd").val();
                            },
                            "checkTimeBegin" : function (){
                            	return $("#checkTimeBegin").val();
                            },
                            "ifOrder" : function (){
                            	return $("#ifOrder").val();
                            },
                            "recruitName" : function (){
                            	return $("#recruitName").val();
                            },
                            "tutorName" : function (){
                            	return $("#tutorName").val();
                            },
                            "stdName" : function (){
                            	return $("#stdName").val();
                            },
                            "ifNewRepeatBook" : function (){
                            	return $("#ifNewRepeatBook").val();
                            },
                            "remark" : function (){
                            	return $("#remark").val();
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
                        "mData" : "userName"
                    }, {
                        "mData" : null
                    }, {
                        "mData" : "address"
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
                    } , {
                        "mData" : "batchId"
                    } ],
                    "columnDefs" : [ {
                        "render" : function(data, type, row, meta) {
                            var dom = '<input type="checkbox" value="'+ row.sendId + '" name="sendIds"/>';
                            return dom;
                        },
                        "targets" : 0
                    }, {
                        "render" : function(data, type, row, meta) {
                            return _findDict("grade", row.grade);
                        },
                        "targets" : 1
                    }, {
                        "render" : function(data, type, row, meta) {
                        	 return _findDict("semester", row.semester);
                        },
                        "targets" : 2
                    }, {
                        "render" : function(data, type, row, meta) {
                        	var dom ='';
                        	dom = row.stdName;
                        	if(row.stdType ==='2'){
                        		dom += ' <sup style="color:#f00">外</sup>';
                        	}
                        	return dom;
                       },
                       "targets" : 3
                   }, {
                        "render" : function(data, type, row, meta) {
                            var pfsnName = row.pfsnName;
                            var unvsName = row.unvsName;
                            var recruitType = row.recruitType;
                            var pfsnCode = row.pfsnCode;
                            var pfsnLevel = row.pfsnLevel;
                            var text='';
                            _findDict("recruitType", recruitType).indexOf('成人')!=-1?text+='[成教]':text+='[国开]';

                            text += unvsName + "<br/>";

                            _findDict("pfsnLevel", pfsnLevel).indexOf('高中')!=-1?text+='[专科]':text+='[本科]';
                            text += pfsnName;

                            return text;
                        },
                        "targets" : 4,
                        "class" : "text-l no-warp"
                    },{
						"render" : function(data, type,row, meta) {
							var dom = row.recruitName;
							if(row.recruitStatus ==='2'){
                        		dom += ' <sup style="color:#f00">离</sup>';
                        	}
							return dom;
						},
						"targets" : 5
					},{
						"render" : function(data, type,row, meta) {
							var dom = row.tutorName;
							if(row.tutorStatus ==='2'){
                        		dom += ' <sup style="color:#f00">离</sup>';
                        	}
							return dom;
						},
						"targets" : 6
					},{
                        "render" : function(data, type, row, meta) {
                        	 return _findProvinceName(row.provinceCode) + _findCityName(row.provinceCode, row.cityCode)
                             + _findDistrictName(row.provinceCode, row.cityCode, row.districtCode);
                        },
                        "targets" :8,
                        "class":"text-l"
                    },{
                        "targets" : 9,
                        "class":"text-l"
                    },{
						"render" : function(data, type,
								row, meta) {
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
						"targets" : 11
					},{
						"render" : function(data, type, row, meta) {
							var dom = '';
							if('3' == row.orderBookStatus){
								if(row.logisticsName.indexOf("jd")>=0){
									dom += '<a onclick="printJd(\''+ row.sendId + '\')" title="打印快递单" class="tableBtn normal">打印快递单</a>';
								}else{
									dom += '<a onclick="printSf(\''+ row.sendId + '\')" title="打印快递单" class="tableBtn normal">打印快递单</a>';
								}
							}
							return dom;
						},
						"targets" : 12
					},{
                        "render" : function(data, type, row, meta) {
                            if(!row.sendDate){
                                return '-'
                            }
                            var date=row.sendDate.substring(0,10)
                            var time=row.sendDate.substring(11)
                            return date+'<br>'+time
                        },
                        "targets" : 13,"class":"text-l"
                    },{
						"render" : function(data, type, row, meta) {
							var dom = '';
							if(row.remark != null && row.remark != ''){
								dom += '<span>'+row.remark + '</span>';
							}else{
								dom += '无';
							}
							return dom;
						},
						"targets" : 14
					}, {
                        "render" : function(data, type, row, meta) {
                            var dom = '';
                            if('3' != row.orderBookStatus){
	                            dom += '<a title="驳回" href="javascript:;" onclick="rejected(\'' + row.sendId + '\')" class="ml-5" style="text-decoration: none">';
	                            dom += '<i class="iconfont icon-weiwancheng"></i></a>';
                            }
                            return dom;
                        },
                        //指定是第三列
                        "targets" : 16
                    }]
                });

            });
           
            function _search_order() {
                myDataTableOrderBook.fnDraw(true);
            }
            
            function sfOrders() {
				var chk_value = [];
				var $input = $("input[name=sendIds]:checked");
				
				$input.each(function() {
					chk_value.push($(this).val());
				});
				
				if(chk_value == null || chk_value.length <= 0){
					layer.msg('未选择任何数据!', {
						icon : 5,
						time : 1000
					});
					return;
				}
				
				layer.confirm('确认下单吗？',function(index) {
					$.ajax({
						type : 'POST',
						url : '/sendBooks/sfOrders.do',
						data : {
							sendIds : chk_value
						},
						dataType : 'json',
						success : function(data) {
							if (data.code == _GLOBAL_SUCCESS) {
								layer.msg('批量下单成功!', {
									icon : 1,
									time : 2000
								});
								myDataTableOrderBook.fnDraw(false);
								$("input[name=all]").attr("checked",false);
							}
						}
					});
				});
			}
            
            
            function jdOrders() {
				var chk_value = [];
				var $input = $("input[name=sendIds]:checked");
				
				$input.each(function() {
					chk_value.push($(this).val());
				});
				
				if(chk_value == null || chk_value.length <= 0){
					layer.msg('未选择任何数据!', {
						icon : 5,
						time : 1000
					});
					return;
				}
				
				if(chk_value.length >=5000){
					layer.msg('京东批量下单不能超过5000单!', {
						icon : 5,
						time : 1000
					});
					return;
				}
				
				layer.confirm('确认下单吗？',function(index) {
					$.ajax({
						type : 'POST',
						url : '/sendBooks/jdOrders.do',
						data : {
							sendIds : chk_value
						},
						dataType : 'json',
						success : function(data) {
							if (data.code == _GLOBAL_SUCCESS) {
								layer.msg('批量下单成功!', {
									icon : 1,
									time : 2000
								});
								myDataTableOrderBook.fnDraw(false);
								$("input[name=all]").attr("checked",false);
							}
						}
					});
				});
			}
            
            function printSfs(){
				var chk_value = [];
				var $input = $("input[name=sendIds]:checked");
				
				$input.each(function() {
					chk_value.push($(this).val());
				});
				
				if(chk_value == null || chk_value.length <= 0){
					layer.msg('未选择任何数据!', {
						icon : 5,
						time : 1000
					});
					return;
				}
				var url = '/sendBooks/sfPrints.do'+ '?sendIds[]='+chk_value;
				window.open(url);
			}
            function printJds(){
				var chk_value = [];
				var $input = $("input[name=sendIds]:checked");
				
				$input.each(function() {
					chk_value.push($(this).val());
				});
				
				if(chk_value == null || chk_value.length <= 0){
					layer.msg('未选择任何数据!', {
						icon : 5,
						time : 1000
					});
					return;
				}
				var url = '/sendBooks/jdPrints.do'+ '?sendIds[]='+chk_value;
				window.open(url);
			}
            function printSf(sendId){
    			var url = '/sendBooks/sfPrint.do'+ '?sendId='+sendId;
    			window.open(url);
    		}
            function printJd(sendId){
    			var url = '/sendBooks/jdPrint.do'+ '?sendId='+sendId;
    			window.open(url);
    		}
            
            function exportSendInfo(){
            	$("#export-form").submit();
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
            
            function querySfOrders() {
                var url = '/sendBooks/querySfOrders.do';
                
                layer.confirm('确认要批量顺丰下单吗？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : url,
                        data : {
                            "userName" : function() {
                                return $("#userName").val();
                            },
                            "mobile" : function() {
                                return $("#mobileo").val();
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
                            "semester" : function() {
                                return $("#semestero").val();
                            },
                            "orderBookStatus" : function() {
                                return $("#orderBookStatuso").val();
                            },
                            "pfsnLevel" : function (){
                            	return $("#pfsnLevel").val();
                            },
                            "textbookType" : function (){
                            	return $("#textbookType").val();
                            },
                            "beginTime" : function (){
                            	return $("#beginTime").val();
                            },
                            "endTime" : function (){
                            	return $("#endTime").val();
                            },
                            "logisticsNo" : function (){
                            	return $("#logisticsNo").val();
                            },
                            "checkTimeEnd" : function (){
                            	return $("#checkTimeEnd").val();
                            },
                            "checkTimeBegin" : function (){
                            	return $("#checkTimeBegin").val();
                            },
                            "ifOrder" : function (){
                            	return $("#ifOrder").val();
                            }
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('顺丰批量处理订单成功!', {
                                    icon : 1,
                                    time : 3000
                                });
                                myDataTableOrderBook.fnDraw(false);
                            }
                        }
                    });
                });
         }
         function queryJdOrders() {
                var url = '/sendBooks/queryJdOrders.do';
                
                layer.confirm('确认要批量京东下单吗？', function(index) {
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
                            "semester" : function() {
                                return $("#semestero").val();
                            },
                            "orderBookStatus" : function() {
                                return $("#orderBookStatuso").val();
                            },
                            "pfsnLevel" : function (){
                            	return $("#pfsnLevel").val();
                            },
                            "textbookType" : function (){
                            	return $("#textbookType").val();
                            },
                            "beginTime" : function (){
                            	return $("#beginTime").val();
                            },
                            "endTime" : function (){
                            	return $("#endTime").val();
                            },
                            "logisticsNo" : function (){
                            	return $("#logisticsNo").val();
                            },
                            "checkTimeEnd" : function (){
                            	return $("#checkTimeEnd").val();
                            },
                            "checkTimeBegin" : function (){
                            	return $("#checkTimeBegin").val();
                            },
                            "ifOrder" : function (){
                            	return $("#ifOrder").val();
                            }
                        },
                        dataType : 'json',
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('京东批量处理订单成功!', {
                                    icon : 1,
                                    time : 3000
                                });
                                myDataTableOrderBook.fnDraw(false);
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
                     $("#otherReject").prop('disabled',false)
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
                     	if(otherReject.length>20){
                     		 layer.msg('最大支持20个字符!', {
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
	                             myDataTableOrderBook.fnDraw(false);
	                          }
	                      }
	                  });
                     layer.close(index)
                 },
                 area:['500px','300px'],
                 content:$("#rejectContent")
             });
         }