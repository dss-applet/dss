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
package eu.europa.ejusticeportal.dss.applet;

import eu.europa.ejusticeportal.dss.Build;
import eu.europa.ejusticeportal.dss.applet.common.JavaScriptExpressionEvaluator;
import eu.europa.ejusticeportal.dss.applet.common.UIControllerHome;
import eu.europa.ejusticeportal.dss.applet.controller.cb.CallbackHandler;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEvent;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UIEventHandler;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UISynchEventHandler;
import eu.europa.ejusticeportal.dss.applet.controller.ui.UploadAppletLogHandler;
import eu.europa.ejusticeportal.dss.applet.event.ActivateCardTerminalWatcher;
import eu.europa.ejusticeportal.dss.applet.event.AppletStarted;
import eu.europa.ejusticeportal.dss.applet.event.AppletStopped;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.UIActivation;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.check.MacUnsafeCheck;
import eu.europa.ejusticeportal.dss.applet.model.check.VendorCheck;
import eu.europa.ejusticeportal.dss.applet.model.check.VersionCheck;
import eu.europa.ejusticeportal.dss.applet.model.service.AppletInitSemaphore;
import eu.europa.ejusticeportal.dss.applet.model.service.AsynchronousCallerHome;
import eu.europa.ejusticeportal.dss.applet.model.service.FileChooserHome;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousCaller;
import eu.europa.ejusticeportal.dss.applet.server.AsynchronousServerCall;
import eu.europa.ejusticeportal.dss.applet.server.ServerCall;
import eu.europa.ejusticeportal.dss.applet.view.UIEventListener;
import eu.europa.ejusticeportal.dss.applet.view.UIFunction;
import eu.europa.ejusticeportal.dss.applet.view.dialog.FileChooserImpl;
import eu.europa.ejusticeportal.dss.applet.view.dialog.PasswordInputFactoryImpl;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.ServerCallId;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;
import eu.europa.ejusticeportal.dss.common.exception.ExceptionUtils;
import eu.europa.ejusticeportal.dss.common.exception.UnexpectedException;

import java.applet.Applet;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Signing Applet.
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
@SuppressWarnings("serial")
public class DssApplet extends Applet {

    
    private static final long VERSION_CHECK_DELAY = 20000L;
    private static final DssLogger LOG = LoggingHome.getInstance().getLogger(DssApplet.class.getSimpleName());
    //Avoid logging string that are > 10ko
    private static final int LOGLIMIT = 0x2710;
    /**
     * Set this code if we are exiting for a particular condition that we can provide help with
     */
    private MessagesEnum managedExitCode;

