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
package eu.europa.ejusticeportal.dss.applet.controller.cb;

import eu.europa.ejusticeportal.dss.applet.event.SigningStatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.UserSurvey;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * Handle the callback of the get messages - Upload the signed Pdf to the server..
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1705 $ - $Date: 2014-04-25 20:09:21 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class UploadSignedPdfCbHandler extends AbstractCbHandler {

    /**
     * Handle the callback
     * 
     * @param the callback XML
     */
    public void doHandle(String xml) throws CodeException {
        SignatureStatus signStatus = (SignatureStatus) fromString(xml);
        Event.getInstance().fire(new SigningStatusRefreshed(signStatus));
        SignatureEvent si = SignatureInformationHome.getInstance().getSignatureEvent();
        if (si.isNeedsUserInput()) {
            Event.getInstance().fire(new UserSurvey(si));
        } else {
            AsynchronousCallerHome.getInstance().getCaller().fire(
                    new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics, SignatureInformationHome.getInstance()
                    .getSignatureEvent()));
            SignatureInformationHome.getInstance().reset();
        }
    }
    
    /**
     * Get the label for the handler
     */
    public String getLabel(){
        return "Signed PDF receipt";
    }
}
