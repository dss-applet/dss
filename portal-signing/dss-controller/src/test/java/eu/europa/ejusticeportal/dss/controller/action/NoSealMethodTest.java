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

import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.SignedDocumentValidator;
import eu.europa.ec.markt.dss.validation102853.crl.OnlineCRLSource;
import eu.europa.ec.markt.dss.validation102853.ocsp.OnlineOCSPSource;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.controller.CommonsHttpLoaderFactory;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.HttpProxyConfig;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;
import eu.europa.ejusticeportal.dss.controller.signature.PdfUtils;
import eu.europa.ejusticeportal.dss.controller.signature.RefreshingTrustedListsCertificateSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 *
 * Test the seal method using the SealMethod.NO_SEAL
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 dÃ©c. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class NoSealMethodTest {

    private static final String TEST_XML = "<test>here is some xml for testing</test>";
    private static final String SEALED_PDF_FILE = "target/hello-world-sealed.pdf";
    private static final String NO_SEALED_PDF_FILE = "target/hello-world-no-sealed.pdf";
    private static SealMethod SEAL_METHOD = SealMethod.NO_SEAL;

    private static DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
    private static PortalFacade portal;

    /**
     * Initialises the trusted list store and creates the sealed PDF for the other tests.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void createSealedPDF() throws Exception {
        System.setProperty(SealedPDFService.PASSWORD_FILE_PROPERTY, "classpath:server.pwd");
        System.setProperty(SealedPDFService.CERTIFICATE_FILE_PROPERTY, "classpath:server.p12");
        portal = new PortalFacadeTestImpl(TEST_XML, "dss/hello-world.pdf");
        HttpProxyConfig hc = Mockito.mock(HttpProxyConfig.class);
        Mockito.when(hc.isHttpEnabled()).thenReturn(Boolean.FALSE);
        Mockito.when(hc.isHttpsEnabled()).thenReturn(Boolean.FALSE);

        Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getLotlUrl()).thenReturn("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml");
        Mockito.when(config.getRefreshPeriod()).thenReturn(3600);
        Mockito.when(config.getSealMethod()).thenReturn(SEAL_METHOD);
        ((PortalFacadeTestImpl)portal).setConfig(config);
        RefreshingTrustedListsCertificateSource.init(hc, config);
        RefreshingTrustedListsCertificateSource.getInstance().refresh();

        byte [] originalDocument = portal.getPDFDocument(getRequest());
        byte[] doc = SealedPDFService.getInstance().sealDocument(originalDocument,portal.getPDFDocumentXML(getRequest()), "disclaimer", SEAL_METHOD);// document with embedded XML, signed by the server
        OutputStream os = new FileOutputStream(SEALED_PDF_FILE);
        IOUtils.write(doc, os);

    }

    /**
     * Test the validation of the sealed PDF with the method SealMethod.NO_SEAL
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void sealedPdfValidationTest() throws Exception {
        byte [] original = IOUtils.toByteArray(PortalFacadeTestImpl.class.getClassLoader().getResourceAsStream("dss/hello-world-no-sealed.pdf"));
        byte [] sealedPdf = IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE));
        assertTrue("The document is not supposed to have changed.", Arrays.equals(original, sealedPdf));
    }

    private static HttpServletRequest getRequest() {
    	HttpServletRequest r  = Mockito.mock(HttpServletRequest.class);
    	HttpSession session = Mockito.mock(HttpSession.class);
    	Mockito.when(r.getSession()).thenReturn(session);
        return r;
    }

    private DocumentValidationService getDocumentValidationService(){
        DocumentValidationService s = DocumentValidationService.getInstance();
        return s;
    }

}
