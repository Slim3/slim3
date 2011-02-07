<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>multibox Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/multibox/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/multibox/index.jsp">index.jsp</a></li>
</ul>
<hr />

<form method="post" action="${f:url('')}">
aaaArray:<br />
<input type="checkbox" ${f:multibox("aaaArray", "111")}/>111<br />
<input type="checkbox" ${f:multibox("aaaArray", "222")}/>222<br />
<input type="submit" value="Submit"/>
</form>
</body>
</html>
