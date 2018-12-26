var myDataTable;
    $(function () {

        _init_select("reptStatus", dictJson.reptStatus, null);
        _init_select("reptType", dictJson.reptType, null);
        _init_select("paymentType", dictJson.paymentType, null);
        
        $.ajax({
			type : "POST",
			url : "/feeStatistics/sCampusJson.do",
			data : {
			},
			dataType : 'json',
			success : function(data) {
				if(data.code == _GLOBAL_SUCCESS){
					var campusJson = data.body;
					_init_select("homeCampusId",campusJson,null);
					
					
				}
			}
		});
      // 初始校区下拉框
     	_simple_ajax_select({
    			selectId : "campusId",
    			searchUrl : "/campus/selectList.do",
    			sData : {},
    			showText : function(item) {
    				return item.campusName;
    			},					
    			showId : function(item) {
    				return item.campusId;
    			},
    			placeholder : '--请选择校区--'
    	});	

        myDataTable = $('.table-sort')
            .dataTable(
                {
                    "processing": true,
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/rept/list.do",
                        type: "post",
                        data: function (data) {
                            return searchData(data);
                        }
                    },
                    "pageLength": 10,
                    "pagingType": "full_numbers",
                    "ordering": false,
                    "searching": false,
                    "createdRow": function (row, data,
                                            dataIndex) {
                        $(row).addClass('text-c');
                        $(row).children('td').eq(6).attr(
                            'style', 'text-align: left;');
                        $(row).children('td').eq(7).attr(
                            'style', 'text-align: left;');
                    },
                    "language": _my_datatables_language,
                    columns: [{
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
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }],
                    "columnDefs": [
                        {
                            "render": function (data, type,
                                                row, meta) {
                                return '<input type="checkbox" value="' + row.reptId + '" name="reptIds"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                            	var dom = '';
                            	
                            	for (var i = 0; i < row.serialMarks.length; i++) {
									var serialMark = row.serialMarks[i];
									dom += serialMark + '<br>';
								}
                                return dom;
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                            	var dom = '';
                            	dom += row.stdName +'<br>';
                            	if(row.campusName){
	                            	dom += '归属校区：' + row.campusName;
                            	}
                            	
                                return dom;
                            },
                            "targets": 1
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if (row.sfMailno != null && row.sfMailno != '') {
                                     dom += '<a href="http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/' + row.sfMailno + '" target="_blank"><p class="c-blue">'
                                        + row.sfMailno + '</p></a>';
                                }
                                return dom;
                            },
                            "targets": 8
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dateTime= row.applyTime;
                                if(!dateTime){
                                    return '-'
                                }
                                var date=dateTime.substring(0,10)
                                var time=dateTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets": 3,"class":"text-l no-warp"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += _findDict("reptStatus", row.reptStatus);
                                return dom;
                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += '寄件人:财务部<br/>';
                                var sendTime = '';
                                if (row.sendTime != null && row.sendTime != '') {
                                    sendTime = row.sendTime;
                                }
                                dom += '寄件时间:' + sendTime;
                                return dom;
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += '申请类型：' + _findDict('reptType',row.reptType) + '<br>';
                                // 快递信息
                                if(row.reptType == '2'){
                                	dom += '支付方式:' + _findDict('paymentType',row.paymentType) + '<br/>';
                                	dom += '快递费用:' + row.expressAmount + '<br/>';
	                                dom += '收件人:' + row.userName + '<br/>';
	                                dom += '收件电话:' + row.mobile + '<br/>';
	                                dom += '收件地址:' + row.province + row.city + row.district + row.address;
                                } else if (row.reptType == '3') {
                                	dom += '自取校区：' + row.reptCampusName;
                                }
                                return dom;
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += '<table class="table table-border table-bordered radius">';
                                dom += '<thead>';
                                dom += '<tr>';
                                dom += '<th class="td-s" width="80">科目</th>';
                                dom += '<th class="td-s" width="40">应缴</th>';
                                dom += '<th class="td-s" width="40">已缴</th>';
                                dom += '</tr>';
                                dom += '</thead><tbody>';
                                var payable = 0.00;
                                var paidAmount = 0.00;
                                if (null != row.items && row.items.length > 0) {
                                    for (var i = 0; i < row.items.length; i++) {
                                        var payInfo = row.items[i];
                                        if (null != payInfo) {
                                            dom += '<tr>';
                                            dom += '<td class="td-s">'
                                                + payInfo.itemCode
                                                + ':'
                                                + payInfo.itemName
                                                + '</td>';
                                            dom += '<td class="td-s">'
                                                + payInfo.feeAmount
                                                + '</td>';
                                            dom += '<td class="td-s">';
                                            dom += payInfo.amount;
                                            dom += '</td>';
                                            dom += '</tr>';
                                            paidAmount = paidAmount + (payInfo.amount * 1);
                                            payable = payable + (payInfo.feeAmount * 1);
                                        }
                                    }
                                }
                                dom += '<tr >';
                                dom += '<td class="td-s">合计：</td>';
                                dom += '<td class="td-s">' + payable
                                    + '</td>';
                                dom += '<td class="td-s">' + paidAmount + '</td>';
                                dom += '</tr></tbody>';
                                dom += '</table>';
                                return dom;
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                            	var dom = '';
                                var orderStatus = row.reptStatus;
                                dom += '<a class="tableBtn normal" onclick="printRept(\'' + row.reptId + '\')">打印收据</a>';
                                if ('2' == orderStatus) {
                                    dom += '<a class="tableBtn normal" onclick="printSf(\'' + row.reptId + '\')">打印快递单</a>';
                                }
                                if(row.hasInform == '0'){
	                                if('2' == row.reptType ){
	                                	dom += '<a class="tableBtn normal ml-5" onclick="sendInform(\'' + row.reptId + '\')">微信提醒</a>';
	                                }
                                }
                                
                                if('7' == row.rept_status){
                                	dom += '<a class="tableBtn normal ml-5" onclick="picked(\'' + row.reptId + '\')">处理</a>';
                                }
                                return dom;
                            },
                            "targets": 11
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                            	var dom = '';
                            	dom += '<a title="'+ row.stdName +'" class="tableBtn normal"  type="button" onclick="toAccount(\''+ row.stdId + '\')">学员账户</a><br>';
                            	
                                return dom;
                            },
                            "targets": 9
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if (row.remark != null && row.remark != '') {
                                    dom += '<span>' + row.remark + '</span>';
                                } else {
                                    dom += '无';
                                }
                                return dom;
                            },
                            "targets": 10
                        }]
                });
    });

    function printRept(reptId) {
        var url = "/rept/reptPrint.do" + '?reptId=' + reptId;
        window.open(url);
    }

    function printRepts() {
        var chk_value = [];
        var $input = $("input[name=reptIds]:checked");

        $input.each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }
        var url = "/rept/reptPrints.do" + '?reptIds[]=' + chk_value;
        window.open(url);
    }

    function toAccount(stdId) {
		var url = '/stdFee/toAccount.do' + '?stdId=' + stdId;
		layer_show('学员账户', url, null, null, function() {
// myDataTable.fnDraw(false);
		}, true);
	}
    
    function searchData(data) {
        return {
            stdName: $("#stdName").val() ? $("#stdName").val() : '',
            mobile: $("#mobile").val() ? $("#mobile").val() : '',
            reptStatus: $("#reptStatus").val() ? $("#reptStatus").val() : '',
            idCard: $("#idCard").val() ? $("#idCard").val() : '',
            homeCampusId: $("#homeCampusId").val() ? $("#homeCampusId").val() : '',
            campusId: $("#campusId").val() ? $("#campusId").val() : '',
            applyTime: $("#applyTime").val() ? $("#applyTime").val() : '',
            sendTime: $("#sendTime").val() ? $("#sendTime").val() : '',
            sfMailno: $("#sfMailno").val() ? $("#sfMailno").val() : '',
            reptType: $("#reptType").val() ? $("#reptType").val() : '',
            paymentType: $("#paymentType").val() ? $("#paymentType").val() : '',
            start: data.start,
            length: data.length
        };
    }

    function searchRept() {
        myDataTable.fnDraw(true);
        countAmount();
    }
    
    function countAmount(){
    	$.ajax({
            type: 'POST',
            url: '/rept/count.do',
            data: {
            	stdName: function (){
            		return $("#stdName").val() ? $("#stdName").val() : '';
            	},
				mobile: function (){
            		return $("#mobile").val() ? $("#mobile").val() : '';
            	},
				reptStatus: function (){
            		return $("#reptStatus").val() ? $("#reptStatus").val() : '';
            	},
				idCard: function (){
            		return $("#idCard").val() ? $("#idCard").val() : '';
            	},
				homeCampusId: function (){
            		return $("#homeCampusId").val() ? $("#homeCampusId").val() : '';
            	},
				campusId: function (){
            		return $("#campusId").val() ? $("#campusId").val() : '';
            	},
				applyTime: function (){
            		return $("#applyTime").val() ? $("#applyTime").val() : '';
            	},
				sendTime: function (){
            		return $("#sendTime").val() ? $("#sendTim").val() : '';
            	},
				sfMailno: function (){
            		return $("#sfMailno").val() ? $("#sfMailno").val() : '';
            	},
				reptType: function (){
            		return $("#reptType").val() ? $("#reptType").val() : '';
            	},
				paymentType: function (){
            		return $("#paymentType").val() ? $("#paymentType").val() : '';
            	}
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    $("#reptAmount").text(data.body);
                }
            }
    	});
    }

    function printSf(reptId) {
        var url = "/rept/sfPrint.do" + '?reptId=' + reptId;
        window.open(url);
    }

    function printSfs() {
        var chk_value = [];
        var $input = $("input[name=reptIds]:checked");

        $input.each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }
        var url = "/rept/sfPrints.do" + '?reptIds[]=' + chk_value;
        window.open(url);
    }
    
    function picked(reptId){
    	layer.confirm('确认处理吗？', function (index) {
            // 此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: "/rept/picked.do",
                data: {
                    reptId: reptId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('处理成功!', {
                            icon: 6,
                            time: 1000
                        });
                    }
                }
            });

        });
    }
    
    function sendInform(reptId){
    	layer.confirm('确认发送提醒吗？', function (index) {
            // 此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: "/rept/sendReptInform.do",
                data: {
                    reptId: reptId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('发送成功!', {
                            icon: 6,
                            time: 1000
                        });
                    }
                }
            });

        });
    }
    
    function pickedBatch(){
    	var chk_value = [];
        var $input = $("input[name=reptIds]:checked");

        $input.each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认自取收据为已处理吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: "/rept/pickeds.do",
                data: {
                    reptIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('批量处理成功!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                    }
                }
            });
        });
    }

    function sfOrders() {
        var chk_value = [];
        var $input = $("input[name=reptIds]:checked");

        $input.each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认下单吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: "/rept/reptOrders.do",
                data: {
                    reptIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('批量下单成功!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                    }
                }
            });
        });
    }

    function mailed() {
        var chk_value = [];
        var $input = $("input[name=reptIds]:checked");

        $input.each(function () {
            chk_value.push($(this).val());
        });

        if (chk_value == null || chk_value.length <= 0) {
            layer.msg('未选择任何数据!', {
                icon: 5,
                time: 1000
            });
            return;
        }

        layer.confirm('确认已邮寄吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: "/rept/sended.do",
                data: {
                    reptIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('操作成功!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(false);
                        $("input[name=all]").attr("checked", false);
                    }
                }
            });
        });
    }