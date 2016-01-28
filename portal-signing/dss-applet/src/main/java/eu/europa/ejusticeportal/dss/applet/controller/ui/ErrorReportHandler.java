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
 * Handles the event that requires the error report to be logged, or not.
 *
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1705 $ - $Date: 2014-04-25 20:09:21 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class ErrorReportHandler implements UIEventHandlerDelegate {

    private static final ErrorReportHandler INSTANCE = new ErrorReportHandler();
    
    /**
     * 
     * The default constructor for ErrorReportHandler.
     */
    private ErrorReportHandler (){
        
    }
    /**
     * Handle the event
     * 
     * @param e the event to handle
     * @param arg the argument of the event
     */
    public void doHandle(UIEvent e, String arg) throws CodeException {
        
        if (e.equals(UIEvent.agreeSendErrorReport) && arg!=null){
            //we can use the stack trace given in the argument
            SignatureInformationHome.getInstance().getSignatureEvent().setErrorDescription(arg);            
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_error_report_thanks, MessageLevel.INFO));
        }
        //log the error in all cases - the description will be the class name or code if the user has not agreed
        //to give the stack
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
    public static ErrorReportHandler getInstance() {
        return INSTANCE;
    }

}
