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
/**
 *
 * A component on the UI representing a certificate
 *
 * <p>DISCLAIMER: Project owner DG-JUSTICE.</p>
 *
 * @version $Revision: 322 $ - $Date: 2012-11-27 17:40:10 +0100 (Tue, 27 Nov
 * 2012) $
 * @author <a href="mailto:ejustice.project-dss@arhs-developments.com">ARHS
 * Developments</a>
 */
package eu.europa.ejusticeportal.dss.applet.view.component;

import eu.europa.ejusticeportal.dss.common.Utils;

public class Certificate extends Text {
		
	private boolean recommended;
	private boolean hide;
	private boolean dateValid;
	private String summaryInfo;
	private String issuerName;
	private String issuerCountry;

	/**
	 * Constructor for {@link Certificate}
	 * @param id the uniue id of the certificate, which is used to link the selected one to one held in
	 * applet memory
	 * @param displayName what is displayed to the end user
	 * @param dateValid true if the certificate is not expired or is after start of validity 
	 * think the user will not want to use it.
	 * @param summaryInfo 
	 * @param issuerName the name of the certificate issuer
	 * @param country the country of the issuer
	 */
	public Certificate(String id, String displayName, boolean dateValid, String summaryInfo,String issuerName, String country) {
		super(id);
		this.setText(displayName);
		this.setDateValid(dateValid);
		this.setSummaryInfo(summaryInfo);
		this.setIssuerName(issuerName);
		this.setIssuerCountry(country==null?"":country);
	}
	/**
	 * Set the recommended
	 * @param recommended
	 */
	public void setRecommended(boolean recommended) {
        setChanged(this.recommended!=recommended);
		this.recommended = recommended;
	}

	public void setHide(boolean hide) {
	    setChanged(this.hide!=hide);
		this.hide = hide;
	}

	/**
     * Writes the component as a Json string
     *
     * @return the json string
     */
    @Override
    public String toJson() {
        StringBuilder s = new StringBuilder();        
        s.append("\"recommended\":").append(Utils.escape(Boolean.toString(recommended))).append(",");
        s.append("\"dateValid\":").append(Utils.escape(Boolean.toString(dateValid))).append(",");
        s.append("\"summaryInfo\":\"").append(Utils.escape(summaryInfo)).append("\"").append(",");
        s.append("\"issuerName\":\"").append(Utils.escape(issuerName)).append("\"").append(",");
        s.append("\"issuerCountry\":\"").append(Utils.escape(issuerCountry)).append("\"").append(",");
        s.append("\"hide\":").append(Utils.escape(Boolean.toString(hide)));
        final String tmp = super.toJson();
        if (tmp.length() != 0) {
            s.append(",").append(tmp);
        }
        return s.toString();
    }

    /**
     * @return the dateValid
     */
    public boolean isDateValid() {
        return dateValid;
    }

    /**
     * @param dateValid the dateValid to set
     */
    public final void setDateValid(boolean dateValid) {
        setChanged(this.dateValid!=dateValid);
        this.dateValid = dateValid;
    }

    /**
     * @return the summaryInfo
     */
    public String getSummaryInfo() {
        return summaryInfo;
    }

    /**
     * @param summaryInfo the summaryInfo to set
     */
    public final void setSummaryInfo(String summaryInfo) {
        setChanged(this.summaryInfo,summaryInfo);
        this.summaryInfo = summaryInfo;
    }
    /**
     * @return the issuerName
     */
    public String getIssuerName() {
        return issuerName;
    }
    /**
     * @param issuerName the issuerName to set
     */
    public final void setIssuerName(String issuerName) {
        setChanged(this.issuerName, issuerName);
        this.issuerName = issuerName;
    }
    /**
     * @return the issuerCountry
     */
    public String getIssuerCountry() {
        return issuerCountry;
    }
    /**
     * @param issuerCountry the issuerCountry to set
     */
    public final void setIssuerCountry(String issuerCountry) {
        setChanged(this.issuerCountry,issuerCountry);
        this.issuerCountry = issuerCountry;
    }

}
