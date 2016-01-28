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

import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the MessageBundleHome
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class MessageBundleHomeTest {

    MessageBundle mb;

    /**
     * Set up the MessageBundle
     */
    @Before
    public void setUp() {
        mb = new MessageBundle();
        Map<String, String> m = new HashMap<String, String>();
        for (MessagesEnum e : MessagesEnum.values()) {
            m.put(e.name(), e.name());
        }
        mb.setMessages(m);
        MessageBundleHome.getInstance().init(mb);
    }

    /**
     * Test getting the MessageBundle
     */
    @Test
    public void testGetMessageBundle() {
        MessageBundle result = MessageBundleHome.getInstance().getMessageBundle();
        assertNotNull(result);
        assertEquals(result.getMessages().size(), MessagesEnum.values().length);
    }

    /**
     * Test getting the message
     */
    @Test
    public void testGetMessage() {
        for (MessagesEnum e : MessagesEnum.values()) {
            assertNotNull(MessageBundleHome.getInstance().getMessage(e.name()));
            assertEquals(MessageBundleHome.getInstance().getMessage(e.name()), e.name());
        }
    }
}
