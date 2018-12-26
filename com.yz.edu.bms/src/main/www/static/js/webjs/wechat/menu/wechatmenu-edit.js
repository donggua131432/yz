function menuTypeChange(){
		var menuType = $("input[name='menuType']:checked").val();
		$("#pId").val(null).trigger("change");
		if(1 == menuType){
			$("#pMenuBox").css("display", "none");
		}else{
			$("#pMenuBox").css("display", "block");
		}
	}

$(function(){
	var exType = $("#exType").val();
	var url = '';
	
	$('.radio-box input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	_simple_ajax_select({
		selectId : "pId",
		searchUrl : '/wechatmenu/sMenu.do',
		sData : {
			
		},
		showText : function(item) {
			return item.menuTitle;
		},
		showId : function(item) {
			return item.id;
		},
		placeholder : '--请选择上级菜单--'
	});
	
	_simple_ajax_select({
		selectId : "belongPublic",
		searchUrl : '/wechatmenu/sPublic.do',
		sData : {
			
		},
		showText : function(item) {
			return item.pubName;
		},
		showId : function(item) {
			return item.pubId;
		},
		placeholder : '--请选择公众号--'
	});
	
	$('input:radio[name="menuType"]').on('ifChecked', function(event){
		menuTypeChange();
	});  
	
	
	if('ADD' == exType){
		url = '/wechatmenu/add.do';
        menuTypeChange();
	}else if('UPDATE' == exType){
		url = '/wechatmenu/edit.do';
        // console.log(menu);
        if(null != menu.pId && '' != menu.pId && '1'!= menu.pId){
            $("#secondMenu").iCheck('check');
            $("#pId").append(new Option(menu.pName, menu.pId, false, true));
        }else {
            $("#firstMenu").iCheck('check');
            $("#pId").val(null).trigger("change");
            $("#pMenuBox").css("display", "none");
		}
        // $("#pId").val(menu.pId).trigger("change");
        $("#belongPublic").append(new Option(menu.pubName, menu.belongPublic, false, true));
		// $("#belongPublic").val(menu.belongPublic).trigger("change");
	}
	_init_select("eventType", dictJson.eventType, menu.eventType);
	
	
	$("#form-wechatmenu-add").validate({
		rules : {
			menuTitle : {
				required : true
			},
			menuType : {
				required : true
			},
			pId : {
				required : true
			},
			menuContent : {
				required : true
			},
			weight : {
				required : true
			},
			belongPublic : {
				required : true
			},
			eventType : {
				required : true
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