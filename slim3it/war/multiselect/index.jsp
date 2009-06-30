<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>multiselect Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<form method="post" action="${f:url('')}">
<select name="aaaArray" multiple="multiple" size="3">
<option ${f:multiselect("aaaArray", "1")}>One</option>
<option ${f:multiselect("aaaArray", "2")}>Two</option>
<option ${f:multiselect("aaaArray", "3")}>Three</option>
</select><br />
<input type="submit" value="Submit"/>
</form>
</body>
</html>
