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
			
			 $(function() {
            	
                //图片上传
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
                $("#empName").val(varBaseInfo.empName);
                $("#idCard").val(varBaseInfo.idCard);
                $("#birthdayInput").val(varBaseInfo.birthday);
                
                _init_select("idType", dictJson.idType,varBaseInfo.idType);
                _init_select("sexSelect", dictJson.sex,varBaseInfo.sex);
                _init_select("education", dictJson.edcsType,varBaseInfo.education);
                _init_select("nation", dictJson.nation, varBaseInfo.nation);
                _init_select("politicalStatus", dictJson.politicalStatus,varBaseInfo.politicalStatus);
                _init_select("rprType", dictJson.rprType,varBaseInfo.rprType);
                _init_select("maritalStatus", dictJson.maritalStatus,varOther.maritalStatus);
                _init_select("empType", dictJson.empType,varBaseInfo.empType);
                
                $("#address").val(varBaseInfo.address);
                
                if(varExType =='UPDATE'){//如果是修改操作
                	 var headPortrait = varOther.headPortrait;

                     if (headPortrait) {
                         createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
                     }
                    if($("#idType").val()==1 && varBaseInfo.idCard !=null){
                    	$("#idCard").attr("disabled", "disabled");
                    }
                }
                $("#headPortrait").val(varOther.headPortrait);
                $("#telephone").val(varOther.telephone);
                $("#emergencyContact").val(varBaseInfo.emergencyContact);
                $("#emergencyMobile").val(varBaseInfo.emergencyMobile);
                $("#email").val(varOther.email);
                $("#qq").val(varOther.qq);
                $("#wechat").val(varOther.wechat);
                $("#englishLevel").val(varOther.englishLevel);
                $("#computerLevel").val(varOther.computerLevel);
                $("#finishSchool").val(varOther.finishSchool);
                $("#finishMajor").val(varOther.finishMajor);
                $("#finishTime").val(varOther.finishTime);
                $("#remark").val(varOther.remark);
                $("#finishCode").val(varOther.finishCode);
                $("#mobile").val(varBaseInfo.mobile);
                
                var rprZipCode = _init_area_select("rprProvinceCode", "rprCityCode", "rprDistrictCode",varBaseInfo.rprProvinceCode,varBaseInfo.rprCityCode,varBaseInfo.rprDistrictCode);
                var nowZipCode = _init_area_select("provinceCode", "cityCode", "districtCode",varBaseInfo.provinceCode,varBaseInfo.cityCode,varBaseInfo.districtCode);
                $("#idType").change(function() {
					if ($(this).val() == 1) {
						$("#birthdayInput").attr("disabled", "disabled");
						$("#sexSelect").attr("disabled", "disabled");
						$("#idCard").bind("change", function() {
							if (($(this).val().length != 15) && ($(this).val().length != 18)) {
								 layer.msg('输入的身份证号码长度应该是18位或15位，请检查', {
                                     icon : 2,
                                     time : 4000
                                 });
						        return false;
						    }
							var msg = _resolve_card2($(this).val());
							_init_select("sexSelect", dictJson.sex, msg.sex);
							$("#birthdayInput").val(msg.birthday);
						});
					} else {
					    $("#sexSelect").removeAttr("disabled");
						$("#birthdayInput").removeAttr("disabled");
						$("#idCard").removeAttr("disabled");
						$("#idCard").unbind("change");
					}
				});
                
                
                $("#form-student-info").validate({
                      rules : {
                    	empName :{
                    		required :true
                    	},                    
                        idType : {
                            required : true
                        },
                        idCard : {
                        	required : true
                        },
                        sex : {
                        	required : true
                        },
                        mobile :{
                        	required : true,
                            isMobile : true
                        },
                        birthday : {
                        	required : true
                        },
                        empType : {
                        	required : true
                        },
                        rprType : {
                            required : true
                        },
                        rprProvinceCode : {
                            required : true
                        },
                        rprCityCode : {
                            required : true
                        },
                        provinceCode : {
                            required : true
                        },
                        cityCode : {
                            required : true
                        },
                        address : {
                            required : true,
                            maxlength : 125
                        },                      
                        email : {
                            email : true
                        },
                        qq : {
                            isQq : true
                        },
                        remark : {
                            maxlength : 100
                        }
                    },  
                    messages : {
                    	idCard : {
        				remote : "该证件号已经存在信息,请核查是否正确！"
        			   }
        		    },
                    onkeyup : false,
                    focusCleanup : true,
                    success : "valid",
                    submitHandler : function(form) {
                    	$("#sex").val($("#sexSelect").val());
						$("#birthday").val($("#birthdayInput").val());
						
                        $(form).ajaxSubmit({
                            type : "post", //提交方式  
                            dataType : "json", //数据类型  
                            url : '/recruiter/baseUpdate.do', //请求url  
                            success : function(data) { //提交成功的回调函数
                                if ('00' == data.code) {
                                	if($("#exType").val() =='Add'){
                                		$("#topEmpId").val(data.body);
                                    	$("#topExType").val('UPDATE');
                                    	$("#empId").val(data.body);
                                    	$("#exType").val('UPDATE');
                                	}
                                    layer.msg('招生老师基本信息保存成功', {
                                        icon : 1,
                                        time : 1000
                                    });
                                }
                            }
                        });
                    }
                });

            });