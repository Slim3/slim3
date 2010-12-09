<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>checkbox Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/checkbox/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/checkbox/index.jsp">index.jsp</a></li>
</ul>
<hr />

<form method="post" action="${f:url('')}">
aaa:${f:h(aaa)}<br />
<input type="checkbox" ${f:checkbox("aaa")}/><br />
<input type="submit" value="Submit"/>
</form>
</body>
</html>
