var myDataTable;
      var imgArr=[];
      var imgArrObj=[];

      $(function() {
          myDataTable = $('#annexListTable').dataTable({
              "serverSide" : true,
              "dom" : 'rtp',
              "ajax" : {
                  url : "/annexCheck/getAnnexList.do",
                  type : "post",
                  data : {
                      learnId : learnId
                  }
              },
              "paging" : false,
              "ordering" : false,
              "searching" : false,
              "createdRow" : function(row, data, dataIndex) {
                  $(row).addClass('text-c');
              },
              "columns" : [
                  {"mData" : null},
                  {"mData" : "annexName"},
                  {"mData" : null},
                  {"mData" : null},
                  {"mData" : "uploadUser"},
                  {"mData" : "uploadTimeStr"},
                  {"mData" : null},
                  {"mData" : null}
              ],
              "columnDefs" : [
                  {"targets" : 0,"render" : function(data, type, row, meta) {
                          return '<input type="checkbox" value="'+ row.annexId + '" name="annexId" />';
                  }},
                  { "targets" : 2,"render" : function(data, type, row, meta) {
                      var dom = '<form method="post" enctype="multipart/form-data">';
                      dom += '<div class="addPhoto">';
                      if (row.annexUrl && row.annexUrl != '') {
                          dom += '<p class="c-666 dropPhoto" style="display:none;">点击 <a href="javascript:;" onclick="addPhoto(this)">添加</a>或将图片拖拽到此处上传</p>';
                          dom += '<input type="file" class="getPhoto" name="annexFile" style="display: none">';
                          dom += '<div>';
                          dom += '<span onclick="openPhoto(event,this)"><img class="imgChild" layer-src="' + _FILE_URL + row.annexUrl + '"  src="' + _FILE_URL + row.annexUrl + '" onerror="nofind(this)" /></span>';
                          dom += '<i class="iconfont icon-shanchu deletePhoto2" onclick="deletePhoto2(this)" ></i>';
                          dom += '</div>';
                      } else {
                          dom += '<p class="c-666 dropPhoto">点击 <a href="javascript:;" onclick="addPhoto(this)">添加</a>或将图片拖拽到此处上传</p>';
                          dom += '<input type="file" class="getPhoto" name="annexFile" style="display: none">';
                          dom += '<div>';
                          dom += '<span onclick="openPhoto(event,this)"></span>';
                          dom += '<i class="iconfont icon-shanchu deletePhoto2" onclick="deletePhoto2(this)" style="display: none"></i>';
                          dom += '</div>';
                      }
                      dom += '<input type="hidden" name="annexUrl" value="' + (row.annexUrl ? row.annexUrl : '') + '" />';
                      dom += '<input type="hidden" name="annexId" value="' + row.annexId + '" />';
                      dom += '<input type="hidden" name="learnId" value="' + row.learnId + '" />';
                      dom += '<input type="hidden" name="isRequire" value="' + row.isRequire + '" />';
                      dom += '</div>';
                      dom += '</form>';
                      return dom;
                  }},
                  {"targets" : 3,"render" : function(data, type, row, meta) {
                      var dom = '';

                      if ('1' === row.isRequire) {
                          dom = '<i class="Hui-iconfont Hui-iconfont-xuanze"></i>';
                      } else {
                          dom = '<i class="Hui-iconfont Hui-iconfont-close"></i>';
                      }
                      return dom;
                  }},
                  {"targets" : 6,"render" : function(data, type, row, meta) {
                      var className;
                      var spanText = _findDict('annexStatus', row.annexStatus);
                      switch (row.annexStatus) {
                      case '1':
                          className = 'label-default';
                          break;
                      case '2':
                          className = 'label-warning';
                          break;
                      case '3':
                          className = 'label-success';
                          if('0' == row.isRequire) {
                              spanText = '已上传';
                          }
                          break;
                      case '4':
                          className = 'label-danger';
                          break;
                      }
                      return '<span class="label ' + className + ' radius">' + spanText + '</span>';
                  }},
                  {"targets" : 7,"render" : function(data, type, row, meta) {
                      var dom = '';
                      var annexStatus = row.annexStatus;
                      if ('2' == annexStatus) {
                          dom += '<a title="通过" href="javascript:;" onclick="charge(\'' + row.learnId + '\',\'' + row.annexId
                                  + '\', \'3\')" class="ml-5" style="text-decoration: none">通过</a>';
                          dom += '<a title="驳回" href="javascript:;" onclick="charge(\'' + row.learnId + '\',\'' + row.annexId
                                  + '\', \'4\')" class="ml-5" style="text-decoration: none">驳回</a>';
                      } else if ('1' == annexStatus) {
                          dom += '<span style="color:red;">请先上传附件</span>';
                      } else if ('3' == annexStatus) {
                          dom += '<span style="color:green;">完成</span>';
                      } else {
                          dom += '<span style="color:red;">请重新上传附件【原因：' + row.reason + '】</span>';
                      }
                      return dom;
                  }}
              ],
              "drawCallback" : function(oSetting, json) {
                  //上传图片
                  document.documentElement.addEventListener('dragover', function(e) {
                      e.preventDefault();
                  });
                  document.documentElement.addEventListener('drop', function(e) {
                      e.preventDefault();
                  });

                  $('.addPhoto img').on('load', function() {
                      var height = this.height;
                      var width = this.width;
                      if (height > width) {
                          $(this).css('height', '60px');
                      } else {
                          $(this).css('width', '60px');
                      }
                  });

                  $('.dropPhoto').on('drop', function(e) {
                      var formData = new FormData($(this).closest('form')[0]);
                      if(!formData.get('annexFile')){
                          layer.msg('该浏览器不支持拖拽上传，请使用点击上传', {
                              icon: 2,
                              time: 2000
                          });
                          return
                      }
                      e = e.originalEvent;
                      var box = $(this).siblings('div').find('span');
                      upPhoto(box, e);
                      $(this).hide().siblings('div').find('span,i').show();
                  });

                  $('.getPhoto').on('change', function() {
                      upPhoto($(this).siblings('div').find('span'), this);
                      $(this).siblings('p').hide().siblings('div').find('span,i').show();
                  });

                  var imgAllArrLen=$(".imgChild").length;

                  $(".imgChild").on('load',function () {
                      if(!--imgAllArrLen){
                          // 加载完成
                          $(".imgChild").each(function (i,e) {
                              if(!$(e).attr('nofind')){
                                  imgArr.push(this.src)
                              }
                          })
                      }                    
                  });
                  // console.log(imgArr);
              }
          });

          $("#bt_submit").click(function() {
              layer_close();
          });
      });

      //图片放大预览
      function openPhoto(e,obj) {
          e.stopPropagation();
          var img =$("#photoPreview img");
          img.removeAttr('photoScale rotateIndex').css('transform','scale(1) rotate(0deg)');

          var src = $(obj).find('img').attr('src');
          var nofind=$(obj).find('img').attr('nofind');
          if(nofind){
              layer.msg('当前图片地址异常！', {icon : 0});
              return
          }
          // console.log(src);
          $('#photoPreview').fadeIn(500).find('img').attr('src', src);
      }

      //图片旋转
      function photoRotate(e) {
          e.stopPropagation();
          var img =$("#photoPreview img");
          var rotateIndex;
          if(!img.attr('rotateIndex')){
              img.attr('rotateIndex',1);
              rotateIndex=1
          }else {
              rotateIndex=parseInt(img.attr('rotateIndex'))+1;
              img.attr('rotateIndex',rotateIndex)
          }
          var deg=rotateIndex*90;
          // console.log(deg);
          img.css('transform','scale(1) rotate('+deg+'deg)');
      }

      //图片二次放大
      function photoScale(e) {
          e.stopPropagation();
          var img =$("#photoPreview img");
          if(!img.attr('photoScale')){
              img.attr('photoScale',1)
              if(!img.attr('rotateIndex')){
                  img.css('cursor','zoom-out').css('transform','scale(1.5) rotate(0deg)')
              }else {
                  var rotateIndex=img.attr('rotateIndex');
                  var deg=rotateIndex*90;
                  img.css('cursor','zoom-out').css('transform','scale(1.5) rotate('+deg+'deg)')
              }
          }else {
              if(!img.attr('rotateIndex')){
                  img.removeAttr('photoScale');
                  img.css('cursor','zoom-in').css('transform','scale(1) rotate(0deg)')
              }else {
                  var rotateIndex=img.attr('rotateIndex');
                  var deg=rotateIndex*90;
                  img.removeAttr('photoScale');
                  img.css('cursor','zoom-in').css('transform','scale(1) rotate('+deg+'deg)')
              }
          }
      }

      //图片向左翻阅
      function photoTurnLeft(e) {
          e.stopPropagation();
          var src=$("#photoPreview img").attr('src');
          var index;
          for(var i=0;i<imgArr.length;i++){
              if(imgArr[i].indexOf(src)!=-1){
                  index=i-1;
                  index=index<0?imgArr.length-1:index;
                  // console.log(index);
                  src=imgArr[index]
                  $("#photoPreview img").attr('src',src);
                  return
              }
          }
      }
      //图片向右翻阅
      function photoTurnRight(e) {
          e.stopPropagation();
          var src=$("#photoPreview img").attr('src');
          var index;
          for(var i=0;i<imgArr.length;i++){
              if(imgArr[i].indexOf(src)!=-1){
                  index=i+1;
                  index=index>imgArr.length-1?0:index;
                  // console.log(index);
                  src=imgArr[index]
                  $("#photoPreview img").attr('src',src);
                  return
              }
          }
      }

      $(document).on('click',function () {
          $('#photoPreview').fadeOut(500)
      });

      function addPhoto(obj, url, annexType) {
          $(obj).parent().siblings('.getPhoto').click();
      }

      function upPhoto(box, e) {
          var file;
          if (e.dataTransfer) {
              file = e.dataTransfer.files[0];
          } else {
              file = e.files[0];
          }
          var type = file.type;
          var fr = new FileReader();
          fr.readAsDataURL(file);

          fr.addEventListener('load', function() {
              var result = fr.result;
              if (type.indexOf('image/') > -1) {
                  createPhoto(box, result);
                  ajaxFile('/annexCheck/updateAnnexInfo.do', box, '1',file,result);
              } else {
                  layer.msg('请选择正确的图片文件格式！', {
                      icon : 0,
                      time : 1000
                  });
              }
          });
      }

      //    删除图片
      function deletePhoto2(obj) {
          var src=$(obj).siblings('span').find('img').attr('src');
          var index;
          for(var i=0;i<imgArr.length;i++){
              if(imgArr[i].indexOf(src)!=-1){
                  index=i;
              }
          }
          $(obj).hide();
          $(obj).siblings('span').html('');
          $(obj).parent().siblings('p').show();
          $(obj).parent().siblings('input[name="annexFile"]').val(null);
          imgArr.slice(index,1);
          ajaxFile('/annexCheck/delAnnexInfo.do', obj, '2');
      }

      function createPhoto(box, imgSrc) {
          $(box).html('');
          var img = document.createElement('img');
          $(img).on('load', function() {
              var height = this.height;
              var width = this.width;
              if (height > width) {
                  $(img).css('height', '60px');
              } else {
                  $(img).css('width', '60px');
              }
          });
          $(img).attr('layer-src',imgSrc);
          img.src = imgSrc;
          imgArr.push(imgSrc);
          $(box).html(img);
      }

      function ajaxFile(url, obj, type, file) {
          var $form = $(obj).closest('form');
          var success = function (data) {
              if ('00' == data.code) {
                  if ('1' == type) {
                      layer.msg('附件上传成功', {
                          icon: 1,
                          time: 1000
                      });
                  } else {
                      layer.msg('附件删除成功', {
                          icon: 1,
                          time: 1000
                      });
                  }
                  annexListTable.fnDraw(false);
              }
          };

          if (window.FormData) {
              var formData = new FormData($form[0]);
              if (type === '1' && !formData.get('annexFile').size) {
                  formData.set('annexFile', file);
              }

              $.ajax({
                  type: 'post',
                  url: url,
                  data: formData,
                  dataType: 'json',
                  processData: false,
                  contentType: false,
                  success: success
              });
          } else {
              $form.ajaxSubmit({
                  type: 'post',
                  dataType: 'json',
                  url: url,
                  success: success
              });
          }
      }

      var index;
      function charge(learnId, annexId, annexStatus) {
          if ('4' == annexStatus) {
              index = layer.open({
                  type : 1,
                  area: ['400px'],
                  title : '请输入驳回原因',
                  content : $('#reason-box').html(),
                  btn : [ '提交', '关闭' ],
                  btn1 : function(index, layero) {
                      doCharge(learnId, annexId, annexStatus, $(layero).find('textarea').val());
                  }
              });
          } else {
              doCharge(learnId, annexId, annexStatus);
          }
      }

      function doCharge(learnId, annexId, annexStatus, reason) {
          $.ajax({
              type : 'POST',
              url : '/annexCheck/charge.do',
              data : {
                  'learnId' : learnId,
                  'annexId' : annexId,
                  'annexStatus' : annexStatus,
                  'reason' : reason
              },
              dataType : 'json',
              success : function(data) {
                  if ('00' == data.code) {
                      layer.msg("审核成功", {
                          icon : 1,
                          time : 1000
                      }, function() {
                          if ('4' == annexStatus) {
                              layer.close(index);
                              $("#reason").text('');
                          }
                          myDataTable.fnDraw(false);
                      });
                  }
              },
          });
      }

      function nofind(t){
          var errorPng="/images/ucnter/avatar-default-S.gif"
          // if(window.location.href.indexOf('bms')==-1){
          //     errorPng="/images/ucnter/avatar-default-S.gif"
          // }
          $(t).attr('nofind','true')
          t.src=errorPng;
          t.onerror=null;
      }