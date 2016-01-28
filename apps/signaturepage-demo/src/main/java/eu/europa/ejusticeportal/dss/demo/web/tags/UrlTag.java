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
/*
 * $HeadURL: https://forge.aris-lux.lan/svn/dgdevco-prospect/trunk/pro-eval/pro-eval-taglibs/src/main/java/eu/europa/ec/devco/proeval/web/tags/UrlTag.java $
 * $Revision: 2294 $
 * $Date: 2013-10-16 17:48:50 +0200 (Wed, 16 Oct 2013) $
 * $Author: decouxya $
 * 
 * Application: pro-eval
 * Contractor: ARHS-Developments
 */
package eu.europa.ejusticeportal.dss.demo.web.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.taglibs.standard.tag.common.core.ImportSupport;
import org.apache.taglibs.standard.tag.common.core.ParamParent;
import org.apache.taglibs.standard.tag.common.core.ParamSupport;
import org.apache.taglibs.standard.tag.common.core.Util;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * This tag provides a way to include url in a JSP in a way that is JSP-include
 * and proxy tolerant.
 * <p>
 * The <code>value</code> attribute contains the complete path starting from the
 * context root, but this one is replaced by a '/'.
 * <p>
 * Example :
 * <p>
 * 
 * <pre>
 * &lt;etd:url value=&quot;/images/logo.gif&quot;/&gt;
 * </pre>
 * <p>
 * Where :
 * <ul>
 * <li>The first '/' is seen as the context root directory ;</li>
 * <li><code>images</code> is a subfolder of the context root ;</li>
 * </ul>
 * <p>
 * If the request url is <code>/home.do</code>, the URL generated by this tag is
 * <code>./images/logo.gif</code>. If the request url is
 * <code>/sample/path/home.do</code>, the resulting URL is
 * <code>./../../images/logo.gif</code>.
 * <p>
 * As URL are relative, they can go through a proxy that would add a level in
 * the path dot part of the generated URL). Further, URL located in included JSP
 * are still valid as the complete path in the web context is maintained (second
 * part of the URL).
 * 
 */
public class UrlTag extends BodyTagSupport implements ParamParent {
    private static final long serialVersionUID = -1283658242268157190L;
    /**
     * Evaluated expression of <code>value</code>.
     */
    private String evaluatedValue;
    /**
     * <code>value</code> attribute that contains the relative URL.
     */
    private String value;
    /**
     * Contains all URL parameters information given by subtags
     * <code>&lt;c:param></code>.
     */
    private ParamSupport.ParamManager params; // added parameters
    /**
     * Name of the variable, if any, in which the result URL can be stored.
     */
    private String var; // 'var' attribute
    /**
     * Defines the scope of the var, if any, that contains the result URL.
     */
    private int scope; // processed 'scope' attr
    /**
     * If tag contains parameters.
     */
    private boolean hasParameters;
    /**
     * Tag strategy used to modify the URL.
     */
    private static UrlTagStrategy tagStrategy = new RequestUrlRelativeUrlTagStrategy();

    /**
     * Default constructor.
     */
    public UrlTag() {
        super();
        init();
    }

    /**
     * Resets all fields.
     */
    private void init() {
        this.evaluatedValue = null;
        this.value = null;
        this.var = null;
        this.params = null;
        this.hasParameters = false;
        this.scope = PageContext.PAGE_SCOPE;
    }

    /**
     * Registers, the variable name, if any, in which the resulting URL is stored.
     *
     * @param variable The result variable name.
     */
    public void setVar(final String variable) {
        this.var = variable;
    }

    /**
     * Return the page context.
     * @return the page context.
     */
    public PageContext getPageContext() {
        return this.pageContext;
    }

    /**
     * Sets the scope of the result variable.
     * @param varScope The scope of the result variable.
     */
    public void setScope(final String varScope) {
        this.scope = Util.getScope(varScope);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.taglibs.standard.tag.common.core.ParamParent#addParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void addParameter(final String name, final String val) {
        this.params.addParameter(name, val);
        this.hasParameters = true;
    }

    /**
     * Return the param manager.
     * @return the param manager.
     */
    public ParamSupport.ParamManager getParamManager() {
        return this.params;
    }

    /**
     * Returns the value of the <code>value</code> attribute of the tag, as
     * written into the JSP page.
     *
     * @return the value of the <code>value</code> attribute of the tag
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Registers the <code>value</code> attribute.
     *
     * @param url The value attribute.
     */
    public void setValue(final String url) {
        this.value = url;
    }

    /**
     * Returns the value of the <code>value</code> attribute, after being
     * evaluated if it is a JSP expression.
     *
     * @return the evaluated value of the <code>value</code> attribute.
     */
    public String getEvaluatedValue() {
        return this.evaluatedValue;
    }

    @Override
    public int doStartTag() throws JspException {
        if (ImportSupport.isAbsoluteUrl(this.evaluatedValue)) {
            throw new JspException("This tag can only be used with relative URL. Value used : " + this.evaluatedValue);
        }
        this.params = new ParamSupport.ParamManager();
        evaluateExpressions();
        return EVAL_BODY_BUFFERED;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        String result = tagStrategy.resolveURL(this);
        final HttpServletResponse response = ((HttpServletResponse) this.pageContext.getResponse());
        result = response.encodeURL(result);
        // store or print the output
        if (this.var != null) {
            this.pageContext.setAttribute(this.var, result, this.scope);
        } else {
            try {
                this.pageContext.getOut().write(result);
            } catch (IOException e) {
                throw new JspException("URL could not be written", e);
            }
        }
        return EVAL_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    @Override
    public void release() {
        init();
        super.release();
    }

    /**
     * Evaluates the EL-based <code>value</code> attribute of the tag.
     *
     * @throws JspException
     *             if evaluation fails.
     */
    private void evaluateExpressions() throws JspException {
        this.evaluatedValue =
            this.hasParameters ? (String) ExpressionUtil.evalNotNull("url", "value", this.value, String.class, this,
                this.pageContext) : this.value;
    }

    /**
     * @return the tagStrategy
     */
    public static UrlTagStrategy getTagStrategy() {
        return tagStrategy;
    }

    /**
     * @param tagStrategy
     *            the tagStrategy to set
     */
    public static void setTagStrategy(UrlTagStrategy tagStrategy) {
        UrlTag.tagStrategy = tagStrategy;
    }
}
