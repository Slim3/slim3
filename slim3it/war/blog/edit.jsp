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
<form action="${f:url('update')}" method="post">
<input type="hidden" name="id" value="${f:h(id)}"/>
Title<br />
<input type="text" name="title" value="${f:h(title)}"/><br />
Content<br />
<textarea name="content">${f:h(content)}</textarea><br />
<input type="submit" value="Update"/>
</form>
</body>
</html>
