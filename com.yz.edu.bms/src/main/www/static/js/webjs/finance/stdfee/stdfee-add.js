$(function () {

        //学员
        _simple_ajax_select({
            selectId: "learnId",
            searchUrl: '/stdFee/findStudentInfo.do',
            sData: {},
            showText: function (item) {
                return item.stdName + '-' + item.grade + "级";
            },
            showId: function (item) {
                return item.learnId;
            },
            placeholder: '--学员--'
        });

        _simple_ajax_select({
            selectId: "itemCode",
            searchUrl: '/stdFee/findItemCodeHaveNot.do',
            sData: {
                learnId: function () {
                    return $("#learnId").val();
                }
            },
            showText: function (item) {
                var text = item.itemCode + ':' + item.itemName;
                return text;
            },
            showId: function (item) {
                return item.itemCode;
            },
            placeholder: '--科目--'
        });

        $("#form-payable-add").validate({
            rules: {
                learnId: {
                    required: true,
                },
                amount: {
                    required: true,
                    min: 0,
                    number: true
                },
                itemCode: {
                    required: true
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/stdFee/add.do', //请求url
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('新增成功！', {icon: 1, time: 1000}, function () {
                                window.parent.myDataTable.fnDraw(true);
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    });

    function stdChange() {
        var learnId = $("#learnId").val();
        if (null != learnId && '' != learnId) {
            $("#itemCode").removeAttr("disabled");
        } else {
            $("#itemCode").attr("disabled", "disabled");
        }
    }