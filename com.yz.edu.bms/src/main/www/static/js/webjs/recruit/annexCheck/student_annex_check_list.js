var myDataTable;
$(function() {
    myDataTable = $('.table-sort').dataTable({
        "processing": true,
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/annexCheck/getAnnexCheckList.do",
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
		    {"mData" : 'stdName'},
            {"mData" : null},
            {"mData" : null},
            {"mData" : 'campusName'},
            {"mData" : 'dpName'},
            {"mData" : 'recruitName'},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null},
            {"mData" : null}
        ],
		"columnDefs" : [
		    {"targets" : 1,"render" : function(data, type, row, meta) {
				return _findDict('grade', row.grade);
		    }},
            {"targets" : 2, "class" : "text-l", "render" : function(data, type, row, meta) {
                var pfsnName = row.pfsnName,unvsName = row.unvsName,recruitType = row.recruitType,
                    pfsnCode = row.pfsnCode,pfsnLevel = row.pfsnLevel, text = "";
                if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                    text += "[成教]";
                }else {
                    text += "[国开]";
                }
                text += unvsName + "<br/>";

                if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                    text += "[专科]";
                }else {
                    text += "[本科]";
                }
                text += pfsnName+"(" + pfsnCode + ")"
                return text;
            }},
            {"targets" : 6,"render" : function(data, type, row, meta) {
                return _findDict('stdStage', row.stdStage);
            }},
            {"targets" : 7,"class":"text-l no-warp","render" : function(data, type, row, meta) {
                var isDataCompleted = row.isDataCompleted;
                var dom = '';
                if ('1' == isDataCompleted) {
                    dom = '<i class="iconfont icon-queren icon-s success"></i>已完善';
                } else {
                    dom = '<i class="iconfont icon-s warning">i</i>未完善';
                }
                return dom;
            }},
            {"targets" : 8,"class":"text-l no-warp","render" : function(data, type, row, meta) {
                var myAnnexStatus = row.myAnnexStatus, text =_findDict('myAnnexStatus', row.myAnnexStatus);
                if ('3' == myAnnexStatus) {
                    text = '<i class="iconfont icon-queren icon-s success"></i>'+text;
                } else if ('4' == myAnnexStatus) {
                    text = '<i class="iconfont  icon-s warning">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                } else if ('2' == myAnnexStatus) {
                    text = '<i class="iconfont icon-s warning">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                } else if ('1' == myAnnexStatus) {
                    text = '<i class="iconfont icon-s warning">i</i>' + text + '<span>(' + row.annexCount + ')</span>';
                }
                return text;
            }},
            {"targets" : 9,"render" : function(data, type, row, meta) {
                var dom = '';
                dom = '<a title="进入核查" href="javascript:;" onclick="showCheckPage(\'' + row.learnId + '\', \'' + row.stdId + '\', \'' + row.recruitType + '\')" class="ml-5" style="text-decoration: none">';
                dom += '<i class="Hui-iconfont f-18">&#xe692;</i></a>';
                return dom;
            }}
        ]
	});

	_init_select('myAnnexStatus', dictJson.myAnnexStatus);
	/*_init_select('recruitType', dictJson.recruitType);*/
	_init_select('scholarship', dictJson.scholarship);
	_init_select('grade', dictJson.grade);
	_init_select('sg', dictJson.sg); //优惠分组
	_init_select("pfsnLevel", dictJson.pfsnLevel);
	 _init_select("inclusionStatus",dictJson.inclusionStatus);
	$("#sg").change(function() { //联动
		_init_select({
			selectId : 'scholarship',
			ext1 : $(this).val()
		}, dictJson.scholarship);
	 });
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
	
	$("#recruitType").change(function() {
		_init_select({
			selectId : 'grade',
			ext1 : $(this).val()
		}, dictJson.grade);

	});

	$("#isDataCompleted").select2({
		placeholder : "--请选择--",
		allowClear : true
	});

	$("#stdStage").select2({
		placeholder : "--请选择--",
		allowClear : true
	});

    //校区-部门-组 联动
    //_init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
	
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
 	$("#campusId").append(new Option("", "", false, true));
 	
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
 	
});

var index;

function showCheckPage(learnId, stdId, recruitType) {
	var url = '/annexCheck/toCheckEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId + '&recruitType=' + recruitType;
	layer_show('考前资料核查', url, null, null, function() {
//					myDataTable.fnDraw(true);
	}, true);
}

function _search() {
	myDataTable.fnDraw(true);
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