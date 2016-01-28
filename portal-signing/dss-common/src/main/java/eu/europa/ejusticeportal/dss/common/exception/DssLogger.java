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



import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 
 * Logger class for all applet logging. The class delegates logging to a java.util.logging.Logger for normal logging as
 * configured by, for example, logging.properties. The class also writes each log entry to an InMemoryHandler, if
 * configured.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1699 $ - $Date: 2014-04-23 20:22:44 +0200 (Wed, 23 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class DssLogger {

    private final Logger logger;

    private static final InMemoryHandler HANDLER = InMemoryHandler.getInstance();

    /**
     * 
     * Construct DssLogger.
     * 
     * @param name the name of the logger for example class.getSimpleName();
     */
    private DssLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    /**
     * Log a message with parameters at a particular level
     * 
     * @param level the level
     * @param msg the message
     * @param params the message parameters
     */
    public void log(Level level, String msg, Object[] params) {
        logger.log(level, msg, params);

        if (HANDLER != null) {
            HANDLER.publish(new LogRecord(level, MessageFormat.format(msg, params)));
        }
    }

    /**
     * Log a message at a particular level
     * 
     * @param level the level
     * @param msg the message
     * @param param1 the single parameter for the message
     */
    public void log(Level level, String msg, Object param1) {
        logger.log(level, msg, param1);

        if (HANDLER != null) {
            HANDLER.publish(new LogRecord(level, MessageFormat.format(msg, param1)));
        }
    }
    /**
     * Log a message at a particular level
     * 
     * @param level the level
     * @param msg the message
     * @param t the underlying exception
     */
    public void log(Level level, String msg, Throwable t) {
        logger.log(level, msg, t);

        if (HANDLER != null) {
            HANDLER.publish(new LogRecord(level, MessageFormat.format(msg, t)));
        }
    }

    /**
     * Get a DssLogger
     * 
     * @param name the name of the logger
     * @return the logger
     */
    public static DssLogger getLogger(String name) {
        return new DssLogger(name);
    }

    /**
     * Write an info level message to the log
     * 
     * @param msg the message to write
     */
    public void info(String msg) {
        logger.info(msg);

        if (HANDLER != null) {
            HANDLER.publish(new LogRecord(Level.INFO, msg));
        }

    }

    /**
     * Checks if the underlying java.util.logging.Logger is logging this level
     * 
     * @param level the level to check
     * @return true if logging
     */
    public boolean isLoggable(Level level) {
        return logger.isLoggable(level);
    }

    /**
     * Log a message at the given level
     * 
     * @param level the level
     * @param msg the message
     */
    public void log(Level level, String msg) {
        logger.log(level, msg);

        if (HANDLER != null) {
            HANDLER.publish(new LogRecord(level, msg));
        }
    }

    /**
     * Get an anonymous logger
     * 
     * @return the logger
     */
    public static DssLogger getAnonymousLogger() {
        return new DssLogger("Anonymous");
    }

    /**
     * Log an exception
     * @param message a messaage
     * @param e a {@link Throwable}
     */
    public void error(String message, Throwable e) {
        logger.log(Level.SEVERE,message,e);
    }

    /**
     * Log a warning
     * @param message a message
     * @param e a {@link Throwable}
     */
    public void warn(String message, Throwable e) {
        logger.log(Level.WARNING,message,e);
    }

    /**
     * Log info 
     * @param message
     * @param e
     */
    public void info(String message, Throwable e) {
        logger.log(Level.INFO,message,e);
        
    }

    /**
     * Debug
     * @param message
     * @param e
     */
    public void debug(String message, Throwable e) {
        logger.log(Level.FINE,message,e);
    }

    /**
     * Log an information message
     * @param message the message
     * @param params the message parameters
     */
    public void info(String message, Object[] params) {
        logger.log(Level.INFO,message,params);
        
    }

}
