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

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.applet.common.JavaSixClassName;
import eu.europa.ejusticeportal.dss.applet.event.CardAdvice;
import eu.europa.ejusticeportal.dss.applet.event.SigningMethodChanged;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Defaults;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manage the lifecycle of the SigningContext.
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class SigningContextHome {


    // The minimum JRE for MSCAPI sup
    private static final double MIN_JRE_FOR_MSCAPI = 1.6;
    private static final DssLogger LOG = DssLogger.getLogger(SigningContextHome.class.getSimpleName());
    private static SigningContextHome instance;
    public static final String CARD_UNKNOWN_MSCAPI = "Card unknown (MSCAPI)";
    public static final String CARD_UNKNOWN_PKCS11 = "Card unknown (PKCS11)";
    

    private SigningContextHome() {
    }

    /**
     * Get the single instance of SigningContextHome
     * 
     * @return the instance
     */
    public static SigningContextHome getInstance() {
        if (null == instance) {
            instance = new SigningContextHome();
        }
        return instance;
    }

    private SigningContext signingContext;

    /**
     * Initialise the SigningContextHome with a SigningContext
     * 
     * @param signingContext
     */
    public void init(SigningContext signingContext) {
        this.signingContext = signingContext;
        correct();
        Event.getInstance().fire(new CardAdvice());
        Event.getInstance().fire(new SigningMethodChanged(SigningMethodHome.getInstance().getSigningMethod()));

    }

    /**
     * Gets the card provider URL
     * @return the URL, or null if there is none
     */
    public String getCardProviderURL() {

        String url = null;
        for (CardProfile cp : signingContext.getCardProfiles()) {
            if ((cp.getUrl() != null) && (cp.getUrl().length() != 0)) {
                url = cp.getUrl();
                break;
            }
        }
        return url;
    }

    /**
     * @return the Signing Context handled by the singleton.
     */
    public SigningContext getSigningContext() {
        return signingContext;
    }

    /**
     * Make sure the signingContext is then usable.
     */
    private void correct() {
        // Remove null case
        if (getSigningContext().getCardProfiles() == null) {
            // The server did not find any Card profile. This can happen when:
            // 1. No smartcard is connected to the client.
            // 2. The smartcard detection is not available.
            LOG.info("CardProfiles of Signing Context is null");
            ArrayList<CardProfile> lcp = new ArrayList<CardProfile>();
            getSigningContext().setCardProfiles(lcp);
        }

        if (AppletSigningMethod.installed_cert.equals(SigningMethodHome.getInstance().getSigningMethod())){
            addMSCAPIProfile();
        } else {
            setDefaults(getSigningContext());
            Fingerprint fingerprint = FingerprintHome.getInstance().getFingerprint();       
            if (!fingerprint.isCardDetectionAvailable()) {
                addPkcs11Profile();
            }            
        }
    }

    /**
     * Adds a PKCS11 profile - this is used when there is no card in the card store and card detection is available,
     * or on windows when the user provides a library.
     * @param fingerprint
     */
    public void addPkcs11Profile() {
        // Create a synthetic CardProfile because there is at least one smart card that is not in the
        // card profile repository or card detection is not available and there may be a smart card
        // (applies, at least, to JRE<6)
        LOG.info("At least one smartcard is not recognized.");
        boolean added = false;
        for (CardProfile cp:getSigningContext().getCardProfiles()){
            if (cp.getApi()==null){
                cp.setApi(SignatureTokenType.PKCS11.name());
                cp.setCardDescription(CARD_UNKNOWN_PKCS11);
                added = true;
                break;
            }
        }
        if (!added) {
            CardProfile cp = new CardProfile();
            cp.setApi(SignatureTokenType.PKCS11.name());
            cp.setCardDescription(CARD_UNKNOWN_PKCS11);
            getSigningContext().getCardProfiles().add(cp);
        }
    }
    

    /**
     * Sets the default values on card profiles
     * @param sc the SigningContext
     */
    private void setDefaults(SigningContext sc) {
        for (CardProfile cp:sc.getCardProfiles()){
            if (cp.getApi()==null){
                
                if (sc.getOs().equals(OS.WINDOWS) && Fingerprint.getJreVersion(FingerprintHome.getInstance().getFingerprint()) >= MIN_JRE_FOR_MSCAPI){
                    cp.setApi(getDefaultWindowsApi());
                    cp.setCardDescription(CARD_UNKNOWN_MSCAPI);
                }else {
                    cp.setApi(SignatureTokenType.PKCS11.name());
                    cp.setCardDescription(CARD_UNKNOWN_PKCS11);
                }                                
            }
        }
    }
    /**
     * Get the default API to use on Windows
     * @return the default API
     */
    private String getDefaultWindowsApi() {
        
        try {
            Class.forName(JavaSixClassName.DssMscapiProvider.getClassName());
        } catch (Exception e){
            return SignatureTokenType.MSCAPI.name();
        }
        return SignatureTokenType.NEWMSCAPI.name();
    }

    /**
     * Add a default card profile for the Microsoft Crypto API if user is on windows with Java >= 6 and there
     * are no other card profiles.
     */
    private void addNewMSCAPIProfile() {

        /*
         * Add the profile only if there is no actual MSCAPI profile.
         */
        boolean profileAlreadyExist = false;
        for (CardProfile cp : getSigningContext().getCardProfiles()) {
            if (cp.getApi() == SignatureTokenType.NEWMSCAPI) {
                profileAlreadyExist = true;
            }
        }
        if (!profileAlreadyExist) {

            Fingerprint fingerprint = FingerprintHome.getInstance().getFingerprint();
            if (fingerprint.getOs().toLowerCase().contains("windows")
                    && Fingerprint.getJreVersion(fingerprint) >= MIN_JRE_FOR_MSCAPI) {
                LOG.info("Java >= 6 and Windows, cardProfile for MSCAPI added.");
                final CardProfile mscapiAttempt = new CardProfile();
                mscapiAttempt.setCardDescription(CARD_UNKNOWN_MSCAPI);
                mscapiAttempt.setSynthetic(true);
                mscapiAttempt.setApi(SignatureTokenType.NEWMSCAPI.name());
                mscapiAttempt.setAtr("");
                getSigningContext().getCardProfiles().add(mscapiAttempt);
            }
        }
    }

    /**
     * Add a MSCAPI profile for the windows certificate signing method
     */
    private void addMSCAPIProfile() {
        final CardProfile mscapiAttempt = new CardProfile();
        mscapiAttempt.setCardDescription(CARD_UNKNOWN_MSCAPI);
        mscapiAttempt.setSynthetic(true);
        mscapiAttempt.setApi(SignatureTokenType.MSCAPI.name());
        mscapiAttempt.setAtr("");
        getSigningContext().getCardProfiles().add(mscapiAttempt);

    }

    
    /**
     * Gets the default algorithms
     * 
     * @return a list of DifSigAlgorithm
     * 
     *         If the Strategy is LIST the list contains the algorithms in the order they should be tried. If the
     *         Strategy is RANDOM, the last element in the list is the default algorithm; the other algorithms are in
     *         random order and should be tried in the order of the list.
     */
    public List<DigestAlgorithm> getDefaultAlgorithms() {
        List<DigestAlgorithm> a = new ArrayList<DigestAlgorithm>();
        Defaults d = signingContext.getDefaults();
        if (d != null) {
            switch (d.getStrategy()) {
            case LIST:
                a.addAll(d.getAlgorithms());
                break;
            case RANDOM:
            	a.addAll(d.getAlgorithms());
                Collections.shuffle(a);
            	while (a.size()>d.getMaxTries()){
            		a.remove(0);
            	}
                a.add(d.getDefaultAlgorithm());
                break;
            default:
            }
        }
        return a;
    }
}
