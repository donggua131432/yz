$(function(){
    $("#admin-role-save").remove()
	 var headPortrait = varGoodsInfo.annexPath;

     if (headPortrait) {
         createPhoto(_FILE_URL + headPortrait + "?" + Date.parse(new Date()));
     }
     $("select").attr("disabled", true);
     $("#annexPath").val(varGoodsInfo.annexUrl);
     
     $("#pcd").text(varGoodsInfo.provinceName+'-'+varGoodsInfo.cityName+'-'+varGoodsInfo.districtName+'-'+varGoodsInfo.streetName+'-'+varGoodsInfo.address);
     $("#applyReason").text(varGoodsInfo.applyReason);
     $("#operUserName").text(varGoodsInfo.operUserName);
     $("#operTime").text(varGoodsInfo.operTime);
     $("#receiveMobile").text(varGoodsInfo.receiveMobile);
     $("#receiveName").text(varGoodsInfo.receiveName);
     $("#applyName").text(varGoodsInfo.applyName);
     $("#goodsNum").text(varGoodsInfo.goodsNum);
     $("#goodsPrice").text(varGoodsInfo.goodsPrice);
     $("#goodsName").text(varGoodsInfo.goodsName);
     if(varGoodsInfo.ifSuccess =="0"){
    	 $("#remark").text(varGoodsInfo.remark).css('color','#f00');
     }else{
    	 $("#remark").text(varGoodsInfo.remark);
     }
     
});
function createPhoto(imgSrc) {
    var box = $("#member-photo");
    box.empty();
    var img = document.createElement('img');
    $(img).on('load', function() {
        var height = this.height;
        var width = this.width;
        if (height > width) {
            $(img).css('height', '140px')
        } else {
            $(img).css('width', '140px')
        }
    });
    img.src = imgSrc;
    box.html(img);
}