 $(function () {
          var type = $("#type").val();
          
            var validateUrl = '/mpjar/validate.do'
          
          $("#jar-form").validate({
              rules: {
                  jarName: {
                      remote: { //验证用户名是否存在
                          type: "POST",
                          url: validateUrl, //servlet
                          data: {
                              'type': type,
                              'jarName': function () {
                                  return $("#jarName").val();
                              }
                          }
                      },
                      required: true
                  },
                  registerClass: {required: true},
                  stageClass: {required: true},
                  chargeClass: {required: true},
                  jarDesc: {required: true},
                  isAllow: {required: true}
              },
              messages: {
                  ruleCode: {
                      remote: "jar包已存在，请重新输入jar包名称！"
                  }
              },
              onkeyup: false,
              focusCleanup: true,
              success: "valid",
              submitHandler: function (form) {
                  
                    var ajaxUrl =  '/mpjar/add.do'
                  
                  $(form).ajaxSubmit({
                      type: "post", //提交方式
                      dataType: "json", //数据类型
                      url: ajaxUrl, //请求url
                      success: function (data) { //提交成功的回调函数
                          if (_GLOBAL_SUCCESS == data.code) {
                              layer.msg('添加成功', {
                                  icon: 1,
                                  time: 1000
                              }, function () {
                                  layer_close();
                              });
                          }
                      }
                  });
              }
          });
      });