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
import eu.europa.ejusticeportal.dss.applet.common.UIControllerHome;
import eu.europa.ejusticeportal.dss.applet.event.SessionExpired;
/**
 * 
 * Handles the UI event for session expiration, either redirecting to a new page or 
 * first allowing some action from the applet.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SessionExpiredHandler implements UIEventListenerDelegate{

    /**
     * Handle the event
     * @param event the event to handle
     */
    public void doHandle(Object event) {
        /*
         * Inform the user that his session has expired and prompt him to save his form.
         */
        SessionExpired e = (SessionExpired) event;
        if (e.isRedirect()) {
        	UIControllerHome.getInstance().getUiController().eval(UIFunction.redirectSessionExpired);
        } else {
        	UIControllerHome.getInstance().getUiController().eval(UIFunction.promptSessionExpired);
        } 
    }

}
