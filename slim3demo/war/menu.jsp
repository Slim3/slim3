<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<div id="menu">
<ul>
<li><a href="${f:url('/blog/')}">CRUD</a></li>
<li><a href="${f:url('/checkbox/')}">Checkbox</a></li>
<li><a href="${f:url('/multibox/')}">Multibox</a></li>
<li><a href="${f:url('/radio/')}">Radio</a></li>
<li><a href="${f:url('/select/')}">Select</a></li>
<li><a href="${f:url('/multiselect/')}">Multiselect</a></li>
<li><a href="${f:url('/upload/')}">Upload</a></li>
<li><a href="${f:url('/blobstore/')}">Blobstore</a></li>
<li><a href="${f:url('/locale/')}">Locale</a></li>
<li><a href="${f:url('/timezone/')}">TimeZone</a></li>
<li><a href="${f:url('/ajax/')}">HTML with Ajax</a></li>
<li><a href="${f:url('/ktrwjr/index.html')}">Server side test</a></li>
</ul>
</div>