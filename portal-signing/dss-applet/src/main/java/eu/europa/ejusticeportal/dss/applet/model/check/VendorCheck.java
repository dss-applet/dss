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
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;

/**
 * 
 * Test if the vendor is supported
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class VendorCheck {
    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(VendorCheck.class.getSimpleName());
    /**
     * Test if the JRE vendor is supported (OpenJDK does not work)
     * @return true if the vendor string contains sun or oracle.
     */
    public boolean isOK() {
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                
                String vendor = System.getProperty("java.vm.name");
                if (vendor != null && vendor.toLowerCase().contains("openjdk")) {
                    vendor = "openjdk";
                }                    
                else {
                    vendor = System.getProperty("java.vendor");
                }
                LOG.log(Level.INFO, "Vendor is "+vendor);
                if (vendor!=null) {
                    vendor = vendor.toLowerCase();
                    if (!(vendor.contains("sun")||vendor.contains("oracle")||vendor.contains("apple"))){
                        LOG.log(Level.INFO,"The vendor is not supported: "+vendor);
                        return false;
                    } else {
                        LOG.log(Level.INFO,"The vendor is supported: "+vendor);
                        return true;
                    }
                }
                return true;
             }
         });        
    }
}
