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
                url: "/graduatePaper/getAttachments.do",
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
                "mData": "attachmentName"
            }, {
                "mData": null
            }, {
                "mData": null
            }, {
                "mData":"commentTime"
            },{
                "mData":"createTime"
            }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        if (row.checkStatus && row.checkStatus == '1') {
                            return "<label class='label label-success radius'>审核通过</label>";
                        } else if (row.checkStatus && row.checkStatus == '2') {
                            return "<label class='label label-danger radius'>审核不通过</label>";
                        } else {
                            return "<label class='label radius'>未审核</label>";
                        }
                    },
                    "targets": 1
                },{
                    "render": function (data, type, row, meta) {
                        if(row.commentContent){
                            return row.commentContent.replace(/[\n\r]/g,'<br/>');
                        }
                        return row.commentContent;
                    },
                    "targets": 2,
                    "class": "text-l"
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