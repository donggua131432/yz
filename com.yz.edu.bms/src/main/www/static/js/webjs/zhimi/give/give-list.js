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
                    url : "/zhimi_give/list.do",
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
                        var dom = '<a title="查看" href="javascript:void(0);" onclick="showView(\'' + row.id + '\')" class="ml-5" style="text-decoration: none">' +
                            '<i class="iconfont icon-chakan"></i></a>';
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

        function giveZhimi_add() {
            var url = '/zhimi_give/toAdd.do';
            layer_show('新增智米赠送申请', url, null, 520, function () {

            });
        }


        function delAll() {
            var chk_value = [];
            var $input = $("input[name=ids]:checked"), flag=true;

            $input.each(function () {
                chk_value.push($(this).val());
                if($(this).attr("reasonStatus")!='1'){
                    flag=false;
                }
            });

            if(!flag){
                layer.msg('只能删除未审核的数据!', {icon: 5,time: 1000});
                return;
            }

            if (chk_value == null || chk_value.length <= 0) {
                layer.msg('未选择任何数据!', {icon: 5,time: 1000});
                return;
            }

            layer.confirm('确认要删除吗？', function (index) {
                $.ajax({
                    type: 'POST',
                    url: '/zhimi_give/deleteGiveRecords.do',
                    data: {
                        ids: chk_value
                    },
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == _GLOBAL_SUCCESS) {
                            layer.msg('已删除!', {
                                icon: 1,
                                time: 1000
                            });
                            myDataTable.fnDraw(false);
                            $("input[name=all]").attr("checked", false);
                        }
                    }
                });
            });
        }

        //查看详情
        function showView(id) {
            var url = '/zhimi_give/toView.do'+'?id='+ id;
            layer_show('查看详情', url, null, 520);
        }