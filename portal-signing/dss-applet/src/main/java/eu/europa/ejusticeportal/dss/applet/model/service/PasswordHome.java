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

/**
 * 
 * Manages PKCS11 and PKCS12 passwords
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class PasswordHome {

    private static volatile PasswordHome instance;
    private AsynchronousPasswordInputCallback passwordInputCallback;
    /**
     * Constructor for PasswordHome
     */
    private PasswordHome(){
        
    }
    /**
     * Get the static instance
     * @return the instance
     */
    public static PasswordHome getInstance(){
        if (instance == null){
            instance = new PasswordHome();
        }
        return instance;
    }

    /**
     * Gets the currently active PasswordInputCallback
     * @return the passwordInputCallback
     */
    public AsynchronousPasswordInputCallback getPasswordInputCallback() {
        return passwordInputCallback;
    }

    /**
     * Sets the currently active PasswordInputCallback
     * @param passwordInputCallback the passwordInputCallback to set
     */
    public void setPasswordInputCallback(AsynchronousPasswordInputCallback passwordInputCallback) {
        this.passwordInputCallback = passwordInputCallback;
    }
      
    /**
     * Reset the password input state
     */
    public void reset(){
        if (passwordInputCallback != null){
            passwordInputCallback.reset();
        }
    }
    /**
     * @return
     */
    public boolean wasCancelled() {

        return passwordInputCallback!=null && passwordInputCallback.wasCancelled();
    }    
}
