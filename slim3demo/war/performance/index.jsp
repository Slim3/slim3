<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>performance Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<table>
<tr>
<td>${putLL}</td><td>${deleteLL}</td>
<td>${putSlim3}</td><td>${deleteSlim3}</td>
<td>${putJDO}</td><td>${deleteJDO}</td>
</tr>
<tr>
<td>
<form method="post" action="putLL">
<input type="submit" value="put LL"/>
</form>
</td>
<td>
<form method="post" action="deleteLL">
<input type="submit" value="delete LL"/>
</form>
</td>
<td>
<form method="post" action="putSlim3">
<input type="submit" value="put Slim3"/>
</form>
</td>
<td>
<form method="post" action="deleteSlim3">
<input type="submit" value="delete Slim3"/>
</form>
</td>
<td>
<form method="post" action="putJDO">
<input type="submit" value="put JDO"/>
</form>
</td>
<td>
<form method="post" action="deleteJDO">
<input type="submit" value="delete JDO"/>
</form>
</td>
</tr>
</table>
</body>
</html>
