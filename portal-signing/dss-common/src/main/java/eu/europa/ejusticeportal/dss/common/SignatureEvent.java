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
import java.util.List;

/**
 * 
 * Information collected in the event of an attempt to sign a document.
 * The information is supposed to be useful to enhance the {@Link CardProfileRepository}
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SignatureEvent extends DssEvent implements Serializable{
    
	private static final long serialVersionUID = 1L;
	public String signingMethod;
	public String signatureAlgorithm;
	public String digestAlgorithm;
	public String api;
	public String atr;
	public String userSuppliedPkcs11Path;
	public String userSuppliedCardIssuer;
	public boolean signed;
	public int detectedCardCount;
	public String keyUsage;
	public String issuerDN;
	public String extensions;
	public String recommended;
	public boolean needsUserInput;

	public String errorDescription;

    /**
     * 
     * The constructor for SignatureLogEntry.
     * 
     * @param os the operating system name
     * @param arch the architecture (32/64/unknown)
     * @param jreVersion the jre version
     */
    public SignatureEvent(String os, String arch, double jreVersion) {
        super(DssEventType.SG.name(), os, arch, jreVersion);
    }

    /**
     * 
     * The default constructor for SignatureLogEntry.
     */
    public SignatureEvent() {
        super(DssEventType.SG.name());
    }

    /**
     * The constructor for SignatureEvent.
     * @param signingContext the signingcontext
     */
    public SignatureEvent(SigningContext signingContext) {
        super(DssEventType.SG.name());
        if (signingContext != null){
            setArch(signingContext.getArchitecture().getName());
            setOs(signingContext.getOs().getName());
            setJreVersion(signingContext.getJreVersion());

        }
    }

    /**
     * @return true if the user can enhance the information
     */
    public boolean isNeedsUserInput() {
        return needsUserInput;
    }

    /**
     * @param needsUserInput true if the user can enhance the information
     */
    public void setNeedsUserInput(boolean needsUserInput) {
        this.needsUserInput = needsUserInput;
    }

    /**
     * @return the signingMethod
     */
    public String getSigningMethod() {
        return signingMethod;
    }

    /**
     * @param signingMethod the signingMethod to set
     */
    public void setSigningMethod(String signingMethod) {
        this.signingMethod = signingMethod;
    }

    /**
     * @return the signatureAlgorithm
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * @param signatureAlgorithm the signatureAlgorithm to set
     */
    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * @return the digestAlgorithm
     */
    public String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /**
     * @param digestAlgorithm the digestAlgorithm to set
     */
    public void setDigestAlgorithm(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * @return the userSuppliedPkcs11Path
     */
    public String getUserSuppliedPkcs11Path() {
        return userSuppliedPkcs11Path;
    }

    /**
     * @param userSuppliedPkcs11Path the userSuppliedPkcs11Path to set
     */
    public void setUserSuppliedPkcs11Path(String userSuppliedPkcs11Path) {
        this.userSuppliedPkcs11Path = userSuppliedPkcs11Path;
    }

    /**
     * @return the userSuppliedCardIssuer
     */
    public String getUserSuppliedCardIssuer() {
        return userSuppliedCardIssuer;
    }

    /**
     * @param userSuppliedCardIssuer the userSuppliedCardIssuer to set
     */
    public void setUserSuppliedCardIssuer(String userSuppliedCardIssuer) {
        this.userSuppliedCardIssuer = userSuppliedCardIssuer;
    }

    /**
     * @return the signed
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * @param signed the signed to set
     */
    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    /**
     * @return the errorDescription
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    /**
     * Get the KeyUsage of the certificate used to sign
     * @return the keyUsage
     */
    public String getKeyUsage() {
        return keyUsage;
    }

    /**
     * Set the keyUsage of the certificate used to sign
     * @param keyUsage the keyUsage
     */
    public void setKeyUsage(String keyUsage) {
        this.keyUsage = keyUsage;
    }

    /**
     * @param errorDescription the errorDescription to set
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    /**
     * @return the api
     */
    public String getApi() {
        return api;
    }

    /**
     * @param api the api to set
     */
    public void setApi(String api) {
        this.api = api;
    }

    /**
     * @return the atr
     */
    public String getAtr() {
        return atr;
    }

    /**
     * @param atr the atr to set
     */
    public void setAtr(String atr) {
        this.atr = atr;
    }

    /**
     * @return the number of cards detected. If more than one, the statistics regarding signature are not 100% reliable.
     */
    public int getDetectedCardCount() {
        return detectedCardCount;
    }

    /**
     * @param detectedCardCount the number of cards detected.
     */
    public void setDetectedCardCount(int detectedCardCount) {
        this.detectedCardCount = detectedCardCount;
    }

    /**
     * Sets the error description from a list of strings
     * @param exceptionStatusCodes the list of strings
     */
    public void setErrorDescription(List<String> exceptionStatusCodes) {
        
        if (exceptionStatusCodes!=null){
            StringBuilder s = new StringBuilder("");
            for (String code:exceptionStatusCodes){
                s.append(code).append(" ");
            }
            setErrorDescription(s.toString());
        }        
    }

    /**
     * @return the name of the certificate issuer
     */
    public String getIssuerDN() {
        return issuerDN;
    }

    /**
     * 
     * @param issuerDN the name of the issuer to set
     */
    public void setIssuerDN(String issuerDN) {
        this.issuerDN = issuerDN;
    }

    /**
     * @return the extensions
     */
    public String getExtensions() {
        return extensions;
    }

    /**
     * @param extensions the extensions to set
     */
    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    /**
     * @return the recommended
     */
    public String getRecommended() {
        return recommended;
    }

    /**
     * @param recommended the recommended to set
     */
    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    @Override
    public String toString() {
        return "SignatureEvent [signingMethod=" + signingMethod + ", signatureAlgorithm=" + signatureAlgorithm
                + ", digestAlgorithm=" + digestAlgorithm + ", api=" + api + ", atr=" + atr
                + ", userSuppliedPkcs11Path=" + userSuppliedPkcs11Path + ", userSuppliedCardIssuer="
                + userSuppliedCardIssuer + ", signed=" + signed + ", detectedCardCount=" + detectedCardCount
                + ", keyUsage=" + keyUsage + ", issuerDN=" + issuerDN + ", extensions=" + extensions + ", recommended="
                + recommended + ", needsUserInput=" + needsUserInput + ", errorDescription=" + errorDescription + "]";
    }



}
