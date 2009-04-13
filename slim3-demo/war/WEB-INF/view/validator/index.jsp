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
<title>Validator</title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>
<h1>Validator</h1>


<html:errors/>
<s:form>
<table>
<tr>
<td>Byte:</td><td><html:text property="byteText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Short:</td><td><html:text property="shortText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Integer:</td><td><html:text property="integerText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Long:</td><td><html:text property="longText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Float:</td><td><html:text property="floatText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Double:</td><td><html:text property="doubleText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Date:</td><td><html:text property="dateText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Credit card(9999999999999999):</td><td><html:text property="creditcardText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>E-mail address:</td><td><html:text property="emailText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>URL:</td><td><html:text property="urlText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Integer(3～10):</td><td><html:text property="intRangeText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Long(3～10):</td><td><html:text property="longRangeText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Float(3.0～10.0):</td><td><html:text property="floatRangeText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Double(3.0～10.0):</td><td><html:text property="doubleRangeText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Minimum length of characters is 3:</td><td><html:text property="minlengthText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Maximum length of characters is 10:</td><td><html:text property="maxlengthText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>Number(999):</td><td><html:text property="numberText" errorStyleClass="err" /></td>
</tr>
<tr>
<td>validwhen1Text:</td><td><html:text property="validwhen1Text" errorStyleClass="err" /></td>
</tr>
<tr>
<td>validwhen2Text<br />
(Mandatory if validwhen1Text exists):</td><td><html:text property="validwhen2Text" errorStyleClass="err" /></td>
</tr>
</table>
<input type="submit" name="submit" value="submit"/>
</s:form>
</body>
</html>