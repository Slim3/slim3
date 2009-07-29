<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>guestbook Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<c:if test="${user != null}">
Hello ${user.nickname}!!! <a href="${logoutURL}">Logout</a>
</c:if>
<c:if test="${user == null}">
<a href="${loginURL}">Login</a>
</c:if>
<br />
<form action="${f:url('sign')}" method="post">
<div><textarea name="content" rows="3" cols="60"></textarea></div>
<div><input type="submit" value="Post Greeting" /></div>
</form>
<c:forEach var="e" items="${greetings}">
<c:if test="${e.author != null}">
${f:h(e.author.nickname)} wrote:
</c:if>
<c:if test="${e.author == null}">
anonymous wrote:
</c:if>
${f:h(e.content)}<br />
</c:forEach>
<br />
</body>
</html>
