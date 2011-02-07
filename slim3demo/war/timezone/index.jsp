<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Timezone Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/timezone/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/timezone/ChangeController.java">ChangeController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/timezone/index.jsp">index.jsp</a></li>
</ul>
<hr />

<fmt:setTimeZone value="${f:timeZone()}"/>
<a href="${f:url('change?timeZone=UTC')}">UTC</a>
<a href="${f:url('change?timeZone=PST')}">PST</a>
<a href="${f:url('change?timeZone=JST')}">JST</a><br />
Current time zone: ${timeZone}<br />
Now: <fmt:formatDate value="${now}" type="both" dateStyle="full" timeStyle="full" />
</body>
</html>
