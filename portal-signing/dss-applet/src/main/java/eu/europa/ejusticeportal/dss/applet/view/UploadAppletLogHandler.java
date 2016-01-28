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

import eu.europa.ejusticeportal.dss.Build;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.AppletCloseEvent;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.InMemoryHandler;

import java.util.logging.Level;

/**
 * 
 * Handles the UI event stopping the applet.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1705 $ - $Date: 2014-04-25 20:09:21 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class UploadAppletLogHandler implements UIEventListenerDelegate{    
    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(UploadAppletLogHandler.class.getSimpleName());
    /**
     * Handle the event
     * @param event the event to handle
     */
    public void doHandle(Object event) {
        LOG.log(Level.INFO, "Uploading log for applet build version {0} {1}", new Object[]{Build.getBuildVersion(),Build.getBuildTimestamp()});
        //send the log to the server
        String log = InMemoryHandler.getInstance().getLog();
        AsynchronousCallerHome.getInstance().getCaller().fire(
                new ServerCall(AsynchronousServerCall.callServer, ServerCallId.uploadAppletLog, new AppletCloseEvent(log)));        
    }

}
