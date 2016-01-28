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
package eu.europa.ejusticeportal.dss.applet.model.service;

import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.common.KeepAliveEvent;
import eu.europa.ejusticeportal.dss.common.ServerCallId;

/**
 * 
 * Fires a logging event to prevent session time out.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SessionKeepAliveHome {

    private static SessionKeepAliveHome instance = new SessionKeepAliveHome();
    //The period that we can wait between subsequent calls without doing the keep alive
    private long period = 5*60*1000;
    
    private long lastTime = System.currentTimeMillis();
    /**
     * Get the singleton instance of the class
     * 
     * @return the instance
     */
    public static SessionKeepAliveHome getInstance() {
        return instance;
    }

    /**
     * Start the heart beat
     */
    public void keepAlive() {
        if ((System.currentTimeMillis() - period) > lastTime ) {
            lastTime = System.currentTimeMillis();
            AsynchronousCallerHome
                    .getInstance()
                    .getCaller()
                    .fire(new ServerCall(AsynchronousServerCall.callServer, ServerCallId.logStatistics,
                            new KeepAliveEvent()));
        }
        
    }
}
