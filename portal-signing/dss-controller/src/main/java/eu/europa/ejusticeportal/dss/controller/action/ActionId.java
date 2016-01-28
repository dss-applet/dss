/*******************************************************************************
 * Digital Signature Applet
 * 
 *  Copyright (C) 2014 European Commission, Directorate-General for Justice (DG  JUSTICE), B-1049 Bruxelles/Brussel
 * 
 *  Developed by: ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg)  
 * 
 *  http://www.arhs-developments.com
 * 
 *  This file is part of the "Digital Signature Applet" project.
 * 
 *  Licensed under the EUPL, version 1.1 or – as soon they are approved by the European  Commission - subsequent versions of the EUPL (the "Licence"). 
 *  You may not use this  work except in compliance with the Licence. You may obtain a copy of the Licence at:
 * 
 *  http://ec.europa.eu/idabc/eupl.html
 * 
 *  Unless required by applicable law or agreed to in writing, software distributed under   the Licence is distributed on  
 *  an "AS IS" basis, WITHOUT WARRANTIES OR   CONDITIONS OF ANY KIND, either  express or implied. 
 * 
 *  See the Licence for the  specific language governing permissions and limitations under the Licence.
 ******************************************************************************/
package eu.europa.ejusticeportal.dss.controller.action;

import eu.europa.ejusticeportal.dss.common.ServerCallId;

/**
 *
 * IDs  for actions
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public enum ActionId {

    /**
     * id for the signature context action
     */
    AJAX_GET_SIGNATURE_CONTEXT(ServerCallId.getSigningContext.name()),
    /**
     * id for the digest action
     */
    AJAX_GET_SEALED_PDF(ServerCallId.getSealedPdf.name()),
    /**
     * id for the document signing action
     */
    AJAX_SIGN_DOC(ServerCallId.uploadSignedPdf.name()),
    /**
     * id for the error message action
     */
    AJAX_GET_LOCALISED_MESSAGE(ServerCallId.getMessageBundle.name()),
    /**
     * id for download of PDF (sealed)
     */
    DOWNLOAD_PDF("dss_applet_btn_download_input"),
    /**
     * id for download of PDF (sealed)
     */
    DOWNLOAD_SIGNED_PDF("dss_applet_btn_download_signed"),

    /**
     * id for reload of applet
     */
    RELOAD_APPLET("dss_applet_btn_reload_applet"),
    /**
     * id for download of PDF (sealed)
     */
    DOWNLOAD_PDF_NO_JAVA("dss_applet_btn_download_input_ol"),
    /**
     * id for upload of PDF (signed)
     */
    UPLOAD_PDF("dss_applet_btn_upload_signed_input"),
    /**
     * id for upload of PDF (signed) - no Java
     */
    UPLOAD_PDF_NO_JAVA("dss_applet_btn_upload_signed_input_ol"),

    /**
     * id for the redirection if applet is not allowed.
     */
    FORWARD_NOJAVA("dss_applet_forward_nojava"),
    /**
     * id to stay on no java page.
     */
    STAY_NOJAVA("dss_applet_forward_nojava_ol"),
    /**
     * id to send the statistical data and log it
     */
    AJAX_LOG_STATISTIC(ServerCallId.logStatistics.name()),
    /**
     * id to send the applet event log and log it
     */
    AJAX_LOG_APPLET_EVENTS(ServerCallId.uploadAppletLog.name()),
    /**
     * The name of the parameter for the AJAX request type
     */
    AJAX_REQUEST_ID("id"),
    /**
     * The extension of the parameter for the no java form.
     */

    /**
     * id for expired session in an ajax call
     */
    EXPIRED_SESSION("dss_applet_expired_session"),

    /**
     * id for Next
     */
    NEXT("dss_applet_next"),

    /**
     * id for Other action
     */
    OTHER("OTHER"),

    /**
     * id for expired session in Ajax call from applet
     */
    EXPIRED_SESSION_AJAX("expired_session_ajax"),
    
    /**
     * id for select signing method
     */
    SELECT_SIGN_METHOD("dss_applet_choose_sign_method"),
    
    /**
     * id to show the test page
     */
    TEST_PAGE("dss_applet_test_page");
    
    
    private final String name;

    ActionId(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the Id
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the actionId from the name
     * @param name the name
     * @return the ActionId
     */
    public static ActionId fromName(String name) {
        for (ActionId id : ActionId.values()) {
            if (id.getName().equals(name)) {
                return id;
            }
        }
        throw new IllegalArgumentException(name + " is not a valid name");
    }

}
