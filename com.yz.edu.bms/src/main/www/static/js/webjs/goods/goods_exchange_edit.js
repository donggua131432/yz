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
   
    //初始化兑换周期
    _init_select("intervalSelect",[
		                               {"dictValue":"1","dictName":"1天"},
		                               {"dictValue":"2","dictName":"2天"},
		                               {"dictValue":"3","dictName":"3天"},
		                               {"dictValue":"5","dictName":"5天"},
		                               {"dictValue":"7","dictName":"7天"},
		                               {"dictValue":"10","dictName":"10天"},
		                               {"dictValue":"365","dictName":"一年"}
		                               ],varGoodsInfo.interval);
	$("#goodsSelect").select2({
		placeholder : "--请选择商品--",
		allowClear : true
	});
	var ue = UE.getEditor('goodsContent');
	

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
	        	 //console.log(data);
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
		        		$("#skuId").val(selectedId);
		        		$("#jdGoodsType").val(data.jdGoodsType);
	       				$("#goodsName").val(data.goodsName);
	       				$("#salesName").val(data.goodsName);
	       				$("#costPrice").val(data.originalPrice);
			  			$("#originalPrice").val(data.costPrice);
	       				var headPortrait = data.coverUrlPortrait;
	       		        if (headPortrait) {
	       		            createPhoto( headPortrait + "?" + Date.parse(new Date()));
	       		        }
	       		        $("#coverUrlPortrait").val(headPortrait);
	       		        $("#goodsDesc").val(data.goodsDesc);
	       		        if(data.goodsContent !=null){
	       		        	var ue = UE.getEditor('goodsContent');
	       		        	ue.setContent(data.goodsContent);
	       		        }
	        	 }
	  			
	         }
	    });
		
		/*var selected=$.map(goodList,function(item){
			if(item.skuId===selectedId){
				return item;
			}
		});
		//console.log(selected[0].goodsContent)
		if(selected.length > 0){
			$("#skuId").val(selectedId);
			$("#goodsName").val(selected[0].goodsName);
			$("#salesName").val(selected[0].goodsName);
			$("#costPrice").val(selected[0].costPrice);
			$("#originalPrice").val(selected[0].originalPrice);
			var headPortrait = selected[0].coverUrlPortrait;
	        if (headPortrait) {
	            createPhoto( headPortrait + "?" + Date.parse(new Date()));
	        }
	        $("#coverUrlPortrait").val(headPortrait);
	        $("#goodsDesc").val(selected[0].goodsDesc);
	        if(selected[0].goodsContent !=null){
	        	ue.setContent(selected[0].goodsContent);
	        }
		}*/
		
	 });
	 
	 if(exType =='UPDATE'){//如果是修改操作
    	 var headPortrait = varGoodsInfo.annexUrl;

         if (headPortrait) {
             createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
         }
         $("select").attr("disabled", true);
         $("#coverUrlPortrait").val(varGoodsInfo.annexUrl);
    }
    
    ue.addListener("ready", function () {
        // editor准备好之后才可以使用
        if(varGoodsInfo.goodsContent !=null){
          ue.setContent(varGoodsInfo.goodsContent);	
        }
        

    });
    if(exType =='LOOK'){//如果是查看操作,禁用
    	$("select").attr("disabled", true);
    	$('input:not("#bt_cancel")').attr('disabled',true);
    	var headPortrait = varGoodsInfo.annexUrl;
        if (headPortrait) {
             createPhoto(headPortrait + "?" + Date.parse(new Date()));
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
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$("#form-exchange-info").validate({
		rules:{
			goodsName :{
				required:true,
				maxlength :100
			},
			goodsSelect :{
				required:true,
				maxlength :100
			},
			goodsCount:{
				required : true,
				digits:true,
				maxlength:4  
			},
			costPrice:{
				required : true
			},
			originalPrice:{
				required : true
			},
			goodsDesc:{
				required : true,
				maxlength : 200  
			},
			salesPrice :{
				required : true,
				digits:true
			},
			onceCount:{
				required : true,
				digits:true
			},
			showSeq :{
				required : true,
				digits:true
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
			//赋值
			$("#interval").val($("#intervalSelect").val());
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/exchange/exchangeUpdate.do", //请求url  
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
                    $("#coverUrlPortrait").val("");
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
            
            function reset_good(){
            	var skuId=$("#skuId").val();
            	if(skuId==null||skuId.length==0){
            		layer.msg('京东商品Id为空，不能还原为为京东商品！', {
                        icon : 1
                    });
            	}
            	
            	$.ajax({
       	         url: '/exchange/getJdGoodDetail.do',
       	         dataType : 'json',
       	         data : {
       					"skuId" : function(){
       						return $("#skuId").val();
       					}
       			 },
       	         success: function(data){
       	        	var data = data.body;
       				$("#goodsName").val(data.goodsName);
       				$("#jdGoodsType").val(data.jdGoodsType);
       				$("#salesName").val(data.goodsName);
       				$("#costPrice").val(data.originalPrice);
		  			$("#originalPrice").val(data.costPrice);
       				var headPortrait = data.coverUrlPortrait;
       		        if (headPortrait) {
       		            createPhoto( headPortrait + "?" + Date.parse(new Date()));
       		        }
       		        $("#coverUrlPortrait").val(headPortrait);
       		        $("#goodsDesc").val(data.goodsDesc);
       		        if(data.goodsContent !=null){
       		        	var ue = UE.getEditor('goodsContent');
       		        	ue.setContent(data.goodsContent);
       		        }
       		     	$("#isPhotoChange").val('1');
       		     	layer.msg('还原成功！', {icon : 1, time : 1000},function(){
						
					});
       		    
       	         }
       	    });
            	
            	
            	
           }
            
            function checknum(obj){
                if(!/^\d*(\.\d{1,2})?$/.test(obj.value)) alert("错误：必须为数值!")
            }