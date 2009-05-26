<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<jsp:include page="/layout.jsp">
    <jsp:param name="title" value="Index"/>
    <jsp:param name="content">
    <jsp:attribute name="value"> 
<h1>Welcome to Slim3</h1>
    </jsp:attribute>
    </jsp:param>
</jsp:include>