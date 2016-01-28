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

import eu.europa.ejusticeportal.dss.applet.testimpl.FileChooserTestImpl;

import java.io.File;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test of FileChooserHome class
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class FileChooserHomeTest {

    /**
     * Test of class FileChooserHome.
     */
    @Test
    public void testFileChooserHome() {
        FileChooserHome instance = FileChooserHome.getInstance();
        File pkcs11 = new File("fakepkcs11");
        File pkcs12 = new File("fakepkcs12");
        File pdfopen = new File("fakepdfopen");
        File pdfsave = new File("fakepdfsave");
        instance.init(new FileChooserTestImpl(pkcs11, pkcs12, pdfopen, pdfsave));

        File expectedpkcs11 = instance.getFileChooser().choosePKCS11FileOpen();
        File expectedpkcs12 = instance.getFileChooser().choosePKCS12FileOpen();
                File expectedpdfsave = instance.getFileChooser().choosePdfFileSave("");

        assertEquals(expectedpkcs11, pkcs11);
        assertEquals(expectedpkcs12, pkcs12);
        assertEquals(expectedpdfsave, pdfsave);
    }
}
