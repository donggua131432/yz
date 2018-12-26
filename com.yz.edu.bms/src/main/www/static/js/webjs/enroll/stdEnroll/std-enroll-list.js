//报考院校
_simple_ajax_select({
    selectId: "unvsIdStay",
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
$("#unvsIdStay").append(new Option("", "", false, true));
//专业
$("#unvsIdStay").change(function () {
    $("#pfsnIdStay").removeAttr("disabled");
    init_pfsn_select_stay();
});
$("#gradeStay").change(function () {
    $("#pfsnIdStay").removeAttr("disabled");
    init_pfsn_select_stay();
});
$("#pfsnLevelStay").change(function () {
    $("#pfsnIdStay").removeAttr("disabled");
    init_pfsn_select_stay();
});
$("#pfsnIdStay").append(new Option("", "", false, true));
$("#pfsnIdStay").select2({
    placeholder: "--请先选择院校--"
});
//招生部门
_init_campus_select("campusIdStay", "dpIdStay", "groupIdStay", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');

//根据数据字典初始化下拉框
/*_init_select('recruitTypeStay', dictJson.recruitType);*/
_init_select('stdStageStay', dictJson.stdStage);
_init_select('recruitStatusStay', dictJson.empStatus);
_init_select('gradeStay', dictJson.grade);
_init_select('scholarshipStay', dictJson.scholarship);
_init_select('pfsnLevelStay', dictJson.pfsnLevel);
_init_select("stdTypeStay", dictJson.stdType);

//是否已报名确认
_init_select("isOkStay", [
    {"dictValue": "0", "dictName": "未确认"},
    {"dictValue": "1", "dictName": "已确认"}
]);
//是否有考生号
_init_select("isExamNoStay", [
    {"dictValue": "1", "dictName": "有"},
    {"dictValue": "0", "dictName": "无"}
]);
//是否已参加考试签到
_init_select("isExamSignStay", [
    {"dictValue": "0", "dictName": "未签到"},
    {"dictValue": "1", "dictName": "已签到"}
]);
//有无成绩
_init_select("isScoreStay", [
    {"dictValue": "0", "dictName": "无"},
    {"dictValue": "1", "dictName": "有"}
]);
//有无加分
_init_select("isAddScoreStay", [
    {"dictValue": "0", "dictName": "无"},
    {"dictValue": "1", "dictName": "有"}
]);
//---------------------------------------------------------------------------------------------------------------

//报考院校
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
//录取院校
_simple_ajax_select({
    selectId: "unvsIdAdmit",
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
$("#unvsIdAdmit").append(new Option("", "", false, true));
//专业
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

_simple_ajax_select({
    selectId: "pfsnIdAdmit",
    searchUrl: '/unvsProfession/findAllKeyValue.do',
    sData: {},
    showText: function (item) {
        return "(" + item.pfsn_code + ")" + item.pfsn_name;
    },
    showId: function (item) {
        return item.pfsn_id;
    },
    placeholder: '--请选择专业--'
});
$("#pfsnIdAdmit").append(new Option("", "", false, true));
//招生部门
_init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');

//根据数据字典初始化下拉框
/*_init_select('recruitType', dictJson.recruitType);*/
_init_select('stdStage', dictJson.stdStage);
_init_select('recruitStatus', dictJson.empStatus);
_init_select('grade', dictJson.grade);
_init_select('scholarship', dictJson.scholarship);
_init_select('pfsnLevel', dictJson.pfsnLevel);
_init_select("stdType", dictJson.stdType);

//是否已报名确认
_init_select("isOk", [
    {"dictValue": "0", "dictName": "未确认"},
    {"dictValue": "1", "dictName": "已确认"}
]);
//是否有考生号
_init_select("isExamNo", [
    {"dictValue": "1", "dictName": "有"},
    {"dictValue": "0", "dictName": "无"}
]);
//是否已参加考试签到
_init_select("isExamSign", [
    {"dictValue": "0", "dictName": "未签到"},
    {"dictValue": "1", "dictName": "已签到"}
]);
//录取状态
_init_select("isAdmit", [
    {"dictValue": "1", "dictName": "已录取"},
    {"dictValue": "2", "dictName": "未录取"}
]);
//有无成绩
_init_select("isScore", [
    {"dictValue": "0", "dictName": "无"},
    {"dictValue": "1", "dictName": "有"}
]);
//有无加分
_init_select("isAddScore", [
    {"dictValue": "0", "dictName": "无"},
    {"dictValue": "1", "dictName": "有"}
]);
//录入与报读是否一致
_init_select("isAdmitEnroll", [
    {"dictValue": "0", "dictName": "否"},
    {"dictValue": "1", "dictName": "是"}
]);
$.ajax({
    type: "POST",
    dataType: "json", //数据类型
    url: '/stdEnroll/getHomeCampusInfo.do?isStop=',
    success: function (data) {
        var campusJson = data.body;
        if (data.code == '00') {
            _init_select("homeCampusId", campusJson);
        }
    }
});
_init_select("ifCampusId", [
    {"dictValue": "0", "dictName": "未分配"},
    {"dictValue": "1", "dictName": "已分配"}
]);

var myEnrollDataTable;
var myEnrolledDataTable;

//标签块
$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
$(function() {
    //初始化数据表格
    myEnrollDataTable = $('#enrollTable').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/stdEnroll/list.do",
            type : "post",
            data : function(data){
                data = $.extend({},{start:data.start, length:data.length},$("#enrollForm").serializeObject());
                return data;
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
        columns : [
            {"mData" : null},
            {"mData" : "stdName"},
            {"mData" : null},
            {"mData" : null},
            {"mData" : "recruit"},
            {"mData" : null},
            {"mData" : null},
            {"mData" : "ticketNo"},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null}
        ],
        "columnDefs" : [
            {"targets" : 0,"render" : function(data, type, row, meta) {
                return '<input type="checkbox" value="'+ row.learnId + '" name="learnIds"/>';
            }},
            {"targets" : 2,"render" : function(data, type, row, meta) {
                return _findDict("recruitType",row.recruitType);
            }},
            {"targets" : 3,"render" : function(data, type, row, meta) {
                return _findDict("grade",row.grade);
            }},
            {"targets" : 5,"class":"text-l","render" : function(data, type, row, meta) {
                var dom = '';
                dom += row.unvsName+"<br/>";
                if(_findDict("pfsnLevel",row.pfsnLevel).indexOf("高中")!=-1){
                    dom += "[专科]";
                }else {
                    dom += "[本科]";
                }
                dom += row.pfsnName+row.pfsnCode;
                return dom;
            }},
            {"targets" : 6,"render" : function(data, type, row, meta) {
                return _findDict("stdStage",row.stdStage);
            }},
            {"targets" : 8,"render" : function(data, type, row, meta) {
                var dom = '';
                for (var i = 0; i < row.scores.length; i++) {
                    var score = row.scores[i];
                    dom += score.courseName + ':';
                    dom += score.score + '</br>';
                }
                return dom;
            }},
            {"targets" : 9,"render" : function(data, type, row, meta) {
                var amount = 0;
                for (var i = 0; i < row.scores.length; i++) {
                    var score = row.scores[i];
                    amount += parseFloat(score.score);
                }
                return amount;
            }},
            {"targets" : 10,"render" : function(data, type, row, meta) {
                var amount = 0;
                if(null != row.points){
                    amount = row.points;
                }
                return amount;
            }},
            {"targets" : 11,"render" : function(data, type, row, meta) {
                var amount = 0;
                for (var i = 0; i < row.scores.length; i++) {
                    var score = row.scores[i];
                    amount += parseFloat(score.score);
                }
                if(null != row.points){
                    amount += parseFloat(row.points);
                }
                return amount;
            }},
            {"targets" : 12,"render" : function(data, type, row, meta) {
                var dom = '';
                dom += '<a title="录取学员" href="javascript:void(0)" onclick="enrrolStd(\'' + row.learnId + '\',\'ENROLL\')" class="ml-5" style="text-decoration:none">';
                dom += '<i class="iconfont icon-edit"></i></a>';
                return dom;
            }}
        ]
    });

    myEnrolledDataTable = $('#enrolledTable').dataTable({
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : "/stdEnroll/enrolledList.do",
            type : "post",
            data : function(data){
                data = $.extend({},{start:data.start, length:data.length},$("#enrolledForm").serializeObject());
                return data;
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
        columns : [
            {"mData" : null},
            {"mData" : "std_name"},
            {"mData" : null},
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
                return '<input type="checkbox" value="'+ row.learn_id + '" class="sendIdsCheckBox" name="enrolledLearnIds"/>';
            }},
            {"targets" : 2,"class":"text-l","render" : function(data, type, row, meta) {
                var dom = '';
                if (row.unvs_name != null) dom += row.unvs_name+"</br>";
                if (row.pfsn_level != null) {
                    if(_findDict("pfsnLevel", row.pfsn_level).indexOf("高中")!=-1){
                        dom += "[专科]";
                    }else {
                        dom += "[本科]";
                    }
                }
                if (row.pfsn_name != null) dom += row.pfsn_name;
                if (row.pfsn_code != null) dom += '('+row.pfsn_code+')';
                return dom;
            }},{
                "targets" : 3,"class":"text-l","render" : function(data, type, row, meta) {
                    var dom = '';
                    if (row.unvs_name_admit != null) dom += row.unvs_name_admit+"</br>";
                    if (row.pfsn_level_admit != null) {
                        if(_findDict("pfsnLevel", row.pfsn_level_admit).indexOf("高中")!=-1){
                            dom += "[专科]";
                        }else {
                            dom += "[本科]";
                        }
                    }
                    if (row.pfsn_name_admit != null) dom += row.pfsn_name_admit;
                    if (row.pfsn_code_admit != null) dom += '('+row.pfsn_code_admit+')';
                    return dom;
            }},
            {"targets" : 4,"render" : function(data, type, row, meta) {
                return _findDict("stdStage",row.std_stage);
            }},
            {"targets" : 5,"render" : function(data, type, row, meta) {
                var dom = '';
                if('1' == row.firstOrderStatus){
                    dom += '<span>应:' + row.firstYearFee + '</span>';
                }else{
                    dom += _findDict("orderStatus",row.firstOrderStatus);
                }
                return dom;
            }},
            {"targets" : 6,"render" : function(data, type, row, meta) {
                var dom = '';
                var ctStatus = row.ct_status;
                if('2' == ctStatus){
                    dom +='已审核';
                }else if('3' == ctStatus){
                    dom +='待审核';
                }else{
                    dom +='';
                }
                return dom;
            }},
            {"targets" : 7,"render" : function(data, type, row, meta) {
                return row.ta_name;
            }},
            {"targets" : 8,"render" : function(data, type, row, meta) {
                return row.exam_no;
            }},
            {"targets" : 9,"render" : function(data, type, row, meta) {
                return row.campus_name;
            }},
            {"targets" : 10,"render" : function(data, type, row, meta) {
                var dom = '';
                var recruitType = row.recruit_type;
                if('2' == recruitType){
                    dom += '<a title="查看" href="javascript:void(0)" onclick="gk_check(\'' + row.learn_id + '\')" class="ml-5" style="text-decoration:none">';
                    dom += '<i class="iconfont icon-shenhe"></i></a>';
                }
                dom += '<a title="查看" href="javascript:void(0)" onclick="enrrolStd(\'' + row.learn_id + '\',\'UPDATE\')" class="ml-5" style="text-decoration:none">';
                dom += '<i class="iconfont icon-chakan"></i></a>';
                return dom;
            }}
        ]
    });

    $('.checkAll').on('click',function () {
        if($(this).prop("checked")){
            $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').each(function (i,e) {
                if(!$(e).prop("checked")){
                    $(e).prop('checked',true)
                }
            })
        }else {
            $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').prop('checked',false)
        }
    })
});


