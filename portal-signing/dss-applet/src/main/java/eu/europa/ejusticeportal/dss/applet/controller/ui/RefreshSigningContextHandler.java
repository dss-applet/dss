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

import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.action.RefreshSigningContextAction;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * Handles the event that requires the signing context to refresh (the refresh of the certificates).
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class RefreshSigningContextHandler implements UIEventHandlerDelegate {

    private static final RefreshSigningContextHandler INSTANCE = new RefreshSigningContextHandler();

    /**
     * 
     * The default constructor for RefreshSigningContextHandler.
     */
    private RefreshSigningContextHandler() {

    }

    /**
     * Handle the event
     * 
     * @param e the event to handle
     * @param arg the argument of the event
     */
    public void doHandle(UIEvent e, String arg) throws CodeException {

        new RefreshSigningContextAction().exec();

        Event.getInstance().fire(
                new StatusRefreshed(MessagesEnum.dss_applet_message_certs_refreshed, MessageLevel.INFO));
    }

    /**
     * Gets the Singleton instance of this class.
     * 
     * @return
     */
    public static RefreshSigningContextHandler getInstance() {
        return INSTANCE;
    }

}
