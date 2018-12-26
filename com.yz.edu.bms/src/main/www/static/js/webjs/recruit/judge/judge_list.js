var myDataTable;
$(function() {
    
    _init_select('grade', dictJson.grade);
    _init_select('scholarship', scholarships);
    _init_select('scholarshipStatus', dictJson.scholarshipStatus);

    myDataTable = $('.table-sort').dataTable({
        "processing": true,
        "serverSide" : true,
        "dom" : 'rtilp',
        "ajax" : {
            url : '/judge/getList.do',
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
            "mData" : null
        }, {
            "mData" : null
        }, {
            "mData" : null
        }, {
            "mData" : "recruitName"
        }, {
            "mData" : null
        }, {
            "mData" : null
        } ],
        "columnDefs" : [ {
            "render" : function(data, type, row, meta) {
                return '<input type="checkbox" name="learnIds" value="' + row.learnId + '" />'
            },
            "targets" : 0
        }, {
            "render" : function(data, type, row, meta) {
                return studentInfo(row);
            },
            "className" : "text-l",
            "targets" : 1
        }, {
            "render" : function(data, type, row, meta) {
                return enrollInfo(row);
            },
            "className" : "text-l",
            "targets" : 2
        }, {
            "render" : function(data, type, row, meta) {
                return history(row);
            },
            "className" : "text-l",
            "targets" : 3
        }, {
            "render" : function(data, type, row, meta) {
                var scholarshipStatus = _findDict('scholarshipStatus', row.scholarshipStatus);
                return scholarshipStatus ? scholarshipStatus : '无';
            },
            "targets" : 5
        }, {
            "render" : function(data, type, row, meta) {
                var dom = '';
                dom = '<a title="审核" href="javascript:;" onclick="toEidt(\'' + row.learnId + '\')" class="ml-5" >审核</a>';
                return dom;
            },
            //指定是第三列
            "targets" : 6
        } ]
    });
});

/*用户-编辑*/
function toEidt(learnId) {
    var url = '/judge/toJudge.do?learnId=' + learnId;
    layer_show('圆梦审核', url, 850, 500, function() {
        _search();
    }, true);
}

function setBatch(type) {
    var learnIds = '';

    $.each($("input[name='learnIds']:checked"), function(index) {
        var learnId = $(this).val();

        learnIds += learnId + ',';
    });

    if (learnIds && learnIds.length > 0) {
        learnIds = learnIds.substring(0, learnIds.length - 1);
    } else {
        layer.msg('请选择学员', {
            icon : 5,
            time : 1000
        });
        return;
    }

    if ('1' == type) {
        layer.confirm('批量通过', function(index) {
            $.ajax({
                type : 'POST',
                url : '/judge/check.do',
                data : {
                    'learnIds' : learnIds,
                    'scholarshipStatus' : '2'
                },
                dataType : 'json',
                success : function(data) {
                    if (_GLOBAL_SUCCESS = data.code) {
                        layer.msg('审核成功', {
                            icon : 1,
                            time : 1000
                        }, function() {
                            layer.close(index);
                            _search();
                            $(":checked").prop("checked", false);
                        });
                    }
                }
            });

        });
    } else if ('2' == type) {
        layer.open({
            content : $("#judge-box").html(),
            btn : [ '确定', '取消' ],
            yes : function(index, layero) {
                var reason = $(layero).find("#refundReason").val();

                $.ajax({
                    type : 'POST',
                    url : '/judge/check.do',
                    data : {
                        'learnIds' : learnIds,
                        'scholarshipStatus' : '3',
                        'reason' : reason
                    },
                    dataType : 'json',
                    success : function(data) {
                        if (_GLOBAL_SUCCESS = data.code) {
                            layer.msg('审核成功', {
                                icon : 1,
                                time : 1000
                            }, function() {
                                layer.close(index);
                                _search();
                                $(":checked").prop("checked", false);
                            });
                        }
                    }
                });
            }
        });
    }
}

function _search() {
    myDataTable.fnDraw(true);
}

