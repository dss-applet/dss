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
package eu.europa.ejusticeportal.dss.applet.common;

import eu.europa.ejusticeportal.dss.applet.view.UIFunction;

/**
 * The purpose of this interface is to abstract the {@link UIFunction} calls from the implementation
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public interface UIController {

    /**
     * Execute the given {@link UIFunction} with the given function parameters
     * 
     * @param function the function to execute
     * @param params the parameters
     * @return the result of the execution
     */
    Object eval(UIFunction function, String... params);
}
