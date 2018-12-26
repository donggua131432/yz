$(function() {
          var recruitType = recruitType_global;

          $("#unvsName").val(history_global.unvsName);
          $("#adminssionTime").val(history_global.adminssionTime);
          $("#graduateTime").val(history_global.graduateTime);
          $("#profession").val(history_global.profession);
          $("#diploma").val(history_global.diploma);
          $("#unvsName").val(history_global.unvsName);
          _init_select("edcsType", dictJson.edcsType, history_global.edcsType);
          _init_select("edcsSystem", dictJson.edcsSystem, history_global.edcsSystem);
          _init_area_select("oldProvinceCode", "oldCityCode", "oldDistrictCode", history_global.oldProvinceCode, history_global.oldCityCode, history_global.oldDistrictCode);

          $("#subject").val(history_global.subject);
          $("#subjectCategory").val(history_global.subjectCategory);
          _init_select("studyType", dictJson.studyType,history_global.studyType);
          _init_select("materialType", dictJson.materialType,history_global.materialType);
          $("#materialCode").val(history_global.materialCode);

          $("#form-history-info").validate({
              rules : {
                  unvsName : {maxlength : 25},
                  edcsSystem : {maxlength : 125}
              },
              onkeyup : false,
              focusCleanup : true,
              success : "valid",
              submitHandler : function(form) {
                  
                  var url = '/' + updateUrl;
                  $(form).ajaxSubmit({
                      type : "post", //提交方式
                      dataType : "json", //数据类型
                      url : url, //请求url
                      success : function(data) { //提交成功的回调函数
                          if ('00' == data.code) {
                              layer.msg('学员学历信息保存成功', {
                                  icon : 1,
                                  time : 1000
                              });
                          }
                      }
                  });
              }
          });

          if(recruitType === '2') {
              var isOpenUnvs = history_global.isOpenUnvs;
              $("#isOpenUnvs-" + isOpenUnvs).attr("checked", "checked");
              $(":radio[name='isOpenUnvs']").rules("add", {required:true});
              $('.skin-minimal input').iCheck({
                  checkboxClass: 'icheckbox-blue',
                  radioClass: 'iradio-blue',
                  increaseArea: '20%'
              });
          }

          showByRecruitType(recruitType);
          $("#edcsType").change(function () {
              showByRecruitType(recruitType);
          });
      });

      function showByRecruitType(recruitType) {
          if(recruitType==='2'){
              var edcsType = $("#edcsType").val(),undergraduate="679";
              if(edcsType!="" && undergraduate.indexOf(edcsType)>=0){
                  $("#subject, #subjectCategory, #studyType, #materialType, #materialCode").parent().parent().removeClass('invisible');
                  $("#profession, #diploma").parent().prev().find("span").show();
              }else{
                  $("#subject, #subjectCategory, #studyType, #materialType, #materialCode").parent().parent().addClass('invisible');
                  $("#profession, #diploma").parent().prev().find("span").hide();
              }
          }
      }