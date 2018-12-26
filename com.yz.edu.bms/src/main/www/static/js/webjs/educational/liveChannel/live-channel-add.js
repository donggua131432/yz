
    $(function () {

        //初始化年度下拉框
        _init_select("year", dictJson.year);

        $("#courseId").select2({
            placeholder:'--请选择--'
        });

        $("#year").change(function () {
            if($("#courseId").html()){
                $('#courseId').select2('destroy');
                $("#courseId").html('');
                $("#courseId").change();
            }
            if($(this).val()){
                //初始考场
                _simple_ajax_select({
                    selectId: "courseId",
                    searchUrl: '/live/findCourseByYear.do',
                    sData: {
                        year: function () {
                            return $("#year").val();
                        }
                    },
                    showText: function (item) {
                        return item.course_name;
                    },
                    showId: function (item) {
                        return item.course_id;
                    },
                    placeholder: '--请选择--'
                });
            }else {
                $("#courseId").select2({
                    placeholder:'--请选择--'
                });
            }



        });

        $("#courseId").change(function () {
            var val=$("#courseId option:selected").text();
            if(val==='请选择'){
                val='';
            }
            $("#channelName").val(val)
        });

        $("#form-room-assign-edit").validate({
            rules: {
                courseId: {
                    required: true,
                },
                channelName:{
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
                    url: '/live/add.do',
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