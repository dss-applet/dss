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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test the FileSeeker
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(JUnit4.class)
public class FileSeekerTest {

    private static final String s = System.getProperty("file.separator");
    private static final File home = new File(System.getProperty("user.home"));
    
    private static final StringBuilder log = new StringBuilder();
    @BeforeClass
    public static void setUpTestFileStructure() throws IOException, InterruptedException{
                
        assertTrue(s!=null && s.length()!=0);
        assertTrue(home.exists());        
        File testFileStructure = new File(home,"dss_applet_test/aaa/bb bb/cc ccc/ddd dd");

        if (testFileStructure.exists()){
            FileUtils.deleteDirectory(testFileStructure);
        }
        testFileStructure = new File(home,"dss_applet_test/aaa/bb bb/cc ccc/ddd dd");
        if (!testFileStructure.exists()){
            testFileStructure.mkdirs();
        }
        assertTrue(testFileStructure.exists());
        File library = new File(testFileStructure,"library.dll");
        if (!library.exists()){
            library.createNewFile();
        }
        assertTrue(library.exists());
        
        File library2Folder = new File(home,"dss_applet_test/aaa");
        File library2 = new File(library2Folder,"pkcs11.so");
        if (!library2.exists()){
            library2.createNewFile();
        }
        assertTrue(library2.exists());
        
        File library3Folder = new File(home,"dss_applet_test/aaa/bb bb/cc ccc");
        File library3 = new File(library3Folder,"pkcs11.so");
        if (!library3.exists()){
            library3.createNewFile();
        }
        assertTrue(library3.exists());
        
        try {
            //Show the directory structure we created
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "tree", "/a","/F", new File(home,"dss_applet_test").getAbsolutePath());
        
        
            Process p = pb.start();
            InputStreamReader isr = new InputStreamReader(p.getInputStream(), Charset.forName("US-ASCII"));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line=br.readLine())!=null){
                log(line);
            }
        } catch (Exception e){
            log(e.getMessage());
        }
    }
    /**
     * Test of search method, of class FileSeeker.
     */
    @Test
    public void testSearch() {
        List<String> listLibraryPath = new ArrayList<String>();
        listLibraryPath.add("c:/TEST/tata*titi/*/tata*/*titi/si*.dll");
        listLibraryPath.add("c:/TEST/*.dll");
        listLibraryPath.add("c:/TEST/gclib*.dll");
        listLibraryPath.add("c:/TEST/gclib.dll");
        listLibraryPath.add("/opt/TEST/tata*titi/*/tata*/*titi/si*.so");
        listLibraryPath.add("gclib.dll");
        Set<String> result = FileSeeker.getInstance().search(listLibraryPath);
        assertNotNull(result);
        testPaths(result);
    }
    /**
     * Test search for the library using an exact file match
     * @throws IOException
     */
    @Test
    public void testExactFileSearch() throws IOException{
        String search = p("dss_applet_test/aaa/bb bb/cc ccc/ddd dd/library.dll");
        
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(1,paths.size());
        testPaths(paths);
        log(search, paths);
    }
    /**
     * Test search for the library using a wildcard in the library name
     * @throws IOException
     */
    @Test
    public void testWildcardLibrarySearch() throws IOException{
        String search = p("dss_applet_test/aaa/bb bb/cc ccc/ddd dd/lib*.dll");
        Set<String> paths = FileSeeker.getInstance().search(search);
        
        assertNotNull(paths);
        assertEquals(1,paths.size());
        testPaths(paths);
        log(search, paths);
    }
    /**
     * Test search for the library using more than one wildcard in the library name
     * @throws IOException
     */
    @Test
    public void testManyWildcardLibrarySearch() throws IOException{
        String search = p("dss_applet_test/aaa/bb bb/cc ccc/ddd dd/lib*y.*dl*");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(1,paths.size());
        testPaths(paths);
        log(search, paths);
    }
    /**
     * Test search for the library using a wildcard in a folder
     * @throws IOException
     */
    @Test
    public void testWildcardFolderSearch() throws IOException{
        String search = p("dss_applet_test/aaa/bb bb/cc ccc/ddd*d/library.dll");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(1,paths.size());
        testPaths(paths);
        log(search, paths);
    }
    /**
     * Test search for the library using several wildcard in a folder
     * @throws IOException
     */
    @Test
    public void testManyWildcardFolderSearch() throws IOException{
        String search = p("dss_applet_test/a*a/bb bb/cc*/d*dd*d/library.dll");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(1,paths.size());
        testPaths(paths);
        log(search, paths);
    }
    /**
     * Test search for the library using several wildcard in a folder, only wildcards
     * @throws IOException
     */
    @Test
    public void testManyAllWildcardFolderSearch() throws IOException{
        String search = p("dss_applet_test/*/*/*/*/library.dll");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(1,paths.size());
        testPaths(paths);
        log(search, paths);
    }
    /**
     * Test search for the library using several wildcard in a folder, only wildcards,
     * and some wildcards in the file name too!
     * @throws IOException
     */
    @Test
    public void testManyAllWildcardFolderSearchWithLibraryWildCard() throws IOException{
        String search = p("dss_applet_test/*/*/*/*/*.*ll");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        testPaths(paths);
        assertEquals(1,paths.size());
        log(search, paths);
        
    }
    /**
     * Test that the search does not go into sub-folders when there are no wildcards defined in the search path.
     * @throws IOException
     */
    @Test
    public void testSubFolderFileSearch() throws IOException{        
        String search = p("dss_applet_test/library.dll");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(0,paths.size());
        testPaths(paths);
        log(search, paths);
    }    
    
    /**
     * Test search expecting to find more than one file
     * @throws IOException
     */
    @Test
    public void testManyFileSearch() throws IOException{
        String search = p("dss_applet_test/**/pkcs11.so");
        Set<String> paths = FileSeeker.getInstance().search(search);
        assertNotNull(paths);
        assertEquals(2,paths.size());
        testPaths(paths);
        log(search, paths);
    }
   
    /**
     * Test each found path exists. Assertion error if the path does not exist.
     * @param paths the paths to test
     */
    private void testPaths(Set<String> paths){
        for (String path:paths){
            File file = new File(path);
            assertTrue("File did not exist "+file.getAbsolutePath(),file.exists());
        }
    }
    /**
     * Prepares the path string
     * @param p the path to prepare
     * @return the prepared path
     */
    private String p (String p){
        String search = home.getAbsolutePath()+s+p;
        return search.replace("\\", "/");
    }
    

    /**
     * Just write the log to a file.
     */
    @AfterClass
    public static void after(){
    }
    /**
     * Logs a result
     * @param l
     */
    private static void log(String l){
        log.append(l).append("\n");
    }
    
    /**
     * @param search
     * @param paths
     */
    private static void log(String search, Set<String> paths) {
        log("---------------------------------------------------");
        log("Search: "+search);
        log("Find: ");
        for (String p:paths){
            log(p);
        }
        log("---------------------------------------------------");
        log("");
    }

}
