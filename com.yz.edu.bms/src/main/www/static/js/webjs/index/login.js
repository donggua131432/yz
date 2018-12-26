$(function() {

			/* $.validator.addMethod("userName", function(value, element) {       
			   return this.optional(element) || /^[\u4e00-\u9fa5]{2,6}$|^[a-zA-Z]{4,12}$/gi.test(value);       
			}, "输入2-4个汉字或者4-20个英文。"); */

			$("#login-form")
					.validate(
							{
								rules : {
									username : {
										required : true
									},
									password : {
										required : true,
										minlength : 6,
										maxlength : 16
									},
									validCode : {
										required : true,
										rangelength : [ 4, 4 ]
									}
								},
								onkeyup : false,
								focusCleanup : true,
								success : "valid",
								submitHandler : function(form) {
								    var psw=$("#password").val()
									$(form)
											.ajaxSubmit(
													{
														type : "post", //提交方式  
														dataType : "json", //数据类型  
														url : '/login.do', //请求url  
														success : function(data) { //提交成功的回调函数
                                                            if ('00' === data.code) {
                                                                if(psw=='123456'){
                                                                    window.localStorage.isDefaultPsw='1'
                                                                }else{
                                                                    window.localStorage.isDefaultPsw='0'
																}
																window.location.href = '/index.do';
															} else if ('E000032' == data.code
																	|| 'E000033' == data.code) {
																freshValidCode();
																$("#validCode")
																		.val('');
															} else if ('E000031' == data.code) {
																freshValidCode();
																$("#validCode")
																		.val('');
																$("#username")
																		.val('');
																$("#password")
																		.val('');
																$("#username")
																		.focus();
															}
														}
													})
								}
							});

			$('#validCodeImg').click(function() {
				freshValidCode();
			});

			/*  $(".yanzheng").on('keyup',function(){
			 	if($(this).val().length>=4){
			       $('.submit').click();
			 	}
			}); */
		});

		function freshValidCode() {
			$('#validCodeImg').attr(
					"src",
					'/validCode.do' + "?timestamp="
							+ Date.parse(new Date()));
		}