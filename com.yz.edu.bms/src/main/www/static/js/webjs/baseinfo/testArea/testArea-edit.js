 //市缓存变量
            var cityCache;
            var districtCache;
            //县区缓存变量
            var districtCache;
            $(function() {

                //初始化考区类型下拉框
                /* _init_select("taType",dictJson.taType,$("#taType").val()); */

                //初始化省市区
                var rprZipCode = _init_area_select("provinceId", "cityId", "areaId", $("#provinceIdc").val(), $("#cityIdc").val(), $("#areaIdc").val());

                $("#areaId").change(function() {
                    var areaId = $(this).val();
                    var cityName = getValue($("#cityId").val()).replace("市", "");
                    var areaName = getValue(areaId).replace("县", "").replace("区", "");
                    $("#taName").val(cityName + areaName);
					var taCode = '';
					if(areaId) {
					    taCode = areaId;
					}
					
					$("#taCode").val(taCode)
                    
                });

                $('.radio-box input').iCheck({
                    checkboxClass : 'icheckbox-blue',
                    radioClass : 'iradio-blue',
                    increaseArea : '20%'
                });

                $("#form-member-add").validate({
                    rules : {
                        provinceId : {
                            required : true,
                        },
                        taName : {
                            required : true,
                        },
                        taCode : {
                            remote : { //验证考区编号是否重复
                                type : "POST",
                                url : '/testArea/validate.do', //servlet
                                data : {
                                    exType : function() {
                                        return $("#exType").val();
                                    },
                                    taCode : function() {
                                        return $("#taCode").val();
                                    },
                                    oldTaCode : function() {
                                        return $("#oldTaCode").val();
                                    }
                                }
                            },
                            required : true
                        },
                        isAllow : {
                            required : true,
                        }
                    },
                    messages : {
                        taCode : {
                            remote : "编号已存在，请修改后提交"
                        }
                    },
                    onkeyup : false,
                    focusCleanup : true,
                    success : "valid",
                    submitHandler : function(form) {
                        $(form).ajaxSubmit({
                            type : "post", //提交方式  
                            dataType : "json", //数据类型  
                            url : $("#exType").val() == "UPDATE" ? '/testArea/editTestArea.do' : '/testArea/insertTestArea.do', //请求url 
                            success : function(data) { //提交成功的回调函数  
                                if (data.code == _GLOBAL_SUCCESS) {
                                    layer.msg('操作成功！', {
                                        icon : 1,
                                        time : 1000
                                    },function () {
                                        if($("#exType").val() == "UPDATE"){
                                            window.parent.myDataTable.fnDraw(false);
                                        }else{
                                            window.parent.myDataTable.fnDraw(true);
                                        }
                                        layer_close();
                                    });
                                }
                            }
                        })
                    }
                });
            });

            //根据编码获取省市区相应值
            function getValue(key) {
                for (var i = 0; i < pcdJson.length; i++) {
                    if (key == pcdJson[i].provinceCode) {
                        return pcdJson[i].provinceName;
                    }
                    var city = pcdJson[i].city;
                    for (var o = 0; o < city.length; o++) {
                        if (key == city[o].cityCode) {
                            return city[o].cityName;
                        }
                        var district = city[o].district;
                        for (var p = 0; p < district.length; p++) {
                            if (key == district[p].districtCode) {
                                return district[p].districtName;
                            }
                        }
                    }
                }
                return "";
            }