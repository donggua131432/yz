$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$('select').select2({
		placeholder : "--请选择--",
		allowClear : true,
		width : "59%"
	});
	
	if(exType =='UPDATE'){//如果是修改操作
    	 var headPortrait = examInfo.fileUrl;
         if (headPortrait) {
             createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
         }
	}
    var url = '/studentGKUnifiedExam/insertStdGKUnifiedExamInfo.do';
	$("#form-gkUnified-edit").validate({
		rules : {
			title : {
				required : true
			},
			startTime : {
				required : true
			},
			endTime : {
				required : true
			},
			testSubject : {
				required : true,
				maxlength :20
			},
			operationDesc : {
				required : true,
				maxlength :50
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
						layer.msg('操作成功！', {icon : 1, time : 2000},function(){
							window.parent.myDataTable.fnDraw(false);
							layer_close();
						});
					}
				}
			})
		}
	});
});
function loadFile(img) {
    var filePath = img.value;
    var fileExt = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
    if (img.files && img.files[0]) {
    	var size=(img.files[0].size / 1024).toFixed(0);
		// 文件大小限制5m
    	if(size>1024*5){
    		layer.msg('文件最大为5M！',{icon:0})
            img.value = "";
            return;
		}
    }
}
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
function deletPhoto() {
    layer.confirm('确定删除？', function() {
        $('#member-photo').empty();
        $("#isPhotoChange").val('1');
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