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
package eu.europa.ejusticeportal.dss.controller.profile;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.action.PortalFacadeTestImpl;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * Test the card profile repository
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
@RunWith(JUnit4.class)
public class CardProfileRepositoryTest {

    private CardProfileRepository repo;
    private static final String ATR_LUXTRUST = "3B 7D 94 00 00 80 31 80 65 B0 83 02 04 7E 83 00 90 00";
    private static final String ATR_AUSTRIAN_E = "3B DD 96 FF 81 B1 FE 45 1F 03 80 31 B0 52 02 03 64 04 1B B4 22 81 05 18";
    private static final String ATR_SWISS = "3B F2 18 00 02 C1 0A 31 FE 58 C8 08 74";

    private PortalFacade portal = new PortalFacadeTestImpl();
    /**
     * Initialise
     */
    @Before
    public void init() {
        repo = new CardProfileRepositoryImpl();
    }

    /**
     * Test Luxtrust
     * @throws NoSuchAlgorithmException 
     */
    @Test
    public void testLuxtrust() throws NoSuchAlgorithmException {

        SigningContext scWin32 = createSigningContext();
        Fingerprint fpWin32 = new Fingerprint();
        fpWin32.setOs(System.getProperty("os.name").toLowerCase());
        fpWin32.setNavPlatform("Win32");
        fpWin32.setJreVersion(System.getProperty("java.version"));
        CardProfile cd = new CardProfile();
        cd.setAtr(ATR_LUXTRUST);
        List<CardProfile> lcd32 = new ArrayList<CardProfile>();
        lcd32.add(cd);
        fpWin32.setCardProfiles(lcd32);
        repo.findCardProfiles(null,portal,fpWin32, scWin32);
        assertEquals(SignatureTokenType.NEWMSCAPI, scWin32.getCardProfiles().get(0).getApi());
        assertEquals("The URL is wrong","https://www.luxtrust.lu/fr/simple/189",scWin32.getCardProfiles().get(0).getUrl());
        SigningContext scWin64 = createSigningContext();
        Fingerprint fpWin64 = new Fingerprint();
        fpWin64.setOs(System.getProperty("os.name").toLowerCase());
        fpWin64.setNavPlatform("Win64");
        fpWin64.setJreVersion(null);
        cd = new CardProfile();
        cd.setAtr(ATR_LUXTRUST);
        List<CardProfile> lcd64 = new ArrayList<CardProfile>();
        lcd64.add(cd);
        fpWin64.setCardProfiles(lcd64);
        repo.findCardProfiles(null,portal,fpWin64, scWin64);
        assertEquals(SignatureTokenType.NEWMSCAPI, scWin64.getCardProfiles().get(0).getApi());

    }

    /**
     * Test Austrian e-card
     * @throws NoSuchAlgorithmException 
     */
    @Test
    public void testAustrianECard() throws NoSuchAlgorithmException {

        SigningContext scWin32 = createSigningContext();
        Fingerprint fpWin32 = new Fingerprint();
        fpWin32.setOs(System.getProperty("os.name").toLowerCase());
        fpWin32.setNavPlatform("Win32");
        fpWin32.setJreVersion("a strange version");
        CardProfile cd = new CardProfile();
        cd.setAtr(ATR_AUSTRIAN_E);
        List<CardProfile> lcd32 = new ArrayList<CardProfile>();
        lcd32.add(cd);
        fpWin32.setCardProfiles(lcd32);
        repo.findCardProfiles(null,portal,fpWin32, scWin32);
        assertEquals(SignatureTokenType.MOCCA, scWin32.getCardProfiles().get(0).getApi());

        SigningContext scWin64 = createSigningContext();
        Fingerprint fpWin64 = new Fingerprint();
        fpWin64.setOs(System.getProperty("os.name").toLowerCase());
        fpWin64.setNavPlatform("Win64");
        fpWin64.setJreVersion("1.5.1");
        scWin64.setJreVersion(1.51);
        cd = new CardProfile();
        cd.setAtr(ATR_AUSTRIAN_E);
        List<CardProfile> lcd64 = new ArrayList<CardProfile>();
        lcd64.add(cd);
        fpWin64.setCardProfiles(lcd64);
        repo.findCardProfiles(null,portal,fpWin64, scWin64);
        assertNull(scWin64.getCardProfiles().get(0).getApi());
    }
    
