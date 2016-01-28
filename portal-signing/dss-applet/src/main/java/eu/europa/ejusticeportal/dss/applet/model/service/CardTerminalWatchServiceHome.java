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
package eu.europa.ejusticeportal.dss.applet.model.service;

import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

/**
 * Home for the {@link CardTerminalWatchService}
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class CardTerminalWatchServiceHome {

 
    private static final DssLogger LOG = DssLogger.getLogger(CardTerminalWatchServiceHome.class.getSimpleName());
    private static final CardTerminalWatchServiceHome INSTANCE = new CardTerminalWatchServiceHome();
    
    private static CardTerminalWatchService service;
    private volatile boolean isRunning = false;
    
    private CardTerminalWatchServiceHome(){       
        service = newInstance();
    }
    
    private CardTerminalWatchService newInstance()  {
    	try {
    		return (CardTerminalWatchService)Class.forName(JavaSixClassName.CardTerminalWatchService.getClassName()).newInstance();
    	} catch (Exception e) {
    		LOG.error("Error starting the card terminal watch service",e);
    		return null;
    	}
    	
    }
    /**
     * Gets the instance
     * @return the instance
     */
    public static CardTerminalWatchServiceHome getInstance(){        
        return INSTANCE;
    }
    /**
     * Stops the service
     * @return the service
     */
    private void stop(){
        if (service !=null && isRunning){
            isRunning = false;
            service.shutdown();
            service = newInstance(); 
        }
    }

    /**
     * Stops the service
     * @return the service
     */
    private void start(){
        if (service !=null && !isRunning){
            isRunning = true;
            service.startup();
        }
    }

    /**
     * @param active
     */
    public void activate(boolean active) {
        if (AppletSigningMethod.sc.equals(SigningMethodHome.getInstance().getSigningMethod())){
            if (active){
                start();
            } else {
                stop();
            }
        }
    }
}
