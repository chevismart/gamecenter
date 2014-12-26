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
    <script type="text/javascript" src="../../js/third-party/jquery-2.1.1.min.js"></script>
    <script type="text/javascript">
        function topup() {
            var order = '{"appId":"1224905202","nonceStr":"e1916abc-5b3a-4252-8ebe-82f5c26c67e3","package":"prepay_id=wx201412270209502820be3ee90042735293","paySign":"99B9ECB4C9EF64500463858F3A8B0E7F","signType":"MD5","timeStamp":"1419617372"}';
//            alert(order);
            WeixinJSBridge.invoke('getBrandWCPayRequest', order, function (res) {
                WeixinJSBridge.log(res.err_msg);
                alert(res.err_code + res.err_desc + res.err_msg);
            });
        }
    </script>
</head>
<body>

<input name="topupAmount" id="topupAmount"/>
<a href="javascript:void(0)" onclick="topup()">click me</a>

<div>
    Hihihihi: <s:text name="tempJsonStr" id="orderInfo"/>
</div>
<%--<s:form>--%>
<%--<s:text name="topupAmount" id="topupAmount"/>--%>
<%--<s:a href="javascript:void(0);" onclick="topup();"/>--%>
<%--</s:form>--%>
</body>
</html>
