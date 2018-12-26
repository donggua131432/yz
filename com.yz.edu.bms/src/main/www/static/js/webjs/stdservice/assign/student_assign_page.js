var empListTable;
$(function () {
    empListTable = $('#empListTable').dataTable(
        {
            "serverSide": true,
            "dom": 'rtilp',
            "ajax": {
                url: '/assign/getEmpList.do',
                type: "post",
                data: function (data) {
                    return searchData(data);
                }
            },
            "pageLength": 10,
            "pagingType": "full_numbers",
            "ordering": false,
            "searching": false,
            "lengthMenu": [10, 20],
            "language": _my_datatables_language,
            "createdRow": function (row, data, dataIndex) {
                $(row).addClass('text-c');
            },
            "columns": [{
                "mData": "empName"
            }, {
                "mData": null
            }, {
                "mData": "mobile"
            }, {
                "mData": null
            }, {
                "mData": null
            }],
            "columnDefs": [
                {
                    "render": function (data, type, row, meta) {
                        var dom = '';
                        dom += '<div>'
                        dom += '<input type="button" class="submit tableBtn normal" value="选择" onclick="ditribution(\'' + row.empId + '\',\''
                            + row.campusId + '\',\'' + row.dpId + '\',\'' + row.empName + '\',\'' + row.campusName + '\',\'' + row.dpName + '\',\''
                            + row.campusManager + '\',\'' + row.empStatus + '\');" />';
                        dom += '</div>';

                        return dom;
                    },
                    "targets": 4
                }, {
                    "render": function (data, type, row, meta) {
                        return _findDict("jobTitle", row.jtId);
                    },
                    "targets": 1
                }, {
                    "render": function (data, type, row, meta) {
                        return _findDict("empStatus", row.empStatus);
                    },
                    "targets": 3
                }],

        });

    var addType = addType_global;
    var studentList = studentList_global;
    
    if(addType=='3'){
    	//查询批量分配
    	var datas={};
    	$.each($(window.parent.document).find('#searchForm').serializeArray(),function(){
    		datas[this.name]=this.value;
    	});
    	var url = "/assign/queryDistribution.do";
        $.ajax({
            type : 'POST',
            url : url,
            data: datas,
            dataType : 'json',
            success : function(data) {
                if (data.code == _GLOBAL_SUCCESS) {
                	studentList=data.body;
                	showStudentList(studentList);
                }
            }
        });
    }else{
    	showStudentList(studentList);
    }
    
	
    
    
});

function showStudentList(studentList){
	$.each(studentList, function (index, data) {
        var dom = '';
        var pfsnName = data.pfsnName;
        var unvsName = data.unvsName;
        var recruitType = data.recruitType;
        var pfsnCode = data.pfsnCode;
        var pfsnLevel = data.pfsnLevel;

        var text = "[" + _findDict("recruitType", recruitType) + "]";
        text += unvsName + ":";
        text += "(" + pfsnCode + ")" + pfsnName + "[" + _findDict("pfsnLevel", pfsnLevel) + "]";

        dom += "<tr class='text-c'>";
        dom += "<td>" + data.stdName + "</td>";
        dom += "<td>" + data.idCard + "</td>";
        dom += "<td>" + _findDict('stdStage', data.stdStage) + "</td>";
        dom += "<td>" + (data.stdNo ? data.stdNo : '') + "</td>";
        dom += "<td class='text-l'>" + text + "</td>";
        dom += "<td>" + (data.campusName == null ? '无' : data.campusName) + "</td>";
        dom += "<td>" + (data.recruitName == null ? '无' : data.recruitName) + "</td>";
        dom += "</tr>";
        $("#studentListTable").find("tbody").append(dom);
        $(".page-container").append("<input type='hidden' name='learnIds' value='" + data.learnId + "' />")
    });
}

function searchData(pageData) {
    return {
        campus: $("#campus").val() ? $("#campus").val() : '',
        department: $("#department").val() ? $("#department").val() : '',
        group: $("#group").val() ? $("#group").val() : '',
        mobile: $("#mobile").val() ? $("#mobile").val() : '',
        jtIds: "FDY",
        start: pageData.start,
        length: pageData.length
    };
}

function _search() {
    empListTable.fnDraw(true);
}

function ditribution(empId, campusId, dpId, empName, campusName, dpName, campusManager, empStatus) {
    layer.confirm('确定分配?', function () {
        var ids = '';
        $.each($("input[name='learnIds']"), function (index, data) {
            var learnId = $(data).val();
            ids += $(this).val() + ",";
        });

        if (ids && ids.length > 0) {
            ids = ids.substring(0, ids.length - 1);
        }
        var submitData = {
            learnIds: ids,
            empId: empId,
            empName: empName,
            campusId: campusId,
            dpId: dpId,
            campusManager: campusManager
        };

        $.ajax({
            url: '/assign/distribution.do',
            dataType: 'json',
            type: 'post',
            data: submitData,
            success: function (data) {
                if (data.code == _GLOBAL_SUCCESS) {
                    layer.msg('分配成功', {
                        icon: 1,
                        time: 1000
                    }, function () {
                        window.parent.myDataTable.fnDraw(false);
                        layer_close();
                    });
                }
            }
        });
    });
}