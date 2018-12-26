 var myDataTable,columns,yGradeArray;
        var gradeArray=dictJson.grade;
//        console.log(gradeArray);
//        console.log($("#year").val());
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

            addTr();
            myDataTable = $('.table-sort').dataTable({
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/studentStat/studentStatList.do",
                    type: "post",
                    data: function (pageData) {
                        return searchData(pageData);
                    }
                },
                "pageLength": 10,
                "pagingType": "full_numbers",
                "ordering": false,
                "searching": false,
                "lengthMenu": [10,100,1000],
                "createdRow": function (row, data, dataIndex) {
                    $(row).addClass('text-c');
                },
                "language": _my_datatables_language,
                "columns":columns
            });
            studentStatTotal();

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
            
            
           
            
            
        });

        function _search() {
            var statGroupArray = new Array(), stdStageArray = new Array();
            $.each($("input[name='statGroup']:checked"),function(){
                statGroupArray.push($(this).val());
            });

            $.each($("input[name='stdStage']:checked"),function(){
                stdStageArray.push($(this).val());
            });

            if($("#year").val() == '' && $("#grade").val() == ''){
                layer.msg('【招生年度】和【年级】必须至少指定一个查询条件！', {
                    icon : 1,
                    time : 2000
                });
                return false;
            }
            if(stdStageArray == '' || stdStageArray == null){
                layer.msg('请选择学员状态！', {
                    icon : 1,
                    time : 2000
                });
                return false;
            }
            if(statGroupArray == '' || statGroupArray == null){
                layer.msg('请选分组条件！', {
                    icon : 1,
                    time : 2000
                });
                return false;
            }

            myDataTable.fnDestroy(true);
            addTr();
            myDataTable = $('.table-sort').dataTable({
                "processing": true,
                "serverSide": true,
                "dom": 'rtilp',
                "ajax": {
                    url: "/studentStat/studentStatList.do",
                    type: "post",
                    data: function (pageData) {
                        return searchData(pageData);
                    }
                },
                "pageLength": 10,
                "pagingType": "full_numbers",
                "ordering": false,
                "searching": false,
                "lengthMenu": [10,100,1000],
                "createdRow": function (row, data, dataIndex) {
                    $(row).addClass('text-c');
                },
                "language": _my_datatables_language,
                "columns":columns
            });
            studentStatTotal();
        }

        function studentStatTotal() {
            var statGroupArray = new Array(), stdStageArray = new Array();
            $.each($("input[name='statGroup']:checked"),function(){
                statGroupArray.push($(this).val());
            });

            $.each($("input[name='stdStage']:checked"),function(){
                stdStageArray.push($(this).val());
            });

            var data = {
                year : $("#year").val() ? $("#year").val() : '',
                grade : $("#grade").val() ? $("#grade").val() : '',
                unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
                pfsnCata : $("#pfsnCata").val() ? $("#pfsnCata").val() : '',
                pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
                taId : $("#taId").val() ? $("#taId").val() : '',
                recruitCampusId : $("#recruitCampusId").val() ? $("#recruitCampusId").val() : '',
                recruitDpId : $("#recruitDpId").val() ? $("#recruitDpId").val() : '',
                recruit : $("#recruit").val() ? $("#recruit").val() : '',
                scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
                createTime : $("#createTime").val() ? $("#createTime").val() : '',
                createTime1 : $("#createTime1").val() ? $("#createTime1").val() : '',
               	payTime : $("#payTime").val() ? $("#payTime").val() : '',
               	payTime1 : $("#payTime1").val() ? $("#payTime1").val() : '',	
               	regTime : $("#regTime").val() ? $("#regTime").val() : '',
               	regTime1 : $("#regTime1").val() ? $("#regTime1").val() : '',			
                stdStage :stdStageArray ? stdStageArray.join(','):'',
                statGroup : statGroupArray ? statGroupArray.join(','):''
            };

            $.ajax({
                type: 'POST',
                url: "/studentStat/studentStatTotal.do",
                data: data,
                dataType : 'json',
                success: function (data) {
                    $("#totalSpan").text(data['body']['countTotal']);
                }
            });
        }

        function searchData(pageData) {
            var statGroupArray = new Array(), stdStageArray = new Array();
            $.each($("input[name='statGroup']:checked"),function(){
                statGroupArray.push($(this).val());
            });

            $.each($("input[name='stdStage']:checked"),function(){
                stdStageArray.push($(this).val());
            });

            return {
                year : $("#year").val() ? $("#year").val() : '',
                grade : $("#grade").val() ? $("#grade").val() : '',
                unvsId : $("#unvsId").val() ? $("#unvsId").val() : '',
                pfsnCata : $("#pfsnCata").val() ? $("#pfsnCata").val() : '',
                pfsnLevel : $("#pfsnLevel").val() ? $("#pfsnLevel").val() : '',
                taId : $("#taId").val() ? $("#taId").val() : '',
                recruitCampusId : $("#recruitCampusId").val() ? $("#recruitCampusId").val() : '',
                recruitDpId : $("#recruitDpId").val() ? $("#recruitDpId").val() : '',
                recruit : $("#recruit").val() ? $("#recruit").val() : '',
                scholarship : $("#scholarship").val() ? $("#scholarship").val() : '',
                createTime : $("#createTime").val() ? $("#createTime").val() : '',
                createTime1 : $("#createTime1").val() ? $("#createTime1").val() : '',
                payTime : $("#payTime").val() ? $("#payTime").val() : '',
                payTime1 : $("#payTime1").val() ? $("#payTime1").val() : '',	
                regTime : $("#regTime").val() ? $("#regTime").val() : '',
                regTime1 : $("#regTime1").val() ? $("#regTime1").val() : '',			
                stdStage :stdStageArray ? stdStageArray.join(','):'',
                statGroup : statGroupArray ? statGroupArray.join(','):'',
                start : pageData.start,
                length : pageData.length
            };
        }

        function addTr() {
            $("#statTable").empty();
            $("#statTable").html("<table class='table table-border table-bordered table-hover table-bg table-sort'><thead><tr id='statTableTr' class='text-c'></tr></thead><tbody></tbody></table>");
            var tr = $("#statTableTr");
            columns = new Array();
            var statGroupArray = new Array();
            $.each($("input[name='statGroup']:checked"),function(){
                statGroupArray.push($(this).val());
            });
            for(var i=0; i<statGroupArray.length; i++){
                if(statGroupArray[i] == 'c.recruit_campus_id'){
                    tr.append("<th width='90'>校区</th>")
                    var columnData = {};
                    columnData['mData']='campus_name';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'a.unvs_id'){
                    tr.append("<th>院校</th>")
                    var columnData = {};
                    columnData['mData']='unvs_name';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'a.ta_id'){
                    tr.append("<th>考试县区</th>")
                    var columnData = {};
                    columnData['mData']='ta_name';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'b.pfsn_cata'){
                    tr.append("<th>专业大类</th>")
                    var columnData = {};
                    columnData['mData']='pfsn_cata';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'b.pfsn_level'){
                    tr.append("<th>专业层次</th>")
                    var columnData = {};
                    columnData['mData']='pfsn_level';
                    columns.push(columnData);
                }
                
             
                if(statGroupArray[i] == 'b.pfsn_name'){
                    tr.append("<th>专业</th>")
                    var columnData = {};
                    columnData['mData']='pfsn_name';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'c.recruit'){
                    tr.append("<th>招生老师</th>")
                    var columnData = {};
                    columnData['mData']='emp_name';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'c.recruit_dp_id'){
                    tr.append("<th>招生部门</th>")
                    var columnData = {};
                    columnData['mData']='dp_name';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'a.std_stage'){
                    tr.append("<th>学员状态</th>")
                    var columnData = {};
                    columnData['mData']='std_stage';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'b.year'){
                    tr.append("<th>年</th>")
                    var columnData = {};
                    columnData['mData']='year';
                    columns.push(columnData);
                }

                if(statGroupArray[i] == 'b.grade'){
                    tr.append("<th>年级</th>")
                    var columnData = {};
                    columnData['mData']='grade';
                    columns.push(columnData);
                }
            }

            $("#statTableTr").append("<th>人数</th>");
            if ($("#year").val() == '2019' || $("#grade").val() == '2019' || $("#grade").val() == '201803' || $("#grade").val() == '201809') {
                $("#statTableTr").append("<th>标准人数</th>");
            }
            var columnData1 = {};
            columnData1['mData']='count';
            columns.push(columnData1);
            if ($("#year").val() == '2019' || $("#grade").val() == '2019' || $("#grade").val() == '201803' || $("#grade").val() == '201809') {
                var columnData2 = {};
                columnData2['mData'] = 'audit_count';
                columns.push(columnData2);
            }
        }