    /**
     * Set to true when the javascript has been able to call a method on the applet, let's us know that
     * security for LiveConnect is working.
     */
    private boolean calledBack = false;
    private boolean versionOK = false;
    /**
     * DssApplet initialisation :
     * <ul>
     * <li>[0] Register event listener.</li>
     * <li>[1] Initialize the component allowing the Applet to evaluate JavaScript. </li>
     * <li>[2] Initialise the password input factory for token. </li>
     * <li>[3] Initialise the FileChooser implementation. </li>
     * <li>[4] Check the privilege of the DssApplet. </li>
     * <li>[5] Get parameters from the browser. </li>
     * <li>[6] Asynchronous call to server : asynchGetMessages, asynchGetSealedPdf. </li>
     * <li>[7] Detect the fingerprint and refresh the signing context.</li>
     * <li>[8] Activate the UI.</li>
     * </ul>
     *
     * @throws RuntimeException if the init() method results by a
     *                          RuntimeException, the Applet stop by calling
     *                          the initFailure method.
     *
     * {@inheritDoc}
     */
    @Override
    public void init() {
        //preload libraries
        preloadLibraries();
        
    	init(null,null,true);
    	LOG.info("Managed exit code is "+managedExitCode);
    	Timer t = new Timer("Version Checker", true);
    	t.schedule(new JreVersionChecker(),VERSION_CHECK_DELAY);
    }
    public void init(String asynchronousCallerClass, String uiControllerClass, boolean initJs){
        try {
            final String navPlatform = getParameter("navPlatform");
            final String userAgent = getParameter("userAgent");
            final String signingMethod = getParameter("signMethod");            
            final String problemVersion = getParameter("problemJreVersions");            
            FingerprintHome.getInstance().init(navPlatform, userAgent);

            
            if (!new VendorCheck().isOK()) {
                LOG.log(Level.INFO, "The vendor is not supported");
                managedExitCode = MessagesEnum.dss_applet_message_unsupported_vendor;
                return;
            }                        
            if (!new MacUnsafeCheck().isOK(FingerprintHome.getInstance().getFingerprint())){
                LOG.log(Level.INFO, "Mac needs to be in unsafe mode");
                managedExitCode = MessagesEnum.dss_applet_mac_unsafe;
                return;
            }
            // [1] Initialize the component allowing the Applet to evaluate JavaScript.
            if (initJs){
                JavaScriptExpressionEvaluator.getInstance().init(this);
            }
            versionOK = new VersionCheck().isOK(FingerprintHome.getInstance().getFingerprint(),problemVersion);
            
            LOG.log(Level.INFO, "init applet version {0} {1}", new Object[] { Build.getBuildVersion(),
                    Build.getBuildTimestamp() });
            // [0] Register event listener.
            Event.getInstance().registerListener(new UIEventListener());

            AsynchronousCallerHome.getInstance().init(asynchronousCallerClass);
            UIControllerHome.getInstance().init(uiControllerClass);
            Event.getInstance().fire(new LoadingRefreshed(true, false));
            // [2] Initialise the password input factory for token.
            TokenManager.getInstance().init(PasswordInputFactoryImpl.getInstance());
            // [3] Initialise the FileChooser implementation.
            FileChooserHome.getInstance().init(new FileChooserImpl());
            // [4] Check the privilege of the DssApplet.
            checkPrivileges();

            AppletSigningMethod sm = null;
            if (signingMethod !=null && signingMethod.length()!=0){
                sm = AppletSigningMethod.valueOf(signingMethod);
            }
            if (sm == null) {
                sm = AppletSigningMethod.sc;
                LOG.log(Level.INFO, "Signing method not provided, defaulting to smart card");                
            }
            SigningMethodHome.getInstance().setSigningMethod(sm);
            
            
            // [6] Asynchronous call to server : asynchGetMessages, asynchGetSealedPdf.
            AsynchronousCaller ac = AsynchronousCallerHome.getInstance().getCaller();
            ac.fire(new ServerCall(AsynchronousServerCall.callServer,ServerCallId.getMessageBundle, (Object[]) null));
            ac.fire(new ServerCall(AsynchronousServerCall.callServer, ServerCallId.getSealedPdf, (Object[]) null));
            managedExitCode = MessagesEnum.dss_applet_message_ok;
        } catch (RuntimeException ex) {
            ExceptionUtils.log(new UnexpectedException(ex, "Fail to initialise the applet."), LOG);
            initFailure();
        }

        /*
         * Optional - The environment detection is not required for the user to
         * sign. He still can use PKCS12.
         */
        try {
            // [7] Detect the fingerprint and refresh the signing context.
            FingerprintHome.getInstance().refresh();
            AsynchronousCaller ac = AsynchronousCallerHome.getInstance().getCaller();
            ac.fire(new ServerCall(AsynchronousServerCall.callServer,ServerCallId.getSigningContext, FingerprintHome.getInstance()
                    .getFingerprint()));
        } catch (CodeException ex) {
            // failure, but the user can still remove the card or use PKCS12 instead.
            AppletInitSemaphore.getInstance().setSigningContextReady(true);
            Event.getInstance().fire(new StatusRefreshed(MessagesEnum.valueOf(ex.getCode()), MessageLevel.ERROR));
        } catch (Exception ex ){
            ExceptionUtils.log(new UnexpectedException(ex, "Fail to initialise the applet."), LOG);
            initFailure();
        }

        //start monitoring the card terminals
        if (AppletSigningMethod.sc.equals(SigningMethodHome.getInstance().getSigningMethod())
                &&FingerprintHome.getInstance().getFingerprint().getOs().toLowerCase().contains("windows")){
            Event.getInstance().fire(new ActivateCardTerminalWatcher(true));
        }

        // [8] Activate the UI.
        Event.getInstance().fire(new UIActivation(this));        
        LOG.info("Applet initialised");
    }