function searchData(pageData) {
    return {
        stdName : $("#stdName").val() ? $("#stdName").val() : '',
        idCard : $("#idCard").val() ? $("#idCard").val() : '',
        mobile : $("#mobile").val() ? $("#mobile").val() : '',
        unvsName : $("#unvsName").val() ? $("#unvsName").val() : '',
        pfsnName : $("#pfsnName").val() ? $("#pfsnName").val() : '',
        grade : $("#grade").val() ? $("#grade").val() : '',
        scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
        scholarshipStatus : $("#scholarshipStatus").val() ? $("#scholarshipStatus").val() : '',
        start : pageData.start,
        length : pageData.length
    };
}

function studentInfo(data) {
    var sex = _findDict('sex', data.sex) ? _findDict('sex', data.sex) : '无';
    var nation = _findDict('nation', data.nation) ? _findDict('nation', data.nation) : '无';
    var politicalStatus = _findDict('politicalStatus', data.politicalStatus) ? _findDict('politicalStatus', data.politicalStatus) : '无';
    var edcsType = _findDict('edcsType', data.edcsType) ? _findDict('edcsType', data.edcsType) : '无';

    var province = _findProvinceName(data.wpProvinceCode) ? _findProvinceName(data.wpProvinceCode) : '';
    var city = _findCityName(data.wpProvinceCode, data.wpCityCode) ? _findCityName(data.wpProvinceCode, data.wpCityCode) : '';
    var district = _findDistrictName(data.wpProvinceCode, data.wpCityCode, data.wpDistrictCode) ? _findDistrictName(data.wpProvinceCode, data.wpCityCode,
            data.wpDistrictCode) : '';

    var address = province + city + district + (data.wpAddress ? data.wpAddress : '');
    address = address ? address : '无';

    var dom = '<ul>';
    dom += '<li>姓名：' + data.stdName + '，性别：' + sex + '，民族：' + nation + '</li>';
    dom += '<li>身份证号码：' + data.idCard + '</li>';
    dom += '<li>政治面貌：' + politicalStatus + '</li>';
    dom += '<li>文化程度：' + edcsType + '</li>';
    dom += '<li>单位地址：' + address + '</li>';
    dom += '</ul>';

    return dom;
}

function enrollInfo(data) {
    var unvsName = data.unvsName ? data.unvsName : '无';
    var pfsnLevel = _findDict('pfsnLevel', data.pfsnLevel) ? _findDict('pfsnLevel', data.pfsnLevel) : '无';
    var pfsnName = data.pfsnName ? data.pfsnName : '无';
    var taName = data.taName ? data.taName : '无';
    var grade = _findDict('grade', data.grade) ? data.grade : '无';
    var enrollTime = data.enrollTime ? data.enrollTime : '无';
    var dom = '<ul>';
    dom += '<li>报考院校：' + unvsName + '</li>';
    dom += '<li>报考层次：' + pfsnLevel + '</li>';
    dom += '<li>报考专业：' + pfsnName + '</li>';
    dom += '<li>考试县区：' + taName + '</li>';
    dom += '<li>入学年度：' + grade + '</li>';
    dom += '<li>报名时间：' + enrollTime + '</li>';
    dom += '</ul>';

    return dom;
}

function history(data) {
    var hUnvsName = data.hUnvsName ? data.hUnvsName : '无';
    var hPfsnName = data.hPfsnName ? data.hPfsnName : '无';
    var graduateTime = data.graduateTime ? data.graduateTime : '无';
    var diploma = data.diploma ? data.diploma : '无';

    var province = _findProvinceName(data.hProvinceCode) ? _findProvinceName(data.hProvinceCode) : '';
    var city = _findCityName(data.hProvinceCode, data.hCityCode) ? _findCityName(data.hProvinceCode, data.hCityCode) : '';
    var district = _findDistrictName(data.hProvinceCode, data.hCityCode, data.hDistrictCode) ? _findDistrictName(data.hProvinceCode, data.hCityCode, data.hDistrictCode)
            : '';

    var address = province + city + district;
    address = address ? address : '无';

    var dom = '<ul>';
    dom += '<li>原毕业院校：' + hUnvsName + '</li>';
    dom += '<li>原毕业专业：' + hPfsnName + '</li>';
    dom += '<li>原毕业时间：' + graduateTime + '</li>';
    dom += '<li>原毕业证编号：' + diploma + '</li>';
    dom += '<li>原毕业院校所在省市：' + address + '</li>';
    dom += '</ul>';

    return dom;
}