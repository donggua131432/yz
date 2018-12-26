var myDataTable;
			$(function() {

				_init_select("recruitType",dictJson.recruitType,null);
				_init_select("scholarship",dictJson.scholarship,null);
				_init_select("stdStage",dictJson.stdStage,null);
				_init_select('grade', dictJson.grade);
				_init_select('pfsnLevel', dictJson.pfsnLevel);
				$('select').select2({
                    placeholder : "--请选择--",
                    allowClear : true,
                    width : "59%"
                });
			   $("#recruitType").change(function() {
		          _init_select({
		            selectId : 'grade',
		            ext1 : $(this).val()
		          }, dictJson.grade);
		        });
			   
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
		       	 _init_select('sg', dictJson.sg); //优惠分组
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
		    	 $("#grade").change(function () {
	 	            $("#pfsnId").removeAttr("disabled");
	 	            init_pfsn_select();
			      });
	 			 $("#pfsnId").append(new Option("", "", false, true));
	 			 $("#pfsnId").select2({
	 		            placeholder: "--请先选择院校--"
	 		     });
				myDataTable = $('.table-sort')
						.dataTable(
								{
                                    "processing": true,
									"serverSide" : true,
									"dom" : 'rtilp',
									"ajax" : {
										url : "/stdFee/list.do",
										type : "post",
										dataType : 'json',
										data : function(data) {
							                  return searchData(data);
						                }
									},
									"pageLength" : 10,
									"pagingType" : "full_numbers",
									"ordering" : false,
									"searching" : false,
									"createdRow" : function(row, data,
											dataIndex) {
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
										"mData" : "accAmount"
									}, {
										"mData" : null
									} ], //[成教]汕头大学:(0003)会计学[专升本]
									"columnDefs" : [
											{
												"render" : function(data, type, row, meta) {
                                                    var dom = '';
                                                    dom+=_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                                                    dom += row.unvsName+"<br>";
                                                    dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                                    dom += row.pfsnName;
                                                    dom += '(' + row.pfsnCode + ')';
                                                    return dom;
												},
												"targets" : 2,
                                                "class":"text-l"
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom ='';
						                        	dom += '<a title="查看学员信息" onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\')"><span class="c-blue">'+ row.stdName +'</span></a>'
						                        	if(row.stdType ==='2'){
						                        		dom += ' <sup style="color:#f00">外</sup>';
						                        	}
						                        	if(row.scholarship){
						                        		dom += '<br>优惠类型：' + _findDict('scholarship',row.scholarship);
						                        	}
						                        	if(row.inclusionStatus){
						                        		dom += '<br>入围状态：' + _findDict('inclusionStatus',row.inclusionStatus);
						                        	}
						                        	if(row.feeName){
						                        		dom += '<br>收费标准：' + row.feeName;
						                        	}
						                        	if(row.offerName){
						                        		dom += '<br>优惠政策：' + row.offerName;
						                        	}
													return dom;
												},
												"targets" : 0,"class":"text-l"
											},
											{
												"render" : function(data, type,
														row, meta) {

													return row.grade + '级';
												},
												"targets" : 1
											},
											{
												"render" : function(data, type,
														row, meta) {
													return _findDict(
															"stdStage",
															row.stdStage);
												},
												"targets" : 3
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													dom += '<table class="table table-border table-bordered radius">';
													dom += '<thead>';
													dom += '<tr>';
													dom += '<th class="td-s" width="80">科目</th>';
													dom += '<th class="td-s" width="40">应缴</th>';
													dom += '<th class="td-s" width="40">缴费状态</th>';
													dom += '</tr>';
													dom += '</thead><tbody>';

													var amount = 0.00;
													if(null != row.payInfos && row.payInfos.length > 0){
														for (var i = 0; i < row.payInfos.length; i++) {
															var payInfo = row.payInfos[i];
															dom += '<tr>';
															
															var itemName = getItemName(payInfo.itemName,row.grade);
															
															dom += '<td class="td-s">'
																	+ payInfo.itemCode
																	+ ':'
																	+ itemName
																	+ '</td>';
															dom += '<td class="td-s">'
																	+ payInfo.payable
																	+ '</td>';
															var status = payInfo.subOrderStatus;
															dom += '<td class="td-s">';
															if('2' == status){
																dom += '' + _findDict("orderStatus", status) + '';
															}else{
																dom += '<span>' + _findDict("orderStatus", status) + '</span>';
															}
															dom += '</td>';
															dom += '</tr>';
															amount = amount
																	+ parseFloat(payInfo.payable);
														}
													}
													dom += '<tr >';
													dom += '<td class="td-s">合计：</td>';
													amount = amount.toFixed(2);
													dom += '<td class="td-s">' + amount
															+ '</td>';
													dom += '<td class="td-s"></td>';
													dom += '</tr></tbody>';
													dom += '</table>';
													return dom;
												},
												"targets" : 4
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													dom += '<a title="学员账户" class="tableBtn normal"  type="button" onclick="toAccount(\''+ row.stdId + '\')">学员账户</a>';
													return dom;
												},
												"targets" : 5
											},
											{
												"render" : function(data, type,
														row, meta) {
													var dom = '';
													var canpay = false;
													var orderStatus = row.orderStatus;
													if('1' == orderStatus){
														for (var i = 0; i < row.payInfos.length; i++) {
															var payInfo = row.payInfos[i];
															if(payInfo.subOrderStatus == 1){
																canpay = true;
															}
														}
														if(canpay){
															dom += '<a class="tableBtn normal mb-10" style="display: inline-block;padding: 2px 5px;margin-bottom: 5px;"  onclick="toPay(\''+ row.learnId + '\')">进入缴费</a><br>';
														}
													}
													if(null != row.payInfos && row.payInfos.length > 0){
														dom += '<a class="tableBtn blue" style="display: inline-block;padding: 2px 5px;margin-bottom: 5px;" onclick="toDetail(\''+ row.learnId +'\')">缴费详情</a><br>';
													}
													//有新的收费标准,并且不相同的时候
													if(null != row.feeId && null != row.nowFeeId && row.feeId　!=row.nowFeeId){
														dom += '<a class="tableBtn blue" style="display: inline-block;padding: 2px 5px;margin-bottom: 5px;" onclick="afreshStdOrder(\''+ row.learnId +'\')">刷新订单</a>';	
													}
													
													return dom;
												},
												"targets" : 7
											} ]
								});
			});

			function searchData(data){
				return {
					stdName : $("#stdName").val() ? $("#stdName").val() : '',
					idCard : $("#idCard").val() ? $("#idCard").val() : '',
					mobile : $("#mobile").val() ? $("#mobile").val() : '',
					recruitType : $("#recruitType").val() ? $("#recruitType").val() : '',
					scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
					stdStage : $("#stdStage").val() ? $("#stdStage").val() : '',
					grade : $("#grade").val() ? $("#grade").val() : '',
					unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
                    pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
					pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
					sg : $("#sg").val() ? $("#sg").val() : '',
					inclusionStatus : $("#inclusionStatus").val()? $("#inclusionStatus").val() :'',
					taName : $("#taName").val() ? $("#taName").val() :'',		
					start : data.start,
					length : data.length
				};
			}

			function searchStdFee() {
				myDataTable.fnDraw(true);
			}

			function toPay(learnId) {
				var url = '/stdFee/toPay.do' + '?learnId=' + learnId;
				layer_show('学员缴费', url, null, 510, function() {
//					myDataTable.fnDraw(false);
				}, true);
			}

			function toDetail(learnId) {
				var url = '/stdFee/toPayDetail.do' + '?learnId='
						+ learnId;
				layer_show('学员缴费详情', url, null, 510, function() {
//					myDataTable.fnDraw(false);
				}, true);
			}

			function addPayable() {
				var url = '/stdFee/toAdd.do';
				layer_show('新增应缴', url, null, 510, function() {
					myDataTable.fnDraw(true);
				}, false);
			}

			/*用户-编辑*/
			function toEidt(learnId, stdId) {
				var url = '/studentBase/toEdit.do' + '?learnId='
						+ learnId + '&stdId=' + stdId;
				layer_show('学员信息', url, null, null, function() {
					myDataTable.fnDraw(false);
				}, true);
			}

			function toAccount(stdId) {
				var url = '/stdFee/toAccount.do' + '?stdId=' + stdId;
				layer_show('学员账户', url, null, null, function() {
//					myDataTable.fnDraw(false);
				}, true);
			}

			function init_pfsn_select() {
				_simple_ajax_select({
					selectId : "pfsnId",
					searchUrl : '/baseinfo/sPfsn.do',
					sData : {
						sId : function() {
							return $("#unvsId").val() ? $("#unvsId").val() : '';
						},
						ext1 : function() {
							return $("#pfsnLevel").val() ? $("#pfsnLevel")
									.val() : '';
						},
						ext2 : function() {
							return $("#grade").val() ? $("#grade").val() : '';
						}
					},
					showText : function(item) {
						var text = '(' + item.pfsnCode + ')' + item.pfsnName;
						text += '[' + _findDict('pfsnLevel', item.pfsnLevel)
								+ ']';
						return text;
					},
					showId : function(item) {
						return item.pfsnId;
					},
					placeholder : '--请选择专业--'
				});
				$("#pfsnId").append(new Option("", "", false, true));
			}
			
			  function afreshStdOrder(learnId) {
			        layer.confirm('确定要刷新此学业的缴费项目吗？', function (index) {
			            //此处请求后台程序，下方是成功后的前台处理……
			            $.ajax({
			                type: 'POST',
			                url: '/stdFee/afreshStdOrder.do',
			                data: {
			                	learnId: learnId
			                },
			                dataType: 'json',
			                success: function (data) {
			                    if (data.code == _GLOBAL_SUCCESS) {
			                        myDataTable.fnDraw(false);
			                        layer.msg('已刷新!', {
			                            icon: 5,
			                            time: 1000
			                        });
			                    }
			                }
			            });
			        });
			    }
			  
			  function batchRefreshStdOrder() {
	                var url = '/stdFee/batchAfreshStdOrder.do';
	                var unvsId = $("#unvsId").val();
	                var grade = $("#grade").val();
	                if(unvsId =='' || grade==''){
	                	 layer.msg('请选择院校以及年级!!!', {
	                         icon : 5,
	                         time : 3000
	                     });
	                     return;
	                }

	                layer.confirm('确认要刷新订单数据？', function(index) {
	                    $.ajax({
	                        type : 'POST',
	                        url : url,
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
	                            "unvsId" : function() {
	                                return $("#unvsId").val();
	                            },
	                            "pfsnId" : function() {
	                                return $("#pfsnId").val();
	                            },
	                            "grade" : function() {
	                                return $("#grade").val();
	                            },
	                            "recruitType" : function() {
	                                return $("#recruitType").val();
	                            },
	                            "scholarship" : function() {
	                                return $("#scholarship").val();
	                            },
	                            "stdStage" : function() {
	                                return $("#stdStage").val();
	                            },
	                          	"pfsnLevel" : function (){
	                          		return $("#pfsnLevel").val();
	                          	},
	                          	"sg" : function (){
	                          		return $("#sg").val();
	                          	},
	                          	"inclusionStatus" : function (){
	                          		return $("#inclusionStatus").val();
	                          	},
	                          	"taName" : function (){
	                          		return $("#taName").val();
	                          	}
	                        },
	                        dataType : 'json',
	                        success : function(data) {
	                            if (data.code == _GLOBAL_SUCCESS) {
	                                layer.msg('刷新成功!', {
	                                    icon : 1,
	                                    time : 3000
	                                });
	                                myDataTable.fnDraw(false);
	                            }
	                        }
	                    });
	                });
	            }