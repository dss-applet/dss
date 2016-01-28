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

import eu.europa.ec.markt.dss.signature.token.PasswordInputCallback;

/**
 * 
 * Interface to allow a password input to be collected from a different thread.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public interface AsynchronousPasswordInputCallback extends PasswordInputCallback{

    /**
     * Set if the password input was cancelled
     * @param cancelled true if cancelled
     */
    void setCancelled(boolean cancelled);
    
    /**
     * Allows to check if the password input was cancelled.
     * @return true if the password input was cancelled.
     */
    boolean wasCancelled();
    /**
     * Sets the password
     * @param password the password to set
     */
    void setPassword(char [] password);

    /**
     * Set if the wrong PIN was entered
     * @param b true if the wrong pin was entered
     */
    void setWrongPin(boolean b);
    
    /**
     * Allows to check if the wrong PIN was entered
     * @return true if the wrong PIN was entered
     */
    boolean wasWrongPin();
    
    /**
     * Return true if the password dialog is currently visible
     * @return true if visible, false if not
     */
    boolean isVisible();
    
    /**
     * Reset the flags
     */
    void reset();
}
