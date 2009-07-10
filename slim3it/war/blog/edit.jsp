<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Blog(Slim3)</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form action="${f:url('update')}" method="post">
<input type="hidden" ${f:hidden("key")}/>
<input type="hidden" ${f:hidden("version")}/>
Title<br />
<input type="text" ${f:text("title")} class="${f:errorClass('title', 'error')}"/>${f:h(errors.title)}<br />
Content<br />
<textarea name="content" class="${f:errorClass('content', 'error')}">${f:h(content)}</textarea>${f:h(errors.content)}<br />
<input type="submit" value="Update"/>
</form>
</body>
</html>