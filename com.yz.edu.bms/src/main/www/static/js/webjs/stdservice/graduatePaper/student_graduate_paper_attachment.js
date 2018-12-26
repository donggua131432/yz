var myDataTable;
    $(function () {
        initTab();
    });

    function initTab() {
        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/graduatePaper/getCheckAttachments.do",
                type: "post",
                data: {
                    learnId:getQueryString("learnId")
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "language": _my_datatables_language,
            columns: [{
                "mData": null
            }, {
                "mData":null
            }, {
                "mData":"createTime"
            }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        return '<a href='+ _FILE_URL+ row.attachmentUrl+'>' + row.attachmentName + "</a><br/>";
                    },
                    "targets": 0
                },
                {
                    "render": function (data, type, row, meta) {
                        var dom = '';

                        dom = '<a title="审核" href="javascript:;" onclick="pass(\'' + row.attachmentId + '\')" class="ml-5" style="text-decoration: none">';
                        dom += '<i class="iconfont icon-shenhe"></i></a>';
                        return dom;
                    },
                    "targets": 1
                }
            ]
        });
    }


    function getQueryString(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }

    function pass(attachmentId) {
        var url = '/graduatePaper/paperAttachment.do?attachmentId='+attachmentId;
        layer_show('审核', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }