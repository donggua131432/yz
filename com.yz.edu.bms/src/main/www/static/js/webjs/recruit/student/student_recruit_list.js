var myDataTable;
$(function() {
    //是学校分配
    _init_select("assignFlag", [
        {"dictValue": "0", "dictName": "否"},
        {"dictValue": "1", "dictName": "是"}
    ]);
    _init_select('myAnnexStatus', dictJson.myAnnexStatus);
    _init_select('stdStage', dictJson.stdStage);
    _init_select('recruitType', dictJson.recruitType);
    _init_select('grade', dictJson.grade);
    _init_select('recruitStatus', dictJson.empStatus);
    //是否外校
    _init_select("stdType", dictJson.stdType);
    $("#recruitType").change(function () {
        _init_select({
            selectId: 'grade',
            ext1: $(this).val()
        }, dictJson.grade);
    });
    _init_select('sg', dictJson.sg); //优惠分组
    _init_select('scholarship', dictJson.scholarship);
    _init_select("inclusionStatus", dictJson.inclusionStatus);
    $("#sg").change(function () { //联动
        _init_select({
            selectId: 'scholarship',
            ext1: $(this).val()
        }, dictJson.scholarship);
    });
    $('select').select2({
        placeholder: "--请选择--",
        allowClear: true,
        width: "59%"
    });
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
        placeholder: '--请选择--'
    });
    $("#unvsId").append(new Option("", "", false, true));
    $("#unvsId").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#grade").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#pfsnLevel").change(function () {
        $("#pfsnId").removeAttr("disabled");
        init_pfsn_select();
    });
    $("#pfsnId").append(new Option("", "", false, true));
    $("#pfsnId").select2({
        placeholder: "--请先选择院校--"
    });
    myDataTable = $('.table-sort').dataTable({
        "processing": true,
        "serverSide": true,
        "dom": 'rtilp',
        "ajax": {
            url: "/recruit/findRecruitStudents.do",
            type: "post",
            data: function (pageData) {
                pageData = $.extend({}, {start: pageData.start,length: pageData.length}, $("#searchForm").serializeObject());
                return pageData;
            }
        },
        "pageLength": 10,
        "pagingType": "full_numbers",
        "ordering": false,
        "searching": false,
        "lengthMenu": [10, 20],
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "language" : _my_datatables_language,
        columns : [
          {"mData" : null},
          {"mData" : null},
          {"mData" : "grade"},
          {"mData" : null},
          {"mData" : null},
          {"mData" : "taName"},
          {"mData" : null},
          {"mData" : null},
          {"mData" : null},
          {"mData" : null},
          {"mData" : null},
          {"mData" : null},
          {"mData" : null},
          {"mData" : null}
        ],
        "columnDefs" : [
            {"targets" : 0,"render" : function(data, type, row, meta) {
                  return '<input type="checkbox" value="'+ row.learnId + '" name="learnId" stdId="' + row.stdId + '" />';
            }},
            {"targets" : 1,"render" : function(data, type, row, meta) {
                var dom = "<div class='name-mark-box'>"+'<p class="name-mark-name no-warp">'+row.stdName+'</p>';
                dom+='<p class="text-c">';
                if(row.stdType ==='2'){
                  dom += '<span class="name-mark mark-red">外</span>';
                }
                if(row.assignFlag === '1'){
                  dom += '<span class="name-mark mark-blue">分配</span>';
                }
                dom+="</p></div>";
                return dom;
            }},
            {"targets" : 3,"class" : "text-l","render" : function(data, type, row, meta) {
                var pfsnName = row.pfsnName,unvsName = row.unvsName,recruitType = row.recruitType,
                  pfsnCode = row.pfsnCode,pfsnLevel = row.pfsnLevel, text = '';
                if (recruitType) {
                    if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                        text += "[成教]";
                    }else {
                        text += "[国开]";
                    }
                }
                if (unvsName) text += unvsName + "<br/>";
                if (pfsnLevel) {
                    if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                        text += "[专科]";
                    }else {
                        text += "[本科]";
                    }
                }
                if (pfsnName) text += pfsnName;
                if (pfsnCode) text += "(" + pfsnCode + ")";
                return text ? text : '无';
            }},
            {"targets" : 4,"render" : function(data, type, row, meta) {
                return _findDict("stdStage", row.stdStage);
            }},
            {"targets" : 6,"render" : function(data, type, row, meta) {
                var scholarship= _findDict('scholarship', row.scholarship);
                if(row.inclusionStatus!=null && scholarship!=null ){
                    if(row.inclusionStatus == '4' || row.inclusionStatus=='5' || row.inclusionStatus=='6') {
                        return scholarship + "<br/>[" + _findDict('inclusionStatus', row.inclusionStatus) + "]";
                    }
                }
                return scholarship;
            }},
            {"targets" : 7, "class" : "text-l no-warp", "render" : function(data, type, row, meta) {
                var dom = "";
                if (row.counsellingFees) {
                    if ('2' == row.counsellingFeesStatus) {
                        dom += "<p class='tablePay-normal'>考前辅导费：" + row.counsellingFees + "</p>";
                    } else {
                        dom += "<p class='tablePay-normal'>考前辅导费：<span>" + row.counsellingFees + "</span></p>";
                    }
                }
                if (row.tuition) {
                    if ('2' == row.tuitionStatus) {
                        dom += "<p class='tablePay-normal'>第一年学费：" + row.tuition + "</p>";
                    } else {
                        dom += "<p class='tablePay-normal'>第一年学费：<span>" + row.tuition + "</span></p>";
                    }
                }
                dom += "<p class='tablePay-normal'>&nbsp;&nbsp;&nbsp;滞留账户：" + row.demurrageAccount + "</p>";
                return dom;
            }},
            {"targets" : 8,"render" : function(data, type, row, meta) {
                var counsellingFeesStatus = row.counsellingFeesStatus;
                if ('1' == counsellingFeesStatus) {
                    return "未缴费";
                } else {
                    var sendStatus = row.sendStatus;
                    if (sendStatus && sendStatus != 'null') {
                        return _findDict('receiveStatus', sendStatus);
                    }
                    return "发书记录缺失";
                }
            }},
            {"targets":9,"class":"text-l no-warp", "render":function(data, type, row, meta) {
                var isDataCompleted = row.isDataCompleted, dom = '', titleMsg="";
                if ('1' == isDataCompleted) {
                    dom = '<i class="iconfont icon-queren icon-s success"></i>已完善';
                    titleMsg="已完善";
                }else if('2' == isDataCompleted){
                    dom = '<i class="iconfont icon-s warning">i</i>已驳回';
                    titleMsg="驳回原因："+row.ext1;
                } else {
                    dom = '<i class="iconfont icon-s warning">i</i>未完善';
                    titleMsg="未完善";
                }
                var text = '<a title="'+titleMsg+'" href="javascript:void(0);" onclick="eidt(\'' + row.learnId + '\', \'' + row.stdId + '\', \'' + row.recruitType + '\')" style="text-decoration: none">';
                text += dom;
                text += '</a>';
                return text;
            }},
            {"targets":10,"class":"text-l no-warp","render" : function(data, type, row, meta) {
                var myAnnexStatus = row.myAnnexStatus;
                var text = _findDict('myAnnexStatus', row.myAnnexStatus);
                if ('3' == myAnnexStatus) {
                    text = '<i class="iconfont icon-queren icon-s success"></i>'+ text;
                } else if ('4' == myAnnexStatus) {
                    text = '<i class="iconfont  icon-s warning" style="cursor:pointer;">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                } else if ('2' == myAnnexStatus) {
                    text = '<i class="iconfont icon-s warning" style="cursor:pointer;">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                } else if ('1' == myAnnexStatus) {
                    text = '<i class="iconfont icon-s warning" style="cursor:pointer;">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                }
                var dom = '<a title="学员信息编辑" href="javascript:;" onclick="eidt(\'' + row.learnId + '\', \'' + row.stdId
                  + '\', \'annex\')" style="text-decoration: none">';
                dom += text;
                dom += '</a>';
                return dom;
            }},
            {"targets":11, "render" : function(data, type, row, meta) {
                var dom = row.recruitName;
                if(row.empStatus ==='2'){
                    dom += '<span class="name-mark-out">离</span>';
                }
                return dom;
            },"class":"no-warp"},
            { "targets" : 12,"class":"text-c","render" : function(data, type, row, meta) {
                var remark = row.remark ? row.remark : "", userId = row.userId ?　row.userId : "", dom='';
                if(!!remark){
                    dom='<a href="javascript:void(0);" class="remarkBox" style="text-decoration: none">'+remark+'<i title="修改备注" onclick="showEditRemark(\'' + userId + '\', \''+row.stdId+'\')" class="Hui-iconfont Hui-iconfont-shuru remarkIcon"></i></a>';
                }else {
                    dom = '<a title="添加备注" href="javascript:void(0);" onclick="showEditRemark(\'' + userId + '\', \''+row.stdId+'\')" style="text-decoration: none">';
                    dom += '<i class="iconfont icon-shenhe"></i></a>';
                }
                return dom;
            }},
            {"targets":13, "render" : function(data, type, row, meta) {
                var dom = '<a title="编辑" href="javascript:void(0);" onclick="eidt(\'' + row.learnId + '\', \'' + row.stdId + '\', \'' + row.recruitType + '\')" class="ml-5" style="text-decoration: none">';
                dom += '<i class="iconfont icon-edit"></i></a>';
                return dom;
            }}
        ]
    });
});

