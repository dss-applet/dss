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
package eu.europa.ejusticeportal.dss.applet.model.service;

import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.applet.model.CardDetector;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.EnumBasedCodeException;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;

import java.util.logging.Level;

/**
 * Manage the fingerprint lifecycle.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class FingerprintHome {

    private static FingerprintHome instance;

    private FingerprintHome() {
    }

    /**
     * Get the single instance of FingerprintHome
     * 
     * @return the instance
     */
    public static FingerprintHome getInstance() {
        if (null == instance) {
            instance = new FingerprintHome();
        }
        return instance;
    }

    private static final DssLogger LOG = DssLogger.getLogger(FingerprintHome.class.getSimpleName());
    private Fingerprint fingerprint;
    private String navPlatform;
    private String userAgent;

    /**
     * Initialise Fingerprint Home with browser parameters.
     * 
     * @param navPlatform the platform
     * @param userAgent the userAgent
     */
    public void init(String navPlatform, String userAgent) {
        this.navPlatform = navPlatform;
        this.userAgent = userAgent;
        fingerprint = new Fingerprint();
        fingerprint.setNavPlatform(navPlatform);
        fingerprint.setUserAgent(userAgent);
        new EnvironmentInitAction(fingerprint).exec();
    }

    /**
     * PRE : init(...) has been previously invoked.
     * 
     * @throws CodeException CodeException is raised when applet can not use the card detection.
     */
    public void refresh() throws CodeException {
        // [0] new
        fingerprint = new Fingerprint();
        fingerprint.setNavPlatform(navPlatform);
        fingerprint.setUserAgent(userAgent);
        // [1] Environment
        new EnvironmentInitAction(fingerprint).exec();
        // [2] Cards
        if (AppletSigningMethod.sc.equals(SigningMethodHome.getInstance().getSigningMethod())){
            detectCard();
        }
    }

    /**
     * @return the fingerprint instance
     */
    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    /**
     * Gets the JRE version
     * @return the JRE version
     */
    public double getJreVersion(){
        return Fingerprint.getJreVersion(fingerprint);
    }


    private void detectCard() throws CodeException {
        LOG.info("Attempting to read cards");
        if (Fingerprint.getJreVersion(fingerprint) < 1.6) {
            LOG.info("Using Java 5 card detection");
            fingerprint.setCardDetectionAvailable(false);
        } else {
            try {
                LOG.info("Using Java 6 card detection");
                fingerprint.setCardDetectionAvailable(false);
                final CardDetector detector = (CardDetector) Class.forName(
                        JavaSixClassName.CardDetectorJre6.getClassName()).newInstance();
                fingerprint.setCardProfiles(detector.detectCard());
                fingerprint.setCardDetectionAvailable(true);
                LOG.log(Level.INFO, "{0} cards detected", fingerprint.getCardProfiles().size());

            } catch (InstantiationException ex) {
                ExceptionUtils.exception(new EnumBasedCodeException(ex,
                        MessagesEnum.dss_applet_message_card_detection_unavailable), LOG);
            } catch (IllegalAccessException ex) {
                ExceptionUtils.exception(new EnumBasedCodeException(ex,
                        MessagesEnum.dss_applet_message_card_detection_unavailable), LOG);
            } catch (ClassNotFoundException ex) {
                ExceptionUtils.exception(new EnumBasedCodeException(ex,
                        MessagesEnum.dss_applet_message_card_detection_unavailable), LOG);
            } catch (Exception ex) {
                // Technical failure, but the user can still remove the card or use PKCS12 instead.
                ExceptionUtils.exception(new EnumBasedCodeException(ex,
                        MessagesEnum.dss_applet_message_technical_failure), LOG);
            }

        }

    }
}
