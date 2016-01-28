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
package eu.europa.ejusticeportal.dss.applet.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.applet.common.JavaScriptExpressionEvaluator;
import eu.europa.ejusticeportal.dss.applet.event.AppletStarted;
import eu.europa.ejusticeportal.dss.applet.event.AppletStopped;
import eu.europa.ejusticeportal.dss.applet.event.CertificateSelected;
import eu.europa.ejusticeportal.dss.applet.event.CertificatesRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.ClearMessages;
import eu.europa.ejusticeportal.dss.applet.event.LibraryUpdated;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.P12Updated;
import eu.europa.ejusticeportal.dss.applet.event.SessionExpired;
import eu.europa.ejusticeportal.dss.applet.event.SigningMethodChanged;
import eu.europa.ejusticeportal.dss.applet.event.SigningStatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.UserSurvey;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningContextHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.applet.model.token.CertificateDisplayUtils;
import eu.europa.ejusticeportal.dss.applet.view.component.Message;
import eu.europa.ejusticeportal.dss.applet.view.component.Text;
import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test of UIEventListener class
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JavaScriptExpressionEvaluator.class, SigningContextHome.class})
@PowerMockIgnore({"org.xml.*", "javax.xml.*"})
public class UIEventListenerTest {

    private JavaScriptExpressionEvaluator mockJS;
    private UIEventListener instance;
    private SigningContextHome mockSc;

    /**
     * Set up object for unit test
     *
     */
    @Before
    public void setUp() {
        Event.getInstance().registerListener(new EventListener() {
            
            public void process(Object event) {
                
            }
        });
        PowerMockito.spy(JavaScriptExpressionEvaluator.class);
        mockJS = Mockito.mock(JavaScriptExpressionEvaluator.class);
        Mockito.when(JavaScriptExpressionEvaluator.getInstance()).thenReturn(mockJS);
        instance = new UIEventListener();

        PowerMockito.spy(SigningContextHome.class);
        mockSc = Mockito.mock(SigningContextHome.class);
        Mockito.when(SigningContextHome.getInstance()).thenReturn(mockSc);
        SigningContext sc = new SigningContext();
        sc.setArchitecture(Arch.B32.name());
        sc.setOs(OS.WINDOWS.name());
        sc.setJreVersion(1.234);
        Mockito.when(mockSc.getSigningContext()).thenReturn(sc);
        
        MessageBundle mb = new MessageBundle();
        Map<String, String> m = new HashMap<String, String>();
        for (MessagesEnum e : MessagesEnum.values()) {
            m.put(e.name(), e.name());
        }
        mb.setMessages(m);
        MessageBundleHome.getInstance().init(mb);
        UIState.transition(UIState.SC____);
    }

    @After
    public void tearDown() {
        UIState.reset();
    }

    /**
     * Test of process CertificatesRefreshed event, of class UIEventListener.
     */
    @Test
    public void testProcessCertificatesRefreshed() {
        boolean keyUsageNonRep [] = new boolean [] {true,true};
        X509Certificate cert = Mockito.mock(X509Certificate.class);
        PublicKey pk = Mockito.mock(PublicKey.class);
        Mockito.when(pk.getAlgorithm()).thenReturn("RSA");
        Mockito.when(cert.getPublicKey()).thenReturn(pk);
        CardProfile cp = Mockito.mock(CardProfile.class);
        CertificateDisplayDetails c1 = new CertificateDisplayDetails("Subject1","Issuer1","LU", "Serial1", "hash1", true, true,keyUsageNonRep, cert, cp,"");
        c1.setDateValid(true);        
        CertificateDisplayDetails c2 = new CertificateDisplayDetails("Subject1","Issuer1","LU", "Serial1", "hash1", true, true,keyUsageNonRep, cert, cp,"");
        c2.setDateValid(true);
        List<CertificateDisplayDetails> expectedCertificates = Arrays.asList(c1, c2);        
        SigningMethodHome.getInstance().setSigningMethod(AppletSigningMethod.sc);
        CertificateDisplayUtils.prepare(expectedCertificates, AppletSigningMethod.sc);
        instance.process(new CertificatesRefreshed(expectedCertificates));
        assertEquals(expectedCertificates.size(), UI.CERTIFICATES_RECOMMENDED.getOptions().size());
        assertEquals(expectedCertificates.get(0).getName(), UI.CERTIFICATES_RECOMMENDED.getOptions().get(0).getText());
        assertEquals(expectedCertificates.get(1).getName(), UI.CERTIFICATES_RECOMMENDED.getOptions().get(1).getText());
    }

