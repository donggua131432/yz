var myDataTable;
$(function() {
	
	$("#form-member-add").validate({
        rules : {
        	content : {
                required : true,
            }
        },
        messages : {
            errorCode : {
                remote : "参数名称已存在"
            }
        },
        onkeyup : false,
        focusCleanup : true,
        success : "valid",
        submitHandler : function(form) {
            var  urls = '/studying/insertRemark.do';
            $(form).ajaxSubmit({
                type : "post", //提交方式  
                dataType : "json", //数据类型  
                url : urls, //请求url 
                success : function(data) { //提交成功的回调函数
                    layer.msg('提交成功', {
                        icon : 1,
                        time : 1000
                    },function () {
                        //myDataTable.fnDraw(false);
                        layer_close();
                    });
                },
                error : function(data) {
                    layer.msg('提交失败', {
                        icon : 5,
                        time : 1000
                    },function () {
                        layer_close();
                    });
                }
            })
        }
    });
	
	
 	
	myDataTable = $('.table-sort').dataTable(
			{
				"serverSide" : true,
				"dom" : 'rtilp',
				"ajax" : {
					url : "/studying/showRemarkData.do",
					data:{
						"learnId" : function() {
							return $("#learnId").val();
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
					"mData" : null
				}, {
					"mData" : "empName"
				}],
				"columnDefs" : [
			            {"render" : function(data, type, row, meta) {
							return '<input type="checkbox" value="'+ row.lrId+';'+row.learnId + '" name="lrIds"/>';
						   },
						"targets" : 0
						},
						{"render" : function(data, type, row, meta) {
								return row.content;
						   },
						   "targets" : 1
						},
						{"render" : function(data, type, row, meta) {
								return new Date(row.createTime).format("yyyy-MM-dd hh:mm");
						   },
						   "targets" : 2
					     }
				  ]
			});
	});
/*用户-添加*/
function remark_add() {
	var  url = '/studying/toInsertRemark.do'+'?learnId=' + $("#learnId").val() ;
    layer_show('添加业务备注', url, null, 510, function() {
        myDataTable.fnDraw(true);
    });
}