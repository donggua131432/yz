 var myDataTable;
	$(function(){
		var stdInfo = stdName +' - '+ grade + '级 - [' + _findDict("recruitType",recruitType) + ']'
			+ unvsName + ':(' + pfsnCode + ')' + pfsnName + '[' + _findDict("pfsnLevel",pfsnLevel) + ']';
		$("#stdInfo").text(stdInfo);

		myDataTable = $('.table-sort').dataTable({
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : detailUrl,
				type : "post",
				dataType : 'json',
				data : {
					learnId : function(){
						return $("#learnId").val();
					}
				}
			},
			"pageLength" : 10,
			"pagingType" : "full_numbers",
			"ordering" : false,
			"searching" : false,
			"createdRow" : function(row, data,dataIndex) {
				$(row).addClass('text-c');
			},
			"language" : _my_datatables_language,
			columns : [
				{"mData" : null},
				{"mData" : "amount"},
				{"mData" : null},
				{"mData" : "payable"},
				{"mData" : "demurrageAfter"},
				{"mData" : null},
				{"mData" : "payTime"},
				{"mData" : "serialNo"},
				{"mData" : "orderNo"},
				{"mData" : "outSerialNo"},
				{"mData" : "empName"},
				{"mData" : null},
				{"mData" : null},
				{"mData" : null},
				{"mData" : "checkUser"},
				{"mData" : "remark"}
			], //[成教]汕头大学:(0003)会计学[专升本]
			"columnDefs" : [
				{"targets" : 0,"render" : function(data, type, row, meta) {
					var dom = '';
					var payInfos = row.payInfos;
					if(!payInfos[0]) return '/';
					for (var i = 0; i < payInfos.length; i++) {
						var payInfo = payInfos[i];
						var itemName = getItemName(payInfo.itemName,grade);
						dom += payInfo.itemCode + ':';
						dom += itemName;
						if(i != (payInfos.length - 1)){
							dom += '</br>';
						}
					}
					return dom;
				}},
				{"targets" : 2,"render" : function(data, type,row, meta) {
						var dom = '';
						var subSerials = row.subSerials;
						if(null != subSerials && subSerials.length > 0){
							for (var i = 0; i < subSerials.length; i++) {
								var subSerial = subSerials[i];
								if(subSerial.paymentType == '6'){
									dom += '滞留账户抵扣：' + subSerial.amount + '<br>';
								}else if(subSerial.paymentType == '7'){
									dom += '智米抵扣：' + subSerial.amount + '<br>';
								}else if(subSerial.paymentType == '8'){
									dom += '优惠券抵扣：' + subSerial.amount + '<br>';
								}else if(subSerial.paymentType == '9'){
									dom += '现金账户抵扣：' + subSerial.amount + '<br>';
								}

							}
						}
						return dom;
				}},
				{"targets" : 5,"render" : function(data, type,row, meta) {
					var dom = '';
					var reptStatus = row.reptStatus;
					if(null == reptStatus){	// 现场打印
						var subSerials = row.subSerials;
					    var canPrint = false;
						if(null != subSerials && subSerials.length > 0){
							for (var i = 0; i < subSerials.length; i++) {
								var subSerial = subSerials[i];
								var payType = subSerial.paymentType;
								//'1','10','11','2','4'
								if(payType == '1' || payType == '10' || payType == '11' || payType == '2' || payType == '4'){
									canPrint = true;
								}
							}
						}
						
						if(canPrint){
							dom += '<a class="tableBtn normal" onclick="printRept(\'' + row.serialMark + '\')">打印收据</a>';
						} else {
							dom += '未打印';
						}
						
					}else{
						dom += _findDict("reptStatus",reptStatus);
					}
					return dom;
				}},
				{"targets" : 11,"render" : function(data, type,row, meta) {
					return _findDict("paymentType",row.paymentType);
				}},
				{"targets" : 12,"render" : function(data, type,row, meta) {
					return "";
				}},
				{"targets" :13,"render" : function(data, type,row, meta) {
					var status = row.serialStatus;
					if('3' == status ){
						return '否';
					}else{
						return '是';
					}
				}}
			]
		});
	});
	
	// 现场打印
	function printRept(serialMark){
		$.ajax({
            type: 'POST',
            url: "/rept/printSiteRept.do",
            data: {
            	serialMark: serialMark
            },
            dataType: 'json',
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                	toPrint(data.body);
                }
            }
        });
	}
	
	function toPrint(reptId) {
        var url = "/rept/reptPrint.do" + '?reptId=' + reptId;
        window.location.href = url;
        //window.open(url);
    }