<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<jsp:include page="/layout.jsp">
    <jsp:param name="title" value="Blog"/>
    <jsp:param name="content">
    <jsp:attribute name="value">
<a href="${f:url('create')}">Create</a>
<table>
<thead>
<tr><th>Title</th><th>Content</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${blogList}">
<tr>
<td>${f:h(e.title)}</td><td>${f:h(e.content)}</td>
<c:set var="editUrl" value="edit?key=${e.key}"/>
<c:set var="deleteUrl" value="delete?key=${e.key}"/>
<td><a href="${f:url(editUrl)}">Edit</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('delete OK?')">Delete</a></td>
</tr>
</c:forEach>
</tbody>
</table>
    </jsp:attribute>
    </jsp:param>
</jsp:include>