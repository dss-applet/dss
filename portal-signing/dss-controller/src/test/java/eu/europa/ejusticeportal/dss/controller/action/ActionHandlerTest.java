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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import eu.europa.ejusticeportal.dss.common.SignedForm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * 
 * Tests the ActionHandler
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1747 $ - $Date: 2014-10-06 10:39:58 +0200 (Mon, 06 Oct 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(Parameterized.class)
public class ActionHandlerTest {

    private ActionId actionId;

    public ActionHandlerTest(ActionId actionId) {
        this.actionId = actionId;
        System.setProperty(SealedPDFService.PASSWORD_FILE_PROPERTY, "classpath:server.pwd");
        System.setProperty(SealedPDFService.CERTIFICATE_FILE_PROPERTY, "classpath:server.p12");
    }

    
    /**
     * Get the parameters for the test case
     * @return
     */
    @Parameters
    public static List<Object[]> getParameters() {
        ActionId[] ids = ActionId.values();
        Object o[][] = new Object[ids.length][1];
        int i = 0;
        for (ActionId id : ids) {
            o[i][0] = id;
            i++;
        }

        return Arrays.asList(o);
    }

    /**
     * Test handling actions
     * @throws IOException 
     */
    @Test
    public void testHandleAction() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse(os);
        boolean handled;
        if (actionId.name().startsWith("AJAX_")){
            handled = ActionHandler.handleAjaxRequest(actionId, new PortalFacadeTestImpl(), request, response);
        } else {
            handled = ActionHandler.handleRequest(actionId, new PortalFacadeTestImpl(), request, response);
        }
        switch (actionId) {
        case AJAX_GET_SEALED_PDF:
        case AJAX_GET_SIGNATURE_CONTEXT:
        case AJAX_SIGN_DOC:
        case DOWNLOAD_PDF:
        case DOWNLOAD_PDF_NO_JAVA:
        case AJAX_GET_LOCALISED_MESSAGE:
        case AJAX_LOG_STATISTIC:        
        case AJAX_LOG_APPLET_EVENTS:
        case DOWNLOAD_SIGNED_PDF:
            assertTrue(actionId.name()+"was not handled",handled);
            break;
        case UPLOAD_PDF:
        case FORWARD_NOJAVA:        	
        case UPLOAD_PDF_NO_JAVA:        
        case EXPIRED_SESSION_AJAX:
        case RELOAD_APPLET:
            assertFalse(handled);
            break;
        case NEXT:
            PdfTempStore.storeSignedForm(request, new SignedForm());
            assertTrue(ActionHandler.handleNext(new PortalFacadeTestImpl(), request, response));
            break;
        case OTHER://handled by the portal
        case EXPIRED_SESSION://handled outside of the Action Handler
        case AJAX_REQUEST_ID://not really an action ID - it distinguishes the ajax data
        case STAY_NOJAVA://not really an action ID - a flag to retain on no java pages.
        case SELECT_SIGN_METHOD:
        case TEST_PAGE:
            break;
        default:
            fail(actionId.name() + " is not handled.");
        }
    }

    private HttpServletRequest getRequest() {

        return new MockHttpServletRequest();
    }

    private HttpServletResponse getResponse(final OutputStream os) throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getOutputStream()).thenReturn(new ServletOutputStreamImpl(os));
        return response;
    }
}
