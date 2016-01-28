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
import static junit.framework.Assert.assertTrue;

import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.SignedDocumentValidator;
import eu.europa.ec.markt.dss.validation102853.pades.PDFDocumentValidator;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.SealedPDF;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.TestUtils;
import eu.europa.ejusticeportal.dss.controller.action.SigningAction.DataWrapper;
import eu.europa.ejusticeportal.dss.controller.signature.PdfUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.gson.Gson;

/**
 *
 * Test the GetSealedPDF class
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
public class GetSealedPdfTest {

    private static final String TEST_XML = ByteOrderMark.UTF_8 + "<test>here is some &amp; xml for testing</test>";
    private PortalFacade portal;

    /**
     * Initialises
     */
    @Before
    public void init() {
        portal = new PortalFacadeTestImpl(TEST_XML, "dss/hello-world.pdf");
    }

    /**
     * Test getting the document signed with embedded XML.
     * Uses p12 resources from the classpath
     * @throws Exception
     */        
    @Test
    public void getSealedPDFTest_classpath() throws Exception {
        System.setProperty(SealedPDFService.PASSWORD_FILE_PROPERTY, "classpath:server.pwd");
        System.setProperty(SealedPDFService.CERTIFICATE_FILE_PROPERTY, "classpath:server.p12");
        getSealedPdf();
    }
    
    /**
     * Test getting the document signed with embedded XML.
     * Uses p12 resources from the fileSystem
     * @throws Exception
     */        
    @Test
    public void getSealedPDFTest_filesystem() throws Exception {
        InputStream is = GetSealedPdfTest.class.getClassLoader().getResourceAsStream("server.pwd");
        File f = File.createTempFile("test", "pwd");
        FileOutputStream fos = new FileOutputStream(f);
        IOUtils.copy(is, fos);        
        String url = "file:///"+f.getAbsolutePath().replace('\\', '/');
        System.setProperty(SealedPDFService.PASSWORD_FILE_PROPERTY, url);
        
        InputStream is2 = GetSealedPdfTest.class.getClassLoader().getResourceAsStream("server.p12");
        File f2 = File.createTempFile("test", "pwd");
        FileOutputStream fos2 = new FileOutputStream(f2);
        IOUtils.copy(is2, fos2);
        String url2 = "file:///"+f2.getAbsolutePath().replace('\\', '/');
        System.setProperty(SealedPDFService.CERTIFICATE_FILE_PROPERTY, url2);
        
        getSealedPdf();
        is.close();
        is2.close();
        fos.close();
        fos2.close();
        f.delete();
        f2.delete();
    }
    
    
    @SuppressWarnings("unchecked")
    private void getSealedPdf() throws Exception{
        GetSealedPdf d = new GetSealedPdf();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpServletRequest request = getRequest(); 
        request.getParameterMap().put(SigningAction.DATA_PARAM, new String[]{Utils.toString(new Fingerprint())});
        ServletOutputStream sos = new ServletOutputStreamImpl(baos);        
        d.elaborate(portal, request, getResponse(sos));
        String s = new String(baos.toByteArray(),"UTF-8");
        Gson gson = new Gson();
        DataWrapper w = gson.fromJson(s, DataWrapper.class);
        SealedPDF spdf = (SealedPDF) Utils.fromString(w.getData());
        byte [] doc = Base64.decodeBase64(spdf.getPdfBase64());
        OutputStream os = new FileOutputStream("target/hello-world-sealed.pdf");
        IOUtils.write(doc, os);
        PDFDocumentValidator validator = (PDFDocumentValidator) SignedDocumentValidator
                .fromDocument(new InMemoryDocument(doc));

        validator.setCertificateVerifier(new CommonCertificateVerifier(null,null,null,null));
        doc = PdfUtils.signPdf(doc, SealedPDFService.getInstance().getToken());
        assertTrue(TestUtils.isSealed(null, doc, SealedPDFService.getInstance().getToken(), SealMethod.SEAL_CUSTOM));
        String testXML = new String(PdfUtils.extractAttachment(IOUtils.toByteArray(new FileInputStream(
                "target/hello-world-sealed.pdf")), SealedPDFService.EMBEDDED_XML_ID), "UTF-8");
        assertEquals(testXML, GetSealedPdfTest.TEST_XML);
        
    }

    private HttpServletRequest getRequest() {

        return new MockHttpServletRequest(){
        	Map<String,String[]> map = new HashMap<String, String[]>();
        	public Map<String, String[]> getParameterMap(){
        		return map;
        
        	}
            public String getParameter(String key){
            	return map.get(key)[0];
            }
        };
        
        
    }

    private HttpServletResponse getResponse(final ServletOutputStream os) throws IOException {
       return new MockHttpServletResponse() {

		@Override
		public ServletOutputStream getOutputStream() {
			return os;
		}
    	   
       };
    }
}
