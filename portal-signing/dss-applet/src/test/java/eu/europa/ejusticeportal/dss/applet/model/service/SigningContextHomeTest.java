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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test the SigningContextHome
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FingerprintHome.class)
@PowerMockIgnore( { "org.xml.*", "javax.xml.*" })
public class SigningContextHomeTest {

    final SigningContext scEmpty = new SigningContext();
    final Fingerprint fpEmpty = new Fingerprint();
    final SigningContext scUnknown = new SigningContext();
    final Fingerprint fpUnknown = new Fingerprint();
    final SigningContext scStandard = new SigningContext();
    final Fingerprint fpStandard = new Fingerprint();

    /**
     * Set up the test data
     */
    @Before
    public void setUp() {
        fpEmpty.setOs("windows");
        fpEmpty.setJreVersion("1.6_27");

        ArrayList<CardProfile> lcp = new ArrayList<CardProfile>();
        scUnknown.setCardProfiles(lcp);
        ArrayList<CardProfile> lcd = new ArrayList<CardProfile>();
        CardProfile cd = new CardProfile();
        cd.setAtr("AA BB CC DD");
        lcd.add(cd);
        fpUnknown.setCardProfiles(lcd);
        fpUnknown.setOs("windows");
        fpUnknown.setJreVersion("1.6_27");
        fpUnknown.setCardDetectionAvailable(true);

        CardProfile cp = new CardProfile();
        lcp = new ArrayList<CardProfile>();
        cp.setApi(SignatureTokenType.MOCCA.name());
        cp.setCardDescription("A card");
        cp.setDigestAlgo(DigestAlgorithm.SHA256.name());
        cp.setLibraryPath(null);

        scEmpty.setOs(OS.WINDOWS.name());
        scUnknown.setOs(OS.LINUX.name());
        cp.setUrl("test.com");
        cp.setAtr("AA BB CC DD");
        lcp.add(cp);
        scStandard.setCardProfiles(lcp);
        lcd = new ArrayList<CardProfile>();
        cd = new CardProfile();
        cd.setAtr("AA BB CC DD");
        lcd.add(cd);
        fpStandard.setCardProfiles(lcd);
        fpStandard.setOs("windows");
        fpStandard.setJreVersion("1.6_27");
        fpStandard.setCardDetectionAvailable(true);
        
        Event.getInstance().registerListener(new EventListener() {
            
            public void process(Object event) {                
            }
        });
    }

    /**
     * Test for an empty fingerprint
     */
    @Test
    public void testCorrectEmpty() {
        PowerMockito.mockStatic(FingerprintHome.class);
        FingerprintHome fpMock = Mockito.mock(FingerprintHome.class);
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fpMock);
        Mockito.when(fpMock.getFingerprint()).thenReturn(fpEmpty);
        
        SigningContextHome.getInstance().init(scEmpty);
        
        SigningContext result = SigningContextHome.getInstance().getSigningContext();
        assertNotNull(result);
        //expect one PKCS11 placeholder because card detection is not available in our empty fingerprint
        assertEquals(1,result.getCardProfiles().size());

    }

    /**
     * Test for an unknown card
     */
    @Test
    public void testCorrectUnkown() {
        PowerMockito.mockStatic(FingerprintHome.class);
        FingerprintHome fpMock = Mockito.mock(FingerprintHome.class);
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fpMock);
        Mockito.when(fpMock.getFingerprint()).thenReturn(fpUnknown);
        SigningContextHome.getInstance().init(scUnknown);
        SigningContext result = SigningContextHome.getInstance().getSigningContext();
        assertNotNull(result);
        assertEquals(0,result.getCardProfiles().size());

        
    }

    /**
     * Test for a known card
     */
    @Test
    public void testCorrectStandard() {
        PowerMockito.mockStatic(FingerprintHome.class);
        FingerprintHome fpMock = Mockito.mock(FingerprintHome.class);
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fpMock);
        Mockito.when(fpMock.getFingerprint()).thenReturn(fpStandard);
        SigningContextHome.getInstance().init(scStandard);
        SigningContext result = SigningContextHome.getInstance().getSigningContext();
        assertNotNull(result);
        assertEquals(1,result.getCardProfiles().size());

        CardProfile moccaProfile = result.getCardProfiles().get(0);

        assertEquals(SignatureTokenType.MOCCA,moccaProfile.getApi());
        assertEquals(DigestAlgorithm.SHA256,moccaProfile.getDigestAlgo());
       
    }
}
