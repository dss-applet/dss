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

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.pades.PAdESService;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.crl.OnlineCRLSource;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.HttpProxyConfig;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.TestUtils;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;
import eu.europa.ejusticeportal.dss.controller.signature.PdfUtils;
import eu.europa.ejusticeportal.dss.controller.signature.RefreshingTrustedListsCertificateSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 *
 * Test the validation of a PDF coming from the portal.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 dÃ©c. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class ValidateSignedAcrobatPdfTest {

    private static final String SEALED_PDF_FILE = "target/hello-world-sealed.pdf";
    private static final String SIGNED_PDF_FILE = "target/hello-world-signed.pdf";

    private static PortalFacade portal;
    
    /**
     * Initialises the trusted list store and creates the sealed PDF for the other tests.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void createSealedPDF() throws Exception {

        StringWriter writer = new StringWriter();
        IOUtils.copy(PortalFacadeTestImpl.class.getClassLoader().getResourceAsStream("dss/original.xml"), writer, "UTF-8");
        String xml = writer.toString();

        System.setProperty(SealedPDFService.PASSWORD_FILE_PROPERTY, "classpath:server.pwd");
        System.setProperty(SealedPDFService.CERTIFICATE_FILE_PROPERTY, "classpath:server.p12");
        portal = new PortalFacadeTestImpl(xml, "dss/original.pdf");
        HttpProxyConfig hc = Mockito.mock(HttpProxyConfig.class);
        Mockito.when(hc.isHttpEnabled()).thenReturn(Boolean.FALSE);
        Mockito.when(hc.isHttpsEnabled()).thenReturn(Boolean.FALSE);
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getSealMethod()).thenReturn(SealMethod.SEAL_CUSTOM);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        //Mockito.when(config.getLotlUrl()).thenReturn("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml");
        Mockito.when(config.getLotlUrl()).thenReturn("https://example.com");
        Mockito.when(config.getRefreshPeriod()).thenReturn(3600);
        RefreshingTrustedListsCertificateSource.init(hc, config);
        RefreshingTrustedListsCertificateSource.getInstance().refresh();


        byte[] doc = SealedPDFService.getInstance().sealDocument(portal.getPDFDocument(getRequest()),portal.getPDFDocumentXML(getRequest()), "disclaimer", SealMethod.SEAL_CUSTOM);// document with embedded XML, signed by the server
        OutputStream os = new FileOutputStream(SEALED_PDF_FILE);
        IOUtils.write(doc, os);
        PAdESService pades = new PAdESService(new CommonCertificateVerifier(true)){

            @Override
            protected void assertSigningDateInCertificateValidityRange(SignatureParameters parameters) {
            }
            
        };
        SignatureParameters p = new SignatureParameters();
        p.setDigestAlgorithm(DigestAlgorithm.SHA256);
        p.setPrivateKeyEntry(SealedPDFService.getInstance().getToken().getKeys().get(0));
        p.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        p.setSigningToken(SealedPDFService.getInstance().getToken());
        DSSDocument signed = pades.signDocument(new InMemoryDocument(doc), p);
        os = new FileOutputStream(SIGNED_PDF_FILE);
        IOUtils.write(signed.getBytes(), os);

    }

    /**
     * Test the validation of the sealed PDF
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void sealedPdfValidationTest() throws FileNotFoundException, IOException {

        byte[] doc = IOUtils.toByteArray(new FileInputStream(SIGNED_PDF_FILE));
        assertTrue(TestUtils.isSealed(null, doc, SealedPDFService.getInstance().getToken(), SealMethod.SEAL_CUSTOM));
        String testXML = new String(PdfUtils.extractAttachment(IOUtils
                .toByteArray(new FileInputStream(SEALED_PDF_FILE)), SealedPDFService.EMBEDDED_XML_ID), "UTF-8");

        InputStream isXml = PortalFacadeTestImpl.class.getClassLoader().getResourceAsStream("dss/original.xml");

        StringWriter writer = new StringWriter();
        IOUtils.copy(isXml, writer, "UTF-8");
        String xml = writer.toString();
        assertEquals(testXML, xml);

    }
    private static HttpServletRequest getRequest() {
    	HttpServletRequest r  = Mockito.mock(HttpServletRequest.class);
    	HttpSession session = Mockito.mock(HttpSession.class);
    	Mockito.when(r.getSession()).thenReturn(session);
        return r;
    }

    private static HttpServletResponse getResponse(final OutputStream os) throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getOutputStream()).thenReturn(new ServletOutputStreamImpl(os));        return response;
    }

}
