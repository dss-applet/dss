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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;

import java.math.BigInteger;
import java.security.Principal;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bouncycastle.jce.X509Principal;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 * Test for {@link CertificateDisplayDetailsComparator}
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class CertificateDisplayUtilsTest {

    private static final String name = "MÄNNIK,MARI-LIIS,47101010033";
    private static final boolean[] keyUsageRecommended = new boolean[] { true, true };
    private static final boolean[] keyUsageNotRecommended = new boolean[] { true, false };
    private static final boolean[] keyUsageNotSet = null;
    private static final String subjectDN = "SERIALNUMBER=47101010033,G=MARI-LIIS,SN=MÄNNIK,CN=\"MÄNNIK,MARI-LIIS,47101010033\",OU=authentication,O=ESTEID,C=EE";
    
    
    /**
     * Tests getting the displayed certificates when one of them should be recommended
     * @throws CertificateEncodingException
     */
    @Test
    public void testGetDisplayNameMOCCAOneRecommended() throws CertificateEncodingException {

        MessageBundle mb = new MessageBundle();
        mb.setMessages(new HashMap<String, String>());
        MessageBundleHome.getInstance().init(mb);
        
        SignatureTokenType api = SignatureTokenType.MOCCA;
        
        CertificateDisplayDetails cdd1 = CertificateDisplayUtils.getDisplayDetails(getKey(keyUsageRecommended, subjectDN, "RSA"),
                getCardProfile(api));

        CertificateDisplayDetails cdd2 = CertificateDisplayUtils.getDisplayDetails(getKey(keyUsageNotRecommended, subjectDN, "RSA"),
                getCardProfile(api));

        CertificateDisplayDetails cdd3 = CertificateDisplayUtils.getDisplayDetails(getKey(keyUsageNotSet, subjectDN, "RSA"),
                getCardProfile(api));


        List <CertificateDisplayDetails> cdds = Arrays.asList(cdd1,cdd2,cdd3); 
        CertificateDisplayUtils.prepare(cdds, AppletSigningMethod.sc);        
        assertTrue(cdd1.isRecommended());
        assertEquals(name, cdd1.getName());
        assertFalse(cdd3.isRecommended());
        assertEquals(name, cdd3.getName());
        assertFalse(cdd2.isRecommended());
        assertEquals(name, cdd2.getName());

    }
    
    /**
     * Tests getting the displayed certificates when one of them should be recommended
     * @throws CertificateEncodingException
     */
    @Test
    public void testGetDisplayNameMOCCANoneRecommended() throws CertificateEncodingException {

        MessageBundle mb = new MessageBundle();
        mb.setMessages(new HashMap<String, String>());
        MessageBundleHome.getInstance().init(mb);
        
        SignatureTokenType api = SignatureTokenType.MOCCA;
        
        CertificateDisplayDetails cdd1 = CertificateDisplayUtils.getDisplayDetails(getKey(keyUsageNotRecommended, subjectDN, "RSA"),
                getCardProfile(api));
        assertFalse(cdd1.isRecommended());
        assertEquals(name, cdd1.getName());

        CertificateDisplayDetails cdd2 = CertificateDisplayUtils.getDisplayDetails(getKey(keyUsageNotRecommended, subjectDN, "RSA"),
                getCardProfile(api));
        assertFalse(cdd2.isRecommended());
        assertEquals(name, cdd2.getName());

        CertificateDisplayDetails cdd3 = CertificateDisplayUtils.getDisplayDetails(getKey(keyUsageNotSet, subjectDN, "RSA"),
                getCardProfile(api));
        assertFalse(cdd3.isRecommended());
        assertEquals(name, cdd3.getName());

        List <CertificateDisplayDetails> cdds = Arrays.asList(cdd1,cdd2,cdd3); 
        CertificateDisplayUtils.prepare(cdds, AppletSigningMethod.sc);        
    }

    
    private CardProfile getCardProfile(SignatureTokenType api) {
        CardProfile cp = Mockito.mock(CardProfile.class);
        Mockito.when(cp.getApi()).thenReturn(api);
        return cp;
    }

    private DSSPrivateKeyEntry getKey(boolean[] keyUsage, String subjectDN, String algo) throws CertificateEncodingException {

        DSSPrivateKeyEntry key = Mockito.mock(DSSPrivateKeyEntry.class);
        Principal principal = Mockito.mock(Principal.class);

        javax.security.auth.x500.X500Principal x509Principal = new javax.security.auth.x500.X500Principal("CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US");
        X509Certificate cert = Mockito.mock(X509Certificate.class);
        
        PublicKey publicKey = Mockito.mock(PublicKey.class);
        Mockito.when(publicKey.getAlgorithm()).thenReturn("SHA1withRSA");
        
        Mockito.when(key.getCertificate()).thenReturn(cert);
        Mockito.when(cert.getKeyUsage()).thenReturn(keyUsage);
        Mockito.when(cert.getSubjectDN()).thenReturn(principal);
        Mockito.when(cert.getEncoded()).thenReturn("encoded".getBytes());
        Mockito.when(cert.getSerialNumber()).thenReturn(new BigInteger("9999999999999"));
        Mockito.when(cert.getIssuerX500Principal()).thenReturn(x509Principal);
        Mockito.when(cert.getSubjectX500Principal()).thenReturn(x509Principal);
        Mockito.when(cert.getSigAlgOID()).thenReturn("1.2.840.113549.1.1.13");
        Mockito.when(principal.getName()).thenReturn(subjectDN);
        Mockito.when(key.getCertificate().getPublicKey()).thenReturn(publicKey);        
        Mockito.when(publicKey.getAlgorithm()).thenReturn(algo);
        return key;
    }
}
