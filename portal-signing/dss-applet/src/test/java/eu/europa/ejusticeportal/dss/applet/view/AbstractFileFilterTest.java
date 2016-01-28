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
package eu.europa.ejusticeportal.dss.applet.view;

import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.AbstractFileFilter;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.PDFFileFilter;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.PKCS11FileFilter;
import eu.europa.ejusticeportal.dss.applet.view.dialog.filter.PKCS12FileFilter;
import eu.europa.ejusticeportal.dss.common.MessageBundle;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

/**
 * 
 * Test the PKCS12 file filter
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(Parameterized.class)
public class AbstractFileFilterTest {

    private Class<AbstractFileFilter> clazz;
    private String[] fileNames;
    private boolean[] accepts;

    private int mode;
    /**
     * 
     * The constructor for PKCS12FileFilterTest.
     * 
     * @param filterCLass the filter class
     * @param fileNames the test file names
     * @param accepts expected results
     */
    public AbstractFileFilterTest(Class<AbstractFileFilter> filterCLass, String[] fileNames, boolean[] accepts, int mode) {
        this.clazz = filterCLass;
        this.fileNames = fileNames;
        this.accepts = accepts;
        this.mode = mode;
    }

    /**
     * Gets the parameters for the test
     * 
     * @return
     */
    @Parameters
    public static List<Object[]> getParameters() {

        Object o[][] = new Object[][] {
                { PKCS12FileFilter.class, new String[] { "accept.p12", "accept.P12", "not.P13", "not" },
                        new boolean[] { true, true, false, false },JFileChooser.FILES_ONLY },
                { PDFFileFilter.class, new String[] { "accept.pdf", "accept.PDF", "not.pfd", "not" },
                        new boolean[] { true, true, false, false } ,JFileChooser.FILES_ONLY},
                {
                        PKCS11FileFilter.class,
                        new String[] { "accept.dll", "accept.DLL", "accept.so", "accept.SO", "accept.dylib",
                                "accept.dylIB", "not.exe", "not" },
                        new boolean[] { true, true, true, true, true, true, false, false } , JFileChooser.FILES_AND_DIRECTORIES}

        };
        return Arrays.asList(o);
    }

    /**
     * Test the accept filter
     * 
     * @throws Exception
     */
    @Test
    public void testAccept() throws Exception {
        AbstractFileFilter filter = (AbstractFileFilter) clazz.newInstance();

        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(Boolean.TRUE);
        assertTrue(filter.accept(file));
        MessageBundle mb = new MessageBundle();
        mb.setMessages(new HashMap<String, String>());
        MessageBundleHome.getInstance().init(mb);
        int i = 0;
        for (String fileName : fileNames) {
            file = Mockito.mock(File.class);
            Mockito.when(file.isDirectory()).thenReturn(false);
            Mockito.when(file.getName()).thenReturn(fileName);
            assertTrue(filter.accept(file) == accepts[i]);
            assertTrue(filter.getDescription().length()>0);
            assertTrue(filter.getFileSelectionMode()==mode);
            i++;
        }

    }
}
