<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--
 * $HeadURL: https://forge.aris-lux.lan/svn/dgdevco-prospect/trunk/pro-eval/pro-eval-webapp/src/main/webapp/WEB-INF/jsp/layout/template.jsp $
 * $Revision: 2340 $
 * $Date: 2013-10-21 11:33:38 +0200 (Mon, 21 Oct 2013) $
 * $Author: morelem $
 *
 * Application: pro-eval
 * Contractor: ARHS-Developments
--%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags"%><%--
--%><%@ taglib prefix="bean" tagdir="/WEB-INF/tags/bean"%><%--
--%><?xml version="1.0" encoding="UTF-8" ?><%--
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Signing Page Demo</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Import font from google webfont -->
<link href='http://fonts.googleapis.com/css?family=Open+Sans+Condensed:300,700' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Open+Sans:300' rel='stylesheet' type='text/css'>

<!-- Template CSS for mobile - Do not modify -->
<link rel="stylesheet" href="css/reset.css">
<link rel="stylesheet" href="css/modernapp.css">
<link rel="stylesheet" href="css/animations/component.css">
<link rel="stylesheet" href="css/animations/animations.css">
<!-- Template CSS for desktop - Do not modify -->
<link rel="stylesheet" href="css/modernapp-desktop.css" media="screen and (min-width: 900px)">
<link rel="stylesheet" href="css/dss_applet/jqueryui/jquery-ui.css">
<link rel="stylesheet" href="css/dss_applet/jqueryui/jquery.ui.dialog.css">
<link rel="stylesheet" href="css/dss_applet/dss_applet.css">

<!-- Template JS to activate HTML5 for old browsers - Do not modify -->
<script type="text/javascript" src="js/template.js"></script>
<script type="text/javascript" src="js/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.4.custom.min.js"></script>

<!-- Specific CSS and JS for old browsers - Do not modify -->
<!--[if lte IE 8]>
<link rel="stylesheet" href="css/modernapp-desktop.css">
<link rel="stylesheet" href="css/modernapp-ie.css">
<script type="text/javascript"> activeHTML(); </script>
<![endif]-->
<!--[if IE 7]>
<link rel="stylesheet" href="css/modernapp-ie7.css">
<script type="text/javascript" src="css/fonts/lte-ie7.js"></script>	
<![endif]-->

	</head>
	
	<body>
			<c:set var = "successMessage" scope="request"><bean:message key='dss_applet_message_signedpdf_ok' bundle='generalLabels'/></c:set>
 			<c:set var="formAction" value="choosesign.html" scope="request"/>
			<c:import url="header.jsp" charEncoding="UTF-8"/>
			<div id="contentPanel">
				<div id="contentDiv">
				    <div class="pt-page pt-page-1">
	    				<div class="content" style="margin-top:50px;" role"main">
	            			<c:import url="${contentUrl}" charEncoding="UTF-8" />
	        			</div>
    				</div>
				</div>
			</div>
<!-- jQuery loader -->
<!-- Template JS - Do not modify -->
<script type="text/javascript" src="js/modernapp.js"></script>
<!-- Loading animation -->
<script type="text/javascript">
$(document).ready(function(){
	//$('.pt-page-1').css({display:'block'}).addClass('pt-page-flipInLeft');		
});
</script>

<!-- Custom scripts HERE -->
 	
	</body>
</html>