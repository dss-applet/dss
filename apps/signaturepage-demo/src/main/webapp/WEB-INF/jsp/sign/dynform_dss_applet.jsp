<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="bean" tagdir="/WEB-INF/tags/bean"%>
<%@ taglib prefix="df" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags/html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript" src="js/dynforms/dss_applet/jquery.client.js"></script>
<script type="text/javascript" src="js/dynforms/dss_applet/dss_applet_tooltip.js"></script>
<script type="text/javascript" src="js/dynforms/dss_applet/dss_applet_loading.js"></script>
<script type="text/javascript" src="js/dynforms/dss_applet/dss_applet_ui.js"></script>
<link rel="stylesheet" href="css/dss_applet/showLoading.css" />

<c:set var="chooseOtherMethod"><bean:message key="dss_applet_message_click_other_sign_option" bundle="generalLabels"/></c:set>
<c:set var="chooseOtherCertificate"><bean:message key="dss_applet_message_click_different_certificate" bundle="generalLabels"/></c:set>
<c:set var="cancel"><bean:message key="dss_applet_message_cancel" bundle="generalLabels"/></c:set>
<c:set var="showOtherCertificates"><bean:message key="dss_applet_show_other_certs" bundle="generalLabels"/></c:set>


        <!--Hidden labels that will be used in javascript-->
        <span id="dss_applet_message_yes" style="display:none;"><bean:message key="dss_applet_message_yes" bundle="generalLabels"/></span>
        <span id="dss_applet_message_no" style="display:none;"><bean:message key="dss_applet_message_no" bundle="generalLabels"/></span>        
        <span id="dss_applet_message_ok" style="display:none;"><bean:message key="dss_applet_message_ok" bundle="generalLabels"/></span>
        <span id="dss_applet_message_cancel" style="display:none;"><bean:message key="dss_applet_message_cancel" bundle="generalLabels"/></span>      
        <span id="dss_applet_showSurveyTitle" style="display:none;"><bean:message key="dss_applet_showSurveyTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_errorReportTitle" style="display:none;"><bean:message key="dss_applet_errorReportTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_requestPasswordTitle" style="display:none;"><bean:message key="dss_applet_requestPasswordTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_showOpenSealedPdfTitle" style="display:none;"><bean:message key="dss_applet_showOpenSealedPdfTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_showAppletDeathTitle" style="display:none;"><bean:message key="dss_applet_showAppletDeathTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_showUnsupportedVendorTitle" style="display:none;"><bean:message key="dss_applet_showUnsupportedVendorTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_appletInitialisationFailureTitle" style="display:none;"><bean:message key="dss_applet_appletInitialisationFailureTitle" bundle="generalLabels"/></span>
        <span id="dss_applet_sessionExpiredTitle" style="display:none;"><bean:message key="dss_applet_sessionExpiredTitle" bundle="generalLabels"/></span>       
        <span id="dss_applet_mwHelp1Title" style="display:none;"><bean:message key="dss_applet_mwHelp1Title" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp2Title" style="display:none;"><bean:message key="dss_applet_mwHelp2Title" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp3Title" style="display:none;"><bean:message key="dss_applet_mwHelp3Title" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp4Title" style="display:none;"><bean:message key="dss_applet_mwHelp4Title" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp5Title" style="display:none;"><bean:message key="dss_applet_mwHelp5Title" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp6Title" style="display:none;"><bean:message key="dss_applet_mwHelp6Title" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp7Title" style="display:none;"><bean:message key="dss_applet_mwHelp7Title" bundle="generalLabels"/></span>
        <span id="dss_applet_message_mw_7_upgrade_java" style="display:none;"><bean:message key="dss_applet_message_mw_7_upgrade_java" bundle="generalLabels"/></span>
        <span id="dss_applet_mwHelp8Title" style="display:none;"><bean:message key="dss_applet_mwHelp8Title" bundle="generalLabels"/></span>
        <span id="dss_applet_recommended_for_signature" style="display:none;"><bean:message key="dss_applet_recommendedForSignature" bundle="generalLabels"/></span>
        <span id="dss_applet_pin_pad_entry" style="display:none;"><bean:message key="dss_applet_pin_pad_entry" bundle="generalLabels"/></span>
        <span id="dss_applet_message_mw_2_proceed" style="display:none;"><bean:message key="dss_applet_message_mw_2_proceed" bundle="generalLabels"/></span>
        <span id="dss_applet_longOperation" style="display:none;"><bean:message key="dss_applet_message_long_operation" bundle="generalLabels"/></span>
		<span id="dss_applet_popupBlockWarn" style="display:none;"><bean:message key="dss_applet_popup_block_warn" bundle="generalLabels"/></span>
		<span id="dss_applet_show_other_certs" style="display:none;"><bean:message key="dss_applet_show_other_certs" bundle="generalLabels"/></span>
		<span id="dss_applet_message_warning_title" style="display:none;"><bean:message key="dss_applet_message_warning" bundle="generalLabels"/></span>
        <span id="dss_applet_message_unsupported_java_version" style="display:none;"><bean:message key="dss_applet_message_unsupported_java_version" bundle="generalLabels"/></span>
        <span id="dss_applet_message_bug_java_version" style="display:none;"><bean:message key="dss_applet_message_bug_java_version" bundle="generalLabels"/></span>
		<span id="dss_applet_message_bug_java_version_applet" style="display:none;"><bean:message key="dss_applet_message_bug_java_version_applet" bundle="generalLabels"/></span>
		<script type="text/javascript">
 			$(document).ready(function() {
	        	initServerCallUrl();
		    }); 
		</script>
		<c:set var="dss_applet_msg_link_nojava">
			<bean:message key="dss_applet_message_redirect_to_no_java_link" bundle="generalLabels"/>	
		</c:set>
		 <!--Prompt the user for collecting data-->
        <div id="dss_applet_user_survey" style="display: none;">
            <df:label_desc name="dss_applet_survey_desc"/>
            <br/>
            <p class="df_description_left" id="dss_applet_txt_sc_info"></p>
            <br/>
            <df:label_desc name="dss_applet_survey_issuer_desc"/>
            <p class="df_description_left">
                <input id="dss_applet_txt_sc_issuer" style="margin: 10px;" type="text" class="df_textbox" maxlength="50"/>
            </p>
            <df:label_desc name="dss_applet_survey_agree"/>
        </div>

        <!--Prompt the user to send the error report-->
        <div id="dss_applet_error_report" style="display: none;">
            <df:label_desc name="dss_applet_error_report_desc"/>
            <br/>
            <p class="df_description_left"><a id="dss_applet_show_error_report_details_link" onclick="$('#dss_applet_error_report_details').show();" class='dss_applet_link'><bean:message key="dss_applet_show_error_report_details" bundle="generalLabels"/></a>
            <div id="dss_applet_error_report_details" style="display:none;">
            	<p class="df_description_left dss_applet_error_report" id="dss_applet_txt_error_report"></p>            	
            </div>
            <br/>
            <df:label_desc name="dss_applet_error_report_agree"/>
        </div>


        <!--Prompt the user for password (PKCS11 or PKCS12)-->
        <div id="dss_applet_password" style="display: none;">
            <df:label_desc name="dss_applet_provide_password"/>
            <br/>
            <p class="df_description_left" id="dss_applet_txt_provide_password"></p>
            <br/>
            <df:label_desc name="dss_applet_txt_password_desc"/>
            <p class="df_description_left">
                <input id="dss_applet_txt_password" style="margin: 10px;" type="password" class="df_textbox" maxlength="50"/>
            </p>
            <span id="dss_applet_txt_wrong_pin" style="display:none;" class="df_description_left"><bean:message key="dss_applet_message_wrong_pin" bundle="generalLabels"/></span>
        </div>

        <!--Warn the user that he has chosen an expired or not yet valid certificate-->
        <div id="dss_applet_invalid_cert_date_dialog" style="display: none;">
            <p><bean:message key="dss_applet_cert_date_invalid" bundle="generalLabels"/></p>
        </div>


        <!--Prompt the user to open the SealedPdf-->
        <div id="dss_applet_openSealedPdf_dialog" style="display: none;">
            <p><bean:message key="dss_applet_message_prompt_openSealedPdf" bundle="generalLabels"/></p>
        </div>

        <!--Prompt the user that his session has expired and he may want to save his pdf-->
        <div id="dss_applet_saveSealedPdf_dialog" style="display: none;">
            <p><bean:message key="dss_applet_message_prompt_session_expired" bundle="generalLabels"/></p>
        </div>

        <!--Prompt the user that the applet has died and allow him to reload -->
        <div id="dss_applet_death" style="display: none;">
            <p><bean:message key="dss_applet_message_death" bundle="generalLabels"/></p>
            <span id="dss_applet_link_applet_death" onclick='rebootApplet();' class='dss_applet_link'><bean:message key="dss_applet_message_death_link" bundle="generalLabels"/></span>
        </div>
 
         <!--Prompt the user that the version is not supported -->
        <div id="dss_applet_unsupported_version" style="display: none;">
            <p><bean:message key="dss_applet_message_unsupported_java_version" bundle="generalLabels"/></p>
        </div>       
          <!--Prompt the user that the version is buggy -->
        <c:set var="dss_applet_msg_link_bug_version">
			<bean:message key="dss_applet_message_bug_java_version" bundle="generalLabels"/>	
		</c:set>
		<c:set var="dss_applet_msg_link_bug_version">
			<bean:message key="dss_applet_message_bug_java_version" bundle="generalLabels"/>	
		</c:set>
		<c:set var="dss_applet_message_click_here_to_proceed">
			<bean:message key="dss_applet_message_click_here_to_proceed" bundle="generalLabels"/>	
		</c:set>
		<c:set var="dss_applet_message_bug_java_version_help">
			<c:url value='dynform_signing_help_action.do?#dss_applet_message_bug_java_version'/>		
		</c:set>
		<c:set var="dss_applet_mac_unsafe_help">
			<c:url value='dynform_signing_help_action.do?#dss_applet_mac_unsafe'/>		
		</c:set>
		<c:set var="dss_applet_not_scriptable_help">
			<c:url value='dynform_signing_help_action.do?#dss_applet_not_scriptable'/>		
		</c:set>
		<c:set var="dss_applet_mac_unsafe">
			<bean:message key="dss_applet_mac_unsafe" bundle="generalLabels"/>	
		</c:set>
		<c:set var="dss_applet_not_scriptable">
			<bean:message key="dss_applet_not_scriptable" bundle="generalLabels"/>	
		</c:set>
        <c:set var="dss_applet_message_click_here_to_proceed_title">
          <bean:message key="dss_applet_message_click_here_to_proceed_title" bundle="generalLabels"/>  
        </c:set>
        <c:set var="dss_applet_mac_unsafe_title">
          <bean:message key="dss_applet_mac_unsafe_title" bundle="generalLabels"/>  
        </c:set>
        <c:set var="dss_applet_not_scriptable_title">
          <bean:message key="dss_applet_not_scriptable_title" bundle="generalLabels"/>  
        </c:set>
        <c:set var="dss_applet_message_click_other_sign_option_title">
          <bean:message key="dss_applet_message_click_other_sign_option_title" bundle="generalLabels"/>  
        </c:set>
        <c:set var="dss_applet_not_scriptable_title">
          <bean:message key="dss_applet_not_scriptable_title" bundle="generalLabels"/>  
        </c:set>				
     
        <!-- Middleware help dialogs-->
        <!-- Dialog 1 -->
        <div id="dss_applet_mw_1" style="display:none;">
        	<p><bean:message key="dss_applet_message_mw_1_1" bundle="generalLabels"/></p>
        	<p><bean:message key="dss_applet_message_mw_1_2" bundle="generalLabels"/>
			<span id="dss_applet_mw_1_yes" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "5");' class='dss_applet_link'><bean:message key="dss_applet_message_mw_1_yes" bundle="generalLabels"/></span>|        	
        	<span id="dss_applet_mw_1_no" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "2");' class='dss_applet_link'><bean:message key="dss_applet_message_mw_1_no" bundle="generalLabels"/></span>|
        	<span id="dss_applet_mw_1_dunno" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "3")' class='dss_applet_link'><bean:message key="dss_applet_message_mw_1_dunno" bundle="generalLabels"/></span>
        	</p>
        </div>
        <!-- Dialog to give the user the web page of the card provider -->
        <div id="dss_applet_mw_2" style="display:none;">
        	<p><bean:message key="dss_applet_message_mw_2_1" bundle="generalLabels"/></p>
        	<p><bean:message key="dss_applet_message_mw_2_2" bundle="generalLabels"/></p>
        	<p><span id="dss_applet_txt_redirect_issuer" style="display:none;"><bean:message key="dss_applet_message_mw_redirect_to_issuer" bundle="generalLabels"/></span></p>
        	<p><bean:message key="dss_applet_message_mw_2_4" bundle="generalLabels"/></p>
        	<span class="dss_applet_link"><a id="dss_applet_provider_url" href ="" onclick="closeMwDialog();downloadPdfFromServer();" style="display:none;"  target="_blank">Click here to download and continue</a></span>       	
        </div>
        
        <!-- Dialog to advise the user to look for the card software -->
        <div id="dss_applet_mw_3" style="display:none;">
        	<p><bean:message key="dss_applet_message_mw_3_1" bundle="generalLabels"/></p>
        	<p><bean:message key="dss_applet_message_mw_3_2" bundle="generalLabels"/>
			<span id="dss_applet_mw_3_yes" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "5");' class='dss_applet_link'><bean:message key="dss_applet_mw_3_yes" bundle="generalLabels"/></span>|        	
        	<span id="dss_applet_mw_3_no" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "2");' class='dss_applet_link'><bean:message key="dss_applet_mw_3_no" bundle="generalLabels"/></span>
        	</p>
        </div>
        
        <!-- Dialog to say that we cannot help -->
        <div id = "dss_applet_mw_4" style="display:none;">
        	<p><bean:message key="dss_applet_message_mw_4_1" bundle="generalLabels"/></p>
        </div>

        <!-- The pkcs11 library file dialog -->
        <div id="dss_applet_mw_5" style="display:none;">
        	<div id="dss_applet_smartcard_advice_pkcs11_jre6" style="display:none">
        		<p><bean:message key="dss_applet_file_pkcs11_desc" bundle="generalLabels"/></p>
        		<p><bean:message key="dss_applet_file_pkcs11_desc_2" bundle="generalLabels"/></p>
        		<p><span id="dss_applet_txt_pkcs11_windows" style="display:none;"><bean:message key="dss_applet_file_pkcs11_desc_windows" bundle="generalLabels"/></span></p>
        	</div>
        	<div id = "dss_applet_smartcard_advice_pkcs11_jre5" style="display:none">
        		<p><bean:message key="dss_applet_file_pkcs11_desc_jre5" bundle="generalLabels"/></p>        	
        	</div>
        	<p><span id="dss_applet_txt_pkcs11_error" style="display:none;"></span></p>        	                        
			<html:text property="dss_applet_file_pkcs11" styleClass="df_textbox" maxlength="250" styleId="dss_applet_file_pkcs11_value" readonly="true"/>
			<span class="ieborder"><html:button property="dss_applet_file_pkcs11_browse" styleClass="df_button styleButton" styleId="dss_applet_file_pkcs11_browse"><bean:message key="dss_applet_browse" bundle="generalLabels"/></html:button></span>
        </div>
        
        <!-- Dialog allowing the user to open the card provider URL -->
        <div id = "dss_applet_mw_6" style="display:none;">
        	<p><bean:message key="dss_applet_message_mw_6_1" bundle="generalLabels"/></p>        	
        </div>        


        <!-- Dialog to help upgrade Java to latest version -->
        <div id = "dss_applet_mw_7" style="display:none;">
        	<p><bean:message key="dss_applet_message_mw_7_1" bundle="generalLabels"/></p>        	
        </div> 
        <!--Prompt the user to close the page -->
        <div id="dss_applet_mw_8" style="display: none;">
            <p><bean:message key="dss_applet_message_mw_8_1" bundle="generalLabels"/></p>
        </div>
        <!--Message shown when operation takes some time.-->       
        <div id="dss_applet_loading_message" style="display: none;text-align: center;" class="dss_applet_loading_message">
        	<p id="dss_applet_loading_text1"></p>
        	<p id="dss_applet_loading_text2" style="color: red;"></p>
        	<span class="ieborder" id="dss_cancel_Loading_span"><input type="submit" value="${cancel}" id="dss_applet_cancel_loading_button" class="df_button styleButton" style="margin-bottom:10px;"></span>
        </div>
        <!--Some information to help with MOCCA -->
        <div id="dss_applet_mocca_help" style="display: none;">
            <p id="dss_applet_mocca_help_text"></p>
        </div>
	
		<!--  
			<span id="dss_applet_link_nojava"><df:linkPartText onclick="redirectToNoJavaPage();" href="#" styleClass="dss_applet_link" linkText="${dss_applet_msg_link_nojava}"/></span>                         
		-->
        <df:section name="dss_applet_section">
			<!-- div displayed when there is no Java -->
			<div id ="dss_applet_sec_download_java" style="display:none;" class="dss_applet_block">

                <div id="dss_applet_no_java">
                    <p><bean:message key="dss_applet_prompt_to_download_java" bundle="generalLabels"/></p>
					<p id="dss_applet_sec_download_java_mac_chrome" style = "display: none;"><bean:message key="dss_applet_sec_download_java_mac_chrome" bundle="generalLabels"/></p>
					<div  style="text-align:center"> 
                        <html:button titleKey="dss_applet_install_java_save_sign_online_tt" bundle="generalLabels"
                        			 property="dss_applet_install_java_save_sign_online" styleClass="df_button styleButton"
                                     styleId="dss_applet_install_java_save_sign_online"
                                     onclick="downloadPdfFromServer(); redirectToDownloadJavaPage();" >
                            <bean:message key="dss_applet_install_java_save_sign_online" bundle="generalLabels"/>
                        </html:button>

                        <html:button titleKey="dss_applet_dont_install_java_sign_offline_tt" bundle="generalLabels"
                        			 property="dss_applet_dont_install_java_sign_offline" styleClass="df_button styleButton"
                                     styleId="dss_applet_dont_install_java_sign_offline"
                                     onclick="redirectToNoJavaPage();" >
                            <bean:message key="dss_applet_dont_install_java_sign_offline" bundle="generalLabels"/>
                        </html:button>
                        <html:button titleKey="dss_applet_java_configured_tt" bundle="generalLabels"
                        			 property="dss_applet_java_configured" styleClass="df_button styleButton"
                                     styleId="dss_applet_java_configured"
                                     onclick="reloadApplet();" >
                            <bean:message key="dss_applet_java_configured" bundle="generalLabels"/>
                        </html:button>
					</div>
                </div>
			</div>
			<div id="dss_applet_bug_version" style="display: none;" class="dss_applet_block">
            	<br/>
            	<p><df:linkPartText linkText="${dss_applet_msg_link_bug_version}" href="${dss_applet_message_bug_java_version_help }"  target="_blank" styleClass="dss_applet_link"/>
            	<df:linkPartText linkText="${dss_applet_message_click_here_to_proceed}" href="#" onclick="appletGo('${requestScope.dss_sign_method.code}');" styleClass="dss_applet_link"/></p>
            	<div style="text-align:center">
					<html:button titleKey="dss_applet_btn_back" bundle="generalLabels"
	                   			 property="dss_applet_java_configured" styleClass="df_button styleButton"
	                             styleId="dss_applet_btn_back"                             
	                             onclick="redirectToNoJavaPage();" >
	                     <bean:message key="dss_applet_btn_back" bundle="generalLabels"/>
	                </html:button>
					<html:button titleKey="dss_applet_install_java" bundle="generalLabels"
	                   			 property="dss_applet_java_configured" styleClass="df_button styleButton"
	                             styleId="dss_applet_install_java"
	                             onclick="downloadPdfFromServer(); redirectToDownloadJavaPage();" >
	                     <bean:message key="dss_applet_install_java" bundle="generalLabels"/>
	                </html:button>
                </div>
                <br/>
                <br/>           
        	</div> 
        	<!-- Apple mac needs to be unsafe -->
        	<div id="dss_applet_mac_unsafe" style="display: none;" class="dss_applet_block">
            	<br/>
            	<p><df:linkPartText linkText="${dss_applet_mac_unsafe}" href="${dss_applet_mac_unsafe_help }"  target="_blank" styleClass="dss_applet_link"/></p>
            	<div  style="text-align:center">
					<html:button titleKey="dss_applet_btn_back" bundle="generalLabels"
	                   			 property="dss_applet_java_configured" styleClass="df_button styleButton"
	                             styleId="dss_applet_btn_back"                             
	                             onclick="redirectToNoJavaPage();" >
	                     <bean:message key="dss_applet_btn_back" bundle="generalLabels"/>
	                </html:button>
	            </div>
                <br/>
                <br/>           
        	</div> 
        	<!-- Tamperred communication -->
        	<div id="dss_applet_tampered_communication" style="display: none;" class="dss_applet_block">
            	<br>
				<p><bean:message key="dss_applet_tampered_communication" bundle="generalLabels"/></p>
            	<div  style="text-align:center">
                    <c:if test="${not testMode eq true }">                        
					<html:button titleKey="dss_applet_btn_back" bundle="generalLabels" 
	                   			 property="dss_applet_java_configured" styleClass="df_button_display_none styleButton"
	                             styleId="dss_applet_btn_back"                             
	                             onclick="redirectToNoJavaPage();" >
	                     <bean:message key="dss_applet_btn_back" bundle="generalLabels"/>
	                </html:button>
                    </c:if>
                    <c:if test="${testMode eq true }">                        
                    <html:button titleKey="dss_applet_btn_back" bundle="generalLabels" 
                                 property="dss_applet_java_configured" styleClass="df_button styleButton"
                                 styleId="dss_applet_btn_back"                             
                                 onclick="redirectToNoJavaPage();" >
                       <bean:message key="dss_applet_btn_back" bundle="generalLabels"/>
                    </html:button>
                    </c:if>
	            </div>
                <br>
                <br>           
        	</div>
        	<!-- Vendor needs to be oracle -->
        	<div id="dss_applet_unsupported_vendor" style="display: none;" class="dss_applet_block">
            	<br/>
            	<p><bean:message key="dss_applet_message_unsupported_vendor" bundle="generalLabels"/></p>
            	<div  style="text-align:center">
					<html:button titleKey="dss_applet_btn_back" bundle="generalLabels"
	                   			 property="dss_applet_java_configured" styleClass="df_button styleButton"
	                             styleId="dss_applet_btn_back"                             
	                             onclick="redirectToNoJavaPage();" >
	                     <bean:message key="dss_applet_btn_back" bundle="generalLabels"/>
	                </html:button>
	                <html:button titleKey="dss_applet_install_java" bundle="generalLabels"
	                   			 property="dss_applet_java_configured" styleClass="df_button styleButton"
	                             styleId="dss_applet_install_java"
	                             onclick="downloadPdfFromServer(); redirectToDownloadJavaPage();" >
	                     <bean:message key="dss_applet_install_java" bundle="generalLabels"/>
	                </html:button>
                </div>
                <br/>
                <br/>           
        	</div>        	 
			<script src="js/dynforms/dss_applet/deployjava.js" >
            </script>
            <div id="dss_applet_container"></div>
			<script type="text/javascript">
                launchApplet( '${requestScope.dss_sign_method.code}' );
            </script>
            <div id="dss_applet_sec_sign_pdf" style="display:none;">

                <!--Info message template-->
                <div id="dss_applet_message_info" class="dss_applet_message_info" style="display:none;"></div>

                <!--Success message template-->
                <div id="dss_applet_signed_ok" class="dss_applet_message_success" style="display:none;"><h3><c:out value="${successMessage}"/> </h3></div>

                <!--Success message template-->
                <div id="dss_applet_message_success" class="dss_applet_message_success" style="display:none;"></div>

                <!--Warning message template-->
                <div id="dss_applet_message_warning" class="dss_applet_message_warning" style="display:none;"></div>

                <!--Error message template-->
                <div id="dss_applet_message_error" class="dss_applet_message_error" style="display:none;"></div>

				<!-- container for the info/success/warning/error messages -->
                <div id="dss_applet_messages" style="display:none;"></div>

                <!--Part 2: Configure-->
                <div id="dss_applet_sec_config">
                <div class="dss_applet_block">
                    <!-- The method (P12 or Smartcard) -->
                    <h2><bean:message key="dss_applet_message_method_${requestScope.dss_sign_method.code}" bundle="generalLabels"/>&nbsp;<a href="<c:url value="dynform_signing_help_action.do"/>" target="_blank"  class="dss_applet_link"><img style="vertical-align: middle;" src="images/help_icon.png" alt="<bean:message key="dss_applet_lnk_help" bundle="generalLabels"/>" title="<bean:message key="dss_applet_lnk_help" bundle="generalLabels"/>"/></a></h2>
					
					<!-- Click here to select another signing option -->                    
                    <p class="df_description_left" style="display:none;" id="dss_applet_cmp_sign_method">
	                     <df:linkPartText action ="${formAction }" styleClass="dss_applet_link" linkText="${chooseOtherMethod}"/>                         
                    </p>
                    <!-- Click here to sign with a different certificate -->
                    <p class="df_description_left" style="display:none;" id="dss_applet_cmp_other_sign_cert">
                         <df:linkPartText action ="${formAction }" styleClass="dss_applet_link" onclick="dss_applet.handleUiEvent('signOtherCertificate',null); return false;" linkText="${chooseOtherCertificate}"/>
                    </p>
                    
                    <!-- PKCS12 -->
                    <div id="dss_applet_file_pkcs12" class = "dss_applet_widget" style="display:none;">
                        <df:label_desc name="dss_applet_file_pkcs12_desc"/>
                        <html:text property="dss_applet_file_pkcs12" styleClass="df_textbox" maxlength="250" styleId="dss_applet_file_pkcs12_value" readonly="true"/>
                        <span class="ieborder"><html:button titleKey="dss_applet_file_pkcs12_browse_tt" bundle="generalLabels" property="dss_applet_file_pkcs12_browse" styleClass="df_button styleButton" styleId="dss_applet_file_pkcs12_browse"><bean:message key="dss_applet_browse" bundle="generalLabels"/></html:button></span>
                    </div>
                    <!--Signing Certificate-->
                    
                    <!-- When there is only one certificate to recommend -->
                    <div id = "dss_applet_cmp_one_recommended" style="display:none;">
                    	<p><bean:message key="dss_applet_message_you_will_sign_with" bundle="generalLabels"/></p>
                    	<span><span id="dss_applet_cert_one_recommended"> &nbsp;</span> <img id="dss_applet_cert_one_recommended_summary" src="images/info_ico.png" title="" alt="" style="vertical-align: middle; display:none;"/></span>
                    	<br/>
                    </div>

					<div id="dss_applet_choose_certificate_desc" class="df_description_left" style="display:none;"><bean:message key="dss_applet_sel_certificates_desc" bundle="generalLabels"/></div>
                    
                    <!-- The recommended certificates -->
                    <div id="dss_applet_sel_certificates_recommended" class = "dss_applet_widget dss_applet_cert_list" style="display:none;">
                        <div id="dss_applet_sel_certificates_recommended_desc" style="display:none;"><p><bean:message key="dss_applet_sel_certificates_recommended_desc" bundle="generalLabels"/></p><br/></div>                        
                        <div id="dss_applet_sel_certificates_recommended_list"></div>
                    </div>
                    
                    <span id = "dss_applet_show_hidden_certs" style="display:none;"><df:linkPartText href="#" styleClass="dss_applet_link" onclick="dss_applet.handleUiEvent('showOtherCertificates',null); return false;" linkText="${showOtherCertificates}"/></span>
                    
                    <!-- The other certificates -->
                    <div id="dss_applet_sel_certificates_other" class = "dss_applet_widget dss_applet_cert_list" style="display:none;">
                        <div id="dss_applet_sel_certificates_other_desc" style="display:none;"><p><bean:message key="dss_applet_sel_certificates_other_desc" bundle="generalLabels"/></p><br/></div>                        
                        <div id="dss_applet_sel_certificates_other_no_recommend_desc" style="display:none;"><p><bean:message key="dss_applet_sel_certificates_other_no_recommend_desc" bundle="generalLabels"/></p><br/></div>
                        <div id="dss_applet_sel_certificates_other_list"></div>
                    </div>
                    
                    <!-- Warning when there are no certificates after a search of the SC/P12 -->
					<div id="dss_applet_sel_certificates_no_certs" style="display:none; color:red; padding-bottom:10px;" class="df_description_left"><bean:message key="dss_applet_sel_certificates_no_certs" bundle="generalLabels"/></div>

                    <!-- advice to the user to insert card correctly -->
                    <div id="dss_applet_smartcard_advice_no_card" style = "display:none;">
                        <p class="df_description_left">
                        	<img src="css/dss_applet/images/warning.png">&nbsp;<bean:message key="dss_applet_message_advice_no_card_detected" bundle="generalLabels"/>                        	
                        </p>
                    </div>                   
                    
                    
                    <!-- advice to the user to install software (Java 5) -->
                    <div id="dss_applet_smartcard_advice_java5" style = "display:none;">
                        <p class="df_description_left" >
                        	<bean:message key="dss_applet_message_advice_jre5_upgrade" bundle="generalLabels"/>&nbsp;<a class="dss_applet_link" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "7")'><bean:message key="dss_applet_message_advice_jre5_upgrade_link" bundle="generalLabels"/></a>                       
                        </p>
                        <p class="df_description_left">
                        	<bean:message key="dss_applet_message_advice_jre5_pkcs11" bundle="generalLabels"/>&nbsp;<a class="dss_applet_link" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "1")'><bean:message key="dss_applet_message_advice_jre5_pkcs11_link" bundle="generalLabels"/></a>
                        </p>
                    </div>
                    
                    <!-- The selected certificate -->
                    <span id="dss_applet_sel_certificates_value" style="display:none;"></span>
                    <span id="dss_applet_sel_certificates_date_valid" style="display:none;"></span>
					<!-- Link to open smart card help dialogs -->
					<span  id="dss_applet_smartcard_advice_java6" class="ieborder" style="line-height: 3em; display:none;">
					<span><a id="dss_applet_smartcard_advice_java6_link" class="dss_applet_link" onclick='dss_applet.handleUiEvent("showMiddlewareHelp", "1")'><bean:message key="dss_applet_message_advice_jre6_link" bundle="generalLabels"/></a></span>
					</span>					
					<br>                    
                    <!-- Read the card again button -->
                    <span class="ieborder">
					<html:button titleKey="dss_applet_btn_refresh_tt" bundle="generalLabels" style="display:none;" property="dss_applet_btn_refresh" styleClass="df_button styleButton" styleId="dss_applet_btn_refresh"><bean:message key="dss_applet_refresh" bundle="generalLabels"/></html:button>
					</span>
					
				
					<span id="dss_applet_smartcard_advice_mscapi" style = "display:none;"><bean:message key="dss_applet_message_use_os_cert_store" bundle="generalLabels"/></span>							
                </div>
                </div>
				<div id = "dss_applet_sec_sign" style = "display:block;">
                <!--Part 2: Sign-->
                <div class="dss_applet_block">                    
                    <!-- Sign button -->
                    <div id="dss_applet_btn_sign" class = "dss_applet_widget" style="display:none;">
                        <df:label_desc name="dss_applet_btn_sign_desc"/>
                        <c:choose>
							<c:when test="${testMode eq true }">                        
                        		<span class="ieborder dss_applet_sign"><html:button titleKey="dss_applet_btn_sign_input_tt" bundle="generalLabels" property="dss_applet_btn_sign_input" styleClass="df_button styleButton" styleId="dss_applet_btn_sign_input"><bean:message key="dss_applet_sign_test" bundle="generalLabels"/></html:button></span>
                        	</c:when>
                        	<c:otherwise>
                        		<span class="ieborder dss_applet_sign"><html:button titleKey="dss_applet_btn_sign_input_tt" bundle="generalLabels" property="dss_applet_btn_sign_input" styleClass="df_button styleButton" styleId="dss_applet_btn_sign_input"><bean:message key="dss_applet_sign" bundle="generalLabels"/></html:button></span>
                        	</c:otherwise>
                        </c:choose>
                    </div>                    
                    <!-- Download signed PDF -->
                    <div id="dss_applet_file_download_signed" class = "dss_applet_widget" style="display:none;">
                        <df:label_desc name="dss_applet_file_download_signed_desc"/>
                        <span class="ieborder"><html:button titleKey="dss_applet_file_download_signed_input_tt" bundle="generalLabels" property="dss_applet_file_download_signed_input" styleClass="df_button styleButton" styleId="dss_applet_file_download_signed_input"><bean:message key="dss_applet_download_pdf" bundle="generalLabels"/></html:button></span>
                    </div>
                </div>
                </div>
            </div>
        </df:section>

               
        <p class="br">&nbsp;</p>

        <!--Hidden control to switch for no java form-->
        <p style="display: none;"><input type="submit" id="dss_applet_forward_nojava" name="dss_applet_forward_nojava"/></p>

        <!--Hidden control to send EXPIRED_SESSION with the current form-->
        <p style="display: none;"><input type="submit" id="dss_applet_expired_session" name="dss_applet_expired_session"/></p>

        <!--Hidden control to download PDF from server-->
        <p style="display: none;"><input type="submit" id="dss_applet_btn_download_input" name="dss_applet_btn_download_input"/></p>
        <p style="display: none;"><input type="submit" id="dss_applet_btn_reload_applet" name="dss_applet_btn_reload_applet"/></p>

		

        <!-- The selected signing method -->
		<html:hidden property="signingMethod" styleId="dss_selected_applet_signing_method"/>
		               
        <!-- Hidden label for the server call URL used by javascript ajax -->
        <c:choose>
			<c:when test="${testMode eq true }">
				<c:set var="ajaxAction" value="dynform_signing_testpage_ajax_action.do"/>
	 		</c:when>
	 		<c:otherwise>
	 			<c:set var="ajaxAction" value="dynform_signing_ajax_action.do"/>
	 		</c:otherwise>
		</c:choose>     
 		
        <span id="dss_applet_servercall_url" style="display:none;"><c:url value="${ajaxAction}"/></span>
 		