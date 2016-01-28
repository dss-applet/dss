<%@ tag body-content="scriptless" pageEncoding="UTF-8"%><%--
--%><%@ taglib prefix="bean" tagdir="/WEB-INF/tags/bean"%><%--
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags" %><%--
--%><%@ attribute name="id" required="false" %><%--
--%><%@ attribute name="name" required="true" %><%--
--%><%@ attribute name="style" required="false" %><%--
--%><%@ attribute name="onclick" required="false" %><%--
--%><c:set var="labelText"><bean:message key = "${name}"/></c:set>
<label><c:out value="${labelText}"/></label>