 $(function() {
        //初始考试年度下拉框
        $.ajax({
            type: "POST",
            dataType : "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=1',
            success: function(data){
                var eyJson = data.body;
                if(data.code=='00'){
                    _init_select("eyId",eyJson,vareyId);
                }
            }
        });
        //初始考场
        _simple_ajax_select({
            selectId: "placeId",
            searchUrl: '/examRoomAssign/findAllKeyValue.do',
            sData: {
            },
            showText: function (item) {
                return item.ep_name;
            },
            showId: function (item) {
                return item.ep_id;
            },
            checkedInfo : {
                value : varplaceId, name:epName
            },
            placeholder: '--请选择考场--'
        });

        $("#form-room-assign-edit").validate({
            rules : {
                placeId : {
                    required : true,
                },
                date : {
                    required : true,
                },
                startTime : {
                    required : true,
                },
                endTime : {
                    required : true,
                },
                seats : {
                    required : true,
                }
            },
            messages : {
                taCode : {
                    remote : "编号已存在，请修改后提交"
                }
            },
            onkeyup : false,
            focusCleanup : true,
            success : "valid",
            submitHandler : function(form) {
                $(form).ajaxSubmit({
                    type : "post", //提交方式
                    dataType : "json", //数据类型
                    url :'/examRoomAssign/edit.do',
                    success : function(data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon : 1,
                                time : 1000
                            },function () {
                                if($("#exType").val() == "UPDATE"){
                                    window.parent.myDataTable.fnDraw(false);
                                }else{
                                    window.parent.myDataTable.fnDraw(true);
                                }
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    });