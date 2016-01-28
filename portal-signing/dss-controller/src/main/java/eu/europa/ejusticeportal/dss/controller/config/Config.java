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
package eu.europa.ejusticeportal.dss.controller.config;

import java.io.InputStream;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ejusticeportal.dss.common.Defaults;
import eu.europa.ejusticeportal.dss.common.Defaults.Strategy;
import eu.europa.ejusticeportal.dss.controller.PortalFacade;
import eu.europa.ejusticeportal.dss.controller.exception.SigningException;
import eu.europa.ejusticeportal.dss.model.SigningMethod;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.CardProfileType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.DefaultsType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.DigAlgoType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.Parameter;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.SigningConfigType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.SigningContextRepositoryType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.SigningMethodsType;

/**
 * An XML implementation of a CardProfileRepository & MSPolicyRepository
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 373 $ - $Date: 2012-12-04 14:16:11 +0100 (mar., 04 déc. 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public final class Config  {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static Config instance = new Config();
    /**
     * The smartcard profiles
     */

    private Map<String, CardProfileType> profiles = new HashMap<String, CardProfileType>();
    private Map<Pattern, String> specialProfiles = new HashMap<Pattern, String>();

    private Defaults defaults;
    private String cachedXml = null;

    private Schema schema;
    private Map<String,List<SigningMethod>> methods;
    /**
     * 
     * The default constructor for SigningContextRepositoryXmlImpl.
     */
    private Config() {
        
        InputStream is = null;
        try {
            is = Config.class.getClassLoader().getResourceAsStream("signingcontext-v1.xsd");
            StreamSource source = new StreamSource(is,CardProfileNamespace.NS);
            schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(source);
            
        } catch (SAXException e){
            LOGGER.error("Unable to load XML validation schema - validation of configuration xml will not be done.", e);
        } finally {
            IOUtils.closeQuietly(is);
        }       
    }

    /**
     * Gets the instance
     * 
     * @return the instance
     */
    public static Config getInstance() {
        return instance;
    }

    /**
     * Refresh the repository
     * 
     * @param portal the PortalFacade
     */
    private void refresh(PortalFacade portal) {
        String xml = portal.getCardProfileXML();
        if (xml == null || xml.isEmpty()) {
            profiles.clear();
            specialProfiles.clear();
            cachedXml = xml;
        } else if (!xml.equals(cachedXml)) {

            try {
                synchronized (profiles) {
                    profiles.clear();
                    specialProfiles.clear();
                    InputStream is = null;

                    try {
                        
                        JAXBElement<SigningConfigType> rep = parseXml(xml);
                        List<CardProfileType> cps = rep.getValue().getSigningContextRepository().getCardProfiles().getCardProfile();

                        for (CardProfileType cp : cps) {
                            profiles.put(cp.getATR(), cp);
                        }

                        for (String atr : profiles.keySet()) {
                            if (atr.contains(".") || atr.contains("[")) {
                                specialProfiles.put(Pattern.compile(atr), atr);
                            }
                        }
                        defaults = getDefaults(rep.getValue().getSigningContextRepository());
                        methods = getMethods(rep.getValue().getSigningMethods());
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                    cachedXml = xml;
                }
            } catch (Exception e) {
                LOGGER.error("Error loading signing context.", e);
                throw new SigningException(e);
            }

        }
    }

    /**
     * @param signingMethods
     * @return
     */
    private Map<String,List<SigningMethod>> getMethods(SigningMethodsType smt) {
        Map<String,List<SigningMethod>> ms = new HashMap<String,List<SigningMethod>>();        
        if (smt!=null){
        for (eu.europa.ejusticeportal.dynforms.signing.profile.v1.SigningMethod m: smt.getSigningMethod()){
            SigningMethod sm = new SigningMethod();
            sm.setCode(m.getCode());
            sm.setJspPage(m.getJspPage());
            sm.setNeedsJava(m.isNeedsJava());
            sm.setCategory(m.getCategory());
            sm.setNeedsJavaScript(m.isNeedsJavaScript());
            sm.setPlatforms(m.getPlatform());
            if (m.getParameter()!=null){
                Map<String,String> params = new HashMap<String, String>();
                for (Parameter p:m.getParameter()){                        
                    params.put(p.getKey(), p.getValue());
                }
                sm.setParameterMap(params);
            }
            initDataTemplate(sm);
            if (ms.get(sm.getCategory())==null){
            	ms.put(sm.getCategory(), new ArrayList<SigningMethod>());
            }
            ms.get(sm.getCategory()).add(sm);            
        }
        }
        return ms;
    }
    /**
     * Initialises the template for the request post, if it exists
     * @param sm the signing method
     */
    private void initDataTemplate(SigningMethod sm) {
        if (sm.getParameterMap()!=null){
            String template = sm.getParameterMap().get("XML_REQUEST_DATA_TEMPLATE");
            if (template!=null){
                try {
                    sm.getParameterMap().put("XML_REQUEST_DATA",new String(IOUtils.toByteArray(Config.class.getClassLoader().getResourceAsStream(template)),"UTF-8"));
                } catch (Exception e) {
                    LOGGER.debug("No template",e);
                }
            }
        }        
    }
    /**
     * @param xml
     * @return
     * @throws JAXBException
     */
    private JAXBElement<SigningConfigType> parseXml(String xml) throws JAXBException {
        JAXBContext context;
        JAXBElement<SigningConfigType> rep;
        try {            
            context = JAXBContext.newInstance(CardProfileNamespace.NS);
            Unmarshaller u = context.createUnmarshaller();
            u.setSchema(schema);            
            rep = (JAXBElement<SigningConfigType>)u.unmarshal(new StreamSource(new StringReader(xml)));
        } catch (JAXBException e) {
            LOGGER.error("XML Error in the dss applet card profile repository file", e);
            //try again without validation
            context = JAXBContext.newInstance(CardProfileNamespace.NS);
            Unmarshaller u = context.createUnmarshaller();
            rep = (JAXBElement<SigningConfigType>)u.unmarshal(new StreamSource(new StringReader(xml)));
        }
        return rep;
    }

    private Defaults getDefaults(SigningContextRepositoryType signingContextRepositoryType) throws NoSuchAlgorithmException {
        DefaultsType dt = signingContextRepositoryType.getDefaults();
        Defaults ds = new Defaults();
        if (dt != null) {
            ds.setMaxTries(dt.getMaxTries() == null ? 0 : dt.getMaxTries().intValue());
            ds.setDefaultAlgorithm(dt.getDefaultDigAlgorithm()==null?null:DigestAlgorithm.valueOf(dt.getDefaultDigAlgorithm().value()).name());
            ds.setStrategy(Strategy.valueOf(dt.getStrategy().value()).name());
            if (dt.getDigestAlgorithm() != null) {
                List<String> algos = new ArrayList<String>();
                for (DigAlgoType t : dt.getDigestAlgorithm()) {
                    algos.add(DigestAlgorithm.valueOf(t.value()).name());
                }
                ds.setAlgorithms(algos);
            }
        }
        return ds;
    }

    /**
     * Get the card profile for the given ATR
     * 
     * @param atr the ATR of the card
     * @return the corresponding card profile, or none if none correspond
     */
    public CardProfileType getProfile(PortalFacade portal, String atr) {
        CardProfileType cp;

        synchronized (profiles) {
            refresh(portal);
            cp = profiles.get(atr);
            if (cp == null) {
                for (Map.Entry<Pattern, String> e : specialProfiles.entrySet()) {
                    if (e.getKey().matcher(atr).matches()) {
                        cp = profiles.get(e.getValue());
                        break;
                    }
                }
            }
        }
        return cp;
    }

    public Defaults getDefaults(PortalFacade portal) {
        synchronized (profiles) {
            refresh(portal);
            return defaults;
        }

    }
    
    /**
     * Get all the available signing methods
     * @return the signing methods
     */
    public Map<String,List<SigningMethod>> getSigningMethods(PortalFacade portal) {
        synchronized (profiles) {
            refresh(portal);
            return methods;
        }
    }
    }
