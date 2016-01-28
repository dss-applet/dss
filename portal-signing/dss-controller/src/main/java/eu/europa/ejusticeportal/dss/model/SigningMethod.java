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
package eu.europa.ejusticeportal.dss.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A signing method.
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class SigningMethod implements Serializable{

    
	
	private static final long serialVersionUID = 1L;

	private boolean needsJavaScript;
    
    private boolean needsJava;
    
    private String category;
    
    private String code;
    
    private String jspPage;
    
    private Map<String,String> parameterMap;

    private List<String> platforms;

    /**
     * @return true if the signing method needs JavaScript
     */
    public boolean getNeedsJavaScript() {
        return needsJavaScript;
    }

    /**
     * @param needsJavaScript true if the signing method needs JavaScript
     */
    public void setNeedsJavaScript(boolean needsJavaScript) {
        this.needsJavaScript = needsJavaScript;
    }

    /**
     * 
     * @return true if the signing method needs Java
     */
    public boolean getNeedsJava() {
        return needsJava;
    }

    /**
     * @param needsJava true if the signing method needs Java
     */
    public void setNeedsJava(boolean needsJava) {
        this.needsJava = needsJava;
    }

    /**
     * @return the code of the signing method
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code of the signing method
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the a parameter map configuring the signing method
     */
    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    /**
     * @param parameterMap the parameter map to configure the signing method
     */
    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    /**
     * @return the name of the JSP page that supports the method
     */
    public String getJspPage() {
        return jspPage;
    }

    /**
     * @param jspPage the name of the JSP page that supports the method
     */
    public void setJspPage(String jspPage) {
        this.jspPage = jspPage;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SigningMethod [needsJavaScript=" + needsJavaScript + ", needsJava=" + needsJava + ", code=" + code
                + ", jspPage=" + jspPage + ", parameterMap=" + parameterMap + "]";
    }

    /**
     * Sets the list of platforms. If null or empty, all platforms are assumed
     * Otherwise look at navigator.platform.toLowerCase containing the platform string.
     * @param platforms the list of platforms
     */
    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    /**
     * @return the platforms
     */
    public List<String> getPlatforms() {
        return platforms;
    }
    
    /**
     * Get the category for this method
     * @return the category
     */
    public String getCategory() {
		return category;
	}

    /**
     * Set the category for this method
     * @param category the category
     */
	public void setCategory(String category) {
		this.category = category;
	}
}
