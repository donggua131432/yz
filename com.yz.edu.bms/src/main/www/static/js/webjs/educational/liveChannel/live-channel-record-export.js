 $(function () {

     $("select").select2({
         placeholder: '--请选择--'
     })

     _simple_ajax_select({
         selectId: "channelId",
         searchUrl: '/live/findChannel.do',
         sData: {
         },
         showText: function (item) {
             return item.channel_id+'-'+item.channel_name+'-'+item.year;
         },
         showId: function (item) {
             return item.channel_id;
         },
         placeholder: '--请选择频道--'
     });

     $("#channelId").change(function () {
         initDate();
     })


     $("#form-room-assign-edit").validate({
            rules: {
                channelId: {
                    required: true,
                },
                liveDate: {
                    required: true
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                layer.msg("正在导出中，耐心等待，请勿再次点击",{time:10*1000});
                //$("#save").val("正在导出").attr('disabled','disabled').css({opacity:'0.5'});
                var url = '/live/recordExport.do';
                $('<form method="post" action="' + url + '">'
                    + '<input type="text" name="channelId" value="' + $("#channelId").val() + '"/>'
                    + '<input type="text" name="liveDate" value="' + $("#liveDate").val() + '"/>'
                    + '</form>').appendTo('body').submit().remove();
                //$("#save").val("导出").removeAttr('disabled').css({opacity:'1'});
            }
        });
    });

function initDate() {
    _simple_ajax_select({
        selectId: "liveDate",
        searchUrl: '/live/findLiveDate.do',
        sData: {
            channelId:$("#channelId").val()
        },
        showText: function (item) {
            return item.cp_date;
        },
        showId: function (item) {
            return item.cp_date;
        },
        placeholder: '--请选择日期--'
    });
}