    private void preloadLibraries() {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                Security.addProvider(new BouncyCastleProvider());
                return null;
            }
        });        
    }
    /**
     * Check that all required privilege are granted, stop applet if not.
     */
    private void checkPrivileges() {

        try {
            final SecurityManager security = System.getSecurityManager();
            if (security != null) {
                final Permission perm = new AllPermission();
                // Throws a security exception if not allowed
                security.checkPermission(perm);
            }
        } catch (Exception ex) {
            ExceptionUtils.log(new UnexpectedException(ex, "Fail to check privileges."), LOG);
            initFailure();
        }
    }

    /**
     * Performs all necessary operations in case of failure at init time
     * (display a proper error message or redirect
     * (see 'redirectToNoJavaPage'), stop the Applet, ...
     *
     * Note : another option would be to throw an exception out the init()
     * function ().
     */
    private void initFailure() {
        UIControllerHome.getInstance().getUiController().eval(UIFunction.redirectToNoJavaPage);
        Event.getInstance().fire(new LoadingRefreshed(false, true));
        TokenManager.getInstance().closeTokenConnections();
    }

    /**
     * Called from when a callback is received from asynch call to
     * the server.
     *
     * @param method  callback name
     * @param data    xml data
     * @param errCode xml error code
     */
    public void handleServerCallBack(final String method, final String data, final String errCode, final String hash, final String algo) {
        calledBack = true;                
        
        CallbackHandler callbackHandler;
        
        if(errCode==null && !canTrustServerHash(data, hash, algo)){
            UIControllerHome.getInstance().getUiController().eval(UIFunction.closeForTamperedCommunication);
            callbackHandler = new CallbackHandler(null, "dss_applet_message_technical_failure");
        }
        else{
            ServerCallId serverCall = ServerCallId.valueOf(method);
            if (data!=null && data.length() < LOGLIMIT) {
                LOG.log(Level.FINEST, "handleServerCallBack:\nmethod={0}\narg1={1}\narg2={2}", new Object[] { serverCall.name(), data,
                        errCode });
            } else if (data!=null){
                LOG.log(Level.FINEST, "handleServerCallBack:\nmethod={0}\narg1={1}\narg2={2}", new Object[] { serverCall.name(),
                        "data too long", errCode });
            } else {
                LOG.log(Level.FINEST, "handleServerCallBack:\nmethod={0}\narg1={1}\narg2={2}", new Object[] { serverCall.name(),
                        "data is null", errCode });
            }
            if (errCode !=null) {
                LOG.log(Level.INFO, "Server calls back after \"{0}\" with error code \"{1}\"", new Object[] { serverCall.getLabel(),
                    errCode });
            } else {
                LOG.log(Level.INFO, "Server calls back after \"{0}\"", new Object[] { serverCall.getLabel(),
                        errCode });            
            }
            callbackHandler =  new CallbackHandler(data, errCode);
        }
        callbackHandler.handle(ServerCallId.valueOf(method));
    }
    
    /** 
     * This methods checks if the data coming from the server can be trusted.
     * The hash provided by the server is checked using the public key.
     * @param data the data coming from the server.
     * @param serverHash the hash of the data coming from the server.
     * @param algo the algorithm used for the server hash.
     * @return <code>true</code> if the serverHash can be verified with the public key.
     */
    private boolean canTrustServerHash(final String data, final String serverHash, final String algo){
        Certificate certificate;
        InputStream pemInputStream;
        try {
            pemInputStream = getClass().getClassLoader().getResourceAsStream("certificate.pem");
            if (pemInputStream == null) {
                LOG.log(Level.SEVERE, "Missing certificate.pem file. Impossible to check if the data coming from the server can be trusted.");
                return false;
            }
        } catch (Exception e){
            LOG.log(Level.SEVERE, "Missing certificate.pem file. Impossible to check if the data coming from the server can be trusted.");
            return false;
        }

        try {
            certificate = CertificateFactory.getInstance("X.509").generateCertificate(pemInputStream);
            PublicKey publicKey =certificate.getPublicKey();
            Signature sigVerify = Signature.getInstance(new String(Base64.decodeBase64(algo)), "BC");
            sigVerify.initVerify(publicKey);
            sigVerify.update(data.getBytes("UTF-8"));
            
            boolean signatureMatch = sigVerify.verify(Base64.decodeBase64(serverHash));
            if(signatureMatch){
                LOG.log(Level.INFO, "The data coming from the server can be trusted.");
                return true;
            }
            else{
                LOG.log(Level.SEVERE, "!!! Tampered data received !!!");
                LOG.log(Level.INFO,serverHash);
                LOG.log(Level.INFO,data);
                return false;
            }
        } catch (CertificateException e) {
            LOG.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            LOG.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            LOG.error(e.getMessage(), e);
        } catch (SignatureException e) {
            LOG.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.log(Level.SEVERE, "Impossible to check if the data coming from the server can be trusted.");
        return false;
    }

    /**
     * Called from JavaScript when an UI event is triggered.
     *
     * @param method event name
     * @param arg
     */
    public void handleUiEvent(final String method, final String arg) {
        //Java 5 plugin does not properly handle array arguments between java and javascript (String... args or String[] args)
        //This is a fallback solution
        final String logArg;
        if (method!=null && (method.equals(UIEvent.providePassword.name())||method.equals(UIEvent.refusePassword.name()))){
            //mask the password in the log
            logArg=(arg==null?null:arg.replaceAll(".", "*"));
        } else {
            logArg = arg;
        }
        LOG.log(Level.FINE, "handleUiEvent:\nmethod={0}\narg={1}", new Object[] { method, logArg });

        new UIEventHandler(arg).handle(UIEvent.valueOf(method));
    }

    /**
     * Called from JavaScript when an UI event is triggered - synchronous method calls.
     * Needed exceptionally when the browser should block until the method executes.
     * @param method event name
     * @param arg
     */
    public void handleUiEventSynch(final String method, final String arg) {
        //Java 5 plugin does not properly handle array arguments between java and javascript (String... args or String[] args)
        //This is a fallback solution
        final String logArg;
        if (method!=null && (method.equals(UIEvent.providePassword.name())||method.equals(UIEvent.refusePassword.name()))){
            //mask the password in the log
            logArg=(arg==null?null:arg.replaceAll(".", "*"));
        } else {
            logArg = arg;
        }
        LOG.log(Level.FINE, "handleUiEventSynch:\nmethod={0}\narg={1}", new Object[] { method, logArg });

        new UISynchEventHandler(arg).handle(UIEvent.valueOf(method));
    }

    /**
     * A method to allow the page to check that the applet is still alive.
     * This is needed because some actions on some cards on some OS cause the JVM to crash.
     * The page periodically invokes this method and can react if the invocation fails.
     */
    public void ping() {
        LOG.log(Level.FINE,"ping");
    }
    /**
     * DssApplet start :
     * <ul>
     * <li>[1] Show the UI and set the event. </li>
     * </ul>
     *
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (managedExitCode == MessagesEnum.dss_applet_message_ok) {
            LOG.info("Applet is starting...");
            UIControllerHome.getInstance().getUiController().eval(UIFunction.initCommonJava);            
            Event.getInstance().fire(new AppletStarted());
            LOG.info("Applet is started");
        }
    }

    /**
     * DssApplet stop :
     * <ul>
     * <li>[1] Close the UI. </li>
     * <li>[2] Close tokens connections. </li>
     * </ul>
     *
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        LOG.info("Applet is stopping...");
        TokenManager.getInstance().closeTokenConnections();
        Event.getInstance().fire(new ActivateCardTerminalWatcher(false));
        Event.getInstance().fire(new AppletStopped());                
    }

    /**
     * DssApplet destroy :
     * <ul>
     * <li>[1] Close the JRE instance which was running the DssApplet.</li>
     * </ul>
     *
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        LOG.info("Applet is destroying...");
        /*
         * The Java process created by the applet can take more than five minutes to exit, the System is required to
         * exit to bypass this behaviour.
         * All required operations are already done in stop() function.
         */
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
           public Void run() {
               //TODO remove the System.exit(0). 
               //If the user has other applets running in his browser we also kill those.
                System.exit(0);
                return null;
            }
        });
    }
    /**
     * Method called from the page beforeunload event handler to allow the applet event log to be uploaded.
     */
    public void uploadAppletLog(){
        boolean ask = getParameter("askForEventLog") == null?false:Boolean.parseBoolean(getParameter("askForEventLog"));
        if (ask) {
            try {
                //don't go through the normal UI event handler because we have to block on this method
                //if we don't block, the page unloads and destroys the applet.
                UploadAppletLogHandler.getInstance().doHandle(null, null);
            } catch (CodeException e) {
                LOG.error("Error uploading applet log",e);
            }
        }
    }
    /**
     * Method to force the applet to close; called directly from the UI.
     */
    public void killApplet(){
    	destroy();
    }
    

    /**
     * Method allowing the javascript to detect if the vendor is supported.
     * @return "true" if the vendor is supported, "false" if not, "" if the test was not done yet
     */
    public String getManagedExitCode() {
        if (managedExitCode == null) {
            return "";
        } else {
            return managedExitCode.name();
        }
    }
    
    /**
     * This task checks that the server has been able to call back to the applet. If it hasn't, there's
     * a security setting problem with JRE 1.7_u20 to 45.
     */
    class JreVersionChecker extends TimerTask {
        public void run() {
            if (!versionOK && !calledBack){
                LOG.info("Problem version "+FingerprintHome.getInstance().getJreVersion());
                managedExitCode = MessagesEnum.dss_applet_message_bug_java_version;
                final String problemJreWarning = getParameter("problemJreWarning");
                try {
                    SwingUtilities.invokeAndWait(new Runnable(){
                            public void run(){
                                JOptionPane.showMessageDialog(DssApplet.this,problemJreWarning, "Unsupported version",JOptionPane.OK_OPTION);
                            }
                            });
                } catch (InterruptedException e) {
                    LOG.error("JreVersionChecker Interrupted", e);
                } catch (InvocationTargetException e) {
                    LOG.error("JreVersionChecker Target", e);
                }
                return;
            }
        }

    }
}
