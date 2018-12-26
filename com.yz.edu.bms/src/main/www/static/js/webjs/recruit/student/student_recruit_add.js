$(function () {
    //分步填写
    var current_fs, next_fs, previous_fs, animating, recruitType = $("#recruitType").val(), gradeData;

    $("fieldset[page='history']").hide();
    $("fieldset[page='enroll']").hide();

    $(".next").click(function () {

        console.log("aaaaaaaaaaa:"+animating);

        if (animating) {
            return false;
        }
        current_fs = $(this).parents('fieldset');
        next_fs = $(this).parents('fieldset').next();
        var pageName = $(next_fs).attr("page");
        if ('enroll' == pageName) {
            $("#pfsnLevel").rules("add", {required: true});
            $("#enrollType").rules("add", {required: true});
            $("#grade").rules("add", {
                remote: {
                    type: "POST",
                    url: '/recruit/validateBaseInfo.do',
                    data: {
                        idType: function () {
                            return $("#idType").val();
                        },
                        idCard: function () {
                            return $("#idCard").val();
                        },
                        grade: function () {
                            return $("#grade").val();
                        }
                    }
                },
                required: true,
                messages: {
                    remote: '学员已在该年级注册，请勿重复录入'
                }
            });
            $("#unvsId").rules("add", {required: true});
            $("#pfsnId").rules("add", {required: true});
            $("#taId").rules("add", {required: true});

            if (recruitType === '1') {
                $(":radio[name='bpType']").rules("add", {required: true});
                $("#points").rules("add", {required: true, isDigits: true});
            }

            $("#scholarship").rules("add", {
                remote: {
                    type: "POST",
                    url: '/recruit/validateRecruit.do',
                    data: {
                        stdId: function () {
                            return $("#stdId").val();
                        },
                        scholarship: function () {
                            return $("#scholarship").val();
                        }
                    }
                },
                required: true,
                messages: {
                    remote: '当前学员因为限制，无法报名相应类型的圆梦计划，请在白名单中添加'
                }
            });

            var dateStr = $("#birthdayInput").val();
            var bDate = new Date(dateStr);
            var now = new Date();
            var checkedBpType = '1';
            if (recruitType === '1') {
                if (now.getFullYear() - bDate.getFullYear() >= 25) {
                    checkedBpType = '3';
                }
            }

            $(":radio[name='bpType'][value='" + checkedBpType + "']").iCheck('check');
        }
        //验证上一步必填项是否通过
        if ($("#form-student-info").valid() == false) {
            return false;
        }
        $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");
        next_fs.fadeIn();
        current_fs.fadeOut();
        animating = false;
    });

    $(".previous").click(function () {
        if (animating) {
            return false;
        }
        current_fs = $(this).parents('fieldset');
        previous_fs = $(this).parents('fieldset').prev();
        $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");
        previous_fs.fadeIn();
        current_fs.fadeOut();
        animating = false;
    });

    var oldZipCode = _init_area_select("oldProvinceCode", "oldCityCode", "oldDistrictCode"),
        rprZipCode = _init_area_select("rprProvinceCode", "rprCityCode", "rprDistrictCode"),
        nowZipCode = _init_area_select("nowProvinceCode", "nowCityCode", "nowDistrictCode",null,null,null,'1'),
        wpZipCode = _init_area_select("wpProvinceCode", "wpCityCode", "wpDistrictCode");

    $("#zipCodeInput").val(nowZipCode);
    $("#rprProvinceCode").change(_zipCodeChange);
    $("#rprCityCode").change(_zipCodeChange);
    $("#rprDistrictCode").change(_zipCodeChange);

    $("#idType").change(function () {
        if ($(this).val() == 1) {

            if (!$("#sexSelect").val() || !$("#birthdayInput").val()) {
                $("#idCard").change();
            }

            $("#birthdayInput").attr("disabled", "disabled");
            $("#sexSelect").attr("disabled", "disabled");
            $("#zipCodeInput").attr("disabled", "disabled");
            $("#sexSelect").attr("disabled", "disabled");
            $("#birthdayInput").attr("disabled", "disabled");
            $("#idCard").bind("change", function () {
                var msg = _resolve_card2($(this).val());
                _init_select("sexSelect", dictJson.sex, msg.sex);
                $("#birthdayInput").val(msg.birthday);
                $("#zipCodeInput").val(_init_area_select("rprProvinceCode", "rprCityCode", "rprDistrictCode", msg.provinceCode, msg.cityCode, msg.districtCode));
            });
            $("#idCard").rules("remove");
            $("#idCard").rules("add", {required: true, isIdCardNo: true});
        } else {
            $("#sexSelect").removeAttr("disabled");
            $("#birthdayInput").removeAttr("disabled");
            $("#zipCodeInput").removeAttr("disabled");
            $("#sexSelect").removeAttr("disabled");
            $("#birthdayInput").removeAttr("disabled");
            //$("#idCard").unbind("change");
            $("#idCard").rules("remove");
            $("#idCard").rules("add", {required: true, rangelength:[10,10]});
        }
    });
    $("#idCard").change(ifExistInfo);
    $("#mobile").change(ifMobileExistInfo);

    $("#form-student-info").validate({
        rules: {
            idType: {required: true},
            stdName: {required: true, maxlength: 15},
            idCard: {required: true, isIdCardNo: true},
            sexSelect: {required: true},
            birthdayInput: {required: true},
            address: {maxlength: 125,isAddress:true},
            mobile: {required: true,isMobile : true},
            emergencyContact: {isTel: true},
            telephone: {isTel: true},
            email: {email: true},
            qq: {isQq: true},
            remark: {maxlength: 100},
            rprProvinceCode: {required: true},
            rprCityCode: {required: true}
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            if ($("#feeId").val() == null || $("#feeId").val() == '' || $("#feeId").val() == undefined) {
                layer.msg('收费标准不能为空', {icon: 1, time: 1000});
                return false;
            }
            $("#sex").val($("#sexSelect").val());
            $("#zipCode").val($("#zipCodeInput").val());
            $("#birthday").val($("#birthdayInput").val());
            $(form).ajaxSubmit({
                type: "post", //提交方式
                dataType: "json", //数据类型
                url: '/recruit/recruitAdd.do', //请求url
                success: function (data) { //提交成功的回调函数
                    if ('00' == data.code) {
                        layer.msg('学员信息录入成功', {
                            icon: 1,
                            time: 1000
                        }, function () {
                            window.parent.myDataTable.fnDraw(true);
                            layer_close();
                        });
                    }
                }
            });
        }
    });


    _init_select("sexSelect", dictJson.sex);
    _init_select("rprType", dictJson.rprType);
    _init_select("edcsType", dictJson.edcsType);
    _init_select("edcsSystem", dictJson.edcsSystem);
    _init_select("pfsnLevel", dictJson.pfsnLevel);
    _init_select("enrollType", dictJson.enrollType);

    _init_select("studyType", dictJson.studyType);
    _init_select("materialType", dictJson.materialType);
    //图片上传
    $('#selectPhoto').on('change', function () {
        var file = this.files[0];
        var type = file.type;
        var box = $('#member-photo');
        var fr = new FileReader();
        fr.readAsDataURL(file);
        $(fr).on('load', function () {
            var result = fr.result;
            if (type.indexOf('image/') > -1) {
                createPhoto(result);
                $("#isPhotoChange").val('1');
            } else {
                alert('请选择正确的图片文件格式！');
            }
        });
    });


    if (recruitType === '2') {
        _init_select({"selectId": "idType", "ext1": recruitType}, dictJson.idType);
        _init_select({"selectId": "nation", "ext1": recruitType}, dictJson.nation);
        _init_select({"selectId": "politicalStatus", "ext1": recruitType}, dictJson.politicalStatus);
        _init_select("jobStatus", dictJson.jobStatus);
        _init_select({"selectId": "maritalStatus", "ext1": recruitType}, dictJson.maritalStatus);
        //国开(临时)
        // gradeData = [{"dictValue": "201803", "dictName": "201803", "ext1": "2"}];
        gradeData = [{"dictValue": "201809", "dictName": "201809", "ext1": "2"}];
    } else {
        _init_select("idType", dictJson.idType);
        _init_select("nation", dictJson.nation);
        _init_select("politicalStatus", dictJson.politicalStatus);
        _init_select("jobType", dictJson.jobType);
        _init_select("maritalStatus", dictJson.maritalStatus);

        gradeData = [
            // 暂时限制成教仅可录入2019级学员
            {"dictValue": "2019", "dictName": "2019", "ext1": "1"}
            /*{"dictValue": "2018", "dictName": "2018", "ext1": "1"},*/
            // {"dictValue": "2017", "dictName": "2017", "ext1": "1"}
        ];
    }
    _init_select({selectId: "grade", ext1: $("#recruitType").val()}, gradeData);
    _init_radio_box("bpTypeBox", "bpType", dictJson.bpType);
    _init_area_select("oldProvinceCode", "oldCityCode", "oldDistrictCode");
    $("#pfsnId").select2({
        placeholder: "--请先选择专业--"
    });
    $("#taId").select2({
        placeholder: "--请先选择考区--"
    });
    $("#secPfsnId").select2({
        placeholder: "--请先选择专业--"
    });
    $("#secTaId").select2({
        placeholder: "--请先选择考区--"
    });
    $("#scholarship").select2({
        placeholder: "--请选择收费标准--"
    });

    $("#pfsnLevel").change(function () {
        init_pfsn_select("unvsId", "pfsnId");
        init_ta_select("pfsnId", "taId");
        scholarshipSelectChange();
    });

    $("#grade").change(function () {

        initWishSelects({
            unvsSelectId: 'unvsId',
            pfsnSelectId: 'pfsnId',
            taSelectId: 'taId',
            unvsSearchUrl: '/baseinfo/sUnvs.do',
            pfsnSearchUrl: '/baseinfo/sPfsn.do',
            taSearchUrl: '/baseinfo/sTa.do'
        });

        initWishSelects({
            unvsSelectId: 'secUnvsId',
            pfsnSelectId: 'secPfsnId',
            taSelectId: 'secTaId',
            unvsSearchUrl: '/baseinfo/sUnvs.do',
            pfsnSearchUrl: '/baseinfo/sPfsn.do',
            taSearchUrl: '/baseinfo/sTa.do'
        });


        init_pfsn_select("unvsId", "pfsnId");
        init_ta_select("pfsnId", "taId");
        scholarshipSelectChange();
    });

    initWishSelects({
        unvsSelectId: 'unvsId',
        pfsnSelectId: 'pfsnId',
        taSelectId: 'taId',
        unvsSearchUrl: '/baseinfo/sUnvs.do',
        pfsnSearchUrl: '/baseinfo/sPfsn.do',
        taSearchUrl: '/baseinfo/sTa.do'
    });

    initWishSelects({
        unvsSelectId: 'secUnvsId',
        pfsnSelectId: 'secPfsnId',
        taSelectId: 'secTaId',
        unvsSearchUrl: '/baseinfo/sUnvs.do',
        pfsnSearchUrl: '/baseinfo/sPfsn.do',
        taSearchUrl: '/baseinfo/sTa.do'
    });

    $("#pfsnId").change(scholarshipSelectChange);
    $("#taId").change(scholarshipSelectChange);
    $("#scholarship").change(feeTablesChange);


    $(":radio[name='bpType']").on('ifChecked', function () {
        var points = 0;
        $('#points').rules('remove');
        $('#points').rules('add', {
            required: true,
            isDigits: true
        });
        switch ($(this).val()) {
            case '1':
                $('#points').attr("readonly", "readonly");
                break;
            case '2':
            case '3':
                $('#points').attr("readonly", "readonly");
                points = 20;
                break;
            case '4':
                points = 30;
                $('#points').attr("readonly", "readonly");
                break;
            case '5':
                $('#points').rules('add', {max: 50});
                $('#points').removeAttr("readonly");
                break;
        }
        $("#points").val(points);
    });

    showByRecruitType(recruitType);
    $("#edcsType").change(function () {
        showByRecruitType(recruitType);
    });
});

