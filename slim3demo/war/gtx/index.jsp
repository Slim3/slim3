<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Performance comparison between local transactions and global transactions</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/gtx/IndexController.java">IndexController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/gtx/PutController.java">PutController</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/war/gtx/index.jsp">index.jsp</a></li>
</ul>
<hr />
Local Transaction test code:<br />
<pre>
long start = System.currentTimeMillis();
for (int i = 0; i < count; i++) {
    Transaction tx = Datastore.beginTransaction();
    Datastore.put(tx, new Entity("Hoge"));
    tx.commit();
}
long time = System.currentTimeMillis() - start;
</pre>
Global Transaction test code:<br />
<pre>
start = System.currentTimeMillis();
GlobalTransaction gtx = Datastore.beginGlobalTransaction();
for (int i = 0; i < count; i++) {
    gtx.put(new Entity("Hoge"));
}
gtx.commit();
time = System.currentTimeMillis() - start;
</pre>
<table border="1">
<thead>
<tr><th>Entity Groups</th><th>Local Transaction(millis)</th><th>Global Transaction(millis)</th><th>Test</th></tr>
</thead>
<tbody>
<c:forEach var="v" varStatus="s" items="${gtxResultList}">
<tr><td>${s.count}</td><td>${v.tx}</td><td>${v.gtx}</td><td><form method="post" action="put?count=${s.count}"><input type="submit" value="Execute"/></form></td></tr>
</c:forEach>
</tbody>
</table>

</body>
</html>
