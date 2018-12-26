 $(function() {
                var map = {};
                //课程类型
                _init_select("textbookType", dictJson.courseType, $("#textbookType").val());
                //初始化入学考试科目
                //_init_select("testSubject", dictJson.testSubject);

                $("#testSubject").select2({
                    placeholder : "--请选择--",
                    allowClear : true,
                    width : '100%'
                });

                if ("FD" == $("#textbookType").val()) {
                    $("#testSubjectd").show();
                }

                $("#textbookType").change(function() {
                    if ("FD" == $("#textbookType").val()) {
                        $("#testSubjectd").show();
                    } else {
                        $("#testSubjectd").hide();
                    }
                });

                // $("#testSubject").append(new Option(_findDict("testSubject", $("#testSubjecth").val()), $("#testSubjecth").val(), false, true));

                /* 
                //往年教材
                _simple_ajax_select({
                    selectId : "beYearSearch",
                    searchUrl : '[[@{/textBook/findTextBookByName.do}]]',
                    sData : {},
                    showText : function(item) {
                        map[item.textbook_id] = item;
                        return item.textbook_name;
                    },
                    showId : function(item) {
                        return item.textbook_id;
                    },
                    placeholder : '往年教材'
                });
                
                 $("#beYearSearch").change(function(){
                	alert( map[$("#beYearSearch").val()].textbook_name);
                	
                 }); */
				if($("#pub").val()) {
                  var date = new Date($("#pub").val()).format("yyyy-MM-dd");
                  $("#publisherTime").val(date);
				}

                $('.skin-minimal input').iCheck({
                    checkboxClass : 'icheckbox-blue',
                    radioClass : 'iradio-blue',
                    increaseArea : '20%'
                });

                $("#form-member-add").validate({
                    rules : {
                        textbookName : {
                            required : true,
                        },
                        textbookType : {
                            required : true,
                        },
                        isBook : {
                        	required : true,
                        }
                    },
                    onkeyup : false,
                    focusCleanup : true,
                    success : "valid",
                    submitHandler : function(form) {
                        var urls;
                        if ($("#exType").val() == "UPDATE") {
                            urls = '/textBook/updateTextBook.do';
                        } else {
                            urls = '/textBook/insertTextBook.do';
                        }
                        $(form).ajaxSubmit({
                            type : "post", //提交方式  
                            dataType : "json", //数据类型  
                            url : urls, //请求url 
                            success : function(data) { //提交成功的回调函数  
                            	if(_GLOBAL_SUCCESS == data.code){
                            		layer.msg('操作成功！', {icon : 1, time : 1000},function(){
                                        window.parent.myDataTable.fnDraw(false);
            							layer_close();
            						});
                            	}
                            	
                            },
                        })
                    }
                });
            });

            function del(e) {
                $(e).parent().remove();
            }