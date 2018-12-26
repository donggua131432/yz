 $(function() {
        var sc = "studentConcats";
        showPageInfo(sc);
        //图片上传
        $('#selectPhoto').on('change', function() {
          var file = this.files[0];
          var type = file.type;
          var box = $('#member-photo');
          var fr = new FileReader();
          fr.readAsDataURL(file);
          $(fr).on('load', function() {
            var result = fr.result;
            if (type.indexOf('image/') > -1) {
              createPhoto(result);
              $("#isPhotoChange").val('1');
            } else {
              layer.msg('请选择正确的图片文件格式！', {
                icon : 1,
                time : 1000
              });
            }
          });
        });

        $("#form-contacts-info").validate({
          rules : {
            mobile : {
              required : true,
              isMobile : true
            },
            emergencyContact : {
              required : true,
              isTel : true
            },
            telephone : {
              isTel : true
            },
            email : {
              email : true
            },
            qq : {
              isQq : true
            }
          },
          onkeyup : false,
          focusCleanup : true,
          success : "valid",
          submitHandler : function(form) {
            $(form).ajaxSubmit({
              type : "post", //提交方式  
              dataType : "json", //数据类型  
              url : "/jStudying/updateContacts.do", //请求url  
              success : function(data) { //提交成功的回调函数
                if ('00' == data.code) {
                  layer.msg('学员联系信息更新成功', {
                    icon : 1,
                    time : 1000
                  }, function() {
                    layer_close();
                  });
                }
              }
            });
          }
        });
        
        $("#bt_cancel").click(function(){
          layer_close();
        });
        
      });
  function showPageInfo(sc) {
	  if(sc.headPortrait) {
    	createPhoto(_FILE_URL + sc.headPortrait + "?" + Date.parse(new Date()));
    	$("#headPortrait").val(sc.headPortrait);
      }
	  
	  $("#stdName").text(sc.stdName);
	  $("#idType").text(_findDict("idType", sc.idType));
	  $("#idCard").text(sc.idCard);
	  var rprProvinceCode = sc.rprProvinceCode;
	  var rprCityCode = sc.rprCityCode;
	  var rprDistrictCode = sc.rprDistrictCode;
	  
	  var saProvinceCode = sc.saProvinceCode;
	  var saCityCode = sc.saCityCode;
	  var saDistrictCode = sc.saDistrictCode;
	  var saAddress = sc.saAddress;
	  
	  var rprAddressText = _findProvinceName(rprProvinceCode)
	  
	  $("#rprAddress").text(concatAddress(rprProvinceCode, rprCityCode, rprDistrictCode));
	  $("#receiveAddress").text(concatAddress(saProvinceCode, saCityCode, saDistrictCode, saAddress));
	  $("#mobile").val(sc.mobile);
	  $("#emergencyContact").val(sc.emergencyContact);
	  $("#telephone").val(sc.telephone);
	  $("#email").val(sc.email);
	  $("#qq").val(sc.qq);
	  $("#wechat").val(sc.wechat);
	  $("#workPlace").val(sc.workPlace);
  }
      function deletPhoto() {
        layer.confirm('确定删除？', function() {
          $('#member-photo').empty();
          $("#headPic").val(null);
          $("#isPhotoChange").val("1");
          layer.msg('已删除', {
            icon : 1
          });
        });
      }

      function createPhoto(imgSrc) {
        var box = $("#member-photo");
        box.empty();
        var img = document.createElement('img');
        $(img).on('load', function() {
          var height = this.height;
          var width = this.width;
          if (height > width) {
            $(img).css('height', '140px');
          } else {
            $(img).css('width', '140px');
          }
        });
        img.src = imgSrc;
        box.html(img);
      }
      
      function concatAddress(provinceCode, cityCode, districtCode, address) {
        var provinceName = _findProvinceName(provinceCode);
        var cityName = _findCityName(provinceCode, cityCode);
        var districtName = _findDistrictName(provinceCode, cityCode, districtCode);
       
        var text =  provinceName ? provinceName : '';
        text += cityName ? cityName : '';
        text += districtName ? districtName : '';
        text += address ? address : '';
        return text;
      }