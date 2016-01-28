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
package eu.europa.ejusticeportal.dss.controller;


/**
 * Configuration of HTTP Proxy used by signature validation services/ refreshing trusted lists
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public interface HttpProxyConfig {

	/**
	 * @return true if the proxy for HTTP is enabled
	 */
	boolean isHttpEnabled();
	/**
	 * 
	 * @return the HTTP proxy host
	 */
	String getHttpHost();
	/**
	 * 
	 * @return the HTTP proxy port
	 */
    int getHttpPort();
    /**
     * 
     * @return the HTTP proxy user name
     */
    String getHttpUser();
    /**
     * 
     * @return the HTTP proxy password
     */
    String getHttpPassword();
    

	/**
	 * @return true if the proxy for HTTPS is enabled
	 */
	boolean isHttpsEnabled();
	/**
	 * 
	 * @return the HTTPS proxy host
	 */
	String getHttpsHost();
	/**
	 * 
	 * @return the HTTPS proxy port
	 */
    int getHttpsPort();
    /**
     * 
     * @return the HTTPS proxy user name
     */
    String getHttpsUser();
    /**
     * 
     * @return the HTTPS proxy password
     */
    String getHttpsPassword();
    
}
