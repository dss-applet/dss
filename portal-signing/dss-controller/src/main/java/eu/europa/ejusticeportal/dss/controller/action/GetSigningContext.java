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
package eu.europa.ejusticeportal.dss.controller.action;

import eu.europa.ejusticeportal.dss.common.Arch;
import eu.europa.ejusticeportal.dss.common.Fingerprint;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.Utils;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;
import eu.europa.ejusticeportal.dss.controller.profile.CardProfileRepositoryImpl;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Gets the signing context.
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
public class GetSigningContext extends SigningAction {

    private CardProfileRepositoryImpl signingContextRepository;

    /**version assumed when not found in the system property*/
    private double defaultJreVersion = 8d;

    /**
     *
     * The constructor for GetSigningContext.
     */
    public GetSigningContext() {
    }

    @Override
    protected Object getResponseObject(final PortalFacade portal, final HttpServletRequest request, final Object o) {

        final Fingerprint fp = (Fingerprint) o;
        final SigningContext sc = new SigningContext();
        sc.setDetectedCardCount(fp.getCardProfiles().size());
        sc.setArchitecture(getArch(fp).name());
        sc.setJreVersion(getJreVersion(fp));
        sc.setOs(Utils.getOs(fp).name());
        final CardProfileRepositoryImpl scr = getSigningContextRepository(portal);
        try {
            scr.findCardProfiles(request,portal,fp, sc);
        } catch (NoSuchAlgorithmException e) {
            throw new SigningException(e);
        }              
        return sc;
    }

    CardProfileRepositoryImpl getSigningContextRepository(final PortalFacade portal) {
        if (signingContextRepository == null){
            signingContextRepository = new CardProfileRepositoryImpl();
        }
        return signingContextRepository ;
    }
    
    /**
     * Get the Java Runtime Environment version
     * @param fp the Fingerprint
     * @return the version
     */
    private Double getJreVersion(Fingerprint fp) {
        if (fp.getJreVersion() == null) {
            return defaultJreVersion;
        }
        final String version = fp.getJreVersion().replaceAll("_", ".");
        return Utils.parseJreVersion(version);
    }



    /**
     * Gets the architecture (64 or 32 bit)
     *
     * @param fp the Fingerprint
     * @return the architecture
     */
    Arch getArch(Fingerprint fp) {
        if (fp.getOsVersion()!=null && fp.getOsVersion().contains("64")){
            return Arch.B64;
        }
        if (fp.getNavPlatform().indexOf("32") != -1 ||fp.getNavPlatform().indexOf("i686")!=-1) {
            return Arch.B32;
        }
        if (fp.getNavPlatform().indexOf("64") != -1) {
            return Arch.B64;
        }
        if (fp.getArch().indexOf("32") != -1)
        {
            return Arch.B32;
        }
        if (fp.getArch().indexOf("64") !=-1){
            return Arch.B64;
        }
        return Arch.UNKNOWN;
    }

}
