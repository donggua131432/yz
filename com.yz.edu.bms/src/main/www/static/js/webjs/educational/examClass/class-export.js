 $(function () {

     $("select").select2({
         placeholder: '--请选择--'
     })

     //初始省、市、区
     _init_area_select("provinceCode", "cityCode", "districtCode","440000");

     $("#eyId").change(function () {
         initEpName();
     })

     $("#cityCode").change(function () {
         initEpName();
     })

     $("#districtCode").change(function () {
         initEpName();
     })

     //初始考试年度
     $.ajax({
         type: "POST",
         dataType : "json", //数据类型
         url: '/examAffirm/getExamYear.do?status=1',
         success: function(data){
             var eyJson = data.body;
             if(data.code=='00'){
                 _init_select("eyId",eyJson);
             }
         }
     });

     $("#form-room-assign-edit").validate({
            rules: {
                eyId: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {
                layer.msg("正在导出中，请勿再次点击");
                var url = '/examClass/classExport.do';
                $('<form method="post" action="' + url + '">'
                    + '<input type="text" name="eyId" value="' + $("#eyId").val() + '"/>'
                    + '<input type="text" name="cityCode" value="' + $("#cityCode").val() + '"/>'
                    + '<input type="text" name="districtCode" value="' + $("#districtCode").val() + '"/>'
                    + '<input type="text" name="epId" value="' + $("#epId").val() + '"/>'
                    + '</form>').appendTo('body').submit().remove();
            }
        });
    });

    function initEpName() {
        //初始考试年度
        $.ajax({
            type: "POST",
            dataType : "json", //数据类型
            data:{eyId:$("#eyId").val(),cityCode:$("#cityCode").val(),districtCode:$("#districtCode").val()},
            url: '/examClass/selectEpName.do',
            success: function(data){
                var json = data.body;
                if(data.code=='00'){
                    _init_select("epId",json);
                }
            }
        });
    }
