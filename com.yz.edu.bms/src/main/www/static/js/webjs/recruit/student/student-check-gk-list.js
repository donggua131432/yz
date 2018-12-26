var myDataTable;
$(function () {
    var arr = [],barWidth;

    //如果是国开三审和终审才可以批量审核
    if(jtList.join("").indexOf("GKZL3")>-1 || jtList.join("").indexOf("GKZL4")>-1){
        $("#batchButton").show();
    }

    _init_select('myAnnexStatus', dictJson.myAnnexStatus);
    _init_select('scholarship', dictJson.scholarship);
    _init_select('grade', dictJson.grade);
    _init_select('sg', dictJson.sg); //优惠分组
    _init_select("pfsnLevel", dictJson.pfsnLevel);
    _init_select("inclusionStatus",dictJson.inclusionStatus);
    //初始化学员阶段下拉框
    _init_select("stdStage", dictJson.stdStage);
    $("#sg").change(function() { //联动
        _init_select({
            selectId : 'scholarship',
            ext1 : $(this).val()
        }, dictJson.scholarship);
    });
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

    $("#recruitType").change(function() {
        _init_select({
            selectId : 'grade',
            ext1 : $(this).val()
        }, dictJson.grade);

    });

    $("#isDataCompleted").select2({
        placeholder : "--请选择--",
        allowClear : true
    });

    $("#stdStage").select2({
        placeholder : "--请选择--",
        allowClear : true
    });

    //校区-部门-组 联动
    //_init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');


    _simple_ajax_select({
        selectId: "campusId",
        searchUrl: '/campus/selectList.do',
        sData: {},
        showId: function (item) {
            return item.campusId;
        },
        showText: function (item) {
            return item.campusName;
        },
        placeholder: '--请选择招生校区--'
    });

    _simple_ajax_select({
        selectId: "dpId",
        searchUrl: '/dep/selectList.do',
        sData: {},
        showId: function (item) {
            return item.dpId;
        },
        showText: function (item) {
            return item.dpName;
        },
        placeholder: '--请选择招生部门--'
    });

    _simple_ajax_select({
        selectId: "taId",
        searchUrl: '/testArea/findAllKeyValue.do',
        sData: {},
        showId: function (item) {
            return item.taId;
        },
        showText: function (item) {
            return item.taName;
        },
        placeholder: '--请选择考试区域--'
    });

    myDataTable = $('.table-sort').dataTable({
        "processing": true,
        "serverSide": true,
        "dom": 'rtilp',
        "ajax": {
            url: "/gkCheckInfo/findAllGkStudentList.do",
            type: "post",
            data : function(pageData) {
                pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
                return pageData;
            }
        },
        "pageLength": 10,
        "pagingType": "full_numbers",
        "ordering": false,
        "searching": false,
        "fnDrawCallback": function () {
            for (var i = 0; i < arr.length; i++) {
               var progress = new Progress({}, arr[i].id, {
                    step: arr[i].steps,
                    animation: true,
                    change: true,
                    isCheck: arr[i].isCheck
                });
                progress.init();
            }
        },
        "createdRow": function (row, data, dataIndex) {
            $(row).addClass('text-c');
        },
        "language": _my_datatables_language,
        columns: [
            {"mData": null},
            {"mData": null},
            {"mData": null},
            {"mData": null},
            {"mData": "campusName"},
            {"mData": "dpName"},
            {"mData": "recruitName"},
            {"mData": null},
            {"mData": null},
            {"mData": null},
            {"mData": null},
            {"mData": null}
        ],
        "columnDefs": [
            {"targets": 0,"render": function (data, type, row, meta) {
                return '<input type="checkbox" name="learnId" value="'+ row.learnId + '"  stdStage="' + row.stdStage + '" />';
            }},
            {"targets": 1,"render": function (data, type, row, meta) {
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
            {"targets": 2,"render": function (data, type, row, meta) {
                return _findDict("grade", row.grade);
            }},
            {"targets": 3,"render": function (data, type, row, meta) {
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
            {"targets": 7,"render": function (data, type, row, meta) {
                return _findDict("stdStage", row.stdStage);
            }},
            {"targets": 8, "class": "text-l","render": function (data, type, row, meta) {
                var isDataCompleted = row.isDataCompleted, dom = '';
                if ('1' == isDataCompleted) {
                    dom = '<i class="iconfont icon-queren icon-s success"></i>已完善';
                } else {
                    dom = '<i class="iconfont icon-s warning">i</i>未完善';
                }
                return dom;
            }},
            {"targets":9,"class":"text-l no-warp","render" : function(data, type, row, meta) {
                var myAnnexStatus = row.annexStatus, text = _findDict('myAnnexStatus', row.annexStatus);
                if ('3' == myAnnexStatus) {
                    text = '<i class="iconfont icon-queren icon-s success"></i>'+text;
                } else if ('4' == myAnnexStatus) {
                    text = '<i class="iconfont  icon-s warning">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                } else if ('2' == myAnnexStatus) {
                    text = '<i class="iconfont icon-s warning">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                } else if ('1' == myAnnexStatus) {
                    text = '<i class="iconfont icon-s warning">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                }
                return text;
            }},
            {"targets": 10, "class": "no-warp","render": function (data, type, row, meta) {

                var isCheck = true, checkRecordList = row.checkRecordList, step = 2, timestamp = uuid(), stepBar = "stepBar" + timestamp;
                    var width=parseInt(100/(checkRecordList.length+2))+'%';
                var dom = '<div id="' + stepBar + '" class="ui-stepBar-wrap">'
                    + '<div class="ui-stepBar"><div class="ui-stepProcess"></div></div>'
                    + '<div class="ui-stepInfo-wrap"><table class="ui-stepLayout" border="0" cellpadding="0" cellspacing="0"><tr >'
                    + '<td class="ui-stepInfo"  style="border: 0px;width: '+width+'!important;">'
                    + '<a class="ui-stepSequence"><i class="iconfont icon-queren"></i></a>'
                    + '<p class="ui-stepName">提交:' + (row.createUser?row.createUser:"无") + '</p></td>';

                for (var i = 0; i < checkRecordList.length; i++) {
                    var empName = checkRecordList[i].updateUser == null ?  "无" : checkRecordList[i].updateUser,
                        strJtId = _findDict("jtId", checkRecordList[i].jtId), subJtId = strJtId.substring(strJtId.length-2,strJtId.length);
                    dom += '<td class="ui-stepInfo" style="border: 0px;width: '+width+'!important;">';
                    if (checkRecordList[i].crStatus == '4') {
                        dom += '<a class="ui-stepSequence" title="查看驳回原因" onclick="undoClick(\'' + checkRecordList[i].reason + '\')"><i class="iconfont icon-butongguo"></i></a>';
                        isCheck = false;
                    } else if (checkRecordList[i].crStatus == '3') {
                        dom += '<a class="ui-stepSequence"><i class="iconfont icon-queren"></i></a>';
                        step++;
                    }else{
                        dom += '<a class="ui-stepSequence"><i class="iconfont icon-queren"></i></a>';
                    }
                    //审核人
                    dom += '<p class="ui-stepName">' + subJtId + ":" + empName + '</p></td>';
                }

                dom += '<td class="ui-stepInfo" style="border: 0px;width: '+width+'!important;"><a class="ui-stepSequence"><i class="iconfont icon-queren"></i></a>';
                dom += '<p class="ui-stepName">完成</p></td></tr></table></div></div>';
                arr.push({"steps": step, "isCheck": isCheck, "id": stepBar, "speed": 500});
                return dom;
            }},
            {"targets": 11,"render": function (data, type, row, meta) {
                var dom = '<a title="审核" href="javascript:;" onclick="showCheckPage(\'' + row.learnId + '\', \'' + row.stdId + '\', \'' + row.recruitType + '\')" ' +
                    'class="ml-5" style="text-decoration: none">审核</a>';
                return dom;
            }}
        ]
    });
});

function _search() {
    myDataTable.fnDraw(true);
}

function uuid() {
    return Math.random().toString(36).substring(3, 8)
}

function undoClick(reason){
    if(null == reason || reason == '' || 'null' == reason ){
        reason = "无";
    }
    layer.alert(reason);
}

function showCheckPage(learnId, stdId, recruitType) {
    var url = '/annexCheck/toCheckEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId + '&recruitType=' + recruitType;
    layer_show('考前资料核查', url, null, null, function() {
        //myDataTable.fnDraw(true);
    }, true);
}

function init_pfsn_select() {
    _simple_ajax_select({
        selectId : "pfsnId",
        searchUrl : '/baseinfo/sPfsn.do',
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
}

//批量审核  只有学员阶段为【12】待录取，【5】已录取的学员才可以批量审核
var checksFlag = false; //防止重复提交
function batchChecks() {
    var chk_value = [], $input = $("input[name=learnId]:checked"), stdStageFlage=true, jtIdStr=jtList.join(",");
    $input.each(function () {
        chk_value.push($(this).val());
        var stdStageVar = $(this).attr("stdStage");
        if(jtIdStr.indexOf("GKZL3")>-1 && jtIdStr.indexOf("GKZL4")>-1){
            if(stdStageVar !="12" && stdStageVar !="5") stdStageFlage = false;
        }else{
            if(jtIdStr.indexOf("GKZL3")>-1 && stdStageVar!='12') stdStageFlage = false;
            if(jtIdStr.indexOf("GKZL4")>-1 && stdStageVar!='5') stdStageFlage = false;
        }
    });

    if(!stdStageFlage){
        /*if(jtIdStr.indexOf("GKZL3")>-1){
            layer.msg('批量审核只能选择【待录取】状态学员数据！', {icon: 5, time: 3000});
        }
        if(jtIdStr.indexOf("GKZL4")>-1){
            layer.msg('批量审核只能选择【已录取】状态学员数据！', {icon: 5, time: 3000});
        }*/

        if(jtIdStr.indexOf("GKZL3")>-1 && jtIdStr.indexOf("GKZL4")>-1){
            layer.msg('批量审核只能选择【待录取，已录取】状态学员数据！', {icon: 5, time: 3000});
        }else{
            if(jtIdStr.indexOf("GKZL3")>-1) layer.msg('批量审核只能选择【待录取】状态学员数据！', {icon: 5, time: 3000});
            if(jtIdStr.indexOf("GKZL4")>-1) layer.msg('批量审核只能选择【已录取】状态学员数据！', {icon: 5, time: 3000});
        }
        return;
    }

    if (chk_value == null || chk_value.length <= 0) {
        layer.msg('未选择任何数据！', {icon: 5, time: 1000});
        return;
    }

    console.log(chk_value);
    layer.confirm('确认要批量审核通过吗？', function (index) {
        if(checksFlag) return;
        checksFlag = true;
        $.ajax({
            type: 'POST',
            url: '/gkCheckInfo/batchChecks.do',
            data: {
                learnIds: chk_value
            },
            dataType: 'json',
            success: function (data) {
                if (data.body == "SUCCESS") {
                    layer.msg('审核成功', {icon: 1, time: 3000});
                    myDataTable.fnDraw(true);
                }else{
                    layer.msg('审核失败【审核记录数据不完整，请初始化审核数据】', {icon: 5, time: 3000});
                }
                checksFlag = false;
                $("input[name=learnId]").attr("checked", false);
            }
        });
    });
}
