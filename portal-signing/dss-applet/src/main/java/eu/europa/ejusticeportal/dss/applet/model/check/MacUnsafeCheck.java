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

import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedAction;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.Utils;

import java.io.File;

/**
 * 
 * Test if the applet executes on Mac and Safari without the unsafe mode
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class MacUnsafeCheck {

    /**
     * If we are on Mac and we cannot list the files on the home folder, then we are not in unsafe mode
     * and the applet is not usable.
     * @param fp the {@link Fingerprint}
     * @return true if everything is ok, false if the applet must stop.
     */
    public boolean isOK(Fingerprint fp) {
        if (OS.MACOS.equals(Utils.getOs(fp))){
            try {
                new AbstractPrivilegedAction() {                
                    @Override
                    protected void doExec() {
                        if (new File(System.getProperty("user.home")).listFiles() == null) {
                            throw new MacNotUnsafeException();
                        }                    
                    }
                }.doExec();
            } catch (MacNotUnsafeException e) {
                return false;
            }
        }        
        return true;
    }
    
    /**
     * Exception thrown when os is Mac and we are not in unsafe mode
     */
    private static class MacNotUnsafeException extends RuntimeException {
        private static final long serialVersionUID = 1L;        
    }
}
