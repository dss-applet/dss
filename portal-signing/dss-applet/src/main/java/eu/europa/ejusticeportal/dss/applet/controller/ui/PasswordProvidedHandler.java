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

import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;

/**
 * 
 * Handles the event that a password has been provided or refused
 *
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1705 $ - $Date: 2014-04-25 20:09:21 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class PasswordProvidedHandler implements UIEventHandlerDelegate {
    
    private static final PasswordProvidedHandler INSTANCE = new PasswordProvidedHandler();
    
    /**
     * The default constructor for PasswordProvidedHandler.
     */
    private PasswordProvidedHandler() {
        
    }

    /**
     * Handle the event
     * 
     * @param e the event to handle
     * @param arg the argument of the event
     */
    public void doHandle(UIEvent e, String arg) throws CodeException {
        synchronized (PasswordHome.getInstance().getPasswordInputCallback()) {        	
        	PasswordHome.getInstance().getPasswordInputCallback().setCancelled(e.equals(UIEvent.refusePassword));
            PasswordHome.getInstance().getPasswordInputCallback().setPassword(arg==null?null:arg.toCharArray());
            //notify PasswordInputDialog
            PasswordHome.getInstance().getPasswordInputCallback().notifyAll();
        }
    }

    /**
     * Gets the Singleton instance of the class
     * @return the instance
     */
    public static PasswordProvidedHandler getInstance() {
        return INSTANCE;
    }

}
