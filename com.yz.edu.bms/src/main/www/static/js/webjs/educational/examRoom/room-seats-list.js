 var myDataTable;
    $(function () {

        $("#eyId").append("<option value=''>请选择</option>");


        $("#placeId").select2({
            placeholder:'--请选择--'
        });

        //初始考场
        initPlaceId();

        $("#eyId").change(function () {
            //初始考场
            initPlaceId();
        })

        //初始化招生类型
        _init_select('recruitType', dictJson.recruitType);

        //初始化年级下拉框
        _init_select("grade", dictJson.grade);

        //初始化专业层次下拉框
        _init_select("pfsnLevel", dictJson.pfsnLevel);

        //初始化院校名称下拉框
        _simple_ajax_select({
            selectId: "unvsId",
            searchUrl: '/bdUniversity/findAllKeyValue.do',
            sData: {},
            showText: function (item) {
                return item.unvs_name;
            },
            showId: function (item) {
                return item.unvs_id;
            },
            placeholder: '--请选择院校--'
        });
        $("#unvsId").append(new Option("", "", false, true));

        $("#unvsId").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
	     });
        $("#grade").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
	     });
		 $("#pfsnLevel").change(function () {
            $("#pfsnId").removeAttr("disabled");
            init_pfsn_select();
	     });
		 $("#pfsnId").append(new Option("", "", false, true));
		 $("#pfsnId").select2({
	            placeholder: "--请先选择院校--"
	     });
        
        //初始考试年度
        $.ajax({
            type: "POST",
            dataType : "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=',
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
                url: "/examRoomSeats/findAllExamRoomSeats.do",
                type: "post",
                data: {
                    "stdName": function () {
                        return $("#stdName").val();
                    },
                    "idCard": function () {
                        return $("#idCard").val();
                    },
                    "mobile": function () {
                        return $("#mobile").val();
                    },
                    "unvsId": function () {
                        return $("#unvsId").val();
                    },
                    "pfsnId": function () {
                        return $("#pfsnId").val();
                    },
                    "pfsnLevel": function () {
                        return $("#pfsnLevel").val();
                    },
                    "grade": function () {
                        return $("#grade").val();
                    },
                    "placeId": function () {
                        return $("#placeId").val();
                    },
                    "eyId": function () {
                        return $("#eyId").val();
                    },
                    "startTime": function () {
                        return $("#startTime").val();
                    },
                    "endTime": function () {
                        return $("#endTime").val();
                    },
                    "recruitType": function () {
                        return $("#recruitType").val();
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
                    "mData": "stdName"
                },
                {
                    "mData": null
                },
                {
                    "mData": null
                },
                {
                    "mData": "examYear"
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
                    "mData": "eypCode"
                },
                {
                    "mData": null
                }],
            "columnDefs": [
                {
                    "render": function (data, type,
                                        row, meta) {
                        return _findDict('grade', row.grade);
                    },
                    "targets": 1
                },
                {
                    "render": function (data, type, row, meta) {
                        var text = unvsText(row);
                        return text ? text : '无';
                    },
                    "targets": 2,"class":"no-warp text-l"
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
                    "targets": 5,"class":"text-l"
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                        return row.placeName+":"+"座位["+row.esNum+"]"
                    },
                    "targets": 6
                },
                {
                    "render": function (data, type,
                                        row, meta) {
                        if (row.signStatus == 1) {
                            return '<span class="label label-success radius">已签到</span>';
                        } else {
                            return '<span class="label label-danger radius">未签到</span>';
                        }
                    },
                    "targets": 8
                }
            ]
        });

    });

    /* 专业代号导入*/
    function profession_excel_import() {
        var url = '/examRoomSeats/professionImport.do';
        layer_show('专业代号导入', url, null, 510, function () {
            myDataTable.fnDraw(true);
        });
    }

    /* 自动生成座位表*/
    function generate() {
        var url = '/examRoomSeats/toGenerateExamSeats.do';
        layer_show('自动生成座位表', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }

    /* 座位表PDF导出*/
    function pdf() {
        var url = '/examRoomSeats/toGetExamSeatsInfo.do';
        layer_show('座位表导出', url, null, 510, function () {
            myDataTable.fnDraw(false);
        });
    }

    function unvsText(data) {
        var pfsnName = data.pfsnName;
        var unvsName = data.unvsName;
        var recruitType = data.recruitType;
        var pfsnCode = data.pfsnCode;
        var pfsnLevel = data.pfsnLevel;

        var text = '';
        if (recruitType) {
            if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                text += "[成教]";
            }else {
                text += "[国开]";
            }
        }
        if (unvsName) {
            text += unvsName + "<br/>";
        }
        if (pfsnLevel) {
            if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                text += "[专科]";
            }else {
                text += "[本科]";
            }
        }
        if (pfsnName) {
            text += pfsnName;
        }
        if (pfsnCode) {
            text += "(" + pfsnCode + ")";
        }
        return text;
    }

    function _search() {
        myDataTable.fnDraw(true);
    }

    function initPlaceId() {
        _simple_ajax_select({
            selectId: "placeId",
            searchUrl: '/examRoomAssign/findAllKeyValueByEyId.do',
            sData: {
                eyId: function () {
                    return $("#eyId").val();
                }
            },
            showText: function (item) {
                return item.ep_name;
            },
            showId: function (item) {
                return item.ep_id;
            },
            placeholder: '--请选择--'
        });
        $("#placeId").append(new Option("", "", false, true));
    }
    
    function init_pfsn_select() {
    	_simple_ajax_select({
			selectId : "pfsnId",
			searchUrl : '/baseinfo/sPfsn.do',
			sData : {
				sId :  function(){
					return $("#unvsId").val() ? $("#unvsId").val() : '';	
				},
				ext1 : function(){
					return $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '';
				},
				ext2 : function(){
					return $("#grade").val() ? $("#grade").val() : '';
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
		$("#pfsnId").append(new Option("", "", false, true));
    }