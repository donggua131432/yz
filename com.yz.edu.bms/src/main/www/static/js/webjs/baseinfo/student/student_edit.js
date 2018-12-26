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
                $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");

                $(".tabBar").find("span").click(function() {
                    var id = $(this).attr('name');
                    if ('base' == id) {
                        loadPage('tab_base', '/studentBase/toBase.do', {
                            'stdId' : stdId,
                            'learnId' : learnId
                        });
                    } else if ('history' == id) {
                        loadPage('tab_history', '/studentBase/toHistory.do', {
                            'learnId' : learnId
                        });
                    } else if ('recruit' == id) {
                        loadPage('tab_recruit', '/studentBase/toRecruit.do', {
                            'learnId' : learnId
                        });
                    } else if ('annex' == id) {
                        loadPage('tab_annex', '/studentBase/toAnnexList.do', {
                        	't':varT,
                            'learnId' : learnId
                        });
                    } else if ('fee' == id) {
                        loadPage('tab_fee', '/studentBase/toFee.do', {
                            'learnId' : learnId
                        });
                    } else if ('score' == id) {
                        loadPage('tab_score', '/studentBase/toScore.do', {
                            'learnId' : learnId
                        });
                    } else if ('charge' == id) {
                        loadPage('tab_charge', '/studentBase/toCharge.do', {
                            'learnId' : learnId
                        });
                    } else if ('modify' == id) {
                        loadPage('tab_modify', '/studentBase/toModify.do', {
                            'learnId' : learnId
                        });
                    }
                });
                var tabName = $("#tabName").val();
                if (tabName && '' != tabName && 'undefined' != tabName) {
                    $("span[name='" + tabName + "']").trigger("click");
                    my_tab_cache[tabName] = true;
                } else {
                    loadPage('tab_base', '/studentBase/toBase.do', {
                        'stdId' : stdId,
                        'learnId' : learnId
                    });
                }
            });

            function loadPage(id, _url, _data) {
                var _switch = my_tab_cache[id];
                if (_switch && true == _switch) {

                } else {
                	var loadurl=_url+"?t="+_data["t"]+"&stdId="+_data["stdId"]+"&learnId="+_data["learnId"];
                	$('#' + id).load(loadurl,function(){
                		my_tab_cache[id] = true;
                	});
//                    $('#' + id).empty();
//                    $.ajaxLoad({
//                        type : "GET",
//                        url : _url,
//                        async: false,
//                        data : _data,
//                        success : function(data) {
//                            $('#' + id).html(data);
//                            my_tab_cache[id] = true;
//                        }
//                    });
                }
            }