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
<title>Condition: ${f:h(id)}</title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>

<h1>Condition: ${f:h(id)}</h1>

<html:errors/>
"id" is ${f:h(id)}.<br />
<c:if test="${id != null}">
"id" is not null.
</c:if>
<c:if test="${id == null}">
"id" is null.
</c:if>
<br />
<c:choose>
<c:when test="${id == '1'}">
"id" is one.
</c:when>
<c:when test="${id == '2'}">
"id" is two.
</c:when>
<c:otherwise>
"id" is other.
</c:otherwise>
</c:choose>
</body>
</html>