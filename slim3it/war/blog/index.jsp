<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Blog</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<a href="${f:url('create')}">Create</a>
<table>
<thead>
<tr><th>Title</th><th>Content</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${c.blogList}">
<tr>
<td>${f:h(e.title)}</td><td>${f:h(e.content)}</td>
<c:set var="url" value="edit?id=${e.key.id}"/>
<td><a href="${f:url(url)}">Edit</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>
