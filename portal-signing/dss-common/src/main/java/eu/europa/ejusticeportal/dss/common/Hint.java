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
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;
import java.util.List;

/**
 * A {@link Hint} contains information to help choose a recommended certificate.
 * It's useful when there are more than one certificate in a smart card, or we
 * are not sure if the certificate comes from a smart card (i.e. MSCAPI).
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 339 $ - $Date: 2012-10-23 13:30:50 +0200 (mar., 23 oct.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class Hint implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HintElement element;
	private List<String> patterns;
	
	public Hint () {
		
	}
	public HintElement getElement() {
		return element;
	}
	public void setElement(HintElement element) {
		this.element = element;
	}
	public List<String> getPatterns() {
		return patterns;
	}
	public void setPatterns(List<String> patterns) {
		this.patterns = patterns;
	}
	@Override
	public String toString() {
		return "Hint [element=" + element + ", patterns=" + patterns + "]";
	}

}
