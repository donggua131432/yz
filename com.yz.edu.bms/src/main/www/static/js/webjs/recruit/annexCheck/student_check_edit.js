	var my_tab_cache = {
          tab_base : false,
          tab_history : false,
          tab_recruit : false,
          tab_annex : false,
          tab_charge : false
      };
      $(function() {
          //标签块
          $.Huitab("#tab_demo .tabBar span", "#tab_demo .tabCon", "current", "click", "0");
          $(".tabBar").find("span").click(function() {
              var id = $(this).attr('name');
              if ('base' == id) {
                  loadPage('tab_base', '/annexCheck/toBase.do', {
                      'stdId' : stdId,
                      'learnId' : learnId,
                      'recruitType' : recruitType
                  });
              } else if ('history' == id) {
                  loadPage('tab_history', '/annexCheck/toHistory.do', {
                      'learnId' : learnId,
                      'recruitType' : recruitType
                  });
              } else if ('recruit' == id) {
                  loadPage('tab_recruit', '/annexCheck/toRecruit.do', {
                      'learnId' : learnId
                  });
              } else if ('annex' == id) {
                  loadPage('tab_annex', '/annexCheck/showAnnexList.do', {
                      'learnId' : learnId
                  });
              } else if ('charge' == id) {
                  loadPage('tab_charge', '/annexCheck/toCheck.do', {
                      'learnId' : learnId,
                      'recruitType' : recruitType
                  });
              }
          });

          //点击基本信息选项
          $(".tabBar").find("span[name=annex]").click();

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