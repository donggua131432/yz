var my_tab_cache = {
    tab_base: false,
    tab_history: false,
    tab_recruit: false,
    tab_annex: false,
    tab_fee: false,
    tab_score: false,
    tab_charge: false,
    tab_modify: false
};

$(function () {
    //标签块
    $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
    $(".tabBar").find("span").click(function () {
        var id = $(this).attr('name');
        if ('base' == id) {
            loadPage('tab_base', '/recruit/toBase.do', {
                'stdId': stdId,
                'learnId': learnId,
                'recruitType': recruitType
            });
        } else if ('history' == id) {
            loadPage('tab_history', '/recruit/toHistory.do', {
                'stdId': stdId,
                'learnId': learnId,
                'recruitType': recruitType
            });
        } else if ('recruit' == id) {
            loadPage('tab_recruit', '/recruit/toRecruit.do', {
                'learnId': learnId
            });
        } else if ('annex' == id) {
            loadPage('tab_annex', '/recruit/toAnnexList.do', {
                'learnId': learnId
            });
        } else if ('fee' == id) {
            loadPage('tab_fee', '/recruit/toFee.do', {
                'learnId': learnId
            });
        } else if ('score' == id) {
            loadPage('tab_score', '/recruit/toScore.do', {
                'learnId': learnId
            });
        } else if ('charge' == id) {
            loadPage('tab_charge', '/recruit/toCharge.do', {
                'learnId': learnId
            });
        } else if ('modify' == id) {
            loadPage('tab_modify', '/recruit/toModify.do', {
                'learnId': learnId
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
        $('#' + id).empty();
        $.ajax({
            type: "GET",
            url: _url,
            data: _data,
            success: function (data) {
                $('#' + id).html(data);
                my_tab_cache[id] = true;
            }
        });
    }
}