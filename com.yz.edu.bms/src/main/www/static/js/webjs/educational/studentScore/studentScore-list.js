var myDataTable;
		$(function() {
			
		    //初始化年级下拉框
			 _init_select("grade",dictJson.grade);
			//初始有无成绩
		   _init_select("isScore",[
		  	 {
		  	 			"dictValue":"1","dictName":"是"
		  	 		},
		  	 {
		  	 			"dictValue":"2","dictName":"否"
		  	 		}
		  	 	]);
		 //初始是否加分
		   _init_select("isAddScore",[
		  	 {
		  	 			"dictValue":"1","dictName":"是"
		  	 		},
		  	 {
		  	 			"dictValue":"2","dictName":"否"
		  	 		}
		  	 	]);
			
			myDataTable = $('.table-sort').dataTable(
					{
                        "processing": true,
						"serverSide" : true,
						"dom" : 'rtilp',
						"ajax" : {
							url : "/studentScore/findAllStudentScore.do",
							type : "post",
							data : {
								"stdName" : function() {
									return $("#stdName").val();
								},"telephone" : function() {
									return $("#telephone").val();
								},"idCard" : function() {
									return $("#idCard").val();
								}, "grade" : function() {
									return $("#grade").val();
								}
							}
						},
						"pageLength" : 10,
						"pagingType" : "full_numbers",
						"ordering" : false,
						"searching" : false,
						"createdRow" : function(row, data, dataIndex) {
							$(row).addClass('text-c');
						},
						"language" : _my_datatables_language,
						columns : [ {
							"mData" : null
						}, {
							"mData" : "stdName"
						}, {
							"mData" : null
						}, {
							"mData" : null
						},  {
							"mData" : "empName"
						},{
							"mData" : null
						}, {
							"mData" : null
						}, {
							"mData" : "ticketNo"
						}, {
							"mData" : null
						} ],
						"columnDefs" : [
								{
									"render" : function(data, type, row, meta) {
										return '<input type="checkbox" value="'+ row.learnId + '" name="stdIds"/>';
									},
									"targets" : 0
								},
								{
									"render" : function(data, type, row, meta) {
										return _findDict("stdStage",row.stdStage);
									},
									"targets" : 6
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = ""
										if(row.unvsName){
											dom = row.unvsName+"</br>"+"[";
											for (var i = 0; i < dictJson.pfsnLevel.length; i++) {
                                                if(dictJson.pfsnLevel[i].dictValue == row.pfsnLevel){
                                                    if(dictJson.pfsnLevel[i].dictName.indexOf("高中")!=-1){
                                                        dom += "专科]";
                                                    }else {
                                                        dom += "本科]";
                                                    }
                                                }
								          	 }
								          	 dom+=row.pfsnName
										}
										return dom;
									},
									"targets" : 5,
									"class":"text-l"
								},
								{
									"render" : function(data, type, row, meta) {
										for (var i = 0; i < dictJson.recruitType.length; i++) {
								          		 if(dictJson.recruitType[i].dictValue == row.recruitType){
								          			 return dictJson.recruitType[i].dictName;
								          		 }
								          	 }
											return "";
									},
									"targets" : 2
								},
								{
									"render" : function(data, type, row, meta) {
											return _findDict("grade",row.grade);;
									},
									"targets" : 3
								},
								{
									"render" : function(data, type, row, meta) {
										var dom = '';

										dom = '<a title="编辑" href="javascript:;" onclick="member_edit(\'' + row.learnId + '\',\'' + row.idCard + '\',\'' + row.stdName + '\')" class="ml-5" style="text-decoration: none">';
										dom += '<i class="iconfont icon-edit f-18"></i></a>';
										return dom;
									},
									//指定是第三列
									"targets" : 8
								} ]
					});

		});

		
		/*用户-编辑*/
		function member_edit(learnId,idCard,stdName) {
			var url = '/studentScore/edit.do' + '?learnId=' + learnId+'&idCard='+idCard+'&stdName='+stdName;
			layer_show('修改入学成绩', url, null, 510, function() {
				myDataTable.fnDraw(false);
			});
		}
		

		function _search(){
			myDataTable.fnDraw(true);
		}