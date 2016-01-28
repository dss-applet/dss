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
package eu.europa.ejusticeportal.dss.demo.web.controller.sign;

import eu.europa.ejusticeportal.dss.controller.DocumentValidationConfig;
import eu.europa.ejusticeportal.dss.controller.SealMethod;
import eu.europa.ejusticeportal.dss.controller.ValidationLevel;
import eu.europa.ejusticeportal.dss.controller.action.DocumentValidationService;
import eu.europa.ejusticeportal.dss.controller.exception.DssInitialisationException;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the configuration
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DefaultDocumentValidationConfig implements DocumentValidationConfig {

    private static final Properties PROPS = new Properties();
    private static final ValidationLevel ORIGIN;
    private static final ValidationLevel WORKFLOW;
    private static final ValidationLevel TAMPERED;
    private static final ValidationLevel TRUSTED;
    private static final ValidationLevel REVOKED;
    private static final ValidationLevel SIGNED;
    private static final ValidationLevel SIGNATURE_FORMAT;    
    private static final ValidationLevel EXPIRED;    
    private static final ValidationLevel SIGNED_BEFORE_SEAL;

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentValidationService.class);
    private static final String LOTL_URL;
    private static final boolean CHECK_LOTL_SIGNATURE;
    private static final int LOTL_REFRESH_PERIOD;
    private static final SealMethod SEAL_METHOD;

    /**
     * Initialise the validations to apply. This determines if given validations are enabled
     * and if they result in error or warning statuses.
     */
    static {
        InputStream is = null;
        try {
            is = DocumentValidationConfig.class.getClassLoader().getResourceAsStream("dss_validation.properties");
            PROPS.load(is);
            ORIGIN = ValidationLevel.fromCode(PROPS.getProperty("ORIGIN"));
            WORKFLOW = ValidationLevel.fromCode(PROPS.getProperty("WORKFLOW"));
            TAMPERED = ValidationLevel.fromCode(PROPS.getProperty("TAMPERED"));
            TRUSTED = ValidationLevel.fromCode(PROPS.getProperty("TRUSTED"));
            REVOKED = ValidationLevel.fromCode(PROPS.getProperty("REVOKED"));
            SIGNED = ValidationLevel.fromCode(PROPS.getProperty("SIGNED"));
            SIGNED_BEFORE_SEAL = ValidationLevel.fromCode(PROPS.getProperty("SIGNED_BEFORE_SEAL"));
            SIGNATURE_FORMAT = ValidationLevel.fromCode(PROPS.getProperty("SIGNATURE_FORMAT"));
            EXPIRED = ValidationLevel.fromCode(PROPS.getProperty("EXPIRED"));
            LOTL_URL = PROPS.getProperty("LOTL_URL");
            LOTL_REFRESH_PERIOD = Integer.valueOf(PROPS.getProperty("LOTL_REFRESH_PERIOD"));
            CHECK_LOTL_SIGNATURE = Boolean.valueOf("CHECK_LOTL_SIGNATURE");
            SEAL_METHOD = readSealMethodFromPropertiesFile();
        }
        catch (RuntimeException e) {
            LOGGER.error("Error loading the validation properties", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error loading the validation properties", e);
            throw new DssInitialisationException("Unable to load dss_validation.properties.", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
    @Override
    public ValidationLevel getOriginValidationLevel(){
        return ORIGIN;
    }
    @Override
    public ValidationLevel getWorkflowValidationLevel(){
        return WORKFLOW;
    }
    @Override
    public ValidationLevel getTamperedValidationLevel(){
        return TAMPERED;
    }
    @Override
    public ValidationLevel getTrustedValidationLevel(){
        return TRUSTED;
    }
    @Override
    public ValidationLevel getRevokedValidationLevel(){
        return REVOKED;
    }
    @Override
    public ValidationLevel getSignedValidationLevel(){
        return SIGNED;
    }

    @Override
    public ValidationLevel getSignatureFormatValidationLevel() {
        return SIGNATURE_FORMAT;
        
    }
	@Override
	public String getLotlUrl() {
		return LOTL_URL;
	}
	@Override
	public int getRefreshPeriod() {
		return LOTL_REFRESH_PERIOD;
	}
	@Override
	public boolean isCheckLotlSig() {
		return CHECK_LOTL_SIGNATURE;
	}
    @Override
    public ValidationLevel getExpiredValidationLevel() {
        return EXPIRED;
    }
    @Override
    public ValidationLevel getSignBeforeSealValidationLevel() {
        return SIGNED_BEFORE_SEAL;
    }
    @Override
    public SealMethod getSealMethod() {
        return SEAL_METHOD;
    }

    /**
     * Reads the SEAL_METHOD from the properties file.
     * Throw a RuntimException in case of failure.
     * @return the SEAL_METHOD read from the properties file.
     */
    private static SealMethod readSealMethodFromPropertiesFile(){
        String property="";
        SealMethod sealMethod = null;
        try {
            property = PROPS.getProperty("SEAL_METHOD");
            sealMethod = SealMethod.fromCode(property);
        }catch (Exception e){
            throw new RuntimeException("Unable to load dss_validation.properties. Invalid value '"+String.valueOf(property)+"' for The 'SEAL_METHOD' parameter is missing");
        }
        return sealMethod;
    }
}
