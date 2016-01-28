

function initExternalMethod() {	
	$("#dss_applet_btn_upload_signed_ol").hide();
	$("#dss_applet_btn_upload_signed_input_ol").attr('disabled','disabled');
	$("#dss_applet_btn_upload_browse_ol").change(function (){ $("#dss_applet_btn_upload_signed_input_ol").removeAttr('disabled');});
	$("#dss_applet_btn_download_input_ol").click(function (){ $("#dss_applet_btn_upload_signed_ol").show();});
}

