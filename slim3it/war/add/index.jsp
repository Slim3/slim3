<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>add Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<ul>
<c:forEach var="e" items="${f:errors()}">
<li>${f:h(e)}</li>
</c:forEach>
</ul>
<form method="post" action="${f:url('')}">
<input type="text" ${f:text("arg1")} class="${f:errorClass('arg1', 'error')}"/> +
<input type="text" ${f:text("arg2")} class="${f:errorClass('arg2', 'error')}"/> = ${f:h(result)}<br />
<input type="submit" value="Submit"/>
</form>
</body>
</html>
