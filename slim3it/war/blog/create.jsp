<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<jsp:include page="/layout.jsp">
    <jsp:param name="title" value="Blog"/>
    <jsp:param name="content">
    <jsp:attribute name="value">
<form action="${f:url('insert')}" method="post">
Title<br />
<input type="text" name="title"/><br />
Content<br />
<textarea name="content"></textarea><br />
<input type="submit" value="Insert"/>
</form>
    </jsp:attribute>
    </jsp:param>
</jsp:include>