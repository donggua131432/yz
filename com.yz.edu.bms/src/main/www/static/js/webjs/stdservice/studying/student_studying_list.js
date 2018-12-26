var myDataTable,leanId,outName,pUnvsName,pPfsnName;
      $(function() {
          _init_select('sex', dictJson.sex);
          _init_select('grade', dictJson.grade);
          _init_select('recruitType', dictJson.recruitType);
          _init_select('pfsnLevel', dictJson.pfsnLevel);
          _init_select('scholarship', dictJson.scholarship);
          _init_select('stdStage',dictJson.stdStage);

          _init_select('empStatus', dictJson.empStatus);
          //是否外校
          _init_select("stdType", dictJson.stdType);

          _simple_ajax_select({
              selectId : "unvsId",
              searchUrl : '/bdUniversity/findAllKeyValue.do',
              sData : {},
              showText : function(item) {
                  return item.unvs_name;
              },
              showId : function(item) {
                  return item.unvs_id;
              },
              placeholder : '--请选择院校--'
          });
          $("#unvsId").append(new Option("", "", false, true));

          $("#unvsId").change(function () {
              $("#pfsnId").removeAttr("disabled");
              init_pfsn_select();
           });
          $("#grade").change(function () {
              $("#pfsnId").removeAttr("disabled");
              init_pfsn_select();
              var grade = $(this).val();
              if(grade.length >4){
                  $("#feeStandard").removeAttr("disabled");
                  _init_select("yearArrears", [
                     {"dictValue" : "-1","dictName" : "有学费未交清"},
                     {"dictValue" : "-2","dictName" : "已交清全部学费"},
                     {"dictValue" : "1","dictName" : "欠第一学年第一学期"},
                     {"dictValue" : "2","dictName" : "不欠第一学年第一学期"},
                     {"dictValue" : "3","dictName" : "欠第一学年第二学期"},
                     {"dictValue" : "4","dictName" : "不欠第一学年第二学期"},
                     {"dictValue" : "5","dictName" : "欠第二学年第三学期"},
                     {"dictValue" : "6","dictName" : "不欠第二学年第三学期"},
                     {"dictValue" : "7","dictName" : "欠第二学年第四学期"},
                     {"dictValue" : "8","dictName" : "不欠第二学年第四学期"}
                  ]);
              }else{
                  $("#feeStandard").val('').trigger("change");
                  $("#feeStandard").prop("disabled", true);
                  _init_select("yearArrears", [
                     {"dictValue" : "-1","dictName" : "有学费未交清"},
                     {"dictValue" : "-2","dictName" : "已交清全部学费"},
                     {"dictValue" : "1","dictName" : "欠Y1第一学年"},
                     {"dictValue" : "2","dictName" : "不欠Y1第一学年"},
                     {"dictValue" : "3","dictName" : "欠Y2第二学年"},
                     {"dictValue" : "4","dictName" : "不欠Y2第二学年"},
                     {"dictValue" : "5","dictName" : "欠Y3第三学年"},
                     {"dictValue" : "6","dictName" : "不欠Y3第三学年"}
                  ]);
              }
           });
           $("#pfsnLevel").change(function () {
              $("#pfsnId").removeAttr("disabled");
              init_pfsn_select();
           });
           $("#pfsnId").append(new Option("", "", false, true));
           $("#pfsnId").select2({
                  placeholder: "--请先选择院校--"
           });

          $("#recruitType").change(function () {
              _init_select({
                  selectId: 'grade',
                  ext1: $(this).val()
              }, dictJson.grade);
              if($(this).val() =='2'){
                  $("#feeStandard").removeAttr("disabled");
                  _init_select("yearArrears", [
                     {"dictValue" : "-1","dictName" : "有学费未交清"},
                     {"dictValue" : "-2","dictName" : "已交清全部学费"},
                     {"dictValue" : "1","dictName" : "欠第一学年第一学期"},
                     {"dictValue" : "2","dictName" : "不欠第一学年第一学期"},
                     {"dictValue" : "3","dictName" : "欠第一学年第二学期"},
                     {"dictValue" : "4","dictName" : "不欠第一学年第二学期"},
                     {"dictValue" : "5","dictName" : "欠第二学年第三学期"},
                     {"dictValue" : "6","dictName" : "不欠第二学年第三学期"},
                     {"dictValue" : "7","dictName" : "欠第二学年第四学期"},
                     {"dictValue" : "8","dictName" : "不欠第二学年第四学期"}
                  ]);
              }else{
                  $("#feeStandard").val('').trigger("change");
                  $("#feeStandard").prop("disabled", true);
                  _init_select("yearArrears", [
                     {"dictValue" : "-1","dictName" : "有学费未交清"},
                     {"dictValue" : "-2","dictName" : "已交清全部学费"},
                     {"dictValue" : "1","dictName" : "欠Y1第一学年"},
                     {"dictValue" : "2","dictName" : "不欠Y1第一学年"},
                     {"dictValue" : "3","dictName" : "欠Y2第二学年"},
                     {"dictValue" : "4","dictName" : "不欠Y2第二学年"},
                     {"dictValue" : "5","dictName" : "欠Y3第三学年"},
                     {"dictValue" : "6","dictName" : "不欠Y3第三学年"}
                  ]);
              }
          });
          _init_select('sg', dictJson.sg); //优惠分组
           _init_select("inclusionStatus",dictJson.inclusionStatus);
          $("#sg").change(function() { //联动
              _init_select({
                  selectId : 'scholarship',
                  ext1 : $(this).val()
              }, dictJson.scholarship);
           });
          //总费是否欠费
          /*_init_select("isArrears", [
              {"dictValue" : "0","dictName" : "否"},
              {"dictValue" : "1","dictName" : "是"}
          ]);*/
          //收费标准
          _init_select("feeStandard" ,[
              {"dictValue" : "0","dictName" : "成教收费标准"},
              {"dictValue" : "1","dictName" : "国开收费标准"}
          ]);

          $("#feeStandard").change(function(){
              if($(this).val() =='0'){
                  _init_select("yearArrears", [
                     {"dictValue" : "-1","dictName" : "有学费未交清"},
                     {"dictValue" : "-2","dictName" : "已交清全部学费"},
                     {"dictValue" : "1","dictName" : "欠Y1第一学年"},
                     {"dictValue" : "2","dictName" : "不欠Y1第一学年"},
                     {"dictValue" : "3","dictName" : "欠Y2第二学年"},
                     {"dictValue" : "4","dictName" : "不欠Y2第二学年"},
                     {"dictValue" : "5","dictName" : "欠Y3第三学年"},
                     {"dictValue" : "6","dictName" : "不欠Y3第三学年"}
                  ]);
              }else{
                  _init_select("yearArrears", [
                      {"dictValue" : "-1","dictName" : "有学费未交清"},
                      {"dictValue" : "-2","dictName" : "已交清全部学费"},
                      {"dictValue" : "1","dictName" : "欠第一学年第一学期"},
                      {"dictValue" : "2","dictName" : "不欠第一学年第一学期"},
                      {"dictValue" : "3","dictName" : "欠第一学年第二学期"},
                      {"dictValue" : "4","dictName" : "不欠第一学年第二学期"},
                      {"dictValue" : "5","dictName" : "欠第二学年第三学期"},
                      {"dictValue" : "6","dictName" : "不欠第二学年第三学期"},
                      {"dictValue" : "7","dictName" : "欠第二学年第四学期"},
                      {"dictValue" : "8","dictName" : "不欠第二学年第四学期"}
                  ]);
              }
          });
          //学年是否欠费(初始化成教)
          _init_select("yearArrears", [
             {"dictValue" : "-1","dictName" : "有学费未交清"},
             {"dictValue" : "-2","dictName" : "已交清全部学费"},
             {"dictValue" : "1","dictName" : "欠Y1第一学年"},
             {"dictValue" : "2","dictName" : "不欠Y1第一学年"},
             {"dictValue" : "3","dictName" : "欠Y2第二学年"},
             {"dictValue" : "4","dictName" : "不欠Y2第二学年"},
             {"dictValue" : "5","dictName" : "欠Y3第三学年"},
             {"dictValue" : "6","dictName" : "不欠Y3第三学年"}
          ]);
          //是否分配辅导老师
          _init_select("isTutor", [
              {"dictValue" : "0","dictName" : "否"},
              {"dictValue" : "1","dictName" : "是"}
          ]);
          //是否填写备注
          _init_select("isRemark", [
              {"dictValue" : "0","dictName" : "否"},
              {"dictValue" : "1","dictName" : "是"}
          ]);


          //招生分校,招生部门
          _init_campus_select("campusId", "dpId", "groupId", '/campus/selectAllList.do', '/dep/selectAllList.do', '/group/selectAllList.do');
          //考试县区
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
          //归属校区
          var url = '/stdEnroll/getHomeCampusInfo.do';
          $.ajax({
              type: "POST",
              dataType : "json", //数据类型
              url: url+'?isStop=0',
              success: function(data){
                   var campusJson = data.body;
                   if(data.code=='00'){
                      _init_select("homeCampusId",campusJson);
                   }
              }
          });

          myDataTable = $('.table-sort').dataTable({
              "processing": true,
              "serverSide": true,
              "dom": 'rtilp',
              "ajax": {
                  url: "/studying/getStudyingListJoinAcc.do",
                  type: "post",
                  data: function (pageData) {
                      var yearArrearsValue=$("#yearArrears").select2('data')[0].text;
                      yearArrearsValue=yearArrearsValue=='请选择'?'':yearArrearsValue;
                      pageData = $.extend({},{start:pageData.start, length:pageData.length}, {yearArrearsValue:yearArrearsValue},$("#searchForm").serializeObject());
                      console.log(pageData);
                      return pageData;
                  }
              },
              "pageLength": 10,
              "pagingType": "full_numbers",
              "ordering": false,
              "searching": false,
              "lengthMenu": [10, 20],
              "createdRow": function (row, data, dataIndex) {
                  $(row).addClass('text-c');
              },
              "language": _my_datatables_language,
              columns: [
                  {"mData": null},
                  {"mData": "stdNo"},
                  {"mData": "schoolRoll"},
                  {"mData": null},
                  {"mData": null},
                  {"mData": null},
                  {"mData": null},
                  {"mData": "tutorName"},
                  {"mData": null},
                  {"mData": null},
                  {"mData": null},
                  {"mData": null}
              ],
              "columnDefs": [
                  {"targets": 0,"render": function (data, type, row, meta) {
                      return '<input type="checkbox" value="' + row.learnId + '" name="learnIds"/>';
                  }},
                  {"targets": 3,"render": function (data, type, row, meta) {
                      var dom =row.stdName;
                      if(row.stdType ==='2'){
                          dom += ' <sup style="color:#f00">外</sup>';
                      }
                      return '<a class="stuName" onclick="toEidt(\''+ row.learnId +'\',\''+ row.stdId +'\')">'+ dom +'</a>';
                  }},
                  {"targets": 4,"render": function (data, type, row, meta) {
                      return _findDict('grade', row.grade);
                  }},
                  {"targets": 5, "render": function (data, type, row, meta) {
                      var text = unvsText(row);
                      return text ? '<a class="stuName" title="查看课表" onclick="make_schedule(\''+row.grade+'\' ,\''+row.pfsnLevel+'\',\''+row.unvsId+'\' ,\''+row.pfsnId+'\',\''+row.unvsName+'\',\''+row.pfsnName+'\'  )">'+text +'</a>': '无';
                  },"class":"text-l"},
                  {"targets": 6,"render": function (data, type, row, meta) {
                      var dom = '';
                      dom += '<p class="tablePay-red">应缴:' + row.feeAmount + '</p>'
                      dom += '<p class="tablePay-normal">滞留:' + row.accAmount + '</p>'
                      return dom;
                  },"class":"text-l no-warp"},
                  {"targets": 8,"render": function (data, type, row, meta) {
                      return '<a href="javascript:;" class="tableBtn normal" title="查看缴费明细" onclick="showPaymentDetail(\'' + row.learnId + '\', \'' + row.stdId + '\');">查看缴费明细</a>';
                  },"class":"no-warp"},
                  {"targets": 9,"render": function (data, type, row, meta) {
                      return '<a href="javascript:;" class="tableBtn normal" title="查看联系方式" onclick="showContacts(\'' + row.learnId + '\');">查看联系方式</a>';
                  },"class":"no-warp"},
                  {"targets": 10,"render": function (data, type, row, meta) {
                      return '<a href="javascript:;" class="tableBtn normal" title="查看服务记录" onclick="showServices(\'' + row.learnId + '\');">查看服务记录</a>';
                  },"class":"no-warp"},
                  {"targets": 11,"render": function (data, type, row, meta) {
                      var dom='<a href="javascript:;" class="tableBtn normal" title="查看业务备注" onclick="showRemark(\'' + row.learnId + '\');">查看业务备注</a>';
                      return dom;
                  }}
              ]
          });
      });

      function showPaymentDetail(learnId, stdId) {
          var url = '/studying/showPaymentDetail.do' + '?learnId=' + learnId + '&stdId=' + stdId;
          layer_show('缴费明细', url, null, null, function() {
              //myDataTable.fnDraw(true);
          });
      }

      function showContacts(learnId) {
          var url = '/studying/showContacts.do' + '?learnId=' + learnId;
          layer_show('联系方式', url, 1000, 550, function() {
              //myDataTable.fnDraw(true);
          });
      }

      function showServices(learnId) {
          //layer.msg('功能开发中');
          var url = '/studying/showServices.do' + '?learnId=' + learnId;
          layer_show('服务记录', url, 1000, null, function() {
              //myDataTable.fnDraw(true);
          });
      }

      function showRemark(learnId) {
          var url = '/studying/changeRemark.do' + '?learnId=' + learnId;
          layer_show('业务备注', url, 1000, null, function() {
              //myDataTable.fnDraw(true);
          });
      }


      /*查看学员详细信息*/
      function toEidt(learnId, stdId) {
          var url = '/studentBase/toEdit.do' + '?learnId=' + learnId + '&stdId=' + stdId + '&t=MODI';
          layer_show('学员信息', url, null, null, function() {
              //myDataTable.fnDraw(true);
          });
      }

      function _search() {
          myDataTable.fnDraw(true);
      }

      function changeRemark(remark) {
          var temp = $(remark).attr("isCompleted") == '1' ? '0' : '1';
          $.ajax({
              type : 'POST',
              url : '/studying/changeRemark.do',
              data : {
                  lrId : $(remark).attr("id"),
                  isCompleted : temp
              },
              dataType : 'json',
              success : function(data) {
                  if (_GLOBAL_SUCCESS == data.code) {
                      $(remark).attr("isCompleted", temp);
                      if (temp == '1') {
                          $(remark).attr("class", "label label-success radius ml-5");
                      } else {
                          $(remark).attr("class", "label label-default radius ml-5");
                      }
                  }
              }
          });
      }

      function register_import() {
          var url = '/stuRegister/toExcelImport.do';
          layer_show('高校学号导入', url, null, 510, function() {
              myDataTable.fnDraw(false);
          });
      }

      //退学申请
      function student_out() {
          leanId=$("table input[name=learnIds]:checked:first").val();
          outName=$("table input[name=learnIds]:checked:first").parent().siblings('td').find("a.stuName").text();
          //console.log(leanId);
          var url = '/studentOut/edit.do' + '?exType=ADD';
          layer_show('添加退学学员', url, null, 510, function(){});
      }


      //查看课程安排
      function show_classplan() {
          var url = '/studying/showClassPlan.do';
          layer_show('查看课程安排', url, null, null, function() {

          },true);
      }


      /*链接到课表*/
      function make_schedule(grade,pfsnLevel,unvsId,pfsnId,unvsName,pfsnName) {
          pUnvsName=unvsName;
          pPfsnName=pfsnName;
          var courseType="XK", semester="1";
          var url = '/classPlan/makeScheduleShow.do'+ '?courseType=' + courseType + '&grade=' + grade + '&pfsnLevel='+pfsnLevel+'&unvsId='+unvsId+'&pfsnId='+pfsnId+'&semester='+semester;
          layer_show('生成课表', url, null, 510, function() {
              myDataTable.fnDraw(false);
          }, true);
      }

      function unvsText(data) {
          var pfsnName = data.pfsnName,unvsName = data.unvsName,recruitType = data.recruitType,
              pfsnCode = data.pfsnCode, pfsnLevel = data.pfsnLevel,text = '';
          if(recruitType) {
              if(_findDict("recruitType", recruitType).indexOf("成人")!=-1){
                  text += "[成教]";
              }else {
                  text += "[国开]";
              }
          }
          if(unvsName) text += unvsName + "</br>";
          if(pfsnLevel) {
              if(_findDict("pfsnLevel", pfsnLevel).indexOf("高中")!=-1){
                  text += "[专科]";
              }else {
                  text += "[本科]";
              }
          }
          if(pfsnName) text += pfsnName;
          if(pfsnCode) text += "(" + pfsnCode + ")";
         return text;
      }

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