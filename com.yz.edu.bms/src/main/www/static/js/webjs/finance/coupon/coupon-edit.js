function cTriggerChange(){
			var type = $("#cTriggerType").val();
			
			if('1' == type){
				$("#betweenScore").show();
				$("#scoreCouponDiv").show();
				unvsOnChange();
			}else{
				$("#betweenScore").hide();
				$("#scoreCouponDiv").hide();
				getFeeItem("");
			}
		}
		
		function checkItemList(itemCode){
			
			if($.inArray(itemCode, attr) >= 0){
				return true;
			}
			return false;
		};
			
		function checkItems(){
			$("#itemDiv input:checkbox").each(function(){
				$(this).attr("checked",checkItemList($(this).val()));
			});
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
						
						var grade = "";
						
						if($("#exType").val() == "UPDATE" && null != pfsnInfo[0]){
							grade = pfsnInfo[0].grade;
						}else{
							grade =  $("#grade").val();
						}
						var dom = '';
						for (var i = 0; i < items.length; i++) {
							var item = items[i];
							dom += '<tr>';
							dom += '<td class="text-c">';
							dom += '<input type="checkbox" name="itemCodes" value="'+item.itemCode+'" />';
							dom += '</td>';
							dom += '<td>';
							var itemName = item.itemName;
							if(grade){
								itemName = getItemName(item.itemName,grade);
							}
							dom += item.itemCode + ':' + itemName;
							dom += '</td>';
							dom += '</tr>';
						}
						$("#items").html(dom);
						checkItems();
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
	    	if ('0' == checked) {
	    		for(var i=0;i<nodes.length;i++){
	    			if('pfsn' == type){
		            	pfsnIds.push(nodes[i].id);
	    			}else if('ta' == type){
	    				taIds.push(nodes[i].id);
	    			}
	        	}
	        } else if("1" == checked) {
	        	for(var i=0;i<nodes.length;i++){
	        		if('pfsn' == type){
	        			pfsnIds.removeByValue(nodes[i].id);
	    			}else if('ta' == type){
	    				taIds.removeByValue(nodes[i].id);
	    			}
	        	}
	        }
	    	if('pfsn' == type){
	    		init_ta_ztree();
			}
	    }
		
		$(function() {
			
			
			$("#form-coupon").validate({
				rules : {
					grade : {
						required : true
					},
					pfsnLevel : {
						required : true,
					},
					unvs : {
						required : true,			
					},
					scholarship : {
						required : true,
					},
					amount : {
						required : true,
						min : 0,
						max : 1000000,
						isStdFee : true
					},
					itemCodes : {
						required : true
					},
					remark : {
						required : true,
						rangelength : [5,100]
					},
					lowestScore : {
						required : true,
						min : 0,
						isStdScore : true
					},
					highestScore : {
						required : true,
						min : function(){
							return $("#lowestScore").val();
						},
						max : 1000,
						isStdScore : true
					},
					publishStartTime : {
						required : true,
						datetime : true
					},
					publishExpireTime : {
						required : true,
						datetime : true
					},
					availableStartTime : {
						required : true,
						datetime : true
					},
					availableExpireTime : {
						required : true,
						datetime : true
					},
					isAllow : {
						required : true
					},
					couponName : {
						required : true,
						rangelength : [5,25],
						remote : { 
							type : "POST",
							url : "/coupon/validateCouponName.do", 
							data : {
								couponName : function() {
									return $("#couponName").val();
								},
							}
						}
					}
				},
				messages : {
					couponName : {
						remote : "优惠券名称已存在"
					}
				},
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					if("ADD" == $("#exType").val()){
						if("1" == $("#cTriggerType").val()){
							if(false == checkPfsnIds(pfsnIds) || false == chekcTaIds(taIds)){
								return;
							} 
						}
						
					}
					//alert(pfsnIds)
					//alert(taIds)
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
									var groupId = "coupon:insert";
									layer.confirm('新增成功！是否关闭页面？', {
										  btn: ['继续操作', '关闭页面'] //可以无限个按钮
										}, function(index, layero){
											var url = "/baseinfo/resetToken.do";
											_resetToken(groupId,url);
											layer.closeAll('dialog');
										}, function(index){
										    window.parent.myDataTable.fnDraw(true);
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
			
			if(null == status){
				status = '1';
			}
			
			_init_radio_box("statusRadio", "isAllow", dictJson.isAllow, status);
			if($("#exType").val() == "UPDATE"){
				
				var cTriggerType = '<span>'+ _findDict("cTriggerType",coupon.cTriggerType) + '</span>';
				cTriggerType += '<input type="hidden" value="'+coupon.cTriggerType+'" name="cTriggerType"/>';
				$("#triggerDiv").html(cTriggerType);
				
				if('2' == coupon.cTriggerType){
					$("#scoreCouponDiv").hide();
					$("#betweenScore").hide();
					getFeeItem('');
				} else if('1' == coupon.cTriggerType){
				
					var gradeDiv = '<span>'+ _findDict("grade", pfsnInfo[0].grade) +'</span>';
					$("#gradeDiv").html(gradeDiv);
					
					var pfsnLevelDiv = '<span>'+ _findDict("pfsnLevel", pfsnInfo[0].pfsnLevel) +'</span>';
					$("#pfsnLevelDiv").html(pfsnLevelDiv);
					
					var shipDiv = '<span>'+ _findDict("scholarship", coupon.scholarship) +'</span>';
					$("#shipDiv").html(shipDiv);
					
					var unvsDiv = '<span>[' + _findDict("recruitType", pfsnInfo[0].recruitType) + ']' + pfsnInfo[0].unvsName + '(' + pfsnInfo[0].unvsCode + ')</span>';
					$("#unvsDiv").html(unvsDiv);
					
					var pfsnDiv = '';
					
					for (var i = 0; i < pfsnInfo.length; i++) {
						var pfsn = pfsnInfo[i];
						pfsnDiv += '<span>('+ pfsn.pfsnCode + ')' + pfsn.pfsnName + '</span></br>';
					}
					
					$("#pfsnDiv").html(pfsnDiv);
					
					var taDiv = '';
					for (var i = 0; i < taInfo.length; i++) {
						var ta = taInfo[i];
						taDiv += '<span>('+ ta.taCode + ')' + ta.taName + '</span></br>';
					}
					$("#taDiv").html(taDiv);
					
					
					
					getFeeItem(pfsnInfo[0].recruitType);
				}
				
			}else{
				_simple_ajax_select({
					selectId : "unvs",
					searchUrl : "/baseinfo/sUnvs.do",
					sData : {
						ext2 : function(){
							return $("#pfsnLevel").val();
						}
					},
					showText : function(item) {
						var text = '[' + _findDict('recruitType', item.recruitType) + ']';
						text += item.unvsName + '(' + item.unvsCode + ')';
						return text;
					},
					showId : function(item) {
						return item.unvsId;
					},
					placeholder : '--请选择院校--'
				});
				//初始化年级下拉框
				_init_select("grade", dictJson.grade, null);
				
				_init_select("pfsnLevel", dictJson.pfsnLevel, null);
				
				_init_select("scholarship", dictJson.scholarship, null);
				
				_init_select("cTriggerType", dictJson.cTriggerType, null);
				
			}
			$("#taztree").hide();
			$("#pfsnztree").hide();
			
			var editUrl = '';
			if("UPDATE" == $("#exType").val()){
				editUrl = "/coupon/edit.do";
			}else{
				editUrl = "/coupon/add.do";
			}
			
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
						ext1 : $("#pfsnLevel").val(),
						ext2 : $("#grade").val()
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
		
		function pfsnLevelOnChange(){
			$("#unvs").val(null).trigger("change");
			var pfsnLevel = $("#pfsnLevel").val();
			if(null != pfsnLevel && '' != pfsnLevel){
				$("#unvs").removeAttr("disabled");
			}else{
				$("#unvs").attr("disabled","disabled");
			}
			init_pfsn_ztree();
		}
		
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
							pfsnIds : pfsnIds
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
			$("#pfsnLevel").val(null).trigger("change");
			$("#unvs").val(null).trigger("change");
			var grade = $("#grade").val();
			if(null != grade && '' != grade){
				$("#pfsnLevel").removeAttr("disabled");
			}else{
				$("#pfsnLevel").attr("disabled","disabled");
			}
			init_pfsn_ztree();
			/* inputFeeName(); */
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