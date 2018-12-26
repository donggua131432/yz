 $(function(){
        if(checkData) {
            var dom = '<tr class="text-c odd">';
            dom += "<td>" + (checkData.titleName ? checkData.titleName : '无') + '</td>'; 
            dom += '<td>' + (_findDict('checkStatus', checkData.checkStatus) ? _findDict('checkStatus', checkData.checkStatus) : '无' )+ '</td>';
            dom += '<td>' + (checkData.updateUser ? checkData.updateUser : '无') + '</td>';
            dom += '<td>' + (checkData.titleName ? (checkData.updateTime ? checkData.updateTime : '无') : '无') + '</td>';
            dom += '</tr>';
            $("#checkTable").append(dom);
        } else {
            var dom = '<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>';
            $("#checkTable").append(dom);
        }
        
        var tbody = $("#graduateTable").find("tbody");
        if(gList && gList.length > 0) {
            $.each(gList,function(index, data){
                var dom = '<tr class="text-c odd">';
                dom += "<td>" + data.titleName + '</td>'; 
                dom += '<td>' + _findDict('checkStatus', data.checkStatus) + '</td>';
                dom += '<td>' + data.updateUser + '</td>';
                dom += '<td>' + data.updateTime + '</td>';
                dom += '</tr>';
                tbody.append(dom);
            });
        } else {
            var dom = '<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>';
            tbody.append(dom);
        }
        
    });