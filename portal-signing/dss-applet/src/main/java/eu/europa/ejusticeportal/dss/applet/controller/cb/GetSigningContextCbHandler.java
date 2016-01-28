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

import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.action.RefreshTokensAction;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.util.logging.Level;

/**
 * 
 * Handle the callback of the signing context - Update the tokens with the given Signing Context.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1842 $ - $Date: 2015-03-12 10:09:51 +0100 (Thu, 12 Mar 2015) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class GetSigningContextCbHandler extends AbstractCbHandler {
    
    private static final DssLogger LOG = DssLogger.getLogger(GetSigningContextCbHandler.class.getSimpleName());
    /**
     * Handle the callback
     * @param xml the callback XML
     */
    public void doHandle(String xml) throws CodeException {
        LOG.log(Level.FINE,"Signing context \n{0}",xml);
        MessageBundleHome.waitForMessages();
        SigningContextHome.getInstance().init((SigningContext) fromString(xml));
        AppletSigningMethod appletSigningMethod = SigningMethodHome.getInstance().getSigningMethod();
        //Refresh token actions for both applet signature methods smartCard and installedCertificate
        //Display a waiting message only for the installedCertificate method.
        if(appletSigningMethod!=null) {
            switch (appletSigningMethod) {
                case installed_cert:
                    Event.getInstance().fire(new LoadingRefreshed(true, false, "",
                            MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_message_insert_card.name())));
                case sc:
                    new RefreshTokensAction(SigningContextHome.getInstance().getSigningContext()).exec();
                default:
            }
        }
        AppletInitSemaphore.getInstance().setSigningContextReady(true);
    }
    
    /**
     * Get the label for the handler
     */
    public String getLabel(){
        return "Signing context";
    }
}
