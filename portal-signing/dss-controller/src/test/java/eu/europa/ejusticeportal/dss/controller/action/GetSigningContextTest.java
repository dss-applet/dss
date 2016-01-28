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
package eu.europa.ejusticeportal.dss.controller.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.profile.CardProfileRepositoryImpl;

/**
 * Unit test of the GetSigningContext action handler
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
@RunWith(value = JUnit4.class)
public class GetSigningContextTest {

    private PortalFacade portal;
    private CardProfileRepositoryImpl repo;
    private HttpServletRequest request;
    /**
     * Initialises
     */
    @Before
    public void init() {
        portal = getPortal();
        repo = new CardProfileRepositoryImpl();
        
        request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getId()).thenReturn("jfdljewrfjeorijfeor");
    }

    /**
     * Test where the card is not in the database
     */
    @Test
    public void testUnknownCard() {
        GetSigningContext s = new GetSigningContext() {
            @SuppressWarnings("unused")//it is used
            CardProfileRepositoryImpl getSigningContextRepositoryXmlImpl() {
                return repo;
            }
        };

        SigningContext sc = (SigningContext) s.getResponseObject(portal, request, getUnknowFingerprint());
        assertNull(sc.getCardProfiles().get(0).getDigestAlgo());
    }
    
    private Fingerprint getUnknowFingerprint(){
        
        Fingerprint fp = new Fingerprint();
        fp.setArch("32");
        fp.setJreVendor("sun");
        fp.setJreVersion("1.6");
        fp.setNavPlatform("windows");
        fp.setOs("windows");
        fp.setOsVersion("7");
        fp.setUserAgent("mozilla");
        CardProfile cd = new CardProfile();
        cd.setAtr("3B 7D 94 00 00 80 31 80 65 B0 83 02 04 7E 83 00 90 00 00 00 00");
        List<CardProfile> lcd = new ArrayList<CardProfile>();
        lcd.add(cd);
        fp.setCardProfiles(lcd);
        return fp;
    }
    /**
     * Test where the card ATR in the database hasn't got a regexp
     */
    @Test
    public void testFind() {
        // 3B 7D 95 00 00 80 31 .. 65 B0 83 11 C0 A9 83 FF FF FF
        GetSigningContext s = new GetSigningContext() {
            @SuppressWarnings("unused")//it isi used
            CardProfileRepositoryImpl getSigningContextRepositoryXmlImpl() {
                return repo;
            }
        };

        SigningContext sc = (SigningContext) s.getResponseObject(portal, request, getKnownFingerpringNoRegEx());
        assertEquals(sc.getCardProfiles().get(0).getCardDescription(), "[Luxembourg] LuxTrust");
    }

    private Fingerprint getKnownFingerpringNoRegEx(){
        Fingerprint fp = new Fingerprint();
        fp.setArch("32");
        fp.setJreVendor("sun");
        fp.setJreVersion("1.6");
        fp.setNavPlatform("windows");
        fp.setOs("windows");
        fp.setOsVersion("7");
        fp.setUserAgent("mozilla");
        CardProfile cd = new CardProfile();
        cd.setAtr("3B 7D 94 00 00 80 31 80 65 B0 83 02 04 7E 83 00 90 00");
        List<CardProfile> lcd = new ArrayList<CardProfile>();
        lcd.add(cd);
        fp.setCardProfiles(lcd);
        return fp;
    }
    /**
     * Test where the card ATR in the database has a regexp
     */
    @Test
    public void testRegexp() {
        PortalFacade p = new PortalFacadeTestImpl("XML", "docLoc", "dss/TestSigningRepo.xml");
        final CardProfileRepositoryImpl r = new CardProfileRepositoryImpl();
        
        // 3B 7D 95 00 00 80 31 .. 65 B0 83 11 C0 A9 83 FF FF FF
        GetSigningContext s = new GetSigningContext() {            
            @SuppressWarnings("unused")//it is used.
            CardProfileRepositoryImpl getSigningContextRepositoryXmlImpl() {
                return r;
            }
        };
        
        SigningContext sc = (SigningContext) s.getResponseObject(p, request, getKnownFingerprintRegEx());
        assertEquals(sc.getCardProfiles().get(0).getCardDescription(), "Not a real card - regex");
    }

    private Fingerprint getKnownFingerprintRegEx(){
        Fingerprint fp = new Fingerprint();
        fp.setArch("32");
        fp.setJreVendor("sun");
        fp.setJreVersion("1.6");
        fp.setNavPlatform("windows");
        fp.setOs("windows");
        fp.setOsVersion("7");
        fp.setUserAgent("mozilla");
        CardProfile cd = new CardProfile();
        cd.setAtr("3B 7D 95 00 00 80 31 99 65 B0 83 11 C0 A9 83 FF FF FF");
        List<CardProfile> lcd = new ArrayList<CardProfile>();
        lcd.add(cd);
        fp.setCardProfiles(lcd);
        return fp;
    }
    /**
     * Test caching
     * @return
     */
    @Test 
    public void testCaching(){
        GetSigningContext s = new GetSigningContext();
        PortalFacadeTestImpl p = new PortalFacadeTestImpl();
        
        SigningContext sc = (SigningContext)s.getResponseObject(p, request, getKnownFingerpringNoRegEx());
        assertEquals(sc.getCardProfiles().get(0).getCardDescription(), "[Luxembourg] LuxTrust");
        
        String xml = p.getCardProfileXML();
        p.setCardProfileXML("");
        sc = (SigningContext)s.getResponseObject(p, request, getKnownFingerpringNoRegEx());
        assertNull(sc.getCardProfiles().get(0).getDigestAlgo());

        p.setCardProfileXML(xml);
        sc = (SigningContext)s.getResponseObject(p, request, getKnownFingerpringNoRegEx());
        assertEquals(sc.getCardProfiles().get(0).getCardDescription(), "[Luxembourg] LuxTrust");
        

        p.setCardProfileXML(null);
        sc = (SigningContext)s.getResponseObject(p, request, getKnownFingerpringNoRegEx());
        assertNull(sc.getCardProfiles().get(0).getDigestAlgo());

        p.setCardProfileXML(xml);
        sc = (SigningContext)s.getResponseObject(p, request, getKnownFingerpringNoRegEx());
        assertEquals(sc.getCardProfiles().get(0).getCardDescription(), "[Luxembourg] LuxTrust");
        
        
    }
    
    
    private static PortalFacade getPortal() {
        return new PortalFacadeTestImpl() ;
    }
    
}
