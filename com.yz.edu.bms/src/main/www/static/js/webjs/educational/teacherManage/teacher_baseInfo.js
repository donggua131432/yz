$(function() {
                //        图片上传
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
                
                //定义院校变量，以免重复获取
            	var campusIdJson;
                var campusName;
             	_simple_ajax_select({
            			selectId : "campusId",
            			searchUrl : "/campus/selectList.do",
            			sData : {},
            			showText : function(item) {
            				return item.campusName;
            			},					
            			showId : function(item) {
            				return item.campusId;
            			},
            			placeholder : '--请选择校区--'
            	});
                $.ajax({
                       type: "POST",
                       dataType: "json",
                       url: "/campus/selectList.do",
                       success: function(data){
                    	   campusIdJson = data.body.data;
                      	   for(var i=0;i<campusIdJson.length;i++){
                      		 if(campusIdJson[i].campusId == $("#campusIds").val()){
                      			campusName = campusIdJson[i].campusName;
             		                    $("#campusId").append(new Option(campusName, $("#campusIds").val(), false, true));
                      		 }
                      	 }
                      }
                });
                
                $("#empName").val(varBaseInfo.empName);
                $("#idCard").val(varBaseInfo.idCard);
                $("#birthdayInput").val(varBaseInfo.birthday);
                $("#mobile").val(varBaseInfo.mobile);
                
                _init_select("teachCertType",dictJson.teachCertType,varothInfo.teachCertType);
                _init_select("idType", dictJson.idType,varBaseInfo.idType);
                _init_select("sexSelect", dictJson.sex,varBaseInfo.sex);
                _init_select("nation", dictJson.nation, varBaseInfo.nation);
                _init_select("politicalStatus", dictJson.politicalStatus,varBaseInfo.politicalStatus);
                _init_select("teachEducation",dictJson.teachEducation,varothInfo.teachEducation);
                
                $("#address").val(varBaseInfo.address);
                
                if(exType =='UPDATE'){//如果是修改操作
                	 var headPortrait = varothInfo.headPortrait;

                     if (headPortrait) {
                         createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
                     }
                     
                    if($("#idType").val()==1 && varBaseInfo.idCard !=null){
                    	$("#idCard").attr("disabled", "disabled");
                    }
                }
               
                
                $("#headPortrait").val(varothInfo.headPortrait);
                $("#telephone").val(varothInfo.telephone);
                $("#jobTitle").val(varothInfo.jobTitle);
                $("#email").val(varothInfo.email);
                $("#qq").val(varothInfo.qq);
                $("#degree").val(varothInfo.degree);
                $("#finishSchool").val(varothInfo.finishSchool);
                $("#finishTime").val(varothInfo.finishTime);
                $("#finishMajor").val(varothInfo.finishMajor);
                $("#remark").val(varothInfo.remark);
                $("#workPlace").val(varothInfo.workPlace);
                $("#hourFee").val(varothInfo.hourFee);
                $("#otherFee").val(varothInfo.otherFee);
                $("#bankCard").val(varothInfo.bankCard);
                $("#teach").val(varothInfo.teach);
                $("#teachIdea").val(varothInfo.teachIdea);
                $("#teachEote").val(varothInfo.teachEote);
                $("#ageInput").val(varothInfo.age);
                $("#professionalTime").val(varothInfo.professionalTime);
                
                var nowZipCode = _init_area_select("provinceCode", "cityCode", "districtCode",varBaseInfo.provinceCode,varBaseInfo.cityCode,varBaseInfo.districtCode);
                $("#idType").change(function() {
					if ($(this).val() == 1) {
						$("#birthdayInput").attr("disabled", "disabled");
						$("#sexSelect").attr("disabled", "disabled");
						$("#ageInput").attr("disabled","disabled");
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
							$("#ageInput").val(msg.age);
						});
					} else {
					    $("#sexSelect").removeAttr("disabled");
						$("#birthdayInput").removeAttr("disabled");
						$("#ageInput").removeAttr("disabled");
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
                        	remote:{
                        		type : 'POST',
            					url : "/recruiter/ifExistInfoByEmpType.do",
            					dataType : 'json',
            					data : {
            						idType : function() {
            							return $("#idType").val();
            						},
            						idCard : function() {
            							return $("#idCard").val();
            						},
            						empType : function() {
            							return $("#empType").val();
            						}
            					}
                        	},
                        	required : true
                        },
                        sex : {
                        	required : true
                        },
                          birthdayInput : {
                        	required : true
                        },
                          ageInput : {
                              required : true
                          },
                        empType : {
                        	required : true
                        },
                        mobile :{
                        	required : true,
                            isMobile : true
                        },
                        campusId:{
                        	required : true
                        },
                        provinceCode : {
                            required : true
                        },
                        cityCode : {
                            required : true
                        },
                        districtCode : {
                            required : true
                        },
                        address : {
                            required : true,
                            isAddress:true,
                            maxlength : 125
                        }, 
                        hourFee :{
                    		required :true
                    	}, 
                    	otherFee :{
                    		required :true
                    	}, 
                    	jobTitle:{
                    		required :true
                    	}, 
                    	degree:{
                    		required :true
                    	}, 
                    	finishSchool:{
                    		required :true
                    	}, 
                    	finishTime:{
                    		required :true
                    	}, 
                    	finishMajor:{
                    		required :true
                    	}, 
                    	workPlace:{
                    		required :true
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
                    focusInvalid:false,
                    success : "valid",
                    submitHandler : function(form) {
                    	$("#sex").val($("#sexSelect").val());
						$("#birthday").val($("#birthdayInput").val());
						$("#age").val($("#ageInput").val());
						
                        $(form).ajaxSubmit({
                            type : "post", //提交方式  
                            dataType : "json", //数据类型  
                            url : "/teacher/baseUpdate.do", //请求url  
                            success : function(data) { //提交成功的回调函数
                                if ('00' == data.code) {
                                	if($("#exType").val() =='Add'){
                                		$("#topEmpId").val(data.body);
                                    	$("#topExType").val('UPDATE');
                                	}
                                    layer.msg('教师基本信息保存成功', {
                                        icon : 1,
                                        time : 1000
                                    });
                                }
                            }
                        });
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