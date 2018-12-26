function initScoreDom(){
			var dom = '';
			if(!addScore){
				addScore = 0;
			}
			
			var paparScore = 0;
			var totalScore = 0;
			dom += '<tbody>';
			dom += '<tr>';
			dom += '<td>';
			for (var i = 0; i < scores.length; i++) {
				var score = scores[i];
				
				dom += score.courseName + ':' + score.score + '</br>';
				paparScore += parseFloat(score.score);
			}
			if(null != addScore && addScore != ''){
				totalScore = parseFloat(addScore) + paparScore;
			}else{
				totalScore = paparScore;
			}
			dom += '</td>';
			dom += '<td class="text-c">' + paparScore + '</td>';
			dom += '<td class="text-c">' + addScore + '</td>';
			dom += '<td class="text-c">' + totalScore + '</td>';
			dom += '</tr>';
			dom += '</tbody>';
			$("#score").append(dom);
		}
		$(function() {
			$("#recruitType").text(_findDict("recruitType",enrollInfo.recruitType));			
			$("#pfsnLevel").text(_findDict("pfsnLevel",enrollInfo.pfsnLevel));		
			var firstAdmit = enrollInfo.firstAdmit;
			var firstAdmitInfo = firstAdmit.unvsName + ':(' + firstAdmit.pfsnCode + ')' + firstAdmit.pfsnName + '[' + _findDict("pfsnLevel",firstAdmit.pfsnLevel) + ']';
			$("#firstAdmit").text(firstAdmitInfo);
			var secondAdmit = enrollInfo.secondAdmit;
			if(null != secondAdmit.unvsName){
				var secondAdmitInfo = secondAdmit.unvsName + ':(' + secondAdmit.pfsnCode + ')' + secondAdmit.pfsnName + '[' + _findDict("pfsnLevel",secondAdmit.pfsnLevel) + ']';
				$("#secondAdmit").text(secondAdmitInfo);
			}
			initScoreDom();
			if(exType == 'UPDATE'){
				$("#showCampusId").show();
				$("#subDiv").html('<input class="btn btn-primary radius" type="submit" value="分配校区">');
			}else{
				$("subDiv").html('<input class="btn btn-primary radius" type="submit" value="录取">');
			}
			
			url = "/stdEnroll/getHomeCampusInfo.do"
				$.ajax({
			         type: "POST",
			         dataType : "json", //数据类型  
			         url: url+'?isStop=0',
			         success: function(data){
			      	     var campusJson = data.body;
			      	     if(data.code=='00'){
			      	    	_init_select("campusId",campusJson,$("#campusIds").val()); 	 
			      	     }
			         }
			    });
			//_init_select("scholarship",dictJson.scholarship,enrollInfo.scholarship);
			
			
			 /* _simple_ajax_select({
				selectId : "unvsId",
				searchUrl : '[[@{/baseinfo/sUnvs.do}]]',
				sData : {
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
			
			_simple_ajax_select({
				selectId : "pfsnId",
				searchUrl : '[[@{/baseinfo/sPfsn.do}]]',
				sData : {
					sId :  function(){
						return $("#unvsId").val() ? $("#unvsId").val() : '';	
					},
					ext2 : function(){
						return $("#grade").val() ? $("#grade").val() : '';
					}
				},
				showText : function(item) {
					var text = '(' + item.pfsnCode + ')' + item.pfsnName;
					text += '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']';
					return text;
				},
				showId : function(item) {
					return item.pfsnId;
				},
				placeholder : '--请选择专业--'
			}); 
			
			_simple_ajax_select({
				selectId : "taId",
				searchUrl : '[[@{/baseinfo/sTa.do}]]',
				sData : {
					sId :  function(){
						return $("#pfsnId").val() ? $("#pfsnId").val() : '';	
					}
				},
				showText : function(item) {
					var text = '(' + item.taCode + ')' + item.taName;
					return text;
				},
				showId : function(item) {
					return item.taId;
				},
				placeholder : '--请选择专业--'
			}); */
			
			var url = '';
			var msg = '';
			if("UPDATE" == exType){
				url = "/stdEnroll/update.do";
				msg = '修改成功！';
			}else{
				url = "/stdEnroll/enroll.do";
				msg = '录取成功！';
			}
			
			$("#form-enroll").validate({
				rules : {
					unvsId : {
						required : true
					},
					pfsnId : {
						required : true
					},
					taId : {
						required : true
					}
				},
				messages : {
					
				},
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						data : {
						},
						url : url, 
						success : function(data) { //提交成功的回调函数  
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg(msg, {icon : 1, time : 1000},function(){
									
									layer_close();
								});
							}
						}
					});
				}
				
			});
			
		});
		
		function unvsChange(){
			$("#pfsnId").val(null).trigger("change");
			$("#taId").val(null).trigger("change");
			var unvs = $("#unvsId").val();
			if(null != unvs && '' != unvs){
				$("#pfsnId").removeAttr("disabled");
			}else{
				$("#pfsnId").attr("disabled","disabled");
			}
		}
		
		function pfsnChange(){
			$("#taId").val(null).trigger("change");
			var pfsnId = $("#pfsnId").val();
			if(null != pfsnId && '' != pfsnId){
				$("#taId").removeAttr("disabled");
			}else{
				$("#taId").attr("disabled","disabled");
			}
		}