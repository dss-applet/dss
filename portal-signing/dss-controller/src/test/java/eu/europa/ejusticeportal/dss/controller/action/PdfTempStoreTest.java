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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import eu.europa.ejusticeportal.dss.common.SignatureStatus;
import eu.europa.ejusticeportal.dss.common.SignedForm;

/**
 *
 * Test the PdfTempStoreTest class
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 1704 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class PdfTempStoreTest {

    /**
     * Test the store.
     *
     * @throws Exception
     */
    @Test
    public void storeTest() throws Exception {
        HttpServletRequest r = getRequest();
        byte[] bytes = new byte[] { 1, 2, 3, 1, 2, 3, 1, 2, 3 };
        SignedForm form = new SignedForm(new SignatureStatus(),bytes,bytes);
        PdfTempStore.storeSignedForm(r, form);
        assertTrue(PdfTempStore.getSignedForm(r).getDocument().length == bytes.length);
        PdfTempStore.storeSignedForm(r, form);
        assertTrue(PdfTempStore.getSignedForm(r).getDocument().length == bytes.length);
        PdfTempStore.deleteSignedForm(r);
        assertNull(PdfTempStore.getSignedForm(r));
        PdfTempStore.deleteSignedForm(r);
    }

    private HttpServletRequest getRequest() {
    	return new MockHttpServletRequest();
    }

    private HttpServletResponse getResponse(final OutputStream os) throws IOException {
        return new MockHttpServletResponse();
    }

}
