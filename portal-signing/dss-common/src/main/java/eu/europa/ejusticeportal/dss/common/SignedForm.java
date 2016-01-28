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
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;

/**
 * A wrapper containing the signature status, signed pdf, and possibly a detached signature
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SignedForm implements Serializable {
        
    private static final long serialVersionUID = 1L;
    public SignatureStatus signatureStatus;
    public byte [] document;
    public byte [] detachedSignature;
    
    /**
     * 
     * The default constructor for SignedForm.
     */
    public SignedForm(){
        
    }
    
    /**
     * 
     * The constructor for SignedForm.
     * @param status the status of the signature
     * @param document the PDF without signature
     * @param detachedSignature the detached signature
     */
    public SignedForm(SignatureStatus status, byte [] document, byte [] detachedSignature) {
        this.signatureStatus = status;
        this.document = document;
        this.detachedSignature = detachedSignature;
    }
    
    /** 
    * The constructor for SignedForm.
    * @param status the status of the signature
    * @param document the PDF with embedded signature
    */
   public SignedForm(SignatureStatus status, byte [] document) {
       this (status, document, null);
   }
    
    /**
     * @return the signature Status summary object
     */
    public SignatureStatus getSignatureStatus() {
        return signatureStatus;
    }
    /**
     * Set the signature status summary object
     * @param signatureStatus the status
     */
    public void setSignatureStatus(SignatureStatus signatureStatus) {
        this.signatureStatus = signatureStatus;
    }
    /**
     * Get the document. This is either the signed+sealed PDF 
     * (if the signature is not detached) or the sealed PDF (if the signature is detached)
     * @return the document
     */
    public byte[] getDocument() {
        return document;
    }
    /**
     * Set the document. This is either the signed+sealed PDF 
     * (if the signature is not detached) or the sealed PDF (if the signature is detached)
     * @param document the document
     */
    public void setDocument(byte[] document) {
        this.document = document;
    }
    /**
     * @return the detached Signature
     */
    public byte[] getDetachedSignature() {
        return detachedSignature;
    }
    /**
     * @param detachedSignature the detached Signature
     */
    public void setDetachedSignature(byte[] detachedSignature) {
        this.detachedSignature = detachedSignature;
    }
    /**
     * @return true if the signature is detached.
     */
    public boolean isDetachedSignature() {
        return detachedSignature != null;
    }
    

}
