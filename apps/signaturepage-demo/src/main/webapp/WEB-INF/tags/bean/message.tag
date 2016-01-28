<%@ tag body-content="empty" pageEncoding="UTF-8"%><%--
--%><%@ taglib uri="urn://sign/taglib" prefix="sign" %><%--
--%><%@taglib uri="http://www.springframework.org/tags" prefix="spring"%><%--
--%><%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %><%--
--%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%--
--%><%@ attribute name="key" required="true"%><%--
--%><%@ attribute name="bundle" required="false"%><%--
--%><spring:message code="${key}"/>