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

import eu.europa.ec.markt.dss.EncryptionAlgorithm;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Sorts the {@link CertificateDisplayDetails} for the user interface
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 *         Developments</a>
 */
public class CertificateDisplayDetailsComparator implements Comparator<CertificateDisplayDetails>, Serializable {

    private static final long serialVersionUID = 1L;
    private static Map<EncryptionAlgorithm,Short> algos = new HashMap<EncryptionAlgorithm,Short>();
    static {
        algos.put(EncryptionAlgorithm.RSA, Short.valueOf("1"));
        algos.put(EncryptionAlgorithm.HMAC, Short.valueOf("2"));
        algos.put(EncryptionAlgorithm.DSA, Short.valueOf("3"));
        algos.put(EncryptionAlgorithm.ECDSA, Short.valueOf("4"));
    }
	@Override
	public int compare(CertificateDisplayDetails c1,
			CertificateDisplayDetails c2) {
	    if (c1.isDateValid()&&!c2.isDateValid()) {
	        return -1;
	    } else if (!c1.isDateValid() && c2.isDateValid()){
	        return 1;
	    } else if (c1.isRecommended()&&!c2.isRecommended()){
	        return 1;
	    } else if (!c1.isRecommended()&&c2.isRecommended()){
	        return -1;
	    } else {
	        return sortInGroup(c1,c2);
	    }    
	}
	
	private int sortInGroup(CertificateDisplayDetails c1, CertificateDisplayDetails c2) {

	    String algo1 = c1.getCertificate().getPublicKey().getAlgorithm();
	    String algo2 = c2.getCertificate().getPublicKey().getAlgorithm();
	    EncryptionAlgorithm ea1 = null;
	    EncryptionAlgorithm ea2 = null;
	    try {
	        ea1 = EncryptionAlgorithm.forName(algo1);
	        ea2 = EncryptionAlgorithm.forName(algo2);
	        if (ea1.equals(ea2)){
	            return c1.getName().compareTo(c2.getName());
	        } else {
	            Short order1 = algos.get(ea1);
	            Short order2 = algos.get(ea2);
	            if (order1!=null && order2 !=null){
	                return order1.compareTo(order2);
	            }
	        }
	    } catch (DSSException e){
	        ea1 = null;
	        ea2 = null;
	    }
	    return 0;
    }
}
