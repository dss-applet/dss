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
import eu.europa.ejusticeportal.dss.applet.event.MessagesReady;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * Handle the callback of the get messages - Save the Message Bundle.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class GetMessagesCbHandler extends AbstractCbHandler {
    
    
    /**
     * Handle the callback
     * @param the callback XML
     */
    public void doHandle(String xml) throws CodeException {
        MessageBundleHome bundleHome = MessageBundleHome.getInstance();
        synchronized (bundleHome) {
            bundleHome.init((MessageBundle) fromString(xml));
            bundleHome.notifyAll();
        }
        Event.getInstance().fire(new MessagesReady());
        AppletInitSemaphore.getInstance().setMessageReady(true);
    }
    
    /**
     * Get the label for the handler
     */
    public String getLabel(){
        return "Translated messages and labels";
    }
}
