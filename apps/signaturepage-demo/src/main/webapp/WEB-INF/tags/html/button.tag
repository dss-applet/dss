<%@ tag body-content="scriptless" pageEncoding="UTF-8"%><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags"%><%--
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags/bean" %><%--
--%><%@ attribute name="styleId" required="false" %><%--
--%><%@ attribute name="title" required="false" %><%--
--%><%@ attribute name="titleKey" required="false" %><%--
--%><%@ attribute name="style" required="false" %><%--
--%><%@ attribute name="property" required="false" %><%--
--%><%@ attribute name="styleClass" required="false" %><%--
--%><%@ attribute name="bundle" required="false" %><%--
--%><%@ attribute name="onclick" required="false" %><%--
--%><c:set var="bTitle"><app:message key = "${titleKey}" bundle="${bundle }"/></c:set>

<button type="button" class ="${styleClass}" title="${bTitle }" id="${styleId}" onclick="${onclick}" style="${style}"><jsp:doBody/></button>