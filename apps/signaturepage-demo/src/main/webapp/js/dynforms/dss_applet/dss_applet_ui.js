var yesMsg;
var noMsg;
var okMsg;
var cancelMsg;
var pingId;
var exitCode;
var showSurveyTitle;
var errorReportTitle;
var requestPasswordTitle;
var showOpenSealedPdfTitle;
var showAppletDeathTitle;
var showUnsupportedVendorTitle;
var appletInitialisationFailureTitle;
var sessionExpiredTitle;
var mwHelp1Title;
var mwHelp2Title;
var mwHelp3Title;
var mwHelp4Title;
var mwHelp5Title;
var mwHelp6Title;
var mwHelp7Title;
var mwHelp8Title;
var mwHelpDownloadBtn;
var upgradeJava;
var recommendedForSignature;
var longOperation;
var popupBlockWarn;
var showOtherCertificates;
var warningTitle;
var unsupportedJavaVersion;
/**
 * Called by the Jsp to set Yes/No label, etc.
 */
function initUILabels() {
    yesMsg = $("#dss_applet_message_yes").html();
    noMsg = $("#dss_applet_message_no").html();
    okMsg = $("#dss_applet_message_ok").html();
    cancelMsg = $("#dss_applet_message_cancel").html();
    showSurveyTitle = $("#dss_applet_showSurveyTitle").html();
    errorReportTitle = $("#dss_applet_dss_applet_message_cancel").html();
    requestPasswordTitle = $("#dss_applet_requestPasswordTitle").html();
    showOpenSealedPdfTitle = $("#dss_applet_showOpenSealedPdfTitle").html();
    showAppletDeathTitle = $("#dss_applet_showAppletDeathTitle").html();
    showUnsupportedVendorTitle = $("#dss_applet_showUnsupportedVendorTitle").html();
    appletInitialisationFailureTitle = $("#dss_applet_appletInitialisationFailureTitle").html();
    sessionExpiredTitle = $("#dss_applet_sessionExpiredTitle").html();
    mwHelp1Title = $("#dss_applet_mwHelp1Title").html();
    mwHelp2Title = $("#dss_applet_mwHelp2Title").html();
    mwHelp3Title = $("#dss_applet_mwHelp3Title").html();
    mwHelp4Title = $("#dss_applet_mwHelp4Title").html();
    mwHelp5Title = $("#dss_applet_mwHelp5Title").html();
    mwHelp6Title = $("#dss_applet_mwHelp6Title").html();
    mwHelp7Title = $("#dss_applet_mwHelp7Title").html();
    mwHelp8Title = $("#dss_applet_mwHelp8Title").html();
    mwHelpDownloadBtn = $('#dss_applet_message_mw_2_proceed').html();
    upgradeJava = $('#dss_applet_message_mw_7_upgrade_java').html();
    recommendedForSignature = $('#dss_applet_recommended_for_signature').html();
    longOperation = $('#dss_applet_longOperation').html();
    pinPadTitle = $('#dss_applet_pin_pad_entry').html();
    popupBlockWarn= $('#dss_applet_popupBlockWarn').html();
    showOtherCerts= $('#dss_applet_show_other_certs').html();
    warningTitle=$("#dss_applet_message_warning_title").html();
    unsupportedJavaVersion=$('#dss_applet_message_unsupported_java_version').html();
    bugJavaVersion=$('#dss_applet_message_bug_java_version').html();
    bugJavaVersionApplet=$('#dss_applet_message_bug_java_version_applet').html();

}

/**
 * Launch the applet inside the jsp page.
 */
function launchApplet(method ) {
	initUILabels();
	var go = false;
	if (deployJava.versionCheck('1.6.0+')) {
		go = true;
	} else {
		showDownloadJavaPrompt();
		return;
	}
	var vNotSupported = deployJava.versionCheck('1.7.0_21+') && !deployJava.versionCheck('1.7.0_51+');
	if (go && vNotSupported) {
		showBugVersion(method);
	} else if (go) {
		appletGo(method);
	} else {
        // can't launch the applet, redirect to download Java
        showDownloadJavaPrompt();
    }
}

function showBugVersion (method) {
    $("#dss_applet_link_nojava").hide();
    $("#dss_applet_bug_version").show();
}


