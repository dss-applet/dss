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
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.util.logging.Level;
/**
 * 
 * Handles the UI event turning on or off the indication that the applet is busy.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class LoadingRefreshedHandler implements UIEventListenerDelegate{
    
    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(LoadingRefreshedHandler.class.getSimpleName());
    /**
     * Handle the event
     * @param event the event to handle
     */
    public void doHandle(Object event) {
        /*
         * Show/Hide the spinning icon (refresh the loading status).
         */
        LoadingRefreshed e = (LoadingRefreshed) event;        
        LOG.log(Level.FINE,"Handling Loading Refresh loading:{0}, error:{1}, message {2}", new Object[]{e.isLoading(),e.isError(), e.getMessage1(), e.getMessage2()});
        UI.setLoading(e.isLoading(), e.isError(), e.getMessage1(), e.getMessage2());
    }

}
