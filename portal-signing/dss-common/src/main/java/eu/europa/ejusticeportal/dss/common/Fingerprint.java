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
import java.util.ArrayList;
import java.util.List;

/**
 * A fingerprint represents a set of information about the user environment, and contains
 * a {@link List} of {@link CardProfile} for detected smart cards.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 339 $ - $Date: 2012-10-23 13:30:50 +0200 (mar., 23 oct.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class Fingerprint implements Serializable{


	private static final long serialVersionUID = 1L;
	public String userAgent;
	public String navPlatform;
	public String jreVendor;
	public String jreVersion;
	public String osVersion;
	public String arch;
	public String os;
	public boolean cardDetectionAvailable;
    
	public List<CardProfile> cardProfiles = new ArrayList<CardProfile>();


    /**
     * @return the jreVendor
     */
    public String getJreVendor() {
        return jreVendor;
    }

    /**
     * @param jreVendor the jreVendor to set
     */
    public void setJreVendor(String jreVendor) {
        if (jreVendor != null) {
            this.jreVendor = jreVendor.toLowerCase();
        }
        this.jreVendor = jreVendor;
    }

    /**
     * @return the jreVersion
     */
    public String getJreVersion() {
        return jreVersion;
    }

    /**
     * @param jreVersion the jreVersion to set
     */
    public void setJreVersion(String jreVersion) {
        if (jreVersion == null) {
            this.jreVersion = null;
        } else {
            this.jreVersion = jreVersion.toLowerCase();
        }
    }

    /**
     * @return the osVersion
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @param osVersion the osVersion to set
     */
    public void setOsVersion(String osVersion) {
        if (osVersion == null) {
            this.osVersion = null;
        } else {
            this.osVersion = osVersion.toLowerCase();
        }

    }

    /**
     * @return the arch
     */
    public String getArch() {
        return arch;
    }

    /**
     * @param arch the arch to set
     */
    public void setArch(String arch) {
        if (arch == null) {
            this.arch = null;
        } else {
            this.arch = arch.toLowerCase();
        }

    }

    /**
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        if (os == null) {
            this.os = null;
        } else {
            this.os = os.toLowerCase();
        }
    }



    /**
     * @return the userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent the userAgent to set
     */
    public void setUserAgent(String userAgent) {
        if (userAgent == null) {
            this.userAgent = null;
        } else {
            this.userAgent = userAgent.toLowerCase();
        }

    }

    /**
     * @return the navPlatform
     */
    public String getNavPlatform() {
        return navPlatform;
    }

    /**
     * @param navPlatform the navPlatform to set
     */
    public void setNavPlatform(String navPlatform) {
        if (navPlatform == null) {
            this.navPlatform = null;
        } else {
            this.navPlatform = navPlatform.toLowerCase();
        }

    }
    /**
     * Get the list of CardProfile
     * @return the list
     */
    public List<CardProfile> getCardProfiles() {
        return cardProfiles;
    }

    /**
     * Set the list of CardProfile
     * @param cardProfiles
     */
    public void setCardProfiles(List<CardProfile> cardProfiles) {
        this.cardProfiles = cardProfiles;
    }



    /**
     * Get the double representation of the JreVersion from the given
     * fingerprint.
     *
     * @param fp the fingerprint
     * @return the double value of the JreVersion
     */
    public static Double getJreVersion(Fingerprint fp) {
        return Utils.parseJreVersion(fp.getJreVersion());
        
    }    

    /**
     * Sets if card detection mechanism  (smartcardio) is available
     * @param available true if card detection is available
     */
    public void setCardDetectionAvailable(boolean available) {
        cardDetectionAvailable = available;        
    }

    /**
     * @return true if smart card detection is available.
     */
    public boolean isCardDetectionAvailable() {
        return cardDetectionAvailable;
    }
    
    @Override
    public String toString() {
        return "Fingerprint [userAgent=" + userAgent + ", navPlatform=" + navPlatform + ", jreVendor=" + jreVendor
                + ", jreVersion=" + jreVersion + ", osVersion=" + osVersion + ", arch=" + arch + ", os=" + os
                + ", cardDetectionAvailable=" + cardDetectionAvailable + ", cardProfiles=" + cardProfiles + "]";
    }

}
