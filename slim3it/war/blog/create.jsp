<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Blog</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form action="${f:url('insert')}" method="post">
Title<br />
<input type="text" name="title"/><br />
Content<br />
<textarea name="content"></textarea><br />
<input type="submit" value="Insert"/>
</form>
</body>
</html>
