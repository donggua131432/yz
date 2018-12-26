$(function(){
	_init_checkbox("goodsUse","goodsUse",
		  [{
		     "dictValue":"1","dictName":"兑换"
	      }],
	      varGoodsInfo.goodsUse);
	 initMap();//初始化地图
	 var ue = UE.getEditor('goodsContent');
	 if(exType =='UPDATE'){//如果是修改操作
    	 var headPortrait = varGoodsInfo.annexUrlPortrait;

         if (headPortrait) {
             createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
         }
         var annexList= varGoodsInfo.annexList;
       	if(annexList !=null){
       		var dom = '';
       		for(var i=0;i<annexList.length;i++){
       			dom +='<div class="file-item" data-path="'+annexList[i].annexUrl+'">';
       			dom +='<a class="cancel" href="javascript:;">&times;</a>';
       	     	dom +='<a href="'+annexList[i].annexUrl+'" data-lightbox="example1"><img src="'+(_FILE_URL+annexList[i].annexUrl)+'"/></a>'
       	        dom +='</div>';

       		}
       	   $("#fileList").html(dom);
       	}
       	
    }
    $("#annexUrlPortrait").val(varGoodsInfo.annexUrlPortrait);
    ue.addListener("ready", function () {
        // editor准备好之后才可以使用
        if(varGoodsInfo.goodsContent !=null){
          ue.setContent(varGoodsInfo.goodsContent);
        }
    });
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
                 alert('请选择正确的图片文件格式！');
             }
         });
     });
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$("#form-goods-add").validate({
		rules:{
			goodsName:{
				required : true,
				maxlength : 100  
			},
			goodsCount:{
				required : true,
				digits:true,
				maxlength:4  
			},
			costPrice:{
				required : true,
				digits:true
			},
			originalPrice:{
				required : true,
				digits:true
			},
			goodsDesc:{
				required : true,
				maxlength : 200  
			},
			takein:{
				required : true,
				digits:true,
				maxlength:2
			},
			address:{
				required : true,
				maxlength : 100
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			 var items = [];
	         $('#fileList > .file-item').each(function () {
	        	 var isNew = '0';
	        	 if($(this).attr('id')){
	        		 isNew = '1'
	        	 }
	        	 var data={'path':$(this).data('path'),'id':isNew}
	             items.push(data);
	         });
	         $("#coverUrls").val(JSON.stringify(items));	
             if ($("#location").val() == null) {
                 layer.msg('请在地图上选择精确活动地址！', {icon: 2, time: 1000});
                 $("#address").focus();
                 return false;
             } 
             //$("#location").val(map.marker.point.lng+',' +map.marker.point.lat);
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/goods/goodsUpdate.do", //请求url  
				success : function(data) { //提交成功的回调函数  
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 1000},function(){
							layer_close();
						});
					}
				}
			})
		}
	
	});
	
});
function initMap() {
	var map = new BMap.Map("addressMap");

	map.enableScrollWheelZoom();//启用地图滚轮放大缩小
	var geoc = new BMap.Geocoder();    
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			var mk = new BMap.Marker(r.point);
			map.addOverlay(mk);
			map.panTo(r.point);
			//alert('您的位置：'+r.point.lng+','+r.point.lat);
			var point = new BMap.Point(r.point.lng,r.point.lat); //默认深圳
			map.centerAndZoom(point,11);
			map.enableScrollWheelZoom(true);
			if(exType =='UPDATE'){
				map.clearOverlays(); 
				var point = varGoodsInfo.location;
				var pointStr = point.split(',');
				var new_point = new BMap.Point(pointStr[0],pointStr[1]);
				var marker = new BMap.Marker(new_point);  // 创建标注
				map.addOverlay(marker);              // 将标注添加到地图中
				map.panTo(new_point); 
			}
			 
			map.addEventListener("click", function(e){        
				var pt = e.point;
				geoc.getLocation(pt, function(rs){
					var addComp = rs.addressComponents;
					//var addr = "当前位置：" + addComp.province + addComp.city +addComp.district + addComp.street + addComp.streetNumber + "<br/>";  
				    //var addr = "纬度: " + pt.lat + ", " + "经度：" + pt.lng; 
				   // alert(addr);
				    $("#address").val(addComp.province + addComp.city +addComp.district + addComp.street + addComp.streetNumber);
				    $("#location").val(pt.lng+',' +pt.lat);
					
				});        
			});
			
		}
		else {
			alert('failed'+this.getStatus());
		}        
	},{enableHighAccuracy: true});
	
}
            function deletPhoto() {
                layer.confirm('确定删除？', function() {
                    $('#member-photo').empty();
                    layer.msg('已删除', {
                        icon : 1
                    });
                })
            }

            function createPhoto(imgSrc) {
                var box = $("#member-photo");
                box.empty();
                var img = document.createElement('img');
                $(img).on('load', function() {
                    var height = this.height;
                    var width = this.width;
                    if (height > width) {
                        $(img).css('height', '140px')
                    } else {
                        $(img).css('width', '140px')
                    }
                });
                img.src = imgSrc;
                box.html(img);
            }
  var uploader = WebUploader.create({// 上传控件
      auto: true,
      swf: '/js/webuploader/Uploader.swf',
      server: '/bms/file/webuploader.do',
      pick: '#filePicker',
      fileNumLimit: 4,
      accept: {// 只允许选择图片文件
          title: 'Images',
          extensions: 'gif,jpg,jpeg,bmp,png',
          mimeTypes: 'image/gif,image/jpeg,image/bmp,image/png'
      }
  });
  uploader.on('fileQueued', function (file) { // 添加文件到队列
      var $list = $('#fileList');
      var $li = $('<div id="' + file.id + '" data-path="" class="file-item thumbnail"><a href="javascript:;" class="cancel">&times;</a></div>');
      $list.append($li);
      uploader.makeThumb(file, function (error, src) {// 创建缩略图
          if (error) {
              $li.append('<span>不能预览</span>');
              return;
          }
          $li.append('<img src="' + src + '"/>');
      }, 100, 100);
      $li.on('click', '.cancel', function () {// 删除图片
          uploader.removeFile(file);
          $li.remove();
      });
  }).on('uploadProgress', function (file, percentage) { // 文件进度
      var $li = $('#' + file.id);
      var $percent = $li.find('.progress');
      if (!$percent.length) {// 避免重复创建
          $percent = $('<div class="progress"></div>').appendTo($li);
      }
      $percent.text('已上传：' + parseInt(percentage * 100) + '%');
  }).on('uploadSuccess', function (file, response) {
      var $li = $('#' + file.id);
      $li.find('.progress').text('上传成功');
      $li.data("path", response._raw);
  }).on('uploadError', function (file) {
      var $li = $('#' + file.id);
      $li.find('.progress').text('上传失败');
  }).on('error', function (type) {
      if (type == 'Q_EXCEED_NUM_LIMIT') {
          layer.msg('最多只能上传4张图片！', {icon: 2, time: 1000});
      }
  }); 
//删掉原来图片
  $('.fileList').on('click', '.cancel', function () {
      $(this).parents('.file-item').remove();
  });