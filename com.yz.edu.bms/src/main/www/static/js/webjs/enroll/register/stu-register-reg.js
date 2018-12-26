function initPayInfoDom(row){
				
				var dom = '';
				dom += '<table class="table table-border table-bordered table-hover table-bg table-sort">';
				dom += '<thead>';
				dom += '<tr>';
				dom += '<th width="80">科目</th>';
				dom += '<th width="40">应缴</th>';
				dom += '<th width="40">缴费状态</th>';
				dom += '</tr>';
				dom += '</thead><tbody>';

				var amount = 0.00;
				for (var i = 0; i < row.payInfos.length; i++) {
					var payInfo = row.payInfos[i];
					dom += '<tr>';
					
					var itemName = getItemName(payInfo.itemName,row.grade);
					
					dom += '<td>'
							+ payInfo.itemCode
							+ ':'
							+ itemName
							+ '</td>';
					dom += '<td>'
							+ payInfo.payable
							+ '</td>';
					var status = payInfo.subOrderStatus;
					dom += '<td>';
					if(status != '1'){
						dom +=  _findDict("orderStatus", status);
					}else{
						dom += '<span>' + _findDict("orderStatus", status) + '</span>';
					}
					
					dom += '</td>';
					dom += '</tr>';
					amount = amount
							+ parseFloat(payInfo.payable);
				}
				amount = amount.toFixed(2);
				dom += '<tr >';
				dom += '<td>合计：</td>';
				dom += '<td>' + amount
						+ '</td>';
				dom += '<td></td>';
				dom += '</tr></tbody>';
				dom += '</table>';
				$("#payInfoDiv").html(dom);
			}
		
			$(function() {
				
				var t = _findDict("recruitType",type);
				var lev = _findDict("pfsnLevel",level);
				var dom = '[' + t + ']' + unvs + '：(' + pCode + ')' + pfsnName + '[' + lev + ']';
				initPayInfoDom(row);
				
				var scholarship =row.scholarship;
				$("#scholarship").html(_findDict("scholarship",scholarship));
				$("#application").html(dom);
				$("#recruitType").html(_findDict("recruitType",recruitType));
				$("#pfsnLevel").html(_findDict("pfsnLevel",pfsnLevel));
				var totalPoint = 0;
				for (var i = 0; i < scores.length; i++) {
					totalPoint += parseFloat(scores[i].score);
				}
				$("#totalPoint").html(totalPoint);
				var  x = $("#points").text();
				var total = totalPoint + parseFloat(x);
				$("#total").html(total);
			});
			
			var regUrl = "/stuRegister/register.do";
			$("#form-regist").validate({
				onkeyup : false,
				focusCleanup : true,
				success : "valid",
				submitHandler : function(form) {
					$(form).ajaxSubmit({
						type : "post", //提交方式  
						dataType : "json", //数据类型  
						url : regUrl, //请求url  
						success : function(data) { //提交成功的回调函数  
							if(data.code == _GLOBAL_SUCCESS){
								layer.msg('注册成功！', {icon : 1, time : 1000},function(){
									layer_close();
								});
							}
						}
					})
				}
			});