function searchEnroll() {
    myEnrollDataTable.fnDraw(true);
}

function searchEnrolled() {
    myEnrolledDataTable.fnDraw(true);
}

function enrrolStd(learnId, exType) {
    var url = '/stdEnroll/toEnroll.do' + '?learnId=' + learnId + '&exType=' + exType;
    layer_show('录取学员', url, null, 510, function () {
        myEnrollDataTable.fnDraw(false);
        myEnrolledDataTable.fnDraw(false);
    }, true);
}

/*function drawTable(type) {
    if (type == 1) {
        myEnrollDataTable.fnDraw(true);
    } else {
        myEnrolledDataTable.fnDraw(true);
    }
}*/

/* function enroll_import() {
    var url = '/stdEnroll/toExcelImport.do';
    layer_show('批量导入录取结果', url, null, 510, function() {
        myEnrollDataTable.fnDraw(true);
        myEnrolledDataTable.fnDraw(true);
    });
} */
/*用户-退学*/
var leanId, outName;

function student_out() {
    leanId = $("table input[name=learnIds]:checked:first").val();
    outName = $("table input[name=learnIds]:checked:first").parent().siblings('td').find("a.stuName").text();
    var url = '/studentOut/edit.do' + '?exType=ADD';
    layer_show('添加退学学员', url, null, 510, function () {
        myDataTable.fnDraw(true);
    });
}

