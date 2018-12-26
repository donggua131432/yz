var fileSize = 0;
var fileNum = 0;
var uploader;
$(function() {
	
    //console.log(exType);
    $("#testSubject").select2({
        placeholder : "--请选择--",
        allowClear : true,
        width : '100%'
    });

	if("UPDATE" == exType) {
	    var row = courseResource_global;
			 if(row) {
			     fileNum = fileSize = row.length;
                 //console.log(row);
                 $.each(row, function(index, data){
                     //console.log(data);
                     var dom = "";
  			        dom += '<li class="file-li">';
                	dom += '<span>' + data.resourceName + '</span>';
                	dom += '<a href="javascript:void(0);" class="btn btn-normal radius ml-10 file-delete" style="t" onclick="deleteLi(this, ' + index + ')">删除</a>';
                	if(data.isAllow=='1'){
                		dom += '<a href="javascript:void(0);" class="btn btn-normal radius ml-10 file-delete" style="t" onclick="editStateLi(this,' + index + ' ,0)">禁用</a>';
                	}else{
                		dom += '<a href="javascript:void(0);" class="btn btn-normal radius ml-10 file-delete" style="t" onclick="editStateLi(this, ' + index + ',1)">启用</a>';
                	}dom += '<input type="hidden" id="' + index + 'name" name="courseResource[' + index + '].name" value="' + data.resourceName + '"/>';
                	dom += '<input type="hidden" id="' + index + 'url" name="courseResource[' + index + '].url" value="' + data.resourceUrl + '" />';
                	dom += '<input type="hidden" class="fileIsNew" id="' + index + 'isNew" name="courseResource[' + index + '].isNew" value="0" />';
                	dom += '<input type="hidden" id="' + index + 'rId" name="courseResource[' + index + '].rId" value="' + data.resourceId + '" />';
                	dom += '</li>';
                	
                	$("#fileList").append(dom);
			    });
			 }
	   if('XK' == courseType) {
	       var thpList = thpList_global;
	       
	       if(thpList && thpList.length > 0) {
	           $.each(thpList, function(index, data){
	             var unvsName = data.unvsName;
                 var recuritType = _findDict('recruitType', data.recruitType);
                     recuritType= recuritType.indexOf("成人")!=-1?"[成教]":"[国开]"
                 var pfsnName = data.pfsnName;
                 var pfsnLevel = _findDict('pfsnLevel', data.pfsnLevel);
                     pfsnLevel= pfsnLevel.indexOf("高中")!=-1?"[专科]":"[本科]"
                 var grade = _findDict('grade', data.grade);
                 var thpName = data.thpName;
                 var thpType = _findDict('thpType', data.thpType);
                 var semester = _findDict('semester', data.semester);
                 var thpId = data.thpId;

                 var dom = '<tr class="text-c">';
                 dom += '<td><input type="checkbox" name="thpIds" id="thpId_' + thpId + '" value="' + thpId + '"></td>';
                 dom += '<td class="text-l">' + grade + '-' + thpName + '</td>';
                 dom += '<td>' + thpType + '</td>';
                 dom += '<td class="text-l no-warp">'  + recuritType + unvsName + '<br>' + pfsnLevel  + grade + '-' + pfsnName + '</td>';
                 dom += '<td><a href="javascript:;" style="text-decoration: none" onclick="del(this, \'thpTable\')" title="删除" class=""><i class="iconfont icon-shanchu"></i></a></td>';
                 dom += '</tr>';
                 $("#thpTable").find("tbody").append(dom);
				});
	       } else {
               var dom = "<tr class='text-c'>";
               dom += '<td valign="top" colspan="8" class="dataTables_empty"><span>尚未选择教学计划！</span></td>';
               dom += "</tr>";
               $("#thpTable").find("tbody").append(dom);
           }
	   } else {
	       var textBooks = textBooks_global;
           if (textBooks && textBooks.length > 0) {
               $.each(textBooks, function(index, data) {
                   var textBookName = data.textbookName;
                   var publisher = data.publisher;
				   var textbookId = data.textbookId;
                   var dom = '<tr class="text-c">';
                   dom += '<td><input type="checkbox" name="textbookIds" value="' + textbookId + '"></td>';
                   dom += '<td>' + textBookName + '</td>';
                   dom += '<td>' + publisher + '</td>';
                   dom += '<td><a href="javascript:;" style="text-decoration: none" onclick="del(this, \'textBookTable\')" title="删除" class=""><i class="iconfont icon-shanchu"></i></a></td>';
                   dom += '</tr>';
                   $("#textBookTable").find("tbody").append(dom);
               });
           } else {
               var dom = "<tr class='text-c'>";
               dom += '<td valign="top" colspan="8" class="dataTables_empty"><span>尚未选择教材！</span></td>';
               dom += "</tr>";
               $("#textBookTable").find("tbody").append(dom);
           }
	   }
	}
	 
	_init_select("courseType", dictJson.courseType, courseType);
	_init_select("year", dictJson.year, year);
	_init_select("grade", dictJson.grade, grade);

    $('.radio-box input').iCheck({
        checkboxClass : 'icheckbox-blue',
        radioClass : 'iradio-blue',
        increaseArea : '20%'
    });

	if("FD" == courseType){
		$("#textBookDiv").show();
		$("#teachPlanDiv").hide();
		$("#testSubjectd").show();
	} else if('XK' == courseType){
	    $("#textBookDiv").hide();
		$("#teachPlanDiv").show();
	}
	
	$("#courseType").change(function(){
		if("FD"==$("#courseType").val()){
			$("#textBookDiv").show();
			$("#teachPlanDiv").hide();
			$("#testSubjectd").show();
		}else{
			$("#textBookDiv").hide();
			$("#teachPlanDiv").show();
			$("#testSubjectd").hide();
		}
	});

//				附件上传
     uploader = WebUploader.create({
        // 文件接收服务端。
        server:'/course/upload.do',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false
    });

    // 当有文件被添加进队列的时候
    uploader.on( 'fileQueued', function( file ) {
        //console.log(file);
        if(((file.size)/1024/1024)>=30){
            layer.msg('您所选择的附件大小大于30M，上传失败率较高，请分批上传！',{icon : 0,time : 2000});
            return
        }
        var id=file.id+'';
        var fileIndex = file.id.substring(8);
           var dom= '<li id="file-li-' + fileIndex + '" class="file-li">' +
               '<span class="info f-14">' + file.name + '</span>' +
               '<span class="state ml-10 f-12">等待上传...</span>' +
            '<a href="javascript:void(0);" class="btn btn-normal radius ml-10 file-delete" onclick="deleteLi(this,'+fileIndex+')">删除</a>' +
            '<input class="fileName" type="hidden" />' +
            '<input class="fileUrl" type="hidden" />' +
            '<input class="fileIsNew" type="hidden"  value="1" /><br></li>'
        $("#fileList").prepend(dom);
    });
  
    $("#ctlBtn").on('click',function () {
        uploader.upload()
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var fileIndex = file.id.substring(8);
        fileSize = $("#fileList").find("li").length;
        var fIndex = fileSize +fileIndex;
        var $li = $( '#file-li-'+fileIndex ),
            $percent = $li.find('.progress .progress-bar');
        var percent=Math.floor(percentage*100)
        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +'<i>0%</i>'+
                '</div>').appendTo( $li ).find('.progress-bar');
        }

        $li.find('span.state').text('上传中...');

        $percent.css( 'width', percentage * 100 + '%' );
    });


    uploader.on( 'uploadSuccess', function( file,ri ) {
        var fileIndex = file.id.substring(8);
        
		    var returnInfo = ri;
		    var code = returnInfo.code;
		    var msg = returnInfo.msg;
		    var fileUrl = returnInfo.body;
		    var fIndex = 0;

            var fileIndex = file.id.substring(8);
            var $li=$( '#file-li-'+fileIndex )
            var fileName = file.name;
		    if(_GLOBAL_SUCCESS == code) {

				fileSize = $("#fileList").find("li").length;
				fIndex = fileSize +fileIndex;
                $li.find("input.fileName").val(fileName).attr("name","courseResource[" + fIndex + "].name");
                $li.find("input.fileUrl").val(fileUrl).attr("name","courseResource[" + fIndex + "].url");
                $li.find("input.fileIsNew").attr("name","courseResource[" + fIndex + "].isNew");
                $li.find('span.state').addClass('c-success').text('上传成功！');


                $li.find('.progress').fadeOut();
                $li.find('span.state').removeClass('c-success').addClass('c-red').text('(new)');
            }else {
                layer.msg(msg, {
                    icon : 0
                },function () {
                    uploader.removeFile( file )
                    $li.remove();
                });
            }});

    uploader.on( 'uploadError', function( file ) {
        var fileIndex = file.id.substring(8);
        $( '#file-li-'+fileIndex ).find('p.state').text('上传出错').addClass('c-error');
    });

    uploader.on( 'uploadComplete', function( file ) {

    });


	$("#form-member-add").validate({
		rules : {  
			courseName : {
				required : true,
			},
			courseType : {
				required : true,
			},
			year : {
				required : true,
			}
		},
		onkeyup : false,
		focusCleanup : true,
		success : "valid",
		submitHandler : function(form) {
			var urls;
			if( $("#exType").val() == "UPDATE"){
				urls = '/course/updateCourse.do';
			}else{
				urls = '/course/insertCourse.do';
			}
			
			$("#hiddenHtml").html("");
			$.each($("#textBookTable").find("input[name='textbookIds']"), function(index){
        		var tbId = $(this).val();
        		if(tbId) {
        		    //$("#form-member-add").append('<input type="hidden" name="textbookIds" value="' + tbId + '" />');
        			$("#hiddenHtml").append('<input type="hidden" name="textbookIds" value="' + tbId + '" />');
        		}
            });
			
			$.each($("#thpTable").find("input[name='thpIds']"), function(index){
        		var tbId = $(this).val();
        		if(tbId) {
        		    $("#hiddenHtml").append('<input type="hidden" name="thpIds" value="' + tbId + '" />');
        		}
            });
			
			$(form).ajaxSubmit({
				type : "post", //提交方式  
				dataType : "json", //数据类型  
				url : urls, //请求url 
				success : function(data) { //提交成功的回调函数  
				    if(_GLOBAL_SUCCESS == data.code) {
                        if("UPDATE" == exType){
                            window.parent.myDataTable.fnDraw(false);
                        }else{
                            window.parent.myDataTable.fnDraw(true);
                        }
                        layer_close();
				    }
				}
			})
		}
	});
});

