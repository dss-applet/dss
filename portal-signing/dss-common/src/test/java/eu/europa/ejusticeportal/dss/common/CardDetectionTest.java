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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * Test CardDetection
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class CardDetectionTest {

    private byte[] byteAtrLuxTrust;
    private static final String strAtrLuxTrust = "3B 7D 94 00 00 80 31 80 65 B0 83 02 04 7E 83 00 90 00";

    @Before
    public void setUp() throws DecoderException {
        byteAtrLuxTrust = Hex.decodeHex("3b7d94000080318065b08302047e83009000".toCharArray());
    }

    /**
     * Test getting the ATR as string
     */
    @Test
    public void testATRtoString() {
        CardProfile cd = new CardProfile();
        cd.setAtr(CardProfile.atrToString(byteAtrLuxTrust));
        assertEquals(cd.getAtr(), strAtrLuxTrust);
    }
}
