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
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * 
 * A type of operating system
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum OS implements Serializable{

    WINDOWS("windows", ".*win.*", ".*win.*"), LINUX("linux", ".*linux.*", ".*linux.*"), MACOS("macos", ".*mac.*",
            ".*macintosh.*"), UNSUPPORTED("unsupported", "", "");
    private final String name;
    private final Pattern sysPropPat;
    private final Pattern userAgentPat;

    /**
     * The constructor for OS.
     * 
     * @param name the name
     * @param sysPropPat pattern to find OS in the system property
     * @param userAgentPat patter to find OS in the user agent
     */
    OS(final String name, final String sysPropPat, final String userAgentPat) {
        this.name = name;
        this.sysPropPat = Pattern.compile(sysPropPat);
        this.userAgentPat = Pattern.compile(userAgentPat);
    }

    /**
     * Gets the name of the OS as found from the os.name system property
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a pattern to match the OS.
     * 
     * @param systemProperty the value of the os.name system property
     * @return the OS or null if not identified
     */
    public static OS getFromSystemProperty(final String systemProperty) {
        if (systemProperty != null) {
            String os = systemProperty.toLowerCase();
            if (WINDOWS.sysPropPat.matcher(os).matches()) {
                if (isUnsupportedWindows(os)){
                    return UNSUPPORTED;
                } else {
                    return WINDOWS;
                }
            } else if (MACOS.sysPropPat.matcher(os).matches()) {
                return MACOS;
            } else if (LINUX.sysPropPat.matcher(os).matches()) {
                return LINUX;
            }
        }
        return null;
    }

    /**
     * Gets the OS from the user agent
     * 
     * @param userAgent
     * @return the OS
     */
    public static OS getFromUserAgent(final String userAgent) {
        if (userAgent != null) {
            final String os = userAgent.toLowerCase();
            if (OS.WINDOWS.userAgentPat.matcher(os).matches()) {
                if (isUnsupportedWindows(os)) {
                    return OS.UNSUPPORTED;
                } else {
                    return OS.WINDOWS;
                }
            } else if (OS.MACOS.userAgentPat.matcher(os).matches()) {
                return OS.MACOS;
            } else if (OS.LINUX.userAgentPat.matcher(os).matches()) {
                return OS.LINUX;
            } else {
                return OS.UNSUPPORTED;
            }

        }
        return OS.UNSUPPORTED;
    }
    /**
     * Checks if a windows version is supported
     * @param os the identifier for the OS
     * @return true if it is an unsupported version.
     */
    private static boolean isUnsupportedWindows(String os){
        return os.contains("95") || os.contains("98") || os.contains("me") || os.contains("ce");        
    }
}
