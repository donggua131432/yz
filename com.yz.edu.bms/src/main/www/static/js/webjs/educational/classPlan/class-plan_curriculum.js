var infos={};
$(function() {
			    var initcourseType = varInitdata.courseType||'';
			    var initgrade = varInitdata.grade||'';
			    var initpfsnLevel = varInitdata.pfsnLevel||'';
			    var initunvsId = varInitdata.unvsId||'';
			    var initpfsnId = varInitdata.pfsnId||'';
			    var initsemester = varInitdata.semester||'';


			    //初始化课程类型
				_init_select("courseType",dictJson.courseType,'XK');
			    //初始化年级
				_init_select("grade",dictJson.grade,initgrade);
			    //初始化专业层次
				_init_select("pfsnLevel",dictJson.pfsnLevel,initpfsnLevel);
			    
				
			   //初始化年度
				_init_select("year",dictJson.year);
				 //初始化学期
				_init_select("semester",dictJson.semester,initsemester);
				 //初始是否包含学习
			    _init_select("isSelfStudy",[
			  	 {
			  	 			"dictValue":"1","dictName":"是"
			  	 		},
			  	 {
			  	 			"dictValue":"0","dictName":"否"
			  	 		}
			  	 	]);
				 
			    $("#pfsnId").select2({
                    placeholder : "--请先选择院校--"
                });
			    
			    $("#pfsnLevel").change(function() {
                    init_pfsn_select("unvsId", "pfsnId");
                });
			    
			    $("#grade").change(function() {
                    init_pfsn_select("unvsId", "pfsnId");
                });
			    
			    $('select').select2({
                    placeholder : "--请选择--",
                    allowClear : true,
                    width : "59%"
                });
			    
			    initWishSelects({
                    unvsSelectId : 'unvsId',
                    pfsnSelectId : 'pfsnId',
                    unvsSearchUrl : '/baseinfo/sUnvs.do',
                    pfsnSearchUrl : '/course/sPfsn.do',
                });
			    
			    _simple_ajax_select({
                    selectId : "campusId",
                    searchUrl : '/classPlan/findCampusByName.do',
                    sData : {},
                    showText : function(item) {
                        return item.campusName;
                    },
                    showId : function(item) {
                        return item.campusId;
                    },
                    placeholder : '请选择校区'
                });
			    $("#campusId").append(new Option("", "", false, true)); 


            	
			    //初始化数据
			    initData(initcourseType,initgrade,initpfsnLevel,initunvsId,initpfsnId,initsemester);

//			    设置院校专业初始值
                if(window.parent.pUnvsName){
                    $("#unvsId").append(new Option(window.parent.pUnvsName, initunvsId, false, true)).trigger("change");
                }
                if(window.parent.pPfsnName){
                    $("#pfsnId").append(new Option(window.parent.pPfsnName, initpfsnId, false, true)).trigger("change");
                }


			    $("#form-subject").validate({
					rules : {
						courseType : {
							required : true
						},
						grade : {
							required : true
						},
						unvsId : {
							required : true
						},
						semester : {
							required : true
						}
					},
					messages : {
					},
					onkeyup : false,
					focusCleanup : true,
					success : "valid",
					submitHandler : function(form) {
                        var index = layer.load(2, {
                            shade: [0.1,'#fff'] //0.1透明度的白色背景
                        });
						$(form).ajaxSubmit({
							type : "post", //提交方式  
							dataType : "json", //数据类型  
							url : '/course/timetable.do', //请求url  
							success : function(data) { //提交成功的回调函数  
								if(data.code == _GLOBAL_SUCCESS){
                                    infos=data.body;
                                    setSubject(data.body);
                                    layer.close(index)
								}
							}
						})
					}
				});
			    
			});
			
			function resetSubject(){
				$("#subTitle").text('');
				
				$("#classTime").text('');
				$("#stdCount").text('');
				$("#pfsnInfo").text('');
				$("#subGrade").text('');
				$("#subjects").html('');
				$("#cpType").text('');
				$("#classes").html('');
				$("#subDetailBody").html('');
				$("#subCourseBody").html('');
			}
			
			function setSubject(sub){
				
				if(null == sub){
					resetSubject()
					return ;
				}
				//上课年度+学期（春季为第一学期、秋季为第二学期）+课程表+院校+年级+层次+专业
				var tempValue="";
				var pfsnLevel = _findDict('pfsnLevel', sub.pfsnLevel);
				if(pfsnLevel)
					pfsnLevel= pfsnLevel.indexOf("高中")!=-1?"[专科]":"[本科]" 
                var semester=$("#semester").find("option:selected").text();	
                if($("#courseType").val()=='FD'){
                	tempValue+=sub.year+"年"+"考前辅导课课表"+sub.unvsName+"-"+ sub.grade + "级";    
                	if(pfsnLevel){
                		tempValue+="-"+pfsnLevel; 
                	}
                	if(sub.pfsnName){
                		tempValue+="-"+ sub.pfsnName; 
                	}
                      
                }else{
                	tempValue+=sub.year+"年"+semester+"课程表"+sub.unvsName+"-"+ sub.grade + "级";         		 
                	if(pfsnLevel){
                		tempValue+="-"+pfsnLevel; 
                	}
                	if(sub.pfsnName){
                		tempValue+="-"+ sub.pfsnName; 
                	}  
                    
                }
				
				$("#subTitle").text(tempValue);

				if(sub.startDate != null && sub.startDate != ''){
					$("#classTime").text(sub.startDate + "  --  "  + sub.endDate);
				}
				
				$("#stdCount").text(sub.stdCount);
				if(sub.pfsnName){
					var pfsnInfo = '[' + _findDict('pfsnLevel', sub.pfsnLevel) + ']' + '-[' + _findDict('grade', sub.grade) + ']';
					 pfsnInfo += sub.pfsnName + '(' + sub.pfsnCode + ')';
					$("#pfsnInfo").text(pfsnInfo);
				}
				 
				$("#subGrade").text(sub.grade+"级");
				
				
				
				//====================================================
				var subCourse = '';
				if(null != sub.courses){
					var courses = sub.courses;
					for (var i = 0; i < courses.length; i++) {
						var cour = courses[i];
						subCourse += '<tr>';
						subCourse += '<td>'+cour.courseName+'</td>';
						subCourse += '<td>'+_findDict("cpType",cour.cpType)+'</td>';
						subCourse += '<td>'+(cour.campusName||'')+'</td>';
						if(cour.cpType=='4'){
							subCourse += '<td>下载“青书学堂”手机APP，使用账号、密码登录进行上课学习</td>';
						}else{
							subCourse += '<td>'+(cour.address||'')+'</td>';
						}
						
						subCourse += '</tr>';
					}
				}
				$("#subCourseBody").html(subCourse);
				//================================================
				
				var subDetail = '';
				if(null != sub.courseInfos){
					for (var i = 0; i < sub.courseInfos.length; i++) {
						var cours = sub.courseInfos[i];
						subDetail += '<tr>';
						subDetail += '<td style="text-align: left;">'+cours.date+'</td>';
						subDetail += '<td>'+cours.week+'</td>';
						subDetail += '<td>';
						for (var j = 0; j < cours.morning.length; j++) {
							var mo = cours.morning[j];
							subDetail += mo+"  &nbsp;";
						}
						subDetail += '</td>';
						subDetail += '<td>';
						for (var k = 0; k < cours.afternoon.length; k++) {
							var mo = cours.afternoon[k];
							subDetail += mo+"  &nbsp;";
						}
						subDetail += '</td>';
						subDetail += '<td>';
						for (var n = 0; n < cours.night.length; n++) {
							var mo = cours.night[n];
							subDetail += mo+"  &nbsp;";
						}
						subDetail += '</td>';
						subDetail += '<tr>';
					}
				}
				$("#subDetailBody").html(subDetail);
				
				
			}
			
			
			function init_pfsn_select(unvsSelectId, pfsnSelectId) {
                _simple_ajax_select({
                    selectId : pfsnSelectId,
                    searchUrl : '/course/sPfsn.do',
                    sData : {
                        sId : function() {
                            return $("#" + unvsSelectId).val();
                        },
                        ext1 : function() {
                            return $("#pfsnLevel").val();
                        },
                        ext2 : function() {
                            return $("#grade").val();
                        }
                    },
                    showText : function(item) {
                        var text = '[' + _findDict('pfsnLevel', item.pfsnLevel) + ']' + '-[' + _findDict('year', item.year) + ']';
                        text += item.pfsnName + '(' + item.pfsnCode + ')';
                        return text;
                    },
                    showId : function(item) {
                        return item.pfsnId;
                    },
                    placeholder : '--请选择专业--'
                });
                
            }
			
			function initWishSelects(config) {
                _simple_ajax_select({
                    selectId : config.unvsSelectId,
                    searchUrl : config.unvsSearchUrl,
                    sData : {
                    },
                    showText : function(item) {
                        var text = '[' + _findDict('recruitType', item.recruitType) + ']';
                        text += item.unvsName + '(' + item.unvsCode + ')';
                        return text;
                    },
                    showId : function(item) {
                        return item.unvsId;
                    },
                    placeholder : '--请选择院校--',
                    width : "59%"
                });

                $("#" + config.unvsSelectId).change(function() {
                    $("#" + config.pfsnSelectId).removeAttr("disabled");
                    init_pfsn_select(config.unvsSelectId, config.pfsnSelectId);
                });

            }
			
			function initData(courseType,grade,pfsnLevel,unvsId,pfsnId,semester){
            	if(courseType!=''&&grade!=''&&pfsnLevel!=''&&unvsId!=''&&pfsnId!=''&&semester!=''){
            		$("#courseType").val(courseType);
            		$("#grade").val(grade);
            		$("#pfsnLevel").val(pfsnLevel);
            		$("#unvsId").val(unvsId);
            		$("#pfsnId").val(pfsnId);
            		$("#semester").val(semester);
            		
            		$.ajax({
                        type: 'POST',
                        url: '/course/timetable.do',
                        data: {
                        	courseType:courseType,
                        	grade:grade,
                        	pfsnLevel:pfsnLevel,
                        	unvsId:unvsId,
                        	pfsnId:pfsnId,
                        	semester:semester
                        },
                        dataType: 'json',
                        success: function (data) {
                        	if(data.code == _GLOBAL_SUCCESS){
								setSubject(data.body);
							}
                        }
                	});
            		
            	}
            	
			}
			function submit(){
				$("#form-subject").submit();
                downloadPDFFlag=true;
                $(".pdfContentDiv").html('')
			}
					
			 /*课程表-Excel导出*/
            function excel_export() {
               $("#dlink").attr("href","");
               HtmlExportToExcel('PanelExcel');	               
            }
			 
            function HtmlExportToExcel(tableid) {
	            $(".s_close").remove();
	            var filename = $('#subTitle').text();
	            if (getExplorer() == 'ie' || getExplorer() == undefined) {
	                HtmlExportToExcelForIE(tableid, filename);
	            }
	            else {
	                HtmlExportToExcelForEntire(tableid, filename)
	            }
	        }
			 
			//IE浏览器导出Excel
	        function HtmlExportToExcelForIE(tableid, filename) {
	            try {
	                var winname = window.open('', '_blank', 'top=10000');
	                var strHTML = document.getElementById(tableid).innerHTML;

	                winname.document.open('application/vnd.ms-excel', 'export excel');
	                winname.document.writeln(strHTML);
	                winname.document.execCommand('saveas', '', filename + '.xls');
	                winname.close();

	            } catch (e) {
	                alert(e.description);
	            }finally {
                    idTmr = window.setInterval("Cleanup();", 1);
                }
	        }
	       function Cleanup() {
	            window.clearInterval(idTmr);
	            CollectGarbage();
	       }
	      //非IE浏览器导出Excel
	        var HtmlExportToExcelForEntire = (function() {
	            var uri = 'data:application/vnd.ms-excel;base64,',
	        template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
	        base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
	        format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
	            return function(table, name) {
	                if (!table.nodeType) { table = document.getElementById(table); }
	                var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }
	                document.getElementById("dlink").href = uri + base64(format(template, ctx));
	                document.getElementById("dlink").download = name + ".xls";
	                document.getElementById("dlink").click();
	            }
	        })()
	        function getExplorer() {
	            var explorer = window.navigator.userAgent;
	            //ie 
	            if (explorer.indexOf("MSIE") >= 0) {
	                return 'ie';
	            }
	            //firefox 
	            else if (explorer.indexOf("Firefox") >= 0) {
	                return 'Firefox';
	            }
	            //Chrome
	            else if (explorer.indexOf("Chrome") >= 0) {
	                return 'Chrome';
	            }
	            //Opera
	            else if (explorer.indexOf("Opera") >= 0) {
	                return 'Opera';
	            }
	            //Safari
	            else if (explorer.indexOf("Safari") >= 0) {
	                return 'Safari';
	            }
	        } 


