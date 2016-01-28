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
package eu.europa.ejusticeportal.dss.controller.action;

import static org.junit.Assert.assertEquals;

import eu.europa.ejusticeportal.dss.controller.ValidationLevel;
import eu.europa.ejusticeportal.dss.controller.exception.DssInitialisationException;

import org.junit.Test;

/**
 *
 * Test the ValidationLevel class
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 1705 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class ValidationLevelTest {

    /**
     * Test
     *
     * @throws Exception
     */
    @Test
    public void levelTest() throws Exception {
        
        assertEquals(ValidationLevel.fromCode("E"), ValidationLevel.EXCEPTION);
        assertEquals("E", ValidationLevel.EXCEPTION.getCode());
    }
    /**
     * Test with invalid input
     */
    @Test (expected=DssInitialisationException.class)
    public void levelTestNegative1(){
        ValidationLevel.fromCode("Unknown");
    }
    /**
     * Test with invalid input
     */
    @Test (expected=DssInitialisationException.class)
    public void levelTestNegative2(){
        assertEquals(ValidationLevel.fromCode(""), ValidationLevel.DISABLED);
    }
    /**
     * Test with invalid input
     */    
    @Test (expected=DssInitialisationException.class)
    public void levelTestNegative3(){
        assertEquals(ValidationLevel.fromCode(null), ValidationLevel.DISABLED);
    }
        
    
}
