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
package eu.europa.ejusticeportal.dss.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import eu.europa.ejusticeportal.dss.controller.PortalFacade;

/**
 * Test the SigningMethodsHome class
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith (JUnit4.class)
public class SigningMethodsHomeTest {

    /**
     * Test filter by platform.
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testByPlatform() throws UnsupportedEncodingException, IOException{
        PortalFacade portal = Mockito.mock(PortalFacade.class);
        InputStream is = SigningMethodsHomeTest.class.getClassLoader().getResourceAsStream("TestSigningRepo.xml");
        
        Mockito.when(portal.getCardProfileXML()).thenReturn(new String(IOUtils.toByteArray(is),"UTF-8"));
       
        Map<String,List<SigningMethod>> methods  = SigningMethodsHome.getInstance().getSigningMethods(portal,"blah blah windows blah blah");              
        Map<String,List<SigningMethod>> methods2  = SigningMethodsHome.getInstance().getSigningMethods(portal,"blah blah linux blah blah");
        Map<String,List<SigningMethod>> methods3  = SigningMethodsHome.getInstance().getSigningMethods(portal);
        
        assertEquals(1,getMethodsCount(methods3)-getMethodsCount(methods2));
        assertEquals(getMethodsCount(methods3),getMethodsCount(methods));
    }

	private int getMethodsCount(Map<String, List<SigningMethod>> methods) {
		int count = 0;
		for (List<SigningMethod> ms:methods.values()){
			count += ms.size();
		}
		return count;
	}
}
