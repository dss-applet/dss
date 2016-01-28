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
package eu.europa.ejusticeportal.dss.applet.model.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

/**
 * Test of PDFHome class
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class PDFHomeTest {

    /**
     * Test of getInstance method, of class PDFHome.
     */
    @Test
    public void testGetInstance() {
        PDFHome.getInstance().setPdfName("test");
        byte[] data = "sealed".getBytes();
        byte[] data1 = "signed".getBytes();
        PDFHome.getInstance().setSealedPdf(data, new Date());
        PDFHome.getInstance().setSignedPdf(data1);

        assertEquals(PDFHome.getInstance().getPdfName(), "test");
        assertEquals(PDFHome.getInstance().getSealedPdf().length, data.length);
        assertEquals(PDFHome.getInstance().getSignedPdf().length, data1.length);
    }
}
