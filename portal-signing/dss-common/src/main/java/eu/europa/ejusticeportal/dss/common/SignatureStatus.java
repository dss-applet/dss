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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * Gives the status of validation checks performed on the digital signature by
 * the signing service
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 1747 $ - $Date: 2012-12-14 14:08:16 +0100 (Fri, 14 Dec
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SignatureStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public boolean originatedInPortal;
    public boolean signedByUser;
    public boolean tamperedAfterSignature;
    public boolean untrustedSignature;
    public boolean revoked;
    public boolean inWorkflow;

    public List<MessagesEnum> warningStatusCodes = new ArrayList<MessagesEnum>();
    public List<MessagesEnum> exceptionStatusCodes = new ArrayList<MessagesEnum>();

    public boolean validFormat;

    /**
     * Sets the status codes
     *
     * @param codes the codes
     */
    public void setWarningStatusCodes(List<MessagesEnum> codes) {
        this.warningStatusCodes = codes;
    }

    /**
     * Gets the status codes
     *
     * @return the codes
     */
    public List<MessagesEnum> getWarningStatusCodes() {
        return Collections.unmodifiableList(warningStatusCodes);
    }

    /**
     * @return the exceptionStatusCodes
     */
    public List<MessagesEnum> getExceptionStatusCodes() {
        return exceptionStatusCodes;
    }

    /**
     * @param exceptionStatusCodes the exceptionStatusCodes to set
     */
    public void setExceptionStatusCodes(List<MessagesEnum> exceptionStatusCodes) {
        this.exceptionStatusCodes = exceptionStatusCodes;
    }

    /**
     * Gets the warning status codes as a list of String value.
     *
     * @return the codes
     */
    public List<String> getWarningStatusCodesAsString() {
        List<String> strStatusCodes = new ArrayList<String>();
        for (MessagesEnum m : getWarningStatusCodes()) {
            strStatusCodes.add(m.name());
        }
        return Collections.unmodifiableList(strStatusCodes);
    }

    /**
     * Gets the exception status codes as a list of String value.
     *
     * @return the codes
     */
    public List<String> getExceptionStatusCodesAsString() {
        List<String> strStatusCodes = new ArrayList<String>();
        for (MessagesEnum m : getExceptionStatusCodes()) {
            strStatusCodes.add(m.name());
        }
        return Collections.unmodifiableList(strStatusCodes);
    }

    /**
     * Check if the overall status is an exception
     *
     * @return true if the overall status is exception
     */
    public boolean isException() {
        return !exceptionStatusCodes.isEmpty();
    }

    /**
     * Check if the overall status is a warning
     *
     * @return true if the overall status is warning
     */
    public boolean isWarning() {
        return exceptionStatusCodes.isEmpty() && !warningStatusCodes.isEmpty();
    }

    /**
     * Checks if there were any warnings.
     *
     * @return true if there were no warnings.
     */
    public boolean isClean() {
        return exceptionStatusCodes.isEmpty() && warningStatusCodes.isEmpty();
    }

    /**
     * Adds a warning code
     *
     * @param code the code to add
     */
    public void addWarningStatusCode(MessagesEnum code) {
        if (!warningStatusCodes.contains(code)){
            warningStatusCodes.add(code);
        }
    }

    /**
     * Adds an exception code
     *
     * @param code the code to add
     */
    public void addExceptionStatusCode(MessagesEnum code) {
        if (!exceptionStatusCodes.contains(code)) {
            exceptionStatusCodes.add(code);
        }
    }

    /**
     * @return true if the file originated in the portal
     */
    public boolean isOriginatedInPortal() {
        return originatedInPortal;
    }

    /**
     * @param originatedInPortal true if the file originated in the portal
     */
    public void setOriginatedInPortal(boolean originatedInPortal) {
        this.originatedInPortal = originatedInPortal;
    }

    /**
     * @return the true if the document was signed (by the user)
     */
    public boolean isSignedByUser() {
        return signedByUser;
    }

    /**
     * @param signedByUser true if the document was signed (by the user)
     */
    public void setSignedByUser(boolean signedByUser) {
        this.signedByUser = signedByUser;
    }

    /**
     * @return true if the document was tampered after signature
     */
    public boolean isTamperedAfterSignature() {
        return tamperedAfterSignature;
    }

    /**
     * @param true if the document was tampered after signature
     */
    public void setTamperedAfterSignature(boolean tamperedAfterSignature) {
        this.tamperedAfterSignature = tamperedAfterSignature;
    }

    /**
     * @return true if the signature was linked to a trusted authority
     */
    public boolean isUntrustedSignature() {
        return untrustedSignature;
    }

    /**
     * @param true if the signature was not linked to a trusted authority
     */
    public void setUntrustedSignature(boolean untrustedSignature) {
        this.untrustedSignature = untrustedSignature;
    }

    /**
     * @return true if the certificate used to sign the document is definitely revoked
     */
    public boolean isRevoked() {
        return revoked;
    }

    /**
     * @param true if the certificate used to sign the document is definitely revoked
     */
    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    /**
     * @return true if the document belongs to the current workflow
     */
    public boolean isInWorkflow() {
        return inWorkflow;
    }

    /**
     * @param true if the document belongs to the current workflow
     */
    public void setInWorkflow(boolean inWorkflow) {
        this.inWorkflow = inWorkflow;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SignatureStatus [originatedInPortal=" + originatedInPortal + ", signedByUser=" + signedByUser
                + ", tamperedAfterSignature=" + tamperedAfterSignature + ", untrustedSignature="
                + untrustedSignature + ", revoked=" + revoked + ", inWorkflow=" + inWorkflow
                + ", warningStatusCodes=" + warningStatusCodes + ", exceptionStatusCodes=" + exceptionStatusCodes
                + "]";
    }

    /**
     * Test if the format of the signature was valid
     * @param true if valid
     */
	public void setValidFormat(boolean validFormat) {
		this.validFormat = validFormat;
		
	}

	/**
	 * 
	 * @return true if the format of the signature was good
	 */
	public boolean isValidFormat() {
		return validFormat;
	}
	
	
    
}
