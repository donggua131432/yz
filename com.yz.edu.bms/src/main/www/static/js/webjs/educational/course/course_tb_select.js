var unSelectedTable;
            $(function() {

                if (selectedList && selectedList.length > 0) {
                    $.each(selectedList, function(index, data) {
                        var textBookName = data.textbookName;
                        var publisher = data.publisher;
						var textbookId = data.textbookId;
						
                        var dom = '<tr class="text-c">';
                        dom += '<td><input type="checkbox" name="textbookIds" value="' + textbookId + '"></td>';
                        dom += '<td class="text-l">' + textbookName + '</td>';
                        dom += '<td class="text-l">' + publisher + '</td>';
                        dom += '<td><a title="删除" href="javascript:;" onclick="tp_delete(this)" style="text-decoration: none"><i class="iconfont icon-shanchu"></i></a></td>';
                        dom += '</tr>';
                        $("#selectedTable").find("tbody").append(dom);
                    });
                } else {
                    var dom = "<tr class='text-c'>";
                    dom += "<span>尚未选择教材</span>";
                    dom += "</tr>";
                    $("#selectedTable").find("tbody").append(dom);
                }

                _init_select("recruitType", dictJson.recruitType);
                _init_select("pfsnLevel", dictJson.pfsnLevel);
                unSelectedTable = $('#unSelectedTable').dataTable(
                        {
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/course/getTbSelectInfo.do",
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
                                "mData" : null
                            }, {
                                "mData" : 'textbookName'
                            }, {
                                "mData" : 'publisher'
                            }, {
                                "mData" : null
                            }],
                            "columnDefs" : [
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return '<input type="checkbox" name="textbookIds" value="' + row.textbookId + '">';
                                        },
                                        "targets" : 0
                                    }, {"targets" : 1,"class":"text-l"},{"targets" : 2,"class":"text-l"},{
                                        "render" : function(data, type, row, meta) {
                                            return '<a class="tableBtn normal" title="选择" onclick="tp_select(this)">选择</a>';
                                        },
                                        "targets" : 3
                                    } ],
                        });

                $("#bt_submit").click(function() {
                    parent.$("#textBookTable").find("tbody").empty();
                    $.each($("#selectedTable").find("tbody tr"), function() {
                        $(this).find("td").last().empty();
                        $(this).find("td").last().append('<input type="button" class="submit btn btn-primary radius" value="删除" onclick="$(this).parent().parent().remove();" />');
                        parent.$("#textBookTable").find("tbody").append(this);
                    });

                    layer_close();
                });

                $("#bt_cancel").click(function() {
                    layer_close();
                });

                $("#sAll").click(function() {
                    var checked = $(this).prop("checked");
                    if (checked) {
                        $("#selectedTable input[name='textbookIds']").prop("checked", true);
                    } else {
                        $("#selectedTable input[name='textbookIds']").prop("checked", false);
                    }
                });

            });

            function tp_delete(bt) {
                $(bt).parent().parent().remove();
                $(":checked").removeAttr("checked");
                _search();
            }

            function tp_select(bt) {
                var dom = $(bt).parent().parent();
                dom.find("td").last().empty();
                dom.find("td").last().append('<a title="删除" href="javascript:;" onclick="tp_delete(this)" style="text-decoration: none"><i class="iconfont icon-shanchu"></i></a>');
                $("#selectedTable").find("tbody").append(dom);
                $(":checked").removeAttr("checked");
                _search();
            }

            function _search() {
                unSelectedTable.fnDraw(true);
            }

            function searchData(pageData) {
                var selectedIds = '';
                $.each($("#selectedTable").find("input[name='textbookIds']"), function(index) {
                    var tbId = $(this).val();
                    if (tbId) {
                        selectedIds += tbId + ',';
                    }
                });

                if (selectedIds && selectedIds.length > 0) {
                    selectedIds = selectedIds.substring(0, selectedIds.length - 1);
                }

                return {
                    textBookName : $("#textBookName").val() ? $("#textBookName").val() : '',
                    publisher : $("#publisher").val() ? $("#publisher").val() : '',
                    textBookType : 'FD',
                    selectedTbIds : selectedIds,
                    start : pageData.start,
                    length : pageData.length
                };
            }

            function addMore() {
                var textbookIds = $("#unSelectedTable input[name='textbookIds']:checked");
                if (textbookIds && textbookIds.length > 0) {
                    $.each($("#unSelectedTable input[name='textbookIds']:checked"), function(index, data) {
                        var dom = $(this).parent().parent();
                        dom.find("td").last().empty();
                        dom.find("td").last().append('<a title="删除" href="javascript:;" onclick="tp_delete(this)" style="text-decoration: none"><i class="iconfont icon-shanchu"></i></a>');
                        $("#selectedTable").find("tbody").append(dom);
                    });
                    $(":checked").removeAttr("checked");
                    _search();
                } else {
                    layer.msg('请选择需要添加的教材', {
                        icon : 2,
                        time : 2000
                    });
                }
            }

            function delMore() {
                var textbookIds = $("#selectedTable input[name='textbookIds']:checked");
                if (textbookIds && textbookIds.length > 0) {
                    $.each($("#selectedTable input[name='textbookIds']:checked"), function(index, data) {
                        $(this).parent().parent().remove();
                    });
                    $(":checked").removeAttr("checked");
                    _search();
                } else {
                    layer.msg('请选择需要删除的教材', {
                        icon : 2,
                        time : 2000
                    });
                }
            }