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
<ul>
<c:forEach var="e" items="${f:errors()}">
<li>${f:h(e)}</li>
</c:forEach>
</ul>
<form action="${f:url('update')}" method="post">
<input type="hidden" ${f:hidden("key")}/>
<input type="hidden" ${f:hidden("version")}/>
Title<br />
<input type="text" ${f:text("title")} class="${f:errorClass('title', 'err')}"/><br />
Content<br />
<textarea name="content" class="${f:errorClass('content', 'err')}">${f:h(content)}</textarea><br />
<input type="submit" value="Update"/>
</form>
</body>
</html>