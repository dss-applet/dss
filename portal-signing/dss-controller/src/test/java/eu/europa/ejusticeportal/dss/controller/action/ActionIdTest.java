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

import org.junit.Test;

/**
 *
 * Test the ActionId class
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 1704 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class ActionIdTest {

    /**
     * Test
     *
     * @throws Exception
     */
    @Test
    public void actionIdTest() throws Exception {
        assertEquals(ActionId.fromName(ActionId.AJAX_REQUEST_ID.getName()), ActionId.AJAX_REQUEST_ID);
    }

    /**
     * Test
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void actionIdNameNotValidTest() throws Exception {
        ActionId.fromName("Not a valid name");
    }

    /**
     * Test
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void actionIdNameNullTest() throws Exception {
        ActionId.fromName(null);
    }

    /**
     * Test
     *
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void actionIdNameEmptyTest() throws Exception {
        ActionId.fromName("");
    }
}
