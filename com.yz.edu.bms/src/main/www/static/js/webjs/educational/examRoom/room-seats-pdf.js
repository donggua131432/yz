 $(function () {
    var downloadPDFFlag=true;
        $("#epId").select2({
            placeholder:'--请选择--'
        });
        $("#examTime").select2({
            placeholder:'--请选择--'
        });
        $("#placeId").select2({
            placeholder:'--请选择--'
        });



         //初始省、市、区
         _init_area_select("provinceCode", "cityCode", "districtCode","440000");

         $("#provinceCode,#cityCode,#districtCode").change(function () {
             $("#epId").val('').change();
         })

        //初始考试年度下拉框
        $.ajax({
            type: "POST",
            dataType: "json", //数据类型
            url: '/examAffirm/getExamYear.do?status=1',
            success: function (data) {
                var eyJson = data.body;
                if (data.code == '00') {
                    _init_select("eyId", eyJson);
                }
            }
        });
        $("#eyId").change(function () {
            $("#eyName").val($(this).find('>option:selected').text());
            if($("#epId").html()){
                $('#epId').select2('destroy');
                $("#epId").html('');
                $("#epId").change();
            }
            if($(this).val()){
                //初始考场
                _simple_ajax_select({
                    selectId: "epId",
                    searchUrl: '/examRoomAssign/findAllKeyValueByEyId.do',
                    sData: {
                        eyId: function () {
                            return $("#eyId").val();
                        },
                        provinceCode: function () {
                            return $("#provinceCode").val();
                        },
                        cityCode: function () {
                            return $("#cityCode").val();
                        },
                        districtCode: function () {
                            return $("#districtCode").val();
                        }
                    },
                    showText: function (item) {
                        return item.ep_name;
                    },
                    showId: function (item) {
                        return item.ep_id;
                    },
                    placeholder: '--请选择--'
                });
                $("#epId").append("<option value=''>请选择考场</option>");
            }else {
                $("#epId").select2({
                    placeholder:'--请选择--'
                });
            }
        });

        $("#epId").change(function () {
            $("#epName").val($(this).find('>option:selected').text());
            if($("#examTime").html()){
                $('#examTime').select2('destroy');
                $("#examTime").html('');
                $("#examTime").change();
            }
            if($(this).val()){
                $.ajax({
                    type: "POST",
                    dataType: "json", //数据类型
                    data: {epId: $(this).val(), eyId: $("#eyId").val()},
                    url: '/examRoomAssign/findExamTime.do',
                    success: function (data) {
                        var json = data.body||[];
                        if (data.code == '00') {
                            json=JSON.parse(JSON.stringify(json).replace(/PM/g,'下午').replace(/AM/g,'上午'));
                            _init_select("examTime", json);
                        }
                    }
                });
            }else {
                $("#examTime").select2({
                    placeholder:'--请选择--'
                });
            }

        });

        $("#examTime").change(function () {
            if($("#placeId").html()){
                $('#placeId').select2('destroy');
                $("#placeId").html('');
                $("#placeId").change();
            }
            if($(this).val()){
                $.ajax({
                    type: "POST",
                    dataType: "json", //数据类型
                    data: {startTime: $(this).val().split("=")[0], endTime: $(this).val().split("=")[1], eyId: $("#eyId").val(), epId: $("#epId").val()},
                    url: '/examRoomSeats/findPlaceInfo.do',
                    success: function (data) {
                        var json = data.body;
                        if (data.code == '00') {
                            _init_select("placeId", json);
                        }
                    }
                });
            }else {
                $("#placeId").select2({
                    placeholder:'--请选择--'
                });
            }
        })

        $("#placeId").change(function () {
            $("#placeName").val($(this).find('>option:selected').text());
            downloadPDFFlag=true;
            $(".pdfContentDiv").html('')
        });

        $("#form-room-assign-edit").validate({
            rules: {
                eyId: {
                    required: true,
                },
                epId: {
                    required: true,
                },
                examTime: {
                    required: true,
                },
                placeId: {
                    required: true,
                }
            },
            onkeyup: false,
            focusCleanup: true,
            success: "valid",
            submitHandler: function (form) {

                $(form).ajaxSubmit({
                    type: "post", //提交方式
                    dataType: "json", //数据类型
                    url: '/examRoomSeats/getExamSeatsInfo.do',
                    success: function (data) { //提交成功的回调函数
                        if (data.code == _GLOBAL_SUCCESS) {
                            //导出PDF
                            var pdfData=data.body;

                            if(pdfData.length==0){
                                layer.msg('没有相关座位表信息！', {icon : 0, time : 1500},function(){
                                });
                                return
                            }
//                            console.log(content);
                            var areaName=$("#epId").select2("data")[0].text;
                            var examTime=$("#examTime").select2("data")[0].text
                            var roomName=$("#placeId").select2("data")[0].text;
                            var examYear=$("#eyId").select2("data")[0].text;

//        下载文件名
                            var fileName=areaName+examTime+roomName;

//        体验优化
                            $("#downloadPDF").prop("disabled",true).attr("value",'生成中...');
                            $(".pdfItem").css('display','block');

//        设置初始页
                            var index=1;


                            if(downloadPDFFlag){
//                                debugger
                                //    引入数据。。。。。,渲染页面
//            获取页总数，即每页47条数据
                                var allPageN=Math.ceil(pdfData.length/47);

//            拼串，渲染页面
                                var content='<div class="pdfItem"><table> <thead> <tr> ' +
                                    '<th colspan="9">'+examYear+'远智教育成教期末考试'+fileName+'考场签到表</th> ' +
                                    '</tr> <tr> <th>座位</th> <th>姓名</th> <th>学号</th> <th>年级</th> <th>院校名称</th> ' +
                                    '<th>层次</th> <th>专业</th> <th>编码</th> <th>签到</th> </tr> </thead> <tbody>';

                                var text=''

                                pdfData.forEach(function (e,i) {
                                    var esNum=e.esNum||'无';
                                    var stdName=e.stdName||'无';
                                    var std_no=e.std_no||'无';
                                    var grade=e.grade||'无';
                                    var unvsName=e.unvsName||'无';
                                    var pfsnLevel=e.pfsnLevel||'无';
                                    var pfsnName=e.pfsnName||'无';
                                    var eypCode=e.eypCode||'无';

//                最后一条数据，结束
                                    if(i==pdfData.length-1){
//                    拼接座位数据
                                        var text2='<tr> ' +
                                            '<td>'+esNum+'</td> ' +
                                            '<td>'+stdName+'</td> ' +
                                            '<td>'+std_no+'</td> ' +
                                            '<td>'+grade+'</td> ' +
                                            '<td>'+unvsName+'</td> ' +
                                            '<td>'+pfsnLevel+'</td> ' +
                                            '<td>'+pfsnName+'</td> ' +
                                            '<td>'+eypCode+'</td> ' +
                                            '<td>'+e.sign+'</td> </tr>';
                                        text+=text2

                                        content+=text;
                                        content+='</tbody> </table>' +
                                            ' <p>'+fileName+'，第'+index+'页，共'+allPageN+'页</p></div>'
                                        $('.pdfContentDiv').html(content);
                                        return;
                                    }

                                    if(i==(47*index)-1){
                                        var text2='<tr> ' +
                                            '<td>'+esNum+'</td> ' +
                                            '<td>'+stdName+'</td> ' +
                                            '<td>'+std_no+'</td> ' +
                                            '<td>'+grade+'</td> ' +
                                            '<td>'+unvsName+'</td> ' +
                                            '<td>'+pfsnLevel+'</td> ' +
                                            '<td>'+pfsnName+'</td> ' +
                                            '<td>'+eypCode+'</td> ' +
                                            '<td>'+e.sign+'</td> </tr>';
                                        text+=text2

                                        content+=text;
                                        content+='</tbody> </table>' +
                                            ' <p>'+fileName+'，第'+index+'页，共'+allPageN+'页</p>+</div>'
                                        index++;
                                        content+='<div class="pdfItem"><table> <thead> <tr> ' +
                                            '<th colspan="9">'+examYear+'远智教育成教期末考试'+fileName+'考场签到表</th> ' +
                                            '</tr> <tr> <th>座位</th> <th>姓名</th> <th>学号</th> <th>年级</th> <th>院校名称</th> ' +
                                            '<th>层次</th> <th>专业</th> <th>编码</th> <th>签到</th> </tr> </thead> <tbody>';
                                        text=''
                                    }else {
//                    拼接座位数据
                                        var text2='<tr> ' +
                                            '<td>'+esNum+'</td> ' +
                                            '<td>'+stdName+'</td> ' +
                                            '<td>'+std_no+'</td> ' +
                                            '<td>'+grade+'</td> ' +
                                            '<td>'+unvsName+'</td> ' +
                                            '<td>'+pfsnLevel+'</td> ' +
                                            '<td>'+pfsnName+'</td> ' +
                                            '<td>'+eypCode+'</td> ' +
                                            '<td>'+e.sign+'</td> </tr>';
                                        text+=text2
                                    }
                                });

                                downloadPDFFlag=false;
                            }

                            $('.pdfItem').width(2080).height(3100).tableExport({type:'pdf',
                                fileName:fileName,
                                jspdf: {orientation: 'p',
                                    margins: {left:10, top:10},
                                    autotable:false
                                }
                            },function () {
                                $('.pdfItem').css("display","none");
                                $("#downloadPDF").prop("disabled",false).attr("value",'生成PDF');
                            });
                        }
                    }
                })
            }
        });

        $("#downloadExcel").click(function () {
            if($("#eyId").val()==""){
                layer.msg("请选择考场年度");
                return;
            }
            if($("#epId").val()==""){
                layer.msg("请选择考场名称")
                return;
            }
            if($("#examTime").val()==""){
                layer.msg("请选择考试时间");
                return;
            }
            if($("#placeId").val()==""){
                layer.msg("请选择课室")
                return;
            }
            layer.msg("正在导出中...")
            var url = '/examRoomSeats/seatsExport.do?eyName=';
            $('<form method="post" action="'  + url + $("#eyName").val() + '&epName=' + $("#epName").val() + '&placeName=' + $("#placeName").val() + '">'
                + '<input type="text" name="eyId" value="' + $("#eyId").val() + '"/>'
                + '<input type="text" name="epId" value="' + $("#epId").val() + '"/>'
                + '<input type="text" name="examTime" value="' + $("#examTime").val() + '"/>'
                + '<input type="text" name="examTimeStr" value="' +$("#examTime").select2("data")[0].text + '"/>'
                + '<input type="text" name="placeId" value="' + $("#placeId").val() + '"/>'
                + '</form>').appendTo('body').submit().remove();
        })
    });