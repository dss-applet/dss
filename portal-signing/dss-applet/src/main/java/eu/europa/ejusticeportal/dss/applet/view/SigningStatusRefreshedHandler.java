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
package eu.europa.ejusticeportal.dss.applet.view;
import eu.europa.ejusticeportal.dss.applet.event.PdfSignedOk;
import eu.europa.ejusticeportal.dss.applet.event.SigningStatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
/**
 * 
 * Handles the UI event to update the status messages with the outcome of the signature validation.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1705 $ - $Date: 2014-04-25 20:09:21 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SigningStatusRefreshedHandler implements UIEventListenerDelegate{
    /**
     * Handle the event
     * @param event the event to handle
     */
    public void doHandle(Object event) {
        SigningStatusRefreshed e = (SigningStatusRefreshed)event;
        if (!UIState.isReadyToSign()) {
        	//this can happen if for example the user pulled the smart card out after the signature but before the callback
        	UI.updateUiStatus(MessageLevel.INFO, MessagesEnum.dss_applet_operation_cancelled, null);
        	return;
        }
        if (!e.getStatus().isException()) {
            Event.getInstance().fire(new PdfSignedOk());
        }
        if (e.getStatus().isClean()) {
            UIState.transitionSigned();
        } else if (e.getStatus().isWarning()) {
            MessagesEnum[] arrayStatusCode = new MessagesEnum[e.getStatus().getWarningStatusCodes().size()];
            UI.updateUiStatus(MessageLevel.WARNING, MessagesEnum.dss_applet_message_signature_status_warning, e
                    .getStatus().getWarningStatusCodes().toArray(arrayStatusCode));
            UIState.transitionSigned();
        } else if (e.getStatus().isException()) {
            SignatureInformationHome.getInstance().getSignatureEvent().setNeedsUserInput(false);
            SignatureInformationHome.getInstance().getSignatureEvent().setSigned(false);
            SignatureInformationHome.getInstance().getSignatureEvent().setErrorDescription(e.getStatus().getExceptionStatusCodesAsString());
            MessagesEnum[] arrayStatusCode = new MessagesEnum[e.getStatus().getExceptionStatusCodes().size()];
            UI.updateUiStatus(MessageLevel.ERROR, MessagesEnum.dss_applet_message_signature_status_error, e
                    .getStatus().getExceptionStatusCodes().toArray(arrayStatusCode));
            AsynchronousCallerHome.getInstance().getCaller().fire(
                    new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics, SignatureInformationHome.getInstance()
                    .getSignatureEvent()));
            SignatureInformationHome.getInstance().reset();
        }
        UI.updateUI(); 
    }

}