function _zipCodeChange() {
    $("#zipCodeInput").val($("#rprDistrictCode").children("option:selected").attr("zipCode"));
}

function ifExistInfo() {
    if (!$("#idType").val()) {
        $("#idType").val(1).change();
        $("#idCard").change();
    }

    $.ajax({
        type: 'POST',
        url: '/recruit/ifExistInfo.do',
        dataType: 'json',
        async: false,
        data: {
            stdName: function () {
                return $("#stdName").val();
            },
            idCard: function () {
                return $("#idCard").val();
            },
            idType: function () {
                return $("#idType").val();
            },
            recruitType: function () {
                return $("#recruitType").val();
            }
        },
        success: function (rData) {
            var result = rData.body;
            if (result && result.errCode === 'E00097') {
                layer.msg('请输入正确的身份证号码!', {
                    icon: 2,
                    time: 4000
                });
                $('#bt_baseInfo_next').attr("disabled", "disabled");
                return;
            }else if (result && result.errCode === 'E00098') {
                layer.msg('该学员已在其他老师名下报读,无法录入', {
                    icon: 2,
                    time: 4000
                });
                $('#bt_baseInfo_next').attr("disabled", "disabled");
                return;
            }else if (result && result.errCode === 'E00099') {
                layer.msg('已经存在报读信息,请联系管理员走转报流程', {
                    icon: 2,
                    time: 4000
                });
                $('#bt_baseInfo_next').attr("disabled", "disabled");
                return;
            }

            if ("false" == result) {
                layer.msg('学员信息已存在，请勿重复录入', {
                    icon: 1,
                    time: 1000
                });
                $('#bt_baseInfo_next').attr("disabled", "disabled");
                return;
            } else if (result) {
                var data = result.baseInfo, other = result.other, recruitType = recruitType_global;
                layer.confirm('检测到学员[' + data.stdName + ']，是否自动填充信息？', function (index) {
                    if (data) {
                        $("#stdId").val(data.stdId);
                        $("#stdName").val(data.stdName);
                        $("#idCard").val(data.idCard);
                        $("#birthdayInput").val(data.birthday);
                        $("#address").val(data.address);
                        $("#zipCodeInput").val(data.zipCode);
                        $("#mobile").val(data.mobile);
                        $("#emergencyContact").val(data.emergencyContact);
                        if (recruitType === '2') {
                            _init_select({"selectId": "idType", "ext1": recruitType}, dictJson.idType, data.idType);
                            _init_select({"selectId": "nation", "ext1": recruitType}, dictJson.nation, data.nation);
                            _init_select({
                                "selectId": "politicalStatus",
                                "ext1": recruitType
                            }, dictJson.politicalStatus, data.politicalStatus);
                            _init_select("jobStatus", dictJson.jobStatus, data.jobStatus);
                        } else {
                            _init_select("idType", dictJson.idType, data.idType);
                            _init_select("nation", dictJson.nation, data.nation);
                            _init_select("politicalStatus", dictJson.politicalStatus, data.politicalStatus);
                            _init_select("jobType", dictJson.jobType, data.jobType);
                        }
                        _init_select("sexSelect", dictJson.sex, data.sex);
                        _init_select("edcsType", dictJson.edcsType, data.edcsType);
                        _init_select("rprType", dictJson.rprType, data.rprType);

                        //_init_area_select("rprProvinceCode", "rprCityCode", "rprDistrictCode", data.rprProvinceCode, data.rprCityCode, data.rprDistrictCode);
                        _init_area_select("nowProvinceCode", "nowCityCode", "nowDistrictCode", data.nowProvinceCode, data.nowCityCode, data.nowDistrictCode);
                        _init_area_select("wpProvinceCode", "wpCityCode", "wpDistrictCode", data.wpProvinceCode, data.wpCityCode, data.wpDistrictCode);

                        $("#wpAddress").val(data.wpAddress);
                        $("#wpTelephone").val(data.wpTelephone);
                        $("#wpTime").val(data.wpTime);
                    }
                    if (other) {
                        if (recruitType === '2') {
                            _init_select({
                                "selectId": "maritalStatus",
                                "ext1": recruitType
                            }, dictJson.maritalStatus, other.maritalStatus);
                        } else {
                            _init_select("maritalStatus", dictJson.maritalStatus, other.maritalStatus);
                        }
                        $("#remark").val(other.remark);
                        $("#workPlace").val(other.workPlace);
                        $("#jobTitle").val(other.jobTitle);
                        $("#wechat").val(other.wechat);
                        $("#qq").val(other.qq);
                        $("#email").val(other.email);
                        $("#telephone").val(other.telephone);
                        if (other.headPortrait) {
                            $("#headPortrait").val(other.headPortrait);
                            createPhoto(_FILE_URL + other.headPortrait + "?" + Date.parse(new Date()));
                        }
                    }

                    layer.close(index);
                });
                $('#bt_baseInfo_next').removeAttr("disabled");
            } else {
                $('#bt_baseInfo_next').removeAttr("disabled");
                $("#stdId").val('');
            }
        }
    });
}

