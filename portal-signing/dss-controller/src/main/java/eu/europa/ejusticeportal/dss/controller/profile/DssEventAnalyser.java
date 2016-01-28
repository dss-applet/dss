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
package eu.europa.ejusticeportal.dss.controller.profile;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.markt.dss.common.SignatureTokenType;
import eu.europa.ejusticeportal.dss.common.AppletSigningMethod;
import eu.europa.ejusticeportal.dss.common.SignatureEvent;
import eu.europa.ejusticeportal.dss.controller.config.CardProfileNamespace;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.APIContextType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.APIType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.CardProfileType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.CardProfilesType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.DigAlgoType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.OSType;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.ObjectFactory;
import eu.europa.ejusticeportal.dynforms.signing.profile.v1.SigningContextRepositoryType;

/**
 * 
 * Transforms a LogEntry into a card profile
 *  
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 * 
 * @version $Revision: 1704 $ - $Date: 2014-04-25 18:44:40 +0200 (Fri, 25 Apr 2014) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class DssEventAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(DssEventAnalyser.class);
    /**
     * Analyses a log entry and decides if there is enough information to create a new entry
     * in the card profile repository
     * @param log the SignatureLogEntry
     * @return the XML for a new entry, or empty string if there is not enough information
     * @throws JAXBException 
     */
    public String transform(SignatureEvent log){
        StringBuffer transformed = new StringBuffer();
        
        if (newProfilePossible(log)){
            ObjectFactory of = new ObjectFactory();
            CardProfilesType cps = of.createCardProfilesType();                        
            CardProfileType cpt = of.createCardProfileType();
            cps.getCardProfile().add(cpt);
            
            cpt.setATR(log.getAtr());
            cpt.setDescription(log.getUserSuppliedCardIssuer()==null?"Unknown":log.getUserSuppliedCardIssuer());
            cpt.setDigestAlgorithm(DigAlgoType.fromValue(log.getDigestAlgorithm()));
            
            APIContextType api = new APIContextType();
            api.setArch(log.getArch());
            api.setOS(OSType.fromValue(log.getOs()));
            api.setAPI(APIType.fromValue(log.getApi()));
            if (log.getApi().equals(SignatureTokenType.PKCS11.name())){
                api.getLibraryPath().add(log.getUserSuppliedPkcs11Path().replace('\\', '/'));
            }
            cpt.getAPIContext().add(api);
            try {
                JAXBContext context = JAXBContext.newInstance(CardProfileNamespace.NS);
                Marshaller m = context.createMarshaller();
                m.setProperty("jaxb.formatted.output", Boolean.TRUE);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                SigningContextRepositoryType sc = of.createSigningContextRepositoryType();
                sc.setCardProfiles(cps);
                m.marshal(of.createSigningContextRepository(sc), os);            
                transformed.append(new String(os.toByteArray(),"UTF-8"));
            } catch (UnsupportedEncodingException e){
                LOGGER.error("Error analysing the log entry.",e);
            } catch (JAXBException e){
                LOGGER.error("Error analysing the log entry.",e);
            }
        }                        
        return transformed.toString();
    }
    /**
     * Tests if there is enough information in the log to create a new card profile
     * @param log the log to test
     * @return true if there is enough information
     */
    public static boolean newProfilePossible(SignatureEvent log){
        boolean possible = false;        
        if (AppletSigningMethod.sc.name().equals(log.getSigningMethod())){            
            if (!log.isNeedsUserInput() || !log.isSigned()){
                possible = false;
            }
            else if (log.getAtr()==null||log.getAtr().isEmpty()){
                possible = false;
            } else if (log.getDigestAlgorithm()==null||log.getSignatureAlgorithm()==null){
                possible = false;
            } else if (log.getArch()==null||log.getOs()==null) {
                possible = false;
            } else if (log.getApi()==null|| (log.getApi().equals(SignatureTokenType.PKCS11.name()) &&
                    (log.getUserSuppliedPkcs11Path()==null||log.getUserSuppliedPkcs11Path().isEmpty()))){
                possible = false;
            }
            else {
                possible = true;
            }
        }        
        return possible;
    }
}
