 $(function () {

        $("#courseId").select2({
            placeholder:'--请选择--'
        })

        //初始考场
        _simple_ajax_select({
            selectId: "courseId",
            searchUrl: '/live/findCourseByYear.do',
            sData: {
            },
            showText: function (item) {
                return item.course_name;
            },
            showId: function (item) {
                return item.course_id;
            },
            checkedInfo : {
                value : liveChannel.courseId, name:course
            },
            placeholder: '--请选择--'
        });

        $("#form-room-assign-edit").validate({
            rules: {
                courseId: {
                    required: true,
                },
                channelPassword:{
                    required: true,
                    minlength:6,
                    maxlength:12
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/live/changePassword.do',
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