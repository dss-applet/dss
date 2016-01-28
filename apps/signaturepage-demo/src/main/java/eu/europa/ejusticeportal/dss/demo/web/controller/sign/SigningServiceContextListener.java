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
package eu.europa.ejusticeportal.dss.demo.web.controller.sign;

import eu.europa.ejusticeportal.dss.controller.signature.RefreshingTrustedListsCertificateSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet context listener that initialises some configuration and resources needed by the
 * DSS services
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SigningServiceContextListener implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(SigningServiceContextListener.class);
	
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("Starting RefreshingTrustedListsCertificateSource");        
        RefreshingTrustedListsCertificateSource.init(DefaultHttpProxyConfig.getInstance(),new DefaultDocumentValidationConfig());
        RefreshingTrustedListsCertificateSource.getInstance().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Stopping RefreshingTrustedListsCertificateSource");
        RefreshingTrustedListsCertificateSource.getInstance().stop();
    }

    
}
