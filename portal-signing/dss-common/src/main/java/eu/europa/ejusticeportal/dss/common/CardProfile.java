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

import eu.europa.ec.markt.dss.DigestAlgorithm;

import java.io.Serializable;
import java.util.List;

/**
 *
 * This class describes the profile for a specified Smart Card.
 * The profile has information we obtain by probing the smart card, enhanced by information
 * provided by the {@linkCardProfileRepository}
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 262 $ - $Date: 2012-10-23 13:30:50 +0200 (mar., 23 oct.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class CardProfile implements Serializable{

	private static final long serialVersionUID = 1L;

	private String api;

    public String digestAlgo;
    public List<String> libraryPath;
    public String cardDescription;
    public String url;
    public String atr;
    public List<String> eventFilters;
    public int terminalIndex;
    public boolean synthetic;

    /**
     * @return the api
     */
    public SignatureTokenType getApi() {
        return api==null?null:SignatureTokenType.valueOf(api);
    }

    /**
     * @param api the api to set
     */
    public void setApi(String api) {
        this.api = api;
    }

    /**
     * @return the libraryPath
     */
    public List<String> getLibraryPath() {
        return libraryPath;
    }

    /**
     * @param libraryPath the libraryPath to set
     */
    public void setLibraryPath(List<String> libraryPath) {
        this.libraryPath = libraryPath;
    }

    /**
     * @return the cardDescription
     */
    public String getCardDescription() {
        return cardDescription;
    }

    /**
     * @param cardDescription the cardDescription to set
     */
    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the Digest Algorithm
     */
    public DigestAlgorithm getDigestAlgo() {
        return digestAlgo==null?null:DigestAlgorithm.valueOf(digestAlgo);
    }

    /**
     * @param digestAlgo the Digest Algorithm to set
     */
    public void setDigestAlgo(String digestAlgo) {
        this.digestAlgo = digestAlgo;
    }

    /**
     * @return the ATR
     */
    public String getAtr() {
        return atr;
    }

    /**
     * @param atr the ATR to set
     */
    public void setAtr(String atr) {
        this.atr = atr;
    }

    /**
     * Check if the card profile was created without a card i.e. for 
     * listing MSCAPI certificates or where card was unknown.
     * @return true if the profile does not correspond to a real card
     */
    public boolean isSynthetic() {
        return synthetic;
    }

    /**
     * Set if the card profile was created without a card i.e. for 
     * listing MSCAPI certificates or where card was unknown.
     * @param synthetic true if so created
     */
    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }

    /**
     * @return the eventFilters
     */
    public List<String> getEventFilters() {
        return eventFilters;
    }

    /**
     * @param eventFilters the eventFilters to set
     */
    public void setEventFilters(List<String> eventFilters) {
        this.eventFilters = eventFilters;
    }
    
   
    /**
     * Transform an ATR byte array into a string
     *
     * @param b the ATR byte array 
     * @return the string (empty if the ATR byte array is empty or null)
     */
    public static String atrToString(byte[] b) {
        final StringBuilder sb = new StringBuilder();
        if (b != null && b.length > 0) {
            sb.append(Integer.toHexString((b[0] & 240) >> 4));
            sb.append(Integer.toHexString(b[0] & 15));

            for (int i = 1; i < b.length; i++) {
                sb.append(' ');
                sb.append(Integer.toHexString((b[i] & 240) >> 4));
                sb.append(Integer.toHexString(b[i] & 15));
            }
        }
        return sb.toString().toUpperCase();
    }



    /**
     * Get the index of the terminal from which the card info was read
     * @return the terminalIndex
     */
    public int getTerminalIndex() {
        return terminalIndex;
    }

    /**
     * Set the index of the terminal from which the card info was read
     * @param terminalIndex the terminalIndex to set
     */
    public void setTerminalIndex(int terminalIndex) {
        this.terminalIndex = terminalIndex;
    }

    

	@Override
	public String toString() {
		return "CardProfile [api=" + api + ", digestAlgo=" + digestAlgo
				+ ", libraryPath=" + libraryPath + ", cardDescription="
				+ cardDescription + ", url=" + url + ", atr=" + atr
				+ ", eventFilters=" + eventFilters + ", terminalIndex="
				+ terminalIndex + ", synthetic="
				+ synthetic + "]";
	}

    
}
