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
package eu.europa.ejusticeportal.dss.applet.common;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * Test the JavaScriptExpressionEvaluator
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@RunWith(value = Parameterized.class)
public class JavaScriptExpressionEvaluatorTest {

    private String method, expected;
    private String[] params;

    /**
     *
     * The constructor for JavaScriptExpressionEvaluatorTest.
     * @param method the method name
     * @param params the method parameters
     * @param expected the expected javascript
     */
    public JavaScriptExpressionEvaluatorTest(String method, String[] params, String expected) {
        this.method = method;
        this.params = params;
        this.expected = expected;
    }

    /**
     * Set up parameters for the test
     * @return the parameters
     */
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { "test", null, "test()" }, { "test", new String[] {}, "test()" },
                { "test", new String[] { "1" }, "test('1')" }, { "test", new String[] { "1", "2" }, "test('1','2')" } };
        return Arrays.asList(data);
    }

    JavaScriptExpressionEvaluator jse = JavaScriptExpressionEvaluator.getInstance();

    /**
     * Test the JavaScriptExpressionEvaluator
     */
    @Test
    public void testBuildJavaScript() {
        Assert.assertEquals(expected, jse.buildJavaScript(method, params));
    }
}
