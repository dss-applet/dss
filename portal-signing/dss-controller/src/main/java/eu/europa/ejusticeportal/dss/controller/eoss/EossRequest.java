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
 * Project: DG Justice - DSS
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/isammp/isamm-pd/trunk/app/buildtools/src/main/resources/eclipse/isamm-pd-java-code-template.xml $
 * $Revision: 6522 $
 * $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * $Author: naramsda $
 */
package eu.europa.ejusticeportal.dss.controller.eoss;

import java.io.Serializable;
import java.util.Map;

/**
 * Request to the external online signature service server
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class EossRequest implements Serializable {

    /**
	 * Default version ID 
	 */
	private static final long serialVersionUID = 1L;
	
	private String data;
    private Map<String, String> parameterMap;
    private String dataUrl;
    private String redirectUrl;
    private String redirectUrlNoJs;
    private String redirectUrlTestPage;
    private String redirectUrlNoJsTestPage;    
    private String methodCode;
    private String eossToken;

    private String dataUrlTestPage;

    /**
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @param parameterMap
     */
    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    /**
     * @param replace
     */
    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    /**
     * @param redirectUrl
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;        
    }

    /**
     * @param redirectUrlNoJs
     */
    public void setRedirectUrlNoJs(String redirectUrlNoJs) {
        this.redirectUrlNoJs = redirectUrlNoJs;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @return the parameterMap
     */
    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    /**
     * @return the dataUrl
     */
    public String getDataUrl() {
        return dataUrl;
    }

    /**
     * @return the redirectUrl
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * @return the redirectUrlNoJs
     */
    public String getRedirectUrlNoJs() {
        return redirectUrlNoJs;
    }

    /**
     * @return the methodCode
     */
    public String getMethodCode() {
        return methodCode;
    }

    /**
     * @param methodCode the methodCode to set
     */
    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    /**
     * @return the eossToken
     */
    public String getEossToken() {
        return eossToken;
    }

    /**
     * @param eossToken the eossToken to set
     */
    public void setEossToken(String eossToken) {
        this.eossToken = eossToken;
    }

    /**
     * @return the redirectUrlTestPage
     */
    public String getRedirectUrlTestPage() {
        return redirectUrlTestPage;
    }

    /**
     * @param redirectUrlTestPage the redirectUrlTestPage to set
     */
    public void setRedirectUrlTestPage(String redirectUrlTestPage) {
        this.redirectUrlTestPage = redirectUrlTestPage;
    }

    /**
     * @return the redirectUrlNoJsTestPage
     */
    public String getRedirectUrlNoJsTestPage() {
        return redirectUrlNoJsTestPage;
    }

    /**
     * @param redirectUrlNoJsTestPage the redirectUrlNoJsTestPage to set
     */
    public void setRedirectUrlNoJsTestPage(String redirectUrlNoJsTestPage) {
        this.redirectUrlNoJsTestPage = redirectUrlNoJsTestPage;
    }

    /**
     * @return the dataUrlTestPage
     */
    public String getDataUrlTestPage() {
        return dataUrlTestPage;
    }

    /**
     * @param dataUrlTestPage the dataUrlTestPage to set
     */
    public void setDataUrlTestPage(String dataUrlTestPage) {
        this.dataUrlTestPage = dataUrlTestPage;
    }




}