function initWishSelects(config) {
    var grade = $("#grade").val();
    //console.log(grade)
    _simple_ajax_select({
        selectId: config.unvsSelectId,
        searchUrl: config.unvsSearchUrl,
        sData: {
            ext1: $("#recruitType").val()
        },
        showText: function (item) {
            if (item.unvsCode == '13667' && grade == '2019') { //临时过滤19级广州工商
                return;
            } else {
                var text = '[' + _findDict('recruitType', item.recruitType) + ']';
                text += item.unvsName + '(' + item.unvsCode + ')';
                return text;
            }
        },
        showId: function (item) {
            if (item.unvsId == '52' && grade == '2019') { //临时过滤19级广州工商
                return;
            } else {
                return item.unvsId;
            }
        },
        placeholder: '--请选择院校--'
    });

    $("#" + config.unvsSelectId).change(function () {
        $("#" + config.pfsnSelectId).removeAttr("disabled");
        init_pfsn_select(config.unvsSelectId, config.pfsnSelectId);
    });

    $("#" + config.pfsnSelectId).change(function () {
        $("#" + config.taSelectId).removeAttr("disabled");
        init_ta_select(config.pfsnSelectId, config.taSelectId);
    });
}

var feeTablesChange = function () {
    $('#feeTable tbody').empty();
    $("#fee_total").text("0.00");
    $("#offerRemark").text('');
    $.ajax({
        url: '/recruit/showFeeList.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#pfsnId").val(),
            taId: $("#taId").val(),
            scholarship: $("#scholarship").val(),
            recruitType: $("#recruitType").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                if (data.body) {
                    $('#feeTable tbody').empty();
                    $("#fee_total").text("0.00");
                    var feeList = data.body.feeList;
                    var feeInfo = data.body.feeInfo;
                    var feeTotal = data.body.feeTotal;
                    if (feeList) {
                        for (var i = 0; i < feeList.length; i++) {
                            var info = feeList[i];
                            var _feeAmount = parseFloat(info.amount);
                            var dom = "<tr class='text-l'>";
                            var itemName = getItemName(info.itemName, $("#grade").val());
                            dom += "<td>" + info.itemCode + ":" + itemName + "</td>";
                            dom += "<td>" + info.amount + "</td>";
                            dom += "<td>" + info.discount + "</td>";
                            dom += "<td>" + info.payable + "</td>";
                            dom += "</tr>";
                            if (_feeAmount > 0) {
                                $('#feeTable tbody').append(dom);
                            }
                        }
                        $("#fee_total").text(feeTotal);
                    } else {
                        $('#feeTable tbody').append('<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty">' +
                            '<div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>');
                    }
                    if (feeInfo) {
                        $("#feeId").val(feeInfo.feeId);
                        $("#offerId").val(feeInfo.offerId);
                        $("#offerRemark").text(feeInfo.offerRemark ? feeInfo.offerRemark : '无');
                    }
                }
            }
        }
    });
};

