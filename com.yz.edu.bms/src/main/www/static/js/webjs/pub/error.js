  var c=5;
  var t;

  function timedCount(){
  	$("#sec").html(c);
  	c=c-1;
  	if(c < 1) {
   		stopCount();
   		toPage();
   	} else {
   		t=setTimeout("timedCount()",1000);
  	}
  }
  
  function toPage() {
  	window.top.location.href = url;
  }

  function stopCount(){
	clearTimeout(t);
  }
			
	$(function(){
		if(errorCode==='E000034'||errorCode==='E000035'){
			$('#errorTitle > p:eq(1)').show()
		}else{
			$('#errorTitle > p:eq(0)').show()
		}
		timedCount();
	});