    /**
     * Test of process PKCS11InputRequired event, of class UIEventListener.
     */
    @Test
    public void testProcessPKCS11InputRequired() {
        SigningMethodHome.getInstance().setSigningMethod(AppletSigningMethod.sc);
        instance.process(new AppletStarted());
        instance.process(new SigningMethodChanged(AppletSigningMethod.sc));        
        assertFalse(UI.FILE_PKCS11.isVisible());
        assertFalse(UI.FILE_PKCS11.isEnabled());
    }

    /**
     * Test of process StatusRefreshed event, of class UIEventListener.
     */
    @Test
    public void testProcessStatusRefreshed() {
        UI.MESSAGES.clear();
        instance.process(new StatusRefreshed(MessagesEnum.dss_applet_message_certs_refreshed, MessageLevel.SUCCESS));
        assertEquals(MessagesEnum.dss_applet_message_certs_refreshed.name(), UI.MESSAGES.getMessages().get(0).getTitle());
        assertEquals(MessageLevel.SUCCESS, UI.MESSAGES.getMessages().get(0).getLevel());
        UI.MESSAGES.clear();
        instance.process(new StatusRefreshed(MessagesEnum.dss_applet_message_signature_status_warning, MessageLevel.WARNING));
        assertEquals(MessagesEnum.dss_applet_message_signature_status_warning.name(), UI.MESSAGES.getMessages().get(0).getTitle());
        assertEquals(MessageLevel.WARNING, UI.MESSAGES.getMessages().get(0).getLevel());
        UI.MESSAGES.clear();
        instance.process(new StatusRefreshed(MessagesEnum.dss_applet_message_signature_status_error, MessageLevel.ERROR));
        assertEquals(MessagesEnum.dss_applet_message_signature_status_error.name(), UI.MESSAGES.getMessages().get(0).getTitle());
        assertEquals(MessageLevel.ERROR, UI.MESSAGES.getMessages().get(0).getLevel());
    }
    /**
     * Test of process StatusRefreshed event, of class UIEventListener. Downlevelling of certain warnings.
     */
    @Test
    public void testProcessStatusRefreshedDownlevel() {
        UI.MESSAGES.clear();
        instance.process(new StatusRefreshed(MessagesEnum.dss_applet_message_pin_entry_cancelled, MessageLevel.ERROR));
        assertEquals(MessagesEnum.dss_applet_message_pin_entry_cancelled.name(), UI.MESSAGES.getMessages().get(0).getTitle());
        assertEquals(MessageLevel.INFO, UI.MESSAGES.getMessages().get(0).getLevel());
        UI.MESSAGES.clear();
        instance.process(new StatusRefreshed(MessagesEnum.dss_applet_message_pin_entry_cancelled_mocca, MessageLevel.ERROR));
        assertEquals(MessagesEnum.dss_applet_message_pin_entry_cancelled_mocca.name(), UI.MESSAGES.getMessages().get(0).getTitle());
        assertEquals(MessageLevel.INFO, UI.MESSAGES.getMessages().get(0).getLevel());
    }

    /**
     * Test of process LoadingRefreshed event, of class UIEventListener.
     */
    @Test
    public void testProcessLoadingRefreshed() {
        instance.process(new LoadingRefreshed(true, true));
        instance.process(new LoadingRefreshed(true, false));
        instance.process(new LoadingRefreshed(false, false));
        instance.process(new LoadingRefreshed(false, true));
        Mockito.verify(mockJS, Mockito.times(2)).eval(UIFunction.setLoading, Boolean.toString(true),null,null);
        Mockito.verify(mockJS).eval(UIFunction.setLoading, Boolean.toString(false), null,null);
    }

  

    /**
     * Test of process AppletStopped event, of class UIEventListener.
     */
    @Test
    public void testProcessAppletStopped() {
        instance.process(new AppletStopped());
        assertEquals(UIState.SF____, UIState.getCurrentState());
    }