function init_ta_select(pfsnSelectId, taSelectId) {
    _simple_ajax_select({
        selectId: taSelectId,
        searchUrl: '/baseinfo/sTa.do',
        sData: {
            sId: function () {
                return $("#" + pfsnSelectId).val();
            }
        },
        showText: function (item) {
            var text = '[' + item.taCode + ']' + item.taName;
            return text;
        },
        showId: function (item) {
            return item.taId;
        },
        placeholder: '--请选择考区--'
    });
}

function init_pfsn_select(unvsSelectId, pfsnSelectId) {
    _simple_ajax_select({
        selectId: pfsnSelectId,
        searchUrl: '/baseinfo/sPfsn.do',
        sData: {
            sId: function () {
                return $("#" + unvsSelectId).val();
            },
            ext1: function () {
                return $("#pfsnLevel").val();
            },
            ext2: function () {
                return $("#grade").val();
            }
        },
        showText: function (item) {
            var text = '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']' + '-['
                + _findDict('year', item.year) + ']';
            text += item.pfsnName + '(' + item.pfsnCode + ')';
            return text;
        },
        showId: function (item) {
            return item.pfsnId;
        },
        placeholder: '--请选择专业--'
    });
}

function deletPhoto() {
    layer.confirm('确定删除？', function () {
        $('#member-photo').empty();
        layer.msg('已删除', {
            icon: 1
        });
    })
}

