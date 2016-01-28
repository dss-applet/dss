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


import eu.europa.ejusticeportal.dss.applet.event.CertificatesRefreshed;
import eu.europa.ejusticeportal.dss.applet.event.StatusRefreshed;
import eu.europa.ejusticeportal.dss.applet.event_api.Event;
import eu.europa.ejusticeportal.dss.applet.model.service.FingerprintHome;
import eu.europa.ejusticeportal.dss.applet.model.service.LoggingHome;
import eu.europa.ejusticeportal.dss.applet.model.service.PasswordHome;
import eu.europa.ejusticeportal.dss.applet.model.service.SigningMethodHome;
import eu.europa.ejusticeportal.dss.applet.model.token.TokenManager;
import eu.europa.ejusticeportal.dss.common.AbstractPrivilegedExceptionAction;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.CardProfile;
import eu.europa.ejusticeportal.dss.common.CertificateDisplayDetails;
import eu.europa.ejusticeportal.dss.common.MessageLevel;
import eu.europa.ejusticeportal.dss.common.MessagesEnum;
import eu.europa.ejusticeportal.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.SigningContext;
import eu.europa.ejusticeportal.dss.common.exception.CodeException;
import eu.europa.ejusticeportal.dss.common.exception.DssLogger;

import java.util.List;
import java.util.logging.Level;

/**
 * Action that refresh the token along with a SigningContext.
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision$ - $Date$
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 *         Developments</a>
 */
public class RefreshTokensAction extends AbstractPrivilegedExceptionAction {

	/**
	 * Delay between refresh and re-refresh (waiting for middleware to warm up).
	 */
	private static final long RE_REFRESH_DELAY = 5000L;
	/** Number of times to try */
	private static final int RE_REFRESH_ATTEMPTS = 2;
	private static final DssLogger LOG = LoggingHome.getInstance().getLogger(
			RefreshTokensAction.class.getSimpleName());

	private SigningContext signingContext;

	/**
	 * 
	 * The constructor for RefreshTokensAction.
	 * 
	 * @param signingContext
	 *            the SigningContext
	 */
	public RefreshTokensAction(SigningContext signingContext) {
		this.signingContext = signingContext;
	}

	/**
	 * 
	 * Execute the RefreshTokensAction.
	 */
	@Override
	protected void doExec() throws CodeException {
		TokenManager tokenManager = TokenManager.getInstance();
		// [0] SigningContext comes from server ...
		AppletSigningMethod m = SigningMethodHome.getInstance()
				.getSigningMethod();
		if (signingContext.getCardProfiles() != null
				&& (AppletSigningMethod.sc.equals(m) || AppletSigningMethod.installed_cert
						.equals(m))) {
			validate();
		}
		// [1] ... then the tokens are refreshed
		tokenManager.refreshTokens();
		List<CertificateDisplayDetails> certs = TokenManager.getInstance()
				.getCertificateNames();
		if (certs.isEmpty()
				&& FingerprintHome.getInstance().getFingerprint()
						.isCardDetectionAvailable()
				&& signingContext.getDetectedCardCount() > 0) {
			LOG.info("No certificates found with card inserted");
			// wait and try again - let the card / middleware warm up.
			// Note this gives user time to switch cards and what we try
			// to sign with can be different from what is in the signing
			// context.
			// It could give rise to an error during signing + some incorrect
			// statistics.
			if (!PasswordHome.getInstance().wasCancelled()) {

				for (int i = 0; i < RE_REFRESH_ATTEMPTS; i++) {
					try {
						Thread.sleep(RE_REFRESH_DELAY);
					} catch (InterruptedException e) {
					}
					tokenManager.refreshTokens();
					certs = TokenManager.getInstance().getCertificateNames();
					if (PasswordHome.getInstance().wasCancelled()) {
						break;
					}
					if (certs.isEmpty()) {
						LOG.info("Still no certificates found with card inserted.");
					} else {
						LOG.log(Level.FINE, "There are now {0} certificates.",
								Integer.toString(certs.size()));
					}
				}
			}
		}
		Event.getInstance().fire(new CertificatesRefreshed(certs));
	}

	/**
	 * Does some validation of the signing context - warn if there are more than
	 * one PKCS11 cards connected - warn if there are more than one MOCCA cards
	 * connected
	 */
	private void validate() {
		int pkcs11Count = 0;
		for (CardProfile cp : signingContext.getCardProfiles()) {
			if (cp.getApi() != null
					&& cp.getApi().equals(SignatureTokenType.PKCS11)) {
				pkcs11Count++;
			}
		}
		int totalCount = signingContext.getDetectedCardCount();
		if (totalCount > 1 && pkcs11Count > 1) {
			// The only multi-card combos that work are MSCAPI, MOCCA or MSCAPI
			// + 1 PKS11
			Event.getInstance().fire(
					new StatusRefreshed(
							MessagesEnum.dss_applet_message_more_than_one_card,
							MessageLevel.ERROR));
		}
	}
}
