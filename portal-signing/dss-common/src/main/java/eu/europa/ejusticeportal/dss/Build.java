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
package eu.europa.ejusticeportal.dss;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Gets the build version and time; used only for logging. The information is taken from the build.properties file in
 * the class path
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class Build {

    private static final Properties P = new Properties();

    static {
        InputStream is = null;
        try {
            is = Build.class.getClassLoader().getResourceAsStream("eu/europa/ejusticeportal/dss/build.properties");
            P.load(is);
        } catch (Exception e) {
            // not so important
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                // not important
            }
        }
    }

    /**
     * 
     * The default constructor for Build.
     */
    private Build() {

    }

    /**
     * Gets the version for the build
     * 
     * @return the version
     */
    public static String getBuildVersion() {
        return P.getProperty("build.version", "?");
    }

    /**
     * Gets the timestamp for the build
     * 
     * @return the timestamp
     */
    public static String getBuildTimestamp() {
        return P.getProperty("build.timestamp", "?");
    }

}
