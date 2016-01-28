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

import static org.junit.Assert.assertTrue;

import eu.europa.ejusticeportal.dss.controller.PortalFacade;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * Test the GetDigest class
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DownloadSignedPdfTest {

    private static final String TEST_XML = "<test>here is some xml for testing</test>";
    private static PortalFacade portal;

    /**
     * Initialises the trusted list store and creates the sealed PDF for the other tests.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void createSealedPDF() throws Exception {
        portal = new PortalFacadeTestImpl(TEST_XML, "dss/hello-world.pdf");
    }

    /**
     * Test downloading the pdf
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test
    public void downloadPdfTest() throws FileNotFoundException, IOException {
        HttpServletRequest r = getRequest();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpServletResponse p = getResponse(os);
        new DownloadSealedPdf().elaborate(portal, r, p);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        PDDocument doc = PDDocument.load(is);
        
        assertTrue(doc.getSignatureFields().isEmpty());
    }

    private HttpServletRequest getRequest() {
    	HttpServletRequest r  = Mockito.mock(HttpServletRequest.class);
    	HttpSession session = Mockito.mock(HttpSession.class);
    	Mockito.when(r.getSession()).thenReturn(session);
        return r;
    }

    private HttpServletResponse getResponse(final OutputStream os) throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getOutputStream()).thenReturn(new ServletOutputStreamImpl(os));        return response;
    }
}
