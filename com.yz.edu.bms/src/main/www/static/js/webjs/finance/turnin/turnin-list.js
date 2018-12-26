var myDataTable;
        $(function () {
        	
      	 _init_select("scholarship", dictJson.scholarship, null);
      	 _init_select("stdStage", dictJson.stdStage, null);
         _init_select("grade", dictJson.grade, null);
         _init_select("pfsnLevel", dictJson.pfsnLevel, null);
        	
         _init_select("grade", dictJson.grade, null);
         _init_select("inclusionStatus", dictJson.inclusionStatus, null);
         _init_select("schoolYear", dictJson.schoolYear, null);
         
      	 _simple_ajax_select({
              selectId: "unvsId",
              searchUrl: '/baseinfo/sUnvs.do',
              sData: {
              },
              showText: function (item) {
                  var text = '[' + _findDict('recruitType', item.recruitType) + ']';
                  text += item.unvsName + '(' + item.unvsCode + ')';
                  return text;
              },
              showId: function (item) {
                  return item.unvsId;
              },
              placeholder: '--请选择院校--'
          });
         $("#unvsId").change(function () {
 	            $("#pfsnId").removeAttr("disabled");
 	            init_pfsn_select();
     	 });
      
     	 $("#pfsnLevel").change(function () {
 	            $("#pfsnId").removeAttr("disabled");
 	            init_pfsn_select();
     	 });
     	 $("#grade").change(function () {
 	            $("#pfsnId").removeAttr("disabled");
 	            init_pfsn_select();
 	      });
 		 $("#pfsnId").append(new Option("", "", false, true));
 		 $("#pfsnId").select2({
 	            placeholder: "--请先选择专业--"
 	     });

            $("#status").select2({
                placeholder: "--请选择--",
                allowClear: true
            });
            $("#hasBillNo").select2({
                placeholder: "--请选择--",
                allowClear: true
            });

            myDataTable = $('.table-sort')
                .dataTable(
                    {
                        "processing": true,
                        "serverSide": true,
                        "dom": 'rtilp',
                        "ajax": {
                            url: "/turnIn/list.do",
                            type: "post",
                            data: function (data) {
                                return searchData(data);
                            }
                        },
                        "pageLength": 10,
                        "pagingType": "full_numbers",
                        "ordering": false,
                        "searching": false,
                        "createdRow": function (row, data,
                                                dataIndex) {
                            $(row).addClass('text-c');
                        },
                        "language": _my_datatables_language,
                        columns: [{
                            "mData": "schoolRoll"
                        }, {
                            "mData": "stdName"
                        }, {
                            "mData": null
                        }, {
                            "mData": "idCard"
                        }, {
                            "mData": "grade"
                        }, {
                            "mData": "unvsName"
                        }, {
                            "mData": null
                        }, {
                            "mData": "pfsnName"
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": null
                        }, {
                            "mData": "amount"
                        }, {
                            "mData": "turnDate"
                        }, {
                            "mData": "returnAmount"
                        }, {
                            "mData": "backDate"
                        }, {
                            "mData": "billNo"
                        }, {
                            "mData": "empName"
                        }, {
                            "mData": null
                        }],
                        "columnDefs": [
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict('sex',row.sex);
                                },
                                "targets": 2
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict(
                                        "pfsnLevel",
                                        row.pfsnLevel);
                                },
                                "targets": 6
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict(
                                        "stdStage",
                                        row.stdStage);
                                },
                                "targets": 8
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict(
                                        "scholarship",
                                        row.scholarship);
                                },
                                "targets": 9
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict(
                                        "inclusionStatus",
                                        row.inclusionStatus);
                                },
                                "targets": 10
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    return _findDict(
                                        "schoolYear",
                                        row.schoolYear);
                                },
                                "targets": 11
                            },
                            {
                                "render": function (data, type,
                                                    row, meta) {
                                    var dom = '';
                                    dom += '<a title="编辑" href="javascript:void(0)" onclick="charges_edit(\''
                                        + row.learnId
                                        + '\','+row.schoolYear+')" class="ml-5" style="text-decoration:none">';
                                    dom += '<i class="iconfont icon-edit"></i></a>';
                                    return dom;
                                },
                                "targets": 18
                            }]
                    });
        });
        
        function init_pfsn_select() {
    		_simple_ajax_select({
    			selectId : "pfsnId",
    			searchUrl : '/baseinfo/sPfsn.do',
    			sData : {
    				sId : function() {
    					return $("#unvsId").val() ? $("#unvsId").val() : '';
    				},
    				ext1 : function() {
    					return $("#pfsnLevel").val() ? $("#pfsnLevel")
    							.val() : '';
    				},
    				ext2 : function() {
    					return $("#grade").val() ? $("#grade").val() : '';
    				}
    			},
    			showText : function(item) {
    				var text = '(' + item.pfsnCode + ')' + item.pfsnName;
    				text += '[' + _findDict('pfsnLevel', item.pfsnLevel)
    						+ ']';
    				return text;
    			},
    			showId : function(item) {
    				return item.pfsnId;
    			},
    			placeholder : '--请选择专业--'
    		});
    		$("#pfsnId").append(new Option("", "", false, true));
    	}
        
        function importExcel() {
			var url = '/turnIn/toImport.do';
			layer_show('上缴导入', url, null, 510, function() {
				myDataTable.fnDraw(true);
			});
		}
        
        function exportExcel(){
        	
        	var unvsId = $("#unvsId").val();
        	var grade = $("#grade").val();
        	var schoolYear = $("#schoolYear").val();
        	
        	if(null == unvsId || unvsId == ''){
        		layer.msg('院校不能为空！', {icon : 2, time : 1000},function(){
				});
        		return false;
        	}
        	
        	if(null == grade || grade == ''){
        		layer.msg('年级不能为空！', {icon : 2, time : 1000},function(){
				});
        		return false;
        	}
        	
        	if(null == schoolYear || schoolYear == ''){
        		layer.msg('学年不能为空！', {icon : 2, time : 1000},function(){
				});
        		return false;
        	}
			$("#turnForm").submit();
        }


        function charges_edit(learnId,schoolYear) {
            var url = '/turnIn/toEdit.do' + '?learnId='
                + learnId + '&schoolYear=' + schoolYear;
            layer_show('上缴编辑', url, null, 510, function () {
                myDataTable.fnDraw(false);
            });
        }



        function searchItem() {
        	var unvsId = $("#unvsId").val();
        	var grade = $("#grade").val();
        	var schoolYear = $("#schoolYear").val();
        	
        	if(null == unvsId || unvsId == ''){
        		layer.msg('院校不能为空！', {icon : 2, time : 1000},function(){
				});
        		return false;
        	}
        	
        	if(null == grade || grade == ''){
        		layer.msg('年级不能为空！', {icon : 2, time : 1000},function(){
				});
        		return false;
        	}
        	
        	if(null == schoolYear || schoolYear == ''){
        		layer.msg('学年不能为空！', {icon : 2, time : 1000},function(){
				});
        		return false;
        	}
            myDataTable.fnDraw(true);
        }
        
        function searchData(data) {
			return {
				schoolRoll : $("#schoolRoll").val() ? $(
						"#schoolRoll").val() : '',
				stdName : $("#stdName").val() ? $("#stdName").val()
						: '',
				idCard : $("#idCard").val() ? $("#idCard").val()
						: '',
				grade : $("#grade").val() ? $("#grade").val() : '',
				unvsId : $("#unvsId").val() ? $("#unvsId").val()
						: '',
				pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel")
						.val() : '',
				pfsnId : $("#pfsnId").val() ? $("#pfsnId").val()
						: '',
				scholarship : $("#scholarship").val() ? $(
						"#scholarship").val() : '',
				inclusionStatus : $("#inclusionStatus").val() ? $(
						"#inclusionStatus").val() : '',
				schoolYear : $("#schoolYear").val() ? $(
						"#schoolYear").val() : '',
				turnDate : $("#turnDate").val() ? $("#turnDate")
						.val() : '',
				backDate : $("#backDate").val() ? $("#backDate")
						.val() : '',
				returnDate : $("#returnDate").val() ? $(
						"#returnDate").val() : '',
				hasBillNo : $("#hasBillNo").val() ? $("#hasBillNo")
						.val()
						: '',
				stdStage : $("#stdStage").val() ? $("#stdStage")
						.val()
						: '',
				start : data.start,
				length : data.length
			};
		}