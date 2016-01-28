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
package eu.europa.ejusticeportal.dss.common.exception;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * 
 * A Log handler that allows the logging information to be collected in memory for upload to the server.
 * To be used for testing only.
 * Note that this class does not implement Handler to allow configuration from logging.properties because
 * the LogManager loads the handlers from the system class path, which does not include the applet jars.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class InMemoryHandler  {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private static final InMemoryHandler INSTANCE = new InMemoryHandler();
    /**
     * Create an <tt>InMemoryHandler</tt> that logs to an internal memory buffer.
     * 
     */
    private InMemoryHandler() {

    }
    
    /**
     * Get the Singleton instance of the InMemoryHandler
     * @return the instance
     */
    public static InMemoryHandler getInstance(){
        return INSTANCE;
    }
    /**
     * Gets the content of the memory log as a String.
     * 
     * @return the log as a String.
     */
    public String getLog() {
        String s;
        try {
            s = new String(baos.toByteArray(), "UTF-8");            
        } catch (UnsupportedEncodingException e) {
            s = "Error getting the log " + ExceptionUtils.getStackTrace(e);
        }
        return s;
    }

    /**The format of the message that will go in the log*/
    private static final String MSG = "[{0, date} {0, time}] :{1}\n";

    /**
     * Write the LogRecord in the log
     * @param record the LogRecord to write
     * @param sessionId 
     */
    public void publish(LogRecord record) {        
        try {
            //we only log the INFO level because other levels may contain sensitive information
            if (record.getLevel().intValue() == Level.INFO.intValue()) {
                String message = record.getMessage();
                //make multiline messages one line because we need the sessionId in each line.
                if (message!=null) {
                    message = message.replace("\r\n", "|");
                    message = message.replace("\n", "|");
                    baos.write(MessageFormat.format(MSG,new Object[]{new Date(record.getMillis()), message}).getBytes("UTF-8"));
                }                
            }
        } catch (IOException e) {
            //the exception would be an encoding exception but UTF-8 is always available.
        }
    }



    /**
     * 
     */
    public void clear() {
        baos = new ByteArrayOutputStream();
        
    }
}