//	        PDF导出
            var downloadPDFFlag=true;
          function pdf_export() {
//        下载文件名
              var fileName=$('#subTitle').text();
//        体验优化
              $("#downloadPDF").html('<i class="iconfont icon-daochu"></i> 导出中...');
              var index = layer.load(2, {
                  shade: [0.1,'#fff'] //0.1透明度的白色背景
              });
              $(".pdfItem").css('display','block');

              var indexCourses=1;
              var indexCourseInfoss=1;

              if(downloadPDFFlag){
//            拼串，渲染页面
                  var contentCourses = '';
                  var contentCourseInfos = '';

                  //    引入数据。。。。。,渲染页面
//            获取页总数，即每页47条数据
                  var allPageNCourses=Math.ceil(infos.courses/47);
                  var allPageNCourseInfos=Math.ceil(infos.courseInfos/47);
                  var allPageN=allPageNCourses+allPageNCourseInfos;

//            课程拼串，渲染页面
				  var subTitle=$("#subTitle").text();
				  var classTime=$("#classTime").text();
				  var stdCount=$("#stdCount").text();
				  var pfsnInfo=$("#pfsnInfo").text();
				  var subGrade=$("#subGrade").text();

				  var contentTop='<div><table> <thead>' +
                      '<tr> <th colspan="4">'+subTitle+'</th>' +
                      '</tr> </thead> <tbody>'+
					  '<tr><td>上课时间</td><td>'+classTime+'</td><td>上课人数</td><td>'+stdCount+'</td></tr>'+
					  '<tr><td>开课专业</td><td>'+pfsnInfo+'</td><td>上课年级</td><td>'+subGrade+'</td></tr>'+
					  '</tbody></table></div><br>';

                  var contentCourses='<div class="pdfItem text-l">'+contentTop+
                      '<table> <thead>' +
                      '<tr> <th width="250">课程</th> <th width="250">上课方式</th> <th width="250">校区</th> <th>地址</th>' +
                      '</tr> </thead> <tbody>';

                  //            课程拼串，渲染页面
                  var contentCourseInfos='<div class="pdfItem text-l">'+
                      '<table> <thead>' +
                      '<tr> <th width="250">日期</th> <th width="250">星期</th> <th width="400">上午(9:00-11:00)</th> <th width="400">下午(14:00-16:00)</th>' +
                      '<th>晚上(19:00-21:00)</th></tr> </thead> <tbody>';

                  var textCourses='';
                  var textCourseInfos='';

                  infos.courses.forEach(function (e,i) {
                      var address=e.address||'无';
                      var campusName=e.campusName||'无';
                      var courseName=e.courseName||'无';
                      var cpType=_findDict("cpType",e.cpType)||'无';

//                最后一条数据，结束
                      if(i==infos.courses.length-1){
//                    拼接座位数据
                          var textCourses2='<tr> ' +
                              '<td>'+courseName+'</td> ' +
                              '<td>'+cpType+'</td> ' +
                              '<td>'+campusName+'</td> ' +
                              '<td>'+address+'</td> ' +
                              '</tr>';
                          textCourses+=textCourses2

                          contentCourses+=textCourses;
                          contentCourses+='</tbody> </table></div>';
                          return;
                      }

                      if(i==(47*indexCourses)-1){
                          var textCourses2='<tr> ' +
                              '<td>'+courseName+'</td> ' +
                              '<td>'+cpType+'</td> ' +
                              '<td>'+campusName+'</td> ' +
                              '<td>'+address+'</td> ' +
                              '</tr>';
                          textCourses+=textCourses2

                          contentCourses+=textCourses;
                          contentCourses+='</tbody> </table></div>'
                          indexCourses++;
                          contentCourses+='<div class="pdfItem text-l"><table> <thead>' +
                              '<tr> <th width="250">课程</th> <th width="250">上课方式</th> <th width="250">校区</th> <th>地址</th>' +
                              '</tr> </thead> <tbody>';
                          textCourses=''
                      }else {
//                    拼接座位数据
                          var textCourses2='<tr> ' +
                              '<td>'+courseName+'</td> ' +
                              '<td>'+cpType+'</td> ' +
                              '<td>'+campusName+'</td> ' +
                              '<td>'+address+'</td> ' +
                              '</tr>';
                          textCourses+=textCourses2
                      }
                  });
				  infos.courseInfos.forEach(function (e,i) {
                      var date=e.date||'无';
                      var week=e.week||'无';
                      var morningArr=e.morning||[];
                      var morning='';
                      var afternoonArr=e.afternoon||[];
                      var afternoon='';
                      var nightArr=e.night||[];
                      var night='';

//                最后一条数据，结束
                      if(i==infos.courseInfos.length-1){
//                    拼接座位数据
                          morningArr.forEach(function (e,i) {
                              morning+=e+' '
                          });
                          afternoonArr.forEach(function (e,i) {
                              afternoon+=e+' '
                          });
                          nightArr.forEach(function (e,i) {
                              night+=e+' '
                          });
                          var textCourseInfos2='<tr> ' +
                              '<td>'+date+'</td> ' +
                              '<td>'+week+'</td> ' +
                              '<td>'+morning+'</td> ' +
                              '<td>'+afternoon+'</td> ' +
                              '<td>'+night+'</td> ' +
                              '</tr>';
                          textCourseInfos+=textCourseInfos2

                          contentCourseInfos+=textCourseInfos;
                          contentCourseInfos+='</tbody> </table></div>';
                          return;
                      }

                      if(i==(40*indexCourseInfoss)-1){
                          morningArr.forEach(function (e,i) {
                              morning+=e+' '
                          });
                          afternoonArr.forEach(function (e,i) {
                              afternoon+=e+' '
                          });
                          nightArr.forEach(function (e,i) {
                              night+=e+' '
                          });
                          var textCourseInfos2='<tr> ' +
                              '<td>'+date+'</td> ' +
                              '<td>'+week+'</td> ' +
                              '<td>'+morning+'</td> ' +
                              '<td>'+afternoon+'</td> ' +
                              '<td>'+night+'</td> ' +
                              '</tr>';

                          textCourseInfos+=textCourseInfos2;

                          contentCourseInfos+=textCourseInfos;
                          contentCourseInfos+='</tbody> </table></div>'
                          indexCourseInfoss++;
                          contentCourseInfos+='<div class="pdfItem text-l"><table> <thead>' +
                              '<tr> <th width="250">日期</th> <th width="250">星期</th> <th width="400">上午(9:00-11:00)</th> <th width="400">下午(14:00-16:00)</th>' +
                              '<th>晚上(19:00-21:00)</th></tr> </thead> <tbody>';
                          textCourseInfos=''
                      }else {
//                    拼接座位数据
                          morningArr.forEach(function (e,i) {
                              morning+=e+' '
                          });
                          afternoonArr.forEach(function (e,i) {
                              afternoon+=e+' '
                          });
                          nightArr.forEach(function (e,i) {
                              night+=e+' '
                          });
                          var textCourseInfos2='<tr> ' +
                              '<td>'+date+'</td> ' +
                              '<td>'+week+'</td> ' +
                              '<td>'+morning+'</td> ' +
                              '<td>'+afternoon+'</td> ' +
                              '<td>'+night+'</td> ' +
                              '</tr>';

                          textCourseInfos+=textCourseInfos2
                      }
                  });
                  $('.pdfContentDiv').html(contentCourses+contentCourseInfos);
                  downloadPDFFlag=false;
              }

              // return;
              // var height = ($('tr').length/50)*3100;
              $('.pdfItem').width(2080).height(3100).tableExport({type:'pdf',
                  fileName:fileName,
                  jspdf: {orientation: 'p',
                      margins: {left:10, top:10},
                      autotable:false
                  }
              },function () {
                  $('.pdfItem').css("display","none");
                  $("#downloadPDF").html('<i class="iconfont icon-daochu"></i> 导出PDF');
                  layer.close(index)
              });
          }