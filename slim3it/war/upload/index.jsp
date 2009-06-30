<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>upload Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form action="${f:url('upload')}" method="post" enctype="multipart/form-data">
<input type="file" name="formFile" /><br />
<input type="submit" value="Upload"/>
</form>
<table>
<thead>
<tr><th>FileName</th><th>Bytes</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${uploadList}">
<tr>
<td>${f:h(e.fileName)}</td><td>${f:h(e.length)}</td>
<c:set var="downloadUrl" value="download?key=${e.key}&version=${e.version}"/>
<c:set var="deleteUrl" value="delete?key=${e.key}&version=${e.version}"/>
<td><a href="${f:url(downloadUrl)}">Download</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('delete OK?')">Delete</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>
