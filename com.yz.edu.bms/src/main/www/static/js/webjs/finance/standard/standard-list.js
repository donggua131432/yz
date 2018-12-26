var myDataTable;
    
    function searchData(data) {
        return {
        	scholarship: $("#scholarship").val() ? $("#scholarship").val() : '',
            status: $("#status").val() ? $("#status").val() : '',
            feeName: $("#feeName").val() ? $("#feeName").val() : '',
            unvsId: $("#unvsId").val() ? $("#unvsId").val() : '',
            pfsnId: $("#pfsnId").val() ? $("#pfsnId").val() : '',
            grade: $("#grade").val() ? $("#grade").val() : '',
            pfsnLevel: $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
            start: data.start,
            length: data.length
        };
    }
    
    $(function () {

        _init_select("scholarship", dictJson.scholarship, null);
        _init_select("status", dictJson.status, null);
        _init_select("grade", dictJson.grade, null);
        _init_select("pfsnLevel", dictJson.pfsnLevel, null);
        _simple_ajax_select({
            selectId: "unvsId",
            searchUrl: '/baseinfo/sUnvs.do',
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
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/standard/list.do",
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
                        $(row).children('td').eq(3).attr(
                            'style', 'text-align: left;')
                    },
                    "language": _my_datatables_language,
                    columns: [{
                        "mData": null
                    }, {
                        "mData": "feeName"
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
                                return '<input type="checkbox" value="' + row.feeId + '" name="feeIds"/>';
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
                                    dom += '<a onClick="standard_stop(\''
                                        + row.feeId
                                        + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                } else if (row.status == '2') {
                                    dom += '<a onClick="standard_start(\''
                                        + row.feeId
                                        + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                }
                                dom += ' &nbsp;';
                                dom += '<a title="编辑" href="javascript:void(0)" onclick="standard_edit(\''
                                    + row.feeId
                                    + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="iconfont icon-edit"></i></a>';
                                dom += ' &nbsp;';
                                dom += '<a title="删除" href="javascript:;" onclick="standard_del(this,\''
                                    + row.feeId
                                    + '\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-shanchu"></i></a>';
                                return dom;
                            },
                            "targets": 8
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                return _findDict(
                                    "scholarship",
                                    row.scholarship);
                            },
                            "targets": 2
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
                            "targets": 7
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                var amount = 0;
                                if(null != row.payable && row.payable.length > 0){
	                                var pay = row.payable
	                                for (var i = 0; i < pay.length; i++) {
	                                    if (null != pay) {
	                                    	
	                                    	var itemName = pay[i].itemName;
	                                    	var pfsnInfo = row.pfsnInfo[0];
	                                    	if(null != pfsnInfo){
	                                    		itemName = getItemName(itemName,pfsnInfo.grade);
	                                    	}
	                                        dom += pay[i].itemCode
	                                            + ':'
	                                            + itemName
	                                            + '['
	                                            + pay[i].defineAmount
	                                            + ']</br>';
	                                    }
	                                    if (null != pay[i].defineAmount) {
	                                        amount = amount
	                                            + parseFloat(pay[i].defineAmount);
	                                    }
	                                }
                                }
                                dom += "总计：" + amount.toFixed(2);
                                return dom;
                            },
                            "targets": 3
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if(null != row.pfsnInfo && row.pfsnInfo.length > 0){
	                                var pfsnInfo = row.pfsnInfo;
	                                for (var i = 0; i < pfsnInfo.length; i++) {
	                                    var pfsn = pfsnInfo[i];
	                                    dom += '('
	                                        + pfsn.pfsnCode
	                                        + ')';
	                                    dom += pfsn.pfsnName
	                                        + '</br>';
                                }
                                }
                                return dom;
                            },
                            "targets": 5,
                            "class":"text-l"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if(null != row.pfsnInfo && row.pfsnInfo.length > 0){
	                                var level = row.pfsnInfo[0].pfsnLevel;
	                                dom += _findDict(
	                                    "pfsnLevel", level);
                                }
                                return dom;
                            },
                            "targets": 4,
                            "class":"text-l"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if(null != row.testArea && row.testArea.length > 0){
	                                var testArea = row.testArea;
	                                for (var i = 0; i < testArea.length; i++) {
	                                    var area = testArea[i];
	                                    dom += area.taName
	                                        + ';';
	                                }
                                }

                                return dom;
                            },
                            "targets": 6,"class":"text-l"
                        }]
                });
    });

    function standard_edit(feeId) {
        var url = '/standard/toEdit.do' + '?feeId=' + feeId;
        layer_show('修改收费标准', url, null, 510, function () {
//            myDataTable.fnDraw(true);
        }, true);
    }

    function standard_add() {
        var url = '/standard/toAdd.do';
        layer_show('添加收费标准', url, null, 510, function () {
//            myDataTable.fnDraw(true);
        }, true);

    }

    /*管理员-删除*/
    function standard_del(obj, id) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/standard/delete.do',
                data: {
                    feeId: id
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

    function standard_stop(feeId) {
        layer.confirm('确认要停用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/standard/block.do',
                data: {
                    feeId: feeId
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

    function standard_start(feeId) {
        layer.confirm('确认要启用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/standard/start.do',
                data: {
                    feeId: feeId
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
    function searchStandard() {
        myDataTable.fnDraw(true);
    }

    function delAll() {
        var chk_value = [];
        var $input = $("input[name=feeIds]:checked");

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
                url: '/standard/deleteFees.do',
                data: {
                    feeIds: chk_value
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