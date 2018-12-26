$(function() {
	
	var row = $("#result").val();
	var json = eval('(' + row + ')');
	initResultInfoDom(json);
})
function initResultInfoDom(row){
	var dom = '';
	dom += '<table class="table table-border table-bordered table-hover table-bg table-sort">';
	dom += '<thead>';
	dom += '<tr><th scope="col" colspan="6">往期中奖结果查看</th></tr>'
	dom += '<tr>';
	dom += '<th width="80">竞拍期数</th>';
	dom += '<th width="40">中拍人</th>';
	dom += '<th width="40">手机号</th>';
	dom += '<th width="40">中拍价</th>';
	dom += '<th width="40">中奖时间</th>';
	dom += '</tr>';
	dom += '</thead><tbody>';

	for (var i = 0; i < row.length; i++) {
		var result = row[i];
		dom += '<tr>';
		dom += '<td>'+ result.planCount + '</td>';
		dom += '<td>'+ result.userName + '</td>';
		dom += '<td>'+ result.mobile + '</td>';
		dom += '<td>'+ result.auctionPrice + '</td>';
		dom += '<td>'+ result.mineTime + '</td>';
		dom += '</tr>';
	}
	dom += '</tbody>';
	dom += '</table>';
	$("#resultInfoDiv").html(dom);
}