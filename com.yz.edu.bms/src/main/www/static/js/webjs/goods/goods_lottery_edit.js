$(function(){
	
	_init_select("intervalSelect",[
	                               {"dictValue":"1","dictName":"1天"},
	                               {"dictValue":"2","dictName":"2天"},
	                               {"dictValue":"3","dictName":"3天"},
	                               {"dictValue":"5","dictName":"5天"},
	                               {"dictValue":"7","dictName":"7天"},
	                               {"dictValue":"10","dictName":"10天"},
	                               {"dictValue":"15","dictName":"15天"},
	                               {"dictValue":"20","dictName":"20天"},
	                               {"dictValue":"30","dictName":"30天"},
	                               {"dictValue":"365","dictName":"一年"}
	                               ],varGoodsInfo.interval);
	
	$("#goodsSelect").select2({
		placeholder : "--请选择商品--",
		allowClear : true
	});
	$("#goodsSelect").empty();
	$("#goodsSelect").append("<option value=''>请选择商品</option>");
	$.ajax({
         url: "/exchange/selectAllList.do",
         dataType : 'json',
         data : {
				"goodsType" : function(){
					return "1";
				}
		 },
         success: function(data){
        	 data = data.body;
        	 for (var i = 0; i < data.length; i++) {
    			var goodsData = data[i];
    			$("#goodsSelect").append("<option value='" + goodsData.goodsId + "' index=" + i + ">" + goodsData.goodsName + "</option>");
        	}
         }
    });
	 var ue = UE.getEditor('goodsContent');
	 var ue2 = UE.getEditor('salesRules');
	 //商品下拉选择时
	  $("#goodsSelect").change(function() {
		var selected = $(this).val();
		$("#goodsId").val(selected);
		//请求后台
		$.ajax({
	         url: "/exchange/getGoodsDetail.do",
	         dataType : 'json',
	         data : {
					"goodsId" : function(){
						return selected;
					}
			 },
	         success: function(data){
	        	data = data.body;
	        	$("#goodsName").val(data.goodsName);
	        	$("#salesName").val(data.goodsName);
	        	$("#goodsCount").val(data.goodsCount);
	        	$("#costPrice").val(data.costPrice);
	        	$("#originalPrice").val(data.originalPrice);
	        	var headPortrait = data.annexUrlPortrait;
	            if (headPortrait) {
	                createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
	            }
	            $("#annexUrlPortrait").val(data.annexUrlPortrait);
	            $("#goodsDesc").val(data.goodsDesc);
	            if(data.goodsContent !=null){
                	ue.setContent(data.goodsContent);
                }
	         }
	    });
		
	 });
	 
	 if( exType=='UPDATE'){//如果是修改操作
    	 var headPortrait = varGoodsInfo.annexUrl;

         if (headPortrait) {
             createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
         }
         $("#goodsSelect").attr("disabled", "disabled");
         $("#goodsCount").val($("#totalCount").val()*$("#winnerCount").val());
         $("#goodsSelect").empty();
			$("#goodsSelect").append("<option value=''>请选择商品</option>");
			$.ajax({
		         url: "/exchange/selectAllList.do",
		         dataType : 'json',
		         data : {
						"goodsType" : function(){
							return "1";
						}
				 },
		         success: function(data){
		        	 data = data.body;
		        	 for (var i = 0; i < data.length; i++) {
	        			var goodsData = data[i];
	        			if (varGoodsInfo.goodsId == goodsData.goodsId) {
	    					$("#goodsSelect").find("option:selected").removeAttr("selected");
	    					$("#goodsSelect").append("<option value='" + goodsData.goodsId  + "' index=" + i + " selected='selected'>" + goodsData.goodsName + "</option>");
	        			}else{
	        				$("#goodsSelect").append("<option value='" + goodsData.goodsId + "' index=" + i + ">" + goodsData.goodsName + "</option>");	
	        			}
		        	}
		         }
		    });
    }
    $("#annexUrlPortrait").val(varGoodsInfo.annexUrl);
    if(varGoodsInfo.planStatus == 3){
    	$("#curCount").text(varGoodsInfo.curCount==null?'0':varGoodsInfo.curCount);	
    }else{
    	$("#curCount").text(varGoodsInfo.curCount==null?'0':varGoodsInfo.curCount-1);
    }
    
    ue.addListener("ready", function () {
        // editor准备好之后才可以使用
        if(varGoodsInfo.goodsContent !=null){
        	ue.setContent(varGoodsInfo.goodsContent);
        }

    });
    ue2.addListener("ready", function () {
        // editor准备好之后才可以使用
        if(varGoodsInfo.salesRules !=null){
        	ue2.setContent(varGoodsInfo.salesRules);	
        }

    });
    if(exType =='LOOK'){//如果是查看操作,禁用
    	$('input:not("#bt_cancel")').attr('disabled',true);
    	$("select").attr("disabled", true);
    	var headPortrait = varGoodsInfo.annexUrl;

        if (headPortrait) {
             createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
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
	$("#form-lottery-info").validate({
		rules:{
			goodsName : {
				required : true,
				maxlength : 100
			},
			salesName : {
				required : true,
				maxlength : 100
			},
			goodsCount : {
				required : true,
				digits:true
			},
			costPrice : {
				required : true,
				digits:true
			},
			originalPrice : {
				required : true,
				digits:true
			},
			goodsDesc : {
				required : true,
				maxlength : 200
			},
			totalCount : {
				required : true,
				digits:true
			},
			runCount : {
				required : true,
				digits:true
			},
			winnerCount : {
				required : true,
				digits:true
			},
			salesPrice : {
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
			//赋值
			$("#interval").val($("#intervalSelect").val());
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : "/lottery/lotteryUpdate.do", //请求url  
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
                        $(img).css('height', '140px')
                    } else {
                        $(img).css('width', '140px')
                    }
                });
                img.src = imgSrc;
                box.html(img);
            }
            
            function showLotteryResult(){
            	var salesId = $("#salesId").val();
            	var url = '/lottery/showLotteryResult.do' + '?salesId='+ salesId;
            	layer_show('查看往期中奖记录', url, null, 510, function() {
            		myDataTable.fnDraw(true);
            	});
            }
            function calculateGoodsCount(){
            	$("#goodsCount").val($("#totalCount").val()*$("#winnerCount").val());
            }