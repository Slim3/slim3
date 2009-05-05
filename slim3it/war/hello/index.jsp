<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Hello</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form action="sayHello" method="post">
Input your name:<input type="text" name="name"/>
<input type="submit" value="Say Hello"/>
</form>
</body>
</html>