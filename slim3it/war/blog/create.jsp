<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Blog(Slim3)</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<jsp:include page="/header.jsp">
    <jsp:param name="title" value="Blog"/>
</jsp:include>
<jsp:include page="/menu.jsp" />
<div id="body">

<form action="${f:url('insert')}" method="post">
Title<br />
<input type="text" ${f:property("title")} class="${f:errorClass('title', 'error')}"/>${f:h(errors.title)}<br />
Content<br />
<textarea name="content" class="${f:errorClass('content', 'error')}">${f:h(content)}</textarea>${f:h(errors.content)}<br />
<input type="submit" value="Insert"/>
</form>

</div>
<jsp:include page="/footer.jsp" />
</body>
</html>