function appletGo(method) {
	$('#dss_applet_bug_version').hide();
    var applet_version = '0.14.4';
    var applet_jars = 'ej-portal-dss-applet-'+applet_version+'.jar';
    applet_jars +=',ej-portal-dss-applet-6-'+applet_version+'.jar';
    applet_jars +=',ej-portal-dss-mocca-'+applet_version+'.jar';

    var attributes = {
        codebase: './dss_applet/',
        code: 'eu.europa.ejusticeportal.dss.applet.DssApplet.class',
        archive: applet_jars,
        id: 'dss_applet',
        mayscript: true,
        width: 800,
        height: 400
    };
    var parameters = {
        navPlatform: navigator.platform,
        userAgent: navigator.userAgent,
        problemJreVersions: '1.7.0_12-1.7.0_45',
        problemJreWarning: bugJavaVersionApplet,
        classloader_cache: false,
        signMethod: method,
        askForEventLog: true
    };
    var version = '1.6';
    deployJava.runApplet(attributes, parameters, version);
    if (typeof dss_applet ==="undefined"){
    	//FF 10
    	dss_applet = $("#dss_applet")[0];
    }
    isMyUnload = true;
    //Check for a managed exit
    exitCode = setInterval(function(){
    	try {
    		var v = dss_applet.getManagedExitCode();
    		if (v === "dss_applet_message_ok") {
    			clearInterval(exitCode);
    		} else if (v !== "") {
    			clearInterval(exitCode);
    			$('#dss_applet').remove();
    			showManagedExit(v);    			
    		}
    	} catch (e){
    		//it's initialising or dead
    	}
    },10000);
}

/**
 * Reloads the applet
 */
function reloadApplet(){
	hideTooltips();
    $(document).ready(function() {
        $("#dss_applet_btn_reload_applet").click();
    });
}
/**
 * Reloads the applet
 */
function rebootApplet(){	
	isMyUnload = true;
	window.location = window.location.pathname;
}

/**
 * Resize the loading overlay
 */
function resizeLoadingOverlay() {
	var overlay = $(".dss_applet_loading-overlay");
	if (overlay!=null){
		overlay.css('height',$("#maincontent").css('height'));
		overlay.css('width',$("#maincontent").css('width'));
	}
}
/**
 * Called by the applet to update the UI
 */
