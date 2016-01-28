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

/**
 * Enumerates the list of JavaScript function implemented in the HTML pages that are called from the Applet.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public enum UIFunction {

    /**
     * Init JavaScript UI event (jQuery).
     */
    initCommonJava,
    /**
     * Update UI elements.
     */
    updateUI,
    /**
     * Redirect user on No Java form.
     */
    redirectToNoJavaPage,
    /**
     * Prompt the user that his session has expired and he may want to download
     * the Sealed Pdf.
     */
    promptSessionExpired,
    /**
     * Redirect the user to the session expired page.
     */
    redirectSessionExpired,
    /**
     * Set the visibility of the loading image.
     */
    setLoading,
    /**
     * Show the user survey for collecting statistical data about the signature.
     */
    showSurvey,
    /**
     * Request a PIN/password from the user
     */
    requestPassword, 
    
    /**
     * Show dialog allowing the user to provide the stack trace of a signature error
     */
    showErrorReport,
    
    /**
     * Reloads the applet - needed for some cards that block PKCS11 after wrong PIN/cancelled PIN.
     */
    reloadApplet,
    
    /**
     * Upload the Applet log.
     */
    uploadAppletLog, 
    
    /**
     * Applet initialisation failed
     */
    appletInitialisationFailure, 
    
    /**
     * Prompt the user to view the PDF before signing it.
     */
    showOpenSealedPdfPrompt, 
    
    /**
     * Show a middleware help dialog
     */
    showMiddlewareHelp, 
    /**
     * Closes the middleware dialog, if it is open
     */
    closeMwDialog,
    
    /**
     * Opens the card provider website in a new window/tab
     */
    openCardInfoUrl, 
    
    /**Show some information about MOCCA pin pad*/
    showMoccaPinPadAdvice, 
    
    /**Close the password dialog should it be open*/
    closePasswordDialog,
    
    /**Set the selected certificate*/
    certificateSelected,
    /**Close the applet due to tampered communication*/
    closeForTamperedCommunication
    ;
    
}