function showEditRemark(userId, stdId) {
    var url = '/invite_user/toEditRemark.do' + '?userId=' + userId + '&stdId=' + stdId;
    layer_show('修改备注', url, 550, 300, function () {
        // myDataTable.fnDraw(false);
    }, false);
}


/*用户-编辑*/
function eidt(learnId, stdId, recruitType) {
    var url = '/recruit/toRecruitEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId + '&recruitType=' + recruitType;
    layer_show('学员信息编辑', url, null, null, function () {
        //myDataTable.fnDraw(false);
    }, true);
}

var index;

function showAddPage() {
    index = layer.open({
        type: 1,
        area: ['400px', '200PX'],
        title: '选择招生类型',
        content: $('#recruit-type-box').html()
    });
}

function getRt(rt) {
    // if(rt==2){
    //     // 国开暂时停止录入
    //     layer.alert('1803级已停止招生，即将开放1809级，敬请期待！', {
    //         icon: 6,
    //     })
    //     return
    // }
    layer.close(index);
    var url = '/recruit/toRecruitAdd.do' + '?recruitType=' + rt;
    layer_show('新增我的学员', url, null, null, function () {
//					myDataTable.fnDraw(true);
    }, true);
}

function searchRecruitList() {
    myDataTable.fnDraw(true);
}

