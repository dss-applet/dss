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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.applet.common.JavaScriptExpressionEvaluator;
import eu.europa.ejusticeportal.dss.applet.controller.cb.CallbackHandler;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.model.service.PDFHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.applet.testimpl.PasswordInputFactoryTestImpl;
import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SealedPDF;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * Test the CallbackHandler
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { JavaScriptExpressionEvaluator.class, FingerprintHome.class, DssLogger.class })
@PowerMockIgnore( { "org.xml.*", "javax.xml.*" })
public class CallbackHandlerTest {

    MessageBundle mb;
    SealedPDF pdf;
    SigningContext sc;
    CardProfile cp;
    SignatureStatus ss;
    String xml;

    /**
     * Set up with test data
     */
    @Before
    public void setUp() {
        //Mock Event
        Event.getInstance().registerListener(Mockito.mock(EventListener.class));
        TokenManager.getInstance().init(new PasswordInputFactoryTestImpl());

        //MessageBundle
        mb = new MessageBundle();
        Map<String, String> m = new HashMap<String, String>();
        for (MessagesEnum e : MessagesEnum.values()) {
            m.put(e.name(), e.name());
        }
        mb.setMessages(m);

        //SealedPdf
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/test/resources/hello-world.pdf");
            pdf = new SealedPDF();
            pdf.setPdfBase64(Base64.encodeBase64String((IOUtils.toByteArray(fis))));
            pdf.setFileName("testName");
        } catch (FileNotFoundException ex) {
            fail("Hello-world.pdf is not available.");
        } catch (IOException ex) {
            fail("Unable to create Sealed pdf object.");
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                fail("Unable to close File stream");
            }
        }

        //Fingerprint
        PowerMockito.mockStatic(FingerprintHome.class);
        FingerprintHome fph = Mockito.mock(FingerprintHome.class);
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fph);
        Fingerprint fp = new Fingerprint();
        fp.setJreVersion("1.6_25");
        fp.setOs("windows");
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
        //SignatureStatus
        ss = new SignatureStatus();
        ss.addWarningStatusCode(MessagesEnum.dss_applet_message_signature_not_trusted);
        ss.addWarningStatusCode(MessagesEnum.dss_applet_message_signature_invalid);
    }

    private String buildXml(Object o) {
        return Utils.toString(o);
    }

    /**
     * Test the callback handler
     */
    @Test
    public void testDoHandle() {
        PowerMockito.spy(JavaScriptExpressionEvaluator.class);
        Mockito.when(JavaScriptExpressionEvaluator.getInstance()).thenReturn(
                Mockito.mock(JavaScriptExpressionEvaluator.class));

        try {
            xml = buildXml(mb);
            new CallbackHandler(xml).doHandle(ServerCallId.getMessageBundle);

            MessageBundle result = MessageBundleHome.getInstance().getMessageBundle();

            assertNotNull(result);
            assertEquals(mb.getMessages().size(), result.getMessages().size());
        } catch (CodeException ex) {
            fail("Error in callback Handler: " + ServerCallId.getMessageBundle);
        }

        try {
            xml = buildXml(pdf);
            new CallbackHandler(xml).doHandle(ServerCallId.getSealedPdf);

            SealedPDF result = new SealedPDF();
            result.setFileName(PDFHome.getInstance().getPdfName());
            result.setPdfBase64(Base64.encodeBase64String(PDFHome.getInstance().getSealedPdf()));

            assertNotNull(result);
            assertNotNull(result.getFileName());
            assertNotNull(result.getPdfBase64());
            assertEquals(pdf.getFileName(), result.getFileName());
            assertEquals(pdf.getPdfBase64(), pdf.getPdfBase64());
        } catch (CodeException ex) {
            fail("Error in callback Handler: " + ServerCallId.getSealedPdf);
        }

        try {
            xml = buildXml(sc);
            FingerprintHome.getInstance().init("testNavPlatform", "TestUserAgent");
            FingerprintHome.getInstance().refresh();
            new CallbackHandler(xml).doHandle(ServerCallId.getSigningContext);

            SigningContext result = SigningContextHome.getInstance().getSigningContext();

            assertNotNull(result);
            //One or more profile is added by default
            assertTrue(sc.getCardProfiles().size() <= result.getCardProfiles().size());
            CardProfile resultCp = sc.getCardProfiles().get(0);
            assertEquals(cp.getApi(), resultCp.getApi());
            assertEquals(cp.getCardDescription(), resultCp.getCardDescription());
            assertEquals(cp.getDigestAlgo(), resultCp.getDigestAlgo());
            assertEquals(cp.getLibraryPath(), resultCp.getLibraryPath());
           
            assertEquals(cp.getUrl(), resultCp.getUrl());
        } catch (CodeException ex) {
            fail("Error in callback Handler: " + ServerCallId.getSigningContext);
        }

        try {
            xml = buildXml(ss);
            new CallbackHandler(xml, null).doHandle(ServerCallId.uploadSignedPdf);

            //Test the event instead
            //            assertTrue(UI.txtStatus.getText().contains(MessagesEnum.dss_applet_message_signature_not_trusted.name()));
            //            assertTrue(UI.txtStatus.getText().contains(MessagesEnum.dss_applet_message_signature_invalid.name()));
        } catch (CodeException ex) {
            fail("Error in callback Handler: " + ServerCallId.uploadSignedPdf);
        }
    }
}
