 $(function() {
                //标签块
                $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");

                $(".tabBar").find("span").click(function() {
                    var id = $(this).attr('name');
                    if ('tab_sh' == id) {
                        loadPage('tab_sh', '/scholarship/toList.do');
                    } else if ('tab_sg' == id) {
                        loadPage('tab_sg', '/scholarship/toSGList.do');
                    }
                });
                loadPage('tab_sh', '/scholarship/toList.do');
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