var myDataTable;
    $(function () {

        $("#isAllow").select2({
            placeholder: "--请选择--",
            allowClear: true
        });
        _init_select("scholarship", dictJson.scholarship, null);
        _init_select('grade', dictJson.grade);
        
        myDataTable = $('.table-sort')
            .dataTable(
                {
                    "processing": true,
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: "/coupon/list.do",
                        type: "post",
                        data: {
                            scholarship: function () {
                                return $("#scholarship").val() ? $("#scholarship").val() : '';
                            },
                            isAllow: function () {
                                return $("#isAllow").val() ? $("#isAllow").val() : '';
                            },
                            couponName: function () {
                                return $("#couponName").val() ? $("#couponName").val() : '';
                            },
                            itemCode : function (){
                            	return $("#itemCode").val() ? $("#itemCode").val() : '';
                            },
                            availableStartTime : function (){
                            	return $("#availableStartTime").val() ? $("#availableStartTime").val() : '';
                            },
                            availableExpireTime : function (){
                            	return $("#availableExpireTime").val() ? $("#availableExpireTime").val() : '';
                            },
                            publishStartTime : function (){
                            	return $("#publishStartTime").val() ? $("#publishStartTime").val() : '';
                            },
                            publishExpireTime : function (){
                            	return $("#publishExpireTime").val() ? $("#publishExpireTime").val() : '';
                            },
                            grade : function (){
                            	return $("#grade").val() ? $("#grade").val() : '';
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
                        "mData": "couponName"
                    }, {
                        "mData": null
                    }, {
                        "mData": null
                    }, {
                        "mData": "amount"
                    }, {
                        "mData": null
                    }, {
                        "mData": "remark"
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
                                return '<input type="checkbox" value="' + row.couponId + '" name="couponIds"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if (row.isAllow == '1') {
                                    dom += '<a onClick="coupon_stop(\''
                                        + row.couponId
                                        + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
                                } else {
                                    dom += '<a onClick="coupon_start(\''
                                        + row.couponId
                                        + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
                                }
                                dom += ' &nbsp;';
                                dom += '<a title="编辑" href="javascript:void(0)" onclick="coupon_edit(\''
                                    + row.couponId
                                    + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="iconfont icon-edit"></i></a>';
                                dom += ' &nbsp;';
                                dom += '<a title="删除" href="javascript:;" onclick="coupon_del(this,\''
                                    + row.couponId
                                    + '\')" class="ml-5" style="text-decoration: none">';
                                dom += '<i class="iconfont icon-shanchu"></i></a>';
                                return dom;
                            },
                            "targets": 10
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                return _findDict(
                                    "scholarship",
                                    row.scholarship);
                            },
                            "targets": 2
                        },{
                            "targets": 1,
                            "class":"text-l"
                        }, {
                            "render": function (data, type,
                                                row, meta) {
                                if (row.isAllow == 1) {
                                    return '<span class="label label-success radius">已启用</span>';
                                } else {
                                    return '<span class="label label-danger radius">已禁用</span>';
                                }
                            },
                            "targets": 9
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                var itemCodes = row.itemCodes
                                for (var i = 0; i < itemCodes.length; i++) {
                                    if (i == (itemCodes.length - 1)) {
                                        dom += itemCodes[i];
                                    } else {
                                        dom += itemCodes[i] + ", ";
                                    }
                                }
                                return dom;
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += row.publishStartTime + '</br>';
                                dom += row.publishExpireTime;
                                return dom;
                            },
                            "targets": 7,"class":"no-warp"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                dom += row.availableStartTime + '</br>';
                                dom += row.availableExpireTime;
                                return dom;
                            },
                            "targets": 8,"class":"no-warp"
                        },
                        {
                            "render": function (data, type,
                                                row, meta) {
                                var dom = '';
                                if (null != row.grade && '' != row.grade) {
                                    dom += row.grade + '级';
                                }
                                return dom;
                            },
                            "targets": 3
                        }
                    ]
                });
    });

    function coupon_edit(couponId) {
        var url = '/coupon/toEdit.do' + '?couponId=' + couponId;
        layer_show('修改优惠券规则', url, null, 510, function () {
//            myDataTable.fnDraw(false);
        }, true);
    }

    function coupon_add() {
        var url = '/coupon/toAdd.do';
        layer_show('添加优惠券规则', url, null, 510, function () {
            myDataTable.fnDraw(true);
        }, true);

    }

    /*管理员-删除*/
    function coupon_del(obj, couponId) {
        layer.confirm('确认要删除吗？', function (index) {
            $.ajax({
                type: 'POST',
                url: '/coupon/delete.do',
                data: {
                    couponId: couponId
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

    function coupon_stop(couponId) {
        layer.confirm('确认要停用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/coupon/block.do',
                data: {
                    couponId: couponId
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

    function coupon_start(couponId) {
        layer.confirm('确认要启用吗？', function (index) {
            //此处请求后台程序，下方是成功后的前台处理……
            $.ajax({
                type: 'POST',
                url: '/coupon/start.do',
                data: {
                    couponId: couponId
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
    function searchCoupon() {
        myDataTable.fnDraw(true);
    }

    function delAll() {
        var chk_value = [];
        var $input = $("input[name=couponIds]:checked");

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
                url: '/coupon/deleteCoupons.do',
                data: {
                    couponIds: chk_value
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