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
 * Default implementation of ICodeException.
 *
 * @author detailoc
 *
 */
@SuppressWarnings("serial")
public abstract class CodeException extends Exception implements ICodeException {

    /**
     *
     * The default constructor for CodeException.
     */
    public CodeException() {
        super();
    }

    /**
     *
     * A constructor for CodeException.
     *
     * @param message a message
     * @param cause   the cause of the exception
     */
    public CodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * A constructor for CodeException.
     *
     * @param message a message
     */
    public CodeException(String message) {
        super(message);
    }
}
