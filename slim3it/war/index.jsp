<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<h1>Index</h1>
<fmt:message key="aaa" /><br />
${c.now}<br />
<fmt:formatDate value="${c.now}" type="both" timeStyle="full"/>
</body>
</html>