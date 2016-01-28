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
 * $HeadURL: https://forge.aris-lux.lan/svn/dgdevco-prospect/trunk/pro-eval/pro-eval-taglibs/src/main/java/eu/europa/ec/devco/proeval/web/tags/UrlTagStrategy.java $
 * $Revision: 2294 $
 * $Date: 2013-10-16 17:48:50 +0200 (Wed, 16 Oct 2013) $
 * $Author: decouxya $
 * 
 * Application: pro-eval
 * Contractor: ARHS-Developments
 */
package eu.europa.ejusticeportal.dss.demo.web.tags;

import javax.servlet.jsp.JspException;

/**
 * Interface for URL resolver used into tags.
 */
public interface UrlTagStrategy {
    /**
     * Returns the actual URL to be written in the HTML page.
     * 
     * @param tag
     *            The tag instance.
     * @return the actual URL to be written in the HTML page
     * @throws JspException
     *             if no request url is found as a request attribute
     */
    String resolveURL(UrlTag tag) throws JspException;
}
