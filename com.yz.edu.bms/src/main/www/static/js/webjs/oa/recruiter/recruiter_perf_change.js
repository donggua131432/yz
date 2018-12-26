var myDataTable;
$(function() {
	$('.radio-box input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	$('.checkAll').on('click',function () {
        if($(this).prop("checked")){
            $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').each(function (i,e) {
                if(!$(e).prop("checked")){
                    $(e).prop('checked',true)
                }
            })
        }else {
            $(this).parents('thead').siblings('tbody').find('.sendIdsCheckBox').prop('checked',false)
        }
    })
	_init_select('stdStage', dictJson.stdStage);
	_init_select('scholarship', dictJson.scholarship);
	_init_select('recruitType', dictJson.recruitType);
	_init_select('stdGrade', dictJson.grade);
	
	_init_select('operStatus', [
	                             {"dictValue":"0","dictName":"待分配"},
                       			 {"dictValue":"1","dictName":"已分配"}
	                             ]);

	$("#teacherName").text(empName);
	$("#oldJobInfo").text(oldCampusName+'--'+oldDpName+'--'+oldTitle);
	$("#newJobInfo").text(newCampusName+'--'+newDpName+'--'+newTitle)
	
	$("#recruitType").change(function() {
		_init_select({
			selectId : 'stdGrade',
			ext1 : $(this).val()
		}, dictJson.grade);

	});

	myDataTable = $('#perfChangeTable').dataTable({
		"serverSide" : true,
		"dom" : 'rtilp',
		"ajax" : {
			url : "/recruiter/perfList.do",
			type : "post",
			data :{
				"modifyId" : function (){
					return $("#modifyId").val();
				},								
				"empId": function () {
                    return $("#oldEmpId").val();
                },
                "dpId": function () {
                    return $("#oldDpId").val();
                },
                "stdName" : function (){
                	return $("#stdName").val();
                },
                "stdPhone" : function (){
                	return $("#stdPhone").val();
                },
                "stdIdCard" : function (){
                	return $("#stdIdCard").val();
                },
                "recruitType" : function (){
                	return $("#recruitType").val();
                },
                "stdUnvsName" : function (){
                	return $("#stdUnvsName").val();
                },
                "stdPfsnName" : function(){
                	return $("#stdPfsnName").val();
                },
                "stdStage" : function (){
                	return $("#stdStage").val();
                },
                "scholarship" : function (){
                	return $("#scholarship").val();
                },
               	"stdGrade" : function (){
               		return $("#stdGrade").val();
               	},
               	"operStatus" : function (){
               		return $("#operStatus").val();
               	}
               	,
                "beginTime" : function(){
                	return $("#beginTime").val();
                },
                "endTime" : function(){
                	return $("#endTime").val();
                }
                
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
		columns : [ {
			"mData" : null
		},{
			"mData" : "stdName"
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : null
		}, {
			"mData" : "operTime"
		}, {
			"mData" : "operUser"
		}  ],
		"columnDefs" : [ 
		{
		    "render": function (data, type, row, meta) {
		        return '<input type="checkbox" class="sendIdsCheckBox" value="'+ row.learnId+';'+row.recrodsNo + '" name="learnIds"/>';
		    },
		    "targets": 0
		},{
			"render" : function(data, type, row, meta) {
				return _findDict('grade', row.grade);
			},
			"targets" : 2
		},{
			"render" : function(data, type, row, meta) {

				return _findDict('scholarship', row.scholarship);
			},
			"targets" : 3
		}, {
			"render" : function(data, type, row, meta) {
				var text = unvsText(row);

				return text ? text : '无';
			},
			"sClass":'text-l',
			"targets" : 4
		},{
			"render" : function(data, type, row, meta) {
				return _findDict('stdStage', row.stdStage);
			},
			"targets" : 5
		},
		{
			"render" : function(data, type, row, meta) {
				return row.oldCampusName+'--'+row.oldDpName;
			},
			"targets" : 6
		},
		{
			"render" : function(data, type, row, meta) {
				if(row.operStatus =='1'){
					return "已分配"
				}else{
					return "待分配";	
				}
				
			},
			"targets" : 7
		},
		{
			"render" : function(data, type, row, meta) {
				if(null !=row.campusName && null !=row.dpName){
					return row.campusName+'--'+row.dpName;	
				}
				return "";
			},
			"targets" : 8
		}]
	});
});


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
		text += "[" + _findDict("recruitType", recruitType) + "]";
	}
	if (unvsName) {
		text += unvsName + "<br/>";
	}
	if (pfsnCode) {
		text += "(" + pfsnCode + ")";
	}
	if (pfsnName) {
		text += pfsnName;
	}
	if (pfsnLevel) {
		text += "[" + _findDict("pfsnLevel", pfsnLevel) + "]";
	}

	return text;
}
//部分分配
function partChange(operType) {
	var chk_value = [];
	$("input[name=learnIds]:checked").each(function() {
		chk_value.push($(this).val());
	});
	if(chk_value.length ==0){
		layer.msg('请选择要分配的学员数据！', {
			icon : 2,
			time : 2000
		});
		return;
	}
	layer.confirm('确认要分配选择的学员吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : '/recruiter/allocationLearn.do',
			data : {
				idArray : chk_value,
				empId : $("#oldEmpId").val(),
				operType:operType,
				personType:$('[name="personType"]:checked').val()
			},
			dataType : 'json',
			success : function(data) {
				layer.msg('分配成功!', {
					icon : 1,
					time : 1000
				});
				myDataTable.fnDraw(true);
				$("input[name=all]").attr("checked", false);
			},
			error : function(data) {
				layer.msg('添加失败！', {
					icon : 1,
					time : 1000
				});
				myDataTable.fnDraw(true);
				$("input[name=all]").attr("checked", false);
			},
		});
	});
}
//全部分配
function allChange(operType) {
	var chk_value = [];
	chk_value.push('1;2'); //防止报错,随便push个数据
	layer.confirm('确认要全部分配学员吗？', function(index) {
		$.ajax({
			type : 'POST',
			url : '/recruiter/allocationLearn.do',
			data : {
				idArray : chk_value,
				empId : $("#oldEmpId").val(),
				operType:operType,
				personType:$('[name="personType"]:checked').val()
			},
			dataType : 'json',
			success : function(data) {
				layer.msg('分配成功!', {
					icon : 1,
					time : 1000
				});
				myDataTable.fnDraw(true);
				$("input[name=all]").attr("checked", false);
			},
			error : function(data) {
				layer.msg('添加失败！', {
					icon : 1,
					time : 1000
				});
				myDataTable.fnDraw(true);
				$("input[name=all]").attr("checked", false);
			},
		});
	});
}