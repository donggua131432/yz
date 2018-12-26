		var myDataTable;
        $(function() {
            $("#reasonStatus").select2({
                placeholder : "--请选择--",
                allowClear : true
            });

            myDataTable = $('.table-sort').dataTable({
                "serverSide" : true,
                "dom" : 'rtilp',
                "ajax" : {
                    url : "/zhimi_give_check/list.do",
                    type : "post",
                    data : function(pageData) {
                        pageData = $.extend({},{start:pageData.start, length:pageData.length}, $("#searchForm").serializeObject());
                        return pageData;
                    }
                },
                "pageLength" : 10,
                "pagingType" : "full_numbers",
                "ordering" : false,
                "searching" : false,
                "lengthMenu" : [ 10, 20 ],
                "createdRow" : function(row, data, dataIndex) {
                    $(row).addClass('text-c');
                },
                "language" : _my_datatables_language,
                columns : [
                    {"mData" : null},
                    {"mData" : "yzCode"},
                    {"mData" : "nickName"},
                    {"mData" : "realName"},
                    {"mData" : "mobile"},
                    {"mData" : null},
                    {"mData" : null},
                    {"mData" : "zhimiCount"},
                    {"mData" : null},
                    {"mData" : "submitUserName"},
                    {"mData" : "createTime"},
                    {"mData" : null},
                    {"mData" : null}
                ],
                "columnDefs" : [
                    {"targets": 0, "render": function (data, type, row, meta) {
                        return '<input type="checkbox"  reasonStatus="'+row.reasonStatus+'" value="' + row.id + '" name="ids"/>';
                    }},
                    {"targets" : 5,"className" : "text-c","render" : function(data, type, row, meta) {
                        return '<a href=\'javascript:void(0)\' onclick="showAccount(\''+ row.userId + '\')" >'+row.zhimiAmount+'</a>';
                    }},
                    {"targets" : 6,"className" : "text-c","render" : function(data, type, row, meta) {
                        if(row.zhimiType == "1") return "进账";
                        if(row.zhimiType == "2") return "出账";
                    }},
                    {"targets" : 8,"className" : "text-l","render" : function(data, type, row, meta) {
                        return row.reasonDesc==null?"":row.reasonDesc;
                    }},

                    {"targets" : 11,"className" : "text-c","render" : function(data, type, row, meta) {
                        if(row.reasonStatus == "1") return "已提交";
                        if(row.reasonStatus == "2") return "已通过";
                        if(row.reasonStatus == "3") return "已驳回";
                    }},
                    {"targets" : 12,"className" : "text-c","render" : function(data, type, row, meta) {
                        var dom="";
                        if(row.reasonStatus=="1"){
                             dom= '<a title="审核" href="javascript:void(0);" onclick="checkView(\'' + row.id + '\')" class="tableBtn normal" style="text-decoration: none">审核</a>';
                        }else{
                            dom= '<a title="查看" href="javascript:void(0);" onclick="showView(\'' + row.id + '\')" class="ml-5" style="text-decoration: none">' +
                                '<i class="iconfont icon-chakan"></i></a>';
                        }
                        return dom;
                    }}
                ]
            });
        });

        function showAccount(userId) {
            layer_show('账户信息', '/invite_user/toAccount.do' + '?userId='
                + userId, 1200, 600, function() {
                myDataTable.fnDraw(false);
            }, false);
        }

        function _search() {
            $(":checked").prop("checked", false);
            myDataTable.fnDraw(true);
        }

        //审核
        function checkView(id) {
            layer_show('审核', '/zhimi_give_check/toCheck.do'+'?id='+ id, null, 560);
        }


        //查看详情
        function showView(id) {
            var url = '/zhimi_give/toView.do'+'?id='+ id;
            layer_show('查看详情', url, null, 520);
        }