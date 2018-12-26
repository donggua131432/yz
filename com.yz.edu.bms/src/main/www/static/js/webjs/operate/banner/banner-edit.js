$(function(){
	
	
		
	var bannerType = '';
	
	var exType = $("#exType").val();
	var url = '';
	
	if('ADD' == exType){
		url = '/banner/add.do';
		bannerType = '1';
	}else if('UPDATE' == exType){
		url = '/banner/edit.do';
		var bannerUrl = banner.bannerUrl;
		
	    if (bannerUrl) {
	        createPhoto(_FILE_URL + bannerUrl + "?" + Date.parse(new Date()));
	    }
	    bannerType = banner.bannerType;
	}
	
	
	$("#bannerUrl").val(bannerUrl);
	
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
	
	_init_radio_box('bannerTypeBox', 'bannerType', dictJson.bannerType, bannerType);
	$("#form-banner-add").validate({
		rules : {
			bannerName : {
				required : true
			},
			bannerType : {
				required : true
			},
			sort : {
				required : true,
				min : 1,
				max : 1000
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : url, //请求url  
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
                 $(img).css('height', '100px')
             } else {
                 $(img).css('width', '250px')
             }
         });
         img.src = imgSrc;
         box.html(img);
     }