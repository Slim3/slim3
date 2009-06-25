<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<blogList>
<c:forEach var="e" items="${blogList}">
    <blog key="${e.key}" version="${e.version}" title="${f:h(e.title)}" content="${f:h(e.content)}"/>
</c:forEach>
</blogList>