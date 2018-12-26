var myDataTable;
$(function() {
	_init_select('stdStage', dictJson.stdStage);
	_init_select('recruitType', dictJson.recruitType);
	_init_select('grade', dictJson.grade);

	$("#recruitType").change(function() {
		_init_select({
			selectId : 'grade',
			ext1 : $(this).val()
		}, dictJson.grade);

	});
	 _init_select('sg', dictJson.sg); //优惠分组
	 _init_select('scholarship', dictJson.scholarship);
	 _init_select("inclusionStatus",dictJson.inclusionStatus);
	 $("#sg").change(function() { //联动
		_init_select({
			selectId : 'scholarship',
			ext1 : $(this).val()
		}, dictJson.scholarship);
	 });
    //是否外校
    _init_select("stdType", dictJson.stdType);
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
        placeholder: '--请选择--'
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
	 
	 
	myDataTable = $('.table-sort').dataTable({
		"processing": true,
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/studentSearch/findAllStudent.do",
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
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : null},
			{"mData" : "tutorName"},
			{"mData" : null}
		],
		"columnDefs" : [
			{"targets" : 0,"render" : function(data, type, row, meta) {
                    var dom = "<div class='name-mark-box'>"+'<p class="name-mark-name no-warp">'+row.stdName+'</p>';
                    dom+='<p class="text-c">';
                    if(row.stdType ==='2'){
                        dom += '<span class="name-mark mark-red">外</span>';
                    }
                    if(row.assignFlag === '1'){
                        dom += '<span class="name-mark mark-blue">分配</span>';
                    }
                    dom+="</p></div>"
                    return dom;
			}},
			{"targets" : 1, "render" : function(data, type, row, meta) {
				return _findDict('grade', row.grade);
			}},
			{"targets" : 2, "class":"text-l", "render" : function(data, type, row, meta) {
				var text = unvsText(row);
				return text ? text : '无';
			}},
			{"targets" : 3,"render" : function(data, type, row, meta) {
				return _findDict('stdStage', row.stdStage);
			}},
			{"targets" : 4,"render" : function(data, type, row, meta) {
				return _findDict('scholarship', row.scholarship);
			}},
			{"targets" : 5,"render" : function(data, type, row, meta) {
				return _findDict('inclusionStatus', row.inclusionStatus);
			}},
			{"targets" : 6,"render" : function(data, type, row, meta) {
			 	return row.dpName == null ? '推广部':row.dpName;
			}},
			{"targets" : 7,"render" : function(data, type, row, meta) {
				var dom = row.recruitName == null ? '智哥':row.recruitName;
				if(row.empStatus ==='2'){
					dom += ' <sup style="color:#f00">离</sup>';
				}
				return dom;
			}},
			{"targets" : 9,"render" : function(data, type, row, meta) {
				var dom = '<a title="编辑" href="javascript:;" onclick="toEidt(\'' + row.learnId + '\',\'' + row.stdId + '\',\'' + row.recruitType + '\')" class="ml-5" style="text-decoration: none">';
				dom += '<i class="iconfont icon-edit"></i></a>';
				return dom;
			}}
		]
	});
	
});



/*用户-编辑*/
function toEidt(learnId, stdId, recruitType) {
	var url = '/allStudent/toEdit.do?learnId=' + learnId + '&stdId=' + stdId+ '&recruitType=' + recruitType;
	layer_show('学员信息', url, null, null, function() {
		//myDataTable.fnDraw(false);
	}, true);
}

function _search() {
	myDataTable.fnDraw(true);
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