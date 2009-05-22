<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<h1>Index</h1>
<ul>
<li><a href="${f:h('/blog/')}">Blog</a></li>
<li><a href="${f:h('/flexblog/')}">Blog with Flex</a></li>
</ul>
</body>
</html>