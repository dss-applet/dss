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
package eu.europa.ejusticeportal.dss.controller.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ejusticeportal.dss.common.SignedForm;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
/**
 * Download a signed PDF
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DownloadSignedPdf {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadSignedPdf.class);

    public DownloadSignedPdf() {
        
    }
    /**
     * Download a signed PDF
     * @param portal the portal
     * @param request the request
     * @param response the response
     */
    public void elaborate(final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response)  {
        
        try {
            download(portal, request, response);
        } catch (IOException e) {
            LOGGER.error("Error  downloading the document.",e);
        }
    }
    private void download(final PortalFacade portal, final HttpServletRequest request,
            final HttpServletResponse response) throws IOException{
        SignedForm sf = PdfTempStore.getSignedForm(request);
        if (sf != null){
            
            String baseFileName = portal.getPDFDocumentName(request);
            String contentType;
            String fileName;
            
            if (sf.isDetachedSignature()) {
                contentType = "application/zip";
                fileName = baseFileName +".zip";
            } else {
                contentType = "application/pdf";
                fileName = baseFileName;
            }             
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName+ "\"");
            if (sf.isDetachedSignature()) {
                writeZip(sf,response, baseFileName);
            } else {
                writeRaw(sf,response);
            }
        }
    }
    /**
     * @param sf
     * @param response
     * @throws IOException 
     */
    private void writeZip(SignedForm sf, HttpServletResponse response, String fileName) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        ZipEntry ze = new ZipEntry(fileName);
        zos.putNextEntry(ze);
        zos.write(sf.getDocument());
        ze = new ZipEntry(fileName+".xml");
        zos.putNextEntry(ze);
        zos.write(sf.getDetachedSignature());
        zos.closeEntry();
        zos.close();
    }
    /**
     * Writes the PDF document to the browser
     * @param sf the container
     * @param response the response
     * @throws IOException 
     */
    private void writeRaw(SignedForm sf, HttpServletResponse response) throws IOException {

        InputStream is = new ByteArrayInputStream(sf.getDocument());
        ServletOutputStream outs = null;
        try {
            outs = response.getOutputStream();
            int r = 0;
            byte[] chunk = new byte[8192];
            while ((r = is.read(chunk)) != -1) {
                outs.write(chunk, 0, r);
            }
            outs.flush();
        } finally {
            IOUtils.closeQuietly(outs);
        }
        
    }
    
}
