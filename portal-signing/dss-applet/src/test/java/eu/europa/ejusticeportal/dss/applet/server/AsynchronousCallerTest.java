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
package eu.europa.ejusticeportal.dss.applet.server;

import static org.junit.Assert.assertTrue;

import eu.europa.ejusticeportal.dss.applet.common.JavaScriptExpressionEvaluator;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * Check interaction with mocked JavaScriptEvaluator
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JavaScriptExpressionEvaluator.class)
@PowerMockIgnore( { "org.xml.*", "javax.xml.*" })
public class AsynchronousCallerTest {

    private class FakeServerCall extends ServerCall {

        /**
         *
         * The constructor for FakeServerCall.
         *
         * @param asynchronousServerCallName name
         * @param params                     parameters
         */
        public FakeServerCall(Enum<?> asynchronousServerCallName,Enum<?>serverCallId, Object... params) {
            super(asynchronousServerCallName, serverCallId,params);
        }
    }

    private enum FakeCallEnum {
        fake
    }

    /**
     * Tests the call execution
     */
    @Test
    public void testFire() {
        PowerMockito.mockStatic(JavaScriptExpressionEvaluator.class);
        JavaScriptExpressionEvaluator jsee = Mockito.mock(JavaScriptExpressionEvaluator.class);
        Mockito.when(JavaScriptExpressionEvaluator.getInstance()).thenReturn(jsee);
        AsynchronousCallerHome.getInstance().getCaller().fire(new FakeServerCall(AsynchronousServerCall.callServer,ServerCallId.getSealedPdf));
        Mockito.verify(jsee).eval(AsynchronousServerCall.callServer,new String []{ServerCallId.getSealedPdf.name(),Utils.escape(Utils.toString(new Object()))});
        assertTrue(new FakeServerCall(AsynchronousServerCall.callServer,ServerCallId.getSealedPdf).toString().length()!=0);
    }
}
