var myDataTable;
$(function() {

    myDataTable = $('.table-sort').dataTable(
            {
                "processing": true,
                "serverSide" : true,
                "dom" : 'rtilp',
                "ajax" : {
                    url : "/whitelist/getStudents.do",
                    type : "post",
                    data : function(pageData) {
                        return searchData(pageData);
                    }
                },
                "pageLength" : 10,
                "pagingType" : "full_numbers",
                "ordering" : false,
                "searching" : false,
                "lengthMenu" : [ 10, 20 ],
                "createdRow" : function(row, data, dataIndex) {
                    $(row).addClass('text-c');
                },
                "language" : _my_datatables_language,
                columns : [ {
                    "mData" : null
                }, {
                    "mData" : "stdName"
                }, {
                    "mData" : null
                }, {
                    "mData" : "idCard"
                }, {
                    "mData" : null
                }, {
                    "mData" : "mobile"
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
                                return '<input type="checkbox" name="stdIds" value="' + row.stdId + '" />'
                            },
                            "targets" : 0
                        },
                        {
                            "render" : function(data, type, row, meta) {
                                return _findDict('idType', row.idType);
                            },
                            "targets" : 2
                        },
                        {
                            "render" : function(data, type, row, meta) {
                                var learnList = row.learnList;
                                return recruitTable(learnList);
                            },
                            "targets" : 4
                        },{
                        "render" : function(data, type, row, meta) {
//                                        console.log(row.createTime);
                            if(!row.createTime){
                                return '-'
                            }
                            var date=row.createTime.substring(0,10)
                            var time=row.createTime.substring(11)
//
                            return date+"<br>"+time
                        },
                        "targets" : 6,"class":"text-l"
                       },
                        {
                            "render" : function(data, type, row, meta) {
								var whitelist = row.whitelist;
								if(whitelist) {
								    var dom = '';
								    $.each(whitelist, function(index, s) {
								        dom += _findDict('scholarship', s) + ',';
								    });
								    
								    if(dom && dom.length > 0) {
								        dom = dom.substring(0, dom.length - 1);
								    }
								    return dom;
								} else {
								    return "无";
								}
                            },
                            "targets" : 7
                        },
                        {
                            "render" : function(data, type, row, meta) {
                                var dom = '<a title="白名单设置" href="javascript:;" onclick="toEidt(\'' + row.stdId + '\')" class="ml-5 tableBtn normal" >白名单设置</a>';
                                return dom;
                            },
                            //指定是第三列
                            "targets" : 8
                        } ]
            });
});

/*用户-编辑*/
function toEidt(stdId) {
    var url = '/whitelist/toSetting.do' + '?stdIds=' + stdId;
    layer_show('白名单设置', url, 850, 500, function() {
        myDataTable.fnDraw(false);
    });
}

function setBatch() {
    var stdIds = '';
    
    $.each($("input[name='stdIds']:checked"), function(index){
       var stdId = $(this).val();
       
       stdIds += stdId + ',';
    });
    
    if(stdIds && stdIds.length > 0) {
        stdIds = stdIds.substring(0, stdIds.length - 1);
	} else {
	    layer.msg('请选择学员', {
          icon : 5,
          time : 1000
      	});
	    return;
	}
    
    var url = '/whitelist/toSetting.do' + '?stdIds=' + stdIds;
    layer_show('白名单设置', url, 850, 500, function() {
        myDataTable.fnDraw(false);
    });
}

function _search() {
    myDataTable.fnDraw(true);
}

function searchData(pageData) {
    return {
        stdName : $("#stdName").val() ? $("#stdName").val() : '',
        idCard : $("#idCard").val() ? $("#idCard").val() : '',
        mobile : $("#mobile").val() ? $("#mobile").val() : '',
        start : pageData.start,
        length : pageData.length
    };
}

function recruitTable(learnList) {
    
	var dom = '<table class="table table-border table-bordered table-hover table-bg table-sort">';
	dom += '<thead>';
	dom += '<tr class="text-c">';
	dom += '<th width="40" class="td-s">年级</th>';
	dom += '<th width="120" class="text-l td-s">院校专业信息</th>';
	dom += '<th width="40" class="td-s">学员阶段</th>';
	dom += '<th width="40" class="td-s">是否结业</th>';
	dom += '</tr>';
	dom += '</thead>';
	dom += '<tbody>';
	if(learnList && learnList.length > 0) {
	    $.each(learnList, function(index, data){
		    var recruitType = data.recruitType;
            var scholarship = data.scholarship;
            var unvsName = data.unvsName;
            var pfsnLevel = data.pfsnLevel;
            var pfsnName = data.pfsnName;
            var grade = data.grade;
            var isCompleted = data.isCompleted;
            var pfsnCode = data.pfsnCode;
            var stdStage = data.stdStage;
            
            var text = "";
            if(_findDict("recruitType", recruitType)){
                _findDict("recruitType", recruitType).indexOf("成人")!=-1?text+="[成教]":text+="[国开]";
            }else {
                text +='[无]';
            }

            text += unvsName||'无';
            text+="<br>";
            if(_findDict("pfsnLevel", pfsnLevel)){
                _findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1?text+="[专科]":text+="[本科]";
            }else {
                text +='[无]';
            }

            text += pfsnName||'无';
            text+="(" ;
            text+= pfsnCode||'无';
            text+=")";

            var complete = '';
            
            if('1' == isCompleted) {
                complete = '<span class="label label-success radius ml-5">是</span>';
            } else {
                complete = '<span class="label label-default radius ml-5">否</span>';
            }
		    
			dom += '<tr>';
			dom += '<td class="td-s">' + _findDict('grade', grade) + '</td>';
			dom += '<td class="text-l td-s">' + text + '</td>';
			dom += '<td class="td-s">' + _findDict('stdStage', stdStage) + '</td>';
			dom += '<td class="td-s">' + complete + '</td>';
			dom += '</tr>';
	    });
	} else {
	    dom += '<tr class="text-c"><td valign="top" colspan="8" class="dataTables_empty td-s"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有学业信息</div></td></tr>';
	}
	
	dom += '</tbody>';
	dom += '</table>';
	
	return dom;
}