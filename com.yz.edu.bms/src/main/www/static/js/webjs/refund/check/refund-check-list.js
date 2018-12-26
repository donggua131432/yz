var myDataTable;
    var jqueryId = "table-sort";
    var postUrl = "/refundCheck/findDirectorApproval.do";
    var editUrl = "/refundCheck/editToDirector.do";
    var map = {};
    $(function () {

        //初始学员状态
        _init_select("stdStage", dictJson.stdStage);
        //初始审批状态
        _init_select("checkStatus", [
            {
                "dictValue": "2", "dictName": "未完成"
            },
            {
                "dictValue": "3", "dictName": "完成"
            }
        ]);
        
        
		$("#tab_demo_bar").empty();
		if (!isSuper) {
			if (isXJ && isXJ == '1') {
				$("#tab_demo_bar").append("<span id=\"is_xj\">校监/主任审批</span>");
				jqueryId = "table-sort";
				postUrl = "/refundCheck/findDirectorApproval.do";
                editUrl = "/refundCheck/editToDirector.do";
			}
			if (isCW && isCW == '1') {
				$("#tab_demo_bar").append("<span id=\"is_cw\">财务审批</span>");
				jqueryId = "table-sortfinancial";
				postUrl = "/refundCheck/findFinancialApproval.do";
                editUrl = "/refundCheck/editToFinancial.do";
			}
			if (isXB && isXB == '1') {
				$("#tab_demo_bar").append("<span id=\"is_xb\">校办审批</span>");
				jqueryId = "table-sortschoolManaged";
				 jqueryId = "table-sortPrincipal";
                 postUrl = "/refundCheck/findPrincipalApproval.do";
                 editUrl = "/refundCheck/editToPrincipal.do";
			}
			
		} else {
			$("#tab_demo_bar").append("<span id=\"is_xj\">校监/主任审批</span>");
			$("#tab_demo_bar").append("<span id=\"is_cw\">财务审批</span>");
			$("#tab_demo_bar").append("<span id=\"is_xb\">校办审批</span>");
			jqueryId = "table-sort";
			postUrl = "/refundCheck/findDirectorApproval.do";
            editUrl = "/refundCheck/editToDirector.do";
		}

        $('select').select2({
            placeholder: "--请选择--",
            allowClear: true,
            width: "59%"
        });
        $("#tab_demo .tabBar span")
            .bind(
                "click",
                function () {
                	var _id = $(this).attr("id");
                    var index = $("#tab_demo .tabBar span")
                        .index(this);
                    if ('is_xj' == _id) {
                        jqueryId = "table-sort";
                        postUrl = "/refundCheck/findDirectorApproval.do";
                        editUrl = "/refundCheck/editToDirector.do";
                    } else if ('is_cw' == _id) {
                        jqueryId = "table-sortfinancial";
                        postUrl = "/refundCheck/findFinancialApproval.do";
                        editUrl = "/refundCheck/editToFinancial.do";
                    } else if ('is_xb' == _id) {
                        jqueryId = "table-sortPrincipal";
                        postUrl = "/refundCheck/findPrincipalApproval.do";
                        editUrl = "/refundCheck/editToPrincipal.do";
                    }
                    if (!map[index]) {
                        myDataTable = $('.' + jqueryId)
                            .dataTable(
                                {
                                    "serverSide": true,
                                    "dom": 'rtilp',
                                    "ajax": {
                                        url: postUrl,
                                        type: "post",
                                        data: {
                                            "stdName": function () {
                                                return $(
                                                    "#stdName")
                                                    .val();
                                            },
                                            "mobile": function () {
                                                return $(
                                                    "#mobile")
                                                    .val();
                                            },
                                            "idCard": function () {
                                                return $(
                                                    "#idCard")
                                                    .val();
                                            },
                                            "stdStage": function () {
                                                return $(
                                                    "#stdStage")
                                                    .val();
                                            },
                                            "checkStatus": function () {
                                                return $(
                                                    "#checkStatus")
                                                    .val();
                                            }
                                        }
                                    },
                                    "pageLength": 10,
                                    "pagingType": "full_numbers",
                                    "ordering": false,
                                    "searching": false,
                                    "createdRow": function (row, data,
                                                            dataIndex) {
                                        $(row)
                                            .addClass(
                                                'text-c');
                                    },
                                    "language": _my_datatables_language,
                                    columns: [
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": "stdName"
                                        },
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": "remark"
                                        },
                                        {
                                            "mData": "demurrageAmount"
                                        },
                                        {
                                            "mData": "refundAmount"
                                        },
                                        {
                                            "mData": "createUser"
                                        },
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": null
                                        },
                                        {
                                            "mData": null
                                        }],
                                    "columnDefs": [
                                        {
                                            "render": function (data,
                                                                type,
                                                                row,
                                                                meta) {
                                                return '<input type="checkbox" value="' + row.outTd + '" name="outTds"/>';
                                            },
                                            "targets": 0
                                        },
                                        {
                                            "render": function (data,
                                                                type,
                                                                row,
                                                                meta) {
                                                return _findDict(
                                                    "grade",
                                                    row.grade);
                                            },
                                            "targets": 2
                                        },
                                        {
                                            "render": function (data, type, row, meta) {
                                                var dom = '';
                                                dom+=_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                                                dom += row.unvsName+"<br>";
                                                dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                                dom += row.pfsnName;
                                                return dom;
                                            },
                                            "targets": 3,"class":"text-l"
                                        },
                                        {
                                            "render": function (data,
                                                                type,
                                                                row,
                                                                meta) {
                                                return _findDict(
                                                    "scholarship",
                                                    row.scholarship);
                                            },
                                            "targets": 4
                                        },
                                        {
                                            "render": function (data,
                                                                type,
                                                                row,
                                                                meta) {
                                                return _findDict(
                                                    "stdStage",
                                                    row.stdStage);
                                            },
                                            "targets": 5
                                        },{
                                            "targets" : 6,"class":"text-l"
                                        }, {
                                            "render" : function(data, type, row, meta) {
                                                var dateTime= row.createTime;
                                                if(!dateTime){
                                                    return '-'
                                                }
                                                var date=dateTime.substring(0,10)
                                                var time=dateTime.substring(11)
                                                return date+'<br>'+time
                                            },
                                            "targets" : 10,"class":"text-l no-warp"
                                        },{
                                            "render": function (data,
                                                                type,
                                                                row,
                                                                meta) {
                                                var dom = '';

                                                var checkStatus = row.checkStatus;
                                                if (3 == checkStatus) {
                                                    dom += '<span class="label label-success radius">'
                                                        + _findDict(
                                                            "checkStatus",
                                                            row.checkStatus)
                                                        + '</span>';
                                                } else {
                                                    dom += '<span class="label radius">'
                                                        + _findDict(
                                                            "checkStatus",
                                                            row.checkStatus)
                                                        + '</span>';
                                                }
                                                return dom;
                                            },
                                            "targets": 11
                                        },
                                        {
                                            "render": function (data,
                                                                type,
                                                                row,
                                                                meta) {
                                                var dom = '';
                                                if (3 == row.checkStatus) {
                                                    dom = '<a title="撤销" href="javascript:;" onclick="member_edit(\''
                                                        + row.refundId
                                                        + '\',\'CANCEL\')" class="ml-5" style="text-decoration: none">';
                                                    dom += '<i class="iconfont icon-tuichu"></i></a>';

                                                } else {
                                                    dom = '<a title="进入审核" href="javascript:;" onclick="member_edit(\''
                                                        + row.refundId
                                                        + '\',\'CHECK\')" class="ml-5" style="text-decoration: none">';
                                                    dom += '<i class="iconfont icon-shenhe"></i></a>';
                                                }
                                                return dom;
                                            },
                                            //指定是第三列
                                            "targets": 12
                                        }]
                                });
                        map[index] = myDataTable;
                    } else {
                        myDataTable = map[index];
                    }
                });

        //标签块table-sortfinancial
        $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon",
            "current", "click", "0");

        myDataTable = $('.table-sort')
            .dataTable(
                {
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: postUrl,
                        type: "post",
                        data: {
                            "stdName": function () {
                                return $("#stdName").val();
                            },
                            "mobile": function () {
                                return $("#mobile").val();
                            },
                            "idCard": function () {
                                return $("#idCard").val();
                            },
                            "stdStage": function () {
                                return $("#stdStage").val();
                            },
                            "checkStatus": function () {
                                return $("#checkStatus").val();
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
                    },
                    "language": _my_datatables_language,
                    columns: [{
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
                        "mData": "remark"
                    }, {
                    	"mData": "demurrageAmount"
                    }, {
                        "mData": "refundAmount"
                    }, {
                        "mData": "createUser"
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
                                return '<input type="checkbox" value="' + row.outTd + '" name="outTds"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                return _findDict("grade",
                                    row.grade);
                            },
                            "targets": 2
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom+=_findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                                dom += row.unvsName+"<br>";
                                dom+=_findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                dom += row.pfsnName;
                                return dom;
                            },
                            "targets": 3,"class":"text-l"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                return _findDict(
                                    "scholarship",
                                    row.scholarship);
                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                return _findDict(
                                    "stdStage",
                                    row.stdStage);
                            },
                            "targets": 5
                        },{"targets" : 6,"class":"text-l"},{
                            "render" : function(data, type, row, meta) {
                                var dateTime= row.createTime;
                                if(!dateTime){
                                    return '-'
                                }
                                var date=dateTime.substring(0,10)
                                var time=dateTime.substring(11)
                                return date+'<br>'+time
                            },
                            "targets" : 10,"class":"text-l no-warp"
                        },{
                            "render": function (data, type, row, meta) {
                                var dom = '';

                                var checkStatus = row.checkStatus;
                                if (3 == checkStatus) {
                                    dom += '<span class="label label-success radius">'
                                        + _findDict(
                                            "checkStatus",
                                            row.checkStatus)
                                        + '</span>';
                                } else {
                                    dom += '<span class="label radius">'
                                        + _findDict(
                                            "checkStatus",
                                            row.checkStatus)
                                        + '</span>';
                                }
                                return dom;
                            },
                            "targets": 11
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if (3 == row.checkStatus) {
                                    dom = '<a title="撤销" href="javascript:;" onclick="member_edit(\''
                                        + row.refundId
                                        + '\',\'CANCEL\')" class="ml-5" style="text-decoration: none">';
                                    dom += '<i class="iconfont icon-tuichu"></i></a>';

                                } else {
                                    dom = '<a title="进入审核" href="javascript:;" onclick="member_edit(\''
                                        + row.refundId
                                        + '\',\'CHECK\')" class="ml-5" style="text-decoration: none">';
                                    dom += '<i class="iconfont icon-shenhe"></i></a>';
                                }
                                return dom;
                            },
                            //指定是第三列
                            "targets": 12
                        }]
                });
        map[0] = myDataTable;
    });

    /*用户-编辑*/
    function member_edit(refundId, exType) {
        var url = editUrl + '?refundId=' + refundId + '&exType='
            + exType;
        layer_show('审核', url, null, 600, function () {
            map[0].fnDraw(false);
            if(map[1]){
            	map[1].fnDraw(false);
            }
            if(map[2]){
            	map[2].fnDraw(false);
            }
        });
    }

    function uuid() {
        return Math.random().toString(36).substring(3, 8)
    }

    function _search() {
        myDataTable.fnDraw(true);
    }