function updateUI(data) {
	hideTooltips();
    var json = jQuery.parseJSON(data);

    for (var i = 0; i < json.length; i++) {

        if (json[i].id!= "messages" && json[i].visible != null) {
            if (json[i].visible === true) {            	
                $("#dss_applet_" + json[i].id).show();
            } else {            	
                $("#dss_applet_" + json[i].id).hide();
            }
        }

        if (json[i].id!= "messages" && (json[i].enabled != null)) {
            if (json[i].enabled === true) {
                if ($("#dss_applet_" + json[i].id).is("div")) {
                    $("#dss_applet_" + json[i].id + "_input").removeAttr('disabled');
                    $("#dss_applet_" + json[i].id + "_value").removeAttr('disabled');
                } else {
                    $("#dss_applet_" + json[i].id).removeAttr('disabled');
                }
            } else {
                if ($("#dss_applet_" + json[i].id).is("div")) {
                    $("#dss_applet_" + json[i].id + "_input").attr('disabled', 'true');
                    $("#dss_applet_" + json[i].id + "_value").attr('disabled', 'true');
                } else {
                    $("#dss_applet_" + json[i].id).attr('disabled', 'true');
                }
            }
        }

        if (json[i].id!= "messages" && json[i].text != null) {
            if($("#dss_applet_" + json[i].id).is("span") && json[i].url !=null){
                $("#dss_applet_" + json[i].id).html(json[i].url);
            } 
            else if($("#dss_applet_" + json[i].id).is("span")){
                $("#dss_applet_" + json[i].id).html(json[i].text);
            }             
            else if($("#dss_applet_" + json[i].id).is("p")){
                $("#dss_applet_" + json[i].id).html(json[i].text);
            }
            else if($("#dss_applet_" + json[i].id).is("img")){
                $("#dss_applet_" + json[i].id).attr("title",json[i].text);
                $("#dss_applet_" + json[i].id).attr("alt",json[i].text);
            }
            else {
                $("#dss_applet_" + json[i].id + "_value").val(json[i].text);
            }
        }
        var j;
        if (json[i].options != null) {
        	if (json[i].id==="sel_certificates_recommended" || json[i].id==="sel_certificates_other") {
        		showCertificates(json[i]);
        	} else {
	            var htmlOptions = "";
	            for (j = 0; j < json[i].options.length; j++) {
	                htmlOptions += "<option value=\"" + j + "\"";
	                if (json[i].options[j].enabled==false){
	                    htmlOptions+=" disabled=\"disabled\" ";
	                }
	                if (json[i].options[j].selected==true){
	                    htmlOptions+=" selected=\"selected\" ";
	                }
	                htmlOptions +=">" + json[i].options[j].text + "</option>";
	            }
	
	            if (json[i].options.length == 1) {
	                $("#dss_applet_" + json[i].id + "_value").val(1);
	                $("#dss_applet_" + json[i].id + "_value").removeClass("df_mandatory");
	                $("#dss_applet_" + json[i].id + "_value").addClass("df_mandatory_filled");
	            } else {
	                $("#dss_applet_" + json[i].id + "_value").removeClass("df_mandatory_filled");
	                $("#dss_applet_" + json[i].id + "_value").addClass("df_mandatory");
	            }	            
	            $("#dss_applet_" + json[i].id + "_value").html(htmlOptions);
        	}
        }
        if (json[i].id=="messages"  && json[i].clear === true){
            // Hide previous messages
            $("#dss_applet_messages").hide();
            $("#dss_applet_messages").html("");
            resizeLoadingOverlay();
        }
        if (json[i].id=="messages" && json[i].messages != null) {
            var level = {
                "SUCCESS" : ["SUCCESS",0],
                "INFO" : ["INFO",1],
                "WARNING" : ["WARNING",2],
                "ERROR" : ["ERROR",3]
            };
            var k = 0;
            for (k=0;k<json[i].messages.length;k++){
	            
            	
            	
                var htmlMsg = "";
                var htmlTitle = "";
                var msgBox;
                var msg = json[i].messages[k];
                var msgId = "dss_applet_message_id_"+msg.id;
	            
                if ($("#"+msgId).length!=null && $("#"+msgId).length>0){
                    //this message already displayed
                    continue;
                }
                // set the level
                if(msg.level == level.SUCCESS[0]){
                    msgBox = $("#dss_applet_message_success").clone();
                }
                else if(msg.level == level.ERROR[0]){
                    msgBox = $("#dss_applet_message_error").clone();
                } else if(msg.level == level.WARNING[0]){
                    msgBox = $("#dss_applet_message_warning").clone();
                } else if(msg.level == level.INFO[0]){
                    msgBox = $("#dss_applet_message_info").clone();
                } else {
                    continue;
                }
	
                msgBox.attr("id",msgId);
                // Set the title
                if(msg.title != "" && msg.title != null){
                    htmlTitle += "<h3>" + msg.title + "</h3>";
                }
	
                if (msg.details!=null){
                    for (j = 0; j < msg.details.length; j++) {
                        var d = msg.details[j];
                        htmlMsg += "<li>"
                        + d.text
                        + "</li>";
                    }
                }
                var htmlMerged = htmlTitle;
                if(htmlMsg != ""){
                    htmlMerged += "<ul>" + htmlMsg + "</ul>";
                }
                if(htmlMerged != ""){
                    msgBox.html(htmlMerged);
                    msgBox.appendTo("#dss_applet_messages");
                    msgBox.show();
                    $("#dss_applet_messages").show();
                    resizeLoadingOverlay();
                }
            }
        }


        if(json[i].url != null){
            $("#dss_applet_" + json[i].id).attr("href", json[i].url);
            $("#dss_applet_" + json[i].id).html(json[i].url);
        }
    
    }
//    resizeLoadingOverlay();

//    $(document).tooltip();
    //tooltip.init();
    dssInitTooltip();

}

function showCertificates(list){
    var htmlOptions = "<div>";
    var j = 0;
    if (list.options.length >0 ){
    	$('#dss_applet_'+list.id+'_desc').show();
    } else {
    	$('#dss_applet_'+list.id+'_desc').hide();
    }
    htmlOptions +="</div>";

    	for (j=0; j<list.options.length; j++) {
        	var option = list.options[j];
        	htmlOptions += addOption(list.id,j,option);
        	htmlOptions += "<br/>";
    	}
    	htmlOptions += "</div>";
    $("#dss_applet_" + list.id + "_list").html(htmlOptions);
    $('#dss_applet_'+list.id+'_list img').each(function (i){
    	$(this).attr("title",list.options[i].summaryInfo);});

}