function createPhoto(imgSrc) {
    var box = $("#member-photo");
    box.empty();
    var img = document.createElement('img');
    $(img).on('load', function () {
        var height = this.height;
        var width = this.width;
        if (height > width) {
            $(img).css('height', '140px')
        } else {
            $(img).css('width', '140px')
        }
    });
    img.src = imgSrc;
    box.html(img);
}

function scholarshipSelectChange() {

    $.ajax({
        url: '/recruit/getScholarships.do',
        dataType: 'json',
        type: 'post',
        data: {
            pfsnId: $("#pfsnId").val(),
            taId: $("#taId").val()
        },
        success: function (data) {
            if (data.code == _GLOBAL_SUCCESS) {
                var dictArray = [];
                if (data.body) {
                    var recruitType = $("#recruitType").val();
                    $.each(data.body, function (index, s) {
                        var name = _findDict('scholarship', s);
                        dictArray.push({
                            'dictValue': s,
                            'dictName': name
                        });
                    });
                }
                var sv;
                if (dictArray.length > 0) {
                    sv = dictArray[0].dictValue;
                }
                _init_select("scholarship", dictArray, sv);
            }
            feeTablesChange();
        }
    });
}

function showByRecruitType(recruitType) {
    if (recruitType === '2') {
        var edcsType = $("#edcsType").val(), undergraduate = "679";
        if (edcsType != "" && undergraduate.indexOf(edcsType) >= 0) {
            $("#subject, #subjectCategory, #studyType, #materialType, #materialCode").parent().parent().removeClass('invisible');
            $("#profession, #diploma").parent().prev().find("span").show();
        } else {
            $("#subject, #subjectCategory, #studyType, #materialType, #materialCode").parent().parent().addClass('invisible');
            $("#profession, #diploma").parent().prev().find("span").hide();
        }
    } else {
        $("#maritalStatus").parent().prev().find("span").hide();
    }
}

function ifMobileExistInfo(){
	 $.ajax({
	    type: 'POST',
	    url: '/recruit/ifMobileExistInfo.do',
	    dataType: 'json',
	    async: false,
	    data: {
	        mobile: function () {
	            return $("#mobile").val();
	        }
	    },
	    success: function (rData) {
	       var result = rData.body;
	       if(!result){
	    	   layer.msg('改手机号已经报读,无法录入', {
                   icon: 2,
                   time: 4000
               });
	       }
	    }
	});
}