$(function() {
        var action = '';
        _init_select("ruleGroup", dictJson.ruleGroup);
        //var type = [[${type}]];
       
        var ruleCodePattern = null;
        
        if('UPDATE' == type) {
            action = '/award/update.do';
            
            //var awardInfo = [[${awardInfo}]];
            $("#ruleCode").val(awardInfo.ruleCode);
            $("#ruleCode").attr("readonly", "readonly");
            
            $("#zhimiCount").val(awardInfo.zhimiCount);
            
            $("#expCount").val(awardInfo.expCount);
            $("#ruleDesc").val(awardInfo.ruleDesc);
            
            var isAllow = awardInfo.isAllow;
            $("#is_allow_" + isAllow).attr('checked', 'checked');
            
            ruleCodePattern = {required : true};
            
            _init_select("ruleGroup", dictJson.ruleGroup, awardInfo.ruleGroup);
            
            $("#startTime").val(awardInfo.startTime);
            $("#endTime").val(awardInfo.endTime);
            
            var isMutex = awardInfo.isMutex;
            $("#is_mutex_" + isMutex).attr('checked', 'checked');
            
            var isRepeat = awardInfo.isRepeat;
            $("#is_repeat_" + isRepeat).attr('checked', 'checked');
            
            $("#sort").val(awardInfo.sort);
            
            var items = awardInfo.attrList;
            var dom = '';
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
              
                dom += '<tr>';
                dom += '<td>' + item.dictName +'<input type="hidden" name="items[' + i + '].dictValue" value="' + item.dictValue + '" />';
                dom += '<input type="hidden" name="items[' + i + '].dictName" value="' + item.dictName + '" />';
                dom += '<input type="hidden" name="items[' + i + '].attrSeq" value="' + i + '" /></td>';
                var attrValue = item.attrValue ==null?'':item.attrValue;
                dom += '<td><input type="input" class="input-text radius size-M" style="width:280px" name="items[' + i + '].attrValue" value="'+attrValue+'" /></td>';
               
                dom += '</tr>';
            }
            $("#items").html(dom);
            $.each($("#itemDiv").find("input[type='number']"), function (i, data) {
                $(data).rules('add', {required: true, range: [0, 100000], isStdFee: true});
            });
            
        } else if('ADD' == type) {
            action = '/award/add.do';
            $("#is_allow_1").attr('checked', 'checked');
            $("#is_mutex_0").attr('checked', 'checked');
            $("#is_repeat_0").attr('checked', 'checked');
            
            var items = awardInfo.attrList;
            var dom = '';
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
              
                dom += '<tr>';
                dom += '<td>' + item.dictName +'<input type="hidden" name="items[' + i + '].dictValue" value="' + item.dictValue + '" />';
                dom += '<input type="hidden" name="items[' + i + '].dictName" value="' + item.dictName + '" />';
                dom += '<input type="hidden" name="items[' + i + '].attrSeq" value="' + i + '" /></td>';
                var attrValue = item.attrValue ==null?'':item.attrValue;
                dom += '<td><input type="input" class="input-text radius size-M" style="width:80px" name="items[' + i + '].attrValue" value="'+attrValue+'" /></td>';
               
                dom += '</tr>';
            }
            $("#items").html(dom);
            $.each($("#itemDiv").find("input[type='number']"), function (i, data) {
                $(data).rules('add', {required: true, range: [0, 100000], isStdFee: true});
            });
            
            ruleCodePattern = {
                    remote : { //验证用户名是否存在
                        type : "POST",
                        url : '/award/validateRuleCode.do', //servlet
                        data : {
                            'type' : type,
                            'dictId' : function() {
                                return $("#ruleCode").val();
                            }
                        }
                    },
                    required : true
                };
        } else {
            return false;
        }
       
        $('.skin-minimal input').iCheck({
            checkboxClass : 'icheckbox-blue',
            radioClass : 'iradio-blue',
            increaseArea : '20%'
        });
       
        
        $("#award-form").validate({
            rules : {
                ruleCode : ruleCodePattern,
                zhimiCount : {
                    required : true,
                    isDigits : true
                },
                expCount : {
                    required : true,
                    isDigits : true
                },
                isAllow : {
                    required : true
                }
            },
            messages : {
                ruleCode : {
                    remote : "规则已存在，请重新输入编码！"
                }
            },
            
            onkeyup : false,
            focusCleanup : true,
            success : "valid",
            submitHandler : function(form) {
                $(form).ajaxSubmit({
                    type : "post", //提交方式  
                    dataType : "json", //数据类型  
                    url : action, //请求url 
                    success : function(data) { //提交成功的回调函数  
                        var msg = '';
                    	if('UPDATE' == type) {
                    	    msg = '更新成功';
                    	} else if('ADD' == type) {
                    	    msg = '添加成功';
                    	}
                    	
                    	if(_GLOBAL_SUCCESS == data.code) {
                    	    layer.msg(msg, {
                                icon : 1,
                                time : 1000
                              }, function(){
                                  layer_close();
                              });
                    	}
                    }
                });
            }
        });
    });