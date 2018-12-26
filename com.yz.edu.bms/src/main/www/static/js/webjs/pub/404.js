 	var c=3;
    var t;

    function timedCount(){
      $("#sec").html(c);
      c=c-1;
      if(c < 1) {
        stopCount();
        toIndex();
      } else {
        t=setTimeout("timedCount()",1000);
      }
    }
    
    function toIndex() {
    	window.top.location.href = '/index.do';
    }
  
    function stopCount(){
    clearTimeout(t);
    }
        
    $(function(){
      timedCount();
    });