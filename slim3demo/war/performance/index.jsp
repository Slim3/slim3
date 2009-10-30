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
Bar count:${count}
<table>
<tr>
<td>${getLL} millis</td>
<td>${getSlim3} millis</td>
<td>${getJDO} millis</td>
</tr>
<tr>
<td>
<form method="post" action="getLL">
<input type="submit" value="Low level API:get"/>
</form>
</td>
<td>
<form method="post" action="getSlim3">
<input type="submit" value="Slim3:get"/>
</form>
</td>
<td>
<form method="post" action="getJDO">
<input type="submit" value="JDO:get"/>
</form>
</td>
</tr>
</table>
Source codes:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/model/Bar.java" target="_blank">Model for Slim3</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/cool/model/BarJDO.java" target="_blank">Model for JDO</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetLLController.java" target="_blank">Controller for Low level API</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetSlim3Controller.java" target="_blank">Controller for Slim3</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetJDOController.java" target="_blank">Controller for JDO</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/service/PerformanceService.java" target="_blank">Service</li>
</ul>
</body>
</html>
