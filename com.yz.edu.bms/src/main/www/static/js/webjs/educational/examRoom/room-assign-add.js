$(function() {
        //初始考试年度
        $.ajax({
            type: "POST",
            dataType : "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=1',
            success: function(data){
                var eyJson = data.body;
                if(data.code=='00'){
                    _init_select("eyId",eyJson);
                }
            }
        });

        //初始考场
        _simple_ajax_select({
            selectId: "placeId0",
            searchUrl: '/examRoomAssign/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.ep_name;
            },
            showId: function (item) {
                return item.ep_id;
            },
            placeholder: '--请选择考场--'
        });
        $("#placeId0").append(new Option("", "", false, true));

        $("#form-room-assign-edit").validate({
            rules : {
                eyId : {
                    required : true,
                }
            },
            onkeyup : false,
            focusCleanup : true,
            success : "valid",
            submitHandler : function(form) {
                var validate=true;
                $('#examConfig > .item').each(function (index) {
                    if (!$(this).find('#placeId' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择考场！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#date' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择日期！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#startTime' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择开始时间！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#endTime' + index + '').val()) {
                        validate=false;
                        layer.msg('请选择结束时间！', {icon: 5, time: 1000});
                        return;
                    }
                    if (!$(this).find('#seats' + index + '').val()) {
                        validate=false;
                        layer.msg('请输入考场容量！', {icon: 5, time: 1000});
                        return;
                    }

                });
                if(!validate) return;

                $(form).ajaxSubmit({
                    type : "post", //提交方式
                    dataType : "json", //数据类型
                    url : '/examRoomAssign/add.do', //请求url
                    success : function(data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('操作成功！', {
                                icon : 1,
                                time : 1000
                            },function () {
                                window.parent.myDataTable.fnDraw(true);
                                layer_close();
                            });
                        }
                    }
                })
            }
        });
    });
    
    function addPlace() {
        var len=$('#examConfig > .item').length;
        var html='<div class="item" style="margin-top: 10px">\n' +
            '                        <select id="placeId'+len+'" class="select" size="1" name="examConfig['+len+'].placeId">\n' +
            '                        </select>\n' +
            '                        <input type="text" onfocus="WdatePicker({ dateFmt:\'yyyy-MM-dd\'})" id="date'+len+'" name="examConfig['+len+'].date" class="input-text Wdate" style="width: 100px;"  placeholder="日期" />\n' +
            '                        &nbsp; <input type="text" placeholder="开始时间"  onfocus="WdatePicker({ dateFmt:\'HH:mm\',minDate:\'#F{$dp.$D(\\\'date'+len+'\\\',{d:-1})}\'})" id="startTime'+len+'" name="examConfig['+len+'].startTime" class="input-text Wdate" style="width: 100px;" /> -\n' +
            '                        <input type="text" placeholder="结束时间"  onfocus="WdatePicker({ dateFmt:\'HH:mm\', minDate:\'#F{$dp.$D(\\\'startTime'+len+'\\\');}\'})" id="endTime'+len+'" name="examConfig['+len+'].endTime" class="input-text Wdate" style="width: 100px;" />\n' +
            '                        <input type="number" class="input-text" id="seats'+len+'" name="examConfig['+len+'].seats" style="width: 80px;" placeholder="考场容量" min="1">\n' +
            '                        <input type="text" class="input-text" name="examConfig['+len+'].remark" style="width: 150px;" placeholder="备注">\n' +
            '                    </div>'
        html=$(html);
        $('#examConfig').append(html);

        _simple_ajax_select({
            selectId: "placeId"+len,
            searchUrl: '/examRoomAssign/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.ep_name;
            },
            showId: function (item) {
                return item.ep_id;
            },
            placeholder: '--请选择考场--'
        });

        $("#placeId"+len).append(new Option("", "", false, true));
    }
    function delPlace() {
        var len=$('#examConfig > .item').length;
        if(len===1) return;
        $('#examConfig > .item:last-child').remove();
    }