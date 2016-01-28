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
/**
 * Manage the UIEvent.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum UIEvent {

    /**
     * The user click on the browse button to provide a driver for PKCS11.
     */
    selectPKCS11File ("Select a PKCS11 file", EventCharacteristic.LOCAL),
    /**
     * The user click on the browse button to provide a certificate file P12.
     */
    selectPKCS12File("Select a PKCS12 certificate file", EventCharacteristic.LOCAL),
    /**
     * The user click on the sign button.
     */
    signDocument ("Sign the PDF with the applet",EventCharacteristic.LONG),
    /**
     * The user click on the refresh button.
     */
    refreshSigningContext("Refresh the signing context",EventCharacteristic.LONG),
    /**
     * The user click on the download signed Pdf button.
     */
    saveSignedPdf("Save the signed PDF to the local computer", EventCharacteristic.LOCAL),
    /**
     * The user click on the view Pdf button.
     */
    openSealedPdf("View the sealed PDF", EventCharacteristic.LOCAL),
    /**
     * When prompted, the user accepts to view the PDF before signing it.
     */
    viewPdfBeforeSigning("Agree to view the sealed PDF before signing",EventCharacteristic.ASYNC, EventCharacteristic.LOCAL),
    /**
     * Pre: User sees that his session has expired. The user agree to save the Sealed Pdf for further processing.
     */
    saveSealedPdf("Save the sealed PDF after session expiry", EventCharacteristic.LOCAL),
    /**
     * The user has already signed the form, but he changes the selected certificate.
     */
    prepareToSignWithOtherCertificate ("Sign the document with another certificate",EventCharacteristic.LONG),
    /**
     * The user agrees to provide information about the signature.
     */
    agreeLogSigningInformation("Agree to provide the signing parameters for unknown card",EventCharacteristic.ASYNC),
    
    /**
     * The user does not agree to provide information about the signature.
     */
    refuseLogSigningInformation("Refuse to provide signing parameters for unknown card",EventCharacteristic.ASYNC),
    
    /** 
     * Agree to provide the error report 
     */
    agreeSendErrorReport("Agree to send the error report",EventCharacteristic.ASYNC),
    /** 
     * Refuse to provide the error report 
     */
    refuseSendErrorReport("Do not agree to send the error report",EventCharacteristic.ASYNC),
    
    /** 
     * Provide a PKCS11 or PKCS12 password 
     */
    providePassword("Provide PKCS11 or PKCS12 password",EventCharacteristic.ASYNC, EventCharacteristic.LOCAL),
    /** 
     * Refuse to provide a PKCS11 or PKCS12 password 
     */
    refusePassword("Cancel PKCS11 or PKCS12 password dialog",EventCharacteristic.ASYNC, EventCharacteristic.LOCAL),
       
    /**
     * We need to upload the applet log
     */
    uploadAppletLog ("Upload the applet event log",EventCharacteristic.BACKGROUND),
    
    /**
     * Show the first step of middleware help
     */
    showMiddlewareHelp("Show a middleware help dialog",EventCharacteristic.ASYNC, EventCharacteristic.LOCAL),
    
    /**
     * Sign with another certificate (after having signed)
     */
    signOtherCertificate("Sign with another certificate after having signed",EventCharacteristic.LONG),
    
    /**
     * Show other certificats
     */
    showOtherCertificates("Show the other certificates on the computer",EventCharacteristic.BACKGROUND, EventCharacteristic.LOCAL);
    ;
    
    
    private EventCharacteristic [] characteristics;
    private String label;
    /**
     * The default constructor for UIEvent.
     * @label a descriptive label for the event, for logging.
     * @characteristics characteristics to assign to the event
     */
    private UIEvent(String label,EventCharacteristic ... characteristics) {
        this.label = label;
        this.characteristics = characteristics;
    }

    /**
     * Checks if the event can take a long time to complete
     * @return true if it takes a long time
     */
    public boolean isLong(){
        return isCharacteristic(EventCharacteristic.LONG);
    }
    /**
     * Checks if the event is local (not involving a server request)
     * @return true if it is local
     */
    public boolean isLocal(){
        return isCharacteristic(EventCharacteristic.LOCAL);
    }
    /**
     * Get the label for the event
     * @return
     */
    public String getLabel(){
        return label;
    }
    /**
     * Checks if the event is asynchronous
     * 
     * @return true if it is asynchronous
     */
    public boolean isAsync() {
        return isCharacteristic(EventCharacteristic.ASYNC);
    }
    /**
     * Checks if the event is background
     * 
     * @return true if it is background
     */
    public boolean isBackground() {
        return isCharacteristic(EventCharacteristic.BACKGROUND);
    }
    
    private boolean isCharacteristic(EventCharacteristic ec){
        boolean b = false;
        if (characteristics==null){
            b = false;
        } else {
            for (EventCharacteristic c: characteristics){
                if (c.equals(ec)){
                    b = true;
                    break;
                }
            }
        }
        return b;
    }
    /**
     * Characteristics of a UIEvent
     */
    enum EventCharacteristic {
        /**The event is done in the background (practically, we don't show the loading overlay)*/
        BACKGROUND,
        /**The event is asynchronous with user interaction (practically, we don't hide the loading overlay)*/
        ASYNC,
        /**The event can take some time to complete (practically, we show a message in the loading overlay*/
        LONG,
        /**The event does not require a server request*/
        LOCAL
    }
}
