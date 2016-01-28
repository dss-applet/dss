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
package eu.europa.ejusticeportal.dss.demo.web.controller.sign;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import eu.europa.ejusticeportal.dss.controller.CommonsHttpLoaderFactory;
import eu.europa.ejusticeportal.dss.controller.HttpProxyConfig;
import eu.europa.ejusticeportal.dss.controller.exception.DssInitialisationException;
/**
 * Default implementation of the HTTP Proxy configuration
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DefaultHttpProxyConfig implements HttpProxyConfig{

	private boolean httpEnabled;
	private String httpHost;
	private String httpPassword;
	private int httpPort;
	private String httpUser;
			        
	private boolean httpsEnabled;
	private String httpsHost;
	private String httpsPassword;
	private int httpsPort;
	private String httpsUser;

	private static DefaultHttpProxyConfig instance;
	static {
	
	instance = new DefaultHttpProxyConfig();
	
	Properties p = new Properties();
    InputStream is = null;
    try {
        is = CommonsHttpLoaderFactory.class.getClassLoader().getResourceAsStream("dss_http_proxy.properties");
        p.load(is);
        
        if (Boolean.valueOf(p.getProperty("HttpEnabled", "FALSE"))){
            instance.httpEnabled=true;
            instance.httpHost=p.getProperty("HttpHost");
            instance.httpPassword=p.getProperty("HttpPassword");
            instance.httpPort = Integer.valueOf(p.getProperty("HttpPort"));
            instance.httpUser = p.getProperty("HttpUser");
        } else {
            instance.httpEnabled = false;
        }
        if (Boolean.valueOf(p.getProperty("HttpsEnabled", "FALSE"))){
            instance.httpsEnabled=true;
            instance.httpsHost=p.getProperty("HttpsHost");
            instance.httpsPassword=p.getProperty("HttpsPassword");
            instance.httpsPort = Integer.valueOf(p.getProperty("HttpsPort"));
            instance.httpsUser = p.getProperty("HttpsUser");
        } else {
            instance.httpsEnabled = false;
        }
    } catch (Exception e){
        throw new DssInitialisationException("Error initialising the HTTP proxy.",e);
    }
    finally {
        IOUtils.closeQuietly(is);
    }
	}
	@Override
	public boolean isHttpEnabled() {
		return httpEnabled;
	}
	@Override
	public String getHttpHost() {
		return httpHost;
	}
	@Override
	public String getHttpPassword() {
		return httpPassword;
	}
	@Override
	public int getHttpPort() {
		return httpPort;
	}
	@Override
	public String getHttpUser() {
		return httpUser;
	}
	@Override
	public boolean isHttpsEnabled() {
		return httpsEnabled;
	}
	@Override
	public String getHttpsHost() {
		return httpsHost;
	}
	@Override
	public String getHttpsPassword() {
		return httpsPassword;
	}
	@Override
	public int getHttpsPort() {
		return httpsPort;
	}
	@Override
	public String getHttpsUser() {
		return httpsUser;
	}
	/**
	 * Get the Singleton instance of the class
	 * @return the instance
	 */
	public static DefaultHttpProxyConfig getInstance() {
		return instance;
	}

}
