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
/*
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/isammp/isamm-pd/trunk/app/buildtools/src/main/resources/eclipse/isamm-pd-java-code-template.xml $
 * $Revision: 6522 $
 * $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * $Author: naramsda $
 */
package eu.europa.ejusticeportal.dss.controller.eoss.ampss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import eu.europa.ejusticeportal.dss.controller.eoss.EossResponse;
import eu.europa.ejusticeportal.dss.controller.eoss.ampss.AmpssParser;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests the parser for ampss
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith (JUnit4.class)
public class AmpssParserTest {

    
    /**
     * Tests an error response (the cancel response)
     * @throws Exception
     */
    @Test
    public void testError() throws Exception{
        String responseXml =  "<?xml version='1.0' encoding='UTF-8'?> <sl:ErrorResponse xmlns:sl='http://www.buergerkarte.at/namespaces/securitylayer/1.2#'> <sl:ErrorCode>6001</sl:ErrorCode><sl:Info>Cancelled by the citizen via the user interface.</sl:Info></sl:ErrorResponse>";
        AmpssParser parser = new AmpssParser();
        EossResponse response = parser.parseResponse(responseXml);        
        assertTrue(response.isCancelled());
        assertTrue(response.getErrorCode().equals("6001"));
        assertEquals("Cancelled by the citizen via the user interface.", response.getErrorMessage());
    }
    /**
     * Test the signature response
     * @throws Exception
     */
    @Test
    public void testSignature() throws Exception {
        InputStream is = AmpssParserTest.class.getClassLoader().getResourceAsStream("dss/ampsssignature.xml");        
        String s = new String (IOUtils.toByteArray(is), "UTF-8");
        AmpssParser parser = new AmpssParser();
        EossResponse response = parser.parseResponse(s);        
        
        assertFalse(response.isCancelled());
        assertTrue(response.getSignature()!=null && response.getSignature().length()>0);
    }
    /**
     * Test invalid response
     */
    @Test 
    public void testParseError(){
        AmpssParser parser = new AmpssParser();
        EossResponse response = parser.parseResponse("This is not XML !");
        assertTrue(response.isParseError());
        
    }
}
