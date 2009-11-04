<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Locale Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<fmt:setLocale value="${f:locale()}"/>
<a href="${f:url('change?locale=en')}"><fmt:message key="en"/></a>
<a href="${f:url('change?locale=ja')}"><fmt:message key="ja"/></a><br />
Current locale: ${f:locale()}
</body>
</html>