function gk_check(learnId) {
    layer.confirm('确认审核吗？', function (index) {
        //此处请求后台程序，下方是成功后的前台处理……
        $.ajax({
            type: 'POST',
            url: '/stdEnroll/GKCheck.do',
            data: {
                learnId: learnId
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    myEnrolledDataTable.fnDraw(true);
                    layer.msg('审核成功!', {
                        icon: 6,
                        time: 1000
                    });
                }
            }
        });
    });
}

// 国开批量审核
var checksFlag = false;

function gk_checks() {
    var chk_value = [];
    var $input = $("input[name=enrolledLearnIds]:checked");
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

    layer.confirm('确认要批量审核通过吗？', function (index) {
        if (checksFlag) {
            return
        }
        checksFlag = true;
        $.ajax({
            type: 'POST',
            url: '/stdEnroll/GKChecks.do',
            data: {
                learnIds: chk_value
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    checksFlag = false;
                    layer.msg('审核成功!', {
                        icon: 1,
                        time: 1000
                    });
                    myEnrolledDataTable.fnDraw(true);
                    $("input[name=enrolledLearnIds]").attr("checked", false);
                }
            }
        });
    });
}

//筛选分配
function queryAllocation() {
    var grade = $("#grade").val();
    if (grade == '') {
        layer.msg('至少要选择一个年级!!!', {
            icon: 5,
            time: 3000
        });
        return;
    }
    var url = '/stdEnroll/toFpCampus.do';
    layer_show('批量分配归属校区', url, 500, 300, function () {
        myEnrolledDataTable.fnDraw(false);
    });
}

