var myDataTable;
    $(function () {

        _init_select("scholarship", dictJson.scholarship);
        _init_select("status", dictJson.status);
        _init_select("grade", dictJson.grade);
        _init_select("pfsnLevel", dictJson.pfsnLevel);
        _init_select("sg", dictJson.sg);
        _init_select('scholarship', dictJson.scholarship);
        $("#sg").change(function() { //联动
   			_init_select({
   				selectId : 'scholarship',
   				ext1 : $(this).val()
   			}, dictJson.scholarship);
   		 });
        
        _simple_ajax_select({
            selectId: "unvsId",
            searchUrl: '/baseinfo/sUnvs.do]',
            sData: {
            },
            showText: function (item) {
                var text = '[' + _findDict('recruitType', item.recruitType) + ']';
                text += item.unvsName + '(' + item.unvsCode + ')';
                return text;
            },
            showId: function (item) {
                return item.unvsId;
            },
            placeholder: '--请选择院校--'
        });
        $("#unvsId").append(new Option("", "", false, true));
        _simple_ajax_select({
			selectId : "pfsnId",
			searchUrl : '/baseinfo/sPfsn.do]',
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
        myDataTable = $('#tab')
            .dataTable(
                {
                    "processing": true,
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/offer/list.do",
                        type: "post",
                        data: {
                            status: function () {
                                return $("#status").val();
                            },
                            offerName: function () {
                                return $("#offerName").val();
                            },
                            pfsnLevel : function (){
                            	return $("#pfsnLevel").val();
                            },
                            unvsId : function (){
                            	return $("#unvsId").val();
                            },
                            pfsnId : function (){
                            	return $("#pfsnId").val();
                            },
                            grade : function (){
                            	return $("#grade").val();
                            },
                            scholarship : function (){
                            	return $("#scholarship").val();
                            },
                            sg : function (){
                            	return $("#sg").val();
                            }
                            
                        }
                    },
                    "pageLength": 10,
                    "pagingType": "full_numbers",
                    "ordering": false,
                    "searching": false,
                    "createdRow": function (row, data,
                                            dataIndex) {
                        $(row).addClass('text-c');
                        $(row).children('td').eq(2).attr(
                            'style', 'text-align: left;');
                    },
                    "language": _my_datatables_language,
                    columns: [{
                        "mData": null
                    }, {
                        "mData": "offerName"
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
                                return '<input type="checkbox" value="' + row.offerId + '" name="offerIds"/>';
                            },
                            "targets": 0
                        },{
                            "targets": 1,
                            "class":"text-l"
                        }, {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if (row.status == '1') {
                                    dom += '<a onClick="offer_stop(\''
                                        + row.offerId
                                        + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                } else if (row.status == '2') {
                                    dom += '<a onClick="offer_start(\''
                                        + row.offerId
                                        + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                }
                                dom += ' &nbsp;';
                                dom += '<a title="编辑" href="javascript:void(0)" onclick="offer_edit(\''
                                    + row.offerId
                                    + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="iconfont icon-edit"></i></a>';
                                dom += ' &nbsp;';
                                dom += '<a title="删除" href="javascript:;" onclick="offer_del(this,\''
                                    + row.offerId
                                    + '\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-shanchu"></i></a>';
                                return dom;
                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                if (row.status == 1) {
                                    return '<span class="label label-success radius">已启用</span>';
                                } else {
                                    return '<span class="label label-danger radius">已禁用</span>';
                                }
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                var amount = 0;
                                var item = row.items;

                                for (var i = 0; i < item.length; i++) {
                                    if (null != item) {
                                    	
                                    	var itemName = "";
                                    	
                                    	if(null != row.pfsnInfos && row.pfsnInfos.length > 0){
                                    		itemName = getItemName(item[i].itemName,row.pfsnInfos[0].grade);
                                    	}else{
                                    		itemName = item[i].itemName;
                                    	}
                                    	
                                        dom += item[i].itemCode
                                            + ':'
                                            + itemName;
                                        
                                        dom  += ' [';
                                        if(item[i].discountType == '1'){
                                        	/* if (null != item[i].defineAmount) {
                                                amount = amount
                                                    + parseFloat(item[i].defineAmount);
                                            } */
                                        	
                                        	dom += '减';
                                        }else{
                                        	
                                        	dom += '乘';
                                        }
                                        dom += ':'
                                            + item[i].defineAmount
                                            + ']</br>';
                                    }
                                    
                                }
                                //dom += "总计：" + amount;
                                return dom;
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                var testArea = row.testArea;
                                for (var i = 0; i < testArea.length; i++) {
                                    var area = testArea[i];
                                    dom += area.taName
                                        + ';';
                                }

                                return dom;
                            },
                            "targets": 3
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {

                                return _findDict("pfsnLevel",row.pfsnLevel);
                            },
                            "targets": 3,"class":"text-l"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += '起: '
                                    + row.startTime;
                                dom += '</br>止: '
                                    + row.expireTime;
                                return dom;
                            },
                            "targets": 4,"class":"no-warp"
                        }]
                });
    });

    function offer_edit(offerId) {
        var url = '/offer/toEdit.do' + '?offerId=' + offerId;
        layer_show('修改收费标准', url, null, 510, function () {
//            myDataTable.fnDraw(false);
        }, true);
    }

    function offer_add() {
        var url = '/offer/toAdd.do';
        layer_show('添加收费标准', url, null, 510, function () {
            myDataTable.fnDraw(true);
        }, true);

    }

    /*管理员-删除*/
    function offer_del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/offer/delete.do',
                data: {
                    offerId: id
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已删除!', {
                            icon: 1,
                            time: 1000
                        });
                    }
                    myDataTable.fnDraw(false);
                }
            });
        });
    }

    function offer_stop(offerId) {
        layer.confirm('确认要停用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/offer/block.do',
                data: {
                    offerId: offerId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('已停用!', {
                            icon: 5,
                            time: 1000
                        });
                    }
                }
            });
        });
    }

    function offer_start(offerId) {
        layer.confirm('确认要启用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/offer/start.do',
                data: {
                    offerId: offerId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        myDataTable.fnDraw(false);
                        layer.msg('已启用!', {
                            icon: 6,
                            time: 1000
                        });
                    }
                }
            });

        });

    }
    function searchOffer() {
        myDataTable.fnDraw(true);
    }

    function delAll() {
        var chk_value = [];
        var $input = $("input[name=offerIds]:checked");

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
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/offer/deleteOffers.do',
                data: {
                    offerIds: chk_value
                },
                dataType: 'json',
                success: function (data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已删除!', {
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