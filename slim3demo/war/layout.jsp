<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${param.title}</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<c:import url="/header.jsp"/>
<c:import url="/menu.jsp" />
<div id="main">
${param.content}
</div>
<c:import url="/footer.jsp" />
</body>
</html>