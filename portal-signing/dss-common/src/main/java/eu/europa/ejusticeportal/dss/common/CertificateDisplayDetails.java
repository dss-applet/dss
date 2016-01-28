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
 * $HeadURL: https://forge.aris-lux.lan/svn/dgjustice-dss/trunk/portal-signing/dss-common/src/main/java/eu/europa/ejusticeportal/dss/common/CertificateDisplayName.java $
 * $Revision: 1559 $
 * $Date: 2013-12-12 18:29:21 +0100 (Thu, 12 Dec 2013) $
 * $Author: MacFarPe $
 */
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * Information to help with the display of a certificate
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1559 $ - $Date: 2013-12-12 18:29:21 +0100 (Thu, 12 Dec 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class CertificateDisplayDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String subjectDN;
    private final String serial;
    private boolean recommended;
    private final String certificateHash;
    private final boolean qualified;
    private final String issuerName;
    private boolean[] keyUsage;
    private final X509Certificate certificate;
    private final CardProfile cardProfile;

    private String expirationDate;
    private String startDate;
    private boolean dateValid;
    private String summaryInfo;
    private String issuerCountry;
    private String extensions;
    private boolean sscd;
    /**
     * 
     * The constructor for CertificateDisplayDetails.
     * 
     * @param subjectDN the subject name
     * @param issuerName the issuer of the certificate
     * @param issuerCountry the country of the issuer 
     * @param serial the serial number
     * @param certificateHash a hash of the certificate
     * @param qualified true if the certificate looks like it could be qualified, using only conditions in the certificate
     * @param sscd true if the certificate came from a secure signature creation device, using only conditions in the certificate.
     * @param keyUsage the {@link KeyUsage} flags from the certificate
     * @param extensions the extensions for the certificate
     */
    public CertificateDisplayDetails(String subjectDN, String issuerName, String issuerCountry, String serial, String certificateHash,boolean qualified, boolean sscd, boolean[] keyUsage,
            X509Certificate certificate, CardProfile cp, String extensions) {
        this.subjectDN = subjectDN;
        this.serial = serial;
        this.certificateHash = certificateHash;
        this.qualified = qualified;
        this.sscd = sscd;
        this.issuerName = issuerName;
        this.keyUsage = keyUsage;
        this.certificate = certificate;
        this.cardProfile = cp;
        this.issuerCountry = issuerCountry;
        this.extensions = extensions;
    }

    /**
     * Gets the name of the certificate
     * 
     * @return the name
     */
    public String getName() {
        return subjectDN;
    }


    /**
     * Gets the certificate hash
     * 
     * @return the certificate hash
     */
    public String getCertificateHash() {
        return certificateHash;
    }


    /**
     * Test if the certificate looks like it could be qualified
     * 
     * @return true if the flag is set
     */
    public boolean isQualified() {
        return qualified;
    }


    /**
     * @return the hide
     */


    /**
     * @return the recommended
     */
    public boolean isRecommended() {
        return recommended;
    }

    /**
     * @return the issuerDN
     */
    public String getIssuerDN() {
        return issuerName;
    }

    /**
     * @param recommended the recommended to set
     */
    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    /**
     * @return the keyUsage
     */
    public boolean[] getKeyUsage() {
        return keyUsage;
    }


    /**
     * @return the certificate
     */
    public X509Certificate getCertificate() {
        return certificate;
    }

    /**
     * @return the cardProfile
     */
    public CardProfile getCardProfile() {
        return cardProfile;
    }

    /**
     * Flags that the certificate is valid, based on the not before/not after dates
     * @param valid true if valid
     */
    public void setDateValid(boolean valid) {
        this.dateValid = valid;        
    }

    /**
     * @return the expirationDate
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the dateValid
     */
    public boolean isDateValid() {
        return dateValid;
    }
    
    
    /**
     * Sets the summary information about the certificate
     * @param summaryInfo the information
     */
    public void setSummaryInfo(String summaryInfo) {
        this.summaryInfo = summaryInfo;
        
    }
    /**
     * Gets the summary info about the certificate
     * @return
     */
    public String getSummaryInfo() {
        return this.summaryInfo;
    }

    /**
     * @return the issuerCountry
     */
    public String getIssuerCountry() {
        return issuerCountry;
    }

    /**
     * @param issuerCountry the issuerCountry to set
     */
    public void setIssuerCountry(String issuerCountry) {
        this.issuerCountry = issuerCountry;
    }

    /**
     * @return the extensions
     */
    public String getExtensions() {
        return extensions;
    }

    /**
     * @return the sscd
     */
    public boolean isSscd() {
        return sscd;
    }

    /**
     * @param sscd the sscd to set
     */
    public void setSscd(boolean sscd) {
        this.sscd = sscd;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CertificateDisplayDetails [subjectDN=" + subjectDN + ", serial=" + serial + ", recommended="
                + recommended +  ", certificateHash=" + certificateHash + ", qualified=" + qualified
                + ", issuerName=" + issuerName + ", keyUsage=" + Arrays.toString(keyUsage) + ", cardProfile="
                + cardProfile + ", expirationDate=" + expirationDate + ", startDate=" + startDate + ", dateValid="
                + dateValid + ", summaryInfo=" + summaryInfo + ", issuerCountry=" + issuerCountry + ", extensions="
                + extensions + ", sscd=" + sscd + "]";
    }
    
    
}
