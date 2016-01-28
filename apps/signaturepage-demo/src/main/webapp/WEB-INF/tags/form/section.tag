<%@ tag body-content="scriptless" pageEncoding="UTF-8"%><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags/bean"%><%--
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %><%--
--%><%@ attribute name="name" required="false" %><%--
--%><%@ attribute name="descr" required="false" %>
<div class="common-box" id="${name}">
<c:if test="${not empty descr}">
	<h2><app:message key="${descr}" bundle="none"/></h2>
</c:if>
		<jsp:doBody/>
</div>