$(function(){
    	 var row = "courseResource";
    	 if(row.length <= 0 || null == row){
    		 /* $("#form-member-add").append("<h1>无上传课程资源</h1>");   */
    		 layer.msg('无课程资源', {icon : 5, time : 1000},function(){
					layer_close();
				});
    	 }
    	 for (var i = 0; i < row.length; i++) {
         var dom = '';    	 
    		 dom = '<div class="row cl">'
    		       +'<label class="form-label col-xs-4 col-sm-3">资源_'+(i+1)+'：</label>'
    		       +'<input type="text" class="input-text"  value="'+row[i].resourceName+'" style="border: 0;" readonly="readonly">'
    		       +'<input class="btn btn-primary radius" type="button" onclick="down(this);" courseResourceId="'+row[i].resourceId+'" value="&nbsp;&nbsp;下载&nbsp;&nbsp;">'
    		       +'</div>';
    	 $("#form-member-add").append(dom);   
    	 }
         // 文件下载
    	 jQuery.download = function(url, method, resourceId){
    	     jQuery('<form action="'+url+'" method="'+(method||'post')+'">' +  // action请求路径及推送方法
    	                 '<input type="text" name="resourceId" value="'+resourceId+'"/>' + // 
    	             '</form>')
    	     .appendTo('body').submit().remove();
    	 };
     });
     
     function down(e){
    	 var courseResourceId = $(e).attr("courseResourceId");
    	 $.download("/course/down.do",'post',courseResourceId);
     }