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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Welcome to Slim3</title>

<link rel="stylesheet" type="text/css" href="css/global.css" />
</head>
<body>
<h1>Welcome to Slim3</h1>

<ul>
<li><s:link href="/add/">Add</s:link></li>
<li><s:link href="/foreach/">Foreach</s:link></li>
<li><s:link href="/foreachButton/">Foreach with button</s:link></li>
<li><s:link href="/nestedForeach/">Nested foreach</s:link></li>
<li><s:link href="/updatableForeach/">Updatable foreach</s:link></li>
<li><s:link href="/updatableNestedForeach/">Updatable nested foreach</s:link></li>
<li><s:link href="/validator/">Validation with annotation</s:link></li>
<li><s:link href="/jsValidator/">Validation with javascript</s:link></li>
<li><s:link href="/select/">Select</s:link></li>
<li><s:link href="/multiselect/">Multiple select</s:link></li>
<li><s:link href="/checkbox/">Checkbox</s:link></li>
<li><s:link href="/multibox/">Multiple checkbox</s:link></li>
<li><s:link href="/radio/">Radio button</s:link></li>
<li><s:link href="/textarea/">Textarea</s:link></li>
<li><s:link href="/condition/?id=1">Condition(id=1)</s:link></li>
<li><s:link href="/token/">Transaction token</s:link></li>
<li><s:link href="/upload/">File upload</s:link></li>
<li><s:link href="/download/">File download</s:link></li>
<li><s:link href="/tiles/">Layout</s:link></li>
<li><s:link href="/ajax/">Ajax</s:link></li>
</ul>
</body>
</html>