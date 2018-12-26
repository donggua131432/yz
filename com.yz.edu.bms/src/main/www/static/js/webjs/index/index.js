 $(function () {
        if(window.localStorage.isDefaultPsw=='1'){
            window.localStorage.isDefaultPsw='0';
            var userId = $("#userId").val();
            var url = '/user/toUserPwd.do' + '?userId=' + userId;
            layer.confirm('您的密码为初始密码，请设置新密码哦~', {icon: 0, title:'温馨提示',btn: ['立即修改','知道了'],offset: '36%'}, function(index){
                layer.close(index);
                layer_show('修改密码', url, 600, 290, function (index) {
                    layer_close(index);
                });
            },function (index) {
                layer.close(index);
            });
        }
        
        // 判断环境切换
        if(window.location.href.indexOf('new.yzou')==-1){
            $('.navbar').css('background-color','#3C8DBC')
            $('.navbar a.logo').css('background-color','#367FA9')
        }
        /*$("#min_title_list li").contextMenu('Huiadminmenu', {
         bindings: {
         'closethis': function(t) {
         if(t.find("i")){
         t.find("i").trigger("click");
         }
         },
         'closeall': function(t) {
         alert('Trigger was '+t.id+'\nAction was Email');
         },
         }
         });*/

//        侧边栏效果
        $('.menu_dropdown a').on('click', function () {
            $(this).addClass('current').parent().siblings().find('a').removeClass('current').parents('dl').siblings().find('a').removeClass('current');
        })

//        顶部tab触发侧边栏
        $(document).on("click", "#min_title_list li", function () {
            var name = $(this).find('span').html();
            if (name == '我的桌面') {
                $('.selected').click();
                return;
            };

            $('.menu_dropdown a').removeClass('current').each(function (i, e) {
                if ($(e).attr("data-title") == name) {
                    if ($(e).parents('dd').siblings('dt').hasClass('selected')) {
                        $(e).addClass("current");
                    } else {
                        $(e).addClass("current").parents('dd').siblings('dt').click();
                    }
                }
            });
        })

        var userAgent = navigator.userAgent;
        if (userAgent.indexOf("Safari") > -1) {
            $('.Hui-aside .menu_dropdown dd li a').addClass('safari')
        }

    });
    /*个人信息*/
    function myselfinfo() {
        layer.open({
            type: 1,
            area: ['300px', '200px'],
            fix: false, //不固定
            maxmin: true,
            shade: 0.4,
            title: '查看信息',
            content: '<div>管理员信息</div>'
        });
    }

    /*资讯-添加*/
    function article_add(title, url) {
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*图片-添加*/
    function picture_add(title, url) {
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*产品-添加*/
    function product_add(title, url) {
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*用户-添加*/
    function member_add(title, url, w, h) {
        layer_show(title, url, w, h);
    }

    function changePwd() {
        var userId = $("#userId").val();
        var url = '/user/toUserPwd.do' + '?userId=' + userId;
        layer_show('修改密码', url, 600, 290, function () {
            layer_close();
        });
    }

    function toIndex(){
        window.location.href = '/index.do';
    }