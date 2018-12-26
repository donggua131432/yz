 var myDataTable,columns,yGradeArray;
        var gradeArray=dictJson.grade;
        $(function() {
            _init_select('scholarship', dictJson.scholarship);
            //初始化专业大类下拉框
            _init_select("pfsnCata", dictJson.pfsnCata);
            //初始化年度下拉框
            _init_select("year", dictJson.year);
            //初始化年级下拉框
            _init_select("grade", gradeArray);
            //初始化专业层次下拉框
            _init_select("pfsnLevel", dictJson.pfsnLevel);
            _init_select('sg', dictJson.sg); //优惠分组
            $("#sg").change(function() { //联动
                _init_select({
                    selectId : 'scholarship',
                    ext1 : $(this).val()
                }, dictJson.scholarship);
            });
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
            _simple_ajax_select({
                selectId: "taId",
                searchUrl: '/testArea/findAllTestArea.do',
                sData: {},
                showText: function (item) {
                    return item.taName;
                },
                showId: function (item) {
                    return item.taId;
                },
                placeholder: '--请选择--'
            });

            _simple_ajax_select({
                selectId: "recruitCampusId",
                searchUrl: '/campus/selectList.do',
                sData: {},
                showText: function (item) {
                    return item.campusName;
                },
                showId: function (item) {
                    return item.campusId;
                },
                placeholder: '--请选择--'
            });

            _simple_ajax_select({
                selectId: "recruitDpId",
                searchUrl: '/dep/selectList.do',
                sData: {},
                showText: function (item) {
                    return item.dpName;
                },
                showId: function (item) {
                    return item.dpId;
                },
                placeholder: '--请选择--'
            });

            $("#unvsId,#taId,#recruitCampusId,#recruitDpId").append(new Option("", "", false, true));



//            根据招生年度过滤年级下拉
            $("#year").on('change',function () {
                var year=parseInt($(this).val());
                if(!year){
                    _init_select("grade", gradeArray);
                    return
                }
                yGradeArray=[];
                gradeArray.forEach(function (e,i) {
                    if(e.dictName.indexOf(year+1+'级')!=-1||e.dictName.indexOf(year+'0')!=-1){
                        yGradeArray.push(e)
                    }
                })
                _init_select("grade", yGradeArray);
            });
            
            $("#checkAll").click(function() { 
                var checkFlag = $(this).prop("checked"); 
                $("input:checkbox[name=stdStage]").each(function() { 
                    $(this).prop("checked", checkFlag); 
                }); 
            });

            $("#checkAllCampus").click(function() {
                var checkFlag = $(this).prop("checked");
                $("input:checkbox[name=stdCampus]").each(function() {
                    $(this).prop("checked", checkFlag);
                });
            });
            
        });

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

 function exportXB(){
     if($("#year").val() == '' && $("#grade").val() == ''){
         layer.msg('【招生年度】和【年级】必须至少指定一个查询条件！', {
             icon : 1,
             time : 2000
         });
         return;
     }
     $("#export-form").attr("action","/dataExport/exportStudentDataXB.do");
     $("#export-form").submit();
     layer.msg("正在导出文件，请勿再次点击");
 }

 function exportGK(){
     if($("#year").val() == '' && $("#grade").val() == ''){
         layer.msg('【招生年度】和【年级】必须至少指定一个查询条件！', {
             icon : 1,
             time : 2000
         });
         return;
     }
     $("#export-form").attr("action","/dataExport/exportStudentDataGK.do");
     $("#export-form").submit();
     layer.msg("正在导出文件，请勿再次点击");
 }
