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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import eu.europa.ejusticeportal.dss.common.Fingerprint;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test the TokenizedLibraryPath
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FingerprintHome.class)
@PowerMockIgnore( { "org.xml.*", "javax.xml.*" })
public class TokenizedLibraryPathTest {

    private static String fileSeparator = "";
    @BeforeClass
    public static void setup(){
        fileSeparator = System.getProperty("file.separator");
        System.setProperty("file.separator","\\");
    }
    @AfterClass
    public static void teardown(){
        System.setProperty("file.separator",fileSeparator);
    }
    /**
     * Test of tokenize method, of class TokenizedLibraryPath.
     */
    @Test
    public void testTokenize() {
        PowerMockito.mockStatic(FingerprintHome.class);
        FingerprintHome fph = Mockito.mock(FingerprintHome.class);
        Fingerprint fp = new Fingerprint();
        fp.setOs("windows");
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fph);
        Mockito.when(fph.getFingerprint()).thenReturn(fp);

        String fileSeparator = System.getProperty("file.separator");
        String absolutePath = "c:/TEST/tata*titi/*/tata*/*titi/si*.dll";
        List<String> expectedSubDir = new ArrayList<String>();
        expectedSubDir.add("tata*titi");
        expectedSubDir.add("*");
        expectedSubDir.add("tata*");
        expectedSubDir.add("*titi");

        TokenizedLibraryPath instance = new TokenizedLibraryPath();
        instance.tokenize(absolutePath);
        assertNotNull(instance.getFileName());
        assertNotNull(instance.getRootDirPath());
        assertNotNull(instance.getWildcardSubDirPath());
        assertEquals(instance.getFileName(), "si*.dll");
        assertEquals(instance.getRootDirPath(), "c:" + fileSeparator + "TEST" + fileSeparator);
        assertArrayEquals(instance.getWildcardSubDirPath().toArray(), expectedSubDir.toArray());

        fp.setOs("linux");
        System.setProperty("file.separator", "/");
        fileSeparator = System.getProperty("file.separator");
        Mockito.when(FingerprintHome.getInstance()).thenReturn(fph);
        Mockito.when(fph.getFingerprint()).thenReturn(fp);
        absolutePath = "/opt/TEST/tata*titi/*/tata*/*titi/si*.so";
        instance = new TokenizedLibraryPath();
        instance.tokenize(absolutePath);
        assertNotNull(instance.getFileName());
        assertNotNull(instance.getRootDirPath());
        assertNotNull(instance.getWildcardSubDirPath());
        assertEquals(instance.getFileName(), "si*.so");
        assertEquals(instance.getRootDirPath(), "/opt" + fileSeparator + "TEST" + fileSeparator);
        assertArrayEquals(instance.getWildcardSubDirPath().toArray(), expectedSubDir.toArray());
    }
}
