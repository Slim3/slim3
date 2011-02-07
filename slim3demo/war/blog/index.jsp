<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Blog(Slim3)</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blog/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blog/CreateController.java">CreateController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blog/EditController.java">EditController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blog/DeleteController.java">DeleteController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blog/InsertController.java">InsertController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blog/UpdateController.java">UpdateController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/service/BlogService.java">BlogService</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/model/Blog.java">Blog model</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/blog/index.jsp">index.jsp</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/blog/create.jsp">create.jsp</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/blog/edit.jsp">edit.jsp</a></li>
</ul>
<hr />

<a href="${f:url('create')}">Create</a>
<table>
<thead>
<tr><th>Title</th><th>Content</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${blogList}">
<tr>
<td>${f:h(e.title)}</td><td>${f:br(f:nbsp(f:h(e.content)))}</td>
<c:set var="editUrl" value="edit/${f:h(e.key)}/${e.version}"/>
<c:set var="deleteUrl" value="delete/${f:h(e.key)}/${e.version}"/>
<td><a href="${f:url(editUrl)}">Edit</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('delete OK?')">Delete</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>