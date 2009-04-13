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
<title>Updatable Foreach</title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>

<h1>Updatable Foreach</h1>
<html:errors/>
<s:form>
<table border="1">
<c:forEach var="m" varStatus="s" items="${mapItems}">
<tr>
<td><input type="text" name="mapItems[${s.index}].id" value="${f:h(m.id)}"/></td>
<td><input type="text" name="mapItems[${s.index}].name" value="${f:h(m.name)}"/></td>
</tr>
</c:forEach>
</table><br />
<input type="submit" name="submit" value="submit"/>
</s:form>
</body>
</html>