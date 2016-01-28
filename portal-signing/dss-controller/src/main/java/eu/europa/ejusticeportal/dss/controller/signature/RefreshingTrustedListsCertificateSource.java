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
package eu.europa.ejusticeportal.dss.controller.signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.validation102853.CertificatePool;
import eu.europa.ec.markt.dss.validation102853.tsl.TrustedListsCertificateSource;
import eu.europa.ejusticeportal.dss.controller.CommonsHttpLoaderFactory;
import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.HttpProxyConfig;
/**
 *
 * A trusted lists certificate source that can be refreshed.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class RefreshingTrustedListsCertificateSource extends TrustedListsCertificateSource implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(RefreshingTrustedListsCertificateSource.class);
    private static RefreshingTrustedListsCertificateSource instance = new RefreshingTrustedListsCertificateSource();
    private static String lotlUrl = "https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-mp.xml";
    private static int lotlRefreshPeriod=3600;
    private static boolean lotlCheckSig;
    private static boolean initialised = false;
    private TrustedListsCertificateSource currentSource = new TrustedListsCertificateSource();

    
    private List<TrustedListsCertificateSource> cache = Collections.synchronizedList(new ArrayList<TrustedListsCertificateSource>());
    
    
    private final Timer t = new Timer();

    public static void init(HttpProxyConfig proxyConfig, DocumentValidationConfig validationConfig) {
    	CommonsHttpLoaderFactory.init(proxyConfig);
    	lotlUrl = validationConfig.getLotlUrl();
    	lotlRefreshPeriod = validationConfig.getRefreshPeriod();
    	lotlCheckSig = validationConfig.isCheckLotlSig();
    	initialised = true;
    	instance = new RefreshingTrustedListsCertificateSource();
    	instance.refresh();
    }
    
    /**
     * Gets the instance of RefreshingTrustedListsCertificateSource
     *
     * @return a RefreshingTrustedListsCertificateSource
     */
    public static RefreshingTrustedListsCertificateSource getInstance() {
    	if (!initialised) {
    		throw new IllegalStateException("The instance is not ready!");
    	}
        return instance;
    }

    /**
     * Refreshes the source
     */
    public void refresh() {
        run();
    }

    /**
     * The method refreshes the certificate source
     */
    public synchronized void run() {
    	if (instance == null) {
    		throw new IllegalStateException("The instance is not ready - invoke the init method!");
    	}
        final TrustedListsCertificateSource newSource = new TrustedListsCertificateSource();
       
        try {

            LOG.info("Reload Trusted List");
            newSource.setDataLoader(CommonsHttpLoaderFactory.getInstance().newLoader());                                               
            newSource.setCheckSignature(lotlCheckSig);
            newSource.setLotlUrl(lotlUrl);
            newSource.setLotlCertificate("classpath://ec.europa.eu.crt");
            newSource.init();
            currentSource = newSource;
        } catch (DSSException e) {
            LOG.error("DSS Exception", e);
        } 
    }

    /**
     * Gets the list of certificates
     *
     * @return the list of certificates
     */
    public CertificatePool getCertificatePool() {
        
        return currentSource.getCertificatePool();
    }

    /**
     * Gets the diagnostic information for the last refresh
     *
     * @return the diagnostic information
     */
    public Map<String, String> getDiagnosticInfo() {
        if (cache.size()==0){
            return Collections.emptyMap();
        }
        return cache.get(cache.size()-1).getDiagnosticInfo();
    }

    /**
     * Starts the refresh
     */
    public void start() {

        
        if (lotlRefreshPeriod == 0) {
            //run once
            run();
        } else {
            //periodic refresh

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    t.cancel();
                }
            });

            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    instance.run();
                }
            };

            t.schedule(task, 0L, lotlRefreshPeriod * 1000L);
        }
    }

    
    public void stop() {
        LOG.info("Stopping RefreshingTrustedListsCertificateSource");
        t.cancel();
    }

}