//查询分配
function selectAllocation() {
    var chk_value = [];
    $("input[name=enrolledLearnIds]:checked").each(function () {
        chk_value.push($(this).val());
    });
    if (chk_value.length == 0) {
        layer.msg('请选择要分配的学员数据！', {
            icon: 2,
            time: 2000
        });
        return;
    }
    var url = '/stdEnroll/toSelectFpCampus.do';
    layer_show('批量分配归属校区', url, 500, 300, function () {
        myEnrolledDataTable.fnDraw(false);
    });
}

function init_pfsn_select_stay() {
    _simple_ajax_select({
        selectId: "pfsnIdStay",
        searchUrl: '/baseinfo/sPfsn.do',
        sData: {
            sId: function () {
                return $("#unvsIdStay").val() ? $("#unvsIdStay").val() : '';
            },
            ext1: function () {
                return $("#pfsnLevelStay").val() ? $("#pfsnLevelStay").val() : '';
            },
            ext2: function () {
                return $("#gradeStay").val() ? $("#gradeStay").val() : '';
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
    $("#pfsnIdStay").append(new Option("", "", false, true));
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


//筛选录取
function queryAdmit() {
    var grade = $("#gradeStay").val();
    if (grade == '') {
        layer.msg('至少要选择一个年级!!!', {
            icon: 5,
            time: 3000
        });
        return;
    }

    var url = '/stdEnroll/queryBatchAdmit.do';
    layer.confirm('确认批量录取学员吗？', function (index) {
        $.ajax({
            type: 'POST',
            url: url,
            data: $.extend({}, $("#enrollForm").serializeObject()),
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    layer.msg('批量录取成功!', {
                        icon: 1,
                        time: 2000
                    });
                    myEnrollDataTable.fnDraw(false);
                    $("input[name=all]").attr("checked", false);
                }
            }
        });
    });
}

//勾选录取
function checkAdmit() {
    var chk_value = [];
    $("input[name=learnIds]:checked").each(function () {
        chk_value.push($(this).val());
    });
    if (chk_value.length == 0) {
        layer.msg('请选择要录取的学员数据！', {
            icon: 2,
            time: 2000
        });
        return;
    }
    var url = '/stdEnroll/checkBatchAdmit.do';
    layer.confirm('确认批量录取学员吗？', function (index) {
        $.ajax({
            type: 'POST',
            url: url,
            data: {
                learnIds: chk_value
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    layer.msg('批量录取成功!', {
                        icon: 1,
                        time: 2000
                    });
                    myEnrollDataTable.fnDraw(false);
                    $("input[name=all]").attr("checked", false);
                }
            }
        });
    });
}
		