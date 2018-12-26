 var myDataTable;
            $(function() {

                //初始化院校类型下拉框
                _init_select("recruitType", dictJson.recruitType);

                //初始状态
                _init_select("isStop", [ {
                    "dictValue" : "1",
                    "dictName" : "是"
                }, {
                    "dictValue" : "0",
                    "dictName" : "否"
                } ]);

                //初始化数据表格
                myDataTable = $('.table-sort').dataTable({
                    "processing": true,
                    "serverSide" : true,
                    "dom" : 'rtilp',
                    "ajax" : {
                        url : "/bdUniversity/findAllBdUniversity.do",
                        type : "post",
                        data : {
                            "unvsName" : function() {
                                return $("#unvsName").val();
                            },
                            unvsCode : function() {
                                return $("#unvsCode").val();
                            },
                            recruitType : function() {
                                return $("#recruitType").val();
                            },
                            isStop : function() {
                                return $("#isStop").val();
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
                        "mData" : "unvsName"
                    }, {
                        "mData" : "unvsCode"
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    }, {
                        "mData" : "remark"
                    }, {
                        "mData" : "sort"
                    }, {
                        "mData" : null
                    }, {
                        "mData" : null
                    } ],
                    "columnDefs" : [ {
                        "render" : function(data, type, row, meta) {
                            return '<input type="checkbox" value="' + row.unvsId + '" name="unvsId"/>';
                        },
                        "targets" : 0
                    }, {
                        "render" : function(data, type, row, meta) {
                            for (var i = 0; i < dictJson.recruitType.length; i++) {
                                if (row.recruitType == dictJson.recruitType[i].dictValue)
                                    return dictJson.recruitType[i].dictName;
                            }
                            return "";
                        },
                        "targets" : 3
                    }, {
                        "render" : function(data, type, row, meta) {
                            var p = row.provinceCode ? row.provinceCode : '';
                            var c = row.cityCode ? row.cityCode : '';
                            var d = row.districtCode ? row.districtCode : '';

                            var province = _findProvinceName(p);
                            var city = _findCityName(p, c);
                            var district = _findDistrictName(p, c, d);

                            var address = '';

                            if (province) {
                                address += province;
                            }

                            if (city) {
                                address += ' - ' + city;
                            }

                            if (district) {
                                address += ' - ' + district;
                            }
                            if(row.unvsAddress){
                                address +=  row.unvsAddress;
                            }else {
                                address += ' - '
                            }


                            return address;
                        },
                        "targets" : 4
                    }, {
                        "render" : function(data, type, row, meta) {
                            var dom = '';

                            dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.unvsId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-edit"></i></a>';
                            /* dom += '&nbsp;&nbsp;&nbsp;';
                            dom += '<a title="删除" href="javascript:;" onclick="member_del(this,\'' + row.unvsId + '\')" class="ml-5" style="text-decoration: none">';
                            dom += '<i class="iconfont icon-shanchu"></i></a>'; */

                            return dom;
                        },
                        //指定是第三列
                        "targets" : 8
                    }, {
                        "render" : function(data, type, row, meta) {
                            return 1 == row.isStop ? '<span class="label radius">已停招</span>' : '<span class="label label-success radius">未停招</span>';
                        },
                        "targets" : 7
                    },{
                        "targets" : 1,
                        "class":"text-l"
                    } ]
                });

            });

            /*用户-添加*/
            function member_add() {
                var url = '/bdUniversity/edit.do' + '?exType=ADD';
                layer_show('添加大学院校', url, null, 480, function() {
//                    myDataTable.fnDraw(true);
                });
            }

            /*用户-编辑*/
            function member_edit(unvsId) {
                var url = '/bdUniversity/edit.do' + '?unvsId=' + unvsId + '&exType=UPDATE';
                layer_show('修改大学院校', url, null, 480, function() {
//                    myDataTable.fnDraw(true);
                });
            }

            /*用户-删除*/
            function member_del(obj, unvsId) {
                layer.confirm('确认要删除吗？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : '/bdUniversity/deleteBdUniversity.do',
                        data : {
                            id : unvsId
                        },
                        dataType : 'json',
                        success : function(data) {
                            layer.msg('已删除!', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=all]").attr("checked", false);
                        },
                        error : function(data) {
                            layer.msg('删除失败！', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=all]").attr("checked", false);
                        },
                    });
                });
            }

            function delAll() {
                var chk_value = [];
                $("input[name=unvsId]:checked").each(function() {
                    chk_value.push($(this).val());
                });

                layer.confirm('确认要删除吗？', function(index) {
                    $.ajax({
                        type : 'POST',
                        url : '/bdUniversity/deleteAllBdUniversity.do',
                        data : {
                            idArray : chk_value
                        },
                        dataType : 'json',
                        success : function(data) {
                            layer.msg('已删除!', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=all]").attr("checked", false);
                        },
                        error : function(data) {
                            layer.msg('删除失败！', {
                                icon : 1,
                                time : 1000
                            });
                            myDataTable.fnDraw(true);
                            $("input[name=all]").attr("checked", false);
                        },
                    });
                });
            }

            function _search() {
                myDataTable.fnDraw(true);
            }