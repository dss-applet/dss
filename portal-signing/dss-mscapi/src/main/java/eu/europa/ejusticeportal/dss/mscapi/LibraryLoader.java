/*******************************************************************************
 * Digital Signature Applet - MSCAPI Implementation Module
 *
 *  Copyright (C) 2014 European Commission, Directorate-General for Justice (DG  JUSTICE), B-1049 Bruxelles/Brussel
 *
 *  Developed by: ARHS Developments S.A. (rue Nicolas Bové 2B, L-1253 Luxembourg)  
 *
 *  http://www.arhs-developments.com
 *
 *  This file is part of the "Digital Signature Applet - MSCAPI Implementation Module" project.
 *
 *  This code is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License version 2 only, as
 *  published by the Free Software Foundation. The European Commission
 *  designates this particular file as subject to the "Classpath" exception
 *  as provided by the European Commission in the
 *  GPLv2-with-Classpath-Exception.txt file that accompanied this code.
 *
 *  This code is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  version 2 for more details (a copy is included in the
 *  GPLv2-with-Classpath-Exception.txt file that accompanied this code).
 *
 *  You should have received a copy of the GNU General Public License version
 *  2 along with this work; if not, write to the Free Software Foundation,
 *  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA or
 *  see <http://www.gnu.org/licenses/gpl-2.0.html>.
 ******************************************************************************/
package eu.europa.ejusticeportal.dss.mscapi;

import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Loads the dlls that are used by the {@link NewMSCPIDSSAction}
 */
public class LibraryLoader implements Runnable {

    // The CRT dll needed by the DLL_NAME dll. This might be available in the jre, otherwise
    // it is loaded as a resource
    private static final String CRT_DLL = "msvcr100.dll";
    private static final String DLL_NAME = "DSS4EJUST.dll";
    private static final String FOLDER_NAME = "DSS4EJUST";

    private static final LibraryLoader INSTANCE = new LibraryLoader();

    private boolean initDone;

    private static final DssLogger LOG = DssLogger.getLogger(LibraryLoader.class.getSimpleName());
    /**
     * starts the invocation
     */
    public void run() {
        if (!initDone) {
            
            try {
                init();
            } catch (Exception e) {
                throw new DSSException(e);
            }
            initDone = true;
        }
    }

   
    private void log(final String x) {
        LOG.info(x);
    }

    /**
     * Stores the DSS-dll in the respective folder
     */
    private void init()  {
        log("LibraryLoader(): start");
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File dllDir = new File(tmpDir, FOLDER_NAME);
        if (!dllDir.exists()) {
            log("LibraryLoader(): create dll-dir: start");
            if (!dllDir.mkdirs()) {
                log("LibraryLoader(): create dll-dir: failed");
            }
            log("LibraryLoader(): create dll-dir: done: " + dllDir.exists());
        } else {
            log("LibraryLoader(): dll-dir exists");
        }

        File dllFile = new File(dllDir, System.currentTimeMillis()+"-"+getRandomNumber()+DLL_NAME);
        dllFile.deleteOnExit();
        if (dllFile.exists()) {
            log("LibraryLoader(): dll exists already - delete it");
            log("LibraryLoader(): dll deleted: " + dllFile.delete());
        }
        if (!dllCopy(dllFile,DLL_NAME)) {
            log("LibraryLoader(): dll not created");
        } else {
            try {
                System.load(dllFile.getAbsolutePath());
            } catch (UnsatisfiedLinkError e) {
                LOG.error("dll not available", e);
                    File crtFile = new File(dllDir, System.currentTimeMillis()+"-"+getRandomNumber()+CRT_DLL);
                    if (!crtFile.exists()||!dllCopy(crtFile,CRT_DLL)){
                        ExceptionUtils.exception(new UnexpectedException("Not able to load "+CRT_DLL), LOG);                        
                    }
                    System.load(crtFile.getAbsolutePath());
                    System.load(dllFile.getAbsolutePath());
            }
        }

        log("LibraryLoader(): done");
    }

    /**
     * Returns a random number between 1 and 1000000000L.
     * @return a random number between 1 and 1000000000L.
     */
    public static long getRandomNumber(){
        long x = 1L;
        long y = 1000000000L;
        Random r = new Random();
        long number = x+((long)(r.nextDouble()*(y-x)));
        return number;
    }
    
    private boolean dllCopy(File target, String name) {
        InputStream in = null;
        FileOutputStream out = null;
        try {

            log("dllCopy(): start : " + target);

            out = new FileOutputStream(target);

            in = LibraryLoader.class.getClassLoader().getResourceAsStream(name);
            log("dllCopy Opened Stream");
            byte[] array = new byte[8192];
            while (true) {

                int i = in.read(array);
                log("dllCopy read bytes " + i);
                if (i < 0) {
                    break;
                }
                out.write(array, 0, i);
            }
            out.flush();
            out.close();

            log("dLLCopy(): done : " + target.exists());

            return target.exists();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error("",e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOG.error("",e);
                }
            }
        }

    }

    /**
     * Get the singleton instance of the loader
     * 
     * @return the instance
     */
    public static LibraryLoader getInstance() {
        return INSTANCE;
    }

}