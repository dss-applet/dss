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
package eu.europa.ejusticeportal.dss.common;

/**
 * Enumeration of the signing methods supported by the DSS Applet.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public enum AppletSigningMethod {
    /**
     * Sign with a smartcard 
     */
    sc(true),
    
    /**
     * (for Windows) Sign with a certificate installed in the OS
     */
    installed_cert(true) ,
    /**
     * Sign with a certificate file P12
     */
    p12(false);
 
    private boolean warnAboutPassword;
    /**
     * 
     * The constructor for SigningMethod.
     * @param true if we should warn about password dialog being hidden by the browser
     */
    private AppletSigningMethod(boolean warnAboutPassword){
    	this.warnAboutPassword = warnAboutPassword;
    }

    /**
     * Test if we must warn the user that the password dialog could be obstructed by the browser
     * @return true if we should warn
     */
	public boolean isWarnAboutPassword() {
		return warnAboutPassword;
	}

}
