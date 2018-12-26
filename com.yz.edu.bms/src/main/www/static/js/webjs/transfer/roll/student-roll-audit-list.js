		var excuteTable;
		var acceptTable;
		
		$(function() {
			
			
		//标签块
		$.Huitab("#tab_demo .tabBar span","#tab_demo .tabCon","current","click","0");
			
		  //申请进度saStatus
		  _init_select("checkStatus",dictJson.saStatus);
		 //学员阶段
		   _init_select("stdStage",[
		  	 {
		  	 			"dictValue":"2","dictName":"考前辅导"
		  	 		},
		  	 {
		  	 			"dictValue":"3","dictName":"考前确认"
		  	 		}
		  	 	]);
		 
		   $('select').select2({
               placeholder : "--请选择--",
               allowClear : true,
               width : "59%"
           });
			
		   acceptTable = $('#acceptTable').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/studentRoll/findStudentAccpetRollModify.do",
							type : "post",
							data : {
								"stdName" : function() {
									return $("#stdName").val();
								},"phone" : function() {
									return $("#phone").val();
								},"idCard" : function() {
									return $("#idCard").val();
								},"unvsName" : function() {
									return $("#unvsName").val();
								},"pfsnName" : function() {
									return $("#pfsnName").val();
								},"stdStage" : function() {
									return $("#stdStage").val();
								}
							}
						},  
						"pageLength" : 10,
						"pagingType" : "full_numbers",
						"ordering" : false,
						"searching" : false,
						"createdRow" : function(row, data, dataIndex) {
							$(row).addClass('text-c');
							$(row).children('td').eq(5).attr(
									'style', 'text-align: left;');
						},
						"language" : _my_datatables_language,
						columns : [ {
							"mData" : "stdName"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						} ],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										return _findDict("grade",row.grade);
									},
									"targets" : 1
								},
								{
									"render" : function(data, type, row, meta) {
                                        var dom ='';
                                        _findDict("recruitType",row.recruitType).indexOf("成人")!=-1?dom+="[成教]":dom+="[国开]";

                                        dom+=row.unvsName+"<br>";
                                        _findDict("pfsnLevel",row.pfsnLevel).indexOf("高中")!=-1?dom+="[专科]":dom+="[本科]";

                                        dom+=row.pfsnName+"("+row.pfsnCode+")";
                                        return dom;
									},
									"targets" : 2,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("stdStage",row.stdStage);
									},
									"targets" : 3
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("scholarship",row.bScholarship);
									},
									"targets" : 4
								},
								{
									"render" : function(data, type, row, meta) {
										return row.createUser;
									},
									"targets" : 6
								},
								{
									"render" : function(data, type, row, meta) {
										var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                                        if(!dateTime){
                                            return '-'
                                        }
                                        var date=dateTime.substring(0,10)
                                        var time=dateTime.substring(11)
                                        return date+'<br>'+time
									},
									"targets" : 7,"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";
										if(null != row.newStdName){
											dom +="姓名:["+row.stdName+"]=>["+row.newStdName+"]</br>";
										}if(null != row.newIdCard){
											dom +="证件号:["+row.idCard+"]=>["+row.newIdCard+"]</br>";
										}if(null != row.newSex){
											dom +="性别:["+_findDict("sex",row.sex)+"]=>["+_findDict("sex",row.newSex)+"]</br>";
										}if(null != row.newNation){
											dom +="民族:["+_findDict("nation",row.nation)+"]=>["+_findDict("nation",row.newNation)+"]</br>";
										}if(null != row.newUnvsId){
											dom +="院校:["+row.unvsName+"]=>["+row.nunvsName+"]</br>";
										}if(null != row.newPfsnId){
											dom +="专业:["+row.pfsnName+"]=>["+row.npfsnName+"]</br>";
										}if(null != row.newTaId){
											dom +="考区:["+row.taName+"]=>["+row.ntaName+"]</br>";
										}if(null != row.newTaId){
											dom +="优惠类型:["+_findDict("scholarship",row.scholarship)+"]=>["+_findDict("scholarship",row.newScholarship)+"]</br>";
										}
										return dom;
									},
									"targets" : 5
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ='<label style="color: red;">已驳回</label>';
										if("1" == row.checkOrder && "1" == row.checkStatus){
											dom ='<label style="color: black;">待审核</label>';
										}if("2" == row.checkOrder && "1" == row.checkStatus){
											dom ='<label style="color: black;">待受理</label>';
										}if("3" == row.checkOrder && "1" == row.checkStatus){
											dom ='<label style="color: black;">待执行</label>';
										}if("3" == row.checkOrder && "2" == row.checkStatus){
											dom ='<label style="color: blue;">已执行</label>';
										}
										return dom;
									},
									"targets" : 8
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
    										dom = '<a title="进入审核" href="javascript:;" onclick="member_edit(\'' + row.modifyId + '\',\'ACCEPT\')" class="ml-5" style="text-decoration: none">';
    										dom += '<i class="iconfont icon-shenhe"></i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 9
								} ]
					});
			
			excuteTable = $('#excuteTable').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/studentRoll/findStudentAuditRollModify.do",
							type : "post",
							data : {
								"stdName" : function() {
									return $("#stdName2").val();
								},"phone" : function() {
									return $("#phone2").val();
								},"idCard" : function() {
									return $("#idCard2").val();
								},"unvsName" : function() {
									return $("#unvsName2").val();
								},"pfsnName" : function() {
									return $("#pfsnName2").val();
								},"stdStage" : function() {
									return $("#stdStage2").val();
								}
							}
						},  
						"pageLength" : 10,
						"pagingType" : "full_numbers",
						"ordering" : false,
						"searching" : false,
						"createdRow" : function(row, data, dataIndex) {
							$(row).addClass('text-c');
							$(row).children('td').eq(5).attr(
									'style', 'text-align: left;');
						},
						"language" : _my_datatables_language,
						columns : [ {
							"mData" : "stdName"
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : null
						} ],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										return _findDict("grade",row.grade);
									},
									"targets" : 1
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = "["+_findDict("recruitType",row.recruitType)+"]"+row.unvsName+":("+row.pfsnCode+")"+row.pfsnName+"["+_findDict("pfsnLevel",row.pfsnLevel)+"]";
										return dom;
									},
									"targets" : 2,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("stdStage",row.stdStage);
									},
									"targets" : 3
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("scholarship",row.bScholarship);
									},
									"targets" : 4
								},
								{
									"render" : function(data, type, row, meta) {
										return row.createUser;
									},
									"targets" : 6
								},
								{
									"render" : function(data, type, row, meta) {
										var dateTime = new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                                        if(!dateTime){
                                            return '-'
                                        }
                                        var date=dateTime.substring(0,10)
                                        var time=dateTime.substring(11)
                                        return date+'<br>'+time
									},
									"targets" : 7,"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";
										if(null != row.newStdName){
											dom +="姓名:["+row.stdName+"]=>["+row.newStdName+"]</br>";
										}if(null != row.newIdCard){
											dom +="证件号:["+row.idCard+"]=>["+row.newIdCard+"]</br>";
										}if(null != row.newSex){
											dom +="性别:["+_findDict("sex",row.sex)+"]=>["+_findDict("sex",row.newSex)+"]</br>";
										}if(null != row.newNation){
											dom +="民族:["+_findDict("nation",row.nation)+"]=>["+_findDict("nation",row.newNation)+"]</br>";
										}if(null != row.newUnvsId){
											dom +="院校:["+row.unvsName+"]=>["+row.nunvsName+"]</br>";
										}if(null != row.newPfsnId){
											dom +="专业:["+row.pfsnName+"]=>["+row.npfsnName+"]</br>";
										}if(null != row.newTaId){
											dom +="考区:["+row.taName+"]=>["+row.ntaName+"]</br>";
										}if(null != row.newTaId){
											dom +="优惠类型:["+_findDict("scholarship",row.scholarship)+"]=>["+_findDict("scholarship",row.newScholarship)+"]</br>";
										}
										return dom;
									},
									"targets" : 5
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ='<label style="color: red;">已驳回</label>';
										if("1" == row.checkOrder && "1" == row.checkStatus){
											dom ='<label style="color: black;">待审核</label>';
										}if("2" == row.checkOrder && "1" == row.checkStatus){
											dom ='<label style="color: black;">待受理</label>';
										}if("3" == row.checkOrder && "1" == row.checkStatus){
											dom ='<label style="color: black;">待执行</label>';
										}if("3" == row.checkOrder && "2" == row.checkStatus){
											dom ='<label style="color: blue;">已执行</label>';
										}
										return dom;
									},
									"targets" : 8
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
    										dom = '<a title="进入执行" href="javascript:;" onclick="member_edit(\'' + row.modifyId + '\',\'EXECUTE\')" class="ml-5" style="text-decoration: none">';
    										dom += '<i class="iconfont icon-shenhe"></i></a>';
										return dom;
									}, 
									//指定是第三列
									"targets" : 9
								} ]
					});

		});

		/*用户-审核*/
		function member_edit(modifyId,exType) {
			var url = '/studentRoll/editToAudit.do' + '?modifyId=' + modifyId + '&exType=' + exType;
			layer_show('信息修改审核', url, null, 510, function() {
				excuteTable.fnDraw(false);
				acceptTable.fnDraw(false);
			});
		}
		
		function delAll() {
			var chk_value = [];
			$("input[name=modifyIds]:checked").each(function() {
				chk_value.push($(this).val());
			});

			layer.confirm('确认要删除吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/studentModify/deleteStudentModify.do',
					data : {
						idArray : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if (data.code == _GLOBAL_SUCCESS) {
							layer.msg('已删除!', {
								icon : 1,
								time : 1000
							});
							excuteTable.fnDraw(false);
							acceptTable.fnDraw(false);
							$("input[name=all]").attr("checked", false);
						}
					}
				});
			});
		}
		
		function excuteSearch() {
			excuteTable.fnDraw(true);
		}
		function acceptSearch() {
			acceptTable.fnDraw(true);
		}