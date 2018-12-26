var myDataTable;
        $(function () {


            //校区-部门-组 联动
            _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');

            _init_select("empType", dictJson.empType);
            _init_select("empStatus", dictJson.empStatus);
            _init_select("sexType",[
                                    {"dictValue":"1","dictName":"男"},
                                    {"dictValue":"2","dictName":"女"}
                                    ]);

            myDataTable = $('.table-sort').dataTable(
                {
                    "serverSide": true,
                    "dom": 'rtilp',
                    "ajax": {
                        url: '/recruiter/list.do',
                        data: {
                            "campusId": function () {
                                return $("#campusId").val();
                            },
                            "dpId": function () {
                                return $("#dpId").val();
                            },
                            "groupId": function () {
                                return $("#groupId").val();
                            },
                            "empName": function () {
                                return $("#empName").val();
                            },
                            "empType": function () {
                                return $("#empType").val();
                            },
                            "empStatus" : function(){
                            	return $("#empStatus").val();
                            },
                            "sexType" : function (){
                            	return $("#sexType").val();
                            },
                            "mobile" : function (){
                            	return $("#mobile").val();
                            },
                            "idCard" : function (){
                            	return $("#idCard").val();
                            },
                            "recruitCode" : function (){
                            	return $("#recruitCode").val();
                            },
                            "yzId" : function (){
                            	return $("#yzId").val();
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
                            "render": function (data, type, row, meta) {
                                return '<input type="checkbox" value="' + row.empId + '" name="empIds"/>';
                            },
                            "targets": 0
                        },
                        {
                            "render": function (data, type, row, meta) {

                                if (row.headShot == null || row.headShot == '') {
                                    var defaultUrl = "emp/default.png";
                                    return '<img style="width: 100px; height: 140px;object-fit: cover;" src="' + (_FILE_URL + defaultUrl + "?" + Date.parse(new Date())) + '">';
                                } else {
                                    return '<img style="width: 100px; height: 140px;object-fit: cover;" src="' + (_FILE_URL + row.headShot + "?" + Date.parse(new Date())) + '">';
                                }
                            },
                            "targets": 1
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';
                                dom += '姓名:' + (row.empName == null ? '暂无' : row.empName) + '</br>';
                                dom += '编码:' + (row.recruitCode == null ? '暂无' : row.recruitCode) + '</br>';
                                dom += '手机:' + (row.mobile == null ? '暂无' : row.mobile) + '</br>';

                                var dom2 = '';
                                for (var i = 0; i < row.jdIds.length; i++) {
                                    var jdId = row.jdIds[i];
                                    if (i == (row.jdIds.length - 1)) {
                                        dom2 += _findDict("jtId", jdId) + ' ';
                                        break;
                                    }
                                    dom2 += _findDict("jtId", jdId) + '; ';

                                }
                                if (row.jdIds.length < 1) {
                                    dom2 = '暂无';
                                }
                                dom += '职位:' + dom2 + '</br>';
                                return dom;
                            },
                            "sClass": 'text-l',
                            "targets": 2
                        },
                        {
                            "render": function (data, type, row, meta) {

                                if (row.dpName == null) {
                                    return "未分配部门";
                                } else {
                                    return row.dpName;
                                }

                            },
                            "targets": 3,
                            "class":"text-l"
                        },
                        {
                            "render": function (data, type, row, meta) {

                                if (row.groupName == null) {
                                    return "未分组";
                                } else {
                                    return row.groupName;
                                }

                            },
                            "targets": 4
                        },
                        {
                            "render": function (data, type, row, meta) {

                                if (row.campusName == null) {
                                    return "未分配校区";
                                } else {
                                    return row.campusName;
                                }

                            },
                            "targets": 6
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var empType = "未知类型";
                                for (var i = 0; i < dictJson.empType.length; i++) {
                                    if (row.empType == dictJson.empType[i].dictValue) {
                                        empType = dictJson.empType[i].dictName;
                                    }

                                }
                                return empType;
                            },
                            "targets": 5
                        },
                        {
                            "render": function (data, type, row, meta) {
                                var empStatus = "未知状态";
                                for (var i = 0; i < dictJson.empStatus.length; i++) {
                                    if (row.empStatus == dictJson.empStatus[i].dictValue) {
                                        empStatus = dictJson.empStatus[i].dictName;
                                    }

                                }
                                return empStatus;
                            },
                            "targets": 7
                        },
                        {
                            "render": function (data, type, row, meta) {

                                if (row.personnelIsAffirm == '1') {
                                    return "是";
                                } else {
                                    return "否";
                                }

                            },
                            "targets": 8

                        },
                        {
                            "render": function (data, type, row, meta) {

                                if (row.isFormalEmp == null) {
                                    return "否";
                                } else {
                                    return row.isFormalEmp;
                                }

                            },
                            "targets": 9

                        },
                        {
                            "render": function (data, type, row, meta) {
                                var dom = '';

                                dom += '<a title="编辑" href="javascript:void(0)" onclick="recruiter_edit(\'' + row.empId + '\')" class="ml-5" style="text-decoration:none">';
                                dom += '<i class="Hui-iconfont">&#xe6df;</i></a>';

                                return dom;
                            },
                            //指定是第三列
                            "targets": 10
                        }]
                });

        });


        /*管理员-招生老师-添加*/
        function recruiter_add() {
            var url = '/recruiter/toEdit.do' + '?exType=Add';
            layer_show('添加招生老师', url, null, null, function () {
                myDataTable.fnDraw(true);
            }, true);
        }
        /*管理员-招生老师-编辑*/
        function recruiter_edit(empId) {
            var url = '/recruiter/toEdit.do' + '?empId=' + empId + '&exType=UPDATE';
            layer_show('修改招生老师', url, null, null, function () {
                myDataTable.fnDraw(false);
            }, true);
        }

        function searchRecruiter() {
            myDataTable.fnDraw(true);
        }
        function excel_import() {
            var url = '/recruiter/excelImport.do';
            layer_show('导入教师信息', url, null, 510, function () {
                myDataTable.fnDraw(true);
            });
        }

        function delAll() {
            var chk_value = [];
            $("input[name=empIds]:checked").each(function () {
                chk_value.push($(this).val());
            });
            if (chk_value.length == 0) {
                layer.msg('请选择要删除的数据！', {
                    icon: 2,
                    time: 2000
                });
                return;
            }
            layer.confirm('确认要删除吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/recruiter/deleteRecruiter.do',
                    data: {
                        idArray: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        layer.msg('已删除!', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(true);
                        $("input[name=all]").attr("checked", false);
                    },
                    error: function (data) {
                        layer.msg('删除失败！', {
                            icon: 1,
                            time: 1000
                        });
                        myDataTable.fnDraw(true);
                        $("input[name=all]").attr("checked", false);
                    },
                });
            });
        }