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
package eu.europa.ejusticeportal.dss.applet.controller.ui.filter;

import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEvent;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEventFilter;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.action.ReloadApplet;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * This filter reloads the applet whenever the signing context is refreshed on a user action and the password entry has
 * been cancelled.
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class ReloadAppletSigningContextFilter implements UIEventFilter {

    /**
     * Apply the filter
     * 
     * @param event the event
     * @param arg the event argument
     */
    public boolean apply(UIEvent event, String arg) throws CodeException {
        if (event.equals(UIEvent.refreshSigningContext)
                && PasswordHome.getInstance().getPasswordInputCallback().wasCancelled()) {
            TokenManager.getInstance().closeTokenConnections();            
            Event.getInstance().fire(new ReloadApplet());
            //do not propagate this event
            return false;
        }
        
        return true;
    }
}
