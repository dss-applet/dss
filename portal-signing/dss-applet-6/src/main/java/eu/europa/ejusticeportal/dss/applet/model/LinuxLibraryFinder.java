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
package eu.europa.ejusticeportal.dss.applet.model;

import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class to find shared object (.so) libraries in linux systems. The algorithm to find the objects is the same as
 * used in the dynamic loader ld. There is one exception to this rule, the ELF variables DT_RPATH and DT_RUNPATH are not
 * evaluated, because they are not available in java.
 * 
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public final class LinuxLibraryFinder {

    private static final DssLogger LOGGER = LoggingHome.getInstance().getLogger(
            LinuxLibraryFinder.class.getSimpleName());

    private LinuxLibraryFinder() {

    }

    /**
     * Finds the library path
     * 
     * @param name name of the library
     * @param is64 true if we should use a 64bit versions
     * @return The file object to the library.
     */
    public static File getLibraryPath(String name, boolean is64) throws FileNotFoundException {
        return findLibrary(name, is64);
    }

    private static File findLibrary(String name, boolean is64) {
        String findExec = "find /lib /usr/lib -name *"+name+".so*";
       
        Process p = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            p = Runtime.getRuntime().exec(findExec);
            is = p.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String next;
            while ((next = br.readLine()) != null) {
                if (is64 && !next.contains("64")){
                    continue;
                }
                File f = testFile(next);
                if (f!=null){
                    LOGGER.info("Using linux library "+f.getAbsolutePath());
                    return f;
                }
            }

        } catch (IOException ex) {
            LOGGER.info("Error looking for a library "+name,ex);
        } finally {
            if (p != null) {
                try {
                    p.getInputStream().close();
                } catch (IOException ex) {
                }
                try {
                    p.getOutputStream().close();
                } catch (IOException ex) {
                }
                try {
                    p.getErrorStream().close();
                } catch (IOException ex) {
                }
                try {
                    if (br!=null) {
                        br.close();
                    }
                } catch (IOException ex) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    
                }
            }
        }
        return null;
    }

    private static File testFile(String name) {
        File f = null;
        try {
            f = new File(name);
            if (!f.canRead()){
                f = null;
            } 
        } catch (Exception e) {
            f = null;
        }
        return f;
    }
}
