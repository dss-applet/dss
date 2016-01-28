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

import java.applet.Applet;

import netscape.javascript.JSObject;

/**
 * Evaluate JavaScript expressions, relying on netscape.javascript.JSObject.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class JavaScriptExpressionEvaluator {

    /**
     * JSObject to which JavaScript evaluation is delegated to.
     */
    private JSObject jsObject;

    /**
     * Initialize the JavaScriptExpressionEvaluator for the provided Applet.
     * This method is intended to be invoked at
     * the very beginning of the Applet.init() method.
     *
     * @param applet
     */
    public void init(final Applet applet) {
        jsObject = JSObject.getWindow(applet);
    }

    /**
     * Singleton.
     */
    private static JavaScriptExpressionEvaluator instance;

    private JavaScriptExpressionEvaluator() {
    }

    /**
     * Since this component is intended to be initialized at beginning of the of
     * the Applet.init() method, by the same
     * thread , the creation of the instance is not synchronized.
     *
     * @return the instance of the JavaScriptExpressionEvaluator.
     */
    public static JavaScriptExpressionEvaluator getInstance() {
        if (instance == null) {
            instance = new JavaScriptExpressionEvaluator();
        }
        return instance;
    }

    /**
     * Evaluate a JavaScript function, given some parameter.
     *
     * Note : this method has been hidden to force everybody to put function
     * names in enum's ;-).
     *
     * PRE: init(Applet) has been previously invoked.
     *
     * @param function the name function to be evaluated
     * @param params   string parameters (can be null or empty).
     * @return an Object, resulting from the evaluation of function(params)
     */
    private Object eval(String function, String... params) {
        return getJsObject().eval(buildJavaScript(function, params));
    }

    /**
     * Same as eval(String function, String... params), using an enum to store
     * the function names.
     *
     * @param e
     * @param params
     * @return
     */
    public Object eval(Enum<?> e, String... params) {
        return eval(e.name(), params);
    }

    /**
     * Build JavaScript expression to be evaluated.
     *
     * @param function
     * @param params
     * @return
     */
    protected String buildJavaScript(String function, String... params) {
        StringBuilder js = new StringBuilder(function).append('(');

        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    js.append(',');
                }
                js.append('\'').append(params[i]).append('\'');
            }
        }
        return js.append(')').toString();
    }

    /**
     * @return the JSobject that evaluates Javascript.
     */
    public JSObject getJsObject() {
        return jsObject;
    }
}
