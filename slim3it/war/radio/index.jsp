<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>radio Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form method="post" action="${f:url('')}">
<input type="radio" ${f:radio("aaa", "111")}/>111<br />
<input type="radio" ${f:radio("aaa", "222")}/>222<br />
<input type="radio" ${f:radio("aaa", "333")}/>333<br />
<input type="submit" value="Submit"/>
</form>
</body>
</html>
