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
package eu.europa.ejusticeportal.dss.applet.model.token;

import eu.europa.ec.markt.dss.DigestAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a ranking list of the digest algorithm, from the strongest to the weakest.
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class DigestAlgorithmRanking {

    /**
     * Singleton reference of DigestAlgorithmRanking.
     */
    private static DigestAlgorithmRanking instance;

    /**
     * Constructs singleton instance of DigestAlgorithmRanking.
     */
    private DigestAlgorithmRanking() {
        rankingDigestAlgorithm.add(DigestAlgorithm.SHA512);
        rankingDigestAlgorithm.add(DigestAlgorithm.SHA384);
        rankingDigestAlgorithm.add(DigestAlgorithm.SHA256);
        rankingDigestAlgorithm.add(DigestAlgorithm.SHA1);
        rankingDigestAlgorithm.add(DigestAlgorithm.MD5);
        rankingDigestAlgorithm.add(DigestAlgorithm.MD2);
    }

    /**
     * Provides reference to singleton getClass() of DigestAlgorithmRanking.
     *
     * @return Singleton instance of DigestAlgorithmRanking.
     */
    public static DigestAlgorithmRanking getInstance() {
        if (instance == null) {
            instance = new DigestAlgorithmRanking();
        }
        return instance;
    }

    private List<DigestAlgorithm> rankingDigestAlgorithm = new ArrayList<DigestAlgorithm>();

    public List<DigestAlgorithm> getRankingDigestAlgorithm() {
        return rankingDigestAlgorithm;
    }
}
