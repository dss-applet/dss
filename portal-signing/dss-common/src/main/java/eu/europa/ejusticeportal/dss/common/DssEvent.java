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
 * 
 * This class represents a logged event concerning an activity performed by the signing service
 * that is important to allow the application managers to understand how the service is used.
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public abstract class DssEvent {

    /**
     * Type of the log entry
     */
    public static enum DssEventType{
        /**
         * Concerns a request for the SigningContext.
         */
        SC,
        
        /**
         * Concerns the attempted signature of a document.
         */
        SG,
        
        /**
         * Concerns the closure of the applet
         */
        AC,
        /**
         * Event used just to keep the session alive.
         */
        KA
    }
    public String logEntryType;    
    public Date eventDate;
    public String os;
    public String arch;
    public double jreVersion;
    
    /**
     * The default constructor for LogEntry.
     */
    public DssEvent(){
        
    }
    /**
     * 
     * The constructor for LogEntry.
     * @param type the type of the log entry
     * @param os the operating system code
     * @param arch the architecture (32 or 64)
     * @param jreVersion the JRE version
     */
    public DssEvent(String type, String os, String arch, double jreVersion){
        this(type);
        this.os = os;
        this.arch = arch;
        this.jreVersion = jreVersion;
        this.eventDate = new Date();
    }
    /**
     * 
     * The constructor for DssEvent.
     * @param type the type of the event
     */
    public DssEvent(String type){
        logEntryType = type;
        this.eventDate = new Date();
    }
    /**
     * @return the logEntryType
     */
    public DssEventType getLogEntryType() {
        return DssEventType.valueOf(logEntryType);
    }

    /**
     * @return the eventDate
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * @return the arch
     */
    public String getArch() {
        return arch;
    }

    /**
     * @return the jreVersion
     */
    public double getJreVersion() {
        return jreVersion;
    }
    /**
     * @param logEntryType the logEntryType to set
     */
    public void setLogEntryType(String logEntryType) {
        this.logEntryType = logEntryType;
    }
    /**
     * @param eventDate the eventDate to set
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate == null ? null : new Date(eventDate.getTime());
    }
    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        this.os = os;
    }
    /**
     * @param arch the arch to set
     */
    public void setArch(String arch) {
        this.arch = arch;
    }
    /**
     * @param jreVersion the jreVersion to set
     */
    public void setJreVersion(double jreVersion) {
        this.jreVersion = jreVersion;
    }
    
    
}
