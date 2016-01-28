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
package eu.europa.ejusticeportal.dss.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.europa.ec.markt.dss.dao.ProxyDao;
import eu.europa.ec.markt.dss.dao.ProxyKey;
import eu.europa.ec.markt.dss.dao.ProxyPreference;

/**
 * 
 * Implementation of ProxyDao as a map of values.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1704 $ - $Date: 2014-04-25 18:44:40 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class ProxyDaoImp implements ProxyDao{

    private static final Map<ProxyKey, ProxyPreference> DATA = new HashMap<ProxyKey, ProxyPreference>();
    
    static {
        //the ProxyPreferenceManager expects all of the entries to be there in order to set them.
        for (ProxyKey key:ProxyKey.values()){
            ProxyPreference p = new ProxyPreference();
            p.setProxyKey(key);
            DATA.put(key, p);
        }
    }
    
    @Override
    public ProxyPreference get(ProxyKey id) {
        return DATA.get(id);
    }

    @Override
    public Collection<ProxyPreference> getAll() {
        return DATA.values();
    }

    @Override
    public void update(ProxyPreference entity) {
        DATA.put(entity.getProxyKey(), entity);        
    }
}
