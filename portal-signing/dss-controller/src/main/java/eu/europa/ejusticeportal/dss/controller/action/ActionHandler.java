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
package eu.europa.ejusticeportal.dss.controller.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.model.SigningMethodsHome;

/**
 *
 * Handles the requests coming from the signing form
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc.
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public final class ActionHandler {

    private static final GetSealedPdf GET_SEALED_PDF = new GetSealedPdf();
    private static final GetSigningContext GET_SIGNING_CONTEXT = new GetSigningContext();
    private static final ValidateSignedPdf VALIDATE_SIGNED_PDF = new ValidateSignedPdf();
    private static final DownloadSealedPdf DOWNLOAD_SEALED_PDF = new DownloadSealedPdf();    
    private static final DownloadSignedPdf DOWNLOAD_SIGNED_PDF = new DownloadSignedPdf();
    private static final GetLocalisedMessages GET_LOCALISED_MESSAGES = new GetLocalisedMessages();
    private static final ExpiredSession EXPIRED_SESSION = new ExpiredSession();
    private static final StoreSignedPdf STORE_SIGNED_PDF = new StoreSignedPdf();
    private static final LoggingStatistics LOGGING_STATISTICS = new LoggingStatistics();

    private static final String TRUE = "true";
    /**
     * 
     * The constructor for ActionHandler.
     */
    private ActionHandler(){
        
    }
    /**
     * Handle the request
     *
     * @param id       the id of the request
     * @param portal   the portal
     * @param request  the request
     * @param response the response
     * @param data     possible form data
     * @return true if the method found something to do, false if nothing to do
     *         or needs to go back to the current page.
     */
    public static boolean handleRequest(final ActionId id, final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response) {
        boolean handled = false;
        switch (id) {
        case DOWNLOAD_PDF:
            DOWNLOAD_SEALED_PDF.elaborate(portal, request, response);
            handled = true;
            break;
        case DOWNLOAD_PDF_NO_JAVA:
            DOWNLOAD_SEALED_PDF.elaborate(portal, request, response);
            request.setAttribute(ActionId.FORWARD_NOJAVA.getName(), TRUE);
            handled = true;
            break;
        case DOWNLOAD_SIGNED_PDF:
            DOWNLOAD_SIGNED_PDF.elaborate(portal, request, response);
            request.setAttribute(ActionId.FORWARD_NOJAVA.getName(), TRUE);
            handled = true;
            break;            
        case FORWARD_NOJAVA:
            request.setAttribute(ActionId.FORWARD_NOJAVA.getName(), TRUE);
            handled = false;
            break;

        case RELOAD_APPLET:
        case SELECT_SIGN_METHOD:
            handled = false;
            break;
        
        default:
            //not handled
        }
        return handled;
    }

    public static boolean handleAjaxRequest(final ActionId id, final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response) {
        boolean handled = false;
        switch (id) {
        case AJAX_GET_SEALED_PDF:
            GET_SEALED_PDF.elaborate(portal, request, response);
            handled = true;
            break;
        case AJAX_GET_SIGNATURE_CONTEXT:
            GET_SIGNING_CONTEXT.elaborate(portal, request, response);
            handled = true;
            break;
        case EXPIRED_SESSION_AJAX:
            EXPIRED_SESSION.elaborate(portal, request, response);
            break;
        case AJAX_SIGN_DOC:
            VALIDATE_SIGNED_PDF.elaborate(portal, request, response);
            handled = true;
            break;
        case AJAX_GET_LOCALISED_MESSAGE:
            GET_LOCALISED_MESSAGES.elaborate(portal, request, response);
            handled = true;
            break;
        case AJAX_LOG_STATISTIC:
        case AJAX_LOG_APPLET_EVENTS:
            LOGGING_STATISTICS.elaborate(portal, request, response);
            handled = true;
            break;
        default:
            //not handled
        }
        return handled;
    }

    /**
     * Handles the Next action
     *
     * @param portal   the portal
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @return true if handled
     */
    public static boolean handleNext(PortalFacade portal, HttpServletRequest request, HttpServletResponse response) {
        if (TRUE.equals(request.getParameter(ActionId.STAY_NOJAVA.getName()))) {
            request.setAttribute(ActionId.FORWARD_NOJAVA.getName(), TRUE);
        }
        return STORE_SIGNED_PDF.storePdf(portal, request);
    }
    
    /**
     * Set the signing methods in the request scope
     * @param portal the portal facade
     * @param request the request
     * @param response the response
     */
    public static void handleCommon(PortalFacade portal, HttpServletRequest request, HttpServletResponse response) {
        
        request.setAttribute("dssSigningMethods", SigningMethodsHome.getInstance().getSigningMethods(portal,request.getHeader("User-Agent")));       
    }
}
