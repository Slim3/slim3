<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Hello</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form action="${f:url('sayHello')}" method="post">
Input your name:<input type="text" name="name"/>
<input type="submit" value="Say Hello"/>
</form>
</body>
</html>