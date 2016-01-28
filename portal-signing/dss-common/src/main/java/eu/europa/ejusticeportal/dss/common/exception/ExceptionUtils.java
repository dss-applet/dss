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


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Exception utils (log, log and throw, ...).
 * 
 * @author detailoc
 * 
 */
public final class ExceptionUtils {

    /**
     * Simply throws the exception
     * 
     * @param <T> the exception to throw
     * @param e the exception to throw
     * @param log not used
     * @throws T the exception
     */
    public static <T extends Throwable> void throwException(final T e, final DssLogger log) throws T {
        throw e;
    }

    /**
     * Throws the exception and logs it as severe
     * 
     * @param <T>
     * @param e the exception
     * @param log the logger
     * @throws T the exception
     */
    public static <T extends Throwable> void exception(final T e, final DssLogger log) throws T {
        log.log(Level.SEVERE, "{0} {1}", new Object[] { e, getStackTrace(e) });
        throw e;
    }

    /**
     * Throws the exception if the condition is true, and logs it as severe
     * 
     * @param <T>
     * @param condition the condition
     * @param e the exception
     * @param log the logger
     * @throws T the exception
     */
    public static <T extends Throwable> void conditionalException(final boolean condition, final T e, final DssLogger log)
            throws T {
        if (condition) {
            exception(e, log);
        }
    }

    /**
     * Logs the exception as severe
     * 
     * @param <T>
     * @param e the exception
     * @param log the logger
     */
    public static <T extends Throwable> void log(final T e, final DssLogger log) {
        log.log(Level.SEVERE, "{0} {1}", new Object[] { e, getStackTrace(e) });
    }

    /**
     * Filters the stack strace
     * 
     * @param t the throwable
     * @return the filtered stack trace
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        printFilteredStackTrace(pw, t);
        pw.flush();
        return sw.toString();
    }

    private ExceptionUtils() {
        super();
    }

    /**
     * Prints this throwable and its backtrace to the specified print writer.
     * 
     * @param s <code>PrintWriter</code> to use for output
     * @param t
     * @since JDK1.1
     */
    public static void printFilteredStackTrace(PrintWriter s, Throwable t) {
        synchronized (s) {
            s.println(t);
            s.println("(Stack Trace filtered from most reflection methods)");
            List<StackTraceElement> trace = filterStackTrace(t);
            for (StackTraceElement element : trace) {
                s.println("\tat " + element);
            }

            Throwable ourCause = t.getCause();
            if (ourCause != null) {
                printStackTraceAsCause(s, ourCause, trace);
            }
        }
    }

    private static void printStackTraceAsCause(PrintWriter s, Throwable cause, List<StackTraceElement> causedTrace) {

        // Compute number of frames in common between this and caused
        List<StackTraceElement> trace = filterStackTrace(cause);
        int m = trace.size() - 1, n = causedTrace.size() - 1;
        while (m >= 0 && n >= 0 && trace.get(m).equals(causedTrace.get(n))) {
            m--;
            n--;
        }
        int framesInCommon = trace.size() - 1 - m;

        s.println("Caused by: " + cause);
        for (int i = 0; i <= m; i++) {
            s.println("\tat " + trace.get(i));
        }
        if (framesInCommon != 0) {
            s.println("\t... " + framesInCommon + " more");
        }

        // Recurse if we have a cause
        Throwable ourCause = cause.getCause();
        if (ourCause != null) {
            printStackTraceAsCause(s, ourCause, trace);
        }
    }

    private static List<StackTraceElement> filterStackTrace(Throwable t) {
        StackTraceElement[] traceElements = t.getStackTrace();
        List<StackTraceElement> filteredElements = new ArrayList<StackTraceElement>();

        for (StackTraceElement element : traceElements) {
            String klass = element.getClassName();
            boolean filtered;

            if (klass.startsWith("sun.reflect.")) {
                filtered = false;
            } else if (klass.startsWith("java.lang.reflect.")) {
                filtered = false;
            } else if (klass.startsWith("org.jboss.")) {
                filtered = false;
            } else if (klass.startsWith("$Proxy")) {
                filtered = false;
            } else {
                filtered = true;
            }

            if (filtered) {
                filteredElements.add(element);
            }
        }

        return filteredElements;
    }

    /**
     * Throws a runtime exception - to be used if a method is invoked that is not implemented.
     * 
     * @throws RuntimeException
     */
    public static void shouldBeImplemented() {
        throw new IllegalStateException("Implementation can't work properly if this method is not correctly overridden");
    }

    /**
     * Throws a runtime exception - to be used if a method is not implemented
     * 
     * @throws RuntimeException
     */
    public static void notImplemented() {
        throw new IllegalStateException("not implemented (TODO)");
    }

    /**
     * Throws a runtime exception - to be used if a method should not be invoked
     * 
     * @throws RuntimeException
     */
    public static void shouldNotBeInvoked() {
        throw new IllegalStateException("should not be invoked!");        
    }

    /**
     * Get the root cause of a Throwable element.
     * 
     * @param t the Throwable element
     * @return the Throwable root cause
     */
    public static Throwable getRootCause(Throwable t) {
        if (t.getCause() == null) {
            return t;
        } else {
            return getRootCause(t.getCause());
        }
    }

    /**
     * Throw a RuntimeException
     * @param e the root cause
     * @param log the logger
     */
	public static void throwRuntimeException(Exception e, DssLogger log) {
		throw new RuntimeException(e);
	}
}
