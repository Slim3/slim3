<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="html" uri="http://struts.apache.org/tags-html"%>  
<%@taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>  
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@taglib prefix="s" uri="http://www.slim3.org/tags"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<title>Nested Foreach</title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>

<h1>Nested Foreach</h1>

<table border="1">
<c:forEach var="mapItems" items="${mapItemsItems}">
    <tr>
    <c:forEach var="m" items="${mapItems}">
        <td>${f:h(m.id)}</td>
        <td>${f:h(m.name)}</td>
    </c:forEach>
    </tr>
</c:forEach>
</table>

</body>
</html>