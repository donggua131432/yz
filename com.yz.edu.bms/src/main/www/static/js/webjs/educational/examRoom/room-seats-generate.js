 $(function () {
        $("#epId").select2({
            placeholder:'--请选择--'
        });
        $("#examTime").select2({
            placeholder:'--请选择--'
        });

        //初始考试年度下拉框
        $.ajax({
            type: "POST",
            dataType: "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=1',
            success: function (data) {
                var eyJson = data.body;
                if (data.code == '00') {
                    _init_select("eyId", eyJson);
                }
            }
        });
        $("#eyId").change(function () {
            if($("#epId").html()) {
                $('#epId').select2('destroy');
                $("#epId").html('');
                $("#epId").change();
            }

            if($(this).val()){
                //初始考场
                _simple_ajax_select({
                    selectId: "epId",
                    searchUrl: '/examRoomAssign/findAllKeyValueByEyId.do',
                    sData: {
                        eyId: function () {
                            return $("#eyId").val();
                        }
                    },
                    showText: function (item) {
                        return item.ep_name;
                    },
                    showId: function (item) {
                        return item.ep_id;
                    },
                    placeholder: '--请选择--'
                });
                $("#epId").append("<option value=''>请选择考场</option>");
            }else {
                $("#epId").select2({
                    placeholder:'--请选择--'
                });
            }

        });

        $("#epId").change(function () {
            if($("#examTime").html()) {
                $('#examTime').select2('destroy');
                $("#examTime").html('');
                $("#examTime").change();
            }
            if($(this).val()){
                $.ajax({
                    type: "POST",
                    dataType: "json", //数据类型
                    data: {epId: $(this).val(), eyId: $("#eyId").val()},
                    url: '/examRoomAssign/findExamTime.do',
                    success: function (data) {
                        var json = data.body||[];
                        if (data.code == '00') {
                            json=JSON.parse(JSON.stringify(json).replace(/PM/g,'下午').replace(/AM/g,'上午'));
                            _init_select("examTime", json);
                        }
                    }
                });
            }else {
                $("#examTime").select2({
                    placeholder:'--请选择--'
                });
            }

        });

        $("#form-room-assign-edit").validate({
            rules: {
                eyId: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/examRoomSeats/generateExamSeats.do',
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon: 1,
                                time: 1000
                            }, function () {
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    });