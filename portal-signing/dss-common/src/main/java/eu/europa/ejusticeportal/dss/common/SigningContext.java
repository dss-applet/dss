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
package eu.europa.ejusticeportal.dss.common;

import java.io.Serializable;
import java.util.List;

/**
 * A signing context is information known about the signing capabilities of the
 * user environment.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 334 $ - $Date: 2012-11-29 11:40:43 +0100 (jeu., 29 nov.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class SigningContext implements Serializable{


	private static final long serialVersionUID = 1L;
	public List<CardProfile> cardProfiles;
	public int detectedCardCount;
	public String os;
	public double jreVersion;
	public String architecture;
	public Defaults defaults;
    
    

    /**
     * @return the card profiles
     */
    public List<CardProfile> getCardProfiles() {
        return cardProfiles;
    }

    /**
     * @param cardProfiles the card profiles to set
     */
    public void setCardProfiles(List<CardProfile> cardProfiles) {
        this.cardProfiles = cardProfiles;
    }

    /**
     * Gets the number of detected cards. Note that this can be less than the number of card profiles
     * because artificial card profiles are added if none detected or on Windows for MSCAPI
     * @return the detectedCardCount
     */
    public int getDetectedCardCount() {
        return detectedCardCount;
    }

    /**
     * Sets the number of detected cards.
     * @param detectedCardCount the detectedCardCount to set
     */
    public void setDetectedCardCount(int detectedCardCount) {
        this.detectedCardCount = detectedCardCount;
    }

    /**
     * @return the os
     */
    public OS getOs() {
        return os == null? OS.UNSUPPORTED: OS.valueOf(os);
    }

    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return the jreVersion
     */
    public double getJreVersion() {
        return jreVersion;
    }

    /**
     * @param jreVersion the jreVersion to set
     */
    public void setJreVersion(double jreVersion) {
        this.jreVersion = jreVersion;
    }

    /**
     * @return the architecture
     */
    public Arch getArchitecture() {
        return architecture == null? Arch.UNKNOWN:Arch.valueOf(architecture);
    }

    /**
     * @param architecture the architecture to set
     */
    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    /**
     * @return the defaults
     */
    public Defaults getDefaults() {
        return defaults;
    }

    /**
     * @param defaults the defaults to set
     */
    public void setDefaults(Defaults defaults) {
        this.defaults = defaults;
    }

    @Override
    public String toString() {
        return "SigningContext [cardProfiles=" + cardProfiles + ", detectedCardCount=" + detectedCardCount + ", os="
                + os + ", jreVersion=" + jreVersion + ", architecture=" + architecture + ", defaults=" + defaults + "]";
    }
}
