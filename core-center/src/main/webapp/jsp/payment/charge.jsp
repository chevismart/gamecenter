<%--
  Created by IntelliJ IDEA.
  User: Chevis
  Date: 2014/12/25
  Time: 22:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>充值</title>
    <script type="text/javascript">
        function topup() {
            var amount = $("#topupAmount").value();
            alert(amount);
        }
    </script>
</head>
<body>
<s:form>
    <s:text name="topupAmount" id="topupAmount"/>
    <s:a href="javascript:void(0);" onclick="topup();"/>
</s:form>
</body>
</html>