    @Test
    public void testSwissECard() throws NoSuchAlgorithmException {

        SigningContext scWin32 = createSigningContext();
        Fingerprint fpWin32 = new Fingerprint();
        fpWin32.setUserAgent("mozilla/5.0 (windows nt 6.1; wow64; rv:26.0) gecko/20100101 firefox/26.0");
        fpWin32.setOs("windows 7");
        fpWin32.setOsVersion("6.1");
        fpWin32.setArch("x86");
        fpWin32.setCardDetectionAvailable(true);
        fpWin32.setJreVendor("Oracle Corporation");
        fpWin32.setNavPlatform("win32");
        fpWin32.setJreVersion("1.7.0_45");
        CardProfile cd = new CardProfile();
        cd.setAtr(ATR_SWISS);
        cd.setApi(SignatureTokenType.MOCCA.name());
        List<CardProfile> lcd32 = new ArrayList<CardProfile>();
        lcd32.add(cd);
        fpWin32.setCardProfiles(lcd32);
        repo.findCardProfiles(null,portal,fpWin32, scWin32);
        assertEquals(SignatureTokenType.NEWMSCAPI, scWin32.getCardProfiles().get(0).getApi());
    }
    /**
     * Test for ATR not in repository
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testCardNotFound() throws NoSuchAlgorithmException{
        SigningContext scWin32 = createSigningContext();
        Fingerprint fpWin32 = new Fingerprint();
        fpWin32.setOs(System.getProperty("os.name").toLowerCase());
        fpWin32.setNavPlatform("Win32");
        fpWin32.setJreVersion(System.getProperty("java.version"));
        CardProfile cd = new CardProfile();
        cd.setAtr("THIS IS NOT IN THE LIST");
        List<CardProfile> lcd32 = new ArrayList<CardProfile>();
        lcd32.add(cd);
        fpWin32.setCardProfiles(lcd32);
        repo.findCardProfiles(null,portal,fpWin32, scWin32);
        assertTrue(scWin32.getCardProfiles().get(0).getDigestAlgo()==null);        
    }
    
    /**
     * Test for when there is no API
     */
    @Test
    public void testNoAPI() throws NoSuchAlgorithmException{
        SigningContext scWin32 = createSigningContext();
        Fingerprint fpWin32 = new Fingerprint();
        fpWin32.setOs(System.getProperty("os.name").toLowerCase());
        fpWin32.setNavPlatform("Win32");
        fpWin32.setJreVersion(System.getProperty("java.version"));
        CardProfile cd = new CardProfile();
        cd.setAtr("THIS IS NOT IN THE LIST");
        List<CardProfile> lcd32 = new ArrayList<CardProfile>();
        lcd32.add(cd);
        fpWin32.setCardProfiles(lcd32);
        repo.findCardProfiles(null,portal,fpWin32, scWin32);
        assertTrue(scWin32.getCardProfiles().get(0).getDigestAlgo()==null);              
    }
    
    /**
     * Test empty ATR
     */
    @Test
    public void testNoC() throws NoSuchAlgorithmException{
        SigningContext scWin32 = createSigningContext();
        Fingerprint fpWin32 = new Fingerprint();
        fpWin32.setOs(System.getProperty("os.name").toLowerCase());
        fpWin32.setNavPlatform("Win32");
        fpWin32.setJreVersion(System.getProperty("java.version"));
        List<CardProfile> lcd32 = new ArrayList<CardProfile>();
        CardProfile cd = new CardProfile();
        cd.setAtr("");
        lcd32.add(cd);
        fpWin32.setCardProfiles(lcd32);
        repo.findCardProfiles(null,portal,fpWin32, scWin32);
        assertTrue(scWin32.getCardProfiles().get(0).getDigestAlgo()==null);        
        
    }
    /**
     * Creates a signingcontext
     * @return the SigningContext
     */
    private SigningContext createSigningContext(){
        SigningContext sc = new SigningContext();
        sc.setArchitecture(Arch.B32.name());
        sc.setDetectedCardCount(1);
        sc.setJreVersion(7.45);
        sc.setOs(OS.WINDOWS.name());
        return sc;
    }
}
