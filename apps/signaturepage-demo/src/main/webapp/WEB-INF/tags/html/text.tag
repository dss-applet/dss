<%@ tag body-content="scriptless" pageEncoding="UTF-8"%><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags"%><%--
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %><%--
--%><%@ taglib prefix="app" tagdir="/WEB-INF/tags" %><%--
--%><%@ attribute name="styleId" required="false" %><%--
--%><%@ attribute name="styleClass" required="false" %><%--
--%><%@ attribute name="property" required="false" %><%--
--%><%@ attribute name="readonly" required="false" %><%--
--%><%@ attribute name="maxlength" required="false" %><%--
--%><%@ attribute name="title" required="false" %>
<input type="text" maxlength="${maxlength}" readonly="${readonly}" id="${styleId}" class="${styleClass}" name="${property}">