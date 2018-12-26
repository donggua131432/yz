var my_tab_cache = {
					tab_all : false,            
					tab_tuition : false,   
                    tab_zm : false,   
                    tab_demurrage : false,   
                    tab_cash : false
            };
            $(function() {
//                tab默认进入页面
//                var HuitabIndex=(window.parent.HuitabIndex||0)+"";
//                console.log(HuitabIndex);
                //标签块
                $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");

                $(".tabBar").find("span").click(function() {
                    var id = $(this).attr('name');
                    if ('all' == id) {
                        loadPage('tab_all', '/stdFee/toAllSerials.do', {
                            'stdId' : varStdId
                        });
                    } else if ('tuition' == id) {
                        loadPage('tab_tuition', '/stdFee/toTuition.do', {
                            'stdId' : varStdId
                        });
                    } else if ('zm' == id) {
                        loadPage('tab_zm', '/stdFee/toZMAccount.do', {
                        	'stdId' : varStdId
                        });
                    } else if ('demurrage' == id) {
                        loadPage('tab_demurrage', '/stdFee/toDemurrageAccount.do', {
                        	'stdId' : varStdId
                        });
                    } else if ('cash' == id) {
                        loadPage('tab_cash', '/stdFee/toCashAccount.do', {
                        	'stdId' : varStdId
                        });
                    }
                });

               
//                console.log(tabName);
                if (tabName && '' != tabName && 'undefined' != tabName) {
                    $("span[name='" + tabName + "']").trigger("click");
                } else {
                    loadPage('tab_all', '/stdFee/toAllSerials.do', {
                        'stdId' : varStdId
                    });
                }

            });

            function loadPage(id, _url, _data) {
                var _switch = my_tab_cache[id];
                if (_switch && true == _switch) {

                } else {
                    $('#' + id).empty();
                    $.ajax({
                        type : "GET",
                        url : _url,
                        data : _data,
                        success : function(data) {
                            $('#' + id).html(data);
                            my_tab_cache[id] = true;
                        }
                    });
                }
            }