$(function () {
    var checkData = checkData_global;
    if (checkData) {
        if(recruitType == '1'){
            $.each(checkData, function (index, item) {
                var checkStatus = _findDict('crStatus', item.crStatus),updateUser = item.updateUser, crId = item.crId,
                    updateTime = item.updateTime, dom = '<tr class="text-c odd">', jtId = item.jtId;
                dom += "<td>考前资料核查</td>";
                dom += "<td>" + (checkStatus ? checkStatus : '无') + "</td>";
                dom += "<td>" + (updateUser ? updateUser : '无')+ "</td>";
                dom += "<td>" + (updateUser ? (updateTime ? updateTime : '') : '无') + "</td><td>";

                if ('3' == item.crStatus) {
                    dom += '<a title="撤销" href="javascript:void(0);" onclick="_all_charge(0,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>撤销>>>&nbsp;</a>';
                } else {
                    dom += '<a title="通过" href="javascript:void(0);" onclick="_all_charge(1,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>通过>>>&nbsp;</a>';
                    dom += '<a title="驳回" href="javascript:void(0);" onclick="reclaimRmark(2,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>驳回>>>&nbsp;</a>';
                }
                dom += '</td></tr>';
                $("#checkBody").append(dom);
            });
        }else{
            initCheckRecordHtml(checkData);
        }
    }
});


function initCheckRecordHtml(data){
    var dom = '';
    $.each(data, function (index, item) {
        var checkStatus = _findDict('crStatus', item.crStatus),updateUser = item.updateUser,crId = item.crId,
            updateTime = item.updateTime, jtId = item.jtId;

        dom += '<tr class="text-c odd">';
        dom += "<td>" + _findDict('jtId', jtId) + "</td>";
        dom += "<td>" + (checkStatus ? checkStatus : '无') + "</td>";
        dom += "<td>" + (updateUser ? updateUser : '无')+ "</td>";
        dom += "<td>" + (updateUser ? (updateTime ? updateTime : '') : '无') + "</td><td>";

        index = index-1<0?0:index-1;
        if(jtIdList.join(",").indexOf(jtId)>-1){
            if ('3' ==  item.crStatus) {
                dom += jtId=='GKZL4' ? '<a title="撤销" href="javascript:void(0);" onclick="_all_charge(0,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>撤销>>>&nbsp;</a>' :'已审核';
            }else{
                dom += '<a title="通过" href="javascript:void(0);" onclick="_all_charge(1,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>通过>>>&nbsp;</a>';
                dom += '<a title="驳回" href="javascript:void(0);" onclick="reclaimRmark(2,\''+data[index].crId+'\',\''+data[index].jtId+'\',this)" class="ml-5">&nbsp;>>>驳回>>>&nbsp;</a>';
            }
        }else{
            dom += "";
        }
        dom += '</td></tr>';
    });

    if(data.length!=4){
        dom += '<tr class="text-c odd"><td valign="top" colspan="8" class="dataTables_empty" style="text-align:center;font:bold 13px/22px arial,sans-serif;">审核记录数据不完善！';
        dom += '<a href="javascript:void(0);" style="color:red;" onclick="initGkCheckRecord()">初始化审核数据</a></td></tr>';
    }
    $("#checkBody").html(dom);
}

//初始化审核记录数据
function initGkCheckRecord(){
    $.ajax({
        type: 'POST',
        url: '/gkCheckInfo/initGkCheckRecord.do',
        dataType: 'json',
        data: {
            learnId: learnId_global
        },
        success: function (rData) {
            if (_GLOBAL_SUCCESS == rData.code) {
                layer.msg('初始化审核记录数据成功！', {icon: 1, time: 3000});
                initCheckRecordHtml(rData.body);
            }else{
                layer.msg('初始化审核记录数据失败！', {icon: 1, time: 3000});
            }
        }
    })
}


//驳回备注
function reclaimRmark(type,crId,jtId,obj) {
    layer.open({
        type: 1,
        area: ['400px'],
        title: '请输入驳回原因',
        content: $('#reason-box').html(),
        btn: ['提交', '关闭'],
        btn1: function (index, layero) {
            var ext1 = $(layero).find('textarea').val();
            if (ext1 == "") {
                layer.msg('请填写驳回原因', {icon: 1, time: 1000})
            } else {
                _all_charge(type, crId, jtId, obj, ext1);
                layer.close(index);
            }
        }
    });
}

function _all_charge(type, crId, jtId, obj, ext1) {
    $.ajax({
        type: 'POST',
        url: '/annexCheck/check.do',
        dataType: 'json',
        data: {
            crId: crId,
            learnId: learnId_global,
            recruitType: recruitType,
            isDataCheck: type,
            jtId:jtId,
            ext1: ext1
        },
        success: function (rData) {
            if (_GLOBAL_SUCCESS == rData.code) {
                var checkData = rData.body;
                layer.msg(('1' == type ? '审核成功' : ('2' == type ? '驳回成功' : '撤销成功')), {icon: 1, time: 1000}, function () {
                    var crStatus = _findDict('crStatus', checkData.crStatus),updateUser = checkData.updateUser,
                        updateTime = checkData.updateTime, crId = checkData.crId, dom = '';

                    if ('3' == checkData.crStatus) {
                        if(jtId!='GKZL4' && recruitType=='2'){
                            dom += '已审核';
                        }else{
                            dom += '<a title="撤销" href="javascript:void(0);" onclick="_all_charge(0,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>撤销>>>&nbsp;</a>';
                        }
                    } else {
                        dom += '<a title="通过" href="javascript:void(0);" onclick="_all_charge(1,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>通过>>>&nbsp;</a>';
                        dom += '<a title="驳回" href="javascript:void(0);" onclick="reclaimRmark(2,\''+crId+'\',\''+jtId+'\',this)" class="ml-5">&nbsp;>>>驳回>>>&nbsp;</a>';
                    }

                    var jqueryObj = "";
                    if(type=="2"){
                        jqueryObj = $(obj).parent().parent().prev().find("td");
                        if(jqueryObj.length==0){
                            jqueryObj = $(obj).parent().parent().find("td");
                        }
                    }else{
                        jqueryObj = $(obj).parent().parent().find("td");
                    }

                    jqueryObj.each(function(i){
                        if(i==1) $(this).text(crStatus);
                        if(i==2) $(this).text(updateUser);
                        if(i==3) $(this).text(updateTime);
                        if(i==4) $(this).html(dom);
                    });
                });
            }
        }
    });
}