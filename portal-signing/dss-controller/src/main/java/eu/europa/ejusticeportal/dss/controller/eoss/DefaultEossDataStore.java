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
package eu.europa.ejusticeportal.dss.controller.eoss;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of EossDataStore is a HashMap
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DefaultEossDataStore implements EossDataStore {

    private static final Map<String, EossResponse> DATA = new HashMap<String, EossResponse>();
    /* (non-Javadoc)
     * @see eu.europa.ejusticeportal.dss.controller.eoss.EossDataStore#getResponse(java.lang.String)
     */
    @Override
    public EossResponse getResponse(String token) {
        return DATA.get(token);
    }

    /* (non-Javadoc)
     * @see eu.europa.ejusticeportal.dss.controller.eoss.EossDataStore#setResponse(java.lang.String, eu.europa.ejusticeportal.dss.controller.eoss.EossResponse)
     */
    @Override
    public void setResponse(String token, EossResponse response) {
        DATA.put(token, response);

    }

    /* (non-Javadoc)
     * @see eu.europa.ejusticeportal.dss.controller.eoss.EossDataStore#delete(java.lang.String)
     */
    @Override
    public void delete(String token) {
        DATA.remove(token);
    }

}
