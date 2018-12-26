var myDataTable;
    $(function () {

        //初始省、市、区
        _init_area_select("provinceCode", "cityCode", "districtCode","440000");

        $("#districtCode").append("<option value=''>请选择</option>");
        $("#eyId").append("<option value=''>请选择</option>");

        //初始是否启用
        _init_select("divideStatus", [{
            "dictValue": "1",
            "dictName": "已划分"
        }, {
            "dictValue": "0",
            "dictName": "未划分"
        }]);

        //初始考试年度
        $.ajax({
            type: "POST",
            dataType : "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=1',
            success: function(data){
                var eyJson = data.body;
                if(data.code=='00'){
                    _init_select("eyId",eyJson);
                }
            }
        });

        //初始化数据表格
        myDataTable = $('.table-sort').dataTable({
            "processing": true,
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: "/examClass/list.do",
                type: "post",
                data: {
                    "pyCode": function () {
                        return $("#pyCode").val();
                    },
                    "epName": function () {
                        return $("#epName").val();
                    },
                    "provinceCode": function () {
                        return $("#provinceCode").val();
                    },
                    "cityCode": function () {
                        return $("#cityCode").val();
                    },
                    "districtCode": function () {
                        return $("#districtCode").val();
                    },
                    "startTime": function () {
                        return $("#startTime").val();
                    },
                    "endTime": function () {
                        return $("#endTime").val();
                    },
                    "divideStatus": function () {
                        return $("#divideStatus").val();
                    },
                    "eyId": function () {
                        return $("#eyId").val();
                    }
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
            columns: [
                {
                    "mData": "examYear"
                },
                {
                    "mData": "pyCode"
                },
                {
                    "mData": null
                },
                {
                    "mData": "epName"
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                },
                {
                    "mData": "divideRemark"
                },
                {
                    "mData": null
                }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        return row.provinceName + row.cityName + row.districtName;
                    },
                    "targets": 2
                },
                {
                    "render": function (data, type, row, meta) {
                        var dateTime=row.startTime.replace('AM', '上午').replace('PM', '下午') + "-" + row.endTime;
                        if(!dateTime){
                            return '-'
                        }
                        var date=dateTime.substring(0,10)
                        var time=dateTime.substring(11)
                        return date+'<br>'+time
                    },
                    "targets": 4,"class":"text-l no-warp"
                },
                {
                    "render": function (data, type, row, meta) {
                    	return (row.seats * 1 - row.restSeats) + '/' + row.seats;
                    },
                    "targets": 5
                },
                {
					"render" : function(
							data, type,
							row, meta) {
						var dom = '';
						dom += '<table class="table table-border table-bordered table-bg table-sort" id="tab">';
						dom += '<thead>';
						dom += '<tr class="text-c">';
						dom += '<th width="30" class="td-s">课室名称</th>';
						dom += '<th width="30" class="td-s">划分容量/人</th>';
						dom += '</tr></thead>';
						dom += '<tbody>';
						var places = row.places;
						for (var i = 0; i < places.length; i++) {
							var place = places[i];
							dom += '<tr>';
							dom += '<td class="td-s">' + place.placeName + '</td>';
							dom += '<td class="td-s">' + place.maxCount + '</td>';
							dom += '</tr>';
						}
						dom += '</tbody></table>';
						return dom;
					},
					"targets" : 6
				},
				{
                    "render": function (data, type, row, meta) {
                    	if(null != row.places && row.places.length > 0){
                    		return '已划分';
                    	} else {
                    		return '未划分';
                    	}
                    	
                    },
                    "targets": 7
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                    	var dom = '';
                        dom += '<a title="编辑" href="javascript:void(0)" onclick="edit(\''
                            + row.pyId
                            + '\')" class="ml-5" style="text-decoration:none">';
                        dom += '<i class="iconfont icon-edit"></i></a>';
                        return dom;
                    },
                    "targets": 9
                }
            ]
        });

    });


    /*考场安排-编辑*/
    function edit(pyId) {
        var url = '/examClass/toDivide.do' + '?pyId=' + pyId;
        layer_show('编辑', url, null, null, function () {

        },true);
    }


    function _search() {
        myDataTable.fnDraw(true);
    }

    /*考场教室划分*/
    function classExport() {
        var url = '/examClass/toClassExport.do';
        layer_show('导出考场教室划分', url, null, 400, function () {
        });
    }