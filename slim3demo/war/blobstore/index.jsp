<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>blobstore Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blobstore/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blobstore/UploadController.java">UploadController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blobstore/ShowController.java">ShowController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/blobstore/DeleteController.java">DeleteController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/model/Blobstore.java">Blobstore model</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/blobstore/index.jsp">index.jsp</a></li>
</ul>
<hr />

<form action="${f:blobstoreUrl('upload')}" method="post" enctype="multipart/form-data">
<input type="file" name="formFile" /><br />
<input type="submit" value="Upload"/>
</form>
<table>
<c:if test="${fn:length(dataList) > 0}">
<thead>
<tr><th>Key</th></tr>
</thead>
</c:if>
<tbody>
<c:forEach var="e" items="${dataList}">
<tr>
<td>${f:h(e.key.name)}</td>
<c:set var="showUrl" value="show?keyName=${f:h(e.key.name)}"/>
<c:set var="deleteUrl" value="delete?keyName=${f:h(e.key.name)}"/>
<td><a href="${f:url(showUrl)}">Show</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('delete OK?')">Delete</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>