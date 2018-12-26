$(function () {

        $("#checkStatus").select2({
            placeholder:'--请选择--'
        })

        _init_select("checkStatus", [
            {"dictValue": "1", "dictName": "通过审核"},
            {"dictValue": "2", "dictName": "不通过审核"}
        ]);

        $("#checkStatus").change(function () {
            $("#commentContent").text('');
            if($(this).val()=='1'){
                $("#commentContent").text("你好，资料通过审核，请于3月30日前把通过审核的资料打印出来、按模板要求手写签名、盖章，在把资料邮寄回学校。\n" +
                    "需提交以下资料：\n" +
                    "1、嘉应专科学员需提交：实习鉴定表，实习报告（各打印提交一份纸质资料）\n" +
                    "2、嘉应本科学员需提交：论文、论文选题表、实习鉴定表（各打印提交一份纸质资料）\n" +
                    "3、仲恺专科学员需提交：实习报告（各打印提交一份纸质资料）\n" +
                    "4、仲恺本科学员需提交：论文（各打印提交两份纸质资料）、论文计划书（各打印提交两份纸质资料）\n" +
                    "邮寄地址：惠州市惠城区江北三新南路22号润宇豪庭三楼远智教育\n" +
                    "收件人：黄老师，电话：13302659358")
            }else if($(this).val()=='2'){
                $("#commentContent").text("你好，资料审核不通过，请按照以下要求重新修改：\n" +
                    "看到信息请及时修改并重新上传，审核无误后在打印。")
            }else{
                $("#commentContent").text("");
            }
        })

        $("#form-room-assign-edit").validate({
            rules: {
                checkStatus: {
                    required: true,
                },
                commentContent:{
                    required: true,
                    maxlength:500
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/graduatePaper/updateAttachment.do',
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