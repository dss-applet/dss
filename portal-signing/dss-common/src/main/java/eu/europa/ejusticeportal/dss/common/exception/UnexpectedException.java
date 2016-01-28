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

/**
 * Something unexpected happened (throw this exception when an error code is not
 * required/relevant).
 *
 * @author detailoc
 *
 */
public class UnexpectedException extends RuntimeException implements ICodeException {

    private static final long serialVersionUID = 1L;
    public static final String UNEXPECTED = "UNEXPECTED";
    private final Object[] objects;

    /**
     * Get the code
     *
     * @return the code
     */
    public String getCode() {
        return UNEXPECTED;
    }

    /**
     *
     * A constructor for UnexpectedException.
     *
     * @param message the message
     * @param cause   the cause
     * @param objects the parameters
     */
    public UnexpectedException(String message, Throwable cause, Object... objects) {
        super(message, cause);
        this.objects = objects;
    }

    /**
     *
     * A constructor for UnexpectedException.
     *
     * @param message the message
     * @param objects the parameters
     */
    public UnexpectedException(String message, Object... objects) {
        super(message);
        this.objects = objects;
    }

    /**
     *
     * A constructor for UnexpectedException.
     *
     * @param cause   the cause
     * @param objects the parameters
     */
    public UnexpectedException(Throwable cause, Object... objects) {
        super(UNEXPECTED, cause);
        this.objects = objects;

    }

    /**
     * Gets the objects
     *
     * @return the objects
     */
    public Object[] getObjects() {
        return objects;
    }
}
