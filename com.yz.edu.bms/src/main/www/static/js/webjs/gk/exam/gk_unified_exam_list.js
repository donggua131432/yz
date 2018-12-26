	var myDataTable;
	$(function() {
		$('select').select2({
			placeholder : "--请选择--",
			allowClear : true,
			width : "59%"
		});
		//初始状态
		_init_select("ifShow",[
			{"dictValue":"1","dictName":"是"},
			{"dictValue":"0","dictName":"否"}
		]);
		
		myDataTable = $('.table-sort').dataTable({
			"processing": true,
			"serverSide" : true,
			"dom" : 'rtilp',
			"ajax" : {
				url : '/studentGKUnifiedExam/findStdGKUnifiedExam.do',
				type : "post",
				data : {
					"title": function () {
                        return $("#title").val();
                    },
                    "ifShow": function () {
                        return $("#ifShow").val();
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
			columns : [
				{"mData" : "title"},
				{"mData" : "describe"},
				{"mData" : null},
				{"mData" : "testSubject"},
				{"mData" : "operationDesc"},
				{"mData" : "fileName"},
				{"mData" : null},
				{"mData" : "createUserName"},
				{"mData" : null},
				{"mData" : null}
			],
			"columnDefs" : [
					{"targets" : 2,"render" : function(data, type, row, meta) {
						 return row.startTime+'<br>'+row.endTime
					}},
					{"targets" : 6,"render" : function(data, type, row, meta) {
						if(row.ifShow && row.ifShow =='1'){
							return "<label class='label label-success radius'>已启用</label>";
						}else{
							return "<label class='label label-danger radius'>已禁用</label>";
						}
					}},
					{"targets" : 8,"render" : function(data, type, row, meta) {
						var dateTime = row.createTime;
	                    if(!dateTime){
	                        return '-'
	                    }
	                    var date=dateTime.substring(0,10)
	                    var time=dateTime.substring(11,19)
	                    return date+'<br>'+time
					}},
					{"targets" : 9,"render" : function(data, type, row, meta) {
						var dom = '';
						if (row.ifShow == '1') {
							dom += '<a onClick="setStop(\''+ row.id + '\')" href="javascript:;" title="禁用" style="text-decoration:none"><i class="iconfont icon-tingyong"></i></a>';
						} else if (row.ifShow == '0') {
							dom += '<a onClick="setStart(\''+ row.id + '\')" href="javascript:;" title="启用" style="text-decoration:none"><i class="iconfont icon-qiyong"></i></a>';
						}
						dom += ' &nbsp;';
						dom += '<a title="编辑" href="javascript:void(0)" onclick="setEdit(\''+ row.id + '\')" class="ml-5" style="text-decoration:none">';
						dom += '<i class="iconfont icon-edit"></i></a>';
						dom += ' &nbsp;';
						dom += '<a title="删除" href="javascript:;" onclick="setDel(this,\'' + row.id + '\')" class="ml-5" style="text-decoration: none">';
						dom += '<i class="iconfont icon-shanchu"></i></a>';
						return dom;
					}}
			]
		});
	});

	/*搜素*/
	function searchResult(){
		myDataTable.fnDraw(true);
	}
	//新增
    function toEdit(){
    	var url = '/studentGKUnifiedExam/edit.do?exType=ADD';
		layer_show('新增', url, 900, 600, function() {
		});
    }
    
	/*编辑*/
	function setEdit(id) {
		var url = '/studentGKUnifiedExam/edit.do?exType=UPDATE&id='+id;
		layer_show('编辑', url, 900, 600, function() {
		});
	}
	function setDel(obj, id) {
		layer.confirm('确认要删除吗？', function(index) {
			$.ajax({
				type : 'POST',
				url : '/studentGKUnifiedExam/delGkUnifiedExam.do',
				data : {
					id : id
				},
				dataType : 'json',
				success : function(data) {
					if (data.code == _GLOBAL_SUCCESS) {
						layer.msg('已删除!', {
							icon : 1,
							time : 1000
						});
					}
					myDataTable.fnDraw(false);
				}
			});
		});
	}

	function setStop(id) {
		layer.confirm('确认要停用吗？', function(index) {
			//此处请求后台程序，下方是成功后的前台处理……
			$.ajax({
				type : 'POST',
				url : '/studentGKUnifiedExam/startOrStopGkUnifiedExam.do',
				data : {
					id : id,
					ifShow: '0'
				},
				dataType : 'json',
				success : function(data) {
					if (data.code == _GLOBAL_SUCCESS) {
						myDataTable.fnDraw(false);
						layer.msg('已停用!', {
							icon : 5,
							time : 1000
						});
					}
				}
			});
		});
	}

	function setStart(id) {
		layer.confirm('确认要启用吗？', function(index) {
			//此处请求后台程序，下方是成功后的前台处理……
			$.ajax({
				type : 'POST',
				url : '/studentGKUnifiedExam/startOrStopGkUnifiedExam.do',
				data : {
					id : id,
					ifShow: '1'
				},
				dataType : 'json',
				success : function(data) {
					if (data.code == _GLOBAL_SUCCESS) {
						myDataTable.fnDraw(false);
						layer.msg('已启用!', {
							icon : 6,
							time : 1000
						});
					}
				}
			});
		});

	}
