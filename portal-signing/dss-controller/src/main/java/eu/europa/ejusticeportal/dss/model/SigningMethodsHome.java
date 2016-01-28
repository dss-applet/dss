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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.config.Config;

/**
 * Provides information about the supported signing methods
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class SigningMethodsHome {

    private static final SigningMethodsHome INSTANCE = new SigningMethodsHome();

    /**Code for signing by external means then upload to portal*/
    public static final String SIGN_BY_OTHER_SERVICE = "ext";  
    
    private SigningMethodsHome() {

    }

    public static SigningMethodsHome getInstance() {
        return INSTANCE;
    }


    /**
     * Get all the available signing methods
     * @return the signing methods
     */
    public Map<String,List<SigningMethod>> getSigningMethods(PortalFacade portal) {
        return Config.getInstance().getSigningMethods(portal);
    }

    /**
     * Get the signing method from the code
     * @param signingMethodCode the code
     * @return the SigningMethod
     */
    public SigningMethod getMethod(PortalFacade portal,String signingMethodCode) {
    	for (List<SigningMethod> signingMethods: Config.getInstance().getSigningMethods(portal).values()){
	        for (SigningMethod m: signingMethods){
	            if (m.getCode().equals(signingMethodCode)){
	                return m;
	            }
	        }
    	}
        throw new IllegalArgumentException("The code is not implemented "+signingMethodCode);
    }
    


    /**
     * @param platform - this indicates the current platform (it could be the User-Agent string
     * @return the list of methods filtered by platform.
     */
    public Map<String,List<SigningMethod>> getSigningMethods(PortalFacade portal, String platform) {

        return filterByPlatform(Config.getInstance().getSigningMethods(portal), platform);
    }
    /**
     * Filter the methods by user agent
     * @param signingMethodsMap the methods
     * @param ua the User-Agent
     * @return the filtered list of methods
     */
    private Map<String,List<SigningMethod>> filterByPlatform(Map<String,List<SigningMethod>> signingMethodsMap, String ua) {
    	Map<String,List<SigningMethod>> newMap = new HashMap<String, List<SigningMethod>>();
    	String header = ua;
    	for (List<SigningMethod> signingMethods:signingMethodsMap.values()){
    		List<SigningMethod> sms = null;
	        if (header != null) {
	            header = header.toLowerCase();
	            sms = new ArrayList<SigningMethod>();
	            for (SigningMethod sm:signingMethods){
	                if (sm.getPlatforms()==null||sm.getPlatforms().isEmpty()){
	                    sms.add(sm);
	                } else {
	                    for (String platform:sm.getPlatforms()){
	                        if (header.contains(platform)){
	                            sms.add(sm);
	                            break;
	                        }
	                    }
	                }
	            }
	        } else {
	            sms = signingMethods;
	        }
	        if (sms!=null && !sms.isEmpty()){
	        	newMap.put(sms.get(0).getCategory(), sms);
	        }
    	}
        return newMap;
    }
}
