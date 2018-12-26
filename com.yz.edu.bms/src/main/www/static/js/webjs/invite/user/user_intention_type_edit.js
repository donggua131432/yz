$(function() {
      _init_select("intentionType",[
          {"dictValue": "", "dictName": "--请选择--"},
          {"dictValue": "A", "dictName": "A"},
          {"dictValue": "B", "dictName": "B"},
          {"dictValue": "C", "dictName": "C"}
      ], intentionType);

      $("#form-base-info").validate({
          rules : {
              intentionType:{required:true}
          },
          onkeyup:false,
          focusCleanup:true,
          success:"valid",
          submitHandler : function(form) {
              $(form).ajaxSubmit({
                  type : "post", //提交方式
                  dataType : "json", //数据类型
                  url : '/invite_user/intentionType.do', //请求url
                  success : function(data) {
                      //提交成功的回调函数
                      if ('00' == data.code) {
                          layer.msg('信息保存成功', {icon : 1,time : 1000}, function(){
                              window.parent.myDataTable.fnDraw(false);
                              layer_close();
                          });
                      }
                  }
              });
          }
      });
  });
