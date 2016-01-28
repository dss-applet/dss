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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import eu.europa.ejusticeportal.dss.common.SignedForm;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;

/**
 * 
 * Test StoreSignedPdf
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1704 $ - $Date: 2014-04-25 18:44:40 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class StoreSignedPdfTest {
    private static final String TEST_XML = "<test>here is some xml for testing</test>";
    private PortalFacade portal = new PortalFacadeTestImpl(TEST_XML, "dss/hello-world.pdf");

    /**
     * Test the store
     */
    @Test
    public void storePdfTest() {
        StoreSignedPdf s = new StoreSignedPdf();
        HttpServletRequest r = getRequest();
        PdfTempStore.storeSignedForm(r, new SignedForm());
        assertTrue(s.storePdf(portal, r));
        assertTrue(PdfTempStore.getSignedForm(r) == null);
    }

    /**
     * Test the store with null document
     */
    @Test
    public void storeNullPdfTest() {
        StoreSignedPdf s = new StoreSignedPdf();
        HttpServletRequest r = getRequest();
        assertFalse(s.storePdf(portal, r));
        assertTrue(PdfTempStore.getSignedForm(r) == null);
    }

    private HttpServletRequest getRequest() {
    	return new MockHttpServletRequest();
    }

    private HttpServletResponse getResponse(final OutputStream os) throws IOException {
        return new MockHttpServletResponse();
    }

}
