var myDataTable;
  $(function() {
      _init_select('ruleType', dictJson.ruleType);
      myDataTable = $('.table-sort').dataTable({
          "serverSide" : true,
          "dom" : 'rtilp',
          "ajax" : {
              url : "/award/getRecordsList.do",
              type : "post",
              data : function(pageData) {
                  pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
                  return pageData;
              }
          },
          "pageLength" : 10,
          "pagingType" : "full_numbers",
          "ordering" : false,
          "searching" : false,
          "lengthMenu" : [ 10, 20 ],
          "createdRow" : function(row, data, dataIndex) {
              $(row).addClass('text-c');
          },
          "language" : _my_datatables_language,
          columns : [
              {"mData" : "awardId"},
              {"mData" : null},
              {"mData" : "ruleCode"},
              {"mData" : "awardDesc"},
              {"mData" : "zhimiCount"},
              {"mData" : "expCount"},
              {"mData" : null},
              {"mData" : "updateTime"}
          ],
          "columnDefs" : [
              {"targets" : 1, "class":"text-l no-warp","render" : function(data, type, row, meta) {
                  return user(row);
              }},
              {"targets" : 2,"class" : "text-l"},
              {"targets" : 3,"class" : "text-l"},
              {"targets" : 4,"class" : "text-l"},
              {"targets" : 6, "class":"text-l no-warp","render" : function(data, type, row, meta) {
                  return triggrUser(row);
              }}
          ]
      });
  });

  function _search() {
      myDataTable.fnDraw(true);
  }

  function user(data) {
      var nickname = data.nickname ? data.nickname : '';
      var yzCode = data.yzCode ? data.yzCode : '';
      var name = data.realName ? data.realName : '';
      var mobile = data.mobile ? data.mobile : '';
      var idCard = data.idCard ? data.idCard : '';
      var dom = '<ul>';
      dom += '<li>　　昵称：' + nickname + '</li>';
      dom += '<li>远智编号：' + yzCode + '</li>';
      dom += '<li>真实姓名：' + name + '</li>';
      dom += '<li>　手机号：' + mobile + '</li>';
      dom += '<li>身份证号：' + idCard + '</li>';
      dom += '</ul>';
      return dom;
  }

  function triggrUser(data) {
      var triggerNickname = data.triggerNickname ? data.triggerNickname : '';
      var triggerYzCode = data.triggerYzCode ? data.triggerYzCode : '';
      var triggerRealName = data.triggerRealName ? data.triggerRealName : '';
      var triggerMobile = data.triggerMobile ? data.triggerMobile : '';
      var triggerIdCard = data.triggerIdCard ? data.triggerIdCard : '';
      var dom = '<ul>';
      if(triggerNickname !="") dom += '<li>　　昵称：' + triggerNickname + '</li>';
      if(triggerYzCode !="") dom += '<li>远智编号：' + triggerYzCode + '</li>';
      if(triggerRealName !="") dom += '<li>真实姓名：' + triggerRealName + '</li>';
      if(triggerMobile !="") dom += '<li>　手机号：' + triggerMobile + '</li>';
      if(triggerIdCard !="") dom += '<li>身份证号：' + triggerIdCard + '</li>';
      dom += '</ul>';
      return dom;
  }