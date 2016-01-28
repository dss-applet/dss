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
package eu.europa.ejusticeportal.dss.controller.profile;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.OS;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.SigningContextEvent;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.config.Config;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.APIContextType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.CardProfileType;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;


/**
 * Implementation of a CardProfileRepository & MSPolicyRepository
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class CardProfileRepositoryImpl implements CardProfileRepository {


    /**
     * 
     * The constructor for SigningContextRepositoryXmlImpl.
     * 
     */
    public CardProfileRepositoryImpl() {
    }


    /**
     * Find all of the CardProfile corresponding to the given criteria
     * 
     * @param request the HttpServletRequest
     * @param portal the PortalFacade
     * @param fp the fingerprint describing the user environment and cards
     * @param sc the SigningContext, collecting the card profiles
     */
    @Override
    public void findCardProfiles(final HttpServletRequest request, final PortalFacade portal, final Fingerprint fp,
            final SigningContext sc) throws NoSuchAlgorithmException {
        sc.setCardProfiles(fp.getCardProfiles());
        sc.setDefaults(Config.getInstance().getDefaults(portal));
        if ((fp.getCardProfiles() == null) || (fp.getCardProfiles().isEmpty())) {
            portal.log(request, new SigningContextEvent(sc.getOs().getName(), sc.getArchitecture()
                    .getName(), sc.getJreVersion(), fp));
            return;
        }

        for (CardProfile cp : sc.getCardProfiles()) {

            final String atr = cp.getAtr();
            if (atr == null || atr.isEmpty()) {
                break;
            }
            CardProfileType cpt = Config.getInstance().getProfile(portal, atr);
            if (cpt == null) {
                portal.log(request, new SigningContextEvent(sc.getOs().getName(), sc.getArchitecture()
                        .getName(), sc.getJreVersion(), atr,fp, false));
                return;
            } else {
                portal.log(request, new SigningContextEvent(sc.getOs().getName(), sc.getArchitecture()
                        .getName(), sc.getJreVersion(), atr,fp, true));
            }
            final OS os = sc.getOs();
            final Arch arch = sc.getArchitecture();
            final Double jreVersion = sc.getJreVersion();
            initCardProfile(cp, cpt);
            
            for (APIContextType apc : cpt.getAPIContext()) {
                if (matches(os, arch, jreVersion, apc)) {
                    cp.setApi(apc.getAPI().value());
                    cp.setLibraryPath(apc.getLibraryPath());
                    cp.setEventFilters(apc.getEventFilter());
                    // algorithms at API level may override algorithms at card profile level
                    if (apc.getDigestAlgorithm() != null) {
                        cp.setDigestAlgo(apc.getDigestAlgorithm().value());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Initialises a CardProfile with values from the repository
     * 
     * @param atr the ATR
     * @param cpt the CardProfileType from the repository
     * @return the CardProfile
     */
    private void initCardProfile(CardProfile cp, CardProfileType cpt) {
        
        cp.setCardDescription(cpt.getDescription());
        cp.setDigestAlgo(cpt.getDigestAlgorithm().value());
        cp.setUrl(cpt.getURL());

        
    }

    /**
     * Test if the APIContextType is suitable for the environment
     * 
     * @param os the OS
     * @param arch he bitness
     * @param jreVersion the JRE version
     * @param apc the APIContextType to test
     * @return true if APIContextType is suitable
     */
    private boolean matches(final OS os, final Arch arch, final Double jreVersion, APIContextType apc) {
        boolean osOK = apc.getOS().value().equals(os.getName());
        boolean archOK = apc.getArch().equals(arch.getName()) || apc.getArch().equals(Arch.EITHER.getName());
        boolean jreOK = apc.getMinJreVersion() == null
                || Utils.parseJreVersion(apc.getMinJreVersion()) <= jreVersion;
        return osOK && archOK && jreOK;
    }

}
