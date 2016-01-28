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
import static org.junit.Assert.assertTrue;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Defaults;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * 
 * Test the Defaults methods of the SigningContextHome
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class SigningContextHomeTest2 {

    private List<String> a = new ArrayList<String>();
    private int maxTries;

    /**
     * Set up the SigningContext
     * 
     * @throws NoSuchAlgorithmException
     */
    @Before
    public void init() throws NoSuchAlgorithmException {
    	Event.getInstance().registerListener(new EventListener() {
			
			public void process(Object event) {
				
			}
		});
        SigningContext sc = new SigningContext();
        Defaults defaults = new Defaults();

        a.add(DigestAlgorithm.valueOf("SHA512").name());
        a.add(DigestAlgorithm.valueOf("SHA256").name());
        a.add(DigestAlgorithm.valueOf("SHA1").name());
        a.add(DigestAlgorithm.valueOf("MD5").name());

        maxTries = a.size();
        defaults.setMaxTries(maxTries);
        defaults.setAlgorithms(a);
        sc.setDefaults(defaults);
        FingerprintHome.getInstance().init("", "");
        sc.setCardProfiles(new ArrayList<CardProfile>());
        CardProfile cp = new CardProfile();
        cp.setApi(SignatureTokenType.MSCAPI.name());
        sc.getCardProfiles().add(cp);
        SigningContextHome.getInstance().init(sc);

    }

    /**
     * Test the LIST strategy
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testDefaultsList() throws NoSuchAlgorithmException {
        SigningContextHome.getInstance().getSigningContext().getDefaults().setStrategy(Defaults.Strategy.LIST.name());

        List<DigestAlgorithm> ds = SigningContextHome.getInstance().getDefaultAlgorithms();
        assertEquals(a.size(), ds.size());
        for (int i = 0; i < a.size(); i++) {
            assertEquals(DigestAlgorithm.valueOf(a.get(i)), ds.get(i));
        }
    }

    /**
     * Test the RANDOM strategy
     * 
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testDefaultsRandom() throws NoSuchAlgorithmException {
        SigningContextHome.getInstance().getSigningContext().getDefaults().setStrategy(Defaults.Strategy.RANDOM.name());
        SigningContextHome.getInstance().getSigningContext().getDefaults()
                .setDefaultAlgorithm(DigestAlgorithm.valueOf("SHA1").name());
        List<DigestAlgorithm> ds = SigningContextHome.getInstance().getDefaultAlgorithms();
        assertEquals(DigestAlgorithm.valueOf("SHA1"), ds.get(ds.size() - 1));
        assertEquals(a.size() + 1, ds.size());

        for (String ag : a) {
            assertTrue(ds.contains(DigestAlgorithm.valueOf(ag)));
        }
        List<DigestAlgorithm> randomness1 = new ArrayList<DigestAlgorithm>();
        List<DigestAlgorithm> randomness2 = new ArrayList<DigestAlgorithm>();
        for (int i = 0; i < 1000; i++) {
            randomness1.addAll(SigningContextHome.getInstance().getDefaultAlgorithms());
        }
        for (int i = 0; i < 1000; i++) {
            randomness2.addAll(SigningContextHome.getInstance().getDefaultAlgorithms());
        }
        assertEquals(randomness1.size(), randomness2.size());
        boolean random = false;
        for (int i = 0; i < randomness1.size(); i++) {
            if (!randomness1.get(i).equals(randomness2.get(i))) {
                random = true;
                break;
            }
        }
        assertTrue(random);
        
        SigningContextHome.getInstance().getSigningContext().getDefaults().setMaxTries(2);
        ds = SigningContextHome.getInstance().getDefaultAlgorithms();
        assertEquals(3, ds.size());
        assertEquals(DigestAlgorithm.valueOf("SHA1"),ds.get(2));
    }
    /**
     * Test the defaults when there are none
     */
    @Test
    public void testDefaultsNull(){
        SigningContextHome.getInstance().getSigningContext().setDefaults(null);
        assertNotNull(SigningContextHome.getInstance().getDefaultAlgorithms());
        assertEquals(0,SigningContextHome.getInstance().getDefaultAlgorithms().size());
    }
    
}
