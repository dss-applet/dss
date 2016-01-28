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
package eu.europa.ejusticeportal.dss.controller;


/**
 * Configuration of document validation service
 *
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 *
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public interface DocumentValidationConfig {
    /**
     * Get the validation level for the file having to be sealed by the e-justice portal
     * @return the level
     */
    ValidationLevel getOriginValidationLevel();

    /**
     * Get the validation level for the file having to belong to the workflow
     * @return the level
     */
    ValidationLevel getWorkflowValidationLevel();

    /**
     * Get the validation level for the tampered file check
     * @return the level
     */
    ValidationLevel getTamperedValidationLevel();

    /**
     * Get the validation level for the trusted signature check
     * @return the level
     */
    ValidationLevel getTrustedValidationLevel();

    /**
     * Get the validation level for the revoked signature check
     * @return the level
     */
    ValidationLevel getRevokedValidationLevel();

    /**
     * Get the validation level for the file having to be signed
     * @return the level
     */
    ValidationLevel getSignedValidationLevel();

    /**
     * Get the validation level for the signature format having to be correct
     * @return the level
     */
    ValidationLevel getSignatureFormatValidationLevel();

    /**
     * Get the validation level for the signature time to be before the seal time 
     * @return the level
     */
    ValidationLevel getSignBeforeSealValidationLevel();
    
    /**
     * Get the root URL for the trusted lists
     * @return the root URL
     */
	String getLotlUrl();

	/**
	 * Get the period in seconds for the refresh of the trusted list cache
	 * @return the period in seconds
	 */
	int getRefreshPeriod();

	/**
	 *
	 * @return true if the refreshing trusted list source should verify the signature of trusted lists that it finds
	 */
	boolean isCheckLotlSig();

	/**
	 * Get the validation level for certificate expired or not yet valid
	 * @return the validation level
	 */
    ValidationLevel getExpiredValidationLevel();

	/**
	 * Get the seal method to be used for signing.
	 * @return the sealMethod
	 */
	SealMethod getSealMethod();
}
