$(function(){
        var checkData = checkStatus_global;
        if(checkData) {
            $.each(checkData, function (index, item) {
                var dom = '<tr class="text-c odd">';

                dom += "<td>" + (item.jtId ? _findDict('jtId', item.jtId ) : '无') + '</td>';
                dom += '<td>' + (_findDict('checkStatus', item.checkStatus) ? _findDict('checkStatus', item.checkStatus) : '无' )+ '</td>';
                dom += '<td>' + (item.updateUser ? item.updateUser : '无') + '</td>';
                dom += '<td>' + (item.updateTime ? (item.updateTime ? item.updateTime : '无') : '无') + '</td>';
                dom += '</tr>';
                $("#checkTable").append(dom);
            });
        } else {
            var dom = '<tr class="odd"><td valign="top" colspan="8" class="dataTables_empty"><div style="text-align:center;font:bold 13px/22px arial,sans-serif;color:red;">没有检索到数据！</div></td></tr>';
            $("#checkTable").append(dom);
        }
        
        var gList = graduate_global;
        var tbody = $("#graduateTable").find("tbody");
        if(gList && gList.length > 0) {
            $.each(gList,function(index, data){
                var dom = '<tr class="text-c odd">';
                dom += "<td>" + (data.jtId ? _findDict('jtId', data.jtId ) : '无') + '</td>';
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