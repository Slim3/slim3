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
<s:javascript method="submit"/>
<s:javascript method="submit2" staticJavascript="false"/>
<title>Javascript Validator</title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>

<h1>Javascript Validator</h1>

<html:errors/>
<s:form>
<table>
<tr><td>aaa</td><td><html:text property="aaa"/></td></tr>
<tr><td>bbb</td><td><html:text property="bbb"/></td></tr>
</table>
<s:submit property="submit" jsValidate="true">aaa is required</s:submit>
<s:submit property="submit2" jsValidate="true">bbb is required</s:submit>
</s:form>
</body>
</html>