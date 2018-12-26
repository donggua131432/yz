 $(function () {
        //学员
        _simple_ajax_select({
            selectId: "learnId",
            searchUrl: '/studentRoll/findStudentInfoNew.do',
            sData: {},
            showText: function (item) {
                return item.stdName + " - " + item.grade + "级" + " - " + item.idCard;
            },
            showId: function (item) {
                return item.learnId;
            },
            placeholder: '--学员--'
        });
        //年级事件
        $("#learnId").change(function () {
            $.post('/studentRoll/findStudentEnrollInfo.do', {
                learnId: $("#learnId").val()
            }, function (data) {
                data = data.body;

                $("#taId").val(data.taId);
                $("#scholarshipOld").val(data.scholarship);
                //学员状态
                $("#stdStage").val(_findDict("stdStage", data.stdStage));
                //优惠类型
                $("#scholarship").val(_findDict("scholarship", data.scholarship));
                //入围状态
                $("#inclusionStatus").val(_findDict("inclusionStatus", data.inclusionStatus));
                //层次
                $("#learnPfsnLevel").val(_findDict("pfsnLevel", data.pfsnLevel));
                $("#pfsnLevel").val(data.pfsnLevel);
                $("#zyPfsnLevel").val(_findDict("pfsnLevel", data.pfsnLevel));
                //院校
                $("#unvsName").val(data.unvsName);
                $("#zyUnvsId").val(data.unvsName);
                //专业
                $("#pfsnName").val("(" + data.pfsnCode + ")" + data.pfsnName);
                //姓名
                $("#stdName").val(data.stdName);
                //姓名
                $("#learnStdName").val(data.stdName);
                //身份证号码
                $("#idCard").val(data.idCard);
                $("#learnIdCard").val(data.idCard);
                //性别
                $("#sexv").val(_findDict("sex", data.sex));
                $("#sex").val(data.sex);
                //民族
                $("#nationv").val(_findDict("nation", data.nation));
                $("#nation").val(data.nation);
                $("#unvsId").val(data.unvsId);
                $("#pfsnId").val(data.pfsnId);

                $("#stdId").val(data.stdId);
                $("#grade").val(data.grade);
                //分数
                $("#score").val(data.score);
                $("#zyPfsnName").val("(" + data.pfsnCode + ")" + data.pfsnName);

                $("#taIdv").val(data.taName);

                flushPfns();
                flushTa();

                var ext = ".doc";
                //韩师，东理
                if (data.unvsId == 23 || data.unvsId == 29) {
                    ext = ".xls";
                }
                //国开大学
                if (data.unvsId != 46) {
                    $("#rollfile").attr("href", "../rolldoc/roll_" + data.unvsId + ext);
                }
                //暂无文件
                if(data.unvsId==47 || data.unvsId==50 || data.unvsId==52|| data.unvsId==54){
                    $("#rollfile").attr("href","javascript:;");
                }
            }, "json");
        });

        //性别
        _init_select("newSex", dictJson.sex);

        //民族
        _init_select("newNation", dictJson.nation);

         //院校
         _simple_ajax_select({
             selectId: "newUnvsId",
             searchUrl: '/baseinfo/sUnvs.do',
             sData: {},
             showText: function (item) {
                 return item.unvsName;
             },
             showId: function (item) {
                 return item.unvsId;
             },
             placeholder: '--请选择院校--'
         });

         //专业层次
         _init_select("pfsnLevels2", dictJson.pfsnLevel, "", "--请选择层次--");

         //初始考区
        _init_select("newTaId", "", "", "---请选择考区---");

        //初始化专业
        $("#newPfsnId").select2({
            placeholder: "--请选择专业--",
            allowClear: true
        });

        var checksFlag=false;
        $("#form-member-add").validate({
            onkeyup: false,
            focusCleanup: true,
            rules: {
                learnId: {
                    required: true
                },
                ext1: {
                    required: true
                },
                newIdCard: {isIdCardNo: true}
            },
            success: true,
            submitHandler: function (form) {
                if($("#newUnvsId").val()!='' && $("#pfsnLevels2").val()==""){
                    layer.msg("请选择要变动的层次")
                    return;
                }
                if($("#pfsnLevels2").val()!=''&& $("#newPfsnId").val()==""){
                    layer.msg("请选择要变动的专业")
                    return;
                }
                if($("#newPfsnId").val()!=''&& $("#newTaId").val()==""){
                    layer.msg("请选择要变动的考试县区")
                    return;
                }
                if(checksFlag){
                    layer.msg('请勿重复提交!', {
                        icon : 0,
                        time : 1000
                    });
                    return
                }
                checksFlag=true;
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/studentRoll/insertStudentRollModifyNew.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {icon: 1, time: 1000}, function () {
                                layer_close();
                                checksFlag=false;
                            });
                        }
                    }
                });
            }
        });



        $('#modify').on('click', '.btn-modify', function () {
            if(!$("#stdId").val()){
                layer.msg("请选择学员");
                return;
            }
            if($("#unvsId").val()=='46' && $(this).attr("id")=="btnUnvs"){
                layer.msg("国开不能修改院校");
                return;
            }
            $(this).hide().siblings('div').css('display', 'inline');
            if ($(this).attr('id') === 'btnUnvs' || $(this).attr('id') === 'btnPfsnLevel' || $(this).attr('id') === 'pfsnModify') {
                $(this).closest('.row').next('.row').find('.btn-modify').trigger('click');
            }
        })

        $("#newUnvsId").change(function () {
            flushPfns($(this).val());
            flushTa();
        })

        $("#pfsnLevels2").change(function () {
            flushPfns($("#newUnvsId").val(),$(this).val());
            flushTa();
        })

        $("#newPfsnId").change(function () {
            flushTa($(this).val());
        })

    });

    function flushPfns(unvsId,pfsnLevel) {
        //专业
        _simple_ajax_select({
            selectId: "newPfsnId",
            searchUrl: '/unvsProfession/searchAllowProfessionJson.do',
            sData: {
                unvsId: unvsId||$("#unvsId").val(),
                pfsnLevel: pfsnLevel||$("#pfsnLevel").val(),
                grade: $("#grade").val()
            },
            showText: function (item) {
                return "(" + item.pfsn_code + ")" + item.text;
            },
            showId: function (item) {
                return item.id;
            },
            placeholder: '--请选择专业--'
        });
    }
    
    function flushTa(pfsnId) {
        //考区
        _simple_ajax_select({
            selectId: "newTaId",
            searchUrl: '/testArea/findBdTestAreaByPfsnId.do',
            sData: {
                pfsnId: pfsnId||$("#pfsnId").val()
            },
            showText: function (item) {
                return item.taName;
            },
            showId: function (item) {
                return item.taId;
            },
            placeholder: '--请选择考区--'
        });
    }