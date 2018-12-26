var myDataTable;
	    var testarrId = [];
	    
		$(function() {
			
			//声明数组移除方法
			Array.prototype.removeByValue = function(val) {
				  for(var i=0; i<this.length; i++) {
				    if(this[i] == val) {
				      this.splice(i, 1);
				      break;
				    }
				  }
				}
		    //声明数组会否存在当前元素方法
			Array.prototype.contantValue = function(val) {
				  for(var i=0; i<this.length; i++) {
				    if(this[i] == val) {
				      return true;
				    }
				  }
				    return false;
				}
		
			//初始化拉框
			/*  var parent = $("#dpId");
			 for (var i = 0; i < dictJson.dpName.length; i++) {
					var option = $("<option>").text(dictJson.dpName[i].dictName).val(dictJson.dpName[i].dictValue);
				     if($("#dpId").val() == dictJson.dpName[i].dictValue)
				    	 option.attr("selected","true");
					parent.append(option);
			} */
			
			//标签块
			$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
			
			$('.skin-minimal input').iCheck({
				checkboxClass : 'icheckbox-blue',
				radioClass : 'iradio-blue',
				increaseArea : '20%'
			});

			$("#form-plan-add").validate({
				rules : {
					dpId : {
						required : true
					},
					timeSlot : {
						required : true,
						minlength: 4,
						maxlength : 6,
						number : true,
						digits : true
					},
					targetCount : {
						required : true,
						maxlength : 10,
						number : true,
						digits : true
					},
					minCount : {
						required : true,
						maxlength : 10,
						number : true,
						digits : true
					}
				},
				messages: {  
					timeSlot: {  
					    required: "这是必填字段",  
					    minlength: "请输入正确的长度"  
					   }
					  
				}, 
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						url : '/recruit/edit.do', //请求url  
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
			
			
			/**
		     数据表格开始
		*/
		myDataTable = $('.table-sort').dataTable(
				{
					"serverSide" : true,
					"dom" : 'rtilp',
					"ajax" : {
						url : "/sysLogInfo/findAllSysLogInfo.do",
						type : "post",
						data : {
							"method" : "editOaPlan"
						}
					},
					"pageLength" : 10,
					"pagingType" : "full_numbers",
					"ordering" : false,
					"searching" : false,
					"createdRow" : function(row, data, dataIndex) {
						$(row).addClass('text-c');
					},
					"language" : {
						"emptyTable" : "没有检索到数据！",
						"zeroRecords" : "没有检索到数据！",
						"lengthMenu" : "每页显示 _MENU_ 条"
					},
					columns : [ {
						"mData" : "userName"
					}, {
						"mData" : "method"
					}, {
						"mData" : null
					}],
					"columnDefs" : [
						{
							"render" : function(data, type, row, meta) {
								return new Date(row.updateTime).format('yyyy-MM-dd hh:mm:ss');
							},
							"targets" : 2
						}]
				});
				/**
		     数据表格结束
		*/
		});