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

import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * Privileged action for Dss Applet (throws Exception).
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public abstract class AbstractPrivilegedExceptionAction {

    private static final DssLogger LOG = DssLogger.getLogger(AbstractPrivilegedExceptionAction.class.getSimpleName());

    /**
     * Execute the PrivilegedAction.
     *
     * @throws CodeException
     */
    protected abstract void doExec() throws CodeException;

    /**
     * Execute the doExec() in Privileged mode.
     *
     * @throws CodeException
     */
    public void exec() throws CodeException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws CodeException {
                    doExec();
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            if (e.getException() instanceof CodeException) {
                ExceptionUtils.throwException((CodeException) e.getException(), LOG);
            } else {
                ExceptionUtils.throwException(new UnexpectedException(
                        "Operation running in 'privileged' mode failed, see nested exception.", e.getException()), LOG);
            }
        }
    }
}
