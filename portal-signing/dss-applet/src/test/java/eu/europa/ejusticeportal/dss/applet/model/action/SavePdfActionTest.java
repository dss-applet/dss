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
package eu.europa.ejusticeportal.dss.applet.model.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test the SavePdfAction
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SavePdfActionTest {

    /**
     * Test the doExec method for SavePdfAction
     */
    @Test
    public void testDoExec() {
        FileInputStream fis = null;
        FileInputStream result = null;
        SavePdfAction instance;
        try {
            fis = new FileInputStream("src/test/resources/hello-world.pdf");
            byte[] pdf = (IOUtils.toByteArray(fis));
            instance = new SavePdfAction(pdf, null);
            instance.exec();

            File f = File.createTempFile("test", ".pdf");
            f.deleteOnExit();
            instance = new SavePdfAction(pdf, f);
            instance.exec();
            assertNotNull(f);
            assertTrue(f.exists());
            assertTrue(f.canRead());
            assertTrue(f.canWrite());

            result = new FileInputStream(f);
            byte[] resultPdf = (IOUtils.toByteArray(result));
            assertTrue(resultPdf.length > 0);

        } catch (FileNotFoundException ex) {
            fail("Hello-world.pdf is not available.");
        } catch (IOException ex) {
            fail("IO Issue");
        } finally {
            try {
                fis.close();
                result.close();
            } catch (IOException ex) {
                fail("Unable to close File stream");
            }
        }
    }
}
