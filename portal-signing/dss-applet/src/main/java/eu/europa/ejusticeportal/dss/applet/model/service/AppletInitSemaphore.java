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
 * This singleton is used to know if the asynchronous call to the server that was fired at the initialisation has been
 * properly handled by the applet.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class AppletInitSemaphore {

    /**
     * Singleton reference of AppletInitSemaphore.
     */
    private static volatile AppletInitSemaphore instance;

    /**
     * Constructs singleton instance of AppletInitSemaphore.
     */
    private AppletInitSemaphore() {
    }

    /**
     * Provides reference to singleton AppletInitSemaphore of AppletInitSemaphore.
     *
     * @return Singleton instance of AppletInitSemaphore.
     */
    public static AppletInitSemaphore getInstance() {
        if (instance == null) {
            instance = new AppletInitSemaphore();
        }
        return instance;
    }

    private volatile boolean messageReady = false;
    private volatile boolean sealedPdfReady = false;
    private volatile boolean signingContextReady = false;
    private volatile boolean shuttingDown = false;

    /**
     * @param messageReady set if the MessageBundle is ready to use by the applet.
     */
    public void setMessageReady(boolean messageReady) {
        this.messageReady = messageReady;
    }

    /**
     * @param sealedPdfReady set if the SealedPdf is ready to use by the applet.
     */
    public void setSealedPdfReady(boolean sealedPdfReady) {
        this.sealedPdfReady = sealedPdfReady;
    }

    /**
     * @param signingContextReady set if a SigningContext is ready to use by the applet.
     */
    public void setSigningContextReady(boolean signingContextReady) {
        this.signingContextReady = signingContextReady;
    }

    /**
     * @return true, if all properties are ready or if one has had an error.
     *         false, otherwise
     */
    public boolean isReady() {
        return (this.messageReady && this.sealedPdfReady && this.signingContextReady);
    }

    /**
     * Indicate that the applet is shutting down.
     */
    public void setShuttingDown(){
        shuttingDown = true;
    }
    /**
     * 
     * @return true if the applet is in the process of shutting down.
     */
    public boolean isShuttingDown(){
        return shuttingDown;
    }

    /**
     * @return the signingContextReady
     */
    public boolean isSigningContextReady() {
        return signingContextReady;
    }
}
