var myDataTable;
		$(function() {
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
			
			myDataTable = $('.table-sort').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/studentModify/findStudentAuditModify.do",
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
							$(row).children('td').eq(6).attr(
									'style', 'text-align: left;');
						},
						"language" :_my_datatables_language,
						columns : [ {
							"mData" : null
						}, {
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
										if(row.checkStatus == "1"){
										     return '<input type="checkbox" value="'+ row.modifyId+ '" name="modifyIds"/>';
      									}  else{
      										 return "";
      									}
									},
									"targets" : 0
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("grade",row.grade);
									},
									"targets" : 2
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
									"targets" : 3,
                                    "class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("stdStage",row.stdStage);
									},
									"targets" : 4
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("scholarship",row.scholarship);
									},
									"targets" : 5
								},
								{
									"render" : function(data, type, row, meta) {
										return row.createUser;
									},
									"targets" : 7
								},
								{
									"render" : function(data, type, row, meta) {
										var dateTime= new Date(row.createTime).format('yyyy-MM-dd hh:mm:ss');
                                        if(!dateTime){
                                            return '-'
                                        }
                                        var date=dateTime.substring(0,10)
                                        var time=dateTime.substring(11)
                                        return date+'<br>'+time
									},
									"targets" : 8,"class":"text-l"
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
									"targets" : 6
								},
								{
									"render" : function(data, type, row, meta) {
										var dom ="";
											dom ='<label class="label-secondary label radius">已提交</label>';
										return dom;
									},
									"targets" : 9
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';
    										dom = '<a title="进入审核" href="javascript:;" onclick="member_edit(\'' + row.modifyId + '\')" class="ml-5" style="text-decoration: none">';
    										dom += '<i class="iconfont icon-shenhe"></i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 10
								} ]
					});

		});

		/*用户-审核*/
		function member_edit(modifyId) {
			var url = '/studentModify/editToAudit.do' + '?modifyId=' + modifyId + '&exType=UPDATE';
			layer_show('信息修改审核', url, null, 510, function() {
				myDataTable.fnDraw(false);
			},true);
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
						if(data.code == _GLOBAL_SUCCESS){
							layer.msg('已删除!', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(true);
							$("input[name=all]").attr("checked", false);
						}
					}
				});
			});
		}
		
		function _search() {
			myDataTable.fnDraw(true);
		}
		
		
		function checkBatch(){
			var chk_value = [];
			$("input[name=modifyIds]:checked").each(function() {
				chk_value.push($(this).val());
			});

			layer.confirm('确认审核吗？', function(index) {
				$.ajax({
					type : 'POST',
					url : '/studentModify/checkModifyBatch.do',
					data : {
						modifyIds : chk_value
					},
					dataType : 'json',
					success : function(data) {
						if(data.code == _GLOBAL_SUCCESS){
							
							layer.msg('审核成功!', {
								icon : 1,
								time : 1000
							});
							myDataTable.fnDraw(true);
							$("input[name=all]").attr("checked", false);
						}
					}
				});
			});
		}