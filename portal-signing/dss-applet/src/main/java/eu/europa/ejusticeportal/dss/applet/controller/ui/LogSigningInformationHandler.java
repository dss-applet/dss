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
package eu.europa.ejusticeportal.dss.applet.controller.ui;

import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SignatureInformationHome;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * Handles the event that requires the details of how the user signed to be logged.
 *
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1705 $ - $Date: 2014-04-25 20:09:21 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class LogSigningInformationHandler implements UIEventHandlerDelegate {

    private static final LogSigningInformationHandler INSTANCE = new LogSigningInformationHandler();
    
    /**
     * 
     * The default constructor for LogSigningInformationHandler.
     */
    private LogSigningInformationHandler (){
        
    }
    /**
     * Handle the event
     * 
     * @param e the event to handle
     * @param arg the argument of the event
     */
    public void doHandle(UIEvent e, String arg) throws CodeException {
        
        if (e.equals(UIEvent.agreeLogSigningInformation)){
            //we can use the card provider and the library path
            SignatureInformationHome.getInstance().getSignatureEvent().setUserSuppliedCardIssuer(arg);            
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_user_survey_thanks, MessageLevel.INFO));
        } else {
            //refuse, so we do not take the library path or the card provider. Everything
            //else was already provided to the server so we can log it.
            SignatureInformationHome.getInstance().getSignatureEvent().setUserSuppliedPkcs11Path("");
            SignatureInformationHome.getInstance().getSignatureEvent().setUserSuppliedCardIssuer("");
        }
        AsynchronousCallerHome.getInstance().getCaller().fire(
                new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics, SignatureInformationHome.getInstance()
                .getSignatureEvent()));
        SignatureInformationHome.getInstance().reset();
        Event.getInstance().fire(new LoadingRefreshed(false, false));  
    }

    /**
     * Gets the Singleton instance of LogSigningInformationHandler
     * @return the instance
     */
    public static LogSigningInformationHandler getInstance() {
        return INSTANCE;
    }

}
