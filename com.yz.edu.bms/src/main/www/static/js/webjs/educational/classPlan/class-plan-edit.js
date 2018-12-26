//初始化
            $(function() {
                var map = {};
                //课程类型
                _init_select("courseType", dictJson.courseType, $("#courseType").val());
                //上课方式
                _init_select("cpType", dictJson.cpType, $("#cpType").val());

                $("#courseName").select2({
                    placeholder : "请选择课程名称",
                    allowClear : true
                });
                $("#courseName").append(new Option($("#courseNamev").val(), $("#courseName").val(), false, true));

                $("#courseType").change(function() {
                    //课程名称
                    _simple_ajax_select({
                        selectId : "courseName",
                        searchUrl : '/classPlan/findCourseName.do',
                        sData : {
                            "courseType" : $("#courseType").val()
                        },
                        showText : function(item) {
                            return item.courseName;
                        },
                        showId : function(item) {
                            return item.courseId;
                        },
                        placeholder : '请选择课程名称'
                    });
                });

                //校区
                _simple_ajax_select({
                    selectId : "campusId",
                    searchUrl : '/classPlan/findCampusByName.do',
                    sData : {},
                    showText : function(item) {
                        return item.campusName;
                    },
                    showId : function(item) {
                        return item.campusId;
                    },
                    placeholder : '请选择校区'
                });

                if ($("#campusNamev").val())
                    $("#campusId").append(new Option($("#campusNamev").val(), $("#campusIdh").val(), false, true));

                //教师
                _simple_ajax_select({
                    selectId : "empId",
                    searchUrl : '/classPlan/findEmployeeByName.do',
                    sData : {
                        campusId : function() {
                            return $("#campusId").val();
                        }
                    },
                    showText : function(item) {
                        return item.empName;
                    },
                    showId : function(item) {
                        map[item.empId] = item.otherFee;
                        return item.empId;
                    },
                    placeholder : '请选择教师'
                });
                if ($("#empNamev").val())
                    $("#empId").append(new Option($("#empNamev").val(), $("#empIdh").val(), false, true));

                $("#empId").change(function() {
                    $("#money").val(map[$("#empId").val()]);
                });

                $('.skin-minimal input').iCheck({
                    checkboxClass : 'icheckbox-blue',
                    radioClass : 'iradio-blue',
                    increaseArea : '20%'
                });

                $("#form-member-add").validate({
                    rules : {
                        courseName : {
                            required : true,
                        },
                        courseType : {
                            required : true,
                        },
                        year : {
                            required : true,
                        },
                        startTime : {
                            required : true,
                        },
                        endTime : {
                            required : true,
                        },
                        cpDate : {
                            required : true,
                        }
                    },
                    messages : {
                        errorCode : {
                            remote : "参数名称已存在"
                        }
                    },
                    onkeyup : false,
                    focusCleanup : true,
                    success : "valid",
                    submitHandler : function(form) {
                        var urls;
                        if ($("#exType").val() == "UPDATE") {
                            urls = '/classPlan/updateClassPlan.do';
                        } else {
                            urls = '/classPlan/insertClassPlan.do';
                        }
                        $(form).ajaxSubmit({
                            type : "post", //提交方式  
                            dataType : "json", //数据类型  
                            url : urls, //请求url 
                            success : function(data) { //提交成功的回调函数
                                layer.msg('提交成功', {
                                    icon : 1,
                                    time : 1000
                                },function () {
                                    window.parent.myDataTable.fnDraw(false);
                                    layer_close();
                                });
                            },
                            error : function(data) {
                                layer.msg('提交失败', {
                                    icon : 5,
                                    time : 1000
                                },function () {
                                    layer_close();
                                });
                            }
                        })
                    }
                });
            });

            function countMinute() {
                var startTime = $("#startTime").val();
                var endTime = $("#endTime").val();

                if (null == startTime || '' == startTime) {
                    $("#classTime").val(0);
                    return;
                }

                if (null == endTime || '' == endTime) {
                    $("#classTime").val(0);
                    return;
                }

                var min1 = parseInt(startTime.substr(0, 2)) * 60 + parseInt(startTime.substr(3, 2));
                var min2 = parseInt(endTime.substr(0, 2)) * 60 + parseInt(endTime.substr(3, 2));

                //两个分钟数相减得到时间部分的差值，以分钟为单位
                var n = min2 - min1;
                $("#classTime").val(n);
            }

            function del(e) {
                $(e).parent().remove();
            }