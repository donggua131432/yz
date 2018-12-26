 var empListTable;
            $(function() {
                empListTable = $('#empListTable').dataTable(
                        {
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/recruit/getEmpList.do",
                                type : "post",
                                data : function(data) {
                                    return searchData(data);
                                }
                            },
                            "pageLength" : 10,
                            "pagingType" : "full_numbers",
                            "ordering" : false,
                            "searching" : false,
                            "lengthMenu" : [ 10, 20 ],
                            "language" : _my_datatables_language,
                            "createdRow" : function(row, data, dataIndex) {
                                $(row).addClass('text-c');
                            },
                            "columns" : [ {
                                "mData" : "empName"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : "mobile"
                            }, {
                                "mData" : null
                            }, {
                                "mData" : "campusName"
                            }, {
                                "mData" : "dpName"
                            }, {
                                "mData" : "groupName"
                            }, {
                                "mData" : null
                            } ],
                            "columnDefs" : [
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom = '';
                                            dom += '<div>'
                                            dom += '<input type="button" class="submit btn btn-primary radius" value="选择" onclick="ditribution(\'' + row.empId + '\',\''
                                                    + row.campusId + '\',\'' + row.dpId + '\',\'' + row.empName + '\',\'' + row.campusName + '\',\'' + row.dpName + '\',\''
                                                    + row.campusManager + '\',\'' + row.empStatus + '\');" />';
                                            dom += '</div>';

                                            return dom;
                                        },
                                        "targets" : 7
                                    }, {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("jobTitle", row.jtId);
                                        },
                                        "targets" : 1
                                    }, {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("empStatus", row.empStatus);
                                        },
                                        "targets" : 3
                                    } ],

                        });

            });
            function searchData(pageData) {
                return {
                    campus : $("#campus").val() ? $("#campus").val() : '',
                    department : $("#department").val() ? $("#department").val() : '',
                    group : $("#group").val() ? $("#group").val() : '',
                    mobile : $("#mobile").val() ? $("#mobile").val() : '',
                    jtIds : $("#jtIds").val() ? $("#jtIds").val() : '',
                    start : pageData.start,
                    length : pageData.length
                };
            }

            function _search() {
                empListTable.fnDraw(true);
            }

            function ditribution(empId, campusId, dpId, empName, campusName, dpName, campusManager, empStatus) {

                layer.confirm('确定分配?', function() {
                    var empInfoName = '';
                    if (empName) {
                        if (campusName && 'null' != campusName) {
                            empInfoName += '[' + campusName + '] — '
                        }
                        if (dpName && 'null' != dpName) {
                            empInfoName += '[' + dpName + '] — '
                        }
                        empInfoName += empName + '(' + _findDict('empStatus', empStatus) + ')';
                    } else {
                        empInfoName = '无';
                    }

                    var submitData = {
                        learnId : $("#learnId").val()
                    };
                    if ("ZSLS" == $("#jtIds").val()) {
                        submitData.recruit = empId;
                        submitData.recruitDpId = dpId;
                        submitData.recruitCampusId = campusId;
                        submitData.recruitCampusManager = campusManager;

                        parent.$("#recruitName").text(empInfoName);

                    } else {
                        submitData.tutor = empId;
                        submitData.tutorDpId = dpId;
                        submitData.tutorCampusId = campusId;
                        submitData.tutorCampusManager = campusManager;

                        parent.$("#tutorName").text(empInfoName);
                    }

                    $.ajax({
                        url : '/recruit/distribution.do',
                        dataType : 'json',
                        type : 'post',
                        data : submitData,
                        success : function(data) {
                            if (data.code == _GLOBAL_SUCCESS) {
                                layer.msg('分配成功', {
                                    icon : 1,
                                    time : 1000
                                }, function() {
                                    layer_close();
                                });
                            }
                        }
                    });
                });
            }