function addOption(id,j,option) {
	var htmlOptions = "<div class=\"dss_applet_cert_list_item\"><input id=\""+option.id+"\"type = \"radio\" name=\"dss_applet_sel_certificates_rb"+"\" value=\"" + j + "\"";
    if (option.enabled==false){
        htmlOptions+=" disabled=\"disabled\" ";
    }
    if (option.selected==true){
        htmlOptions+=" checked=\"checked\" ";
    }
    
    htmlOptions += " onclick=\"certificateSelected('"+option.id+"','"+option.dateValid+"')\"";
    htmlOptions +=" ><label for='"+option.id +"'";
    if (!option.dateValid) {
    	htmlOptions +=" class=\"dss_applet_expired\" ";
    }
    
    htmlOptions +=">"; 
    htmlOptions +=option.text;
    htmlOptions +="&nbsp;";
    htmlOptions+="</label>";
    htmlOptions += "<img style=\"vertical-align: middle;\" src=\"images/info_ico.png\"/>";
    htmlOptions += "<br/>";
    htmlOptions += "<span class=\"dss_applet_cert_issuer\">";
    htmlOptions += option.issuerName;
    if (option.issuerCountry!="") {
    	htmlOptions += "&nbsp;</span><span class=\"dss_applet_cert_issuer_country\">(";
    	htmlOptions += option.issuerCountry;
    	htmlOptions += ")</span>";
    }
    htmlOptions += "</div>";
    return htmlOptions;
}
/**
 * Handles selection of a certificate
 * @param id the component id
 * @param index the hash of the selected certificate
 */
function certificateSelected(hash,valid){
	$("#dss_applet_sel_certificates_value").text(hash);
	$("#dss_applet_sel_certificates_date_valid").text(valid);
	dss_applet.handleUiEvent('prepareToSignWithOtherCertificate', null);
}
var shouldUploadAppletLog = true;
/**
 * Called by the applet to initialise the HTML Ui and to set the UI events.
 */
function initCommonJava() {
    $("#dss_applet").width(1);
    $("#dss_applet").height(1);
    $("#dss_applet_link_nojava").hide();
    $("#dss_applet_file_pkcs11_browse").click(function() {
		hideTooltips();
        dss_applet.handleUiEvent("selectPKCS11File", null);
    });
    $("#dss_applet_file_pkcs12_browse").click(function() {
        hideTooltips();
		dss_applet.handleUiEvent("selectPKCS12File", null);
    });
    $("#dss_applet_file_upload_browse").click(function() {
        hideTooltips();
		dss_applet.handleUiEvent("selectSignedFile", null);
    });
    
    $("#dss_applet_btn_sign_input").click(function() {
    	hideTooltips();
		warnExpiredCertificate();        
    });

    $("#dss_applet_btn_refresh").click(function() {
        hideTooltips();
		dss_applet.handleUiEvent("refreshSigningContext", null);
    });
    $("#dss_applet_file_download_signed_input").click(function() {
        hideTooltips();
		dss_applet.handleUiEvent("saveSignedPdf", null);
    });
    $("#dss_applet_btn_open_input").click(function() {
        hideTooltips();
		dss_applet.handleUiEvent("openSealedPdf", null);
    });

    //Make the textarea not editable
    $("#dss_applet_txt_status_value").bind('keydown', function(e) {
        e.preventDefault();
    }, false);
    $("#dss_applet_txt_status_value").bind('vut', function(e) {
        e.preventDefault();
    }, false);

    $("#dss_applet_sel_methods_value").change(function() {
        hideTooltips();
		dss_applet.handleUiEvent("changeMethod", $("#dss_applet_sel_methods_value").val());
    });

    $("#dss_applet_download_unsigned_pdf").click(function() {
        hideTooltips();
		dss_applet.handleUiEvent("saveSealedPdf", null);
    });
    
    $("#dss_applet_txt_card_advice_suppl").click(function(){
    	hideTooltips();
		dss_applet.handleUiEvent("showMiddlewareHelp", "1");
    });
    
    pingId = setInterval(function(){
	    	try {
	    		dss_applet.ping();
	    	} catch (e){
	    		clearInterval(pingId);
	    		showAppletDeath();	    		
	    	}
    	}
    
    ,30000);
    
    $(window).resize(function() {
    	resizeLoadingOverlay();
    });
    
    $('body').on('keypress', '.ui-dialog', function(event) { 
        if (event.keyCode === $.ui.keyCode.ENTER) { 
            $('.ui-dialog-buttonpane button:first', $(this)).click();
            return false;
        }
    });
    
    $(window).on('beforeunload',function(){
    	if (shouldUploadAppletLog) {
    		dss_applet.uploadAppletLog();
    	}
    });
}


