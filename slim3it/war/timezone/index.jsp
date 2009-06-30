<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Timezone Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<fmt:setTimeZone value="${f:timeZone()}"/>
<a href="${f:url('change?timeZone=UTC')}">UTC</a>
<a href="${f:url('change?timeZone=PST')}">PST</a>
<a href="${f:url('change?timeZone=JST')}">JST</a><br />
Current time zone: ${f:timeZone()}<br />
Now: <fmt:formatDate value="${now}" type="both" dateStyle="full" timeStyle="full" />
</body>
</html>
