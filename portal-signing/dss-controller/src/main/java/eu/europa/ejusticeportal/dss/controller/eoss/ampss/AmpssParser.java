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
package eu.europa.ejusticeportal.dss.controller.eoss.ampss;

import eu.europa.ejusticeportal.dss.controller.action.ValidateSignedPdf;
import eu.europa.ejusticeportal.dss.controller.eoss.EossResponse;
import eu.europa.ejusticeportal.dss.controller.eoss.EossResponseParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parses the response from the austrian signature server
 * 
 * <p>
 * DISCLAIMER: Project owner DG-JUSTICE.
 * </p>
 * 
 * @version $Revision: 6522 $ - $Date: 2012-06-11 17:53:23 +0200 (Mon, 11 Jun 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS Developments</a>
 */
public class AmpssParser implements EossResponseParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateSignedPdf.class);

    /** xpath to get the error response code */
    private static final String XPATH_ERROR_CODE = "//*[local-name()=\"ErrorResponse\"]/*[local-name()=\"ErrorCode\"]/text()";
    /** xpath to get the error response message */
    private static final String XPATH_ERROR_MESSAGE = "//*[local-name()=\"ErrorResponse\"]/*[local-name()=\"Info\"]/text()";
    /** xpath to get the signature */
    private static final String XPATH_SIGNATURE = "//*[local-name()=\"Signature\"]";
    
    /**The error code for user cancellation */
    private static final String CANCELLED = "6001";
    /**The error code for cancel after invalid TAN*/
    private static final String CANCELLED_INVALID_TAN = "6000";
    
    
    private static final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
    static {
        DBF.setExpandEntityReferences(false);
    }
    /**
     * Parse the response from ampss
     * 
     * @param s the response XML
     * @return the parsed response.
     */
    @Override
    public EossResponse parseResponse(String s) {

        EossResponse response;
        try {
            response = parse(s);
        } catch (Exception e) {
            LOGGER.error("Error parsing the AMPSS xml response: " + s, e);
            response = new EossResponse();
            response.setParseError(true);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    /**
     * Parse the response from ampss. It will handle an error response or a signature response.
     * 
     * @param s the response to parse
     * @return the parsed response
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    private EossResponse parse(String s) throws XPathExpressionException, ParserConfigurationException,
            SAXException, IOException, TransformerException {
        EossResponse response = new EossResponse();
        DocumentBuilder builder = DBF.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(s.getBytes("UTF-8")));
        XPathFactory xFact = XPathFactory.newInstance();
        XPathExpression xpathError = xFact.newXPath().compile(XPATH_ERROR_CODE);
        NodeList nodes = (NodeList) xpathError.evaluate(doc, XPathConstants.NODESET);
        if (nodes != null && nodes.getLength() != 0) {
            response.setErrorCode(nodes.item(0).getTextContent());
            response.setCancelled(CANCELLED.equals(response.getErrorCode())||CANCELLED_INVALID_TAN.equals(response.getErrorCode()));
            xpathError = xFact.newXPath().compile(XPATH_ERROR_MESSAGE);
            nodes = (NodeList) xpathError.evaluate(doc, XPathConstants.NODESET);
            if (nodes != null && nodes.getLength() != 0) {
                response.setErrorMessage(nodes.item(0).getTextContent());
            }
        } else {
            XPathExpression xpathSignature = xFact.newXPath().compile(XPATH_SIGNATURE);
            nodes = (NodeList) xpathSignature.evaluate(doc, XPathConstants.NODESET);
            if (nodes != null && nodes.getLength() != 0) {
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                StringWriter buffer = new StringWriter();
                transformer.transform(new DOMSource(nodes.item(0)), new StreamResult(buffer));
                response.setSignature(buffer.toString().replaceAll("&#13;", ""));
            } else {
                // we received a response that is not an error or a signature, so it is an error for us.
                response.setParseError(true);
                response.setErrorMessage("-1000");
                LOGGER.error("Received an unknown response from the austrian signature server: "+s);
            }
        }
        return response;
    }
}