/**
 * Called by the applet to prompt a survey (for collecting data about the signing operations)
 */
function showSurvey() {
	hideTooltips();
	$("#dss_applet_txt_sc_issuer").val("");
    $(function() {
        var d = $("#dss_applet_user_survey").dialog({
            minWidth: 480,
            minHeight: 320,
            resizable: true,
            zIndex: 10000,
            modal: true,
            autoOpen: false,
            title: showSurveyTitle,
            buttons: [{
                text: yesMsg,
                click: function() {
                    dss_applet.handleUiEvent("agreeLogSigningInformation", $("#dss_applet_txt_sc_issuer").val());
                    $("#dss_applet_txt_sc_issuer").val("");
                    $(this).dialog("close");
                }
            }, {
                text: noMsg,
                click: function() {
                	dss_applet.handleUiEvent("refuseLogSigningInformation", "");
                    $("#dss_applet_txt_sc_issuer").val("");
                    $(this).dialog("close");
                }
            }]
        });
        d.dialog('open');
    });
}

/**
 * Called by the applet to allow the user to provide the stack trace of an error
 */
function showErrorReport() {
	hideTooltips();
    $(function() {
        var d = $("#dss_applet_error_report").dialog({
            minWidth: 480,
            minHeight: 320,
            resizable: true,
            zIndex: 10000,
            modal: true,
            autoOpen: false,
            title: errorReportTitle,
            buttons: [{
                text: yesMsg,
                click: function() {                	
                    dss_applet.handleUiEvent("agreeSendErrorReport", $("#dss_applet_txt_error_report").html());
                    $(this).dialog("close");
                }
            }, {
                text: noMsg,
                click: function() {
                	dss_applet.handleUiEvent("refuseSendErrorReport", "");
                    $(this).dialog("close");
                }
            }]
        });
        d.dialog('open');
    });
}

var passwordDialog;
/**
 * Called by the applet to request a password for PKCS11 or PKCS12
 * @param explanation text to explain why the password is needed
 * @param "true" if there was a previous attempt with a wrong PIN
 */
function requestPassword(explanation, wrongpin) {
    hideTooltips();
	$("#dss_applet_txt_provide_password").html(explanation);
    $(function() {
    	$("#dss_applet_txt_password").val("");
    	if (wrongpin==="true"){
    		$("#dss_applet_txt_wrong_pin").show();
    	} else {
    		$("#dss_applet_txt_wrong_pin").hide();
    	}
    	
        passwordDialog = $("#dss_applet_password").dialog({
            minWidth: 480,
            minHeight: 320,
            resizable: true,
            zIndex: 10000,
            modal: true,
            autoOpen: false,
            closeOnEscape: false,
            title: requestPasswordTitle,
            buttons: [{
                text: okMsg,
                click: function() {
                	try {
                		dss_applet.handleUiEvent("providePassword", $("#dss_applet_txt_password").val());
                	} catch (e) {
                		//
                	}
                    $("#dss_applet_txt_password").val("");
                    $(this).dialog("close");
                }
            }, {
                text: cancelMsg,
                click: function() {
                	try {
                		dss_applet.handleUiEvent("refusePassword", "");
                	} catch (e){
                		//
                	}
                	$("#dss_applet_txt_password").val("");
                    $(this).dialog("close");
                    
                }
            }]
        });
        passwordDialog.dialog('open');
    });
}
/**
 * Called by the applet when the sealed Pdf has been downloaded - it prompts the user to open it
 */
