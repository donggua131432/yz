$(function(){
        var ebody = $("#eScoreTable").find("tbody");
        if(eScoreList && eScoreList.length > 0) {
            $.each(eScoreList,function(index, data){
                var dom = '<tr class="text-c odd">';
                dom += "<td>" + data.courseName + '</td>';
                dom += '<td>' + data.score + '</td>';
                dom += '</tr>';
                ebody.append(dom);
            });
        } else {
            var dom = '<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>';
            ebody.append(dom);
        }

        var tbody = $("#tScoreTable").find("tbody");
        if(tScoreList && tScoreList.length > 0) {
            $.each(tScoreList,function(index, data){
                var dom = '<tr class="text-c odd">';
                dom += "<td>" + data.semester + '</td>';
                dom += '<td>' + data.courseName + '</td>';
                dom += '<td>' + data.score + '</td>';
                var teacher = data.teacher == null ? "--":data.teacher;
                dom += '<td>' + teacher + '</td>';
                if('1' == data.isPass) {
                    dom += '<td><span class="label label-success radius">通过</span></td>';
                } else {
                    dom += '<td><span class="label label-default radius">不通过</span></td>';
                }
                dom += '</tr>';
                tbody.append(dom);
            });
        } else {
            var dom = '<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>';
            tbody.append(dom);
        }
    });