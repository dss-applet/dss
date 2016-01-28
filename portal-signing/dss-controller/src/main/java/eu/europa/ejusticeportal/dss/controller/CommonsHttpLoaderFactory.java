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

import eu.europa.ec.markt.dss.manager.ProxyPreferenceManager;
import eu.europa.ec.markt.dss.validation102853.https.CommonsDataLoader;
import eu.europa.ec.markt.dss.validation102853.https.OCSPDataLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 1704 $ - $Date: 2013-09-18 07:33:00 +0200 (Wed, 18 Sep
 *          2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 *         Developments</a>
 */
public final class CommonsHttpLoaderFactory {

	private static CommonsHttpLoaderFactory instance;
	private static ProxyPreferenceManager proxyPreferenceManager;
	private static final Logger EXTERNAL_ACCESS_LOG = LoggerFactory
			.getLogger("eu.europa.ejusticeportal.dss.controller.CommonsHttpLoaderFactory.external");

	private CommonsHttpLoaderFactory() {

	}

	public static void init(HttpProxyConfig config) {
		if (instance == null) {
			initProxyPreferenceManager(config);
			instance = new CommonsHttpLoaderFactory();
		}
	}

	private static void initProxyPreferenceManager(HttpProxyConfig config) {
		proxyPreferenceManager = new ProxyPreferenceManager();
		proxyPreferenceManager.setProxyDao(new ProxyDaoImp());
		if (config.isHttpEnabled()) {
			proxyPreferenceManager.setHttpEnabled(true);
			proxyPreferenceManager.setHttpHost(config.getHttpHost());
			proxyPreferenceManager.setHttpPassword(config.getHttpPassword());
			proxyPreferenceManager.setHttpPort(Long.valueOf(config
					.getHttpPort()));
			proxyPreferenceManager.setHttpUser(config.getHttpUser());
		} else {
			proxyPreferenceManager.setHttpEnabled(false);
		}
		if (config.isHttpsEnabled()) {
			proxyPreferenceManager.setHttpsEnabled(true);
			proxyPreferenceManager.setHttpsHost(config.getHttpsHost());
			proxyPreferenceManager.setHttpsPassword(config.getHttpsPassword());
			proxyPreferenceManager.setHttpsPort(Long.valueOf(config
					.getHttpsPort()));
			proxyPreferenceManager.setHttpsUser(config.getHttpsUser());
		} else {
			proxyPreferenceManager.setHttpsEnabled(false);
		}

	}

	/**
	 * Gets the instance
	 * 
	 * @return the intance
	 */
	public static CommonsHttpLoaderFactory getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					"The instance is not ready - method init must be invoked");
		}
		return instance;
	}

	/**
	 * Gets a new CommonsHttpDataLoader
	 * 
	 * @return the loader
	 */
	public CommonsDataLoader newLoader() {
		// overriding the CommonsHttpDataLoader for the requirement to log each
		// external Internet access
		CommonsDataLoader loader = new CommonsDataLoader() {
			private static final long serialVersionUID = 1L;

			@Override
			public byte [] get(String url)  {
				EXTERNAL_ACCESS_LOG.info(url);
				return super.get(url);
			}

			@Override
			public byte[] post(String url, byte[] content) {
				EXTERNAL_ACCESS_LOG.info(url);
				return super.post(url, content);
			}
		};
		loader.setProxyPreferenceManager(proxyPreferenceManager);
		return loader;
	}
	
	   /**
     * Gets a new CommonsHttpDataLoader
     * 
     * @return the loader
     */
    public OCSPDataLoader newOcspLoader() {
        // overriding the CommonsHttpDataLoader for the requirement to log each
        // external Internet access
        OCSPDataLoader loader = new OCSPDataLoader() {
            private static final long serialVersionUID = 1L;

            @Override
            public byte [] get(String url)  {
                EXTERNAL_ACCESS_LOG.info(url);
                return super.get(url);
            }

            @Override
            public byte[] post(String url, byte[] content) {
                EXTERNAL_ACCESS_LOG.info(url);
                return super.post(url, content);
            }
        };
        loader.setProxyPreferenceManager(proxyPreferenceManager);
        return loader;
    }
}