function changeRemark(remark) {
    var temp = $(remark).attr("isCompleted") == '1' ? '0' : '1';
    $.ajax({
        type: 'POST',
        url: '/recruit/changeRemark.do',
        data: {
            lrId: $(remark).attr("id"),
            isCompleted: temp
        },
        dataType: 'json',
        success: function (data) {
            if ('00' == data.code) {
                $(remark).attr("isCompleted", temp);
                if (temp == '1') {
                    $(remark).attr("class", "label label-success radius ml-5");
                } else {
                    $(remark).attr("class", "label label-default radius ml-5");
                }
            }
        },
    });
}

//退学申请
var leanId, outName;

function student_out() {
    leanId = $("table input[name=learnId]:checked:first").val();
    outName = $("table input[name=learnId]:checked:first").parent().siblings('td').find(".stuName").text();
    var url = '/studentOut/edit.do' + '?exType=ADD';
    layer_show('添加退学学员', url, null, 510, function () {
//					myDataTable.fnDraw(true);
    });
}

function myPerformance() {
    var url = '/performance/toMyList.do';
    layer_show('我的绩效', url, null, 510, function () {
        myDataTable.fnDraw(true);
    }, true);
}

function init_pfsn_select() {
    _simple_ajax_select({
        selectId: "pfsnId",
        searchUrl: '/baseinfo/sPfsn.do',
        sData: {
            sId: function () {
                return $("#unvsId").val() ? $("#unvsId").val() : '';
            },
            ext1: function () {
                return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
            },
            ext2: function () {
                return $("#grade").val() ? $("#grade").val() : '';
            }
        },
        showText: function (item) {
            var text = '(' + item.pfsnCode + ')' + item.pfsnName;
            text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
            return text;
        },
        showId: function (item) {
            return item.pfsnId;
        },
        placeholder: '--请选择专业--'
    });
    $("#pfsnId").append(new Option("", "", false, true));
}