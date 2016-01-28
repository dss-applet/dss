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
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Algorithms and strategy to apply when {@link CardProfile} does not have information
 * about the specific smart card.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class Defaults implements Serializable{
    
	private static final long serialVersionUID = 1L;
	/**
     * Type of strategy to use when applying defaults
     */
    public enum Strategy{
        /**
         * Use each algorithm pair in the list until one works or all have been tried. 
         */
        LIST,
        /**
         * Use random algorithms from the list until one works, all have been tried, or maximum tries reached.
         */
        RANDOM
    }
    

    public String strategy;
    public List<String> algorithms;
    public String defaultAlgorithm;
    public int maxTries;
    private transient List<DigestAlgorithm> digestAlgorithms;
    /**
     * @return the strategy
     */
    public Strategy getStrategy() {
        return Strategy.valueOf(strategy);
    }
    /**
     * @param strategy the strategy to set
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
    /**
     * @return the algorithms
     */
    public List<DigestAlgorithm> getAlgorithms() {
        if (digestAlgorithms == null ) {
            digestAlgorithms = new ArrayList<DigestAlgorithm>();            
        }
        if (digestAlgorithms.isEmpty() && algorithms!=null) {
            for (String algo:algorithms) {
                digestAlgorithms.add(DigestAlgorithm.forName(algo));
            }
        }
        return digestAlgorithms;
    }
    /**
     * @param algorithms the algorithms to set
     */
    public void setAlgorithms(List<String> algorithms) {
        this.algorithms = algorithms;
    }
    /**
     * Get the default algorithm to use after trying for max tries
     * @return the defaultAlgorithm
     */
    public DigestAlgorithm getDefaultAlgorithm() {        
        return defaultAlgorithm == null? null: DigestAlgorithm.valueOf(defaultAlgorithm);
    }
    /**
     * Set the default algorithm to use after trying for max tries
     * @param defaultAlgorithm the defaultAlgorithm to set
     */
    public void setDefaultAlgorithm(String defaultAlgorithm) {
        this.defaultAlgorithm = defaultAlgorithm;
    }
    /**
     * Get the maximum number of signature attempts before switching to the default algorithm
     * @return the maxTries
     */
    public int getMaxTries() {
        return maxTries;
    }
    /**
     * Set the maximum number of signature attempts before switching to the default algorithm
     * @param maxTries the maxTries to set
     */
    public void setMaxTries(int maxTries) {
        this.maxTries = maxTries;
    }
}