function showOpenSealedPdfPrompt(){
    hideTooltips();
	$("#dss_applet_openSealedPdf_dialog").dialog({
        autoOpen: true,
        modal: true,
        zIndex: 10000,
        title: showOpenSealedPdfTitle,
        buttons: [{
            text: yesMsg,
            click: function() {
                dss_applet.handleUiEvent("viewPdfBeforeSigning", null);
                $(this).dialog("close");
            }
        }, {
            text: noMsg,
            click: function() {
                $(this).dialog("close");
            }
        }]
    });
}
/**
 * Warning when the user tries to use an expired certificate
 */
function warnExpiredCertificate(){
	hideTooltips();
	var valid = $("#dss_applet_sel_certificates_date_valid").text();
	if (valid == "false") {
	    $("#dss_applet_invalid_cert_date_dialog").dialog({
	        autoOpen: true,
	        modal: true,
	        zIndex: 10000,
	        title: warningTitle,
	        buttons: [{
	            text: yesMsg,
	            click: function() {
	            	dss_applet.handleUiEvent("signDocument", $("#dss_applet_sel_certificates_value").text());
	                $(this).dialog("close");
	            }
	        }, {
	            text: noMsg,
	            click: function() {
	                $(this).dialog("close");
	            }
	        }]
	    });
	} else {
		dss_applet.handleUiEvent("signDocument", $("#dss_applet_sel_certificates_value").text());
	}
}


/**
 * Called if the web page detects that the applet is no longer responding.
 */
function showAppletDeath(){
    $("#dss_applet_death").dialog({
        autoOpen: true,
        modal: true,
        zIndex: 10000,
        title: showAppletDeathTitle
    });
}
/**
 * Called for a managed exit when the applet is not supported.
 */
function showManagedExit(code){
	try {
		dss_applet.killApplet();
	} catch (e) {
		
	}
    $("#dss_applet_link_nojava").hide();
	if (code === "dss_applet_message_unsupported_vendor"){
		 $("#dss_applet_unsupported_vendor").show();
	} else if (code === "dss_applet_message_bug_java_version") {
	    $("#dss_applet_bug_version").show();
	} else if (code === "dss_applet_mac_unsafe") {
		$("#dss_applet_mac_unsafe").show();
	} else if (code === "dss_applet_not_scriptable") {
		$("#dss_applet_not_scriptable").show();
	} else if (code === "dss_applet_tampered_communication") {
		$("#dss_applet_tampered_communication").show();
	}
   
}
/**
* Called from the applet when there is an unrecoverable initialisation failure in the applet.
*/
function appletInitialisationFailure() {
	try {
		dss_applet.stop();
		dss_applet.destroy();
	} catch (e) {
		
	}
    $("#dss_applet_death").dialog({
        autoOpen: true,
        modal: true,
        zIndex: 10000,
        title: appletInitialisationFailureTitle
    });	
}
/**
 * Called by the javascript when java requirements for launching the applet are not met (Java >= 5)
 */
function showDownloadJavaPrompt() {
	$("#dss_applet_bug_version").hide();
    $("#dss_applet_link_nojava").hide();
    $("#dss_applet_unsupported_vendor").hide();
    if ($.client.os === 'Mac' && $.client.browser === 'Chrome') {
    	$("#dss_applet_sec_download_java_mac_chrome").show();
    }
    $("#dss_applet_sec_download_java").show();
    
}

/**
 * Called by the applet when there was a failure in the initialisation.
 * (User does not allow the applet to run, server is unreachable)
 */
function redirectToNoJavaPage() {
    $(document).ready(function() {
        //$("#dss_applet_forward_nojava").click();
    	window.history.back();
    });
}

/**
 * Called when the user refuse to install Java
 */
function redirectToDownloadJavaPage() {
        dwlJavaWindow=window.open('http://www.java.com/download/','_blank');
        dwlJavaWindow.focus();
}

/**
 * Called by the javascript when the user is prompt to download a new version of Java,
 * and he wants to save his form before downloading it.
 */
function downloadPdfFromServer() {
    hideTooltips();
	$(document).ready(function() {
    	shouldUploadAppletLog = false;
        $("#dss_applet_btn_download_input").click();
    });
}

/**
 * Called by the applet when the user session has expired. Prompt the user to save his form.
 */
