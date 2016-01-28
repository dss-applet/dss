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
package eu.europa.ejusticeportal.dss.applet.controller;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.applet.common.JavaScriptExpressionEvaluator;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEventHandler;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.applet.model.service.FileChooserHome;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.applet.testimpl.FileChooserTestImpl;
import eu.europa.ejusticeportal.dss.applet.testimpl.PasswordInputFactoryTestImpl;
import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test of UIEventHandler class
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { JavaScriptExpressionEvaluator.class, FingerprintHome.class })
@PowerMockIgnore( { "org.xml.*", "javax.xml.*" })
public class UIEventHandlerTest {

    final File f = new File("");
    File temp;
    EventListener mockListener;
    UIEventHandler instance;
    FileChooserTestImpl fileChooserTest;
    SigningContext sc;
    CardProfile cp;
    /**
     * Set up object for unit test
     *
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        //Mock file chooser
        temp = null;
        try {
            temp = File.createTempFile("junit", ".pdf");
            temp.deleteOnExit();
        } catch (IOException ex) {
        }
        fileChooserTest = new FileChooserTestImpl(f, new File("src/test/resources/self.p12"), new File(
                "src/test/resources/hello-world.pdf"), temp);
        FileChooserHome.getInstance().init(fileChooserTest);

        mockListener = Mockito.mock(EventListener.class);
        Event.getInstance().registerListener(mockListener);
        TokenManager.getInstance().init(new PasswordInputFactoryTestImpl());

        PowerMockito.spy(JavaScriptExpressionEvaluator.class);
        Mockito.when(JavaScriptExpressionEvaluator.getInstance()).thenReturn(
                Mockito.mock(JavaScriptExpressionEvaluator.class));

        

        PowerMockito.mockStatic(FingerprintHome.class);
        FingerprintHome fph = Mockito.mock(FingerprintHome.class);
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fph);
        Fingerprint fp = new Fingerprint();
        fp.setCardProfiles(new ArrayList<CardProfile>());
        Mockito.when(fph.getFingerprint()).thenReturn(fp);
        //SigningContext
        sc = new SigningContext();
        cp = new CardProfile();
        ArrayList<CardProfile> cpl = new ArrayList<CardProfile>();
        cp.setApi(SignatureTokenType.MSCAPI.name());
        cp.setCardDescription("A card");
        cp.setDigestAlgo(DigestAlgorithm.SHA1.name());
        cp.setLibraryPath(null);
       
        cp.setUrl("test.com");
        cpl.add(cp);
        sc.setCardProfiles(cpl);
        sc.setArchitecture(Arch.B32.name());
        sc.setOs(OS.WINDOWS.name());
        sc.setJreVersion(1.625);
        SigningContextHome.getInstance().init(sc);
        MessageBundleHome mbh = MessageBundleHome.getInstance();
        MessageBundle mb = new MessageBundle();
        mb.setMessages(new HashMap<String, String>());
        mbh.init(mb);
        
    }


}
