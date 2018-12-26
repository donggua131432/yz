$(function() {
    //标签块
    $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");

    $(".tabBar").find("span").click(function() {
        var id = $(this).attr('name');
        if ('tab_sh' == id) {
            $('#tab_sg').html("");
            loadPage('tab_sh', '/sysLog/toBmsLog.do');
        } else if ('tab_sg' == id) {
            $('#tab_sh').html("");
            loadPage('tab_sg', '/sysLog/toBccLog.do');
        }
    });
    loadPage('tab_sh', '/sysLog/toBmsLog.do');
});

function loadPage(id, _url) {
        $('#' + id).empty();
        $.ajax({
            type : "GET",
            url : _url,
            success : function(data) {
                $('#' + id).html(data);
            }
        });
}