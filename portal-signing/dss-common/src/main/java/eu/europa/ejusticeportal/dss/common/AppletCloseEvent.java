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

import java.util.Date;

/**  
 * A log entry for the applet log
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class AppletCloseEvent extends DssEvent{

    private String appletLog;
    /**
     * 
     * The default constructor for AppletLogLogEntry.
     */
    public AppletCloseEvent(){
        setLogEntryType(DssEventType.AC.name());
    }
    /**
     * 
     * The constructor for AppletLogLogEntry.
     * @param log the applet log
     */
    public AppletCloseEvent(String log){        
        setLogEntryType(DssEventType.AC.name());
        setEventDate(new Date());
        appletLog = log;
    }
    /**
     * @return the Applet Log
     */
    public String getAppletLog() {
        return appletLog;
    }
    /**
     * @param appletLog the Applet Log
     */
    public void setAppletLog(String appletLog) {
        this.appletLog = appletLog;
    }
    
    
}
