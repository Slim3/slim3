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

<table>
<tr>
<td>${putEGLL}</td><td>${deleteEGLL}</td>
<td>${putEGSlim3}</td><td>${deleteEGSlim3}</td>
<td>${putEGJDO}</td><td>${deleteEGJDO}</td>
<td>${putEGJDO2}</td><td>${deleteEGJDO2}</td>
</tr>
<tr>
<td>
<form method="post" action="putEGLL">
<input type="submit" value="put EG LL"/>
</form>
</td>
<td>
<form method="post" action="deleteEGLL">
<input type="submit" value="delete EG LL"/>
</form>
</td>
<td>
<form method="post" action="putEGSlim3">
<input type="submit" value="put EG Slim3"/>
</form>
</td>
<td>
<form method="post" action="deleteEGSlim3">
<input type="submit" value="delete EG Slim3"/>
</form>
</td>
<td>
<form method="post" action="putEGJDO">
<input type="submit" value="put EG JDO"/>
</form>
</td>
<td>
<form method="post" action="deleteEGJDO">
<input type="submit" value="delete EG JDO"/>
</form>
</td>
<td>
<form method="post" action="putEGJDO2">
<input type="submit" value="put EG JDO2"/>
</form>
</td>
<td>
<form method="post" action="deleteEGJDO2">
<input type="submit" value="delete EG JDO2"/>
</form>
</td>
</tr>
</table>
</body>
</html>
