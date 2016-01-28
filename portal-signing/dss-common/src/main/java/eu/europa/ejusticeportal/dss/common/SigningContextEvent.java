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


/**
 * 
 * Information collected in the event of a request by the applet for the SigningContext.
 * The information is supposed to be useful for statistical purposes, to see which kind
 * of user environments use the applet.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SigningContextEvent extends DssEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	public String atr;
	public boolean atrFound;
	public Fingerprint fingerprint;

    /**
     * 
     * The constructor for CardProfileRepositoryLogEntry.
     * @param os the OS
     * @param arch the Architecture (32/64)
     * @param jreVersion the JRE version
     * @param atr the ATR
     * @param fp the Fingerprint
     * @param found true if there was a corresponding entry in the repository
     */
    public SigningContextEvent(String os, String arch, double jreVersion, String atr,Fingerprint fp, boolean found){
        this(os,arch,jreVersion,fp);
        setAtr(atr);
        setAtrFound(found);
    }
    /**
     * 
     * The constructor for CardProfileRepositoryLogEntry.
     * @param os the OS
     * @param arch the architecture (32/64)
     * @param jreVersion the JRE version
     * @param fp the Fingerprint
     */
    public SigningContextEvent(String os, String arch, double jreVersion, Fingerprint fp) {
        super(DssEventType.SC.name(), os, arch, jreVersion);
        fingerprint =  fp;
    }
    
    /**
     * 
     * The default constructor for CardProfileRepositoryLogEntry.
     */
    public SigningContextEvent(){
        super(DssEventType.SC.name());
    }
    /**
     * @return the ATR. This is null if there were no cards detected
     */
    public final String getAtr() {
        return atr;
    }

    /**
     * @param atr the ATR .
     */
    public final void setAtr(String atr) {
        this.atr = atr;
    }
    /**
     * @return true if a profile for the ATR was found in the repository
     */
    public final boolean isAtrFound() {
        return atrFound;
    }
    /**
     * @param atrFound true if a profile for the ATR was found in the repository
     */
    public final void setAtrFound(boolean atrFound) {
        this.atrFound = atrFound;
    }
    /**
     * @return the fingerprint
     */
    public Fingerprint getFingerprint() {
        return fingerprint;
    }
    /**
     * @param fingerprint the fingerprint to set
     */
    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }    
}
