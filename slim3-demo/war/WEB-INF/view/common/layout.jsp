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
<title><tiles:getAsString name="title" /></title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>

<table class="layout" width="100%">
  <tr><td class="header" colspan="2"><tiles:insert page="header.jsp" /></td></tr>
  <tr>
    <td class="menu" width="20%"><tiles:insert page="menu.jsp" /></td>
    <td class="content" ><tiles:insert attribute="content" /></td>
  </tr>
  <tr><td class="footer" colspan="2"><tiles:insert page="footer.jsp" /></td></tr>
</table>
</body>
</html>