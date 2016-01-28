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
package eu.europa.ejusticeportal.dss.applet.common;

/**
 * 
 * Enumeration of the classes which can be loaded only with Java 6 or greater.
 * This interface exists mainly for historical reasons because we wanted to support Java5
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public enum JavaSixClassName {

    /**
     * The class used to manage a Mocca connection for a token.
     */
    MOCCADSSAction("eu.europa.ejusticeportal.dss.applet.model.token.MOCCADSSAction"),
    /**
     * The class used to auto-detect the environment with smartcardio library.
     */
    CardDetectorJre6("eu.europa.ejusticeportal.dss.applet.model.CardDetectorJre6"),
    /** Class that monitors insertion/removal of cards */
    CardTerminalWatchService("eu.europa.ejusticeportal.dss.applet.service.DefaultCardTerminalWatchService"),
    /**
     * The class used for the password dialog for Mocca
     */
    MoccaPasswordInputDialog("eu.europa.ejusticeportal.dss.applet.model.MoccaPasswordInputDialog"),

    /**
     * Provider for the NEWMSCAPI api
     */
    DssMscapiProvider("eu.europa.ejusticeportal.dss.mscapi.DssMscapiProvider"),
    /**
     * Mocca card support
     */
    MoccaCardSupport("eu.europa.ejusticeportal.dss.applet.model.MoccaSupport");
    private String className;

    private JavaSixClassName(String className) {
        this.className = className;
    }

    /**
     * Get the package name of the class
     * 
     * @return the package name
     */
    public String getClassName() {
        return className;
    }
}
