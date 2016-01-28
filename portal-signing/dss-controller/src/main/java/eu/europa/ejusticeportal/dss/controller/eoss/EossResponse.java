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
/*
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/isammp/isamm-pd/trunk/app/buildtools/src/main/resources/eclipse/isamm-pd-java-code-template.xml $
 * $Revision: 6522 $
 * $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * $Author: naramsda $
 */
package eu.europa.ejusticeportal.dss.controller.eoss;

import eu.europa.ejusticeportal.dss.common.SignatureStatus;

import java.io.Serializable;

/**
 * Response from an External online signature service
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class EossResponse implements Serializable {
    
    
    private static final long serialVersionUID = 1L;
    private String errorCode;
    private String errorMessage;
    private String signature;
    private boolean parseError;
    private SignatureStatus signatureStatus;
    private boolean timeout;
    private boolean cancelled;
    
    /**
     * 
     * @return true if waiting for a response
     */
    public boolean isWaiting(){
        return !(timeout||parseError||isCancelled()||signatureStatus!=null);
    }
    /**
     * Was the response a cancellation?
     * @return true if the user cancelled out of the signature.
     */
    public boolean isCancelled(){
        return cancelled;
    }


    /**
     * Get the error code if it exists
     * @return the errorCode or null
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Get the error message
     * @return the errorMessage or nukk
     */
    public String getErrorMessage() {
        return errorMessage;
    }


    /**
     * Set the error message
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    /**
     * Get the signature
     * @return the signature 
     */
    public String getSignature() {
        return signature;
    }


    /**
     * Set the signature
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }


    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * @return the parseError
     */
    public boolean isParseError() {
        return parseError;
    }


    /**
     * @param parseError the parseError to set
     */
    public void setParseError(boolean parseError) {
        this.parseError = parseError;
    }



    /**
     * Set the signature status
     * @param signatureStatus the status
     */
    public void setSignatureStatus(SignatureStatus signatureStatus) {
        this.signatureStatus = signatureStatus;
    }


    /**
     * @return the signatureStatus
     */
    public SignatureStatus getSignatureStatus() {
        return signatureStatus;
    }


    /**
     * @return the timeout
     */
    public boolean isTimeout() {
        return timeout;
    }


    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
    /**
     * @param cancelled the cancelled to set
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
        
    /**
     * 
     * @return true if it is ok to continue the process (signature received, but possibly with errors.
     */
    public boolean isOkToContinue(){
        return signatureStatus != null && (signatureStatus.isClean()||signatureStatus.isWarning());
    }
}
