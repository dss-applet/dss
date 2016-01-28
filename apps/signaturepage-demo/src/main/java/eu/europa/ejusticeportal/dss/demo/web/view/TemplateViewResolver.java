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
package eu.europa.ejusticeportal.dss.demo.web.view;

import java.util.Locale;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * <class_description> Class for creating a view instance which is a predefined template
 * with an attribute representing the dynamic part of the template.
 * The dynamic part is the inclusion of the given view name prefixed
 * by "/WEB-INF/jsp/" and suffixed by ".jsp"
 *
 * e.g. user -->  /WEB-INF/jsp/user.jsp
 *
 * @author ARHS Developments/unitb consulting
 * @version $Revision: 2307 $
 */
public class TemplateViewResolver extends InternalResourceViewResolver {
    private String contentUrlParameterName;
    private String templateUrl;

    /**
     * The default constructor for TemplateViewResolver.
     */
    public TemplateViewResolver() {
        setViewClass(JstlView.class);
    }

    /**
     * Sets the url of the template JSP file.
     * @param templateUrl The url of the template JSP file
     */
    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    /**
     * Sets the parameter name of the content url which is included in the template JSP file.
     * @param contentUrlParameterName The parameter name of the content url which is included in the template JSP file
     */
    public void setContentUrlParameterName(String contentUrlParameterName) {
        this.contentUrlParameterName = contentUrlParameterName;
    }

    /**
     * @return he name of the parameter in which the value of the actual body view is stored.
     */
    public String getContentUrlParameterName() {
        return this.contentUrlParameterName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean canHandle(String viewName, Locale locale) {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            return true;
        } else if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            return true;
        } else {
            String contentUrl = getPrefix() + viewName + getSuffix();
            WebApplicationContext webApplicationContext = (WebApplicationContext) getApplicationContext();
            return webApplicationContext.getServletContext().getResourceAsStream(contentUrl) != null;
        }
    }

    /**
     * Loads a JSTL view, just like the usual Use Case, but the given viewName will not be surrounded
     * by the site template; eg: for AJAX requests.
     *
     * @param viewName The body viewName.
     * @param locale The locale of the user, to be injected in the view.
     * @return The JSTL view without the surrounding template.
     * @throws Exception In case the view could not be resolved.
     */
    public AbstractUrlBasedView loadViewWithoutTemplate(String viewName, Locale locale) throws Exception {
        JstlView bareView = (JstlView) resolveViewName(viewName, locale);
        // Take the template out, so we just render the page body
        String contentUrl = (String) bareView.getStaticAttributes().get(this.contentUrlParameterName);
        bareView.setUrl(contentUrl);
        return bareView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        String contentUrl = getPrefix() + viewName + getSuffix();
        InternalResourceView view = (InternalResourceView) super.buildView(viewName);
        view.setUrl(this.templateUrl);
        view.addStaticAttribute(this.contentUrlParameterName, contentUrl);
        return view;
    }
}
