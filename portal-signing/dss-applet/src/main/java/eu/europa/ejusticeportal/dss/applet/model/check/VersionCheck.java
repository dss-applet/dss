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
package eu.europa.ejusticeportal.dss.applet.model.check;

import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

/**
 * 
 * Test if the vendor is supported
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class VersionCheck {
    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(VersionCheck.class.getSimpleName());
    /**
     * Test if the Java version is OK. This is for Internet Explorer only because 
     * we can test without launching the applet for other browsers.
     * @param fp the {@link Fingerprint}
     * @param problemVersion problematic versions. A string like 7.20-7.21,8.01-8.21.
     * @return true if the vendor string contains sun or oracle.
     */
    public boolean isOK(Fingerprint fp, String problemVersion) {
        double myVersion = Fingerprint.getJreVersion(fp);
        if (fp.getUserAgent() != null && problemVersion != null){
                String ranges [] = problemVersion.split(",");
                for (String range:ranges) {
                    String [] versions = range.split("-");
                    if (versions.length==1 && parseVersion(versions[0])==myVersion) {
                        return false;
                    } else if (versions.length == 2 && myVersion >= parseVersion(versions[0]) && myVersion<= parseVersion(versions[1])){
                        return false;
                    }
                }
        }       
        return true;
    }
    private double parseVersion(String version) {
        if (version != null) {
            try {
                return Utils.parseJreVersion(version);
            } catch (NumberFormatException e) {
                LOG.error("Version check passed invalid parameter "+version, e);
            }
        }
        return -1d;
    }
}
