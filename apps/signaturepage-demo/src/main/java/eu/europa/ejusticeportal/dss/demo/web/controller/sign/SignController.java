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
package eu.europa.ejusticeportal.dss.demo.web.controller.sign;

import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.action.ActionHandler;
import eu.europa.ejusticeportal.dss.controller.action.ActionId;
import eu.europa.ejusticeportal.dss.model.SigningMethod;
import eu.europa.ejusticeportal.dss.model.SigningMethodsHome;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for the demonstration 
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1071 $ - $Date: 2013-03-04 11:57:27 +0100 (Mon, 04 Mar 2013) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
@Controller
public class SignController {

    public static final String UPLOADED_FILE_KEY = "uploaded-file";
    private static final String CHOOSE_HTML = "/choosesign.html";
    private static final String CHOOSE_VIEW = "sign/choosesign";
    private static final String FILE_HTML = "/choosefile.html";
    private static final String FILE_VIEW = "sign/choosefile";
    private static final String APPLET_VIEW = "sign/signingapplet";
    private static final String SERVERCALL_HTML = "/dynform_signing_ajax_action.do";
    private static final String HELP_HTML = "/dynform_signing_help_action.do";
    private static final String HELP_VIEW = "sign/help";

    @Autowired
    private PortalFacade portal;

    /**
     * Get the page for selecting a file
     * @return the page mapping
     */
    @RequestMapping(value = FILE_HTML, method = RequestMethod.GET)
    public String chooseFile() {
        return FILE_VIEW;
    }

    /**
     * Upload a file 
     * @param map the {@link ModelMap}
     * @param file the file to upload 
     * @param request the {@link HttpServletRequest}
     * @return the page mapping for choose sign method
     * @throws IOException
     */
    @RequestMapping(value = FILE_HTML, method = RequestMethod.POST)
    public String chooseFile(ModelMap map, @RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws IOException {
        request.getSession().setAttribute(UPLOADED_FILE_KEY, file.getBytes());
        return CHOOSE_VIEW;
    }

    /**
     * Get the help page
     * @return the mapping for the help page
     */
    @RequestMapping(value = HELP_HTML, method = RequestMethod.GET)
    public String viewHelp() {
        return HELP_VIEW;
    }

    /**
     * Get the signing method choice page
     * @param request the {@link HttpServletRequest}
     * @return the page mapping
     */
    @RequestMapping(value = CHOOSE_HTML, method = RequestMethod.GET)
    public String chooseSignMethod(HttpServletRequest request) {
        if (request.getSession().getAttribute(UPLOADED_FILE_KEY)==null){
            return FILE_VIEW;
        } else {
            return CHOOSE_VIEW;
        }
    }

    /**
     * Choose a signing method
     * @param map the {@link ModelMap}
     * @param request the {@link HttpServletRequest}
     * @param sm the signing method code
     * @return the applet page mapping
     */
    @RequestMapping(value = CHOOSE_HTML, method = RequestMethod.POST)
    public String chooseSignMethod(ModelMap map,HttpServletRequest request, @RequestParam(value = "SM", required = true) String sm) {
        if (request.getSession().getAttribute(UPLOADED_FILE_KEY)==null){
            return FILE_VIEW;
        } else {
            SigningMethod m = SigningMethodsHome.getInstance().getMethod(portal, sm);
            map.addAttribute("dss_sign_method", m);
            return APPLET_VIEW;
        }
    }

    /**
     * Handle the {@link GetSigningContext} AJAX request
     * @param id the ajax request id
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     */
    @RequestMapping(value = SERVERCALL_HTML, method = RequestMethod.POST, params = "id=getSigningContext")
    public void getSigningContext(@RequestParam(value = "id", required = true) String id, HttpServletRequest request,
            HttpServletResponse response) {
        ActionHandler.handleAjaxRequest(ActionId.fromName(id), portal, request, response);
    }

    /**
     * Handle the {@link GetSealedPdf} AJAX request
     * @param id the ajax request id
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     */    @RequestMapping(value = SERVERCALL_HTML, method = RequestMethod.POST, params = "id=getSealedPdf")
    public void getSealedPdf(@RequestParam(value = "id", required = true) String id, HttpServletRequest request,
            HttpServletResponse response) {
        ActionHandler.handleAjaxRequest(ActionId.fromName(id), portal, request, response);

    }

     /**
      * Handle the AJAX request for the {@link MessageBundle}
      * @param id the ajax request id
      * @param request the {@link HttpServletRequest}
      * @param response the {@link HttpServletResponse}
      */
     @RequestMapping(value = SERVERCALL_HTML, method = RequestMethod.POST, params = "id=getMessageBundle")
    public void getMessageBundle(@RequestParam(value = "id", required = true) String id, HttpServletRequest request,
            HttpServletResponse response) {
        ActionHandler.handleAjaxRequest(ActionId.fromName(id), portal, request, response);

    }
     /**
      * Handle the AJAX request to upload a signed PDF
      * @param id the ajax request id
      * @param request the {@link HttpServletRequest}
      * @param response the {@link HttpServletResponse}
      */
    @RequestMapping(value = SERVERCALL_HTML, method = RequestMethod.POST, params = "id=uploadSignedPdf")
    public void uploadSignedPdf(@RequestParam(value = "id", required = true) String id, HttpServletRequest request,
            HttpServletResponse response) {
        ActionHandler.handleAjaxRequest(ActionId.fromName(id), portal, request, response);

    }
    /**
     * Handle the AJAX request to log statistics
     * @param id the ajax request id
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     */
    @RequestMapping(value = SERVERCALL_HTML, method = RequestMethod.POST, params = "id=logStatistics")
    public void logStatistics(@RequestParam(value = "id", required = true) String id, HttpServletRequest request,
            HttpServletResponse response) {
        ActionHandler.handleAjaxRequest(ActionId.fromName(id), portal, request, response);

    }

    /**
     * Handle the AJAX request to upload the applet log
     * @param id the ajax request id
     * @param request the {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse}
     */    @RequestMapping(value = SERVERCALL_HTML, method = RequestMethod.POST, params = "id=uploadAppletLog")
    @ResponseBody
    public void uploadAppletLog(@RequestParam(value = "id", required = true) String id, HttpServletRequest request,
            HttpServletResponse response) {
        ActionHandler.handleAjaxRequest(ActionId.fromName(id), portal, request, response);

    }
}
