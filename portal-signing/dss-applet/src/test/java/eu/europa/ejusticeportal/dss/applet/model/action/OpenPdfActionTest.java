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
package eu.europa.ejusticeportal.dss.applet.model.action;

import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.event_api.EventListener;
import eu.europa.ejusticeportal.dss.applet.model.service.MessageBundleHome;
import eu.europa.ejusticeportal.dss.common.MessageBundle;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test of OpenPdfAction class
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class OpenPdfActionTest {

    private EventListener mockListener;
    private OpenPdfAction instance;

    /**
     * Set up object for unit test
     *
     */
    @Before
    public void setUp() {
        instance = new OpenPdfAction("data".getBytes()) {
            @Override
            protected void openURL(String url) {
                assertTrue(url != null);
            }
        };
        mockListener = Mockito.mock(EventListener.class);
        Event.getInstance().registerListener(mockListener);
        MessageBundle bundle = new MessageBundle();
        Map<String, String> messages = new HashMap<String, String>();
        for (MessagesEnum m : MessagesEnum.values()) {
            messages.put(m.name(), m.name());
        }
        bundle.setMessages(messages);
        MessageBundleHome.getInstance().init(bundle);
    }

    /**
     * Test of doExec method, of class OpenPdfAction.
     */
    @Test
    public void testDoExec() {
        instance.doExec();
        Mockito.verify(mockListener, Mockito.atLeastOnce()).process((StatusRefreshed) Mockito.any());
    }
}
