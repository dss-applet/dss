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
/*
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/dgjustice-dss/tags/portal-v8.10.3_applet-v0.14.2-QTM3+4MaintenanceContract/portal-signing/dss-applet/src/main/java/eu/europa/ejusticeportal/dss/applet/controller/ui/UISynchEventException.java $
 * $Revision: 1697 $
 * $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * $Author: MacFarPe $
 */
package eu.europa.ejusticeportal.dss.applet.controller.ui;

/**
 * An exception to throw when there is an error in synchronously handling a UI event
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1697 $ - $Date: 2014-04-23 20:22:00 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class UISynchEventException extends RuntimeException{
    
    private static final long serialVersionUID = 1L;

    /**
     * 
     * The constructor for UISynchEventException.
     * @param message the error message
     */
    public UISynchEventException(String message){
        super(message);
    }

    /**
     * 
     * The constructor for UISynchEventException.
     * @param message the error message
     * @param t the underlying exception
     */
    public UISynchEventException(String message, Throwable t){
        super(message,t);
    }
}