function promptSessionExpired() {
    $(function() {
        var d = $("#dss_applet_saveSealedPdf_dialog").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: sessionExpiredTitle,
            buttons: [{
                text: yesMsg,
                click: function() {
                    dss_applet.handleUiEvent("saveSealedPdf", "redirectSessionExpired");
                    $(this).dialog("close");
                }
            }, {
                text: noMsg,
                click: function() {
                    $(this).dialog("close");
                    redirectSessionExpired();
                }
            }]
        });
        d.dialog('open');

    });
}

/**
 * Called by the applet when the user needs to be redirected on the session expired page.
 */
function redirectSessionExpired() {
    $(document).ready(function() {
        $("#dss_applet_expired_session").click();
    });
}

var currentMwDialog;

/**First dialog in the middleware help*/
function showMiddlewareDialog1() {
	$(function() {
		currentMwDialog = $("#dss_applet_mw_1").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp1Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                    $(this).dialog("close");
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}
/**Show provider website dialog*/
function showMiddlewareDialog2(arg) {
	var actionButtons;
	if ("null" === arg) {
		actionButtons = [{
        	id: 'dss_applet_mw_2_cancel',
            text: cancelMsg,
            click: function() {
            	dss_applet.handleUiEvent("showMiddlewareHelp", "4");
            }
        }
        ,
        {
        	id: 'dss_applet_mw_2_proceed',
            text: mwHelpDownloadBtn,
            click: function() {
            	try {
            		dss_applet.handleUiEventSynch("saveSealedPdf", null);
            	} catch (e) {
            		
            	}
            	if ("null"===arg){
            		dss_applet.handleUiEvent("showMiddlewareHelp", "6");
            	} 
            }
        }
        ];
	} else {
		actionButtons = [{
        	id: 'dss_applet_mw_2_cancel',
            text: cancelMsg,
            click: function() {
            	dss_applet.handleUiEvent("showMiddlewareHelp", "4");
            }
        }];
		$('#dss_applet_provider_url').attr("href",arg);
		$('#dss_applet_provider_url').show();
	}
	$(function() {
		currentMwDialog = $("#dss_applet_mw_2").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            width:'auto',
            height:'auto',
            title: mwHelp2Title,
            buttons: actionButtons
        });
		currentMwDialog.dialog('open');
    });
}
/**Look in program files dialog*/
function showMiddlewareDialog3() {
	$(function() {
		currentMwDialog = $("#dss_applet_mw_3").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp3Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                	dss_applet.handleUiEvent("showMiddlewareHelp", "4");
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}
/**The sorry dialog*/
function showMiddlewareDialog4() {
	$(function() {
		currentMwDialog = $("#dss_applet_mw_4").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp4Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                	currentMwDialog.dialog('close');
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}
/**The PKCS11 dialog*/
function showMiddlewareDialog5() {
	$("#dss_applet_file_pkcs11").hide();
	$("#dss_applet_file_pkcs11").html("");
	$(function() {
		currentMwDialog = $("#dss_applet_mw_5").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp5Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                	dss_applet.handleUiEvent("showMiddlewareHelp", "4");
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}

/**Dialog to ask user to find the site to download middleware*/
function showMiddlewareDialog6() {
	$(function() {
		currentMwDialog = $("#dss_applet_mw_6").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp6Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                	currentMwDialog.dialog('close');
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}
/**Dialog to upgrade java*/
function showMiddlewareDialog7() {
	$(function() {
		currentMwDialog = $("#dss_applet_mw_7").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp7Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                	currentMwDialog.dialog('close');
                }
            },
            {
                text: upgradeJava,
                click: function() {
                	try {
                		dss_applet.handleUiEventSynch("saveSealedPdf", null);}
                	catch (e){
                		//
                	}
                	redirectToDownloadJavaPage();
                	dss_applet.handleUiEvent("showMiddlewareHelp", "8");
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}

/**Dialog shown after we've upgraded Java*/
function showMiddlewareDialog8() {
	$(function() {
		currentMwDialog = $("#dss_applet_mw_8").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: mwHelp8Title,
            buttons: [{
                text: cancelMsg,
                click: function() {
                	currentMwDialog.dialog('close');
                }
            }]
        });
		currentMwDialog.dialog('open');
    });
}


/**
 * Show one of the middleware help dialogs
 * @param id and identifier for the dialog
 * @param arg an argument
 */
function showMiddlewareHelp(id, arg){
	hideTooltips();
	closeMwDialog();
	if (id === "1"){
		showMiddlewareDialog1();
	} else if (id ==="2"){
		showMiddlewareDialog2(arg);
	} else if (id ==="3"){
		showMiddlewareDialog3();
	} else if (id ==="4"){
		showMiddlewareDialog4();
	} else if (id ==="5"){
		showMiddlewareDialog5();
	} else if (id ==="6"){
		showMiddlewareDialog6();
	} else if (id ==="7"){
		showMiddlewareDialog7();
	} else if (id ==="8"){
		showMiddlewareDialog8();
	}
}
/**
 * Closes the currently open middleware dialog
 */
function closeMwDialog(){
	if (currentMwDialog !=null){
		currentMwDialog.dialog('close');
	}
}
var moccaDialog;
/**
 * Show some information to help with mocca PIN pad entry
 * @param message information message
 */
function showMoccaPinPadAdvice(message){
	hideTooltips();
	$(function() {
		$("#dss_applet_mocca_help_text").html(message);
		moccaDialog = $("#dss_applet_mocca_help").dialog({
            resizable: true,
            modal: true,
            zIndex: 10000,
            autoOpen: false,
            title: pinPadTitle
        });
		moccaDialog.dialog('open');
    });
	
}

function closePasswordDialog () {
	
	if (moccaDialog !=null) {
		try {
		moccaDialog.dialog('close');
		dss_applet.handleUiEvent("refusePassword", "");
		} catch (e) {
			
		}
	}
	if (passwordDialog != null) {
		try {
			passwordDialog.dialog('close');
			dss_applet.handleUiEvent("refusePassword", "");
		} catch (e) {
			
		}
	}
}

function hideMoccaPinPadAdvice(){
	if (moccaDialog !=null){
		moccaDialog.dialog('close');
	}
}

var killApplet = function (){
	if (dss_applet != null) {
		try {
			dss_applet.killApplet();
		} catch (e) {
			
		}
	}	
	rebootApplet();
};


var serverCallUrl;
/**
 * Initialise the server call URL
 */
function initServerCallUrl(){
	serverCallUrl = $("#dss_applet_servercall_url").html();
}

/**
 * Call a signing service in the e-Justice portal. The method executes
 * asynchronously and invokes an applet method in a callback to provide
 * the result of the call.
 * @param serverCallId the id of the service, also used in the callback 
 * @param data the data to provide to the service
 */
function callServer(serverCallId, data){
    $.ajax({
        type: "POST",
        url: serverCallUrl,
        data: {
            id: serverCallId,
            data: data
        },
        success: function(data) {
        	if (serverCallId !=null) {
        		handleServerCallBack(serverCallId, data.data, data.errorCode, data.hash, data.algo);
        	}
        },
        error: function(jqXHR, textStatus, errorThrown) {
        	if (serverCallId !=null) {
        		handleServerCallBack(serverCallId, null, 'dss_applet_message_technical_failure', null, null);
        	}
        }
    });	
}

function handleServerCallBack(serverCallId, data, errorCode, hash, algo){
	var typeofDssApplet = typeof dss_applet;
	var typeofHandleSCB ="";
	if (typeofDssApplet !== "undefined") {
		typeofHandleSCB = typeof dss_applet.handleServerCallBack;
	}
	if (typeofDssApplet !=="undefined" && (typeofHandleSCB  === "function"||typeofHandleSCB==="unknown" )){
		dss_applet.handleServerCallBack(serverCallId, data, errorCode,hash, algo);
	} else {
		setLoading(false,"","");
		$('#dss_applet').remove();
		if (!tampered){
			showManagedExit("dss_applet_not_scriptable");
		}
	}
	
}
var tampered = false;
function closeForTamperedCommunication() {
	tampered = true;
	setLoading(false,"","");
	try {
		$('#dss_applet').remove();
		$('#dss_applet_sec_sign_pdf').hide();
		clearInterval(pingId);
	} catch (e) {
		
	}
	showManagedExit("dss_applet_tampered_communication");
}
function hideTooltips(){
	$('.ui-tooltip').each(function(i){$(this).remove();});
}