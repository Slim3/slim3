<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<jsp:include page="/layout.jsp">
    <jsp:param name="title" value="Blog"/>
    <jsp:param name="content">
    <jsp:attribute name="value">
<form action="${f:url('update')}" method="post">
<input type="hidden" name="key" value="${key}"/>
Title<br />
<input type="text" name="title" value="${f:h(title)}"/><br />
Content<br />
<textarea name="content">${f:h(content)}</textarea><br />
<input type="submit" value="Update"/>
</form>
    </jsp:attribute>
    </jsp:param>
</jsp:include>