function startTimeChange(){
				$("#expireTime").val('');
			}
			
			function getFeeItem(recruitType){
				
				$.ajax({
					type : "POST",
					url : "/baseinfo/sFeeItem.do",
					data : {
						sId : recruitType
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							var items = data.body;
							for (var i = 0; i < items.length; i++) {
								var dom = '';
								var item = items[i];
								dom += '<tr>';
								var itemName = getItemName(item.itemName,$("#grade").val());
								var discountTypeId = 'items['+i+'].discountType';
								dom += '<td>'+item.itemCode+':'+itemName+'<input type="hidden" name="items['+i+'].itemCode" value="'+item.itemCode+'" /></td>';
								dom += '<td><select class="select" size="1" id="'+discountTypeId+'" name="'+discountTypeId+'">';
								dom += '<option value="1" index="1">减免 -</option>';
								dom += '<option value="3" index="3">折扣 *</option>';
								dom += '</select></td>';
								
								dom += '<td><input type="number" min="0" class="input-text radius size-M" style="width:80px" value="0.00" id="'+item.itemCode+'" name="items['+i+'].amount"';
								if("Y1" == item.itemCode){
									dom += 'onchange="inputofferName()" /></td>';
								}else{
									dom += '/></td>';
								}
								dom += '</tr>';
								$("#items").append(dom);
							}
							$.each($("#itemDiv").find("input[type='number']"), function(i, data){
								$(data).rules('add', {required : true, range : [0,100000], isStdFee : true});
							});
							$('select').select2({
			                    placeholder : "--请选择--",
			                    allowClear : false,
			                    width : "59%"
			                });
						}
					}
				});
			}
		var myDataTable;
		var pfsnIds = [];
		var taIds = [];
		
		function ztreeCheckAll(tree,checkDom,type){
	    	var isCheck = $("#"+checkDom).val();
	    	if('0' == isCheck){
	    		var treeObj = $.fn.zTree.getZTreeObj(tree);
	        	treeObj.checkAllNodes(true);
	        	repushNodes(treeObj.getCheckedNodes(true),isCheck,type);
	        	$("#"+checkDom).val('1');
	    	}else{
	    		var treeObj = $.fn.zTree.getZTreeObj(tree);
	        	treeObj.checkAllNodes(false);
	        	repushNodes(treeObj.getCheckedNodes(false),isCheck,type);
	        	$("#"+checkDom).val('0');
	    	}
	    	
	    	
	    }
	    
	    
	    function repushNodes(nodes,checked,type){
	    	
	    	taIds.splice(0,taIds.length);
	    	if('pfsn' == type){
	    		pfsnIds.splice(0,pfsnIds.length);
			}
	    	if ('0' == checked) {
	    		for(var i=0;i<nodes.length;i++){
	    			if('pfsn' == type){
		            	pfsnIds.push(nodes[i].id);
	    			}else if('ta' == type){
	    				taIds.push(nodes[i].id);
	    			}
	        	}
	        }
	    	if('pfsn' == type){
	    		init_ta_ztree();
			}
	    }
		
		$(function() {
			
			$('.formControls input').iCheck({
				checkboxClass: 'icheckbox-blue',
				radioClass: 'iradio-blue',
				increaseArea: '20%'
			})
			
			$("#form-offer").validate({
				rules : {
					grade : {
						required : true
					},
					unvs : {
						required : true,			
					},
					offerName : {
						required : true,
					},
					startTime : {
						required : true,
					},
				 	scholarship: {
	                    required: true,
	                },
					expireTime : {
						required : true,
					},
					inclusionStatus : {
						required : true,
					}
				},
				messages : {
					
				},
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					//if("ADD" == $("#exType").val()){
						if(false == checkPfsnIds(pfsnIds) || false == chekcTaIds(taIds)){
							return;
						} 
					//}
					$("#inclusionStatus").attr("disabled", false);
					
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						data : {
							pfsnIds : function(){
								return pfsnIds;
							},
							taIds : function(){
								return taIds;
							}
						},
						url : editUrl, 
						success : function(data) { //提交成功的回调函数  
								if(data.code == _GLOBAL_SUCCESS){
									if("ADD" == $("#exType").val()){
										var groupId = "offer:insert";
										layer.confirm('新增成功！是否关闭页面？', {
											  btn: ['继续操作', '关闭页面'] //可以无限个按钮
											}, function(index, layero){
												var url = "/baseinfo/resetToken.do";
												_resetToken(groupId,url);
												layer.closeAll('dialog');
											}, function(index){
											  	layer_close();
											});
									}else{
										layer.msg('修改成功！', {icon : 1, time : 1000},function(){
                                            window.parent.myDataTable.fnDraw(false);
											layer_close();
										});
									}
								}
						}
					});
				}
				
			});
			
			
			var unvsName = '';
	        var unvsId = '';
	        var grade = '';
	        var scholarship = '';
	        var inclusionStatus = '';
	        
	        if(null != pfsnInfo && pfsnInfo.length > 0){
	        	unvsName = pfsnInfo[0].unvsName;
	        	unvsId = pfsnInfo[0].unvsId;
	        	grade = pfsnInfo[0].grade;
	        	scholarship = pfsnInfo[0].scholarship;
	        	inclusionStatus = pfsnInfo[0].inclusionStatus;
	        }
	        _simple_ajax_select({
				selectId : "unvs",
				searchUrl : "/baseinfo/sUnvs.do",
				sData : {
					/* ext2 : function(){
						return $("#pfsnLevel").val();
					} */
				},
				showText : function(item) {
					var text = '[' + _findDict('recruitType', item.recruitType) + ']';
					text += item.unvsName + '(' + item.unvsCode + ')';
					return text;
				},
				showId : function(item) {
					return item.unvsId;
				},
	            checkedInfo : {
	            	name : unvsName,
	            	value : unvsId + ''
	            },
	            placeholder: '--请选择院校--'
			});
	        
	      //初始化专业
			//$("#unvs").append(new Option(unvsName, unvsId, false, true));
	        
	        if(null == status){
				status = '1';
			}
	        
	      //初始化年级下拉框
	        _init_select("grade", dictJson.grade, grade);

	       // _init_select("pfsnLevel", dictJson.pfsnLevel, pfsnLevel);

	        _init_select("scholarship", dictJson.scholarship, scholarship);

	        _init_radio_box("statusRadio", "status", dictJson.status, status);
	        
	        if(!inclusionStatus){
	        	inclusionStatus = '1';
	        }
			
	        _init_select("inclusionStatus", dictJson.inclusionStatus, inclusionStatus);
	        
	        var pfsnDom = '';
	        
	        var taDom = '';
	        
			if($("#exType").val() == "UPDATE"){
				
					$("#unvs").removeAttr("disabled");
		        	//$("#pfsnLevel").removeAttr("disabled");
					var dictArray = dictJson['discountType'];
					
					for (var i = 0; i < items.length; i++) {
						var itemDom = '';
						var item = items[i];
						itemDom += '';
						itemDom += '<tr>';
						var itemName = getItemName(item.itemName,pfsnInfo[0].grade);
						itemDom += '<td>'+ item.itemCode + ':' + itemName +':</td><input type="hidden" name="items['+ i +'].itemCode" value="' + item.itemCode + '" />';
						var discountTypeId = 'items['+i+'].discountType';
						var discountType = item.discountType;
						itemDom += '<td><select class="select" size="1" id="'+discountTypeId+'" name="'+discountTypeId+'">';
						
						for (var j = 0; j < dictArray.length; j++) {
							var dict = dictArray[j];
				  			if(discountType == dict.dictValue) {
				  				itemDom +="<option value='" + dict.dictValue + "' index=" + i + " selected='selected' >" + dict.dictName + "</option>";
							} else {
								itemDom +="<option value='" + dict.dictValue + "' index=" + i + ">" + dict.dictName + "</option>";
							}
						} 
						
						itemDom += '</select></td>';
						itemDom += '<td><input type="number" class="input-text radius size-M" style="width:80px" name="items[' + i + '].amount" value="' + item.defineAmount + '" id="' + item.itemCode + '" /></td>';
						itemDom += '</tr>';
						
						$("#items").append(itemDom);
					}
					
					$('select').select2({
	                    placeholder : "--请选择--",
	                    allowClear : false,
	                    width : "59%"
	                });
					
					$.each($("#itemDiv").find("input[type='number']"), function(i, data){
						$(data).rules('add', {required : true, range : [0,100000], isStdFee : true});
					});
					
					for (var i = 0; i < pfsnInfo.length; i++) {
		    			pfsnIds.push(pfsnInfo[i].pfsnId);
		    			pfsnDom += '('+ pfsnInfo[i].pfsnCode +')'+pfsnInfo[i].pfsnName + '[' + _findDict('pfsnLevel',pfsnInfo[i].pfsnLevel) + ']' + "，";
		    		}
		    		pfsnDom = pfsnDom.substr(0, pfsnDom.length - 1);
		    		
		    		 for (var i = 0; i < taInfo.length; i++) {
		     			taIds.push(taInfo[i].taId);
		     			taDom += taInfo[i].taName + "，";
		     		}
		    		 taDom = taDom.substr(0, taDom.length - 1);
		    		 init_ta_ztree();
		    		 $("#offerName").val(offerName);
		    		 init_pfsn_ztree();
		    		 init_ta_ztree();
				
				
			}
			$("#taztree").hide();
			$("#pfsnztree").hide();
			
			var editUrl = '';
			if("UPDATE" == $("#exType").val()){
				editUrl = "/offer/edit.do";
			}else{
				editUrl = "/offer/add.do";
			}
			
			 $("#pfsnCheckDiv").html(pfsnDom);
		        $("#taCheckDiv").html(taDom);
		        
		        $("#unvs").change(function(){
		        	unvsOnChange();
		     });
			
		});
		
		
		function checkPfsnIds(pfsnIds){
			if(null == pfsnIds || pfsnIds.length < 1){
				layer.msg('专业不能为空！', {
					icon : 2,
					time : 1000
				});
				return false;
			}
		}
		
		function chekcTaIds(taIds){
			if(null == taIds || taIds.length < 1){
				layer.msg('考区不能为空！', {
					icon : 2,
					time : 1000
				});
				return false;
			}
		}
		
		//考试地区显示事件
		function showPfsn(event) {
			var unvs = $("#unvs").val();
			if(null == unvs || '' == unvs){
				$.Huimodalalert('请选择院校',2000);
				return ;
			}
			event.stopPropagation();
			if(null != pfsnIds && pfsnIds.length > 0){
	        	$('#pfsnztree').siblings('span.btn').text('选取');
	            $("#pfsnztree").show();
	        } else {
				if ($('#user_tree').text() == '') {
					init_pfsn_ztree();
					if ($('#user_tree').text() == ''){
						$.Huimodalalert('没有找到相关专业',2000);
					}
				} else {
					$('#pfsnztree').siblings('span.btn').text('选取');
					$("#pfsnztree").show();
				}
	        }
		}
		
		//考试地区显示事件
		function showTa(event) {
			
			if(null == pfsnIds || '' == pfsnIds || pfsnIds.length < 1){
				$.Huimodalalert('请选择专业',2000);
				return ;
			}
			
			event.stopPropagation();
			//init_ta_ztree();
			if ($('#ta_tree').text() == '') {
					$.Huimodalalert('没有找到相关考区',2000);
			} else {
				$('#taztree').siblings('span.btn').text('选取');
				$("#taztree").show();
			}
		}
		
		function unvsOnChange(){
			init_pfsn_ztree();
			pfsnIds = [];
			init_ta_ztree();
			var unvsId = $("#unvs").val();
			var url = "/ztree/sUnvs.do";
			if(null != unvsId){
				//Ajax调用处理
				$.ajax({
					type : "POST",
					url : url,
					data : {
						unvsId : $("#unvs").val()
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							data = data.body;
							var recruitType = data.recruitType;
							getFeeItem(recruitType);
						}
					}
				});
			}else{
				$("#item").html("");
			}
			inputofferName();
		}
		
		
		function init_pfsn_ztree(){
	        $('#pfsnCheckDiv').html("");
			/**
			   ztree模块开始
			 */
			var zTree;
			var setting = {
				view : {
					dblClickExpand : false,//双击节点时，是否自动展开父节点的标识
					showLine : true,//是否显示节点之间的连线
					fontCss : {
						'color' : 'black'
					},//字体样式函数
					selectedMulti : false
				//设置是否允许同时选中多个节点
				},
				check : {
					//chkboxType: { "Y": "ps", "N": "ps" },
					chkStyle : "checkbox",//复选框类型
					enable : true
				//每个节点上是否显示 CheckBox 
				},
				data : {
					simpleData : {//简单数据模式
						enable : true,
						idKey : "id",
						pIdKey : "pId",
						rootPId : ""
					}
				},
				callback : {
					onClick : function(e,treeId, treeNode) {
						zTree = $.fn.zTree.getZTreeObj("user_tree");
//						psfnZTreeOnCheck(null,treeId, treeNode);
						zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
					},
					onCheck : psfnZTreeOnCheck
				}
			};

			//对复选框进行响应
			function psfnZTreeOnCheck(event, treeId, treeNode) {
					if (treeNode.checked) {
						pfsnIds.push(treeNode.id);
					} else {
						pfsnIds.removeByValue(treeNode.id);
					}
					taIds.splice(0,taIds.length);
					init_ta_ztree();
			};

			var url = "/ztree/sPfsn.do";
				//Ajax调用处理
				$.ajax({
					type : "POST",
					url : url,
					data : {
						sId : $("#unvs").val(),
						//ext1 : $("#pfsnLevel").val(),
						ext2 : $("#grade").val(),
		                pfsnIds : pfsnIds
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							data = data.body;
								var zTree = $.fn.zTree.init($("#user_tree"), setting, data);
		  				 		$('#pfsnztree').siblings('span.btn').text('选取'); 
						}
					}
				});
			/**
			 ztree模块结束
			 */
		}
		
		/* function pfsnLevelOnChange(){
			$("#unvs").val(null).trigger("change");
			var pfsnLevel = $("#pfsnLevel").val();
			if(null != pfsnLevel && '' != pfsnLevel){
				$("#unvs").removeAttr("disabled");
			}else{
				$("#unvs").attr("disabled","disabled");
			}
			init_pfsn_ztree();
		} */
		
		function init_ta_ztree(){
			$('#taCheckDiv').html("");
			/**
			   ztree模块开始
			 */
			var zTree;
			var setting = {
				view : {
					dblClickExpand : false,//双击节点时，是否自动展开父节点的标识
					showLine : true,//是否显示节点之间的连线
					fontCss : {
						'color' : 'black'
					},//字体样式函数
					selectedMulti : false
				//设置是否允许同时选中多个节点
				},
				check : {
					//chkboxType: { "Y": "ps", "N": "ps" },
					chkStyle : "checkbox",//复选框类型
					enable : true
				//每个节点上是否显示 CheckBox 
				},
				data : {
					simpleData : {//简单数据模式
						enable : true,
						idKey : "id",
						pIdKey : "pId",
						rootPId : ""
					}
				},
				callback : {
                    onClick : function(e,treeId, treeNode) {
						zTree = $.fn.zTree.getZTreeObj("ta_tree");
//						taZTreeOnCheck(null,treeId, treeNode);
						zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
					},
					onCheck : taZTreeOnCheck
				}
			};

			//对复选框进行响应
			function taZTreeOnCheck(event, treeId, treeNode) {
					if (treeNode.checked) {
						taIds.push(treeNode.id);
					} else {
						taIds.removeByValue(treeNode.id);
					}
			};

			var url = "/ztree/sTa.do";
					//Ajax调用处理
					$.ajax({
						type : "POST",
						url : url,
						data : {
							pfsnIds : pfsnIds,
			                taIds : taIds
						},
						dataType : 'json',
						success : function(data) {
							if(data.code == _GLOBAL_SUCCESS){
								data = data.body;
									var zTree = $.fn.zTree.init($("#ta_tree"), setting, data);
			  				 		$('#taztree').siblings('span.btn').text('选取'); 
							}
						}
					});
			/**
			 ztree模块结束
			 */
		}
		
		function resetPfsnFeeName(){
			//$("#pfsnLevel").val(null).trigger("change");
			$("#unvs").val(null).trigger("change");
			var grade = $("#grade").val();
			if(null != grade && '' != grade){
				$("#unvs").removeAttr("disabled");
			}else{
				$("#unvs").attr("disabled","disabled");
			}
			init_pfsn_ztree;
			inputofferName();
		}
		
		function inputofferName(){
			var unvsName = '';
			var grade = '';
			if('' != $("#unvs").find("option:selected").val()){
				unvsName = $("#unvs").find("option:selected").text();
			}
			if('' != $("#grade").find("option:selected").val()){
				grade = $("#grade").find("option:selected").text();
			}
			
			var offerName = grade + unvsName + '优惠';
			$("#offerName").val(offerName);
		}
		
		function selectedPfsn() {
            var zTreee = $.fn.zTree.getZTreeObj("user_tree");
            var nodes = zTreee.getCheckedNodes(true), v = "";
            for (var i = 0; i < nodes.length; i++) {
                if(!nodes[i].isParent){
                    v += nodes[i].name + "，";
                }
            }
            v = v.substr(0, v.length - 1);
            $('#pfsnCheckDiv').html(v);
            $("#pfsnztree").fadeOut(200);
        }

        function hidePfsn() {
        	$("#pfsnztree").fadeOut(200);
        }
        
        function selectedTa() {
            var zTreee = $.fn.zTree.getZTreeObj("ta_tree");
            var nodes = zTreee.getCheckedNodes(true), v = "";
            for (var i = 0; i < nodes.length; i++) {
                if(!nodes[i].isParent){
                    v += nodes[i].name + "，";
                }
            }
            v = v.substr(0, v.length - 1);
            $('#taCheckDiv').html(v);
            $("#taztree").fadeOut(200);
        }

        //    考试地区隐藏事件
        function hideTa() {
        	$("#taztree").fadeOut(200);
        }		
		
        function scholarshipChange(){
        	var scholarship = $("#scholarship").val();
        	if(scholarship){
        		$.ajax({
					type : "POST",
					url : "/offer/getSg.do",
					data : {
						scholarship : scholarship
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							if(data.body && data.body != '1'){
								_init_select({selectId : 'inclusionStatus',ext1 : data.body}, dictJson.inclusionStatus,null);
								$("#inclusionStatus").attr("disabled", false);
							} else {
								resetInclusion();
							}
						}
					}
				});
        	} else {
        		resetInclusion();
        	}        	
        }
        
        function resetInclusion(){
        	_init_select("inclusionStatus", dictJson.inclusionStatus, '1');
    		$("#inclusionStatus").attr("disabled", true);
        }