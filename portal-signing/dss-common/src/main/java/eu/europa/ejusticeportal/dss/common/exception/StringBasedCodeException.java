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

import java.util.Arrays;

/**
 * CodeException implementation, based on enumerations.
 *
 * @author detailoc
 *
 */
public class StringBasedCodeException extends CodeException {

    private static final long serialVersionUID = 1L;
    private final String code;
    private final Object[] objects;

    /**
     * Get the code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the objects
     *
     * @return the objects
     */
    public Object[] getObjects() {
        return objects;
    }

    /**
     * Use this constructor to wrap an exception raised at a lower level, and
     * also provide an additional message.
     *
     * @param cause
     * @param code
     * @param objects
     */
    public StringBasedCodeException(String message, Throwable cause, String code, Object... objects) {
        super(message + " " + Arrays.asList(objects), cause);
        this.code = code;
        this.objects = objects;
    }

    /**
     * Use this constructor to wrap an exception raised at a lower level,
     * exception message will set to "code" + "object list".
     *
     * @param cause
     * @param code
     * @param objects
     */
    public StringBasedCodeException(Throwable cause, String code, Object... objects) {
        super(code + " " + Arrays.asList(objects), cause);
        this.code = code;
        this.objects = objects;
    }

    /**
     * Use this constructor for the application code to throw an exception, that
     * is not caused by an exception raised at a lower level.
     *
     * @param code
     * @param objects
     */
    public StringBasedCodeException(String code, Object... objects) {
        super(code + " " + Arrays.asList(objects));
        this.code = code;
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "code=" + code + ", objects=" + Arrays.toString(objects);
    }
}