function showThpBox() {
    var selectedIds = '';
    $.each($("#thpTable").find("input[name='thpIds']"), function(index){
		var thpId = $(this).val();
		if(thpId) {
		    selectedIds += thpId + ',';
		}
    });
    
    if(selectedIds && selectedIds.length > 0) {
        selectedIds = selectedIds.substring(0, selectedIds.length - 1);
    }
    
	var url = '/course/toTpSelectInfo.do' + '?thpIds=' + selectedIds;
	
    layer_show('教学计划选择', url, 1200, 700, null,true);
}

function showTbBox() {
    
    var selectedIds = '';
    $.each($("#textBookTable").find("input[name='textbookIds']"), function(index){
		var tbId = $(this).val();
		if(tbId) {
		    selectedIds += tbId + ',';
		}
    });
    
    if(selectedIds && selectedIds.length > 0) {
        selectedIds = selectedIds.substring(0, selectedIds.length - 1);
    }
    
	var url = '/course/toTbSelectInfo.do?textbookIds=' + selectedIds;
	
    layer_show('教材选择', url, 1200, 700, null,true);
}

function delMore(type){
  if('1' == type) {
    var thpIds = $("#thpTable input[name='thpIds']:checked");
    if(thpIds && thpIds.length > 0) {
      $.each($("#thpTable input[name='thpIds']:checked"), function(index, data){
         $(this).parent().parent().remove();
      });
      $("#thpTable input:checked").removeAttr("checked");
    } else {
        layer.msg('请选择需要删除的教学计划',{icon : 2,time : 2000});
    }
  } else {
      var thpIds = $("#textBookTable input[name='textbookIds']:checked");
      if(thpIds && thpIds.length > 0) {
        $.each($("#textBookTable input[name='textbookIds']:checked"), function(index, data){
           $(this).parent().parent().remove();
        });
        $("#textBookTable input:checked").removeAttr("checked");
      } else {
          layer.msg('请选择需要删除的教材',{icon : 2,time : 2000});
      }
  }
}

