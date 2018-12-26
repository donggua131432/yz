 var unSelectedTable;
            $(function() {

                if (selectedList && selectedList.length > 0) {
                    $.each(selectedList, function(index, data) {
                        var unvsName = data.unvsName;
                        var recuritType = _findDict('recruitType', data.recruitType);
                        var pfsnName = data.pfsnName;
                        var pfsnLevel = _findDict('pfsnLevel', data.pfsnLevel);
                        var grade = _findDict('grade', data.grade);
                        var thpName = data.thpName;
                        var thpType = _findDict('thpType', data.thpType);
                        var semester = _findDict('semester', data.semester);
                        var thpId = data.thpId;

                        var dom = '<tr class="text-c">';
                        dom += '<td><input type="checkbox" name="thpIds" value="' + thpId + '"></td>';
                        dom += '<td class="text-l">' + grade + '-' + thpName + '</td>';
                        dom += '<td>' + thpType + '</td>';
                        dom += '<td class="text-l">';
                        if(recuritType){
                            dom +=  recuritType.indexOf("成人")!=-1?"[成教]":"[国开]";
                        }
                        dom+=unvsName?unvsName+'<br>':'-/<br>'
                        if(pfsnLevel){
                            dom +=  recuritType.indexOf("高中")!=-1?"[专科]":"[本科]";
                        }
                        dom+=grade?grade:'-/'
                        dom+=pfsnName?pfsnName:'-/'
                        dom+='</td>'
                        dom += '<td><a title="删除" href="javascript:;" onclick="tp_delete(this)" class="ml-5" style="text-decoration: none"><i class="iconfont icon-shanchu"></i></a></td>';
                        dom += '</tr>';
                        $("#selectedTable").find("tbody").append(dom);
                    });
                } else {
                    var dom = "<tr class='text-c'>";
                    dom += "<span>尚未选择教学计划</span>";
                    dom += "</tr>";
                    $("#selectedTable").find("tbody").append(dom);
                }

                //_init_select("recruitType", dictJson.recruitType);
                _init_select("pfsnLevel", dictJson.pfsnLevel);
                _init_select("semester",dictJson.semester);
                _init_select("grade", dictJson.grade);
                
                //初始化院校名称下拉框
	            _simple_ajax_select({
	                selectId: "unvsId",
	                searchUrl: "/bdUniversity/findAllKeyValue.do",
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

            	
            	
            	_simple_ajax_select({
            		selectId : "pfsnId",
            		searchUrl : "/baseinfo/sPfsn.do",
            		sData : {
            			sId :  function(){
    						return $("#unvsId").val() ? $("#unvsId").val() : '';	
    					},
    					ext1 : function(){
    						return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
    					},
    					ext2 : function(){
    						return $("#grade").val() ? $("#grade").val() : '';
    					}
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
            	
            	
                unSelectedTable = $('#unSelectedTable').dataTable(
                        {
                            "serverSide" : true,
                            "dom" : 'rtilp',
                            "ajax" : {
                                url : "/course/getTpSelectInfo.do",
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
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            }, {
                                "mData" : null
                            } ],
                            "columnDefs" : [
                                    {
                                        "render" : function(data, type, row, meta) {
                                            return '<input type="checkbox" name="thpIds" value="' + row.thpId + '">';
                                        },
                                        "targets" : 0
                                    },
                                    {
                                        "render" : function(data, type, row, meta) {
                                            var dom=''
//                                            dom+= '[' + _findDict("recruitType", row.recruitType) + ']-' + row.unvsName + '_' + '[' + _findDict("pfsnLevel", row.pfsnLevel) + ']-'
//                                                    + _findDict("grade", row.grade) + '-' + row.pfsnName;
                                            if(_findDict("recruitType", row.recruitType)){
                                                dom +=  _findDict("recruitType", row.recruitType).indexOf("成人")!=-1?"[成教]":"[国开]";
                                            }
                                            dom+=row.unvsName?row.unvsName+'<br>':'-/<br>'
                                            if(_findDict("pfsnLevel", row.pfsnLevel)){
                                                dom +=  _findDict("pfsnLevel", row.pfsnLevel).indexOf("高中")!=-1?"[专科]":"[本科]";
                                            }
                                            dom+=_findDict("grade", row.grade)?_findDict("grade", row.grade):'-/'
                                            dom+=row.pfsnName?row.pfsnName:'-/'
                                            return dom;
                                        },
                                        "targets" : 3,"class":"text-l"
                                    }, {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict("grade", row.grade) + '-' + row.thpName;
                                        },
                                        "targets" : 1,"class":"text-l"
                                    }, {
                                        "render" : function(data, type, row, meta) {
                                            return _findDict('thpType', row.thpType);
                                        },
                                        "targets" : 2
                                    }, {
                                        "render" : function(data, type, row, meta) {
                                            return '<a class="tableBtn normal" title="选择" onclick="tp_select(this)">选择</a>';
                                        },
                                        "targets" : 4
                                    } ],
                        });

                $("#bt_submit").click(function() {
                    parent.$("#thpTable").find("tbody").empty();
                    $.each($("#selectedTable").find("tbody tr"), function() {
                        $(this).find("td").last().empty();
                        $(this).find("td").last().append('<input type="button" class="submit btn btn-primary radius" value="删除" onclick="$(this).parent().parent().remove();" />');
                        parent.$("#thpTable").find("tbody").append(this);
                    });

                    layer_close();
                });

                $("#bt_cancel").click(function() {
                    layer_close();
                });

                $("#sAll").click(function() {
                    var checked = $(this).prop("checked");
                    if (checked) {
                        $("#selectedTable input[name='thpIds']").prop("checked", true);
                    } else {
                        $("#selectedTable input[name='thpIds']").prop("checked", false);
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
                dom.find("td").last().append('<a title="删除" href="javascript:;" onclick="tp_delete(this)" class="ml-5" style="text-decoration: none"><i class="iconfont icon-shanchu"></i></a>');
                $("#selectedTable").find("tbody").append(dom);
                $(":checked").removeAttr("checked");
                _search();
            }

            function _search() {
                unSelectedTable.fnDraw(true);
            }

            function searchData(pageData) {
                var selectedIds = '';
                $.each($("#selectedTable").find("input[name='thpIds']"), function(index) {
                    var thpId = $(this).val();
                    if (thpId) {
                        selectedIds += thpId + ',';
                    }
                });

                if (selectedIds && selectedIds.length > 0) {
                    selectedIds = selectedIds.substring(0, selectedIds.length - 1);
                }

                return {
                	grade : $("#grade").val() ? $("#grade").val() : '',	
                	unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
                	pfsnId : $("#pfsnId").val() ? $("#pfsnId").val() : '',
                	semester : $("#semester").val() ? $("#semester").val() : '',
                    pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
                    selectedThpId : selectedIds,
                    start : pageData.start,
                    length : pageData.length
                };
            }

            function addMore() {
                var thpIds = $("#unSelectedTable input[name='thpIds']:checked");
                if (thpIds && thpIds.length > 0) {
                    $.each($("#unSelectedTable input[name='thpIds']:checked"), function(index, data) {
                        var dom = $(this).parent().parent();
                        dom.find("td").last().empty();
                        dom.find("td").last().append('<a title="删除" href="javascript:;" onclick="tp_delete(this)" class="ml-5" style="text-decoration: none"><i class="iconfont icon-shanchu"></i></a>');
                        $("#selectedTable").find("tbody").append(dom);
                    });
                    $(":checked").removeAttr("checked");
                    _search();
                } else {
                    layer.msg('请选择需要添加的教学计划', {
                        icon : 2,
                        time : 2000
                    });
                }
            }

            function delMore() {
                var thpIds = $("#selectedTable input[name='thpIds']:checked");
                if (thpIds && thpIds.length > 0) {
                    $.each($("#selectedTable input[name='thpIds']:checked"), function(index, data) {
                        $(this).parent().parent().remove();
                    });
                    $(":checked").removeAttr("checked");
                    _search();
                } else {
                    layer.msg('请选择需要删除的教学计划', {
                        icon : 2,
                        time : 2000
                    });
                }
            }