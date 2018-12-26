var goodList=[];
$(function(){
	
   //初始化京东商品池
   $.ajax({
         url: "/exchange/getJdPageNum.do",
         dataType : 'json',
         data : {
		 },
         success: function(data){
        	 
        	 var dictArray = [];
        	 $.each(data.body, function (index, s) {
                 if (s) {
                	 dictArray.push({
                         'dictValue': s.itemCode,
                         'dictName': s.itemName
                     });
                 }
             });
        	_init_select("goodsPageNum",dictArray);
         }
    });
    $("#goodsPageNum").append(new Option("", "", false, true));
   
	$("#goodsSelect").select2({
		placeholder : "--请选择商品--",
		allowClear : true
	});
	
	//省-市-区-街道
	$.ajax({
        url: "/purchasing/getJDProvince.do",
        dataType : 'json',
        success: function(data){
       	 var dictArray = [];
       	 $.each(data.body, function (index, s) {
                if (s) {
               	 dictArray.push({
                        'dictValue': s.code,
                        'dictName': s.name
                    });
                }
            });
       	_init_select("province",dictArray);
        }
   });
   $("#province").append(new Option("", "", false, true));
   
	$("#city").select2({
		placeholder : "--请选择市--",
		allowClear : true
	});
	$("#district").select2({
		placeholder : "--请选择区--",
		allowClear : true
	});
	$("#street").select2({
		placeholder : "--请选择街道--",
		allowClear : true
	});
	//省联动
	$("#province").change(function() {
		var pId = $(this).val();
		$("#city").empty();
		$("#city").append("<option value=''>--请选择市--</option>");
		$.ajax({
	        url: "/purchasing/getJDCity.do",
	        dataType : 'json',
	        data : {
	        	"pId":pId
	        },
	        success: function(data){
	       	 var dictArray = [];
	       	 $.each(data.body, function (index, s) {
	                if (s) {
	               	 dictArray.push({
	                        'dictValue': s.code,
	                        'dictName': s.name
	                    });
	                }
	            });
	       	_init_select("city",dictArray);
	        }
	   });
	});
	//市联动
	$("#city").change(function() {
		var pId = $(this).val();
		$("#district").empty();
		$("#district").append("<option value=''>--请选择区--</option>");
		$.ajax({
	        url: "/purchasing/getJDCounty.do",
	        dataType : 'json',
	        data : {
	        	"cId":pId
	        },
	        success: function(data){
	       	 var dictArray = [];
	       	 $.each(data.body, function (index, s) {
	                if (s) {
	               	 dictArray.push({
	                        'dictValue': s.code,
	                        'dictName': s.name
	                    });
	                }
	            });
	       	_init_select("district",dictArray);
	        }
	   });
	});
	//区联动
	$("#district").change(function() {
		var pId = $(this).val();
		$("#street").empty();
		$("#street").append("<option value=''>--请选择街道--</option>");
		$.ajax({
	        url: "/purchasing/getJDTown.do",
	        dataType : 'json',
	        data : {
	        	"tId":pId
	        },
	        success: function(data){
	       	 var dictArray = [];
	       	 $.each(data.body, function (index, s) {
	                if (s) {
	               	 dictArray.push({
	                        'dictValue': s.code,
	                        'dictName': s.name
	                    });
	                }
	            });
	       	_init_select("street",dictArray);
	        }
	   });
	});
    $("#goodsPageNum").change(function() {
		var selected = $(this).val();
		$("#goodsSelect").empty();
		$("#goodsSelect").append("<option value=''>请选择商品</option>");
		$.ajax({
	         url: "/exchange/getJdSkuByPage.do",
	         dataType : 'json',
	         data : {
					"pageNum" : function(){
						return selected;
					},
					"pageNo":"1"	
			 },
	         success: function(data){
	        	 data = data.body;
	        	 goodList=data;
	        	 for (var i = 0; i < data.length; i++) {
	        		 var goodsData = data[i];
        			$("#goodsSelect").append("<option value='" + goodsData.skuId + "' index=" + i + ">" + goodsData.salesName + "</option>");
	        	}
	         }
	    });
	  });
	 //商品下拉选择时
	  $("#goodsSelect").change(function() {
		var selectedId = $(this).val();
		
		//得到商品详情
		$.ajax({
	         url: "/exchange/getJdGoodDetail.do",
	         dataType : 'json',
	         data : {
					"skuId" : selectedId	
			 },
	         success: function(data){
	        	 var data = data.body;
	        	 if(data!=null){
		        		$("#goodsSkuId").val(selectedId);
	       				$("#goodsName").val(data.goodsName);
	       				$("#goodsPrice").val(data.originalPrice);
	       				$("#jdGoodsType").val(data.jdGoodsType);
	        	 }
	         }
	    });
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
	$("#form-buy-add").validate({
		rules:{
			goodsName :{
				required:true,
				maxlength :100
			},
			goodsSelect :{
				required:true,
				maxlength :100
			},
			goodsNum:{
				required : true,
				digits:true,
				maxlength:4  
			},
			goodsPrice:{
				required : true
			},
			applyName:{
				required : true
			},
			applyReason:{
				required : true,
				maxlength : 200  
			},
			receiveName :{
				required : true
			},
			receiveMobile:{
				required : true,
                isMobile:true,
			},
			address :{
				required : true
			},
			province :{
				required : true
			},
			city :{
				required : true
			},
			district :{
				required : true
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			if ($("#skuId").val() == '') {
                layer.msg('请先选择京东商品！', {icon: 2, time: 1000});
                return;
            }

            if ($("#selectPhoto").val() == '') {
                layer.msg('审批附件为必传！', {icon: 0, time: 1000});
                return;
            }

			$("#provinceName").val($('#province > option:selected').text());
			$("#cityName").val($('#city > option:selected').text());
			$("#districtName").val($('#district > option:selected').text());
			$("#streetName").val($('#street > option:selected').text() =='请选择'?'':$('#street > option:selected').text());
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/purchasing/buyGoods.do", //请求url  
				success : function(data) { //提交成功的回调函数  
					console.log(data.code);
					if(data.code == _GLOBAL_SUCCESS){
						layer.msg('操作成功！', {icon : 1, time : 2000},function(){
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
			$(img).css('height', '140px')
		} else {
			$(img).css('width', '140px')
		}
	});
	img.src = imgSrc;
	box.html(img);
}

function checknum(obj) {
	if (!/^\d*(\.\d{1,2})?$/.test(obj.value))
		alert("错误：必须为数值!")
}