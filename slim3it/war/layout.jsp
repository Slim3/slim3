<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${param.title}</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<table class="layout" width="100%">
  <tr><td class="header" colspan="2"><jsp:include page="header.jsp" /></td></tr>
  <tr>
    <td class="menu" width="20%"><jsp:include page="menu.jsp" /></td>
    <td class="content" style="vertical-align:top;text-align:left">${param.content}</td>
  </tr>
  <tr><td class="footer" colspan="2"><jsp:include page="footer.jsp" /></td></tr>
</table>
</body>
</html>