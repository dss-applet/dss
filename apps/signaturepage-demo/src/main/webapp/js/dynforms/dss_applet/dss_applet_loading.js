var loading = false;
var loadingDialog;
/**
 * Called by the applet to set or unset the loading screen
 */
function setLoading(b, message1, message2){
   var visible = eval(b);
    if(visible == true && !loading){
    	loading = true;
		var l = $("#dss_applet_loading_message").clone();
		if (message1 ==null || "null"=== message1) {
			message1 = "";
		}		
    		l.show();
    		if (message1.length ==0){
    			l.find("#dss_applet_loading_text1").html(longOperation);
    		} else {
    			l.find("#dss_applet_loading_text1").html(message1);	
    		}    		
    		if (message2 !=null && "null"!=message2){
    			l.find("#dss_applet_loading_text2").html(message2);
    			l.find("#dss_applet_loading_text2").show();
    		} 
    		l.find('#dss_applet_cancel_loading_span').show();
    		l.find('#dss_applet_cancel_loading_button').click(killApplet);
    		loadingDialog = l.dialog({
    			  dialogClass: "no-close",
    			  modal:true
    			});
    		loadingDialog.dialog('open');   	    	 
    } else if (visible == true && loading == true){
    	loadingDialog.dialog('close');
    	loading = false;
    	setLoading(b,message1,message2);
    }
    else {
    	loading = false;
    	if (loadingDialog !== undefined) {
    		loadingDialog.dialog('close');
    	}
    	hideMoccaPinPadAdvice();
    }
}
