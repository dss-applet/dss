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


import eu.europa.ejusticeportal.dss.applet.controller.EventHandler;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.SessionExpired;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.applet.model.service.PDFHome;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Handle the callback of the server call. he role of the callback handler is to do something with the response
 * that we get from the server (the server, via the enclosing HTML page, calls back to the applet asynchronously.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class CallbackHandler extends EventHandler<ServerCallId> {

    private static final DssLogger LOG = DssLogger.getLogger(CallbackHandler.class.getSimpleName());
    private String xml;
    private String errCode;
    
    private static Map<ServerCallId, CallbackHandlerDelegate> handlerMapping = new HashMap<ServerCallId, CallbackHandlerDelegate>();
    //map of all CallbackHandlerDelegates that are able to process the response from the server
    static {
        handlerMapping.put(ServerCallId.getSigningContext, new GetSigningContextCbHandler());
        handlerMapping.put(ServerCallId.getSealedPdf, new GetSealedPdfCbHandler());
        handlerMapping.put(ServerCallId.getMessageBundle, new GetMessagesCbHandler());
        handlerMapping.put(ServerCallId.uploadSignedPdf, new UploadSignedPdfCbHandler());
        handlerMapping.put(ServerCallId.logStatistics, new LogStatisticsCbHandler());
        handlerMapping.put(ServerCallId.uploadAppletLog, new LogStatisticsCbHandler());
    }

    /**
     * Constructor of CallbackHandler.
     *
     * @param xml     the xml data send by the server.
     * @param errCode the errCode send by the server.
     */
    public CallbackHandler(String xml, String errCode) {
        this.xml = xml;
        this.errCode = errCode;
    }

    /**
     * Constructor of CallbackHandler.
     * @param xml     the xml data send by the server.
     */
    public CallbackHandler(String xml) {
        this.xml = xml;
        this.errCode = null;
    }
    /**
     * Execute the handler for the specified serverCallback.
     *
     * @param serverCall the serverCall
     */
    @Override
    public void doHandle(ServerCallId serverCall) throws CodeException {

        if (errCode != null) {            
            handleError();
            return;
        }
        
        CallbackHandlerDelegate h = handlerMapping.get(serverCall);
        if (h== null){
            ExceptionUtils.exception(new UnexpectedException(
                    "The callback is not implemented for server call named '" + serverCall + "'"), LOG);            
        } else {
            LOG.log(Level.FINE, "Server provides: {0}", new Object[]{h.getLabel()});
            h.doHandle(xml);
        }
        //tell the view that the operation is done (switch off the page loading). 
        //not done when the event is part of another.
        if (!serverCall.isEnclosed() && AppletInitSemaphore.getInstance().isSigningContextReady()){           
            Event.getInstance().fire(new LoadingRefreshed(false, false));
        }
    }

    /**
     * Handles an error reported by the callback.
     */
    private void handleError(){
        LOG.log(Level.INFO, "The server callback contained an error {0}.", errCode);
        MessagesEnum ec = MessagesEnum.valueOf(errCode);
        if (!AppletInitSemaphore.getInstance().isShuttingDown()){
            //avoid showing the error message during shutdown, which is usually due to unreliable javascript communication
            //as the page unloads. The message would only briefly flash on the screen, so not useful.
            Event.getInstance().fire(new StatusRefreshed(ec, MessageLevel.ERROR));
        }
        if (ec.equals(MessagesEnum.dss_applet_message_session_expired)) {
            if (PDFHome.getInstance().sealedPdfReceived()) {
                /*
                 * The prompt is async in the browser, that means that the
                 * other action (saveSealedPdf, redirectSessionExpired) will
                 * be triggered by the javascript prompt itself.
                 */
                Event.getInstance().fire(new SessionExpired(false));
            } else {
                // The user can't save the sealed PDF because the applet doesn't have it in memory.
                Event.getInstance().fire(new SessionExpired(true));
            }
        }
        Event.getInstance().fire(new LoadingRefreshed(false, true));
    }

}
