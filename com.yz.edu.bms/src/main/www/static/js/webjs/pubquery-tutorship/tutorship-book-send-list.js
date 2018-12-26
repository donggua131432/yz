var myDataTable;
    $(function () {

        //初始辅导书发放状态
        _init_select("orderBookStatus", [{
            "dictValue": "1",
            "dictName": "是"
        }, {
            "dictValue": "0",
            "dictName": "否"
        }]);

        //初始化年级下拉框
        _init_select("grade", dictJson.grade);

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
            placeholder: '--请选择院校--'
        });
        $("#unvsId").append(new Option("", "", false, true));
        //初始化优惠类型
        _init_select("scholarship", dictJson.scholarship);

        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/tutorship/findAllTutorshipBookSend.do",
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
                    "logisticsNo": function () {
                        return $("#logisticsNo").val();
                    },
                    "orderBookStatus": function () {
                        return $("#orderBookStatus").val();
                    },
                    "grade": function () {
                        return $("#grade").val();
                    },
                    "unvsId": function () {
                        return $("#unvsId").val();
                    },
                    "pfsnName": function () {
                        return $("#pfsnName").val();
                    },
                    "pfsnLevel": function () {
                        return $("#pfsnLevel").val();
                    },
                    "scholarship": function () {
                        return $("#scholarship").val();
                    }
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            columns: [{
                "mData": "stdName"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData": "empName"
            }, {
                "mData": "campusName"
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
                    "render": function (data, type, row, meta) {
                        var stdStage =  _findDict("stdStage", row.stdStage);
                        return  stdStage + "("+row.grade+"级)"
                    },
                    "targets": 1
                },
                {
                    "render" : function(data, type, row, meta) {
                        var pfsnName = row.pfsnName;
                        var unvsName = row.unvsName;
                        var recruitType = row.recruitType;
                        var pfsnCode = row.pfsnCode;
                        var pfsnLevel = row.pfsnLevel;

                        var text = '';
                        if (recruitType) {
                            text += _findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                        }

                        if (unvsName) {
                            text += unvsName + "<br/>";
                        }

                        if (pfsnLevel) {
                            text += _findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                        }

                        if (pfsnName) {
                            text += pfsnName;
                        }

                        if (pfsnCode) {
                            text += "(" + pfsnCode + ")";
                        }

                        return text ? text : '无';
                    },
                    "targets" : 2,
                    "class" : "text-l"
                },
                {
                    "render": function (data, type, row, meta) {
                        if(row.subOrderStatus=='2'){
                            return "已："+row.payable;
                        }else {
                            return "<span style='color: red'>应："+row.payable+"</span>";
                        }
                    },
                    "targets": 5,"class" : "text-l"
                }, {
                    "render" : function(data, type, row, meta) {
                        if(!row.books) return '';
                        var dom = "";
                        for (var i = 0; i < row.books.length; i++) {
                            var book = row.books[i];

                            dom += book.textbookId + " " + book.textbookName + "<br>";
                        }
                        return dom;
                    },
                    "targets" : 6,
                    "class":"text-l"
                },
                {
                    "render" : function(data, type, row, meta) {
                        var dom = '';
                        var logisticsNo=row.logisticsNo||'';
                        if(row.logisticsName != null && row.logisticsName == 'jd'){
                        	dom += '<a href="http://www.jdwl.com/order/search?waybillCodes='+row.logisticsNo+'" target="_blank"><p class="c-blue">'
                            + logisticsNo + '</p></a>';
                        }else{
                        	dom += '<a href="http://www.sf-express.com/cn/sc/dynamic_function/waybill/#search/bill-number/'+row.logisticsNo+'" target="_blank"><p class="c-blue">'
                            + logisticsNo + '</p></a>';
                        }
                        return dom;
                    },
                    "targets" : 7,"class":"text-l"
                },
                {
                    "render": function (data, type, row, meta) {
                        if(row.orderBookStatus=='3'){
                            return '已发放';
                        }
                        else{
                            return "<span style='color: red;'>未发放</span>";
                        }
                    },
                    "targets": 8
                }
            ]
        });

    });

    function _search() {
        myDataTable.fnDraw(true);
    }