    /**
     * Test of process SigningStatusRefreshed event, of class UIEventListener.
     */
    @Test
    public void testProcessSigningStatusRefreshed() {
        SignatureStatus signStatus = new SignatureStatus();
        SigningMethodHome.getInstance().setSigningMethod(AppletSigningMethod.sc);
        signStatus.addExceptionStatusCode(MessagesEnum.dss_applet_message_technical_failure);
        signStatus.addExceptionStatusCode(MessagesEnum.dss_applet_message_controller_error);
        signStatus.addWarningStatusCode(MessagesEnum.dss_applet_message_uploaded_pdf_tampered);
        UI.MESSAGES.clear();
        instance.process(new SigningMethodChanged(eu.europa.ejusticeportal.dss.common.AppletSigningMethod.sc));
        instance.process(new CertificateSelected());
        assertEquals(UIState.SC_CS_, UIState.getCurrentState());
        instance.process(new SigningStatusRefreshed(signStatus));
        Message m = UI.MESSAGES.getMessages().get(0);
        assertEquals(MessagesEnum.dss_applet_message_signature_status_error.name(), m.getTitle());
        Text t = UI.MESSAGES.getMessages().get(0).getDetails().get(0);
        assertEquals(MessagesEnum.dss_applet_message_technical_failure.name(), t.getText());
        t = UI.MESSAGES.getMessages().get(0).getDetails().get(1);
        assertEquals(MessagesEnum.dss_applet_message_controller_error.name(), t.getText());
        assertEquals(MessageLevel.ERROR, m.getLevel());
        assertEquals(UIState.SC_CS_, UIState.getCurrentState());
        UI.MESSAGES.clear();
        signStatus = new SignatureStatus();
        signStatus.addWarningStatusCode(MessagesEnum.dss_applet_message_technical_failure);
        signStatus.addWarningStatusCode(MessagesEnum.dss_applet_message_controller_error);
        signStatus.addWarningStatusCode(MessagesEnum.dss_applet_message_uploaded_pdf_tampered);
        instance.process(new SigningStatusRefreshed(signStatus));
        m = UI.MESSAGES.getMessages().get(0);
        assertEquals(UIState.SC_SG_, UIState.getCurrentState());
        UI.MESSAGES.clear();

    }

    /**
     * Test of process CertificateSelected event, of class UIEventListener.
     */
    @Test
    public void testProcessCertificateSelected() {
        SigningMethodHome.getInstance().setSigningMethod(AppletSigningMethod.sc);
        instance.process(new SigningMethodChanged(AppletSigningMethod.sc));
        instance.process(new CertificateSelected());
        assertEquals(UIState.SC_CS_, UIState.getCurrentState());
    }

    /**
     * Test of process LibraryUpdated event, of class UIEventListener.
     */
    @Test
    public void testProcessLibraryUpdated() {
        instance.process(new LibraryUpdated("some/libs"));
        assertEquals("some/libs", UI.FILE_PKCS11.getText());
    }

    /**
     * Test of process P12Updated event, of class UIEventListener.
     */
    @Test
    public void testProcessP12Updated() {
        instance.process(new P12Updated("some/libs"));
        assertEquals("some/libs", UI.FILE_PKCS12.getText());
    }


    /**
     * Test of process SessionExpired event, of class UIEventListener.
     */
    @Test
    public void testProcessSessionExpired() {
        instance.process(new SessionExpired(true));
        Mockito.verify(mockJS).eval(UIFunction.redirectSessionExpired);
        instance.process(new SessionExpired(false));
        Mockito.verify(mockJS).eval(UIFunction.promptSessionExpired);
    }



    /**
     * Test of process UserSurvey event, of class UIEventListener.
     */
    @Test
    public void testProcesUserSurvey() {
        SignatureEvent si = new SignatureEvent();
              
        si.setAtr("AA BB CC DD EE FF");
        si.setApi(SignatureTokenType.MOCCA.name());
        si.setDigestAlgorithm("SHA1");
        si.setUserSuppliedPkcs11Path("some/path/toto.dll");
        si.setUserSuppliedCardIssuer("SomeProvider");
        si.setSignatureAlgorithm("RSA");
        instance.process(new UserSurvey(si));
        assertTrue(UI.TXT_USER_SURVEY.isVisible());
        assertTrue(UI.TXT_USER_SURVEY.getText().contains("some/path/toto.dll"));
        //Provider is provided by the user
        Mockito.verify(mockJS).eval(UIFunction.showSurvey);
    }

    /**
     * Test of process SigningMethodChanged event, of class UIEventListener.
     */
    @Test
    public void testProcessSigningMethodChanged() {        
        SigningMethodHome.getInstance().setSigningMethod(AppletSigningMethod.sc);
        instance.process(new SigningMethodChanged(AppletSigningMethod.sc));        
        assertEquals(UIState.SC____, UIState.getCurrentState());
        UIState.reset();
        SigningMethodHome.getInstance().setSigningMethod(AppletSigningMethod.p12);
        instance.process(new SigningMethodChanged(AppletSigningMethod.p12));
        assertEquals(UIState.P12___, UIState.getCurrentState());
    }
    /**
     * Test the messages
     */
    @Test
    public void testMessage(){
        UI.MESSAGES.clear();
        assertTrue(UI.MESSAGES.isClear());
        Message m = new Message();
        m.setLevel(MessageLevel.WARNING);
        m.setTitle("Warning!");
        UI.MESSAGES.addMessage(m);
        UI.updateUI();
        assertEquals(1,UI.MESSAGES.getMessages().size());
        instance.process(new ClearMessages());
        assertEquals(0,UI.MESSAGES.getMessages().size());        
    }
}
