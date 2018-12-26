
    $(function () {

        $("select").select2({
            placeholder: '--请选择--'
        })

        _init_select("year", [
            {"dictValue": "2016", "dictName": "2016"},
            {"dictValue": "2017", "dictName": "2017"},
            {"dictValue": "2018", "dictName": "2018"},
            {"dictValue": "2019", "dictName": "2019"},
            {"dictValue": "2020", "dictName": "2020"}
        ]);

        //初始化年级下拉框
        _init_select("grade",dictJson.grade);
        _init_select("pfsnLevel", dictJson.pfsnLevel);
        //初始化学期
        _init_select("semester",dictJson.semester);

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
            $("#unvsName").val($(this).find('>option:selected').text());
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

        $("#form-room-assign-edit").validate({
            rules: {
                year: {
                    required: true,
                },
                semester: {
                    required: true,
                },
                grade: {
                    required: true,
                },
                unvsId: {
                    required: true,
                },
                pfsnLevel: {
                    required: true,
                },
                pfsnId: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $("#export").val("正在导出").attr('disabled','disabled').css({opacity:'0.5'});
                //韩师
                if ($("#unvsId").val() == "29" && $("#examTime").val() == "") {
                    layer.msg("请选择考试时间！")
                    return;
                }
                var url = '/studentTScore/templateExcelExport.do';
                $.ajax({
                    url: url,
                    type: "post",
                    data: {
                        unvsName: $("#unvsName").val(),
                        pfsnName: $("#pfsnId>option:selected").text(),
                        unvsId: $("#unvsId").val(),
                        pfsnId: $("#pfsnId").val(),
                        grade: $("#grade").val(),
                        pfsnLevel: $("#pfsnLevel").val(),
                        semester: $("#semester").val(),
                        year: $("#year").val(),
                        examTime:$("#examTime").val()
                    },
                    success: function (data) {
                        if (data == 'none') {
                            layer.msg("该院校没有对应的模板！")
                            return;
                        }
                        var url = _FILE_URL + decodeURIComponent(data);
                        var a = document.createElement('a');
                        a.download = 'data.xlsx';
                        a.href = url;
                        $("body").append(a);  // 修复firefox中无法触发click
                        a.click();
                        $(a).remove();
                        $("#export").val("确定").removeAttr('disabled').css({opacity:'1'});
                    },
                    error:function () {
                        $("#import").val("确定").removeAttr('disabled').css({opacity:'1'});
                    }

                })
            }
        });
    });

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