var my_tab_cache = {
    tab_base : false,
    tab_history : false,
    tab_recruit : false,
    tab_annex : false,
    tab_fee : false,
    tab_score : false,
    tab_charge : false,
    tab_modify : false
};
$(function() {
    //tab默认进入页面
    var HuitabIndex=(window.parent.HuitabIndex||0)+"";
    //标签块
    $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", HuitabIndex);
    $(".tabBar").find("span").click(function() {
        var id = $(this).attr('name');
        if ('base' == id) {
            loadPage('tab_base', '/allStudent/toBase.do', {
                'stdId' : stdId,
                'learnId' : learnId,
                'recruitType' : recruitType
            });
        } else if ('history' == id) {
            loadPage('tab_history', '/allStudent/toHistory.do', {
                'learnId' : learnId,
                'recruitType' : recruitType
            });
        } else if ('recruit' == id) {
            loadPage('tab_recruit', '/allStudent/toRecruit.do', {
                'learnId' : learnId
            });
        } else if ('annex' == id) {
            loadPage('tab_annex', '/allStudent/toAnnexList.do', {
                'learnId' : learnId
            });
        } else if ('fee' == id) {
            loadPage('tab_fee', '/allStudent/toFee.do', {
                'learnId' : learnId
            });
        } else if ('score' == id) {
            loadPage('tab_score', '/allStudent/toScore.do', {
                'learnId' : learnId
            });
        } else if ('charge' == id) {
            loadPage('tab_charge', '/allStudent/toCharge.do', {
                'learnId' : learnId
            });
        } else if ('modify' == id) {
            loadPage('tab_modify', '/allStudent/toModify.do', {
                'learnId' : learnId
            });
        }
    });

    //点击基本信息选项
    $(".tabBar").find("span[name=base]").click();
});

function loadPage(id, _url, _data) {
    var _switch = my_tab_cache[id];
    if (_switch && true == _switch) {

    } else {
//        $('#' + id).empty();
        var loadurl=_url+"?stdId="+_data["stdId"]+"&learnId="+_data["learnId"]+"&recruitType="+_data["recruitType"];
    	$('#' + id).load(loadurl,function(){
    		my_tab_cache[id] = true;
    	});
//        $.ajax({
//            type : "GET",
//            url : _url,
//            data : _data,
//            success : function(data) {
//                $('#' + id).html(data);
//                my_tab_cache[id] = true;
//            }
//        });
    }
}