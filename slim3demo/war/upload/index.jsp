<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>upload Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/upload/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/upload/UploadController.java">UploadController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/upload/DownloadController.java">DownloadController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/upload/ShowController.java">ShowController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/upload/DeleteController.java">DeleteController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/service/UploadService.java">UploadService</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/model/UploadedData.java">UploadedData model</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/model/UploadedDataFragment.java">UploadedDataFragment model</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/upload/index.jsp">index.jsp</a></li>
</ul>
<hr />

<form action="${f:url('upload')}" method="post" enctype="multipart/form-data">
<input type="file" name="formFile" /><br />
<input type="submit" value="Upload"/>
</form>
<table>
<c:if test="${fn:length(dataList) > 0}">
<thead>
<tr><th>FileName</th><th>Bytes</th></tr>
</thead>
</c:if>
<tbody>
<c:forEach var="e" items="${dataList}">
<tr>
<td>${f:h(e.fileName)}</td><td>${f:h(e.length)}</td>
<c:set var="downloadUrl" value="download?key=${f:h(e.key)}&version=${e.version}"/>
<c:set var="showUrl" value="show?key=${f:h(e.key)}&version=${e.version}"/>
<c:set var="deleteUrl" value="delete?key=${f:h(e.key)}&version=${e.version}"/>
<td><a href="${f:url(downloadUrl)}">Download</a></td>
<td><a href="${f:url(showUrl)}">Show</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('delete OK?')">Delete</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>
