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
 * $HeadURL: https://forge.aris-lux.lan/svn/dgdevco-prospect/trunk/pro-eval/pro-eval-taglibs/src/main/java/eu/europa/ec/devco/proeval/web/tags/RequestUrlRelativeUrlTagStrategy.java $
 * $Revision: 2294 $
 * $Date: 2013-10-16 17:48:50 +0200 (Wed, 16 Oct 2013) $
 * $Author: decouxya $
 * 
 * Application: pro-eval
 * Contractor: ARHS-Developments
 */
package eu.europa.ejusticeportal.dss.demo.web.tags;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;


/**
 * Implementation of an IUrlTagStrategy. The goal is to create completely relative
 * paths so that any link may be displayed relatively tothe given request in request
 * attributes. Links will thus not be broken behind a reverse proxy for example.
 */
public class RequestUrlRelativeUrlTagStrategy implements UrlTagStrategy {
    
    /**
     * Returns the actual URL to be written in the HTML page.
     *
     * @param tag
     *            the URL tag for which the URL is computed
     * @return the actual URL to be written in the HTML page
     * @throws JspException
     *             if no request url is found as a request attribute
     */
    @Override
    public String resolveURL(UrlTag tag) throws JspException {
        final HttpServletRequest req = (HttpServletRequest) tag.getPageContext().getRequest();
        final String reqUrl = getContextRootRelativeRequestURL(req);
        if (reqUrl == null) {
       
            throw new JspException(UrlTag.class.getName() + " can only be used if  RequestFilter "
                + "is defined as servlet filter");
        }
       
        if (!tag.getValue().startsWith("/")) {
            throw new JspException("UrlTag value must be absolute to the context root and start with a /. Was '"
                + tag.getValue() + "'");
        }
        String outputUrl = computeURL(tag);
        outputUrl = addQueryString(outputUrl, tag);
        return outputUrl;
    }

    /**
     * Computes the URL value to return.
     *
     * @param tag
     *            the URL tag for which the URL is computed
     * @return the URL to be written in the page
     */
    protected String computeURL(UrlTag tag) {
        final HttpServletRequest req = (HttpServletRequest) tag.getPageContext().getRequest();
        String tagUrlValue = tag.getEvaluatedValue();
        final String reqUrl = getContextRootRelativeRequestURL(req);
        return computeURL(tagUrlValue, reqUrl);
    }

    /**
     * Builds a full relative URL from the request URL.
     * @param destURL The destination URL
     * @param requestURL The request URL
     * @return A full relative URL from the request URL.
     */
    public static String computeURL(String destURL, String requestURL) {
        int count = getDepthFromRoot(requestURL);
        StringBuffer relative = new StringBuffer(".");
        for (; count > 1; count--) {
            relative.append("/");
            relative.append("..");
        }

        relative.append(destURL);
        String outputUrl = relative.toString();

        return outputUrl;
    }

    /**
     * @param reqUrl
     *            the request URL relative to the context root
     * @return the depth in terms of number of directories from the context root
     */
    protected static int getDepthFromRoot(String reqUrl) {
        return StringUtils.countMatches(reqUrl, "/");
    }

    /**
     * @param url
     *            the computed URL
     * @param tag
     *            the URL tag
     * @return the query string to add to the computed URL
     */
    private String addQueryString(String url, UrlTag tag) {
        return tag.getParamManager().aggregateParams(url);
    }

    /**
     * @param request
     *            the HTTP request
     * @return the request URL relative to the context root
     */
    public static String getContextRootRelativeRequestURL(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
