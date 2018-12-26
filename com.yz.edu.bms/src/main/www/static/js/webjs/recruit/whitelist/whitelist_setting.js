$(function(){
        var discountPlans = discountPlans_global;
        var whitelist = whitelist_global;
        
        for(var i=0;i<discountPlans.length;i++) {
            var dp = discountPlans[i];
            var isChecked = false;
        	if(whitelist && whitelist.length > 0) {    
              for(var j=0;j<whitelist.length;j++) {
                  if(whitelist[j] == dp) {
                      isChecked = true;
                      break;
                  }
              }
        	}
            
            dom = '<div class="check-box">';
            dom += '<input type="checkbox" id="scholarships_' + i + '" name="scholarships" ' + (isChecked ? 'checked="checked" ' : '') + ' value="' + dp + '" />';
            dom += '<label for="scholarships_' + i + '">' + _findDict('scholarship', dp) + '</label>';
            dom += '</div>';
            
            $(".skin-minimal").append(dom);
        }
        
        $('.skin-minimal input').iCheck({
        		checkboxClass: 'icheckbox-blue',
        		radioClass: 'iradio-blue',
        		increaseArea: '20%'
        	});
        
        $("#scholarships_all").on("ifChanged", function(event){
            var checked = $(this).prop('checked');
            if(checked) {
                $('input[name="scholarships"]').iCheck('check');  
            } else {
                $('input[name="scholarships"]').iCheck('uncheck');  
            }
        });
        
        $("#bt_submit").click(function(){
            $("#wl_setting_form").ajaxSubmit({
                type : "post", //提交方式  
                dataType : "json", //数据类型  
                url : '/whitelist/setting.do', //请求url  
                success : function(data) { //提交成功的回调函数
                    if (_GLOBAL_SUCCESS == data.code) {
                        layer.msg('白名单信息设置成功', {
                            icon : 1,
                            time : 1000
                        }, function(){
                            layer_close();
                        });
                    }
                }
            });
        });
    });