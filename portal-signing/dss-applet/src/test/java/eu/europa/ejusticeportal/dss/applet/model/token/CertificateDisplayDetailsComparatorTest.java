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
package eu.europa.ejusticeportal.dss.applet.model.token;

import static org.junit.Assert.*;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;

/**
 * Test for {@link CertificateDisplayDetailsComparator}
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith (JUnit4.class)
public class CertificateDisplayDetailsComparatorTest {
    
    private boolean keyUsageNonRep [] = new boolean [] {true,true};
    private X509Certificate cert = Mockito.mock(X509Certificate.class);
    private PublicKey publicKey = Mockito.mock(PublicKey.class);
    private CardProfile cp = Mockito.mock(CardProfile.class);
	private CertificateDisplayDetails c_priority1a = new CertificateDisplayDetails("Subject1","Issuer1","LU", "Serial1", "hash1", true,true, keyUsageNonRep, cert, cp,"");
	private CertificateDisplayDetails c_priority1b = new CertificateDisplayDetails("Subject2","Issuer2","LU", "Serial2", "hash2", true, true,keyUsageNonRep, cert, cp,"");
	private CertificateDisplayDetails c_priority2a = new CertificateDisplayDetails("Subject3","Issuer3","LU", "Serial3", "hash3", false,true,keyUsageNonRep, cert, cp,"");
	private CertificateDisplayDetails c_priority2b = new CertificateDisplayDetails("Subject4","Issuer4","LU", "Serial4", "hash4", false,true,keyUsageNonRep, cert, cp,"");
	private CertificateDisplayDetails c_priority3a = new CertificateDisplayDetails("Subject5","Issuer5","LU", "Serial5", "hash5", false,true,keyUsageNonRep, cert, cp,"");
	private CertificateDisplayDetails c_priority3b = new CertificateDisplayDetails("Subject6","Issuer6","LU", "Serial6", "hash6", false,true,keyUsageNonRep, cert, cp,"");

	@Before
	public void init(){
	    Mockito.when(cert.getPublicKey()).thenReturn(publicKey);
	    Mockito.when(publicKey.getAlgorithm()).thenReturn("RSA");
	}
	@Test
	public void test1() {
		
		List<CertificateDisplayDetails> details = new ArrayList<CertificateDisplayDetails>();
		details.add(c_priority1a);
		details.add(c_priority1b);
		details.add(c_priority2a);
		details.add(c_priority2b);
		details.add(c_priority3a);
		details.add(c_priority3b);
	    Collections.shuffle(details);

		CertificateDisplayUtils.prepare(details, AppletSigningMethod.sc);
		
		Collections.sort(details,new CertificateDisplayDetailsComparator());
		assertTrue(details.get(0).equals(c_priority1a));
		assertTrue(details.get(1).equals(c_priority1b));
		assertTrue(details.get(5).equals(c_priority3b));
	}
}
