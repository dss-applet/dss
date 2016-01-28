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
package eu.europa.ejusticeportal.dss.applet.model.token;

import eu.europa.ec.markt.dss.OID;
import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.validation102853.CertificatePool;
import eu.europa.ec.markt.dss.validation102853.CertificateToken;
import eu.europa.ec.markt.dss.validation102853.certificate.CertificateSourceType;
import eu.europa.ec.markt.dss.validation102853.condition.Condition;
import eu.europa.ec.markt.dss.validation102853.condition.QcStatementCondition;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;
import eu.europa.ejusticeportal.dss.common.KeyUsage;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.x509.qualified.ETSIQCObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Filters a given list of certificates to decide which to recommend or hide
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class CertificateDisplayUtils {

    private static final Condition id_etsi_qcp_public = new QcStatementCondition(OID.id_etsi_qcp_public);
    private static final Condition id_etsi_qcp_public_with_sscd = new QcStatementCondition(
            OID.id_etsi_qcp_public_with_sscd);
    private static final Condition id_etsi_qcs_QcCompliance = new QcStatementCondition(ETSIQCObjectIdentifiers.id_etsi_qcs_QcCompliance);
    private static final Condition id_etsi_qcs_QcSSCD = new QcStatementCondition(ETSIQCObjectIdentifiers.id_etsi_qcs_QcSSCD);

    private static final DssLogger LOG = DssLogger.getLogger(CertificateDisplayUtils.class.getSimpleName());
    private static final CertificateDisplayDetailsComparator comparator = new CertificateDisplayDetailsComparator();
    private static final CertificatePool certificatePool = new CertificatePool();
    
    private CertificateDisplayUtils (){
        
    }
    /**
     * Get the information from the certificate to allow it to be displayed in human readable form.
     * 
     * @param keyEntry the DSSPrivateKeyEntry
     * @return the CertificateDisplayName
     */
    public static CertificateDisplayDetails getDisplayDetails(DSSPrivateKeyEntry keyEntry, CardProfile cp) {
        final X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
        String subjectDN = cert.getSubjectDN().getName();
        Map<String,String> parts = parseLdapName(subjectDN);
        if (parts.get("CN")!=null){
            subjectDN = parts.get("CN");
        }
        String issuerDN = cert.getIssuerX500Principal() == null ? "" : cert.getIssuerX500Principal().getName();
        
        parts = parseLdapName(issuerDN);
        String issuerCountry = parts.get("C")==null?"":parts.get("C");
        
        String issuerName = parts.get("CN") == null?"":parts.get("CN");
        if (parts.get("O")!=null) {
            issuerName += ", " + parts.get("O");
        }
        String serialNumber = formatSerialNumber(cert.getSerialNumber());
        CertificateDisplayDetails cdd = new CertificateDisplayDetails(subjectDN, issuerName,issuerCountry,
                serialNumber, digest(cert), qualified(cert),sscd(cert), cert.getKeyUsage(), cert, cp, extensions(cert));
        //check the expiration/start date
        valid(cdd);
        cdd.setSummaryInfo(summaryInfo(subjectDN,issuerName,issuerCountry,serialNumber,cdd.getStartDate(), cdd.getExpirationDate()));
        return cdd;
    }

    private static boolean sscd(X509Certificate cert) {        
        CertificateToken certificateToken = certificatePool.getInstance(cert, CertificateSourceType.OTHER);
        return id_etsi_qcp_public_with_sscd.check(certificateToken)|| id_etsi_qcs_QcSSCD.check(certificateToken);
    }

    private static String summaryInfo(String subject, String issuer, String issuerCountry, String serial, String validFrom, String validTo) {
        StringBuilder sb = new StringBuilder();
        String lsep = "<br/>";
        String sep = ": ";
        sb.append(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_certificate_summary_issued_to.name()));
        sb.append(sep);
        sb.append(subject);        
        sb.append(lsep);
        sb.append(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_certificate_summary_issued_by.name()));
        sb.append(sep);
        sb.append(issuer);
        if (issuerCountry!=null && issuerCountry.length()!=0){
            sb.append(" (").append(issuerCountry).append(")");
        }
        sb.append(lsep);
        sb.append(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_certificate_summary_serial_no.name()));
        sb.append(sep);
        sb.append(serial);
        sb.append(lsep);
        sb.append(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_certificate_summary_valid_from.name()));
        sb.append(sep);
        sb.append(validFrom);
        sb.append(lsep);
        sb.append(MessageBundleHome.getInstance().getMessage(MessagesEnum.dss_applet_certificate_summary_valid_to.name()));
        sb.append(sep);
        sb.append(validTo);
        sb.append(lsep);
        return sb.toString();
    }

    /**
     * Test if the certificate could be qualified
     * 
     * @param cert the certificate to test
     * @return true if it could be qualified
     */
    private static boolean qualified(X509Certificate cert) {
        CertificateToken certificateToken = certificatePool.getInstance(cert, CertificateSourceType.OTHER);
        return  id_etsi_qcp_public.check(certificateToken)|| id_etsi_qcs_QcCompliance.check(certificateToken);
    }

    private static String extensions(X509Certificate cert) {
        CertificateToken certificateToken = certificatePool.getInstance(cert, CertificateSourceType.OTHER);
        StringBuilder extensions = new StringBuilder();
        if (id_etsi_qcp_public.check(certificateToken)){
            extensions.append(" etsi_qcp_public");
        }
        if (id_etsi_qcp_public_with_sscd.check(certificateToken)) {
            extensions.append(" etsi_qcp_public_with_sscd");
        }
        if (id_etsi_qcs_QcCompliance.check(certificateToken)){
            extensions.append(" etsi_qcs_QcCompliance");
        }
        if (id_etsi_qcs_QcSSCD.check(certificateToken)){
            extensions.append(" etsi_qcs_QcSSCD");
        }
        return extensions.toString();
    }
    /**
     * Prepares the {@link List} of {@link CertificateDisplayDetails}. This means: set the "recommended" certificates,
     * set the serial number in the display if needed, hide certificates
     * 
     * @param certs
     */
    public static void prepare(List<CertificateDisplayDetails> certs, AppletSigningMethod sm) {

        //hide/show based on signing method and certificate extensions
        recommend(certs, sm);


        // sort to have recommended certificates before not recommended
        Collections.sort(certs, comparator);
    }

    /**
     * Set the dateValid flag on the certificates
     * @param certs the {@link List} of {@link CertificateDisplayDetails}
     */
    private static void valid(CertificateDisplayDetails cert) {
        
            try {
                cert.setStartDate(Utils.formatDate(cert.getCertificate().getNotBefore()));
                cert.setExpirationDate(Utils.formatDate(cert.getCertificate().getNotAfter()));
                cert.getCertificate().checkValidity();
                cert.setDateValid(true);
            } catch (CertificateExpiredException e) {
                cert.setDateValid(false);
            } catch (CertificateNotYetValidException e) {
                cert.setDateValid(false);
            }
        
    }


    /**
     * Decide whether to recommend this certificate based on the {@link KeyUsage} flags only
     * 
     * @param keyUsage the {@link KeyUsage} flags to analyse
     * @return true if the certificate could be recommended
     */
    private static boolean isNonRepudiation(boolean[] keyUsage) {
        boolean recommended = false;
        if (keyUsage != null) {
            List<KeyUsage> keyUsages = KeyUsage.decode(keyUsage);
            if (keyUsages.contains(KeyUsage.nonRepudiation)) {
                recommended = true;
            }
        }
        return recommended;
    }

    private static String formatSerialNumber(BigInteger bi) {
        if (bi == null) {
            return "";
        }

        String sn = bi.toString(16);
        char[] chars = sn.toUpperCase().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
            if ((i + 1) % 2 == 0 && i < chars.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * Calculate a digest of the signing certificate. This is used to get a unique id for it.
     * 
     * @param cert the certificate to digest
     * @return the digest (SHA1, encoded as hex)
     */
    public static String digest(X509Certificate cert) {
        String digest = null;
        try {
            MessageDigest sha1digest = MessageDigest.getInstance(X509ObjectIdentifiers.id_SHA1.getId(),
                    new BouncyCastleProvider());
            digest = Hex.encodeHexString(sha1digest.digest(cert.getEncoded()));
        } catch (Exception e) {
            ExceptionUtils.throwException(new UnexpectedException(e), LOG);
        }
        return digest;
    }

    /**
     * Decides whether some certificates should be hidden
     * 
     * @param certs
     */
    private static void recommend(List<CertificateDisplayDetails> certs, AppletSigningMethod sm) {
        switch (sm) {
        case sc:
            for (CertificateDisplayDetails cert : certs) {
                cert.setRecommended(((cert.isSscd()||fromCard(cert)||cert.isQualified())&&isNonRepudiation(cert.getKeyUsage())));
            }
            break;
        case installed_cert:
            
            for (CertificateDisplayDetails cert : certs) {
                if (cert.isSscd()){
                    cert.setRecommended(false);
                    continue;
                }
                cert.setRecommended(cert.isQualified()||isNonRepudiation(cert.getKeyUsage()));
            }
            break;
        case p12:
            for (CertificateDisplayDetails cert : certs) {
                cert.setRecommended(cert.isQualified()||isNonRepudiation(cert.getKeyUsage()));
            }
            break;
        default:
        }
        //do not recommend any hidden and hide any expired
        for (CertificateDisplayDetails cert : certs) {
            if (!cert.isDateValid()){
                cert.setRecommended(false);
            }
        }
    }

    /**
     * Tests if the certificate belongs to a card based on the API
     * 
     * @param cert the {@link CertificateDisplayDetails}
     * @return true if the certificate seems to belong to a card
     */
    private static boolean fromCard(CertificateDisplayDetails cert) {
        CardProfile cp = cert.getCardProfile();
        if (cp.getApi()!=null) {
            if (SignatureTokenType.MOCCA.name().equals(cp.getApi().name()) 
                    || SignatureTokenType.PKCS11.name().equals(cp.getApi().name())) {
                return true;
            }
            
        }
        return false;
    }
    

    /**
     * Parse an LDAP name
     * @param name the LDAP string
     * @return map of LDAP type/value
     */
    private static Map<String,String> parseLdapName(String name) {
        Map<String,String> rdns = new HashMap<String, String>();
        try {
            LdapName ldapName = new LdapName(name);            
            for (Rdn rdn:ldapName.getRdns()){
                rdns.put(rdn.getType(), rdn.getValue().toString());
            }
        } catch (InvalidNameException e) {
            LOG.error( "Error parsing the issuer name "+name, e);
        }
        return rdns;
    }

}
