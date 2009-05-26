<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<ul>
<li><a href="${f:h('/blog/')}">Blog</a></li>
<li><a href="${f:h('/flexblog/')}">Blog with Flex</a></li>
</ul>