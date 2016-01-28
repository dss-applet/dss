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
package eu.europa.ejusticeportal.dss.applet.service;

import eu.europa.ejusticeportal.dss.applet.event.ClearMessages;
import eu.europa.ejusticeportal.dss.applet.event.ClosePasswordDialog;
import eu.europa.ejusticeportal.dss.applet.event.LoadingRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.CardDetectorJre6;
import eu.europa.ejusticeportal.dss.applet.model.action.RefreshSigningContextAction;
import eu.europa.ejusticeportal.dss.applet.model.service.CardTerminalWatchService;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 * 
 * Service that watches the card terminals and fires refresh events when the card is inserted or removed.
 * This is not supported on Linux or MacOS due to implementation of smartcardio on those platforms. 
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DefaultCardTerminalWatchService  extends TimerTask implements CardTerminalWatchService {

    private final List<Watcher> monitors = new ArrayList<Watcher>();
    private List<CardTerminal> terminals = null;
    private static final DssLogger LOG = DssLogger.getLogger(DefaultCardTerminalWatchService.class.getSimpleName());
    private boolean shouldRefreshCards = false;
    private Timer t;
    /**
     * 
     * The default constructor for CardTerminalWatchService.
     */
    public DefaultCardTerminalWatchService(){
       
    }
 
    public void startup(){
    	try {
    		t = new Timer(false);
    		t.schedule(this,0,5000L);
    	} catch (Exception e) {
    		LOG.error("Error starting the terminal watchers", e);
    	}
    }
    public void shutdown() {
    	if (t!=null) {
    		
    		t.cancel();
    	}
    	shutdownMonitors();    	
    }
    /**
     * Startup the service
     */
    public void run(){
    	try {
    	    TerminalFactory factory;
            factory = CardDetectorJre6.getTerminalFactory();
			List<CardTerminal>ts = factory.terminals().list();
			if (!ts.isEmpty() && terminals==null){
				terminals = ts;
				startupTerminals(terminals);
			} else if (!ts.isEmpty() && terminals != null && terminals.size()!=ts.size()){
			    shutdownMonitors();
			    terminals = ts;
			    shouldRefreshCards = true;
			    startupTerminals(terminals);
			    
			}
		} catch (CardException e) {
			if (terminals != null){
				shutdownMonitors();
				terminals = null;
			}
			shouldRefreshCards = true;
		}
    }
    public void startupTerminals(List<CardTerminal> terminals){
        
        try {
             
            for (CardTerminal ct:terminals){
                Watcher m = new Watcher(ct);
                monitors.add(m);
                Thread th = new Thread(m);
                th.start();
                LOG.log(Level.INFO,"Started watching terminal {0}", ct.getName());
                if (shouldRefreshCards){
                	shouldRefreshCards = false;
                	//just to refresh the certificates list
                	fireCardStateChanged(true);
                }
            }
        } catch (Exception e) {
            LOG.error( "Fail to start the card terminal monitoring service",e);
        } 
    }
    /**
     * Shutdown the service - can take maximum 10s to shutdown due to timeout period
     * in the Watcher class.
     */
    public void shutdownMonitors() {
        for (Watcher m:monitors){
            m.stop();
        }
        
    }

    /**
     * Runnable to monitor a particular terminal
     */
    private static final class Watcher implements Runnable {

        private static final long TO = 10000L;
        private CardTerminal terminal;
        private volatile boolean run = true;
        
        /** 
         * The default constructor for Monitor.
         * @param t the terminal to watch
         */
        private Watcher(CardTerminal t){
            this.terminal = t;
        }
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {           
            
            while (run){
                try {
                    watchTerminal();
                } catch (CardException e) {
                    LOG.error( "Failure terminal monitoring service",e);
                }
            }
        }
        /**
         * Stop the service
         */
        public void stop(){
            run = false;
        }
        
        /**
         * Watch the terminals for changes
         * @throws CardException
         */
        private void watchTerminal() throws CardException{
            try {
                if (terminal.isCardPresent()){
                    if (terminal.waitForCardAbsent(TO)){
                        if (run) {
                            fireCardStateChanged(false);
                        }
                    }
                } else {
                    if (terminal.waitForCardPresent(TO)){
                        if (run) {
                            fireCardStateChanged(true);
                        }
                    }
                }
            } catch (CardException e) {
                
            }
        }
        
        
        
    }
    /**
     * Fire the change event
     * @param true if the event was caused by card being inserted, false if caused
     * by removal of card
     */
    private static void fireCardStateChanged(boolean isPresent) {
        try {
            Event.getInstance().fire(new ClearMessages());
            Event.getInstance().fire(new ClosePasswordDialog());
            Event.getInstance().fire(new LoadingRefreshed(true, false,MessagesEnum.dss_applet_message_long_operation));
            if (isPresent){
                //Sleep to let the card warm up
                try {
                    Thread.sleep(4000L);
                } catch (InterruptedException e) {
                 
                }
            }
            new RefreshSigningContextAction().exec();
        } catch (CodeException e) {
            LOG.error("Fail to refresh signing context from the watch service",e);
        }
        try {
            //sleep here is to avoid an exception in smartcardio when we try to listen again for changes too soon after the last change.
            Thread.sleep(500L);
        } catch (InterruptedException e) {
        }
    }
}
