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
package eu.europa.ejusticeportal.dss.applet.model.action;

import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedAction;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;

/**
 *
 * Action with elevated privileged to open the SealedPdf on the client computer.
 *
 * <p> DISCLAIMER: Project owner DG-JUSTICE. </p>
 *
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
public class OpenPdfAction extends AbstractPrivilegedAction {

    private static final DssLogger LOG = DssLogger.getLogger(OpenPdfAction.class.getSimpleName());
    /**
     * content of the PDF
     */
    private final byte[] pdf;

    /**
     * Default constructor of OpenPdfAction.
     *
     * @param pdf
     */
    public OpenPdfAction(final byte[] pdf) {
        this.pdf = pdf.clone();
    }

    /**
     * Execute the OpenPdfAction.
     */
    @Override
    public void doExec() {
        FileOutputStream fos = null;
        try {
            //On MacOS the temp location is difficult to access for a normal user => use user.home instead
            String userHome = System.getProperty("user.home");
            if (new File(userHome).listFiles()==null){
                //e.g. safari on mac
                LOG.info("user.home not accessible, using java.io.tmpdir");
                userHome = System.getProperty("java.io.tmpdir");
            }
            LOG.info("save pdf to "+userHome);
            final File pdfFile = new File(new File(userHome),"dynform"+System.currentTimeMillis()+ ".pdf");
            pdfFile.deleteOnExit();
            final String pdfFilePath = pdfFile.getAbsolutePath();
            fos = new FileOutputStream(pdfFile);

            fos.write(pdf);
            LOG.log(Level.FINE, "The local path of the pdf is : {0}", pdfFilePath);
            IOUtils.closeQuietly(fos);            
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.dss_applet_message_pdf_path, MessageLevel.INFO, pdfFilePath));
            openURL(pdfFilePath);
        } catch (IOException ex) {
            IOUtils.closeQuietly(fos);
            // The user can't do anything about this
            ExceptionUtils.exception(new UnexpectedException(
                    "Can't write PDF to temp area of disk (required for display).", ex), LOG);
        }
    }
    /**
     * Static list of browser for Linux
     */
    private static final String[] BROWSERS = {"google-chrome", "firefox", "opera", "epiphany", "konqueror",
        "conkeror", "midori", "kazehakase", "mozilla"};

    /**
     * Source : http://www.centerkey.com/java/browser/. If possible, use Desktop
     * object to open local file. If not, use native code
     *
     * @param url
     */
    protected void openURL(String url) {
        try { 
            // attempt to use Desktop library from JDK 1.6+
            openDesktop(url);
        } catch (Exception e) { 
            // library not available or failed
            final String osName = System.getProperty("os.name");
            LOG.info("java.awt.Desktop.getDesktop().browse() unavailable for "+osName);

            try {
                if (osName.startsWith("Mac OS")) {
                    Runtime.getRuntime().exec("open "+url);
                } else if (osName.startsWith("Windows")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else { 
                    // assume Unix or Linux
                    String browser = null;
                    for (String b : BROWSERS) {
                        if (browser == null
                                && Runtime.getRuntime().exec(new String[]{"which", b}).getInputStream().read() != -1) {
                            browser = b;
                            Runtime.getRuntime().exec(new String[]{browser, "file:///" + url});
                        }
                    }
                }
            } catch (Exception ex) {
                // Cannot open the PDF
                ExceptionUtils.exception(new UnexpectedException("Unable to open the PDF.", ex), LOG);
            }
        }
    }

    /**
     * Tries to open file using java.awt.Desktop
     * @param url the file to open
     * @throws Exception if desktop not available
     */
    private void openDesktop(String url) throws Exception {
        final Class<?> d = Class.forName("java.awt.Desktop");
        LOG.log(Level.FINE, "URI to open : " + "file:///{0}", url.replace("\\", "/"));
        d.getDeclaredMethod("browse", new Class[]{java.net.URI.class}).invoke(
                d.getDeclaredMethod("getDesktop").invoke(null),
                new Object[]{java.net.URI.create("file:///" + url.replace("\\", "/"))});
        LOG.log(Level.FINE,"java.awt.Desktop.getDesktop().browse() available");
        // above code mimics: java.awt.Desktop.getDesktop().browse()
    }
}
