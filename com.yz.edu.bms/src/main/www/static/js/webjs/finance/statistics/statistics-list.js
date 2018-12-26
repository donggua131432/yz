var myDataTable;
	var index;
	function loading(){
		index = layer.load(1, {
			shade: [0.1,'#fff'] //0.1透明度的白色背景
		});
	}
    function submit(){
    	index = layer.load(1, {
			shade: [0.5,'#fff'] //0.1透明度的白色背景
		});
    	$("#statistics-form").submit();
    	layer.close(index);
    }
    $(function () {
    	 myDataTable = $('.table-sort')
         .dataTable(
             {
                 "processing": true,
                 "serverSide": true,
                 "dom": 'rtilp',
                 "ajax": {
                     url: "/feeStatistics/list.do",
                     type: "post",
                     data: {

                     }
                 },
//                 "pageLength": 10,
                 "pagingType": "full_numbers",
                 "ordering": false,
                 "searching": false,
                 "createdRow": function (row, data,
                                         dataIndex) {
                     $(row).addClass('text-c');
                 },
                 "language": _my_datatables_language,
                 columns: [{
                     "mData": "excelName"
                 }, {
                     "mData": null
                 }, {
                     "mData": null
                 }, {
                     "mData": "remark"
                 }, {
                     "mData": "createTime"
                 }, {
                     "mData": "createUser"
                 }, {
                     "mData": null
                 }],
                 "columnDefs": [
                     {
                         "render": function (data, type,
                                             row, meta) {
                             return _findDict("excelType",row.excelType);
                         },
                         "targets": 1
                     },{
                         "targets": 0,"class":"text-l"
                     },{
                         "render": function (data, type,
                                             row, meta) {
                        	 var url = varBrowser + row.excelUrl;
                             return '<a href="'+url+'" title="下载" style="text-decoration:none"><i class="Hui-iconfont Hui-iconfont-down"></i></a>';
                         },
                         "targets": 6
                     },
                     {
                         "render": function (data, type,
                                             row, meta) {
                        	 
                                 return _findDict("exportStatus",row.exportStatus);
                         },
                         "targets": 2
                     }]
             });
 });
    
	  //初始部门下拉框
	 	_simple_ajax_select({
				selectId : "dpId",
				searchUrl : '/dep/selectList.do',
				sData : {},
				showText : function(item) {
					return item.dpName;
				},					
				showId : function(item) {
					return item.dpId;
				},
				placeholder : '--请选择部门--'
		});	
	 	$("#dpId").append(new Option("", "", false, true));
    	//初始校区下拉框
     	_simple_ajax_select({
    			selectId : "campusId",
    			searchUrl : '/campus/selectList.do',
    			sData : {},
    			showText : function(item) {
    				return item.campusName;
    			},					
    			showId : function(item) {
    				return item.campusId;
    			},
    			placeholder : '--请选择校区--'
    	});	
     	
        _init_select("scholarship", dictJson.scholarship, null);
     	//_init_select("stdStage", dictJson.stdStage, null);
        _init_select("status", dictJson.status, null);
        _init_select("grade", dictJson.grade, null);
        _init_select("pfsnLevel", dictJson.pfsnLevel, null);
        
        _init_checkbox("itemCode","itemCodes",dictJson.itemCode,null);
        _init_checkbox("stdStage","stdStages",dictJson.stdStage,null);
        
        $.ajax({
			type : "POST",
			url : '/feeStatistics/sCampusJson.do',
			data : {
			},
			dataType : 'json',
			success : function(data) {
				if(data.code == _GLOBAL_SUCCESS){
					var campusJson = data.body;
					_init_checkbox("homeCampusId","homeCampusIds",campusJson,null);
					
					
				}
			}
		});
        
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
        _simple_ajax_select({
			selectId : "pfsnId",
			searchUrl : '/baseinfo/sPfsn.do',
			sData : {
				sId : function(){
					return $("#unvsId").val();
				},
				ext1 : function(){
					return $("#pfsnLevel").val();
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
        
        $("#statistics-form").validate({
    		rules : {
    			itemCodes : {
    				required : true
    			}
    		},
    		
    		onkeyup : false,
    		focusCleanup : true,
    		success : "valid",
    		invalidHandler: function(form, validator) {  //不通过回调 
    			if(!$('.showOther .icon-xiala').hasClass('xiala')){
    				_showOther()	
    			}
    			
    		    return false; 
            } ,
    		submitHandler : function(form) {
    			layer.prompt({
                    title: '填写excel文件名：',
                    formType: 2
    			}, function (text, index) {
    				$('#excelName').val(text);
	    			$(form).ajaxSubmit({
	    				type : "post", //提交方式  
	    				dataType : "json", //数据类型  
	    				url : '/feeStatistics/export.do', //请求url  
	    				success : function(data) { //提交成功的回调函数  
	    					if(data.code == _GLOBAL_SUCCESS){
	    						layer.msg('导出处理中', {icon : 1, time : 1000},function(){
	    							
	    						});
	    					}
	    				}
	    			})
    			});
    		}
    	});
    