function del(bt, id) {
    $(bt).parent().parent().remove();
}

function editStateLi(dom, index,status) {
    var isNew = $(dom).siblings("input.fileIsNew").val();
    var index_globle=index;
    if('0' == isNew) {
    	var resourceId = $("#" + index + "rId").val();
    	var tips=(status==1?"启用":"禁用");
    	layer.confirm('确认要'+tips+'吗？', function(index) {
            $.ajax({
                type : 'POST',
                url : '/course/updeteRecourceStatus.do',
                data : {
                	status : status,
                	resourceId : resourceId
                },
                dataType : 'json',
                success : function(data) {
                    if (data.code == _GLOBAL_SUCCESS) {
                        layer.msg('已'+tips+'!', {
                            icon : 1,
                            time : 1000
                        });
                        $(dom).text((status==1?"禁用":"启用"));
                        $(dom).attr("onclick","editStateLi(this,"+index_globle+","+(status==1?"0":"1")+")");  
                    }
                }
            });
        });
        
    }
}
function deleteLi(dom, index) {
    var isNew = $(dom).siblings("input.fileIsNew").val();

    var id='WU_FILE_'+index

    var $li = $( '#file-li-'+index );
    if('0' == isNew) {
        var delId = $("#" + index + "rId").val();
        var delUrl = $("#" + index + "url").val();

        $("#fileList").append('<input type="hidden" name="delResourceIds" value="' + delId + '" />');
        $("#fileList").append('<input type="hidden" name="delResourceUrls" value="' + delUrl + '" />');
    }else {
        uploader.removeFile(id,true)
    }
    $(dom).parent().remove();
}