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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
import java.io.OutputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * Test the ValidateSignedPdf class
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 dÃ©c. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class ValidateSignedPdfTest {

    private static final String TEST_XML = "<test>here is some xml for testing</test>";
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
        System.setProperty(SealedPDFService.PASSWORD_FILE_PROPERTY, "classpath:server.pwd");
        System.setProperty(SealedPDFService.CERTIFICATE_FILE_PROPERTY, "classpath:server.p12");
        portal = new PortalFacadeTestImpl(TEST_XML, "dss/hello-world.pdf");
        HttpProxyConfig hc = Mockito.mock(HttpProxyConfig.class);
        Mockito.when(hc.isHttpEnabled()).thenReturn(Boolean.FALSE);
        Mockito.when(hc.isHttpsEnabled()).thenReturn(Boolean.FALSE);
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getLotlUrl()).thenReturn("https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml");
        Mockito.when(config.getRefreshPeriod()).thenReturn(3600);
        RefreshingTrustedListsCertificateSource.init(hc, config);
        RefreshingTrustedListsCertificateSource.getInstance().refresh();
        byte[] doc = SealedPDFService.getInstance().sealDocument(portal.getPDFDocument(getRequest()),portal.getPDFDocumentXML(getRequest()), "disclaimer",
                SealMethod.SEAL_CUSTOM);// document with embedded XML, signed by the server
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
        assertEquals(testXML, ValidateSignedPdfTest.TEST_XML);

    }
    
    /**
     * Tests that the validation detects a document that has not been signed by the e-justice portal user
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Test
    public void documentIsNotSignedTest() throws Exception{
        SignatureStatus ss = new SignatureStatus();        
        getDocumentValidationService().validatePdf(IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE)), null,ss,portal.getDocumentValidationConfig(), new OnlineCRLSource(),portal.getPDFDocumentName(getRequest()));
        assertTrue(ss.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_pdf_not_signed));        
    }

    /**
     * Tests with a document signed with server certificate
     * @throws Exception 
     */
    @Test
    public void pdfOriginTestPositive() throws Exception {
        
        SignatureStatus ss = new SignatureStatus();
        getDocumentValidationService().validatePdf(IOUtils.toByteArray(new FileInputStream(SIGNED_PDF_FILE)), null,ss,portal.getDocumentValidationConfig(), new OnlineCRLSource(),portal.getPDFDocumentName(getRequest()));
        assertFalse(ss.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_from_server));
    }

    /**
     * Tests with a document that was not signed with the server certificate i.e. it did not come from the portal.
     * @throws Exception 
     */
    @Test
    public void pdfOriginTestNegative() throws Exception {
        
        SignatureStatus ss = new SignatureStatus();
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignatureFormatValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getSignBeforeSealValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSealMethod()).thenReturn(SealMethod.SEAL);
        getDocumentValidationService().validatePdf(IOUtils.toByteArray(new FileInputStream(
                "src/test/resources/dss/invalidorigin.pdf")), null,ss,config, new OnlineCRLSource(), portal.getPDFDocumentName(getRequest()));
        assertTrue(ss.getWarningStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_from_server));
    }

    @Test
    public void signDateTestDisabled() throws Exception {

        DocumentValidationService dvs = getDocumentValidationService();
       
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignatureFormatValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSealMethod()).thenReturn(SealMethod.SEAL);
        Mockito.when(config.getSignBeforeSealValidationLevel()).thenReturn(ValidationLevel.WARN);
        SignatureStatus ss = new SignatureStatus();
        dvs.validatePdf(IOUtils.toByteArray(new FileInputStream(
                "src/test/resources/dss/signed_before_seal.pdf")),null, ss, config, new OnlineCRLSource(), portal.getPDFDocumentName(getRequest()));
        assertTrue(ss.getWarningStatusCodes().contains(MessagesEnum.dss_applet_message_signed_before_seal));

    }
 
    /**
     * Test that a PDF signed after the seal is correctly validated
     * @throws Exception 
     */
    @Test
    public void signDateTestPositive() throws Exception{
        
        SignatureStatus ss = new SignatureStatus();
        getDocumentValidationService().validatePdf(IOUtils.toByteArray(new FileInputStream(
                "src/test/resources/dss/signed_after_seal.pdf")),null, ss,portal.getDocumentValidationConfig(), new OnlineCRLSource(), portal.getPDFDocumentName(getRequest()));
        assertFalse(ss.getWarningStatusCodes().contains(MessagesEnum.dss_applet_message_signed_before_seal));
    }
    /**
     * Test validation of untrusted signature
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    @Test
    public void tamperedSignatureTest() throws Exception {

        byte[] doc2 = PdfUtils.sealPDFCustom(IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE)), SealedPDFService.getInstance().getToken(), "disclaimer");
        PdfUtils.appendAttachment(IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE)), "attachment".getBytes(), "break_the_doc");
        OutputStream os2 = new FileOutputStream("target/hello-world-signed-2.pdf");
        IOUtils.write(doc2, os2);
        SignatureStatus ss = new SignatureStatus();
        getDocumentValidationService().validatePdf(doc2, null,ss,portal.getDocumentValidationConfig(), new OnlineCRLSource(), portal.getPDFDocumentName(getRequest()));
        assertTrue(ss.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_from_server));


        FileInputStream fis = null;
        SignatureStatus ss2 = new SignatureStatus();
        try {
            fis = new FileInputStream("target/hello-world-signed-2.pdf");
            getDocumentValidationService().validatePdf(IOUtils.toByteArray(fis), null,ss2,portal.getDocumentValidationConfig(), new OnlineCRLSource(), portal.getPDFDocumentName(getRequest()));
            assertTrue(ss2.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_from_server));
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }

    /**
     * Test that a valid workflow file is allowed
     *
     * @throws Exception
     */
    @Test
    public void workflowFileTestPostive() throws Exception {
        
        byte[] pdf = IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE));
        SignatureStatus ss = new SignatureStatus();
        getDocumentValidationService().validatePdf(portal, getRequest(), pdf,null, ss, portal.getDocumentValidationConfig(),new OnlineCRLSource());
        assertFalse(ss.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_in_workflow));

    }

    @Test
    public void workflowFileTestDisabled() throws Exception {

        DocumentValidationService dvs = getDocumentValidationService();
        DocumentValidationConfig config = Mockito.mock(DocumentValidationConfig.class);        
        Mockito.when(config.getOriginValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getTamperedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getRevokedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSignedValidationLevel()).thenReturn(ValidationLevel.EXCEPTION);
        Mockito.when(config.getTrustedValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getWorkflowValidationLevel()).thenReturn(ValidationLevel.DISABLED);
        Mockito.when(config.getSignatureFormatValidationLevel()).thenReturn(ValidationLevel.WARN);        
        Mockito.when(config.getExpiredValidationLevel()).thenReturn(ValidationLevel.WARN);
        Mockito.when(config.getSealMethod()).thenReturn(SealMethod.SEAL);
        Mockito.when(config.getSignBeforeSealValidationLevel()).thenReturn(ValidationLevel.WARN);
        byte[] pdf = IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE));
        PortalFacade p2 = new PortalFacadeTestImpl("<not_the_xml/>", "");
        SignatureStatus ss = new SignatureStatus();
        dvs.validatePdf(p2, getRequest(), pdf,null, ss, config, new OnlineCRLSource());
        assertFalse(ss.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_in_workflow));

    }

    /**
     * Test that an invalid workflow file is detected
     *
     * @throws Exception
     */
    @Test
    public void workflowFileTestNegative() throws Exception {
        
        byte[] pdf = IOUtils.toByteArray(new FileInputStream(SEALED_PDF_FILE));
        PortalFacade p2 = new PortalFacadeTestImpl("<not_the_xml/>", "");
        SignatureStatus ss = new SignatureStatus();
        getDocumentValidationService().validatePdf(p2, getRequest(), pdf,null, ss);
        assertTrue(ss.getExceptionStatusCodes().contains(MessagesEnum.dss_applet_message_uploaded_pdf_not_in_workflow));

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
    
    private DocumentValidationService getDocumentValidationService(){
        DocumentValidationService s = DocumentValidationService.getInstance();        
        return s;
    }
    
}
